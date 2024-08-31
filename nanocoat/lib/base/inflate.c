/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/inflate.h"

static sjme_errorCode sjme_inflate_bitFill(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_jint avail, readCount;
	sjme_jubyte* buffer;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Pointless if the input hit EOF. */
	if (inState->inputEof)
		return SJME_ERROR_NONE;
		
	/* How much space is left in the input buffer? */
	avail = INT32_MAX;
	if (sjme_error_is(error = sjme_circleBuffer_available(
		inState->inputBuffer, &avail)) ||
		avail == INT32_MAX)
		return sjme_error_default(error);
	
	/* It is as full as it can get. */
	if (avail <= 0)
		return SJME_ERROR_NONE;
	
	/* Setup target buffer to read into. */
	buffer = sjme_alloca(avail);
	if (buffer == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(buffer, 0, avail);
	
	/* Read everything in as much as possible. */
	readCount = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadFully(
		inState->source, &readCount, buffer, avail)) ||
		readCount == INT32_MAX)
		return sjme_error_default(error);
	
	/* EOF? */
	if (readCount < 0)
	{
		inState->inputEof = SJME_JNI_TRUE;
		return SJME_ERROR_NONE;
	}
	
	/* Push onto the buffer. */
	if (sjme_error_is(error = sjme_circleBuffer_push(
		inState->inputBuffer,
		buffer, readCount,
		SJME_CIRCLE_BUFFER_TAIL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_bitRead(
	sjme_attrInNotNull sjme_bitStream_input inStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrOutNotNull sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_inflate* inState;
	sjme_jint limit;
	
	inState = functionData;
	if (inStream == NULL || inState == NULL || readCount == NULL ||
		outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Always fill in before read. */
	if (sjme_error_is(error = sjme_inflate_bitFill(inState)))
		return sjme_error_default(error);
	
	/* How much is in the input buffer? */
	limit = INT32_MAX;
	if (sjme_error_is(error = sjme_circleBuffer_stored(
		inState->inputBuffer, &limit)) ||
		limit == INT32_MAX)
		return sjme_error_default(error);
	
	/* If this is zero then we do not have enough to read from. */
	if (limit == 0)
	{
		/* However if EOF was hit, we stop. */
		if (inState->inputEof)
		{
			*readCount = -1;
			return SJME_ERROR_NONE;
		}
		
		/* Otherwise, we need more data! */
		return SJME_ERROR_TOO_SHORT;
	}
	
	/* Do not exceed the storage limit. */
	if (limit > length)
		limit = length;
	
	/* Pop from the buffer. */
	if (sjme_error_is(error = sjme_circleBuffer_pop(
		inState->inputBuffer, outBuf, limit,
		SJME_CIRCLE_BUFFER_HEAD)))
		return sjme_error_default(error);
	
	/* Success! */
	*readCount = limit;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_bitWrite(
	sjme_attrInNotNull sjme_bitStream_output outStream,
	sjme_attrInNullable sjme_pointer functionData,
	sjme_attrInNotNullBuf(length) sjme_buffer writeBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_inflate* inState;
	
	inState = functionData;
	if (outStream == NULL || inState == NULL || writeBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Push onto the window. */
	if (sjme_error_is(error = sjme_circleBuffer_push(
		inState->window, writeBuf, length,
		SJME_CIRCLE_BUFFER_TAIL)))
		return sjme_error_default(error);
	
	/* And onto the output buffer. */
	if (sjme_error_is(error = sjme_circleBuffer_push(
		inState->outputBuffer, writeBuf, length,
		SJME_CIRCLE_BUFFER_TAIL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_bufferSaturation(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrInPositiveNonZero sjme_jint bitsNeeded)
{
	sjme_errorCode error;
	sjme_jint ready;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitsNeeded <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
			
	/* The output buffer is saturated? */
	if (inState->outputBuffer->ready >= SJME_INFLATE_IO_BUFFER_SATURATED)
		return SJME_ERROR_BUFFER_SATURATED;
	
	/* How many bits are already in the window? */
	ready = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_bitsReady(
		SJME_AS_BITSTREAM(inState->input),
		&ready)) || ready == INT32_MAX)
		return sjme_error_default(error);
	
	/* Add in all the input buffer bytes. */
	ready += (inState->inputBuffer->ready * 8);
	
	/* Do we have enough? */
	if (ready >= bitsNeeded)
		return SJME_ERROR_NONE;
	return SJME_ERROR_TOO_SHORT;
}

static sjme_errorCode sjme_inflate_drain(
	sjme_attrInNotNull sjme_inflate* inState,
	sjme_attrInOutNotNull sjme_jint* drainOff,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_jint stored, limit;
	
	if (inState == NULL || drainOff == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* How much is in the output buffer? */
	stored = INT32_MAX;
	if (sjme_error_is(error = sjme_circleBuffer_stored(
		inState->outputBuffer, &stored)) ||
		stored == INT32_MAX)
		return sjme_error_default(error);
	
	/* How much can be drained? */
	limit = length - (*drainOff);
	if (limit > stored)
		limit = stored;
		
	/* Nothing to do? */
	if (limit == 0)
		return SJME_ERROR_NONE;
	
	/* Pop off the head. */
	if (sjme_error_is(error = sjme_circleBuffer_pop(
		inState->outputBuffer,
		SJME_POINTER_OFFSET(outBuf, (*drainOff)),
		limit, SJME_CIRCLE_BUFFER_HEAD)))
		return sjme_error_default(error);
	
	/* Move up the drain offset, which is also the cumulative total output. */
	(*drainOff) += limit;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_inflate_stepBType(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_juint isFinal, blockType;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If final was hit, then we are done. */
	if (inState->finalHit)
	{
		inState->step = SJME_INFLATE_STEP_FINISHED;
		return SJME_ERROR_NONE;
	}
		
	/* Can we actually read the block? */
	if (sjme_error_is(error = sjme_inflate_bufferSaturation(
		inState, 3)))
		return sjme_error_default(error);
	
	/* Read in final block flag. */
	isFinal = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input,
		SJME_BITSTREAM_LSB, &isFinal, 1)) ||
		isFinal == INT32_MAX)
		return sjme_error_default(error);
	
	/* Read in block type. */
	blockType = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input,
		SJME_BITSTREAM_LSB, &blockType, 2)) ||
		blockType == INT32_MAX)
		return sjme_error_default(error);
	
	/* Invalid. */
	if (blockType == 3)
		return SJME_ERROR_INFLATE_INVALID_BTYPE;
	
	/* Was the final block hit? */
	if (isFinal)
		inState->finalHit = SJME_JNI_TRUE;
	
	/* Which step to transition to? */
	if (blockType == 0)
		inState->step = SJME_INFLATE_STEP_LITERAL_SETUP;
	else if (blockType == 1)
		inState->step = SJME_INFLATE_STEP_FIXED_SETUP;
	else
		inState->step = SJME_INFLATE_STEP_DYNAMIC_SETUP;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_inflate_stepLiteralData(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_jint left;
	sjme_juint raw;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Copy in what we can, provided the buffers are not saturated */
	/* and we have input data. */
	left = inState->sub.literalLeft;
	while (left > 0)
	{
		/* Can we actually read a byte? */
		if (sjme_error_is(error = sjme_inflate_bufferSaturation(
			inState, 8)))
			goto fail_saturated;
		
		/* Read in byte. */
		raw = INT32_MAX;
		if (sjme_error_is(error = sjme_bitStream_inputRead(
			inState->input,
			SJME_BITSTREAM_LSB, &raw, 8)) ||
			raw == INT32_MAX)
			goto fail_read;
		
		/* Write out bits. */
		if (sjme_error_is(error = sjme_bitStream_outputWrite(
			inState->output,
			SJME_BITSTREAM_LSB, raw, 8)))
			goto fail_write;
		
		/* We consumed a byte. */
		left--;
	}
	
	/* Store for next run. */
	inState->sub.literalLeft = left;
	
	/* Did we read in all the data? Go back to the block type parse. */
	if (left == 0)
		inState->step = SJME_INFLATE_STEP_CHECK_BTYPE;
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_write:
fail_read:
fail_saturated:
	/* Store for next run. */
	inState->sub.literalLeft = left;
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_inflate_stepLiteralSetup(
	sjme_attrInNotNull sjme_inflate* inState)
{
	sjme_errorCode error;
	sjme_juint len, nel;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Literal uncompressed data starts at the byte boundary. */
	if (sjme_error_is(error = sjme_bitStream_inputAlign(
		inState->input,
		8, NULL)))
		return sjme_error_default(error);
		
	/* Can we actually read the lengths? */
	if (sjme_error_is(error = sjme_inflate_bufferSaturation(
		inState, 32)))
		return sjme_error_default(error);
	
	/* Read both the length and its complement. */
	len = INT32_MAX;
	nel = INT32_MAX;
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input, SJME_BITSTREAM_LSB,
		&len, 16)))
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_bitStream_inputRead(
		inState->input, SJME_BITSTREAM_LSB,
		&nel, 16)))
		return sjme_error_default(error);
	
	/* Both of these should be the complement to each other. */
	if (len != (nel ^ 0xFFFF))
		return SJME_ERROR_INFLATE_INVALID_INVERT;
	
	/* Setup for next step. */
	memset(&inState->sub, 0, sizeof(inState->sub));
	inState->sub.literalLeft = len;
	inState->step = SJME_INFLATE_STEP_LITERAL_DATA;
	
	/* Success! */
	return SJME_ERROR_NONE;
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
	
	/* If this previously failed, then recover it. */
	if (sjme_error_is(inState->failed))
		return sjme_error_default(inState->failed);
	
	/* Constant inflation loop. */
	drainOff = 0;
	for (;;)
	{
		/* Can anything be drained? */
		if (inState->outputBuffer->ready > 0)
			if (sjme_error_is(error = sjme_inflate_drain(inState, &drainOff,
				outBuf, length)))
			{
				inState->failed = error;
				return sjme_error_default(error);
			}
		
		/* Finished? */
		if (inState->step == SJME_INFLATE_STEP_FINISHED)
		{
			/* There is still data that can be read. */
			if (drainOff != 0 || inState->outputBuffer->ready > 0)
				break;
			
			/* True EOF */
			*readCount = -1;
			return SJME_ERROR_NONE;
		}
		
		/* Fill in the input buffer as much as possible, this will */
		/* reduce any subsequent disk activity as it is done in bulk. */
		if (sjme_error_is(error = sjme_inflate_bitFill(inState)))
		{
			inState->failed = error;
			return sjme_error_default(error);
		}
		
		/* Check the ready state, stop if not yet ready */
		if (sjme_error_is(sjme_inflate_bufferSaturation(inState,
			1)))
		{
			if (error == SJME_ERROR_TOO_SHORT)
				continue;
			
			if (error == SJME_INFLATE_IO_BUFFER_SATURATED)
				break;
			
			inState->failed = error;
			return sjme_error_default(error);
		}
		
		/* Which step at inflation are we at? */
		error = SJME_ERROR_UNKNOWN;
		switch (inState->step)
		{
			case SJME_INFLATE_STEP_CHECK_BTYPE:
				error = sjme_inflate_stepBType(inState);
				break;
				
			case SJME_INFLATE_STEP_LITERAL_DATA:
				error = sjme_inflate_stepLiteralData(inState);
				break;
				
			case SJME_INFLATE_STEP_LITERAL_SETUP:
				error = sjme_inflate_stepLiteralSetup(inState);
				break;
				
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
			if (error == SJME_ERROR_TOO_SHORT)
				continue;
				
			/* Or we filled the output buffer as much as possible and */
			/* cannot fit anymore. */
			if (error == SJME_INFLATE_IO_BUFFER_SATURATED)
				break;
			
			/* Set as failed. */
			inState->failed = error;
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
	result->failed = SJME_ERROR_NONE;
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
