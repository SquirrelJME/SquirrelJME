/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Generic lists.
 * 
 * @since 2023/12/17
 */

#ifndef SQUIRRELJME_LIST_H
#define SQUIRRELJME_LIST_H

#include <stdarg.h>

#include "sjme/nvm.h"
#include "sjme/comparator.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_LIST_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Determines the name of the given list.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2023/12/17
 */
#define SJME_LIST_NAME(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_list_, SJME_TOKEN_PASTE_PP(type, \
		SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars)))

/**
 * Declares a list type.
 *
 * @param type The type to use for the list values.
 * @param numPointerStars The number of pointer stars.
 * @since 2023/12/17
 */
#define SJME_LIST_DECLARE(type, numPointerStars) \
	/** A list of @c type. */ \
	typedef struct SJME_LIST_NAME(type, numPointerStars) \
	{ \
		/** The length of this type. */ \
		sjme_jint length; \
	 \
		/** The element size of this type. */ \
		sjme_jint elementSize; \
	 \
		/** The elements in the list. */ \
		SJME_TOKEN_TYPE(type, numPointerStars) \
			elements[sjme_flexibleArrayCount]; \
	} SJME_LIST_NAME(type, numPointerStars)

/**
 * Calculates the static size of a list.
 *
 * @param type The element type of the list.
 * @param numPointerStars The number of pointer stars.
 * @param count The length of the list.
 * @return The resultant length of the list.
 * @since 2023/12/17
 */
#define SJME_SIZEOF_LIST(type, numPointerStars, count) \
	(sizeof(SJME_LIST_NAME(type, numPointerStars)) + \
	(offsetof(SJME_LIST_NAME(type, numPointerStars), elements) - \
		offsetof(SJME_LIST_NAME(type, numPointerStars), elements)) + \
	(sizeof(SJME_TOKEN_TYPE(type, numPointerStars)) * (size_t)(count)))

/** List of @c sjme_jbyte. */
SJME_LIST_DECLARE(sjme_jbyte, 0);

/** List of @c sjme_jubyte. */
SJME_LIST_DECLARE(sjme_jubyte, 0);

/** List of @c sjme_jshort. */
SJME_LIST_DECLARE(sjme_jshort, 0);

/** List of @c sjme_jchar. */
SJME_LIST_DECLARE(sjme_jchar, 0);

/** List of @c sjme_jint. */
SJME_LIST_DECLARE(sjme_jint, 0);

/** List of @c sjme_jint* . */
SJME_LIST_DECLARE(sjme_jint, 1);

/** List of @c sjme_juint . */
SJME_LIST_DECLARE(sjme_juint, 0);

/** List of @c sjme_lpstr . */
SJME_LIST_DECLARE(sjme_lpstr, 0);

/** List of @c sjme_lpcstr . */
SJME_LIST_DECLARE(sjme_lpcstr, 0);

/** List of @c sjme_jobject . */
SJME_LIST_DECLARE(sjme_jobject, 0);

/** List of @c sjme_pointer . */
SJME_LIST_DECLARE(sjme_pointer, 0);

/** List of @c sjme_cchar . */
SJME_LIST_DECLARE(sjme_cchar, 0);

/** List of @c sjme_pointerLen . */
SJME_LIST_DECLARE(sjme_pointerLen, 0);

/**
 * Allocates a given list generically.
 *
 * @param inPool The pool to allocate within.
 * @param inLength The length of the list.
 * @param outList The output list.
 * @param elementSize The size of the list elements.
 * @param elementOffset The offset of elements in the list.
 * @param pointerCheck A check to see if it is a valid pointer.
 * @return Any resultant error code, if any.
 * @since 2023/12/17
 */
sjme_errorCode sjme_list_allocR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositive sjme_jint inLength,
	sjme_attrOutNotNull void** outList,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck);

/**
 * Allocates the given list without setting any of the values.
 *
 * @param inPool The pool to allocate within.
 * @return Any error state.
 * @since 2023/12/17
 */
#define sjme_list_alloc(inPool, inLength, outList, type, numPointerStars) \
	sjme_list_allocR((inPool), (inLength), \
		(void**)(outList), \
		sizeof(SJME_TOKEN_TYPE(type, numPointerStars)), \
		offsetof(SJME_LIST_NAME(type, numPointerStars), elements), \
		sizeof(**(outList)))

/**
 * Directly initializes a list.
 *
 * @param inLength The length of the list.
 * @param outList The output list.
 * @param elementSize The size of the list elements.
 * @param elementOffset The offset of elements in the list.
 * @param pointerCheck A check to see if it is a valid pointer.
 * @return Any resultant error code, if any.
 * @since 2024/02/21
 */
