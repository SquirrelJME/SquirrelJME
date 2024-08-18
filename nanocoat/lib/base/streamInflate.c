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

/** The size of the input/output buffer. */
#define SJME_INFLATE_IO_BUFFER_SIZE 2048

/** The window size. */
#define SJME_INFLATE_WINDOW_SIZE 16384

/**
 * The inflation step.
 * 
 * @since 2024/08/17
 */
typedef enum sjme_stream_inflateStep
{
	/** Parse BTYPE and determine how to continue. */
	SJME_INFLATE_STEP_CHECK_BTYPE,
	
	/** Literal uncompressed data header. */
	SJME_INFLATE_STEP_LITERAL_HEADER,
	
	/** Literal uncompressed data. */
	SJME_INFLATE_STEP_LITERAL_DATA,
	
	/** Load in dynamic huffman table. */
	SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD,
	
	/** Process data through the dynamic huffman table. */
	SJME_INFLATE_STEP_DYNAMIC_TABLE_INFLATE,
	
	/** Fixed static huffman table. */
	SJME_INFLATE_STEP_FIXED_TABLE_INFLATE,
	
	/** Finished, nothing is left. */
	SJME_INFLATE_STEP_FINISHED,
} sjme_stream_inflateStep;

/**
 * Inflation buffer state.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_stream_inflateBuffer
{
	/** The amount of data that is ready for processing. */
	sjme_jint ready;
	
	/** The buffer storage. */
	sjme_jubyte buffer[SJME_INFLATE_IO_BUFFER_SIZE];
	
	/** Was EOF hit in this buffer? */
	sjme_jboolean hitEof;
} sjme_stream_inflateBuffer;

/**
 * The window for output inflated data.
 * 
 * @since 2024/08/18
 */
typedef struct sjme_stream_inflateWindow
{
	/** The number of bytes in the window. */
	sjme_jint windowLen;
	
	/** The window buffer. */
	sjme_jubyte window[SJME_INFLATE_WINDOW_SIZE];
} sjme_stream_inflateWindow;

/**
 * Inflation state.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_stream_inflateState
{
	/** The current step in inflation. */
	sjme_stream_inflateStep step;
	
	/** Was the final block hit? */
	sjme_jboolean finalHit;
	
	/** The output window. */
	sjme_stream_inflateWindow window;
	
	/** The amount of literal bytes left to read. */
	sjme_jint literalLeft;
	
	/** The input buffer. */
	sjme_stream_inflateBuffer input;
	
	/** The output buffer. */
	sjme_stream_inflateBuffer output;
} sjme_stream_inflateState;

/**
 * Inflate stream initialization.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_stream_inflateInit
{
	/** The compressed data stream. */
	sjme_stream_input handle;
	
	/** Decompression state. */
	sjme_stream_inflateState* handleTwo;
} sjme_stream_inflateInit;

