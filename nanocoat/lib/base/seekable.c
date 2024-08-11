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

struct sjme_seekableBase
{
	/** Front end data. */
	sjme_frontEnd frontEnd;
	
	/** Functions for stream access. */
	const sjme_seekable_functions* functions;
	
	/** Spinlock for stream access. */
	sjme_thread_spinLock lock;
};

static sjme_errorCode sjme_seekable_autoClose(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree)
{
	sjme_seekable seekable;
	
	if (weak == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover seekable. */
	seekable = weak->pointer;
	if (seekable == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward close. */
	return sjme_seekable_close(seekable);
}

sjme_errorCode sjme_seekable_asInputStream(
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

sjme_errorCode sjme_seekable_close(
	sjme_attrInNotNull sjme_seekable seekable)
{
	if (seekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_seekable_open(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull const sjme_seekable_functions* inFunctions,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_seekable result;
	
	if (inPool == NULL || outSeekable == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* These are required. */
	if (inFunctions->size == NULL ||
		inFunctions->read == NULL ||
		inFunctions->close == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Setup result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), sjme_seekable_autoClose, NULL,
		&result, NULL)) || result == NULL)
		return sjme_error_default(error);
	
	/* Copy in details. */
	result->functions = inFunctions;
	if (copyFrontEnd != NULL)
		memmove(&result->frontEnd,
			copyFrontEnd, sizeof(*copyFrontEnd));
	
	/* Success! */
	*outSeekable = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_seekable_openMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull sjme_pointer base,
	sjme_attrInPositive sjme_jint length)
{
	uintptr_t rawBase;

	if (inPool == NULL || outSeekable == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Check for overflow. */
	rawBase = (uintptr_t)base;
	if (length < 0 || (rawBase + length) < rawBase)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_seekable_openSeekable(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositive sjme_jint length)
{
	if (inSeekable == NULL || outSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (base < 0 || length < 0 || (base + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_seekable_read(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_jbyte* outBuf,
	sjme_attrInPositive sjme_jint seekBase,
	sjme_attrInPositive sjme_jint length)
{
	if (seekable == NULL || outBuf == NULL)
		return SJME_ERROR_NONE;
	
	if (seekBase < 0 || length < 0 || (seekBase + length) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
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
	if (seekable == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}
