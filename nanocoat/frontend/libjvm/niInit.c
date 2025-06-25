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

#include "frontend/libjvm/internals.h"
#include "sjme/alloc.h"
#include "sjme/nvm/boot.h"
#include "sjme/debug.h"

/** Default amount of memory. */
#define SJME_JVM_INIT_MEMORY 67108864

/**
 * Creates a new Java Virtual Machine.
 * 
 * @param pvm The resultant virtual machine.
 * @param penv The output environment.
 * @param args The arguments to the virtual machine creation.
 * @return If successful, @c JNI_OK .
 * @since 2025/06/25
 */
jint JNICALL JNI_CreateJavaVM(
	sjme_attrOutNotNull JavaVM** pvm,
	sjme_attrOutNotNull void** penv,
	sjme_attrInNotNull void* args)
{
	struct JNIInvokeInterface_* resultVm;
	struct JNINativeInterface_* resultEnv;
	sjme_alloc_pool pool;
	sjme_nvm nvmState;
	JavaVMInitArgs* initArgs;
	jint i;

	if (pvm == NULL || penv == NULL || args == NULL)
		return JNI_EINVAL;

	/* Aliased. */
	initArgs = args;

	/* Negative number of options?. */
	if (initArgs->nOptions < 0)
		return JNI_EINVAL;

	/* Either too old or too new. */
	if (initArgs->version < JNI_VERSION_1_1 ||
		initArgs->version > JNI_VERSION_1_8)
		return JNI_EVERSION;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	/* OpenJDK sends these: */
	/* -Djava.class.path=. */
	/* -Dsun.java.launcher=SUN_STANDARD */
	/* -Dsun.java.launcher.pid=30954 */
	for (i = 0; i < initArgs->nOptions; i++)
		sjme_message("Arg %d: %s", i, initArgs->options[i].optionString);
#endif

	/* Allocate the memory needed for SquirrelJME. */
	pool = NULL;
	if (sjme_error_is(sjme_alloc_poolInitMalloc(&pool,
		SJME_JVM_INIT_MEMORY)) || pool == NULL)
		return JNI_ENOMEM;

	/* Allocate resultant function structure. */
	resultVm = NULL;
	if (sjme_error_is(sjme_alloc(pool, sizeof(*resultVm),
			(void**)&resultVm)) ||
		resultVm == NULL)
		goto fail_allocResultVm;

	/* Allocate environment based functions. */
	resultEnv = NULL;
	if (sjme_error_is(sjme_alloc(pool, sizeof(*resultEnv),
			(void**)&resultEnv)) ||
		resultEnv == NULL)
		goto fail_allocResultEnv;

	/* Boot the virtual machine. */
	nvmState = NULL;
	if (sjme_error_is(sjme_nvm_boot(pool,
		NULL, &nvmState)) || nvmState == NULL)
		goto fail_nvmBoot;

	/* Store the environment and VM state into both structures the same. */
	SJME_RESERVED_JVM(resultVm) = resultVm;
	SJME_RESERVED_ENV(resultVm) = resultEnv;
	SJME_RESERVED_NVM(resultVm) = nvmState;
	SJME_RESERVED_JVM(resultEnv) = resultVm;
	SJME_RESERVED_ENV(resultEnv) = resultEnv;
	SJME_RESERVED_NVM(resultEnv) = nvmState;

	/* Then link back to both. */
	nvmState->common.frontEnd.wrapper = resultVm;
	nvmState->common.frontEnd.data = resultEnv;

	/* Success! */
	**pvm = resultVm;
	*penv = resultEnv;
	return JNI_OK;

fail_nvmBoot:
fail_allocResultEnv:
	if (resultEnv != NULL)
		sjme_alloc_free(resultEnv);
fail_allocResultVm:
	if (resultVm != NULL)
		sjme_alloc_free(resultVm);

	return JNI_ERR;
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
