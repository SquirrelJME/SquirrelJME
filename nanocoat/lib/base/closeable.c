/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/closeable.h"

sjme_errorCode sjme_closeable_autoEnqueue(
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
	return sjme_closeable_close(closeable);
}

static sjme_errorCode sjme_closeable_closeCommon(
	sjme_attrInNotNull sjme_closeable closeable,
	sjme_attrInValue sjme_jboolean unref)
{
	sjme_errorCode error;
	
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Only close once! */
	if (sjme_atomic_sjme_jint_compareSet(&closeable->isClosed,
		0, 1))
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

sjme_errorCode sjme_closeable_close(
	sjme_attrInNotNull sjme_closeable closeable)
{
	return sjme_closeable_closeCommon(closeable, SJME_JNI_FALSE);
}

sjme_errorCode sjme_closeable_closeUnRef(
	sjme_attrInNotNull sjme_closeable closeable)
{
	return sjme_closeable_closeCommon(closeable, SJME_JNI_TRUE);
}
