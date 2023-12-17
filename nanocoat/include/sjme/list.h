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
