/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "sjme/alloc.h"
#include "sjme/boot.h"
#include "sjme/debug.h"

/** Default amount of memory. */
#define SJME_JVM_INIT_MEMORY 67108864

/**
 * Creates a new Java Virtual Machine.
 * 
 * @param outVm The resultant virtual machine.
 * @param outEnv The output environment.
 * @param vmArgs The arguments to the virtual machine creation.
 * @return 
 */
sjme_attrUnused jint JNICALL JNI_CreateJavaVM(
	sjme_attrOutNotNull JavaVM** outVm,
	sjme_attrOutNotNull void** outEnv,
	sjme_attrInNotNull void* vmArgs)
{
	struct JNIInvokeInterface_* resultVm;
	struct JNINativeInterface_* resultEnv;
	sjme_alloc_pool* pool;
	sjme_nvm nvmState;
	JavaVMInitArgs* args;
	jint i;
	
	if (outVm == NULL || outEnv == NULL || vmArgs == NULL)
		return JNI_EINVAL;
		
	/* Aliased. */
	args = vmArgs;
	
	/* Negative number of options?. */
	if (args->nOptions < 0)
		return JNI_EINVAL;
		
	/* Either too old or too new. */
	if (args->version < JNI_VERSION_1_1 || args->version > JNI_VERSION_1_8)
		return JNI_EVERSION; 
	
#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	/* OpenJDK sends these: */
	/* -Djava.class.path=. */
	/* -Dsun.java.launcher=SUN_STANDARD */
	/* -Dsun.java.launcher.pid=30954 */
	for (i = 0; i < args->nOptions; i++)
		sjme_message("Arg %d: %s", i, args->options[i].optionString);
#endif
	
	/* Allocate the memory needed for SquirrelJME. */
	pool = NULL;
	if (sjme_error_is(sjme_alloc_poolInitMalloc(&pool,
		SJME_JVM_INIT_MEMORY)) || pool == NULL)
		return JNI_ENOMEM;
		
	/* Allocate resultant function structure. */
	resultVm = NULL;
	if (sjme_error_is(sjme_alloc(pool, sizeof(*resultVm), &resultVm)) ||
		resultVm == NULL)
		goto fail_allocResultVm;
	
	/* Allocate environment based functions. */
	resultEnv = NULL;
	if (sjme_error_is(sjme_alloc(pool, sizeof(*resultEnv), &resultEnv)) ||
		resultEnv == NULL)
		goto fail_allocResultEnv;
	
	/* Boot the virtual machine. */
	nvmState = NULL;
	if (sjme_error_is(sjme_nvm_boot(pool,
		NULL, NULL, &nvmState)) || nvmState == NULL)
		goto fail_nvmBoot;
	
	/* Store the environment and VM state into both structures the same. */
	resultVm->reserved0 = resultVm;
	resultVm->reserved1 = resultEnv;
	resultVm->reserved2 = nvmState;
	resultEnv->reserved0 = resultVm;
	resultEnv->reserved1 = resultEnv;
	resultEnv->reserved2 = nvmState;
	
	/* Then link back to both. */
	nvmState->frontEnd.wrapper = resultVm;
	nvmState->frontEnd.data = resultEnv;
	
	/* Success! */
	**outVm = resultVm;
	*outEnv = resultEnv;
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
 * @param vmArgs A @c JavaVMInitArgs , the @c version field must be set before
 * this is called.
 * @return Either @c JNI_OK or an error such as if the Java version is not
 * supported.
 * @since 2024/03/18
 */
sjme_attrUnused jint JNICALL JNI_GetDefaultJavaVMInitArgs(
	sjme_attrInOutNotNull void* vmArgs)
{
	JavaVMInitArgs* args;
	
	if (vmArgs == NULL)
		return JNI_EINVAL;
	
	/* This is aliased under void. */
	args = vmArgs;
	
	/* Either too old or too new. */
	if (args->version < JNI_VERSION_1_1 || args->version > JNI_VERSION_1_8)
		return JNI_EVERSION; 
	
	/* Indicate that we support this version. */
	args->version = JNI_VERSION_1_8;
	
	/* Clear these. */
	args->nOptions = 0;
	args->options = NULL;
	
	/* Success! */
	return JNI_OK;
}
