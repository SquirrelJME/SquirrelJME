/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/stringPool.h"
#include "sjme/cleanup.h"

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

sjme_errorCode sjme_stringPool_locateStream(
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrOutNotNull sjme_stringPool_string* outString)
{
	if (inStringPool == NULL || inStream == NULL || outString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_stringPool_locateUtf(
	sjme_attrInNotNull sjme_stringPool inStringPool,
	sjme_attrInNotNull sjme_lpcstr inUtf,
	sjme_attrOutNotNull sjme_stringPool_string* outString)
{
	if (inStringPool == NULL || inUtf == NULL || outString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
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
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result), NULL, NULL,
		(sjme_pointer*)&result, NULL)) || result == NULL)
		goto fail_allocResult;
	
	/* Initialize. */
	if (sjme_error_is(error = sjme_nvm_initCommon(
		SJME_AS_NVM_COMMON(result),
		SJME_NVM_STRUCT_STRING_POOL)))
		goto fail_initCommon;
	
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
