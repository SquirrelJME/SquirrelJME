/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Reference counting for types.
 * 
 * @since 2024/01/08
 */

#ifndef SQUIRRELJME_REFCOUNT_H
#define SQUIRRELJME_REFCOUNT_H

#include "sjme/nvm.h"
#include "sjme/atomic.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_REFCOUNT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Determines the name of a reference counted value.
 *
 * @param type The type to reference count.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/08
 */
#define SJME_REFCOUNT_NAME(type, numPointerStars) \
	SJME_TOKEN_PASTE_PP(sjme_refcount_, SJME_TOKEN_PASTE_PP(type, \
		SJME_TOKEN_SINGLE(SJME_TOKEN_STARS_C##numPointerStars)))

/**
 * Declares a reference counted value.
 *
 * @param type The type to reference count.
 * @param numPointerStars The number of pointer stars.
 * @since 2024/01/08
 */
#define SJME_REFCOUNT_DECLARE(type, numPointerStars) \
	typedef struct SJME_REFCOUNT_NAME(type, numPointerStars) \
	{ \
		/** The current reference count. */ \
		sjme_atomic_sjme_jint count; \
	\
		/** The value used. */ \
		type value; \
	} SJME_REFCOUNT_NAME(type, numPointerStars);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_REFCOUNT_H
}
		#undef SJME_CXX_SQUIRRELJME_REFCOUNT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_REFCOUNT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_REFCOUNT_H */
