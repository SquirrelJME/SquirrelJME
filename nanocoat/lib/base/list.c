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
	sjme_pointer outList;
} sjme_list_newData;

static sjme_errorCode sjme_list_newInit(
	sjme_attrOutNotNull sjme_list_newData* newData,
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint rootElementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicTypeId,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrInPositive sjme_jint extraFill)
{
	sjme_errorCode error;
	sjme_list(sjme_jint)* fakeList;

	if (allocPool == NULL || newData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (elementSize <= 0 || elementOffset <= 0 || pointerCheck <= 0 ||
		basicTypeId < 0 || basicTypeId >= SJME_NUM_BASIC_TYPE_IDS ||
		numPointerStars < 0 || length < 0 || rootElementSize <= 0 ||
		extraFill < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Contains root elements only? */
	if (numPointerStars == 0)
		newData->allocSize = elementOffset + (rootElementSize * length) +
			extraFill;

	/* Is a list of pointer types. */
	else
		newData->allocSize = elementOffset + (elementSize * length) +
			extraFill;

	/* Underflow? */
	if (newData->allocSize <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Allocate list. */
	if (sjme_error_is(error = sjme_alloc(allocPool, newData->allocSize,
		&newData->outList)) || newData->outList == NULL)
		return sjme_error_default(error);

	/* Store list length. */
	fakeList = (sjme_list(sjme_jint)*)newData->outList;
	fakeList->length = length;
	fakeList->elementSize = elementSize;
	fakeList->elementOffset = elementOffset;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_list_allocR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInPositive sjme_jint inLength,
	sjme_attrOutNotNull sjme_pointer* outList,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_pointer result;
	sjme_jint size;
	
	if (allocPool == NULL || outList == NULL)
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
#if defined(SJME_CONFIG_DEBUG)
	if (sjme_error_is(error = sjme_allocR(allocPool, size,
		&result SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)) ||
		result == NULL)
#else
	if (sjme_error_is(error = sjme_alloc(allocPool, size, &result)) ||
		result == NULL)
#endif
		return sjme_error_default(error);

	/* Perform direct list initialization. */
	sjme_list_directInitR(inLength, result, elementSize, elementOffset,
		pointerCheck);
	
	/* Give the result! */
	*outList = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_list_copyR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInPositive sjme_jint inNewLength,
	sjme_attrInNotNull sjme_pointer inOldList,
	sjme_attrOutNotNull sjme_pointer* outNewList,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_jint i, limit;
	sjme_list(sjme_jint)* fakeOld;
	sjme_list(sjme_jint)* fakeNew;
	
	if (allocPool == NULL || inOldList == NULL || outNewList == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inNewLength < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Map fake. */
	fakeOld = (sjme_list(sjme_jint)*)inOldList;
	
	/* Wrong element size and/or offset? Might be different types. */
	if (fakeOld->elementSize != elementSize ||
		fakeOld->elementOffset != elementOffset)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Allocate new list first. */
	if (sjme_error_is(error = sjme_list_allocR(allocPool,
		inNewLength, (sjme_pointer*)&fakeNew, elementSize,
		elementOffset, pointerCheck
		SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)))
		return sjme_error_default(error);
	
	/* Copy over elements with direct memory copy. */
	limit = (fakeOld->length < inNewLength ? fakeOld->length : inNewLength);
	memmove((sjme_pointer)(((sjme_intPointer)fakeNew) + elementOffset),
		(sjme_pointer)(((sjme_intPointer)fakeOld) + elementOffset),
		elementSize * limit);
	
	/* Success! */
	*outNewList = fakeNew;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_list_directInitR(
	sjme_attrInPositive sjme_jint inLength,
	sjme_attrOutNotNull sjme_pointer outList,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck)
{
	sjme_jint size;
	sjme_list(sjme_jint)* fakeList;
	
	if (outList == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLength < 0 || elementSize <= 0 || elementOffset <= 0 ||
		pointerCheck <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Calculate the size of the list. */
	size = elementOffset + (elementSize * inLength);
	if (size <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Seed with a fake list. */
	fakeList = outList;
	
	/* Set sizes of the resultant list. */
	fakeList->length = inLength;
	fakeList->elementSize = elementSize;
	fakeList->elementOffset = elementOffset;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_list_flattenArgCV(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outList,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNotNull sjme_lpcstr* argV)
{
	sjme_errorCode error;
	sjme_list_newData newData;
	sjme_list(sjme_lpstr)* result;
	sjme_jint extraFill, i, len;
	sjme_lpcstr arg;
	sjme_pointer destPtr;

	if (allocPool == NULL || outList == NULL || argV == NULL)
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

		/* Determine length of string. */
		/* Use normal string length because we treat everything as a char */
		/* and we do not want to handle modified UTF-8. */
		/* Count string along with NUL terminator. */
		extraFill += strlen(arg) + 1;
	}

	/* Overflow? */
	if (extraFill < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Common initialization of new lists. */
	error = SJME_ERROR_UNKNOWN;
	memset(&newData, 0, sizeof(newData));
	if (sjme_error_is(error = sjme_list_newInit(&newData,
		allocPool, sizeof(sjme_lpstr),
		sizeof(sjme_lpstr),
		offsetof(sjme_list(sjme_lpstr), elements), 4,
		SJME_BASIC_TYPE_ID_OBJECT, 1, argC,
		extraFill)))
		return sjme_error_default(error);

	/* Map result. */
	result = (sjme_list(sjme_lpstr)*)newData.outList;

	/* The destination pointer is at the very end of the element set. */
	destPtr = &result->elements[argC];

	/* Copy strings into sub-splices. */
	for (i = 0; i < argC; i++)
	{
		/* Get next, skip any NULLs. */
		arg = argV[i];
		if (arg == NULL)
			continue;

		/* Element points to this address. */
		result->elements[i] = destPtr;

		/* Copy entire string chunk here. */
		len = sjme_util_sizeToInt(strlen(arg) + 1);
		memmove(destPtr, arg, len);

		/* Move up pointer. */
		destPtr = (sjme_pointer)(((intptr_t)destPtr) + len);
	}

	/* Output resultant list. */
	*outList = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_list_flattenArgNul(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outList,
	sjme_attrInNotNull sjme_lpcstr inNulString)
{
	sjme_errorCode error;
	sjme_jint count, i, allocLen;
	sjme_lpcstr at;
	sjme_lpcstr* argV;
	
	if (allocPool == NULL || outList == NULL || inNulString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Determine the number of strings within. */
	count = 0;
	for (at = inNulString; *at != '\0'; at += strlen(at) + 1)
		count++;
	
	/* Allocate. */
	allocLen = sizeof(*argV) * (count + 1);
	argV = sjme_alloca(allocLen);
	if (argV == NULL)
		return sjme_error_outOfMemory(allocPool, 0);
	memset(argV, 0, allocLen);
	
	/* Allocate temporary argument set. */
	i = 0;
	for (at = inNulString; *at != '\0' && i < count;
		at += strlen(at) + 1, i++)
		argV[i] = at;
	
	/* Perform the flattening. */
	if (sjme_error_is(error = sjme_list_flattenArgCV(allocPool,
		(sjme_list(sjme_lpstr)**)outList, count, argV)))
		goto fail_flatten;
	
	/* Cleanup. */
	sjme_alloca_free(argV);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_flatten:
	if (argV != NULL)
		sjme_alloca_free(argV);
	return sjme_error_default(error);
}

static sjme_jint sjme_list_injectGrowComparator(sjme_cpointer a,
	sjme_cpointer b, int elementSize)
{
	if (a == NULL || b == NULL || elementSize <= 0)
		return -1;

	return memcmp(a, b, elementSize);
}

sjme_errorCode sjme_list_injectGrowR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInPositive sjme_jint growSize,
	sjme_attrInOutNotNull sjme_pointer* inOutList,
	sjme_attrInNotNull sjme_pointer injectValuePtr,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_jint freeSlot, n;
	sjme_jubyte* nothing;

	if (allocPool == NULL || inOutList == NULL || injectValuePtr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (elementSize <= 0 || elementOffset < 0 || growSize <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* There is no list here? */
	freeSlot = -1;
	n = 0;
	if ((*inOutList) == NULL)
		goto skip_grow;

	/* Allocate nothing to compare against. */
	nothing = sjme_alloca(elementSize);
	if (nothing == NULL)
		return sjme_error_outOfMemory(NULL, elementSize);
	memset(nothing, 0, elementSize);

	/* Find the first free slot. */
	n = ((sjme_list_void*)(*inOutList))->length;
	if (sjme_error_is(error = sjme_list_search(*inOutList,
		sjme_list_injectGrowComparator, &nothing, &freeSlot)))
		goto fail_findFirst;

	/* No free slot found, grow the list. */
skip_grow:
	if (freeSlot < 0)
	{
		/* Grow the list. */
		if (sjme_error_is(error = sjme_list_replaceR(allocPool,
			n + growSize,
			inOutList, elementSize, elementOffset, pointerCheck
			SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY)))
			goto fail_replace;

		/* The free slot is at the end. */
		freeSlot = n;
	}

	/* Store here. */
skip_store:
	memmove(SJME_POINTER_OFFSET((*inOutList),
		elementOffset + (elementSize * freeSlot)),
		injectValuePtr, elementSize);
	
	/* Cleanup. */
	sjme_alloca_free(nothing);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_replace:
fail_findFirst:
	if (nothing != NULL)
		sjme_alloca_free(nothing);
	return sjme_error_default(error);
}

sjme_errorCode sjme_list_newAR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint rootElementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicTypeId,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull sjme_pointer* outList,
	sjme_attrInNotNull sjme_pointer inElements)
{
	sjme_errorCode error;
	sjme_list_newData newData;
	sjme_jint at, toOff, fromOff;
	sjme_pointer toPtr;
	sjme_pointer fromPtr;

	/* Common initialization of new lists. */
	error = SJME_ERROR_UNKNOWN;
	memset(&newData, 0, sizeof(newData));
	if (sjme_error_is(error = sjme_list_newInit(&newData,
		allocPool, elementSize, rootElementSize, elementOffset, pointerCheck,
		basicTypeId, numPointerStars, length, 0)))
		return sjme_error_default(error);

	/* Because the input is a sequential "array", we can just copy it all. */
	/* This can turn out to be a very fast operation. */
	memmove((sjme_pointer)(((intptr_t)newData.outList) + elementOffset),
		inElements, elementSize * length);

	/* Return resultant list. */
	*outList = newData.outList;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_list_newVR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint rootElementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicTypeId,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull sjme_pointer* outList,
	...)
{
	va_list list;
	sjme_errorCode result;

	va_start(list, outList);

	result = sjme_list_newVAR(allocPool, elementSize, rootElementSize,
		elementOffset, pointerCheck, basicTypeId, numPointerStars,
		length, outList, list);

	va_end(list);

	return result;
}

sjme_errorCode sjme_list_newVAR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint rootElementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicTypeId,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull sjme_pointer* outList,
	va_list elements)
{
#define SJME_BLA(basicType, cType, pType) \
	case SJME_TOKEN_PASTE_PP(SJME_BASIC_TYPE_ID_, basicType): \
		*((cType*)atPtr) = (cType)va_arg(elements, pType); \
		break

	sjme_errorCode error;
	sjme_list_newData newData;
	sjme_jint at, off;
	sjme_pointer atPtr;

	/* Common initialization of new lists. */
	error = SJME_ERROR_UNKNOWN;
	memset(&newData, 0, sizeof(newData));
	if (sjme_error_is(error = sjme_list_newInit(&newData,
		allocPool, elementSize, rootElementSize, elementOffset, pointerCheck,
		basicTypeId, numPointerStars, length, 0)))
		return sjme_error_default(error);

	/* Store elements from variadic arguments. */
	for (at = 0, off = elementOffset; at < length; at++, off += elementSize)
	{
		/* Calculate the base pointer address. */
		atPtr = (sjme_pointer)(((intptr_t)newData.outList) + off);

		/* Read in depending on the basic type. */
		switch (basicTypeId)
		{
			SJME_BLA(BOOLEAN, sjme_jboolean, int);
			SJME_BLA(BYTE, sjme_jbyte, int);
			SJME_BLA(SHORT, sjme_jshort, int);
			SJME_BLA(CHARACTER, sjme_jchar, int);
			SJME_BLA(INTEGER, sjme_jint, sjme_jint);
			SJME_BLA(LONG, sjme_jlongNative, sjme_jlongNative);
			
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
			SJME_BLA(FLOAT, sjme_jfloatNative, sjme_jfloatNative);
#endif
			
#if defined(SJME_CONFIG_HAS_DOUBLE_HARD)
			SJME_BLA(DOUBLE, sjme_jdoubleNative, sjme_jdoubleNative);
#endif
			
			SJME_BLA(OBJECT, sjme_pointer, sjme_pointer);

				/* Type not implemented. */
			default:
				sjme_todo("Implement: %d", basicTypeId);
				return sjme_error_notImplemented(0);
		}
	}

	/* Return resultant list. */
	*outList = newData.outList;
	return SJME_ERROR_NONE;
#undef SJME_BLA
}

sjme_errorCode sjme_list_replaceR(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInPositive sjme_jint inNewLength,
	sjme_attrInOutNotNull sjme_pointer* inOutList,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck
	SJME_DEBUG_ONLY_COMMA SJME_DEBUG_DECL_FILE_LINE_FUNC_OPTIONAL)
{
	sjme_errorCode error;
	sjme_pointer newList, oldList;
	
	if (allocPool == NULL || inOutList == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate new list? */
	oldList = *inOutList;
	if (oldList == NULL)
		return sjme_list_allocR(allocPool, inNewLength, inOutList,
			elementSize, elementOffset, pointerCheck
			SJME_DEBUG_ONLY_COMMA SJME_DEBUG_FILE_LINE_COPY);

	/* Allocate new list as a copy. */
	newList = NULL;
	if (sjme_error_is(error = sjme_list_copyR(allocPool, inNewLength,
		oldList, &newList, elementSize,
		elementOffset, pointerCheck
		SJME_DEBUG_ONLY_COMMA
		SJME_DEBUG_FILE_LINE_COPY)) || newList == NULL)
		return sjme_error_default(error);

	/* Set new list and free old one. */
	*inOutList = newList;
	return sjme_alloc_free(oldList);
}

sjme_errorCode sjme_list_search(
	sjme_attrInNotNull sjme_pointer inList,
	sjme_attrInNotNull sjme_comparator comparator,
	sjme_attrInNotNull sjme_cpointer findWhat,
	sjme_attrOutNotNull sjme_jint* outIndex)
{
	sjme_list_void* list;
	sjme_jint i, n;
	sjme_pointer base;

	if (inList == NULL || comparator == NULL || findWhat == NULL ||
		outIndex == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Look in a void list. */
	list = (sjme_list_void*)inList;

	/* Go through the entire list. */
	base = SJME_POINTER_OFFSET(list, list->elementOffset);
	for (i = 0, n = list->length; i < n;
		i++, base = SJME_POINTER_OFFSET(base, list->elementSize))
		if (0 == comparator(base, findWhat, list->elementSize))
		{
			*outIndex = i;
			return SJME_ERROR_NONE;
		}

	/* Not found. */
	*outIndex = -1;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_list_searchBinary(
	sjme_attrInNotNull sjme_pointer inList,
	sjme_attrInNotNull sjme_comparator comparator,
	sjme_attrInNotNull sjme_cpointer findWhat,
	sjme_attrOutNotNull sjme_jint* outIndex)
{
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_list_searchReverse(
	sjme_attrInNotNull sjme_pointer inList,
	sjme_attrInNotNull sjme_comparator comparator,
	sjme_attrInNotNull sjme_cpointer findWhat,
	sjme_attrOutNotNull sjme_jint* outIndex)
{
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_list_sort(
	sjme_attrInNotNull sjme_pointer inList,
	sjme_attrInNotNull sjme_comparator comparator)
{
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}
