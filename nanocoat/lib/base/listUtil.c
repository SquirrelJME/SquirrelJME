/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/listUtil.h"

sjme_errorCode sjme_listUtil_binListInt(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_list(sjme_jint)** outList,
	sjme_attrInNotNull sjme_stream_input inputStream)
{
	sjme_errorCode error;
	sjme_jint length, i;
	sjme_list(sjme_jint)* result;
	
	if (allocPool == NULL || outList == NULL || inputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in length. */
	length = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadValueJI(
		inputStream, &length)) ||
		length == INT32_MAX)
		return sjme_error_default(error);
	
	/* Not valid? */
	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Setup target list. */
	result = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		length, &result, sjme_jint, 0)) || result == NULL)
		goto fail_allocList;
	
	/* Read in all values. */
	for (i = 0; i < length; i++)
		if (sjme_error_is(error = sjme_stream_inputReadValueJI(
			inputStream, &result->elements[i])))
			goto fail_readValue;
		
	/* Success! */
	*outList = result;
	return SJME_ERROR_NONE;

fail_readValue:
fail_allocList:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_listUtil_binListUtf(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outList,
	sjme_attrInNotNull sjme_stream_input inputStream)
{
	sjme_errorCode error;
	sjme_jint length, i, actual;
	sjme_jchar utfLen;
	sjme_lpstr* values;
	
	if (allocPool == NULL || outList == NULL || inputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in length. */
	length = INT32_MAX;
	if (sjme_error_is(error = sjme_stream_inputReadValueJI(
		inputStream, &length)) ||
		length == INT32_MAX)
		return sjme_error_default(error);
	
	/* Not valid? */
	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Allocate target values for later list creation. */
	values = sjme_alloca(sizeof(*values) * length);
	if (values == NULL)
		return sjme_error_outOfMemory(NULL, sizeof(*values) * length);
	memset(values, 0, sizeof(*values) * length);
	
	/* Read in all UTF values. */
	for (i = 0; i < length; i++)
	{
		/* Read in UTF sequence length. */
		utfLen = 0;
		if (sjme_error_is(error = sjme_stream_inputReadValueJS(
			inputStream, (sjme_jshort*)&utfLen)))
			goto fail_readValue;
		
		/* Allocate buffer for string. */
		values[i] = sjme_alloca(utfLen + 1);
		if (values[i] == NULL)
		{
			error = sjme_error_outOfMemory(NULL, utfLen + 1);
			goto fail_lowerOom;
		}
		memset(values[i], 0, utfLen + 1);
		
		/* Read in. */
		actual = INT32_MAX;
		if (sjme_error_is(error = sjme_stream_inputReadFully(
			inputStream, &actual,
			values[i], utfLen)))
			goto fail_readFully;
		
		/* Wrong count? */
		if (actual != utfLen)
		{
			error = SJME_ERROR_END_OF_FILE;
			goto fail_eof;
		}
	}
	
	/* Setup list. */
	if (sjme_error_is(error = sjme_list_flattenArgCV(allocPool, outList,
		length, (sjme_lpcstr*)values)))
		goto fail_flatten;
	
	/* Cleanup. */
	for (i = 0; i < length; i++)
		if (values[i] != NULL)
			sjme_alloca_free(values[i]);
	sjme_alloca_free(values);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_flatten:
fail_eof:
fail_readFully:
fail_lowerOom:
fail_readValue:
	if (values != NULL)
	{
		for (i = 0; i < length; i++)
			if (values[i] != NULL)
			{
				sjme_alloca_free(values[i]);
				values[i] = NULL;
			}
		sjme_alloca_free(values);
	}
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_listUtil_findFree(
	sjme_attrInNotNull sjme_list(sjme_pointer)* inList,
	sjme_attrOutNotNull sjme_jint* outFreeSlot)
{
	sjme_jint i, n;
	
	if (inList == NULL || outFreeSlot == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Was a free slot passed and is still free? */
	i = *outFreeSlot;
	n = inList->length;
	if (i < 0 || i >= n || inList->elements[i] != NULL)
		i = -1;
	
	/* Did not have to do anything? */
	if (i >= 0)
	{
		*outFreeSlot = i;
		return SJME_ERROR_NONE;
	}
	
	/* Find free item. */
	for (i = 0; i < n; i++)
		if (inList->elements[i] == NULL)
		{
			*outFreeSlot = i;
			return SJME_ERROR_NONE;
		}
	
	/* None found. */
	*outFreeSlot = -1;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_listUtil_findItemWeak(
	sjme_attrInNotNull sjme_list(sjme_pointer)* inList,
	sjme_attrOutNotNull sjme_jint* outFreeSlot,
	sjme_attrOutNotNull sjme_pointer* outFound,
	sjme_attrInNotNull sjme_listUtil_findItemCompareFunc compareFunc,
	sjme_attrInValue sjme_jint againstI,
	sjme_attrInValue sjme_pointer againstP)
{
	sjme_errorCode error;
	sjme_jint i, n, freeSlot;
	sjme_pointer maybe;
	sjme_alloc_weak weak;
	
	if (inList == NULL || outFreeSlot == NULL || outFound == NULL ||
		compareFunc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Search through the list of weakly referenced items. */
	freeSlot = -1;
	for (i = 0, n = inList->length; i < n; i++)
	{
		/* Skip nulls. */
		maybe = inList->elements[i];
		if (maybe == NULL)
		{
			/* Capable free slot. */
			if (freeSlot < 0)
				freeSlot = i;
			continue;
		}
		
		/* Check to see if this is still valid. */
		weak = NULL;
		if (sjme_error_is(error = sjme_alloc_weakRefGet(maybe,
			&weak)) || weak == NULL)
		{
			/* If not a weak reference, it is considered as no longer valid */
			/* but this is not an actual error case. */
			if (error == SJME_ERROR_NOT_WEAK_REFERENCE)
			{
				error = SJME_ERROR_NONE;
				inList->elements[i] = NULL;
				
				/* Capable free slot. */
				if (freeSlot < 0)
					freeSlot = i;
				continue;
			}
			
			/* Not good! Corruption or otherwise? */
			return sjme_error_default(error);
		}
		
		/* Could it be this one? */
		if (sjme_error_is(error = compareFunc(inList,
			i, maybe, againstI, againstP)))
		{
			/* Try again. */
			if (error == SJME_ERROR_NOT_MATCHED)
				continue;
			
			/* Fail. */
			return sjme_error_default(error);
		}
		
		/* Item was found! */
		*outFreeSlot = -1;
		*outFound = maybe;
		return SJME_ERROR_NONE;
	}
	
	/* Not found. */
	*outFreeSlot = freeSlot;
	*outFound = NULL;
	return SJME_ERROR_NONE;
}
