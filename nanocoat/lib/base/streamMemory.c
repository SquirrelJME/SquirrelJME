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
	memmove(dest, (sjme_pointer)(((uintptr_t)inImplState->handle) +
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
	.read = sjme_stream_inputMemoryRead,
};

static sjme_errorCode sjme_stream_outputMemoryClose(
	sjme_attrInNotNull sjme_stream_output outStream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNullable sjme_pointer* optResult)
{
	if (outStream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing to be done here. */
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
	memmove((sjme_pointer)((uintptr_t)inImplState->handle +
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
	sjme_stream_input result;
	sjme_stream_cacheMemory* cache;
	sjme_errorCode error;

	if (inPool == NULL || outStream == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0 || (((uintptr_t)base) + length) < ((uintptr_t)base))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool,
		SJME_SIZEOF_INPUT_STREAM(sjme_stream_cacheMemory),
		&result)) || result == NULL)
		return sjme_error_default(error);

	/* Set base information. */
	result->functions = &sjme_stream_inputMemoryFunctions;

	/* Get the cache. */
	cache = SJME_INPUT_MEMORY_UNCOMMON(result);

	/* Set initial state information. */
	cache->base = base;
	cache->length = length;

	/* Return result. */
	*outStream = result;
	return SJME_ERROR_NONE;
}


sjme_errorCode sjme_stream_outputOpenMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInNotNull sjme_pointer base,
	sjme_attrInPositive sjme_jint length)
{
	sjme_stream_output result;
	uintptr_t realBase;
	sjme_errorCode error;

	if (inPool == NULL || outStream == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	realBase = (uintptr_t)base;
	if (length < 0 || (realBase + length) < realBase)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool,
		SJME_SIZEOF_OUTPUT_STREAM(sjme_stream_cacheMemory),
		&result)) || result == NULL)
		return sjme_error_default(error);

	/* Set base information. */
	result->functions = &sjme_stream_outputMemoryFunctions;

	/* Get the cache. */
	cache = SJME_OUTPUT_MEMORY_UNCOMMON(result);

	/* Set initial state information. */
	cache->base = base;
	cache->length = length;

	/* Return result. */
	*outStream = result;
	return SJME_ERROR_NONE;
}
