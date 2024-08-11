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
 * Contains the specific state for byte array output.
 *
 * @since 2024/01/09
 */
typedef struct sjme_stream_cacheByteArray
{
	/** The byte array data. */
	sjme_jubyte* array;

	/** The current limit. */
	sjme_jint limit;

	/** Whatever value to store, passed on close. */
	sjme_pointer whatever;

	/** The function to call when the output is finished. */
	sjme_stream_outputByteArrayFinishFunc finish;
} sjme_stream_cacheByteArray;

static sjme_errorCode sjme_stream_outputByteArrayClose(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrOutNullable sjme_pointer* optResult)
{
	sjme_stream_resultByteArray result;
	sjme_errorCode error;

	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initialize result. */
	memset(&result, 0, sizeof(result));
	result.array = cache->array;
	result.length = stream->totalWritten;
	result.free = SJME_JNI_TRUE;
	result.whatever = cache->whatever;
	result.optResult = optResult;

	/* No finish function? */
	if (cache->finish == NULL)
	{
		/* Do not free if this was specified, just return it there. */
		if (optResult != NULL)
		{
			*optResult = cache->array;
			result.free = SJME_JNI_FALSE;
		}
	}

	/* Otherwise, call the finish handler. */
	else
	{
		/* Call handler, if it fails just stop. */
		if (sjme_error_is(error = cache->finish(stream, &result)))
			return sjme_error_default(error);
	}

	/* Are we freeing the array? */
	if (result.free)
		if (sjme_error_is(error = sjme_alloc_free(cache->array)))
			return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_outputByteArrayWrite(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNotNull sjme_cpointer buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
#define GROW_SIZE 32
	sjme_stream_cacheByteArray* cache;
	uintptr_t realBuf;
	sjme_jint available, desireSize;
	sjme_errorCode error;

	if (stream == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	realBuf = (uintptr_t)buf;
	if (length < 0 || (realBuf + length) < realBuf)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Recover cache. */
	cache = SJME_OUTPUT_UNCOMMON(sjme_stream_cacheByteArray, stream);

	/* Not enough bytes to fit into the array buffer? Need to grow it? */
	available = cache->limit - stream->totalWritten;
	if (length > available)
	{
		/* Resultant buffer would overflow? Way too big? */
		desireSize = cache->limit + length + GROW_SIZE;
		if (desireSize < 0)
			return SJME_ERROR_OUT_OF_MEMORY;

		/* Reallocate memory here. */
		if (sjme_error_is(error = sjme_alloc_realloc(
			&cache->array, desireSize)))
			return sjme_error_defaultOr(error,
				SJME_ERROR_OUT_OF_MEMORY);

		/* The buffer's limit has now increased. */
		cache->limit = desireSize;
	}

	/* Copy directly into the array buffer. */
	/* Note that the caller increases totalWritten. */
	memmove(&cache->array[stream->totalWritten], buf, length);

	/* Success! */
	return SJME_ERROR_NONE;
#undef GROW_SIZE
}

static const sjme_stream_outputFunctions sjme_stream_outputByteArrayFunctions =
{
	.close = sjme_stream_outputByteArrayClose,
	.write = sjme_stream_outputByteArrayWrite,
};

sjme_errorCode sjme_stream_outputOpenByteArray(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInPositive sjme_jint initialLimit,
	sjme_attrInNullable sjme_stream_outputByteArrayFinishFunc finish,
	sjme_attrInNullable sjme_pointer whatever)
{
	sjme_stream_output result;
	sjme_stream_cacheByteArray* cache;
	sjme_errorCode error;
	sjme_pointer initBuf;

	if (inPool == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Fallback to a default initial limit if zero. */
	if (initialLimit == 0)
		initialLimit = 32;

	if (initialLimit < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Try to allocate an initial buffer. */
	initBuf = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool,
		initialLimit, &initBuf)) || initBuf == NULL)
		goto fail_initBufAlloc;

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool,
		SJME_SIZEOF_OUTPUT_STREAM(sjme_stream_cacheByteArray),
		&result)) || result == NULL)
		goto fail_streamAlloc;

	/* Set functions. */
	result->functions = &sjme_stream_outputByteArrayFunctions;

	/* Get the cache. */
	cache = SJME_OUTPUT_UNCOMMON(sjme_stream_cacheByteArray, result);

	/* Setup cache. */
	cache->array = initBuf;
	cache->finish = finish;
	cache->whatever = whatever;
	cache->limit = initialLimit;

	/* Return result. */
	*outStream = result;
	return SJME_ERROR_NONE;

	/* Failure cleanup. */
fail_streamAlloc:
	if (result != NULL)
		sjme_alloc_free(result);

fail_initBufAlloc:
	if (initBuf != NULL)
		sjme_alloc_free(initBuf);

	return sjme_error_default(error);
}
