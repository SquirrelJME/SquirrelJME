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
 * Byte stream initialization data.
 * 
 * @since 2024/08/12
 */
typedef struct sjme_stream_byteInit
{
	/** The initial limit. */
	sjme_jint initialLimit;
	
	/** The finish function. */
	sjme_stream_outputByteArrayFinishFunc finish;
	
	/** The data to pass to the finish function. */
	sjme_pointer finishData;
} sjme_stream_byteInit;

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
	sjme_attrInNotNull sjme_stream_implState* inImplState)
{
	sjme_stream_outputByteArrayFinishFunc finish;
	sjme_pointer finishData;
	sjme_stream_resultByteArray result;
	sjme_errorCode error;

	if (stream == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initialize result. */
	memset(&result, 0, sizeof(result));
	result.array = inImplState->buffer;
	result.length = stream->totalWritten;
	result.free = SJME_JNI_TRUE;
	result.whatever = inImplState->handleTwo;
	
	/* Recover finisher. */
	finish = inImplState->handle;
	finishData = inImplState->handleTwo;
	
	/* Call the finish handler, if there is one. */
	if (finish != NULL)
	{
		/* Call handler, if it fails just stop. */
		if (sjme_error_is(error = finish(stream, &result, finishData)))
			return sjme_error_default(error);
	}

	/* Are we freeing the array? */
	if (result.free)
	{
		if (sjme_error_is(error = sjme_alloc_free(inImplState->buffer)))
			return sjme_error_default(error);
		
		inImplState->buffer = NULL;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_stream_outputByteArrayInit(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_errorCode error;
	sjme_pointer initBuf;
	sjme_stream_byteInit* init;
	
	if (stream == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover initializer. */
	init = data;
		
	/* Try to allocate an initial buffer. */
	initBuf = NULL;
	if (sjme_error_is(error = sjme_alloc(inImplState->inPool,
		init->initialLimit, &initBuf)) || initBuf == NULL)
		goto fail_initBufAlloc;
	
	/* Setup state. */
	inImplState->buffer = initBuf;
	inImplState->handle = init->finish;
	inImplState->handleTwo = init->finishData;
	inImplState->limit = init->initialLimit;
	
	/* Success! */
	return SJME_ERROR_NONE;
		
fail_initBufAlloc:
	if (initBuf != NULL)
		sjme_alloc_free(initBuf);

	return sjme_error_default(error);
}

static sjme_errorCode sjme_stream_outputByteArrayWrite(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_implState* inImplState,
	sjme_attrInNotNull sjme_cpointer buf,
	sjme_attrInPositiveNonZero sjme_jint length)
{
#define GROW_SIZE 32
	uintptr_t realBuf;
	sjme_jint available, desireSize;
	sjme_errorCode error;

	if (stream == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	realBuf = (uintptr_t)buf;
	if (length < 0 || (realBuf + length) < realBuf)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Not enough bytes to fit into the array buffer? Need to grow it? */
	available = inImplState->limit - stream->totalWritten;
	if (length > available)
	{
		/* Resultant buffer would overflow? Way too big? */
		desireSize = inImplState->limit + length + GROW_SIZE;
		if (desireSize < 0)
			return SJME_ERROR_OUT_OF_MEMORY;

		/* Reallocate memory here. */
		if (sjme_error_is(error = sjme_alloc_realloc(
			&inImplState->buffer, desireSize)))
			return sjme_error_defaultOr(error,
				SJME_ERROR_OUT_OF_MEMORY);

		/* The buffer's limit has now increased. */
		inImplState->limit = desireSize;
	}

	/* Copy directly into the array buffer. */
	/* Note that the caller increases totalWritten. */
	memmove(&inImplState->buffer[stream->totalWritten],
		buf, length);

	/* Success! */
	return SJME_ERROR_NONE;
#undef GROW_SIZE
}

static const sjme_stream_outputFunctions sjme_stream_outputByteArrayFunctions =
{
	.close = sjme_stream_outputByteArrayClose,
	.init = sjme_stream_outputByteArrayInit,
	.write = sjme_stream_outputByteArrayWrite,
};

sjme_errorCode sjme_stream_outputOpenByteArray(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInPositive sjme_jint initialLimit,
	sjme_attrInNullable sjme_stream_outputByteArrayFinishFunc finish,
	sjme_attrInNullable sjme_pointer finishData)
{
	sjme_stream_byteInit init;
	
	if (inPool == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Fallback to a default initial limit if zero. */
	if (initialLimit == 0)
		initialLimit = 32;

	if (initialLimit < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Setup initializer. */
	memset(&init, 0, sizeof(init));
	init.initialLimit = initialLimit;
	init.finish = finish;
	init.finishData = finishData;
	
	/* Forward to initialization routine. */
	return sjme_stream_outputOpen(inPool,
		outStream,
		&sjme_stream_outputByteArrayFunctions,
		&init,
		NULL);
}

static sjme_errorCode sjme_stream_outputOpenByteArrayToTarget(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_resultByteArray* result,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_stream_resultByteArray* target;
	
	if (stream == NULL || result == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover target result. */
	target = data;
	
	/* Do not free the buffer. */
	result->free = SJME_JNI_FALSE;
	
	/* Copy everything over and continue. */
	memmove(target, result, sizeof(*result));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_stream_outputOpenByteArrayTo(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stream_output* outStream,
	sjme_attrInPositive sjme_jint initialLimit,
	sjme_attrInNotNull sjme_stream_resultByteArray* result)
{
	if (inPool == NULL || outStream == NULL || result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward. */
	return sjme_stream_outputOpenByteArray(inPool, outStream,
		initialLimit, sjme_stream_outputOpenByteArrayToTarget,
		result);
}
