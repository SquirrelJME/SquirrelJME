/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/closeable.h"

static sjme_errorCode sjme_closeable_autoEnqueue(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInPositiveNonZero sjme_jint newCount,
	sjme_attrInValue sjme_jboolean isWeakFree,
	sjme_attrInValue sjme_jboolean isBlockFree)
{
	sjme_errorCode error;
	sjme_closeable closeable;
	
	closeable = data;
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do not close if we are counting and our count is positive still. */
	/* However, we should always close if a free is called, as we are */
	/* going to lose the closeable anyway. */
	if (!isBlockFree && closeable->refCounting && newCount > 0)
		return SJME_ERROR_NONE;
	
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Closeable auto-close %p", closeable);
#endif
	
	/* Only close once! */
	if (sjme_atomic_sjme_jint_compareSet(&closeable->isClosed,
		0, 1))
	{
		/* Call the close handler. */
		if (closeable->closeHandler != NULL)
			if (sjme_error_is(error = closeable->closeHandler(closeable)))
				return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
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
	sjme_alloc_weak weak;
	
	if (inPool == NULL || handler == NULL || outCloseable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Attempt allocation. */
	result = NULL;
	weak = NULL;
#if defined(SJME_CONFIG_DEBUG)
	if (sjme_error_is(error = sjme_alloc_weakNewR(inPool,
		allocSize, sjme_closeable_autoEnqueue,
		(sjme_pointer*)&result,
		&weak, file, line, func)) ||
		result == NULL)
#else
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		allocSize, sjme_closeable_autoEnqueue,
		(sjme_pointer*)&result, &weak)) || result == NULL)
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
	sjme_errorCode error;
	sjme_alloc_weak weak;
	
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Is this a valid weak pointer type closeable? */
	weak = NULL;
	if (sjme_error_is(error = sjme_alloc_weakRefGet(closeable,
		&weak)) || weak == NULL)
	{
		/* Close non-weak based closeable. */
		if (error == SJME_ERROR_NOT_WEAK_REFERENCE)
			return sjme_closeable_autoEnqueue(NULL, closeable,
				0, SJME_JNI_FALSE,
				SJME_JNI_FALSE);
		
		/* Fail otherwise. */
		return sjme_error_default(error);
	}
	
	/* Closing counts as an unref. */
	return sjme_alloc_weakUnRef((sjme_pointer)closeable);
}
