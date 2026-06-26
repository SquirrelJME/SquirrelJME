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
#include "frontend/libjvm/internals.h"
#include "sjme/alloc.h"
#include "sjme/nvm/boot.h"
#include "sjme/debug.h"
#include "sjme/nvm/task.h"

/** Default amount of memory. */
#define SJME_JVM_INIT_MEMORY (64 * 1048576)

/** The global NVM state for the JNI wrapper. */
static sjme_atomic(sjme_nvm) sjme_jni_nvm_state;

jint JNICALL JNI_CreateJavaVM(
	sjme_attrOutNotNull JavaVM** pvm,
	sjme_attrOutNotNull void** penv,
	sjme_attrInNotNull void* args)
{
	sjme_errorCode error;
	struct JNIInvokeInterface_* resultVm;
	struct JNINativeInterface_* resultEnv;
	sjme_alloc_pool pool;
	sjme_nvm nvmState;
	JavaVMInitArgs* initArgs;
	sjme_nvm_task initTask;
	sjme_nvm_bootParam bootParam;
	sjme_jint argc, i, o;
	sjme_lpcstr* argv;
	sjme_nvm_task_taskNewConfig taskConfig;
	sjme_list(sjme_nvm_rom_library)* classPath;

	if (pvm == NULL || penv == NULL || args == NULL)
		return JNI_EINVAL;

	/* Aliased. */
	initArgs = args;

	/* Negative number of options?. */
	argc = initArgs->nOptions;
	if (argc < 0)
		return JNI_EINVAL;

	/* Either too old or too new. */
	if (initArgs->version < JNI_VERSION_1_1 ||
		initArgs->version > JNI_VERSION_1_8)
		return JNI_EVERSION;

	/* Setup target argv container. */
	argv = sjme_alloca(sizeof(*argv) * (argc + 2));
	if (argv == NULL)
		return JNI_ENOMEM;
	memset(argv, 0, sizeof(*argv) * (argc + 2));

	/* Recover process name. */
	argv[0] = "squirreljme";

	/* Decompose arguments. */
	/* OpenJDK sends these: */
	/* -Djava.class.path=. */
	/* -Dsun.java.launcher=SUN_STANDARD */
	/* -Dsun.java.launcher.pid=30954 */
	for (i = 0, o = 1; i < argc; i++, o++)
		argv[o] = initArgs->options[i].optionString;

	/* Check to see if an existing state exists to create a new task under. */
	nvmState = sjme_atomic_g(sjme_nvm, &sjme_jni_nvm_state);
	if (nvmState != NULL)
	{
		/* Use the pre-existing pool. */
		pool = nvmState->allocPool;
	}

	/* Creating a fresh VM */
	else
	{
		/* Allocate the memory needed for SquirrelJME. */
		pool = NULL;
		if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
			SJME_JVM_INIT_MEMORY)) || pool == NULL)
			goto fail_noMemory;
	}

	/* Setup boot parameters. */
	memset(&bootParam, 0, sizeof(bootParam));
	bootParam.nal = &sjme_nal_default;

	/* Allow launcher fallback by default, it is possible that command line */
	/* parsing will set this to false if -jar or a main class is specified. */
	bootParam.launcherFallback = SJME_JNI_TRUE;

	/* Parse the command line. */
	if (sjme_error_is(error = sjme_nvm_parseCommandLine(pool,
		&sjme_nal_default, &bootParam, argc + 1, argv)))
	{
		if (error == SJME_ERROR_EXIT)
			return JNI_OK;
		goto fail_nvmParseArgs;
	}

	/* Always belay main since we are doing this in compatibility with */
	/* the JVM library. */
	bootParam.belay = SJME_NVM_BOOT_BELAY_MAIN;

	/* Allocate resultant function structure. */
	resultVm = NULL;
	if (sjme_error_is(error = sjme_alloc(pool, sizeof(*resultVm),
			(void**)&resultVm)) ||
		resultVm == NULL)
		goto fail_allocResultVm;

	/* Allocate environment based functions. */
	resultEnv = NULL;
	if (sjme_error_is(error = sjme_alloc(pool, sizeof(*resultEnv),
			(void**)&resultEnv)) ||
		resultEnv == NULL)
		goto fail_allocResultEnv;

	/* Creating a fresh virtual machine? */
	if (nvmState == NULL)
	{
		/* Boot the virtual machine. */
		initTask = NULL;
		if (sjme_error_is(error = sjme_nvm_boot(pool,
			&bootParam, &nvmState, &initTask)) ||
			nvmState == NULL || initTask == NULL)
			goto fail_nvmBoot;

		/* Store global state. */
		sjme_atomic_cs(sjme_nvm, &sjme_jni_nvm_state,
			NULL, nvmState);
	}

	/* Adding a task to an existing one. */
	else
	{
		/* The boot parameters need to be translated to tasks. */
		memset(&taskConfig, 0, sizeof(taskConfig));
		
		/* Search the classpath for the libraries specified. */
		classPath = NULL;
		if (sjme_error_is(error = sjme_nvm_rom_resolveClassPathByName(
			nvmState->suite,
			bootParam.mainClassPathByName, &classPath)))
			goto fail_searchClasspath;
		
		/* Setup class loader for the task. */
		taskConfig.classLoader = NULL;
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderNew(nvmState,
			&taskConfig.classLoader, classPath)) ||
			taskConfig.classLoader == NULL)
			goto fail_initClassLoader;

		/* General. */
		taskConfig.noOptimize = bootParam.noOptimize;

		/* Belay from. */
		taskConfig.belay = bootParam.belay;

		/* Arguments. */
		taskConfig.mainClass = bootParam.mainClass;
		taskConfig.mainArgs = bootParam.mainArgs;
		taskConfig.sysProps = bootParam.sysProps;

		/* Terminal configuration. */
		taskConfig.stdOut = SJME_NVM_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
		taskConfig.stdErr = SJME_NVM_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
		
		/* Start the task. */
		initTask = NULL;
		if (sjme_error_is(error = sjme_nvm_task_taskNew(nvmState,
			&taskConfig, &initTask)) || initTask == NULL)
			goto fail_initTask;
	}
	
	/* Store the environment and VM state into both structures the same. */
	/* Note that the first pointer always points to self so that double */
	/* dereference still works without going sane! */
	SJME_JNI_JVM_JVM(resultVm) = resultVm;
	SJME_JNI_JVM_ENV(resultVm) = resultEnv;
	SJME_JNI_JVM_TASK(resultVm) = initTask;
	SJME_JNI_ENV_JVM(resultEnv) = resultVm;
	SJME_JNI_ENV_ENV(resultEnv) = resultEnv;
	SJME_JNI_ENV_TASK(resultEnv) = initTask;

	/* Then link back to both. */
	nvmState->common.frontEnd.wrapper = resultVm;
	nvmState->common.frontEnd.data = resultEnv;

	/* Success! */
	*pvm = (JavaVM*)resultVm;
	*penv = resultEnv;
	return JNI_OK;