sjme_errorCode sjme_list_directInitR(
	sjme_attrInPositive sjme_jint inLength,
	sjme_attrOutNotNull void* outList,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck);

/**
 * Directly initializes a list.
 * 
 * @param inLength The input list length.
 * @param outList The resultant list information.
 * @param type The type used in the list.
 * @param numPointerStars The number of pointer stars used.
 * @since 2024/02/21
 */
#define sjme_list_directInit(inLength, outList, type, numPointerStars) \
	sjme_list_directInitR((inLength), \
		(void*)(outList), \
		sizeof(SJME_TOKEN_TYPE(type, numPointerStars)), \
		offsetof(SJME_LIST_NAME(type, numPointerStars), elements), \
		sizeof(*(outList)))

/**
 * Create a new list with the given set of arguments.
 *
 * @param inPool The pool to allocate within.
 * @param elementSize The element size.
 * @param rootElementSize The root element size.
 * @param elementOffset The element offset.
 * @param pointerCheck Pointer check value.
 * @param basicTypeId The type code of the input.
 * @param numPointerStars The number of pointer stars.
 * @param length The length of the list.
 * @param outList The resultant list.
 * @param inElements The list elements.
 * @return Any resultant error code.
 * @since 2023/12/17
 */
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
	sjme_attrInNotNull void* inElements);

/**
 * Create a new list with the given set of arguments.
 *
 * @param inPool The pool to allocate within.
 * @param type The element type.
 * @param numPointerStars The number of pointer stars.
 * @param length The length of the list.
 * @param outList The resultant list.
 * @param inElements The list elements.
 * @return Any resultant error code.
 * @since 2023/12/17
 */
#define sjme_list_newA(inPool, type, numPointerStars, \
	inLength, outList, inElements) \
	sjme_list_newAR((inPool), \
		sizeof(SJME_TOKEN_TYPE(type, numPointerStars)), \
		sizeof(type), \
		offsetof(SJME_LIST_NAME(type, numPointerStars), elements), \
		sizeof(**(outList)), SJME_TYPEOF_BASIC(type), (numPointerStars), \
		(inLength), (void**)(outList), (void*)(inElements))

/**
 * Create a new list with the given set of arguments.
 *
 * @param inPool The pool to allocate within.
 * @param elementSize The element size.
 * @param rootElementSize The root element size.
 * @param elementOffset The element offset.
 * @param pointerCheck Pointer check value.
 * @param basicTypeId The type code of the input.
 * @param numPointerStars The number of pointer stars.
 * @param length The length of the list.
 * @param outList The resultant list.
 * @param ... The list elements.
 * @return Any resultant error code.
 * @since 2023/12/17
 */
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
	...);

/**
 * Create a new list with the given set of arguments.
 *
 * @param inPool The pool to allocate within.
 * @param type The element type.
 * @param numPointerStars The number of pointer stars.
 * @param length The length of the list.
 * @param outList The resultant list.
 * @param ... The list elements.
 * @return Any resultant error code.
 * @since 2023/12/17
 */
#define sjme_list_newV(inPool, type, numPointerStars, \
	inLength, outList, ...) \
	sjme_list_newVR((inPool), \
		sizeof(SJME_TOKEN_TYPE(type, numPointerStars)), \
		sizeof(type), \
		offsetof(SJME_LIST_NAME(type, numPointerStars), elements), \
		sizeof(**(outList)), SJME_TYPEOF_BASIC(type), (numPointerStars), \
		(inLength), (void**)(outList), __VA_ARGS__)

/**
 * Create a new list with the given set of arguments.
 *
 * @param inPool The pool to allocate within.
 * @param elementSize The element size.
 * @param rootElementSize The root element size.
 * @param elementOffset The element offset.
 * @param pointerCheck Pointer check value.
 * @param basicType The type code of the input.
 * @param numPointerStars The number of pointer stars.
 * @param length The length of the list.
 * @param outList The resultant list.
 * @param elements The list elements.
 * @return Any resultant error code.
 * @since 2023/12/17
 */
sjme_errorCode sjme_list_newVAR(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInPositive sjme_jint elementSize,
	sjme_attrInPositive sjme_jint rootElementSize,
	sjme_attrInPositive sjme_jint elementOffset,
	sjme_attrInValue sjme_jint pointerCheck,
	sjme_attrInNotNull sjme_basicTypeId basicType,
	sjme_attrInPositive sjme_jint numPointerStars,
	sjme_attrInPositive sjme_jint length,
	sjme_attrOutNotNull void** outList,
	va_list elements);

