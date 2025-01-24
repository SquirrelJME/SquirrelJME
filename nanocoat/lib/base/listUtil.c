/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/listUtil.h"

sjme_errorCode sjme_listUtil_binListInt(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_jint** outList,
	sjme_attrInNotNull sjme_stream_input inputStream)
{
	sjme_errorCode error;
	sjme_jint length, i;
	sjme_list_sjme_jint* result;
	
	if (inPool == NULL || outList == NULL || inputStream == NULL)
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
	if (sjme_error_is(error = sjme_list_alloc(inPool,
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
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outList,
	sjme_attrInNotNull sjme_stream_input inputStream)
{
	sjme_errorCode error;
	sjme_jint length, i, actual;
	sjme_jchar utfLen;
	sjme_lpstr* values;
	
	if (inPool == NULL || outList == NULL || inputStream == NULL)
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
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(values, 0, sizeof(*values) * length);
	
	/* Read in all UTF values. */
	for (i = 0; i < length; i++)
	{
		/* Read in UTF sequence length. */
		utfLen = 0;
		if (sjme_error_is(error = sjme_stream_inputReadValueJS(
			inputStream, (sjme_jshort*)&utfLen)))
			return sjme_error_default(error);
		
		/* Allocate buffer for string. */
		values[i] = sjme_alloca(utfLen + 1);
		if (values[i] == NULL)
			return SJME_ERROR_OUT_OF_MEMORY;
		memset(values[i], 0, utfLen + 1);
		
		/* Read in. */
		actual = INT32_MAX;
		if (sjme_error_is(error = sjme_stream_inputReadFully(
			inputStream, &actual,
			values[i], utfLen)))
			return sjme_error_default(error);
		
		/* Wrong count? */
		if (actual != utfLen)
			return SJME_ERROR_END_OF_FILE;
	}
	
	/* Setup list. */
	return sjme_list_flattenArgCV(inPool, outList,
		length, (sjme_lpcstr*)values);
}
