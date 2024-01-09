/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Atomic read/write support.
 * 
 * @since 2024/01/08
 */

#ifndef SQUIRRELJME_ATOMIC_H
#define SQUIRRELJME_ATOMIC_H

#if defined(SJME_MEMIO_ATOMIC_C11)
	#include <stdatomic.h>
#endif

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ATOMIC_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Determines the name of an atomic type.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/08
 */
#define SJME_ATOMIC_NAME(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_atomic_, SJME_TOKEN_PASTE_PP(type, \
		SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars)))

/**
 * Determines the name for an atomic function.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @param name The name to use.
 * @since 2024/01/08
 */
#define SJME_ATOMIC_FUNCTION_NAME(type, numPointerStars, name) \
	SJME_TOKEN_PASTE_PP(SJME_ATOMIC_NAME(type, numPointerStars), name)

/**
 * Prototype for the atomic compare and set.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/09
 */
#define SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars) \
	static sjme_inline sjme_attrArtificial \
		sjme_jboolean SJME_ATOMIC_FUNCTION_NAME(type, numPointerStars, \
			_compareSet) \
			(SJME_ATOMIC_NAME(type, numPointerStars)* atomic, \
			SJME_TOKEN_TYPE(type, numPointerStars) expected, \
			SJME_TOKEN_TYPE(type, numPointerStars) set)

/**
 * Prototype for the atomic get then add.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/09
 */
#define SJME_ATOMIC_PROTOTYPE_GET_ADD(type, numPointerStars) \
	static sjme_inline sjme_attrArtificial \
		SJME_TOKEN_TYPE(type, numPointerStars) \
			SJME_ATOMIC_FUNCTION_NAME(type, numPointerStars, \
				_getAdd) \
			(SJME_ATOMIC_NAME(type, numPointerStars)* atomic, \
			SJME_TOKEN_TYPE(type, numPointerStars) add)

#if defined(SJME_CONFIG_HAS_ATOMIC_GCC)
	/** Memory order used for GCC. */
	#define SJME_ATOMIC_GCC_MEMORY_ORDER __ATOMIC_SEQ_CST

#define SJME_ATOMIC_FUNCTION_COMPARE_SET(type, numPointerStars, hasP) \
	SJME_ATOMIC_PROTOTYPE_COMPARE_SET(type, numPointerStars) \
	{ \
		if (__atomic_compare_exchange_n( \
				SJME_TYPEOF_IF_POINTER(type, numPointerStars, \
					(volatile void**))&atomic->value, \
				SJME_TYPEOF_IF_POINTER(type, numPointerStars, \
					(volatile void**)) &expected, \
				set, 0, SJME_ATOMIC_GCC_MEMORY_ORDER, \
					SJME_ATOMIC_GCC_MEMORY_ORDER)) \
			return SJME_JNI_TRUE; \
		return SJME_JNI_FALSE; \
	}

#define SJME_ATOMIC_FUNCTION_GET_ADD(type, numPointerStars, hasP) \
	SJME_ATOMIC_PROTOTYPE_GET_ADD(type, numPointerStars) \
	{ \
		return \
			__atomic_fetch_add(&atomic->value, \
			add, \
			SJME_ATOMIC_GCC_MEMORY_ORDER); \
	}

#else

#error No atomic access functions.

#endif

/**
 * Common atomic function sets.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @param hasP Does this have a pointer?
 * @since 2024/01/09
 */
#define SJME_ATOMIC_FUNCTION_COMMON(type, numPointerStars, hasP) \
	SJME_ATOMIC_FUNCTION_COMPARE_SET(type, numPointerStars, hasP) \
	SJME_TYPEOF_IF_NOT_POINTER(type, numPointerStars, \
		SJME_ATOMIC_FUNCTION_GET_ADD(type, numPointerStars, hasP))

#if defined(SJME_CONFIG_HAS_ATOMIC_C11)

/**
 * Declares an atomic type.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/08
 */
#define SJME_ATOMIC_DECLARE(type, numPointerStars) \
	typedef struct SJME_ATOMIC_NAME(type, numPointerStars) \
	{ \
		/** The atomic type. */ \
		SJME_TOKEN_TYPE(type, numPointerStars) _Atomic value; \
	} SJME_ATOMIC_NAME(type, numPointerStars); \
	SJME_ATOMIC_FUNCTION_COMMON(type, numPointerStars)

#elif defined(SJME_CONFIG_HAS_ATOMIC_WIN32) || \
	defined(SJME_CONFIG_HAS_ATOMIC_GCC) || \
	defined(SJME_CONFIG_HAS_ATOMIC_OLD)

/**
 * Declares an atomic type.
 *
 * @param type The type used.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/08
 */
#define SJME_ATOMIC_DECLARE(type, numPointerStars) \
	typedef struct SJME_ATOMIC_NAME(type, numPointerStars) \
	{ \
		/** The atomic value. */ \
		SJME_TOKEN_TYPE(type, numPointerStars) volatile value; \
	} SJME_ATOMIC_NAME(type, numPointerStars); \
	SJME_ATOMIC_FUNCTION_COMMON(type, numPointerStars, \
		SJME_TOKEN_SINGLE(SJME_TOKEN_HAS_STARS(numPointerStars)))

#else

#error Atomic definitions missing.

#endif

/** Atomic @c sjme_jbyte. */
SJME_ATOMIC_DECLARE(sjme_jbyte, 0);

/** Atomic @c sjme_jubyte. */
SJME_ATOMIC_DECLARE(sjme_jubyte, 0);

/** Atomic @c sjme_jshort. */
SJME_ATOMIC_DECLARE(sjme_jshort, 0);

/** Atomic @c sjme_jchar. */
SJME_ATOMIC_DECLARE(sjme_jchar, 0);

/** Atomic @c sjme_jint. */
SJME_ATOMIC_DECLARE(sjme_jint, 0);

/** Atomic @c sjme_juint. */
SJME_ATOMIC_DECLARE(sjme_juint, 0);

/** Atomic @c sjme_lpstr. */
SJME_ATOMIC_DECLARE(sjme_lpstr, 0); /* NOLINT(*-non-const-parameter) */

/** Atomic @c sjme_lpcstr. */
SJME_ATOMIC_DECLARE(sjme_lpcstr, 0); /* NOLINT(*-non-const-parameter) */

/** Atomic @c sjme_jobject. */
SJME_ATOMIC_DECLARE(sjme_jobject, 0);

/** Atomic @c sjme_pointer. */
SJME_ATOMIC_DECLARE(sjme_pointer, 0);

/** Atomic @c sjme_cchar. */
SJME_ATOMIC_DECLARE(sjme_cchar, 0);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ATOMIC_H
}
		#undef SJME_CXX_SQUIRRELJME_ATOMIC_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ATOMIC_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ATOMIC_H */
