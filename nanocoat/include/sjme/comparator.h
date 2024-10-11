/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Compares values.
 * 
 * @since 2024/01/03
 */

#ifndef SQUIRRELJME_COMPARATOR_H
#define SQUIRRELJME_COMPARATOR_H

#include "sjme/stdTypes.h"
#include "sjme/tokenUtils.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_COMPARATOR_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * A generic comparator.
 *
 * @param a The first item.
 * @param b The second item.
 * @param elementSize The size of each element.
 * @return The resultant comparison, will be zero, negative, or positive.
 * @since 2024/01/03
 */
typedef sjme_jint (*sjme_comparator)(sjme_cpointer a, sjme_cpointer b,
	int elementSize);

/**
 * Determines the name of a given comparator.
 *
 * @param type The type to use.
 * @param numPointerStars The number of pointer stars used.
 * @since 2024/01/03
 */
#define SJME_COMPARATOR(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_comparator_, SJME_TOKEN_PASTE_PP(type, \
	SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars)))

/**
 * Determines the name of a given comparator.
 *
 * @param type The type to use.
 * @param numPointerStars The number of pointer stars used.
 * @since 2024/01/03
 */
#define SJME_COMPARATOR_INSENSITIVE(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_comparator_insensitive_, \
	SJME_TOKEN_PASTE_PP(type, \
	SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars)))

/**
 * Defines a generic comparator, using a simple subtraction.
 *
 * @param type The type to compare.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/03
 */
#define SJME_COMPARATOR_GENERIC(type, numPointerStars) \
	static sjme_inline sjme_attrArtificial sjme_jint \
		SJME_COMPARATOR(type, numPointerStars)( \
		sjme_cpointer a, sjme_cpointer b, int elementSize) \
	{ \
		return (sjme_jint)(*((const type*)b) - *((const type*)a)); \
	}

/** Generic @c sjme_jbyte comparator. */
SJME_COMPARATOR_GENERIC(sjme_jbyte, 0)

/** Generic @c sjme_jubyte comparator. */
SJME_COMPARATOR_GENERIC(sjme_jubyte, 0)

/** Generic @c sjme_jshort comparator. */
SJME_COMPARATOR_GENERIC(sjme_jshort, 0)

/** Generic @c sjme_jchar comparator. */
SJME_COMPARATOR_GENERIC(sjme_jchar, 0)

/** Generic @c sjme_jint comparator. */
SJME_COMPARATOR_GENERIC(sjme_jint, 0)

/** Generic @c sjme_juint comparator. */
SJME_COMPARATOR_GENERIC(sjme_juint, 0)

/** Generic @c sjme_cchar comparator. */
SJME_COMPARATOR_GENERIC(sjme_cchar, 0)

/**
 * Compares two @c sjme_lpcstr .
 *
 * @param a The first item.
 * @param b The second item.
 * @param elementSize The size of each element.
 * @return The resultant comparison, will be zero, negative, or positive.
 * @since 2024/01/03
 */
sjme_jint SJME_COMPARATOR(sjme_lpcstr, 0)(sjme_cpointer a, sjme_cpointer b,
	int elementSize);

/**
 * Compares two @c sjme_lpcstr without regards to case.
 *
 * @param a The first item.
 * @param b The second item.
 * @param elementSize The size of each element.
 * @return The resultant comparison, will be zero, negative, or positive.
 * @since 2024/01/03
 */
sjme_jint SJME_COMPARATOR_INSENSITIVE(sjme_lpcstr, 0)(
	sjme_cpointer a, sjme_cpointer b, int elementSize);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_COMPARATOR_H
}
		#undef SJME_CXX_SQUIRRELJME_COMPARATOR_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_COMPARATOR_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_COMPARATOR_H */
