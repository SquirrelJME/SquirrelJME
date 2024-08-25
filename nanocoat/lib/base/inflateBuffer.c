/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/inflate.h"

static sjme_errorCode sjme_stream_inflateBufferArea(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrOutNotNull sjme_jint* outRemainder,
	sjme_attrOutNotNull sjme_pointer* outBufOpPos,
	sjme_attrOutNotNull sjme_jint* outBufOpLen)
{
	sjme_jint remainder, chunkSize;
	sjme_jboolean leftSide;
	
	if (buffer == NULL || outBufOpPos == NULL || outBufOpLen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can we even read/write more data? */
	remainder = SJME_INFLATE_IO_BUFFER_SIZE - buffer->ready;
	if (remainder <= 0)
		return SJME_ERROR_TOO_SHORT;
	
	/* If nothing is ready, reset heads. */
	if (buffer->ready == 0)
	{
		buffer->readHead = 0;
		buffer->writeHead = 0;
	}
	
	/* Is the write head on the left, or the right? */
	leftSide = (buffer->writeHead <= buffer->readHead) && buffer->ready != 0;
	if (leftSide)
		chunkSize = buffer->readHead - buffer->writeHead;
	else
		chunkSize = SJME_INFLATE_IO_BUFFER_SIZE - buffer->writeHead;
	
	/* Limit to the remainder amount. */
	if (remainder < chunkSize)
		chunkSize = remainder;
	
	/* Give what was calculated. */
	*outRemainder = remainder;
	*outBufOpPos = &buffer->buffer[buffer->writeHead];
	*outBufOpLen = chunkSize;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateBufferGive(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrInPositiveNonZero sjme_jint count)
{
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (count < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Move count up and adjust write head. */
	buffer->ready += count;
	buffer->writeHead = (buffer->writeHead + count) &
		SJME_INFLATE_IO_BUFFER_MASK;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inflateBufferConsume(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrInPositiveNonZero sjme_jint count)
{
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (count < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Move count down and adjust read head. */
	buffer->ready -= count;
	buffer->readHead = (buffer->readHead + count) &
		SJME_INFLATE_IO_BUFFER_MASK;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
