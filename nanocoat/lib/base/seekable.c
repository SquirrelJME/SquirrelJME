/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/seekable.h"
#include "sjme/debug.h"
#include "sjme/closeable.h"

static sjme_errorCode sjme_seekable_closeHandler(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_seekable seekable;
	
	/* Recover seekable. */
	seekable = (sjme_seekable)closeable;
	if (seekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to close handler. */
	if (seekable->functions->close != NULL)
		return seekable->functions->close(seekable,
			&seekable->implState);
	
	/* No handler, just success. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_seekable_asInputStream(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length,
	sjme_attrInValue sjme_jboolean forwardClose)
{
	if (seekable == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (base < 0 || length < 0 || (base + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_seekable_open(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull const sjme_seekable_functions* inFunctions,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_seekable result;
	
	if (inPool == NULL || outSeekable == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* These are required. */
	if (inFunctions->size == NULL ||
		inFunctions->read == NULL ||
		inFunctions->init == NULL ||
		inFunctions->close == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Setup result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), sjme_closeable_autoEnqueue, NULL,
		(void**)&result, NULL)) || result == NULL)
		return sjme_error_default(error);
	
	/* Copy in details. */
	result->closable.closeHandler = sjme_seekable_closeHandler;
	result->functions = inFunctions;
	if (copyFrontEnd != NULL)
		memmove(&result->frontEnd,
			copyFrontEnd, sizeof(*copyFrontEnd));
	
	/* Initialize. */
	if (sjme_error_is(error = result->functions->init(result,
		&result->implState, data)))
	{
		/* Free before failing. */
		sjme_alloc_free(result);
		
		return sjme_error_default(error);
	}
	
	/* Success! */
	*outSeekable = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_seekable_openSeekable(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length)
{
	if (inPool == NULL || inSeekable == NULL || outSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (base < 0 || length < 0 || (base + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_seekable_read(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_buffer outBuf,
	sjme_attrInPositive sjme_jint seekBase,
	sjme_attrInPositive sjme_jint length)
{
	sjme_errorCode error;
	
	if (seekable == NULL || outBuf == NULL)
		return SJME_ERROR_NONE;
	
	if (seekBase < 0 || length < 0 || (seekBase + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (seekable->functions->read == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Lock seekable. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&seekable->lock)))
		return sjme_error_default(error);
	
	/* Forward read. */
	error = seekable->functions->read(seekable,
		&seekable->implState, outBuf, seekBase, length);
	
	/* Release lock. */
	if (sjme_error_is(sjme_thread_spinLockRelease(&seekable->lock,
		NULL)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
}

sjme_errorCode sjme_seekable_readReverse(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrInRange(2, 8) sjme_jint wordSize,
	sjme_attrOutNotNull sjme_buffer outBuf,
	sjme_attrInPositive sjme_jint seekBase,
	sjme_attrInPositive sjme_jint length)
{
	sjme_errorCode error;
	sjme_jbyte* tempBuf;
	sjme_jint flipBase, hi, lo, halfWord, temp;
	
	if (seekable == NULL || outBuf == NULL)
		return SJME_ERROR_NONE;
	
	if (wordSize < 2 || wordSize > 8 || (wordSize & 1) != 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (seekBase < 0 || length < 0 || (seekBase + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if ((length % wordSize) != 0)
		return SJME_ERROR_UNALIGNED_ACCESS;
	
	if (seekable->functions->read == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Setup temporary buffer. */
	tempBuf = sjme_alloca(length);
	if (tempBuf == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(tempBuf, 0, length);
	
	/* Read in data. */
	if (sjme_error_is(error = sjme_seekable_read(seekable,
		tempBuf, seekBase, length)))
		return sjme_error_default(error);
	
	/* Flip all the contained data. */
	halfWord = wordSize / 2;
	for (flipBase = 0; flipBase < length; flipBase += wordSize)
		for (lo = 0, hi = wordSize - 1; lo < halfWord; lo++, hi--)
		{
			temp = tempBuf[flipBase + lo];
			tempBuf[flipBase + lo] = tempBuf[flipBase + hi];
			tempBuf[flipBase + hi] = temp;
		}
	
	/* Give the flipped data! */
	memmove(outBuf, tempBuf, length);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_seekable_regionLock(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_seekable_lock* outLock,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length)
{
	if (seekable == NULL || outLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (base < 0 || length < 0 || (base + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_seekable_regionLockAsInputStream(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length)
{
	if (seekable == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (base < 0 || length < 0 || (base + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_seekable_regionUnlock(
	sjme_attrInNotNull sjme_seekable_lock inLock,
	sjme_attrInRange(0, SJME_NUM_SEEKABLE_UNLOCK_ACTION)
		sjme_seekable_unlockAction action)
{
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (action < 0 || action >= SJME_NUM_SEEKABLE_UNLOCK_ACTION)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_seekable_size(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_jint* outSize)
{
	sjme_errorCode error;
	sjme_jint size;
	
	if (seekable == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (seekable->functions->size == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
		
	/* Lock seekable. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&seekable->lock)))
		return sjme_error_default(error);
	
	/* Forward size call. */
	size = -1;
	error = seekable->functions->size(seekable,
		&seekable->implState, &size);
	
	/* Release lock. */
	if (sjme_error_is(sjme_thread_spinLockRelease(&seekable->lock,
		NULL)))
		return sjme_error_default(error);
	
	/* Failed? */
	if (sjme_error_is(error) || size < 0)
		return sjme_error_default(error);
	
	/* Success! */
	*outSize = size;
	return SJME_ERROR_NONE;
}
