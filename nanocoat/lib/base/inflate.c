/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/inflate.h"

sjme_errorCode sjme_inflate_destroy(
	sjme_attrInNotNull sjme_inflate* inState)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_inflate_inflate(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrOutNotNull sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	if (inState == NULL || readCount == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_inflate_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_inflate** outState,
	sjme_attrInNotNull sjme_stream_input source)
{
	if (inPool == NULL || outState == NULL || source == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