fail_nvmBoot:
fail_initTask:
fail_initClassLoader:
fail_searchClasspath:
fail_allocResultEnv:
	if (resultEnv != NULL)
		sjme_alloc_free(resultEnv);
fail_allocResultVm:
	if (resultVm != NULL)
		sjme_alloc_free(resultVm);
fail_nvmParseArgs:
fail_noMemory:

	/* Notice. */
	sjme_messageB("SquirrelJME Error: %d", error);

	if (error == SJME_ERROR_OUT_OF_MEMORY)
		return JNI_ENOMEM;
	return JNI_ERR;
}

jint JNICALL JNI_GetCreatedJavaVMs(JavaVM** vmBuf, jsize bufLen, jsize* nVMs)
{
	sjme_todo("Impl?");
}

/**
 * Obtains the default virtual machine configuration.
 * 
 * @param args A @c JavaVMInitArgs , the @c version field must be set before
 * this is called.
 * @return Either @c JNI_OK or an error such as if the Java version is not
 * supported.
 * @since 2024/03/18
 */
jint JNICALL JNI_GetDefaultJavaVMInitArgs(
	sjme_attrInOutNotNull void* args)
{
	JavaVMInitArgs* initArgs;

	if (args == NULL)
		return JNI_EINVAL;

	/* This is aliased under void. */
	initArgs = args;

	/* Either too old or too new. */
	if (initArgs->version < JNI_VERSION_1_1 ||
		initArgs->version > JNI_VERSION_1_8)
		return JNI_EVERSION;

	/* Indicate that we support this version. */
	initArgs->version = JNI_VERSION_1_8;

	/* Clear these. */
	initArgs->nOptions = 0;
	initArgs->options = NULL;

	/* Success! */
	return JNI_OK;
}
