/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "frontend/libjvm/commonJniJvm.h"
#include "sjme/stdTypes.h"
#include "frontend/libjvm/internals.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/loop.h"
#include "sjme/nvm/task.h"

/**
 * Main program entry point.
 * 
 * @param argc Argument count. 
 * @param argv Arguments passed.
 * @return Returns @c EXIT_SUCCESS on success, otherwise another exit code.
 * @since 2025/07/14
 */
int main(int argc, sjme_lpcstr* argv)
{
#define BUF_SIZE 128
	sjme_errorCode error;
	JavaVM* jvm;
	JNIEnv* env;
	JavaVMOption* vmOpt;
	JavaVMInitArgs initArgs;
	sjme_lpcstr* mainArgV;
	sjme_jint i, o, n, vmArgC, mainArgC;
	jclass mainClass, stringClass;
	jmethodID mainMethod;
	sjme_jboolean isSquirrelJME;
	sjme_nvm_task inTask;
	sjme_cchar mainBuf[BUF_SIZE];
	jarray invokeArgs;
	jstring invokeArg;
	sjme_jint exitCode;

	/* Set successful exit code. */
	exitCode = EXIT_SUCCESS;

	/* Allocate space needed for VM options. */
	vmOpt = sjme_alloca(sizeof(*vmOpt) * (argc + 1));
	if (vmOpt == NULL)
		return EXIT_FAILURE;
	memset(vmOpt, 0, sizeof(*vmOpt) * (argc + 1));
	
	/* Convert argc/argv into VM options. */
	/* Note that the option string is non-const, so duplicate the arguments. */
	vmArgC = 0;
	for (i = 1; i < argc; i++)
	{
		/* Stop processing when dash arguments stop, as this is the main */
		/* class and its arguments. */
		if (argv[i][0] != '-')
			break;

		/* Forward argument. */
		vmOpt[vmArgC++].optionString = strdup(argv[i]);
	}

	/* Determine main class and remaining arguments, if applicable. */
	mainArgV = (i < argc ? &argv[i] : NULL);
	mainArgC = (i < argc ? argc - i : 0);

	/* Setup initial arguments. */
	memset(&initArgs, 0, sizeof(initArgs));
	initArgs.version = JNI_VERSION_1_8;
	initArgs.options = vmOpt;
	initArgs.nOptions = vmArgC;

	/* Create JVM. */
	jvm = NULL;
	env = NULL;
	if (JNI_CreateJavaVM(&jvm, (void**)&env, &initArgs) != JNI_OK)
		return EXIT_FAILURE;
	
	/* This will occur if say -version is passed. */
	if (jvm == NULL || env == NULL)
	{
		/* If the version string was found, succeed instead. */
		for (i = 1; i < argc; i++)
		{
			if (argv[i] == NULL)
				continue;

			/* Stop if the start is not a dash, since we do not want */
			/* to parse things passed to main as arguments. */
			if (argv[i][0] != '-')
				break;

			/* Okay options? */
			if (!strcmp("-version", argv[i]) ||
				!strcmp("--version", argv[i]) ||
				!strcmp("-?", argv[i]) ||
				!strcmp("-help", argv[i]) ||
				!strcmp("--help", argv[i]))
				return EXIT_SUCCESS;
		}
		
		/* Did not find the version string, so fail. */
		return JNI_ERR;
	}

	/* Is this SquirrelJME? */
	isSquirrelJME = (SJME_JNI_JVM_TASK(*jvm) ==
		SJME_JNI_JVM_TASK(*env)) && SJME_JNI_JVM_TASK(*env) != NULL &&
		sjme_nvm_isAR(SJME_JNI_JVM_TASK(*env), SJME_NVM_STRUCT_TASK);

	/* Default booting? */
	if (mainArgC <= 0 || mainArgV == NULL)
	{
		/* If not SquirrelJME, then we cannot default boot. */
		if (!isSquirrelJME)
			return JNI_ERR;

		/* Recover the task. */
		inTask = SJME_JNI_ENV_TASK(*env);

		/* Enter the main thread for the task. */
		if (sjme_error_is(error = sjme_nvm_task_taskEnterMain(inTask, NULL)))
			goto fail_enterMain;
		
		/* Run main loop. */
		if (sjme_error_is(error = sjme_nvm_loop_main(inTask->inState,
			&exitCode)))
			goto fail_mainLoop;
	}

	/* Perform normal main launch. */
	else
	{
		/* Map main to slashes. */
		memset(mainBuf, 0, sizeof(mainBuf));
		snprintf(mainBuf, BUF_SIZE - 1, "%s",
			mainArgV[0]);
		for (i = 0, n = sjme_util_sizeToInt(strlen(mainBuf)); i < n; i++)
			if (mainBuf[i] == '.')
				mainBuf[i] = '/';
		
		/* Locate the main class and the entry method. */
		mainClass = (*env)->FindClass(env, mainBuf);
		if (mainClass == NULL)
			goto fail_findMainClass;

		/* Locate the string class. */
		stringClass = (*env)->FindClass(env, "java/lang/String");
		if (stringClass == NULL)
			goto fail_findStringClass;

		/* Locate the main method. */
		mainMethod = (*env)->GetStaticMethodID(env, mainClass, "main",
			"([Ljava/lang/String;)V");
		if (mainMethod == NULL)
			goto fail_findMainMethod;

		/* Setup array to put string arguments in. */
		invokeArgs = (*env)->NewObjectArray(env, mainArgC,
			stringClass, NULL);
		if (invokeArgs == NULL)
			goto fail_initArgsArray;

		/* Fill in string arguments. */
		for (i = 1, o = 0; i < mainArgC; i++, o++)
		{
			/* Setup string. */
			invokeArg = (*env)->NewStringUTF(env, mainArgV[i]);
			if (invokeArg == NULL)
				goto fail_initArg;
			
			/* Set argument here. */
			(*env)->SetObjectArrayElement(env, invokeArgs, o,
				invokeArg);
		}

		/* Invoke it. */
		(*env)->CallStaticVoidMethod(env, mainClass, mainMethod, invokeArgs);
	}

	/* Done! */
	if ((*jvm)->DestroyJavaVM(jvm) != JNI_OK)
		return EXIT_FAILURE;

	/* Success? */
	return exitCode;

fail_mainLoop:
fail_enterMain:
	sjme_message("SquirrelJME VM Error: %d", error);

fail_initArg:
fail_initArgsArray:
fail_findMainMethod:
fail_findMainClass:
fail_findStringClass:
	/* Cleanup VM before exiting. */
	(*jvm)->DestroyJavaVM(jvm);

	/* Failed. */
	return EXIT_FAILURE;
#undef BUF_SIZE
}
