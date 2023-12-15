/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "frontend/emulator/jniHelper.h"
#include "sjme/rom.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"

static sjme_errorCode sjme_jni_virtualSuite_list()
{
	sjme_todo("Implement this?");
	return 0;
}

static sjme_errorCode sjme_jni_virtualSuite_loadLibrary()
{
	sjme_todo("Implement this?");
	return 0;
}

jlong SJME_JNI_METHOD(SJME_CLASS_VIRTUAL_SUITE, _1_1init)
	(JNIEnv* env, jclass classy, jlong poolPtr, jobject this)
{
	sjme_alloc_pool* pool;
	sjme_alloc_pool* reserved;
	sjme_rom_suiteFunctions* result;
	sjme_errorCode error;

	if (poolPtr == 0 || this == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Get the pool pointer. */
	pool = SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr);

	/* Allocate the suite data. */
	result = NULL;
	error = SJME_ERROR_UNKNOWN;
	if (SJME_IS_ERROR(error = sjme_alloc(pool, sizeof(*result),
		(void**)&result)) || result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Set function handlers. */
	result->list = sjme_jni_virtualSuite_list;
	result->loadLibrary = sjme_jni_virtualSuite_loadLibrary;

	/* Setup wrapper to reference. */
	result->frontEnd.data = env;
	result->frontEnd.wrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, this));

	/* Use the given result. */
	return SJME_POINTER_TO_JLONG(result);
}