/**
 * Create a new list with the given set of arguments.
 *
 * @param inPool The pool to allocate within.
 * @param type The element type.
 * @param numPointerStars The number of pointer stars.
 * @param length The length of the list.
 * @param outList The resultant list.
 * @param elements The list elements.
 * @return Any resultant error code.
 * @since 2023/12/17
 */
#define sjme_list_newVA(inPool, type, numPointerStars, \
	inLength, outList, elements) \
	sjme_list_newVAR((inPool), \
		sizeof(SJME_TOKEN_TYPE(type, numPointerStars)),  \
		sizeof(type), \
		offsetof(SJME_LIST_NAME(type, numPointerStars), elements), \
		sizeof(**(outList)), SJME_TYPEOF_BASIC(type), (numPointerStars), \
		(inLength), (void**)(outList), (elements))

/**
 * Flattens argc/argv style lists into a single allocation where the pointers
 * to the arguments point within the same allocation link. The main purpose of
 * this is to have to not have to handle going through the list to free
 * all the containing pointers accordingly.
 *
 * @param inPool The pool to allocate within.
 * @param outList The output list.
 * @param argC The argument count.
 * @param argV The arguments.
 * @return Any resultant error.
 * @since 2023/12/17
 */
sjme_errorCode sjme_list_flattenArgCV(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpcstr** outList,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNotNull sjme_lpcstr* argV);

/**
 * Flattens a string which is split by NUL (@c aNULbNULcNULNUL ) into a list.
 * 
 * @param inPool The pool to allocate within.
 * @param outList The resultant list.
 * @param inNulString The input string.
 * @return Any resultant error.
 * @since 2024/02/19
 */
sjme_errorCode sjme_list_flattenArgNul(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_sjme_lpcstr** outList,
	sjme_attrInNotNull sjme_lpcstr inNulString);

/**
 * Searches the given list for the given element.
 *
 * @param inList The list to look within.
 * @param comparator The comparator to compare between entries.
 * @param findWhat The element to search for within the list.
 * @param outIndex The resultant output index or @c -1 if not found.
 * @return Any resultant orr, if any.
 * @since 2024/01/03
 */
sjme_errorCode sjme_list_search(
	sjme_attrInNotNull void* inList,
	sjme_attrInNotNull sjme_comparator comparator,
	sjme_attrInNotNull const void* findWhat,
	sjme_attrOutNotNull sjme_jint* outIndex);

/**
 * Binary searches the given list, requires that it is sorted.
 *
 * @param inList The list to search within.
 * @param comparator The comparison to use for entries.
 * @param findWhat The element to search for within the list.
 * @param outIndex The resultant index if found, or
 * will be @begincode (-(insertion point) - 1) @endcode if it was not found
 * in the list.
 * @return Any resultant error, if any.
 * @see sjme_list_searchInsertionPoint
 * @since 2024/01/03
 */
sjme_errorCode sjme_list_searchBinary(
	sjme_attrInNotNull void* inList,
	sjme_attrInNotNull sjme_comparator comparator,
	sjme_attrInNotNull const void* findWhat,
	sjme_attrOutNotNull sjme_jint* outIndex);

/**
 * Reverses the insertion point operation to either map to one or to get
 * the insertion point.
 *
 * @param index The index to map.
 * @return The resultant insertion point.
 * @since 2024/01/03
 */
#define sjme_list_searchInsertionPoint(index) \
	(-(index) - 1)

/**
 * Searches the given list in reverse for the given element.
 *
 * @param inList The list to look within.
 * @param comparator The comparator to compare between entries.
 * @param findWhat The element to search for within the list.
 * @param outIndex The resultant output index or @c -1 if not found.
 * @return Any resultant orr, if any.
 * @since 2024/01/03
 */
sjme_errorCode sjme_list_searchReverse(
	sjme_attrInNotNull void* inList,
	sjme_attrInNotNull sjme_comparator comparator,
	sjme_attrInNotNull const void* findWhat,
	sjme_attrOutNotNull sjme_jint* outIndex);

/**
 * Sorts the elements of the given list.
 *
 * @param inList The list of items to search.
 * @param comparator The comparator to use when comparing entries.
 * @return Any resultant error, if any.
 * @since 2024/01/03
 */
sjme_errorCode sjme_list_sort(
	sjme_attrInNotNull void* inList,
	sjme_attrInNotNull sjme_comparator comparator);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_LIST_H
}
		#undef SJME_CXX_SQUIRRELJME_LIST_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_LIST_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LIST_H */
