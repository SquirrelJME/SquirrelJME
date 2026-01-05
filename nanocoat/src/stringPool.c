/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/stringPool.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/util.h"

/** The initial size of the string pool. */
#define SJME_STRING_POOL_INIT 32

/** The amount the size of the string pool should grow. */
#define SJME_STRING_POOL_GROW 16

sjme_errorCode sjme_nvm_stringPool_locateSeqR(
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_stringPool_string* outString,
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint offset
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_jint i, n, firstFree;
	sjme_list(sjme_phantom(sjme_nvm_stringPool_string))* strings;
	sjme_nvm_stringPool_string result;
	sjme_nvm_stringPool_string possible;
	
	if (inStringPool == NULL || inSeq == NULL || outString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (offset < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Lock pool. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inStringPool->common.lock)))
		return sjme_error_default(error);

	/* Set later on. */
	result = NULL;
	
	/* Try to locate the string first. */
	strings = inStringPool->strings;
	firstFree = -1;
	for (i = 0, n = strings->length; i < n; i++)
	{
		/* There might be an element here. */
		possible = sjme_atomic_g(sjme_nvm_stringPool_string,
			&strings->elements[i]);

		/* Is this no longer valid? */
		if (!sjme_nvm_isAR(possible,
			SJME_NVM_STRUCT_STRING_POOL_STRING))
		{
			/* This is considered a free slot. */
			if (firstFree < 0)
				firstFree = i;
			
			/* Wipe reference. */
			sjme_atomic_cs(sjme_nvm_stringPool_string,
				&strings->elements[i], possible, NULL);
			continue;
		}

		/* Consider empty slots as free */
		if (possible == NULL)
		{
			if (firstFree < 0)
				firstFree = i;
			continue;
		}
		
		/* Check for the same sequence. */
		if (!sjme_charSeq_equalsR(possible->seq, inSeq))
			continue;

		/* This is the one! */
		result = possible;
		goto skip_foundString;
	}

	/* Increase the pool size? */
	if (firstFree < 0)
	{
		/* First free is always at the end. */
		firstFree = strings->length;

		/* Grow the list. */
		if (sjme_error_is(error = sjme_list_replace(
			inStringPool->allocPool,
			strings->length + SJME_STRING_POOL_GROW,
			&inStringPool->strings,
			sjme_phantom(sjme_nvm_stringPool_string), 0)))
			goto fail_growList;
		strings = inStringPool->strings;
	}
	
	/* Allocate new result to store in the slot. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_allocR(
		(sjme_nvm)inStringPool->allocPool,
		sizeof(*result), 
		SJME_NVM_STRUCT_STRING_POOL_STRING,
		SJME_AS_NVM_COMMONP(&result)
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) ||
		result == NULL)
		goto fail_stringAlloc;
	
	/* Make copy of the sequence. */
	result->seq = NULL;
	if (sjme_error_is(error = sjme_charSeq_dup(inStringPool->allocPool,
		&result->seq, inSeq)) || result->seq == NULL)
		goto fail_dupSeq;
	
	/* Store it into the pool, all strings are phantom references so that */
	/* they can be cleaned up. */
	sjme_atomic_s(sjme_nvm_stringPool_string,
		&strings->elements[firstFree], result);
	
skip_foundString:
	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inStringPool->common.lock, NULL)))
		goto fail_releaseLock;
	
	/* Success! */
	*outString = result;
	return SJME_ERROR_NONE;

fail_dupSeq:
	if (result != NULL && result->seq != NULL)
	{
		sjme_alloc_free(result->seq);
		result->seq = NULL;
	}
fail_stringAlloc:
	if (result != NULL)
		sjme_alloc_free(result);
fail_growList:
fail_releaseLock:
	/* Unlock before failing. */
	sjme_thread_spinLockRelease(&inStringPool->common.lock,
		NULL);

	if (error == SJME_ERROR_OUT_OF_MEMORY)
		return sjme_error_outOfMemory(inStringPool->allocPool, 0);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_stringPool_locateStreamR(
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_nvm_stringPool_string* outString
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_jshort length;
	sjme_jbyte* chars;
	sjme_jint count;
	sjme_charSeqStatic seq;
	
	if (inStringPool == NULL || inStream == NULL || outString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in string length. */
	length = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &length)) || length < 0)
		return sjme_error_default(error);
	
	/* Allocate buffer to store it within. */
	chars = sjme_alloca(length);
	if (chars == NULL)
		return sjme_error_outOfMemory(NULL, NULL);
	memset(chars, 0, length);
	
	/* Need to read in everything. */
	if (sjme_error_is(error = sjme_stream_inputReadFully(
		inStream, &count, chars, length)))
		return sjme_error_default(error);
	
	/* Too short of a read? */
	if (count != length)
		return SJME_ERROR_END_OF_FILE;

	/* Setup base sequence. */
	memset(&seq, 0, sizeof(seq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&seq,
		(sjme_lpcstr)chars, 0, length)))
		return sjme_error_default(error);
	
	/* Use normal locating logic. */
	return sjme_nvm_stringPool_locateSeqR(inStringPool,
		outString, &seq, 0 
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY);
}

sjme_errorCode sjme_nvm_stringPool_locateUtfR(
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrOutNotNull sjme_nvm_stringPool_string* outString,
	sjme_attrInNotNull sjme_lpcstr inUtf,
	sjme_attrInNegativeOnePositive sjme_jint offset,
	sjme_attrInNegativeOnePositive sjme_jint inUtfLen
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_charSeqStatic seq;
	
	if (inStringPool == NULL || outString == NULL || inUtf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inUtfLen < -1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Determine length? */
	if (inUtfLen == -1)
		inUtfLen = strlen(inUtf);

	/* Setup sequence. */
	memset(&seq, 0, sizeof(seq));
	if (sjme_error_is(error = sjme_charSeq_newUtfStatic(&seq,
		inUtf, offset, inUtfLen)))
		return sjme_error_default(error);

	/* Forward call. */
	return sjme_nvm_stringPool_locateSeq(inStringPool, outString,
		&seq, 0);
}

sjme_errorCode sjme_nvm_stringPool_newR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_nvm_stringPool* outStringPool
	SJME_DEBUG_ONLY_COMMA
	SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_nvm_stringPool result;
	sjme_list(sjme_phantom(sjme_nvm_stringPool_string))* strings;
	
	if (allocPool == NULL || outStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Make sure we have the memory to store the buffer. */
	strings = NULL;
	if (sjme_error_is(error = sjme_list_allocD(
		allocPool, SJME_STRING_POOL_INIT,
		&strings, sjme_phantom(sjme_nvm_stringPool_string), 0)) ||
		strings == NULL)
		goto fail_allocList;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_allocR(
		(sjme_nvm)allocPool,
		sizeof(*result), SJME_NVM_STRUCT_STRING_POOL,
		SJME_AS_NVM_COMMONP(&result)
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) || result == NULL)
		goto fail_allocResult;
	
	/* Setup fields. */
	result->allocPool = allocPool;
	result->strings = strings;
	
	/* Success! */
	*outStringPool = result;
	return SJME_ERROR_NONE;
	
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
fail_allocList:
	if (strings != NULL)
		sjme_alloc_free(strings);
	return sjme_error_default(error);
}
