/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/stringPool.h"
#include "sjme/cleanup.h"
#include "sjme/util.h"

/** The amount the size of the string pool should grow. */
#define SJME_STRING_POOL_GROW 256

sjme_errorCode sjme_stringPool_locateSeq(
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_stringPool_string* outString)
{
	if (inStringPool == NULL || inSeq == NULL || outString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stringPool_locateStreamR(
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_stringPool_string* outString
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_jshort length;
	sjme_jbyte* chars;
	sjme_jint count;
	
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
	
	/* Use normal locating logic. */
	return sjme_stringPool_locateUtfR(inStringPool,
		(sjme_lpcstr)chars, length, outString
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY);
}

sjme_errorCode sjme_stringPool_locateUtfR(
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_lpcstr inUtf,
	sjme_attrInNegativeOnePositive sjme_jint inUtfLen,
	sjme_attrOutNotNull sjme_stringPool_string* outString
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_jint hash, i, n, firstFree;
	sjme_list_sjme_stringPool_string* strings;
	sjme_list_sjme_stringPool_string* oldStrings;
	sjme_stringPool_string result;
	sjme_stringPool_string possible;
	sjme_alloc_weak weak;
	
	if (inStringPool == NULL || inUtf == NULL || outString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inUtfLen < -1)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Lock pool. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inStringPool->common.lock)))
		return sjme_error_default(error);
	
	/* Determine actual string length, if unknown. */
	if (inUtfLen < 0)
		inUtfLen = strlen(inUtf);
	
	/* Calculate hash of string. */
	hash = sjme_string_hashN(inUtf, inUtfLen);
	
	/* Try to locate the string first. */
	strings = inStringPool->strings;
	firstFree = -1;
	result = NULL;
	for (i = 0, n = strings->length; i < n; i++)
	{
		/* There might be an element here. */
		possible = strings->elements[i];
		
		/* Check to see if this no longer exists in the pool. */
		weak = NULL;
		if (possible != NULL)
			if (sjme_error_is(error = sjme_alloc_weakRefGet(possible,
				&weak)))
			{
				/* Not a valid error here. */
				if (error != SJME_ERROR_NOT_WEAK_REFERENCE)
					goto fail_corruptPossible;
			}
		
		/* Is a weak reference but does not actually point to the string? */
		/* If so then this was freed! */
		if (weak != NULL && weak->pointer != possible)
		{
			strings->elements[i] = NULL;
			possible = NULL;
		}
		
		/* Is this a filled slot? */
		if (possible == NULL)
		{
			/* We can put a new string here. */
			if (firstFree < 0)
				firstFree = i;
			continue;
		}
		
		/* If hash or length differ, not a possible match */
		if (possible->hashCode != hash || possible->length != inUtfLen)
			continue;
		
		/* Must be exactly the same! */
		if (0 == memcmp(&possible->chars[0], inUtf, inUtfLen))
		{
			result = possible;
			break;
		}
	}
	
	/* String is not in the pool. */
	if (result == NULL)
	{
		/* Need to make the pool bigger? */
		if (firstFree < 0)
		{
			/* First free is always at the end. */
			firstFree = strings->length;
			
			/* Reallocate the list. */
			oldStrings = strings;
			if (sjme_error_is(error = sjme_list_copy(inStringPool->inPool,
				strings->length + SJME_STRING_POOL_GROW, strings,
				&strings, sjme_stringPool_string, 0)) || strings == NULL)
				goto fail_growList;
			
			/* Set new list. */
			inStringPool->strings = strings;
			
			/* Clear old list. */
			if (sjme_error_is(error = sjme_alloc_free(oldStrings)))
				goto fail_freeOld;
			oldStrings = NULL;
		}
		
		/* Allocate new result to store in the slot. */
		result = NULL;
#if defined(SJME_CONFIG_DEBUG)
		if (sjme_error_is(error = sjme_nvm_allocR(inStringPool->inPool,
			sizeof(*result) + inUtfLen, 
			SJME_NVM_STRUCT_STRING_POOL_STRING,
			SJME_AS_NVM_COMMONP(&result), file, line, func)) ||
			result == NULL)
#else
		if (sjme_error_is(error = sjme_nvm_alloc(inStringPool->inPool,
			sizeof(*result) + inUtfLen, 
			SJME_NVM_STRUCT_STRING_POOL_STRING,
			SJME_AS_NVM_COMMONP(&result))) || result == NULL)
#endif
			goto fail_stringAlloc;
		
		/* Fill in information. */
		memmove(&result->chars[0], inUtf, inUtfLen);
		result->hashCode = hash;
		result->length = inUtfLen;
		result->seq.context = &result->chars[0];
		
		/* Count up as the pool itself references it. */
		if (sjme_error_is(error = sjme_alloc_weakRef(
			result, NULL)))
			goto fail_countUp;
		
		/* Store it into the pool. */
		strings->elements[firstFree] = result;
	}
	
	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inStringPool->common.lock, NULL)))
		goto fail_releaseLock;
	
	/* Success! */
	*outString = result;
	return SJME_ERROR_NONE;

fail_countUp:
fail_initCommon:
fail_stringAlloc:
	if (result != NULL)
		sjme_alloc_free(result);
fail_freeOld:
fail_growList:
fail_corruptPossible:
fail_releaseLock:
	/* Unlock before failing. */
	sjme_thread_spinLockRelease(&inStringPool->common.lock,
		NULL);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_stringPool_new(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_stringPool* outStringPool)
{
	sjme_errorCode error;
	sjme_stringPool result;
	sjme_list_sjme_stringPool_string* strings;
	
	if (inPool == NULL || outStringPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Make sure we have the memory to store the buffer. */
	strings = NULL;
	if (sjme_error_is(error = sjme_list_alloc(
		inPool, SJME_STRING_POOL_GROW,
		&strings, sjme_stringPool_string, 0)) || strings == NULL)
		goto fail_allocList;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inPool,
		sizeof(*result), SJME_NVM_STRUCT_STRING_POOL,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;
	
	/* Setup fields. */
	result->inPool = inPool;
	result->strings = strings;
	
	/* Success! */
	*outStringPool = result;
	return SJME_ERROR_NONE;
	
fail_initCommon:
fail_allocResult:
	if (result != NULL)
		sjme_alloc_free(result);
fail_allocList:
	if (strings != NULL)
		sjme_alloc_free(strings);
	return sjme_error_default(error);
}
