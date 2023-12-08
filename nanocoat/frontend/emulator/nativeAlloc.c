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

/** The Java allocation pool class. */
#define SJME_CLASS_ALLOC_POOL cc_squirreljme_vm_nanocoat_AllocPool

/**
 * Allocates a pool via @c malloc() and returns the pointer to it.
 *
 * @param size The size of the pool.
 * @return The native pointer to the pool.
 * @throws VMException If the pool could not be allocated or initialized.
 * @since 2023/12/08
 */
jlong JNI_METHOD(SJME_CLASS_ALLOC_POOL, _1_1poolMalloc)
	(JNIEnv* env, jclass classy, jint size)
{
	sjme_alloc_pool* result;

	/* Attempt pool allocation. */
	result = NULL;
	if (!sjme_alloc_poolMalloc(&result, size) ||
		result == NULL)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_CODE_UNKNOWN);
		return 0L;
	}

	return SJME_POINTER_TO_JLONG(result);
}

