/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/closeable.h"

static sjme_errorCode sjme_closeable_closeCommon(
	sjme_attrInNotNull sjme_closeable closeable,
	sjme_attrInValue sjme_jboolean unref,
	sjme_attrInValue sjme_jboolean forceClose)
{
	sjme_errorCode error;
	sjme_jint was;
	sjme_alloc_weak weak;
	
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If this is a reference counting closeable, then close it. */
	if (!forceClose && closeable->refCounting)
	{
		/* Get weak reference here. */
		weak = NULL;
		if (sjme_error_is(error = sjme_alloc_weakRefGet(closeable,
			&weak)) || weak == NULL)
			return sjme_error_default(error);
		
		/* Un-reference. */
		was = sjme_atomic_sjme_jint_get(&weak->count);
		if (was >= 0)
			if (sjme_error_is(error = sjme_alloc_weakUnRef(
				(void**)&closeable)))
				return sjme_error_default(error);
		
		/* Reference un-counting may have done an actual close. */
		return SJME_ERROR_NONE;
	}
	
	/* Only close once! */
	was = sjme_atomic_sjme_jint_compareSet(&closeable->isClosed,
		0, 1);
	if (was == 0)
	{
		/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
		sjme_message("Closeable closed %p", closeable);
#endif
		
		/* Forward close call. */
		if (closeable->closeHandler != NULL)
			if (sjme_error_is(error = closeable->closeHandler(closeable)))
				return sjme_error_default(error);
	}
	
	/* Un-reference? */
	if (unref)
		if (sjme_error_is(error = sjme_alloc_weakUnRef((void**)&closeable)))
			return sjme_error_default(error);
	
	/* Success if already closed, or there was no close handler. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_closeable_autoEnqueue(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree)
{
	sjme_closeable closeable;
	
	if (weak == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover closeable. */
	closeable = weak->pointer;
	if (closeable == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Closeable auto-close %p", closeable);
#endif
	
	/* Forward close. */
	return sjme_closeable_closeCommon(closeable,
		SJME_JNI_FALSE, SJME_JNI_TRUE);
}

sjme_errorCode sjme_closeable_allocR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositiveNonZero sjme_jint allocSize,
	sjme_attrInNotNull sjme_closeable_closeHandlerFunc handler,
	sjme_attrInValue sjme_jboolean refCounting,
	sjme_attrOutNotNull sjme_closeable* outCloseable
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_closeable result;
	
	if (inPool == NULL || handler == NULL || outCloseable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Attempt allocation. */
	result = NULL;
#if defined(SJME_CONFIG_DEBUG)
	if (sjme_error_is(error = sjme_alloc_weakNewR(inPool,
		allocSize, sjme_closeable_autoEnqueue,
		NULL, (sjme_pointer*)&result,
		NULL, file, line, func)) ||
		result == NULL)
#else
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		allocSize, sjme_closeable_autoEnqueue, NULL,
		(sjme_pointer*)&result, NULL)) || result == NULL)
#endif
		return sjme_error_default(error);
	
	/* Set fields. */
	result->refCounting = refCounting;
	result->closeHandler = handler;
	
	/* Success! */
	*outCloseable = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_closeable_close(
	sjme_attrInNotNull sjme_closeable closeable)
{
	return sjme_closeable_closeCommon(closeable,
		SJME_JNI_TRUE, SJME_JNI_FALSE);
}
