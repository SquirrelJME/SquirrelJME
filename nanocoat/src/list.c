/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/list.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/util.h"

/**
 * Contains new list data information.
 *
 * @since 2023/12/17
 */
typedef struct sjme_list_newData
{
	/** The allocation size of the list. */
	sjme_jint allocSize;

	/** The resultant list pointer. */
	void* outList;
} sjme_list_newData;

static sjme_errorCode sjme_list_newInit(
	sjme_attrOutNotNull sjme_list_newData* newData,
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint rootElementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicTypeId,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrInPositive sjme_jint extraFill)
{
	if (inPool == NULL || newData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (elementSize <= 0 || elementOffset <= 0 || pointerCheck <= 0 ||
		basicTypeId < 0 || basicTypeId >= SJME_NUM_BASIC_TYPE_IDS ||
		numPointerStars < 0 || length < 0 || rootElementSize <= 0 ||
		extraFill < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Contains root elements only? */
	if (numPointerStars == 0)
		newData->allocSize = elementOffset + (rootElementSize * length);

	/* Is a list of pointer types. */
	else
		newData->allocSize = elementOffset + (elementSize * length);

	/* Underflow? */
	if (newData->allocSize <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

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
	sjme_errorCode error;
	sjme_list_newData newData;
	sjme_jint extraFill, i;
	sjme_lpcstr arg;

	if (inPool == NULL || outList == NULL || argV == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (argC < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Determine the amount of extra fill to store. */
	extraFill = 0;
	for (i = 0; i < argC; i++)
	{
		/* Get next, skip any NULLs. */
		arg = argV[i];
		if (arg == NULL)
			continue;

		/* Count string along with null terminator. */
		extraFill += sjme_stringLength(arg);
	}

	/* Overflow? */
	if (extraFill < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Common initialization of new lists. */
#if 0
	error = SJME_ERROR_UNKNOWN;
	memset(&newData, 0, sizeof(newData));
	if (SJME_IS_ERROR(error = sjme_list_newInit(&newData,
		inPool, elementSize, rootElementSize, elementOffset, pointerCheck,
		basicTypeId, numPointerStars, length, extraFill)))
		return error;
#endif

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_list_newAR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint rootElementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicTypeId,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull void** outList,
	sjme_attrInNotNull void* inElements)
{
	sjme_errorCode error;
	sjme_list_newData newData;
	sjme_jint at;

	/* Common initialization of new lists. */
	error = SJME_ERROR_UNKNOWN;
	memset(&newData, 0, sizeof(newData));
	if (SJME_IS_ERROR(error = sjme_list_newInit(&newData,
		inPool, elementSize, rootElementSize, elementOffset, pointerCheck,
		basicTypeId, numPointerStars, length, 0)))
		return error;

	/* Store list length. */
	(*((sjme_jint*)newData.outList)) = length;

	/* Store elements from array argument. */
	for (at = 0; at < length; at++)
	{
		sjme_todo("Implement this?");
		return SJME_ERROR_NOT_IMPLEMENTED;
	}

	/* Return resultant list. */
	*outList = newData.outList;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_list_newVR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint rootElementSize,
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

	result = sjme_list_newVAR(inPool, elementSize, rootElementSize,
		elementOffset, pointerCheck, basicTypeId, numPointerStars,
		length, outList, list);

	va_end(list);

	return result;
}

sjme_errorCode sjme_list_newVAR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint rootElementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicTypeId,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull void** outList,
	va_list elements)
{
	sjme_errorCode error;
	sjme_list_newData newData;
	sjme_jint at;

	/* Common initialization of new lists. */
	error = SJME_ERROR_UNKNOWN;
	memset(&newData, 0, sizeof(newData));
	if (SJME_IS_ERROR(error = sjme_list_newInit(&newData,
		inPool, elementSize, rootElementSize, elementOffset, pointerCheck,
		basicTypeId, numPointerStars, length, 0)))
		return error;

	/* Store list length. */
	(*((sjme_jint*)newData.outList)) = length;

	/* Store elements from variadic arguments. */
	for (at = 0; at < length; at++)
	{
		sjme_todo("Implement this?");
		return SJME_ERROR_NOT_IMPLEMENTED;
	}

	/* Return resultant list. */
	*outList = newData.outList;
	return SJME_ERROR_NONE;
}