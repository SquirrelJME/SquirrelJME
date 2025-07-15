/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>
#include <jvm.h>

#include "sjme/config.h"
#include "sjme/stdTypes.h"

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
	JavaVM* jvm;
	JNIEnv* env;
	JavaVMOption* vmOpt;
	JavaVMInitArgs initArgs;
	sjme_jint i, o;
	jclass mainClass;
	jmethodID mainMethod;

	/* Allocate space needed for VM options. */
	vmOpt = sjme_alloca(sizeof(*vmOpt) * (argc + 1));
	if (vmOpt == NULL)
		return EXIT_FAILURE;
	memset(vmOpt, 0, sizeof(*vmOpt) * (argc + 1));
	
	/* Convert argc/argv into VM options. */
	/* Note that the option string is non-const, so duplicate the arguments. */
	for (i = 1, o = 0; i < argc; i++, o++)
		vmOpt[o].optionString = strdup(argv[i]);

	/* Setup initial arguments. */
	memset(&initArgs, 0, sizeof(initArgs));
	initArgs.version = JNI_VERSION_1_8;
	initArgs.options = vmOpt;
	initArgs.nOptions = argc - 1;

	/* Create JVM. */
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
				!strcmp("-help", argv[i]) ||
				!strcmp("--help", argv[i]))
				return EXIT_SUCCESS;
		}
		
		/* Did not find the version string, so fail. */
		return JNI_ERR;
	}

	/* Locate the main class and the entry method. */
	mainClass = (*env)->FindClass(env,
		"cc/squirreljme/runtime/cldc/lang/LibJvmBootstrap");
	if (mainClass == NULL)
		goto fail_findMainClass;

	/* Execute it. */
	mainMethod = (*env)->GetStaticMethodID(env, mainClass, "main",
		"([Ljava/lang/String;)V");
	if (mainMethod == NULL)
		goto fail_findMainMethod;

	/* Invoke it. */
	(*env)->CallStaticVoidMethod(env, mainClass, mainMethod, NULL);

	/* Done! */
	if ((*jvm)->DestroyJavaVM(jvm) != JNI_OK)
		return EXIT_FAILURE;

	/* Success! */
	return EXIT_SUCCESS;

fail_findMainMethod:
fail_findMainClass:
	/* Cleanup VM before exiting. */
	(*jvm)->DestroyJavaVM(jvm);

	/* Failed. */
	return EXIT_FAILURE;
}
