/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/list.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"

sjme_errorCode sjme_list_allocR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositive sjme_jint inLength,
	sjme_attrOutNotNull void** outList,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck)
{
	void* result;
	sjme_errorCode error;
	sjme_jint size;

	if (inPool == NULL || outList == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inLength < 0 || elementSize <= 0 || elementOffset <= 0 ||
		pointerCheck <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Calculate the size of the list. */
	size = elementOffset + (elementSize * inLength);
	if (size <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Forward allocation. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc(inPool, size, &result)) ||
		result == NULL)
		return error;

	/* Set size, it is always at the start. */
	*((sjme_jint*)result) = inLength;

	/* Give the result! */
	*outList = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_list_flattenArgCV(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpcstr** outList,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNotNull sjme_lpcstr* argV)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_list_flattenR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicTypeId,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull void** outList,
	...)
{
	va_list list;
	sjme_errorCode result;

	va_start(list, outList);

	result = sjme_list_flattenVR(inPool, elementSize, elementOffset,
		pointerCheck, basicTypeId, numPointerStars, length, outList,
		list);

	va_end(list);

	return result;
}

sjme_errorCode sjme_list_flattenVR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicTypeId,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull void** outList,
	va_list elements)
{
	sjme_message("sjme_list_flattenVR(%p, %d, %d, %d, %d, %d, %d, %p)",
		inPool, elementSize, elementOffset, pointerCheck,
		basicTypeId, numPointerStars, length, outList);

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}