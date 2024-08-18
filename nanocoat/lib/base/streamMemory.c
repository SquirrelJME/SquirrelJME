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

/**
 * Stream initialization data.
 * 
 * @since 2024/08/12
 */
typedef struct sjme_stream_ioInit
{
	/** The base pointer to use. */
	sjme_cpointer base;
	
	/** The length of the memory area. */
	sjme_jint length;
} sjme_stream_ioInit;

static sjme_errorCode sjme_stream_inputMemoryAvailable(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* outAvail)
{
	if (stream == NULL || inImplState == NULL || outAvail == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Calculating this is trivial. */
	*outAvail = inImplState->length - stream->totalRead;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputMemoryClose(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing needs to happen here. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputMemoryInit(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_ioInit* init;
	
	if (stream == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover initializer. */
	init = data;
	
	/* Set initial state information. */
	inImplState->buffer = init->base;
	inImplState->length = init->length;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_inputMemoryRead(
	sjme_attrInNotNull sjme_stream_input stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNotNull sjme_attrOutNegativeOnePositive sjme_jint* readCount,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint length)
{
	sjme_jint limit;

	if (stream == NULL || readCount == NULL || dest == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* End of stream? */
	if (stream->totalRead >= inImplState->length)
	{
		*readCount = -1;
		return SJME_ERROR_NONE;
	}

	/* Determine how many bytes we can actually read. */
	limit = inImplState->length - stream->totalRead;
	if (length < limit)
		limit = length;

	/* Do a direct memory copy. */
	memmove(dest, (sjme_pointer)(((uintptr_t)inImplState->buffer) +
		stream->totalRead), limit);

	/* Indicate read count and consider success! */
	*readCount = limit;
	return SJME_ERROR_NONE;
}

/** Input memory functions. */
static const sjme_stream_inputFunctions sjme_stream_inputMemoryFunctions =
{
	.available = sjme_stream_inputMemoryAvailable,
	.close = sjme_stream_inputMemoryClose,
	.init = sjme_stream_inputMemoryInit,
	.read = sjme_stream_inputMemoryRead,
};

static sjme_errorCode sjme_stream_outputMemoryClose(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	if (outStream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing to be done here. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_outputMemoryInit(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_ioInit* init;
	
	if (stream == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover initializer. */
	init = data;
	
	/* Set initial state information. */
	inImplState->buffer = init->base;
	inImplState->length = init->length;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_outputMemoryWrite(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNotNull sjme_cpointer buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	uintptr_t realBuf;
	sjme_jint written;

	if (stream == NULL || inImplState == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	realBuf = (uintptr_t)buf;
	if (length < 0 || realBuf + length < realBuf)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Overflowing write? */
	written = stream->totalWritten;
	if (written < 0 || (written + length) < 0 ||
		(written + length) > inImplState->length)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Copy the data directly. */
	memmove((sjme_pointer)((uintptr_t)inImplState->buffer +
		written), buf, length);

	/* Success! */
	return SJME_ERROR_NONE;
}

static const sjme_stream_outputFunctions sjme_stream_outputMemoryFunctions =
{
	.close = sjme_stream_outputMemoryClose,
	.init = sjme_stream_outputMemoryInit,
	.write = sjme_stream_outputMemoryWrite,
};

sjme_errorCode sjme_stream_inputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_cpointer base,
	sjme_attrInPositive sjme_jint length)
{
	sjme_stream_ioInit init;
	uintptr_t realBase;

	if (inPool == NULL || outStream == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Make sure memory does not overflow. */
	realBase = (uintptr_t)base;
	if (length < 0 || (realBase + length) < realBase)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Setup initialization input. */
	memset(&init, 0, sizeof(init));
	init.base = base;
	init.length = length;
	
	/* Forward initialization. */
	return sjme_stream_inputOpen(inPool, outStream,
		&sjme_stream_inputMemoryFunctions, &init,
		NULL);
}

sjme_errorCode sjme_stream_outputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInNotNull sjme_pointer base,
	sjme_attrInPositive sjme_jint length)
{
	sjme_stream_ioInit init;
	uintptr_t realBase;

	if (inPool == NULL || outStream == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Make sure memory does not overflow. */
	realBase = (uintptr_t)base;
	if (length < 0 || (realBase + length) < realBase)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Setup initialization input. */
	memset(&init, 0, sizeof(init));
	init.base = base;
	init.length = length;
	
	/* Forward initialization. */
	return sjme_stream_outputOpen(inPool, outStream,
		&sjme_stream_outputMemoryFunctions, &init,
		NULL);
}
