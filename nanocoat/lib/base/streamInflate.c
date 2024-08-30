/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdio.h>

#include "sjme/stream.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/util.h"
#include "sjme/inflate.h"

static sjme_errorCode sjme_stream_inputInflateClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	sjme_errorCode error;
	sjme_stream_input uncompressed;
	
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do nothing if already closed. */
	uncompressed = inImplState->handle;
	if (uncompressed == NULL)
		return SJME_ERROR_NONE;
	
	/* Close it. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(uncompressed))))
		return sjme_error_default(error);
	
	/* Clear it since we cannot use it anymore. */
	inImplState->handle = NULL;
	
	/* Free the decompression state. */
	if (inImplState->handleTwo != NULL)
	{
		/* Free it. */
		if (sjme_error_is(error = sjme_alloc_free(
			inImplState->handleTwo)))
			return sjme_error_default(error);
		
		/* Clear. */
		inImplState->handleTwo = NULL;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputInflateInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_inflate_init* init;
	
	init = data;
	if (stream == NULL || inImplState == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set data. */
	inImplState->handle = init->handle;
	inImplState->handleTwo = init->handleTwo;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputInflateFlushIn(
	sjme_attrInNotNull sjme_stream_input source,
	sjme_attrInNotNull sjme_inflate_state* inState)
{
	sjme_errorCode error;
	sjme_bitStream_input* inBits;
	sjme_pointer bufOpPos;
	sjme_jint bufOpLen;
	sjme_jint remainder, sourceRead;
	
	if (source == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We just use this buffer for input. */
	inBuffer = &state->input;
	
	/* Determine the read/write positions and how much we can chunk at */
	/* the same time. */
	remainder = -1;
	bufOpPos = NULL;
	bufOpLen = -1;
	if (sjme_error_is(error = sjme_inflate_bufferArea(
		inBuffer,
		&remainder,
		&bufOpPos, &bufOpLen)) ||
		remainder < 0 || bufOpPos == NULL || bufOpLen < 0)
	{
		/* No room for anything, just skip. */
		if (error == SJME_ERROR_TOO_SHORT)
			return SJME_ERROR_BUFFER_FULL;
		
		return sjme_error_default(error);
	}
	
	/* Read in data. */
	sourceRead = -2;
	if (sjme_error_is(error = sjme_stream_inputRead(source,
		&sourceRead,
		bufOpPos,
		bufOpLen)) || sourceRead < -1)
		return sjme_error_default(error);
	
	/* If EOF was hit, indicate as such. */
	if (sourceRead == -1)
		inBuffer->hitEof = SJME_JNI_TRUE;
	
	/* Otherwise, move the write head up and up the ready count. */
	else if (sourceRead > 0)
	{
		/* Count source data. */
		if (sjme_error_is(error = sjme_inflate_bufferGive(
			inBuffer,
			sourceRead)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputInflateFlushOut(
	sjme_attrInNotNull sjme_inflate_state* inState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	if (state == NULL || readCount == NULL || dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_inputInflateRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	sjme_errorCode error;
	sjme_stream_input source;
	sjme_inflate_state* inState;
	sjme_jint remainder, lastRemainder;
	
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Fail if closed. */
	source = inImplState->handle;
	state = inImplState->handleTwo;
	if (source == NULL || state == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Is there data to be written to the output? */
	if (state->output.ready > 0)
		return sjme_stream_inputInflateFlushOut(state,
			readCount, dest, length);
	
	/* If there is nothing ready to output and the output hit EOF, then */
	/* there will never be data ready. */
	if (state->output.ready <= 0 && state->output.hitEof)
	{
		*readCount = -1;
		return SJME_ERROR_NONE;
	}
	
	/* The zip data is corrupted or invalid. */
	if (state->invalidInput)
		return SJME_ERROR_IO_EXCEPTION;
	
	/* Try to decompress as much data as possible into the output buffer. */
	lastRemainder = -1;
	while (!state->output.hitEof)
	{
		/* How much room is left in the output? */
		/* We can go over the saturation limit, however we do not want */
		/* to fill past it, so we do not hit the end of the buffer. */
		remainder = SJME_INFLATE_IO_BUFFER_SATURATED - state->output.ready;
		
		/* If this did not change, we probably need more input or the */
		/* output buffer does not have enough space. */
		if (remainder == lastRemainder)
			break;
		
		/* Fill the input buffer as much as possible before we decompress */
		/* as it is more efficient to operate in larger chunks. */
		/* Naturally we stop when there is no input anyway. */
		while (!state->input.hitEof)
		{
			/* Read in. */
			if (sjme_error_is(error = sjme_stream_inputInflateFlushIn(
				source, state)))
			{
				/* If the input buffer is full, it is not an error! */
				if (error == SJME_ERROR_BUFFER_FULL)
					break;
				
				return sjme_error_default(error);
			}
		}
		
		/* Used to determine if we should run the loop again, and if we */
		/* get stuck in a zero-read/write loop. */
		lastRemainder = remainder;
		
		/* Perform inflation. */
		if (sjme_error_is(error = sjme_inflate_decode(
			state)))
		{
			/* Do not fail if there is not enough input data, just stop */
			/* trying to decompress. */
			if (error == SJME_ERROR_TOO_SHORT ||
				error == SJME_ERROR_BUFFER_SATURATED)
				break;
			return sjme_error_default(error);
		}
	}
	
	/* Try flushing to the output again? */
	if (state->output.ready > 0)
		return sjme_stream_inputInflateFlushOut(
			state, readCount, dest, length);
	
	/* If all ends hit EOF, then we are in the EOF state. */
	if (state->input.hitEof && state->output.hitEof)
		*readCount = -1;
	else
		*readCount = 0;
	return SJME_ERROR_NONE;
}

/** Input deflate functions. */
static const sjme_stream_inputFunctions sjme_stream_inputInflateFunctions =
{
	.close = sjme_stream_inputInflateClose,
	.init = sjme_stream_inputInflateInit,
	.read = sjme_stream_inputInflateRead,
};

sjme_errorCode sjme_stream_inputOpenInflate(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_stream_input inCompressed)
{
	sjme_errorCode error;
	sjme_stream_input result;
	sjme_inflate_init init;
	
	if (inPool == NULL || outStream == NULL || inCompressed == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set initialization data. */
	memset(&init, 0, sizeof(init));
	init.handle = inCompressed;
	
	/* Setup decompression state data. */
	if (sjme_error_is(error = sjme_alloc(inPool,
		sizeof(sjme_inflate_state),
		(sjme_pointer*)&init.handleTwo)) ||
		init.handleTwo == NULL)
		goto fail_allocState;
	
	/* Setup sub-stream. */
	result = NULL;
	if (sjme_error_is(error = sjme_stream_inputOpen(inPool,
		&result, &sjme_stream_inputInflateFunctions,
		&init, NULL)) || result == NULL)
		goto fail_open;
	
	/* Valid, so count up the compressed seekable. */
	if (sjme_error_is(error = sjme_alloc_weakRef(inCompressed, NULL)))
		goto fail_countStream;
	
	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;

fail_countStream:
fail_open:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	
fail_allocState:
	if (init.handleTwo != NULL)
		sjme_alloc_free(init.handleTwo);
	
	return sjme_error_default(error);
}
