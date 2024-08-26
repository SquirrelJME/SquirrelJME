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
	/** Non-circle buffer pointer? */
	sjme_pointer externalBuf;
	
	/** The base position. */
	sjme_jint base;
		
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

static sjme_errorCode sjme_circleBuffer_splice(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNull const sjme_circleBuffer_slice* circleSl,
	sjme_attrOutNotNull const sjme_circleBuffer_slice* externSl,
	sjme_attrOutNotNull const sjme_circleBuffer_slice* srcSl,
	sjme_attrOutNotNull const sjme_circleBuffer_slice* destSl,
	sjme_attrOutNotNull sjme_circleBuffer_calcResult* result)
{
	sjme_circleBuffer_slice* circleAl;
	sjme_circleBuffer_slice* externAl;
	sjme_circleBuffer_slice* circleBl;
	sjme_circleBuffer_slice* externBl;
	sjme_circleBuffer_slice* tempXl;
	sjme_jint clipped, len, circleBase, externBase, limit;
	
	if (buffer == NULL || circleSl == NULL || externSl == NULL ||
		srcSl == NULL || destSl == NULL || result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Common parameters used in the clipping algorithm. */ 
	circleBase = circleSl->base;
	externBase = externSl->base;
	len = circleSl->len;
	
	/* Debug. */
	sjme_message("len: %d", len);
	
	/* Completely an inside slice? */
	if (circleBase >= 0 && circleBase < buffer->size &&
		circleBase + len <= buffer->size)
	{
		result->src[0] = *srcSl;
		result->dest[0] = *destSl;
		
		return SJME_ERROR_NONE;
	}
	
	/* Determine source and destination A and B sides to the slice. */
	if (srcSl == circleSl)
	{
		circleAl = &result->src[0];
		externAl = &result->dest[0];
		circleBl = &result->src[1];
		externBl = &result->dest[1];
	}
	else
	{
		circleAl = &result->dest[0];
		externAl = &result->src[0];
		circleBl = &result->dest[1];
		externBl = &result->src[1];
	}
	
	/* External in opposite order? */
	if (circleBase < 0 && circleBase + len > 0)
	{
		tempXl = externBl;
		externBl = externAl;
		externAl = tempXl;
	}
	
	/* Left side slice? */
	if (circleBase < 0)
	{
		clipped = 0 - circleBase;
		
		circleAl->base = 0;
		circleAl->len = len - clipped;
		externAl->base = 0;
		externAl->len = len - clipped;
		
		circleBl->base = buffer->size - clipped;
		circleBl->len = clipped;
		externBl->base = len - clipped;
		externBl->len = clipped;
	}
	
	/* Right side slice? */
	else
	{
		clipped = (circleBase + len) - buffer->size;
		limit = buffer->size - circleBase;
		
		circleAl->base = circleBase;
		circleAl->len = limit;
		externAl->base = 0;
		externAl->len = limit;
		
		circleBl->base = 0;
		circleBl->len = clipped;
		externBl->base = externBase + limit;
		externBl->len = clipped;
	}
	
	/* Propagate buffers. */
	externAl->externalBuf = externSl->externalBuf;
	externBl->externalBuf = externSl->externalBuf;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_circleBuffer_calc(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrOutNotNull sjme_circleBuffer_calcResult* result,
	sjme_attrInValue sjme_circleBuffer_operation operation,
	sjme_attrInNotNullBuf(length) sjme_pointer opData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType,
	sjme_attrInPositiveNonZero sjme_jint seekPos)
{
	sjme_circleBuffer_slice circleSl;
	sjme_circleBuffer_slice externSl;
	sjme_circleBuffer_slice* srcSl;
	sjme_circleBuffer_slice* destSl;
	
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
	memset(&circleSl, 0, sizeof(circleSl));
	memset(&externSl, 0, sizeof(externSl));
	
	/* Default result parameters. */
	result->newReady = buffer->ready;
	result->newReadHead = buffer->readHead;
	result->newWriteHead = buffer->writeHead;
	
	/* External slice is always set to the passed buffer. */
	externSl.externalBuf = opData;
	externSl.base = 0;
	externSl.len = length;
	
	/* Circle slice is always the same length. */
	circleSl.len = length;
	
	/* Debug. */
	sjme_message("OPERATE %d", operation);
	
	/* Pushing as a queue? */
	if (operation == SJME_CIRCLE_BUFFER_PUSH_QUEUE)
	{
		/* External to circle. */
		srcSl = &externSl;
		destSl = &circleSl;
		
		/* Calculate new current data in the buffer. */
		result->newReady = buffer->ready + length;
		if (result->newReady > buffer->size)
			return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
		/* Writing at end? */
		if (seekType == SJME_CIRCLE_BUFFER_LAST)
		{
			result->newWriteHead = buffer->writeHead + length;
			circleSl.base = buffer->writeHead;
		}
		
		/* Writing at start? */
		else
		{
			result->newReadHead = buffer->readHead - length;
			circleSl.base = result->newReadHead;
		}
	}
	
	/* Pushing as a window? */
	else if (operation == SJME_CIRCLE_BUFFER_PUSH_WINDOW)
	{
		/* External to circle. */
		srcSl = &externSl;
		destSl = &circleSl;
		
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Popping? */
	else if (operation == SJME_CIRCLE_BUFFER_POP)
	{
		/* Circle to external. */
		srcSl = &circleSl;
		destSl = &externSl;
		
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Getting? */
	else if (operation == SJME_CIRCLE_BUFFER_GET)
	{
		/* Circle to external. */
		srcSl = &circleSl;
		destSl = &externSl;
		
		/* The positions in the circle buffer are relative to the heads. */
		if (seekType == SJME_CIRCLE_BUFFER_LAST)
		{
			/* Cannot get more data than what exists in the buffer. */
			if ((buffer->ready - seekPos) + length > buffer->ready)
				return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
			
			circleSl.base = buffer->writeHead - seekPos;
		}
		else
		{
			/* Cannot get more data than what exists in the buffer. */
			if (seekPos + length > buffer->ready)
				return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
			
			circleSl.base = buffer->readHead + seekPos;
		}
	}
	
	/* Invalid. */
	else
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Make sure the read/write heads are in range. */
	while (result->newReadHead < 0)
		result->newReadHead += buffer->size;
	while (result->newReadHead >= buffer->size)
		result->newReadHead -= buffer->size;
	while (result->newWriteHead < 0)
		result->newWriteHead += buffer->size;
	while (result->newWriteHead >= buffer->size)
		result->newWriteHead -= buffer->size;
		
	/* Perform splice clipping. */
	return sjme_circleBuffer_splice(buffer, &circleSl, &externSl,
		srcSl, destSl, result);
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
	
	/* These must be in range. */
	if (result->newReady < 0 || result->newReady > buffer->size ||
		result->newReadHead < 0 || result->newReadHead > buffer->size ||
		result->newWriteHead < 0 || result->newWriteHead > buffer->size)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Copy each slice, if valid. */
	for (i = 0; i < 2; i++)
	{
		src = &result->src[i];
		dest = &result->dest[i];
		
		/* Skip if nothing here. */
		if (src->len == 0 && dest->len == 0)
			continue;
		
		/* Fail if both external or both internal. */
		if ((src->externalBuf == NULL) == (dest->externalBuf == NULL))
			return SJME_ERROR_ILLEGAL_STATE;
		
		/* These must always be the same. */
		if (src->len != dest->len)
			return SJME_ERROR_ILLEGAL_STATE;
			
		/* Debug. */
		sjme_message("Slice %d [%p %d %d] <- [%p %d %d]",
			i, dest->externalBuf, dest->base, dest->len,
			src->externalBuf, src->base, src->len);
		
		/* External to internal. */
		if (src->externalBuf != NULL && dest->externalBuf == NULL)
			memmove(&buffer->buffer[dest->base],
				SJME_POINTER_OFFSET(src->externalBuf, src->base),
				src->len);
		
		/* Internal to external. */
		else if (src->externalBuf == NULL && dest->externalBuf != NULL)
			memmove(SJME_POINTER_OFFSET(dest->externalBuf, dest->base),
				&buffer->buffer[src->base],
				src->len);
		
		/* Should not occur. */
		else
			return SJME_ERROR_ILLEGAL_STATE;
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
