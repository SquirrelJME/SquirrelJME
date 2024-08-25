/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/circleBuffer.h"
#include "sjme/debug.h"

/**
 * Generic operation for circle buffers.
 * 
 * @since 2024/08/25
 */
typedef enum sjme_circleBuffer_operation
{
	/** Push operation, queue. */
	SJME_CIRCLE_BUFFER_PUSH_QUEUE,
	
	/** Push operation, window. */
	SJME_CIRCLE_BUFFER_PUSH_WINDOW,
	
	/** Pop operation. */
	SJME_CIRCLE_BUFFER_POP,
	
	/** Get operation. */
	SJME_CIRCLE_BUFFER_GET,
	
	/** The number of operations that can be performed. */
	SJME_CIRCLE_BUFFER_NUM_OPERATIONS
} sjme_circleBuffer_operation;

/**
 * A slice for accessing a buffer.
 * 
 * @since 2024/08/25
 */
typedef struct sjme_circleBuffer_slice
{
	/** The base pointer position. */
	sjme_pointer base;
		
	/** The length. */
	sjme_jint len;
} sjme_circleBuffer_slice;

/**
 * Stores the calculated result of the operation.
 * 
 * @since 2024/08/25
 */
typedef struct sjme_circleBuffer_calcResult
{
	/** The new ready. */
	sjme_jint newReady;
	
	/** The new write head. */
	sjme_jint newWriteHead;
	
	/** The new read head. */
	sjme_jint newReadHead;
	
	/** The source data slice. */
	sjme_circleBuffer_slice src[2];
	
	/** The destination data slice. */
	sjme_circleBuffer_slice dest[2];
} sjme_circleBuffer_calcResult;

