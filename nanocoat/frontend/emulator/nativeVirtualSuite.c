/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>
#include <string.h>

#include "frontend/emulator/common.h"
#include "frontend/emulator/jniHelper.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/nvm/rom.h"

static sjme_errorCode sjme_jni_virtualSuite_init(
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNullable sjme_pointer data)
{
	if (inSuite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing needs to be done here... */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_virtualSuite_libraryId(
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jint* outId)
{
	sjme_todo("Implement this?");
	return 0;
}

static sjme_errorCode sjme_jni_virtualSuite_list(
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrOutNotNull sjme_list_sjme_nvm_rom_library** outLibraries)
{
	JNIEnv* env;
	jobject virtualSuite;
	jmethodID javaListMethod;
	jclass classy;
	sjme_list_sjme_nvm_rom_library* result;

	if (inSuite == NULL || outLibraries == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get instance that we need to call into. */
	env = inSuite->cache.common.frontEnd.data;
	virtualSuite = inSuite->cache.common.frontEnd.wrapper;
	classy = (*env)->GetObjectClass(env, virtualSuite);

	/* Execute method accordingly. */
	javaListMethod = (*env)->GetMethodID(env, classy, "__list", "()J");
	result = SJME_JLONG_TO_POINTER(sjme_list_sjme_nvm_rom_library*,
		(*env)->CallLongMethod(env, virtualSuite, javaListMethod));
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_JNI_EXCEPTION;

	/* Return result of it. */
	*outLibraries = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_jni_virtualSuite_loadLibrary()
{
	sjme_todo("Implement this?");
	return 0;
}

/** Functions for JNI accessed suites. */
static const sjme_nvm_rom_suiteFunctions sjme_jni_virtualSuite_functions =
{
	.init = sjme_jni_virtualSuite_init,
	.libraryId = NULL,
	.list = sjme_jni_virtualSuite_list,
	.loadLibrary = NULL,
};

jlong SJME_JNI_METHOD(SJME_CLASS_VIRTUAL_SUITE, _1_1init)
	(JNIEnv* env, jclass classy, jlong poolPtr, jobject self)
{
	sjme_frontEnd frontEnd;
	sjme_errorCode error;
	sjme_nvm_rom_suite result;

	if (poolPtr == 0 || self == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Seed front end data for initialization. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	frontEnd.data = env;
	frontEnd.wrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, self));

	/* Initialize new suite. */
	if (sjme_error_is(error = sjme_nvm_rom_suiteNew(
		SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr),
		&result, NULL,
		&sjme_jni_virtualSuite_functions,
		&frontEnd)) ||
		result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Use the given result. */
	return SJME_POINTER_TO_JLONG(result);
}
