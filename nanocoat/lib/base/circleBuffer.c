/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/circleBuffer.h"
#include "sjme/debug.h"

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
	if (buffer == NULL || outData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (seekType != SJME_CIRCLE_BUFFER_HEAD &&
		seekType != SJME_CIRCLE_BUFFER_TAIL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (length < 0 || seekPos < 0 || (seekPos + length) < 0 ||
		(seekPos + length) > buffer->ready)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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
	if (buffer == NULL || outData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (seekType != SJME_CIRCLE_BUFFER_HEAD &&
		seekType != SJME_CIRCLE_BUFFER_TAIL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (length < 0 || length > buffer->ready)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_circleBuffer_pushQueue(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrInNotNullBuf(outDataLen) sjme_cpointer inData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType)
{
	if (buffer == NULL || inData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (seekType != SJME_CIRCLE_BUFFER_HEAD &&
		seekType != SJME_CIRCLE_BUFFER_TAIL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (length < 0 || (buffer->ready + length) < 0 ||
		(buffer->ready + length) > buffer->size)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_circleBuffer_pushWindow(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrInNotNullBuf(outDataLen) sjme_cpointer inData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType)
{
	if (buffer == NULL || inData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (seekType != SJME_CIRCLE_BUFFER_HEAD &&
		seekType != SJME_CIRCLE_BUFFER_TAIL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (length < 0 || (buffer->ready + length) < 0 ||
		length > buffer->size)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_circleBuffer_push(
	sjme_attrInNotNull sjme_circleBuffer* buffer,
	sjme_attrInNotNullBuf(outDataLen) sjme_cpointer inData,
	sjme_attrInPositiveNonZero sjme_jint length,
	sjme_attrInValue sjme_circleBuffer_seekEnd seekType)
{
	if (buffer == NULL || inData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (seekType != SJME_CIRCLE_BUFFER_HEAD &&
		seekType != SJME_CIRCLE_BUFFER_TAIL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Forward depending on the mode. */
	if (buffer->mode == SJME_CIRCLE_BUFFER_WINDOW)
		return sjme_circleBuffer_pushWindow(buffer, inData, length, seekType);
	return sjme_circleBuffer_pushQueue(buffer, inData, length, seekType);
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
