/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/stream.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/util.h"

typedef struct sjme_stream_inputSeekableInitData
{
	/** The seekable to access. */
	sjme_seekable seekable;
	
	/** The base offset. */
	sjme_jint base;
	
	/** The length of the chunk. */
	sjme_jint length;
	
	/** Forward close? */
	sjme_jboolean forwardClose;
} sjme_stream_inputSeekableInitData;

static sjme_errorCode sjme_stream_inputSeekableAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inputSeekableClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	sjme_errorCode error;
	
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NONE;
	
	/* Only forward close if it was requested. */
	if (inImplState->forwardClose)
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inImplState->handle))))
			return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputSeekableInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_inputSeekableInitData* init;
	
	init = data;
	if (stream == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set parameters. */
	inImplState->handle = init->seekable;
	inImplState->offset = init->base;
	inImplState->index = 0;
	inImplState->length = init->length;
	inImplState->forwardClose = init->forwardClose;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputSeekableRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	sjme_seekable seekable;
	sjme_errorCode error;
	sjme_jint size, limit, left, readAt;
	
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Recover seekable. */
	seekable = inImplState->handle;
	if (seekable == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* How much length is left? */
	size = inImplState->length;
	readAt = inImplState->index;
	left = size - readAt;
	
	/* Can we quickly determine EOF? */
	if (left <= 0)
	{
		*readCount = -1;
		return SJME_ERROR_NONE;
	}
	
	/* We cannot read more than what is left. */
	limit = (length < left ? length : left);

	/* Read in data. */
	if (sjme_error_is(error = sjme_seekable_read(seekable,
		dest, inImplState->offset + readAt, limit)))
		return sjme_error_default(error);
	
	/* We read exactly that. */
	inImplState->index = readAt + limit;
	*readCount = limit;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static const sjme_stream_inputFunctions sjme_stream_inputSeekableFunctions =
{
	.available = sjme_stream_inputSeekableAvailable,
	.init = sjme_stream_inputSeekableInit,
	.close = sjme_stream_inputSeekableClose,
	.read = sjme_stream_inputSeekableRead,
};

sjme_errorCode sjme_stream_inputOpenSeekable(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length,
	sjme_attrInValue sjme_jboolean forwardClose)
{
	sjme_errorCode error;
	sjme_stream_inputSeekableInitData init;
	
	if (seekable == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (base < 0 || length < 0 || (base + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Setup initialize. */
	memset(&init, 0, sizeof(init));
	init.seekable = seekable;
	init.base = base;
	init.length = length;
	init.forwardClose = forwardClose;
	
	/* Open base stream. */
	return sjme_stream_inputOpen(seekable->inPool,
		outStream, &sjme_stream_inputSeekableFunctions,
		&init, NULL);
}
