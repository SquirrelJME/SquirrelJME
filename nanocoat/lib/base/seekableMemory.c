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

/**
 * Memory initialization info.
 * 
 * @since 2024/08/12
 */
typedef struct sjme_seekable_memoryInitData
{
	/** The base pointer. */
	sjme_pointer base;
	
	/** The memory region size. */
	sjme_jint length;
} sjme_seekable_memoryInitData;

static sjme_errorCode sjme_seekable_memoryClose(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState)
{
	if (inSeekable == NULL || inImplState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Nothing needs to be done for memory regions. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_seekable_memoryInit(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_seekable_memoryInitData* init;
	
	if (inSeekable == NULL || inImplState == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover init data. */
	init = data;
	
	/* Set parameters. */
	inImplState->handle = init->base;
	inImplState->length = init->length;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_seekable_memoryRead(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNullBuf(length) sjme_buffer outBuf,
	sjme_attrInPositive sjme_jint base,
	sjme_attrInPositiveNonZero sjme_jint length)
{
	if (inSeekable == NULL || inImplState == NULL || outBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (base < 0 || length < 0 || (base + length) < 0 ||
		(base + length) > inImplState->length)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Direct memory copy. */
	memmove(outBuf, SJME_POINTER_OFFSET(inImplState->handle, base),
		length);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_seekable_memorySize(
	sjme_attrInNotNull sjme_seekable inSeekable,
	sjme_attrInNotNull sjme_seekable_implState* inImplState,
	sjme_attrOutNotNull sjme_jint* outSize)
{
	if (inSeekable == NULL || inImplState == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Just copy from the implementation state. */
	*outSize = inImplState->length;
	return SJME_ERROR_NONE;
}

static const sjme_seekable_functions sjme_seekable_memoryFunctions =
{
	.close = sjme_seekable_memoryClose,
	.init = sjme_seekable_memoryInit,
	.read = sjme_seekable_memoryRead,
	.size = sjme_seekable_memorySize,
};

sjme_errorCode sjme_seekable_openMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInNotNull sjme_pointer base,
	sjme_attrInPositive sjme_jint length)
{
	uintptr_t rawBase;
	sjme_seekable_memoryInitData init;

	if (inPool == NULL || outSeekable == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Check for overflow. */
	rawBase = (uintptr_t)base;
	if (length < 0 || (rawBase + length) < rawBase)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Setup initialization data. */
	memset(&init, 0, sizeof(init));
	init.base = base;
	init.length = length;
	
	/* Forward initialize. */
	return sjme_seekable_open(inPool, outSeekable,
		&sjme_seekable_memoryFunctions,
		&init, NULL);
}
