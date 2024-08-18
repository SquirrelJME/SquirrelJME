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
#include "3rdparty/miniz/miniz.h"

/** The buffer length for input data. */
#define SJME_DEFLATE_BUFFER_FULL_LEN 4096

/** The offset to the input buffer. */
#define SJME_DEFLATE_BUFFER_IN_OFFSET 0

/** The length of the input buffer. */
#define SJME_DEFLATE_BUFFER_IN_LEN (SJME_DEFLATE_BUFFER_FULL_LEN >> 1)

/** The offset to the output buffer. */
#define SJME_DEFLATE_BUFFER_OUT_OFFSET SJME_DEFLATE_BUFFER_IN_LEN

/** The length of the output buffer. */
#define SJME_DEFLATE_BUFFER_OUT_LEN SJME_DEFLATE_BUFFER_IN_LEN

/**
 * Deflate stream initialization.
 * 
 * @since 2024/08/17
 */
typedef struct sjme_stream_deflateInit
{
	/** The compressed data stream. */
	sjme_stream_input handle;
	
	/** Decompression state. */
	tinfl_decompressor* handleTwo;
	
	/** The input/output buffer. */
	sjme_buffer buffer;
} sjme_stream_deflateInit;

static sjme_errorCode sjme_stream_inputDeflateClose(
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
	
	/* Free the buffer. */
	if (inImplState->buffer != NULL)
	{
		/* Free. */
		if (sjme_error_is(error = sjme_alloc_free(
			inImplState->buffer)))
			return sjme_error_default(error);
		
		/* Clear. */
		inImplState->buffer = NULL;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputDeflateInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_deflateInit* init;
	
	init = data;
	if (stream == NULL || inImplState == NULL || init == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set data. */
	inImplState->handle = init->handle;
	inImplState->handleTwo = init->handleTwo;
	inImplState->buffer = init->buffer;
	
	/* Initialize the inflater. */
	tinfl_init(init->handleTwo);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputDeflateRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	sjme_errorCode error;
	sjme_stream_input uncompressed;
	tinfl_decompressor* state;
	sjme_buffer inBuffer, outBuffer;
	sjme_jint readLimit, writeLimit, inRead, readLeft;
	size_t inSize, outSize, i;
	tinfl_status status;
	
	if (stream == NULL || inImplState == NULL || readCount == NULL ||
		dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Fail if closed. */
	uncompressed = inImplState->handle;
	state = inImplState->handleTwo;
	inBuffer = inImplState->buffer;
	outBuffer = SJME_POINTER_OFFSET(inBuffer, SJME_DEFLATE_BUFFER_OUT_OFFSET);
	if (uncompressed == NULL || state == NULL || inBuffer == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Can we read in more data? */
	if (inImplState->length == 0 &&
		(!inImplState->hitEof ||
		(inImplState->hitEof && inImplState->offset > 0)))
	{
		/* Determine how much data we can read in. */
		readLimit = SJME_DEFLATE_BUFFER_IN_LEN - inImplState->offset;
		
		/* Read in from the base stream. */
		inRead = -2;
		if (sjme_error_is(error = sjme_stream_inputRead(uncompressed,
			&inRead,
			SJME_POINTER_OFFSET(inBuffer, inImplState->offset),
			readLimit)) || inRead < -1)
			return sjme_error_default(error);
		
		/* Pure EOF? */
		if (inRead < 0 && inImplState->offset == 0)
		{
			*readCount = -1;
			return SJME_ERROR_NONE;
		}
		
		/* Shift up count. */
		inImplState->offset += inRead;
		
#if 0 && defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		for (i = 0; i < readLimit; i++)
			printf("%c ", ((sjme_jubyte*)inBuffer)[i]);
		printf("\n");
		for (i = 0; i < readLimit; i++)
			printf("%02x ", ((sjme_jubyte*)inBuffer)[i]);
		printf("\n");
#endif
		
		/* Clear output. */
		memset(outBuffer, 0, SJME_DEFLATE_BUFFER_OUT_LEN);
		
		/* Perform decompression logic. */
		inSize = inImplState->offset;
		outSize = SJME_DEFLATE_BUFFER_OUT_LEN / 2;
		status = tinfl_decompress(state,
			inBuffer,
			&inSize,
			outBuffer,
			outBuffer,
			&outSize,
			TINFL_FLAG_HAS_MORE_INPUT);
		
		/* Store index for wrap around. */
		inImplState->index = outSize;
		
		/* Debug. */
		sjme_message("status: %d, inSize %d -> %d; outSize %d -> %d W@%d",
			status,
			inImplState->offset, (sjme_jint)inSize,
			SJME_DEFLATE_BUFFER_OUT_LEN, (sjme_jint)outSize,
			inImplState->length);
		sjme_message_hexDump(outBuffer, outSize);
		
		/* Failed? */
		if (status == TINFL_STATUS_FAILED || status == TINFL_STATUS_BAD_PARAM)
			return SJME_ERROR_IO_EXCEPTION;
		
		/* Was EOF hit? */
		else if (status == TINFL_STATUS_DONE)
			inImplState->hitEof = SJME_JNI_TRUE;
		
		/* How much data was left in the read? */
		readLeft = inImplState->offset - inSize;
		
		/* Debug. */
		sjme_message("readLeft = %d - %d = %d",
			inImplState->offset, (sjme_jint)inSize, readLeft);
		
		/* Move the input data down, if we still have input data. */
		if (readLeft > 0)
			memmove(inBuffer,
				SJME_POINTER_OFFSET(inBuffer, inSize),
				readLeft);
		
		/* Set whatever amount is still in the input buffer. */
		inImplState->offset = readLeft;
		
		/* Output buffer gets whatever data was output. */
		inImplState->length = outSize;
	}
	
	/* There is output data waiting to be read in? */
	if (inImplState->length > 0)
	{
		/* How much can we grab? */
		writeLimit = (length < inImplState->length ? length :
			inImplState->length);
		
		/* Debug. */
		sjme_message("Draining %d of %d (off %d)...",
			writeLimit, inImplState->length,
			inImplState->offset);
		
		/* Copy data over. */
		memmove(dest, outBuffer, writeLimit);
		
		/* Move buffer data down and trim it. */
		memmove(outBuffer,
			SJME_POINTER_OFFSET(outBuffer, writeLimit),
			inImplState->length - writeLimit);
		inImplState->length -= writeLimit;
		
		/* Success! */
		*readCount = writeLimit;
		return SJME_ERROR_NONE;
	}
	
	/* Is there absolutely nothing left and we hit EOF? */
	if (inImplState->hitEof &&
		inImplState->length == 0 &&
		inImplState->offset == 0)
		*readCount = -1;
	
	/* Otherwise no-op. */
	else
		*readCount = 0;
	
	sjme_message("RC %d; EOF %d; LEN %d; OFF %d",
		*readCount,
		inImplState->hitEof,
		inImplState->length,
		inImplState->offset);
	
	return SJME_ERROR_NONE;
}

/** Input deflate functions. */
static const sjme_stream_inputFunctions sjme_stream_inputDeflateFunctions =
{
	.close = sjme_stream_inputDeflateClose,
	.init = sjme_stream_inputDeflateInit,
	.read = sjme_stream_inputDeflateRead,
};

sjme_errorCode sjme_stream_inputOpenDeflate(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_stream_input inCompressed)
{
	sjme_errorCode error;
	sjme_stream_input result;
	sjme_stream_deflateInit init;
	
	if (inPool == NULL || outStream == NULL || inCompressed == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set initialization data. */
	memset(&init, 0, sizeof(init));
	init.handle = inCompressed;
	
	/* Setup compression state data. */
	if (sjme_error_is(error = sjme_alloc(inPool,
		sizeof(tinfl_decompressor), (sjme_pointer*)&init.handleTwo)) ||
		init.handleTwo == NULL)
		goto fail_allocState;
	
	/* Allocate data buffer. */
	if (sjme_error_is(error = sjme_alloc(inPool,
		SJME_DEFLATE_BUFFER_FULL_LEN, (sjme_pointer*)&init.buffer)) ||
		init.buffer == NULL)
		goto fail_allocBuffer;
	
	/* Setup sub-stream. */
	result = NULL;
	if (sjme_error_is(error = sjme_stream_inputOpen(inPool,
		&result, &sjme_stream_inputDeflateFunctions,
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
fail_allocBuffer:
	if (init.buffer != NULL)
		sjme_alloc_free(init.buffer);
	
fail_allocState:
	if (init.handleTwo != NULL)
		sjme_alloc_free(init.handleTwo);
	
	return sjme_error_default(error);
}