static sjme_errorCode sjme_stream_bitNeed(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrInRange(1, 32) sjme_jint bitCount)
{
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitCount <= 0 || bitCount > 32)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* SJME_ERROR_TOO_SHORT */
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_bitIn(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrInRange(1, 32) sjme_jint bitCount,
	sjme_attrOutNotNull sjme_jint* readValue)
{
	sjme_errorCode error;
	
	if (buffer == NULL || readValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitCount <= 0 || bitCount > 32)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Can we actually read this much in? */
	if (sjme_error_is(error = sjme_stream_bitNeed(buffer, bitCount)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_bitOut(
	sjme_attrInNotNull sjme_stream_inflateBuffer* buffer,
	sjme_attrOutNotNull sjme_stream_inflateWindow* window,
	sjme_attrInRange(1, 32) sjme_jint bitCount,
	sjme_attrOutNotNull sjme_jint writeValue)
{
	if (buffer == NULL || window == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bitCount <= 0 || bitCount > 32)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

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
	sjme_stream_inflateInit* init;
	
	init = data;
	if (stream == NULL || inImplState == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set data. */
	inImplState->handle = init->handle;
	inImplState->handleTwo = init->handleTwo;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_decodeBType(
	sjme_attrInNotNull sjme_stream_input source,
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_errorCode error;
	
	if (source == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can we actually read in the final flag and block type? */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_stream_bitNeed(
		inBuffer, 3)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_decodeLiteralHeader(
	sjme_attrInNotNull sjme_stream_input source,
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_errorCode error;
	
	if (source == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Can we actually read in the literal data header? */
	inBuffer = &state->input;
	if (sjme_error_is(error = sjme_stream_bitNeed(
		inBuffer, 32)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_decodeLiteralData(
	sjme_attrInNotNull sjme_stream_input source,
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_stream_inflateBuffer* outBuffer;
	sjme_errorCode error;
	
	if (source == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_decodeDynLoad(
	sjme_attrInNotNull sjme_stream_input source,
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_errorCode error;
	
	if (source == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_decodeDynInflate(
	sjme_attrInNotNull sjme_stream_input source,
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_stream_inflateBuffer* outBuffer;
	sjme_errorCode error;
	
	if (source == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_decodeFixInflate(
	sjme_attrInNotNull sjme_stream_input source,
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	sjme_stream_inflateBuffer* inBuffer;
	sjme_stream_inflateBuffer* outBuffer;
	sjme_errorCode error;
	
	if (source == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_stream_decode(
	sjme_attrInNotNull sjme_stream_input source,
	sjme_attrInNotNull sjme_stream_inflateState* state)
{
	if (source == NULL || state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Which step are we on? */
	switch (state->step)
	{
			/* Parse the block type. */
		case SJME_INFLATE_STEP_CHECK_BTYPE:
			return sjme_stream_decodeBType(source, state);
			
			/** Literal uncompressed header. */
		case SJME_INFLATE_STEP_LITERAL_HEADER:
			return sjme_stream_decodeLiteralHeader(source, state);
			
			/** Literal uncompressed data. */
		case SJME_INFLATE_STEP_LITERAL_DATA:
			return sjme_stream_decodeLiteralData(source, state);
		
			/** Load in dynamic huffman table. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_LOAD:
			return sjme_stream_decodeDynLoad(source, state);
		
			/** Process data through the dynamic huffman table. */
		case SJME_INFLATE_STEP_DYNAMIC_TABLE_INFLATE:
			return sjme_stream_decodeDynInflate(source, state);
		
			/** Fixed static huffman table. */
		case SJME_INFLATE_STEP_FIXED_TABLE_INFLATE:
			return sjme_stream_decodeFixInflate(source, state);
	}
	
	/* Should not be reached. */
	return SJME_ERROR_ILLEGAL_STATE;
}

static sjme_errorCode sjme_stream_inputInflateFlushOut(
	sjme_attrInNotNull sjme_stream_inflateState* state,
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
	sjme_stream_inflateState* state;
	sjme_jint remainder, sourceRead, lastRemainder;
	
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
	
	/* Fill the input buffer as much as possible before we decompress as it */
	/* is more efficient to operate in larger chunks. */
	/* Naturally we stop when there is no input anyway. */
	while (!state->input.hitEof)
	{
		/* Can we even read in more data? */
		remainder = SJME_INFLATE_IO_BUFFER_SIZE - state->input.ready;
		if (remainder <= 0)
			break;
		
		/* Read in data. */
		sourceRead = -2;
		if (sjme_error_is(error = sjme_stream_inputRead(source,
			&sourceRead,
			&state->input.buffer[state->input.ready],
			remainder)) || sourceRead < -1)
			return sjme_error_default(error);
		
		/* If no data was read in, it might not be ready. */
		if (sourceRead == 0)
			break;
		
		/* Otherwise if EOF was hit, indicate as such. */
		else if (sourceRead == -1)
			state->input.hitEof = SJME_JNI_TRUE;
	}
	
	/* Try to decompress as much data as possible into the output buffer. */
	lastRemainder = -1;
	while (!state->output.hitEof)
	{
		/* How much room is left in the output? */
		remainder = SJME_INFLATE_IO_BUFFER_SIZE - state->output.ready;
		
		/* If this did not change, we probably need more input or the */
		/* output buffer does not have enough space. */
		if (remainder == lastRemainder)
			break;
		
		/* Used to determine if we should run the loop again, and if we */
		/* get stuck in a zero-read/write loop. */
		lastRemainder = remainder;
		
		/* Perform inflation. */
		if (sjme_error_is(error = sjme_stream_decode(
			source, state)))
			return sjme_error_default(error);
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
	sjme_stream_inflateInit init;
	
	if (inPool == NULL || outStream == NULL || inCompressed == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set initialization data. */
	memset(&init, 0, sizeof(init));
	init.handle = inCompressed;
	
	/* Setup decompression state data. */
	if (sjme_error_is(error = sjme_alloc(inPool,
		sizeof(sjme_stream_inflateState),
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
