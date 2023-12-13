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
#include "frontend/emulator/jniHelper.h"

/**
 * Allocates a pool via @c malloc() and returns the pointer to it.
 *
 * @param size The size of the pool.
 * @param wrapper The front end wrapper.
 * @return The native pointer to the pool.
 * @throws VMException If the pool could not be allocated or initialized.
 * @since 2023/12/08
 */
jlong SJME_JNI_METHOD(SJME_CLASS_ALLOC_POOL, _1_1poolMalloc)
	(JNIEnv* env, jclass classy, jint size, jobject wrapper)
{
	sjme_alloc_pool* result;
	sjme_errorCode error;

	/* Attempt pool allocation. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc_poolInitMalloc(&result,
		size)) || result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0L;
	}

	/* Set self reference object, we need a global reference for it. */
	result->frontEndWrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, wrapper));

	return SJME_POINTER_TO_JLONG(result);
}

