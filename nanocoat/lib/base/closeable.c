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

sjme_errorCode sjme_closeable_close(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Closeable closing %p", closeable);
#endif
	
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
			return closeable->closeHandler(closeable);
	}
	
	/* Success if already closed, or there was no close handler. */
	return SJME_ERROR_NONE;
}
