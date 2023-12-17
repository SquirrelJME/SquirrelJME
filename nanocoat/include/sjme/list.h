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

#include "sjme/nvm.h"

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
 * Determines the element type of the given list.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2023/12/17
 */
#define SJME_LIST_ELEMENT_TYPE(type, numPointerStars) \
	type SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_##numPointerStars)

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
		/** The elements in the list. */ \
		SJME_LIST_ELEMENT_TYPE(type, numPointerStars) \
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
	(sizeof(SJME_LIST_ELEMENT_TYPE(type, numPointerStars)) * (size_t)(count)))

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

/** List of @c sjme_juint. */
SJME_LIST_DECLARE(sjme_juint, 0);

/** List of @c sjme_lpcstr. */
SJME_LIST_DECLARE(sjme_lpcstr, 0);

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
 * Allocates the given list.
 *
 * @param inPool The pool to allocate within.
 * @return Any error state.
 * @since 2023/12/17
 */
#define sjme_list_alloc(inPool, inLength, outList, type, numPointerStars) \
	sjme_list_allocR((inPool), (inLength), \
		(void**)(outList), \
		sizeof(SJME_LIST_ELEMENT_TYPE(type, numPointerStars)), \
		offsetof(SJME_LIST_NAME(type, numPointerStars), elements), \
		sizeof(**(outList)))

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
