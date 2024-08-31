/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/inflate.h"

static sjme_errorCode sjme_inflate_bitRead(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrOutNotNull sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	if (inStream == NULL || functionData == NULL || readCount == NULL ||
		outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_inflate_bitWrite(
	sjme_attrInNotNull sjme_bitStream_output outStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrInNotNullBuf(length) sjme_buffer writeBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	if (outStream == NULL || functionData == NULL || writeBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_inflate_drain(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrInOutNotNull sjme_jint* drainOff,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	if (inState == NULL || drainOff == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_inflate_fill(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_jint avail;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* How much space is left in the input buffer? */
	avail = INT32_MAX;
	if (sjme_error_is(error = sjme_circleBuffer_available(
		inState->inputBuffer, &avail)) ||
		avail == INT32_MAX)
		return sjme_error_default(error);
	
	/* It is as full as it can get. */
	if (avail <= 0)
		return SJME_ERROR_NONE;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

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
	sjme_errorCode error;
	sjme_jint drainOff;
	
	if (inState == NULL || readCount == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Constant inflation loop. */
	drainOff = 0;
	for (;;)
	{
		/* Can anything be drained? */
		if (sjme_error_is(error = sjme_inflate_drain(inState, &drainOff,
			outBuf, length)))
			return sjme_error_default(error);
			
		/* EOF? */
		if (drainOff == 0 && inState->step == SJME_INFLATE_STEP_FINISHED)
		{
			*readCount = -1;
			return SJME_ERROR_NONE;
		}
		
		/* Fill in the input buffer as much as possible, this will */
		/* reduce any subsequent disk activity as it is done in bulk. */
		if (sjme_error_is(error = sjme_inflate_fill(inState)))
			return sjme_error_default(error);
		
		/* The output buffer is too saturated, we do not want to overfill. */
		if (inState->outputBuffer->ready >= SJME_INFLATE_IO_BUFFER_SATURATED)
			break;
		
		/* Which step at inflation are we at? */
		error = SJME_ERROR_UNKNOWN;
		switch (inState->step)
		{
			case SJME_INFLATE_STEP_CHECK_BTYPE:
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
				
			case SJME_INFLATE_STEP_LITERAL_SETUP:
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
				
			case SJME_INFLATE_STEP_LITERAL_DATA:
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
				
			case SJME_INFLATE_STEP_DYNAMIC_SETUP:
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
				
			case SJME_INFLATE_STEP_FIXED_SETUP:
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
				
			case SJME_INFLATE_STEP_INFLATE_FROM_TREE:
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
				
			case SJME_INFLATE_STEP_FINISHED:
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
		}
		
		/* Did inflation fail? */
		if (sjme_error_is(error))
		{
			/* Not enough input data, need to fill more! */
			/* Or we filled the output buffer as much as possible and */
			/* cannot fit anymore. */
			if (error == SJME_ERROR_TOO_SHORT ||
				error == SJME_INFLATE_IO_BUFFER_SATURATED)
				break;
			
			return sjme_error_default(error);
		}
	}
	
	/* The amount written is the amount drained. */
	*readCount = drainOff;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_inflate** outState,
	sjme_attrInNotNull sjme_stream_input source)
{
	sjme_errorCode error;
	sjme_inflate* result;
	sjme_circleBuffer* inputBuffer;
	sjme_circleBuffer* outputBuffer;
	sjme_circleBuffer* window;
	sjme_bitStream_input input;
	sjme_bitStream_output output;
	
	if (inPool == NULL || outState == NULL || source == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate resultant state. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool, sizeof(*result),
		(sjme_pointer*)&result)) ||
		result == NULL)
		goto fail_allocResult;
	
	/* Allocate the input buffer. */
	inputBuffer = NULL;
	if (sjme_error_is(error = sjme_circleBuffer_new(inPool,
		&inputBuffer,
		SJME_CIRCLE_BUFFER_QUEUE,
		SJME_INFLATE_IO_BUFFER_SIZE)) || inputBuffer == NULL)
		goto fail_allocInputBuffer;
	
	/* Allocate the output buffer. */
	outputBuffer = NULL;
	if (sjme_error_is(error = sjme_circleBuffer_new(inPool,
		&outputBuffer,
		SJME_CIRCLE_BUFFER_QUEUE,
		SJME_INFLATE_IO_BUFFER_SIZE)) || outputBuffer == NULL)
		goto fail_allocOutputBuffer;
	
	/* Allocate window. */
	window = NULL;
	if (sjme_error_is(error = sjme_circleBuffer_new(inPool,
		&window,
		SJME_CIRCLE_BUFFER_WINDOW,
		SJME_INFLATE_WINDOW_SIZE)) || window == NULL)
		goto fail_allocWindow;
	
	/* Open input bit stream. */
	input = NULL;
	if (sjme_error_is(error = sjme_bitStream_inputOpen(inPool,
		&input, sjme_inflate_bitRead,
		result, NULL)) || input == NULL)
		goto fail_openInput;
	
	/* Open output bit stream. */
	output = NULL;
	if (sjme_error_is(error = sjme_bitStream_outputOpen(inPool,
		&output, sjme_inflate_bitWrite,
		result, NULL)) || output == NULL)
		goto fail_openOutput;
	
	/* Store everything in the result now. */
	result->source = source;
	result->input = input;
	result->inputBuffer = inputBuffer;
	result->output = output;
	result->outputBuffer = outputBuffer;
	result->window = window;
	
	/* Since we hold onto the source, count it up. */
	if (sjme_error_is(error = sjme_alloc_weakRef(source, NULL)))
		goto fail_countSource;
	
	/* Success! */
	*outState = result;
	return SJME_ERROR_NONE;

fail_countSource:
fail_openOutput:
	if (output != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(output));
fail_openInput:
	if (input != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(input));
fail_allocWindow:
	if (window != NULL)
		sjme_circleBuffer_destroy(window);
fail_allocOutputBuffer:
	if (outputBuffer != NULL)
		sjme_circleBuffer_destroy(outputBuffer);
fail_allocInputBuffer:
	if (inputBuffer != NULL)
		sjme_circleBuffer_destroy(inputBuffer);
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
		
	return sjme_error_default(error);
}