static sjme_errorCode sjme_circleBuffer_calc(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNull sjme_circleBuffer_calcResult* result,
	sjme_attrInValue sjme_circleBuffer_operation operation,
	sjme_attrInNotNullBuf(length) sjme_pointer opData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType,
	sjme_attrInPositiveNonZero sjme_jint seekPos)
{
	if (buffer == NULL || result == NULL || opData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (seekType != SJME_CIRCLE_BUFFER_HEAD &&
		seekType != SJME_CIRCLE_BUFFER_TAIL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (operation != SJME_CIRCLE_BUFFER_GET && seekPos != 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (seekPos < 0 || length < 0 || (seekPos + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Clear result first. */
	memset(result, 0, sizeof(*result));
	
	/* Pushing? */
	if (operation == SJME_CIRCLE_BUFFER_PUSH_QUEUE ||
		operation == SJME_CIRCLE_BUFFER_PUSH_WINDOW)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Popping? */
	else if (operation == SJME_CIRCLE_BUFFER_POP)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Getting? */
	else if (operation == SJME_CIRCLE_BUFFER_GET)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Invalid. */
	return SJME_ERROR_INVALID_ARGUMENT;
}

static sjme_errorCode sjme_circleBuffer_operate(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrInNotNull sjme_circleBuffer_calcResult* result)
{
	sjme_jint i;
	sjme_circleBuffer_slice* src;
	sjme_circleBuffer_slice* dest;
	
	if (buffer == NULL || result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Copy each slice, if valid. */
	for (i = 0; i < 2; i++)
	{
		src = &result->src[i];
		dest = &result->dest[i];
		
		/* At least the first should be set. */
		if (i == 0 && (src->base == NULL || dest->base == NULL))
			return SJME_ERROR_ILLEGAL_STATE;
		
		/* Fail if null mismatches. */
		if ((src->base == NULL) != (dest->base == NULL))
			return SJME_ERROR_ILLEGAL_STATE;
		
		/* Stop if there is no slice here. */
		if (src->base == NULL && dest->base == NULL)
			continue;
		
		/* These must always be the same. */
		if (src->len != dest->len || src->base == NULL || dest->base == NULL)
			return SJME_ERROR_ILLEGAL_STATE;
		
		/* Move the slice data over. */
		memmove(dest->base, src->base, src->len);
	}
	
	/* Set new heads. */
	buffer->ready = result->newReady;
	buffer->readHead = result->newReadHead;
	buffer->writeHead = result->newWriteHead;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_circleBuffer_available(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNull sjme_jint* outAvailable)
{
	if (buffer == NULL || outAvailable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	*outAvailable = buffer->size - buffer->ready;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_circleBuffer_destroy(
	sjme_attrInNotNull sjme_circleBuffer* buffer)
{
	sjme_errorCode error;
	
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Free storage first. */
	if (buffer->buffer != NULL)
	{
		if (sjme_error_is(error = sjme_alloc_free(buffer->buffer)))
			return sjme_error_default(error);
		buffer->buffer = NULL;
	}
	
	/* Then the buffer info. */
	return sjme_alloc_free(buffer);
}

sjme_errorCode sjme_circleBuffer_get(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNullBuf(outDataLen) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType,
	sjme_attrInPositiveNonZero sjme_jint seekPos)
{
	sjme_errorCode error;
	sjme_circleBuffer_calcResult result;
	
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Calculate result. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(error = sjme_circleBuffer_calc(
		buffer, &result,
		SJME_CIRCLE_BUFFER_GET,
		outData, length, seekType, seekPos)))
		return sjme_error_default(error);
	
	/* Operate on it. */
	return sjme_circleBuffer_operate(buffer, &result);
}

sjme_errorCode sjme_circleBuffer_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_circleBuffer** outBuffer,
	sjme_attrInValue sjme_circleBuffer_mode inMode,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	sjme_errorCode error;
	sjme_pointer storage;
	sjme_circleBuffer* result;
	
	if (inPool == NULL || outBuffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inMode != SJME_CIRCLE_BUFFER_QUEUE &&
		inMode != SJME_CIRCLE_BUFFER_WINDOW)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (length <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Make sure we can allocate the buffer first. */
	storage = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool,
		length, (sjme_pointer*)&storage)) || storage == NULL)
		goto fail_allocStorage;
	
	/* Then the actual buffer info. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool,
		sizeof(*result), (sjme_pointer*)&result)) || result == NULL)
		goto fail_allocResult; 
	
	/* Setup information. */
	result->size = length;
	result->mode = inMode;
	result->buffer = storage;
	
	/* Success! */
	*outBuffer = result;
	return SJME_ERROR_NONE;
	
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
fail_allocStorage:
	if (storage != NULL)
		sjme_alloc_free(storage);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_circleBuffer_pop(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNullBuf(outDataLen) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType)
{
	sjme_errorCode error;
	sjme_circleBuffer_calcResult result;
	
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Calculate result. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(error = sjme_circleBuffer_calc(
		buffer, &result,
		SJME_CIRCLE_BUFFER_POP,
		outData, length, seekType, 0)))
		return sjme_error_default(error);
	
	/* Operate on it. */
	return sjme_circleBuffer_operate(buffer, &result);
}

sjme_errorCode sjme_circleBuffer_push(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrInNotNullBuf(outDataLen) sjme_cpointer inData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType)
{
	sjme_errorCode error;
	sjme_circleBuffer_calcResult result;
	
	if (buffer == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Calculate result. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(error = sjme_circleBuffer_calc(
		buffer, &result,
		(buffer->mode == SJME_CIRCLE_BUFFER_WINDOW ?
			SJME_CIRCLE_BUFFER_PUSH_WINDOW : SJME_CIRCLE_BUFFER_PUSH_QUEUE),
		inData, length, seekType, 0)))
		return sjme_error_default(error);
	
	/* Operate on it. */
	return sjme_circleBuffer_operate(buffer, &result);
}

sjme_errorCode sjme_circleBuffer_stored(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNull sjme_jint* outStored)
{
	if (buffer == NULL || outStored == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	*outStored = buffer->ready;
	return SJME_ERROR_NONE;
}
