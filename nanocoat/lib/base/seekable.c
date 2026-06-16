/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/seekable.h"
#include "sjme/debug.h"
#include "sjme/closeable.h"

static sjme_errorCode sjme_seekable_closeHandler(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_seekable seekable;
	
	/* Recover seekable. */
	seekable = (sjme_seekable)closeable;
	if (seekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to close handler. */
	if (seekable->functions->close != NULL)
		if (sjme_error_is(error = seekable->functions->close(seekable,
			&seekable->implState)))
			return sjme_error_default(error);

#if defined(SJME_CONFIG_HAS_BROKEN_CODE)
	/* Deallocate self. */
	if (sjme_error_is(error = sjme_alloc_free(seekable)))
		return sjme_error_default(error);
#endif
	
	/* Success!. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_seekable_open(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull const sjme_seekable_functions* inFunctions,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_seekable result;
	
	if (allocPool == NULL || outSeekable == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* These are required. */
	if (inFunctions->size == NULL ||
		inFunctions->init == NULL ||
		inFunctions->close == NULL)
		return sjme_error_notImplemented(0);
	
	/* Setup result. */
	result = NULL;
	if (sjme_error_is(error = sjme_closeable_alloc(allocPool,
		sizeof(*result), sjme_seekable_closeHandler,
		SJME_AS_CLOSEABLEP(&result))) || result == NULL)
		return sjme_error_default(error);
	
	/* Copy in details. */
	result->allocPool = allocPool;
	result->functions = inFunctions;
	if (copyFrontEnd != NULL)
		sjme_frontEnd_copy(&result->frontEnd,
			copyFrontEnd);

	/* Set default size cache. */
	sjme_atomic_s(sjme_jint, &result->cachedSize, -1);
	
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
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length)
{
	if (allocPool == NULL || inSeekable == NULL || outSeekable == NULL)
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
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (seekBase < 0 || length < 0 || (seekBase + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (seekable->functions->read == NULL)
		return sjme_error_notImplemented(0);
	
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
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (wordSize < 2 || wordSize > 8 || (wordSize & 1) != 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (seekBase < 0 || length < 0 || (seekBase + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if ((length % wordSize) != 0)
		return SJME_ERROR_UNALIGNED_ACCESS;
	
	if (seekable->functions->read == NULL)
		return sjme_error_notImplemented(0);
	
	/* Setup temporary buffer. */
	tempBuf = sjme_alloca(length);
	if (tempBuf == NULL)
		return sjme_error_outOfMemory(NULL, length);
	memset(tempBuf, 0, length);
	
	/* Read in data. */
	if (sjme_error_is(error = sjme_seekable_read(seekable,
		tempBuf, seekBase, length)))
		goto fail_read;
	
	/* Flip all the contained data. */
	halfWord = wordSize / 2;
	for (flipBase = 0; flipBase < length; flipBase += wordSize)
		for (lo = 0, hi = wordSize - 1; lo < halfWord; lo++, hi--)
		{
			temp = tempBuf[flipBase + lo];
			tempBuf[flipBase + lo] = tempBuf[flipBase + hi];
			tempBuf[flipBase + hi] = (sjme_jbyte)temp;
		}
	
	/* Give the flipped data! */
	memmove(outBuf, tempBuf, length);
	
	/* Cleanup. */
	sjme_alloca_free(tempBuf);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_read:
	if (tempBuf != NULL)
		sjme_alloca_free(tempBuf);
	return sjme_error_default(error);
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
		return sjme_error_notImplemented(0);

	/* Non-volatile size and it was cached? */
	size = -1;
	if (!seekable->implState.flags.volatileSize)
	{
		/* Check the cache. */
		size = sjme_atomic_g(sjme_jint, &seekable->cachedSize);
		if (size >= 0)
		{
			*outSize = size;
			return SJME_ERROR_NONE;
		}
	}
		
	/* Lock seekable. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&seekable->lock)))
		return sjme_error_default(error);
	
	/* Forward size call. */
	error = seekable->functions->size(seekable,
		&seekable->implState, &size);

	/* Store the cached size. */
	if (!seekable->implState.flags.volatileSize && size >= 0)
		sjme_atomic_s(sjme_jint, &seekable->cachedSize, size);
	
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

sjme_errorCode sjme_seekable_write(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_buffer inBuf,
	sjme_attrInPositive sjme_jint seekBase,
	sjme_attrInPositive sjme_jint length)
{
	sjme_errorCode error;
	
	if (seekable == NULL || inBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (seekBase < 0 || length < 0 || (seekBase + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (seekable->functions->write == NULL)
		return sjme_error_notImplemented(0);
	
	/* Lock seekable. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&seekable->lock)))
		return sjme_error_default(error);
	
	/* Forward write. */
	error = seekable->functions->write(seekable,
		&seekable->implState, inBuf, seekBase, length);
	
	/* Release lock. */
	if (sjme_error_is(sjme_thread_spinLockRelease(&seekable->lock,
		NULL)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
}
