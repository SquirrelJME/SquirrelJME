/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Common emulator defines and functions.
 * 
 * @since 2023/12/28
 */

#ifndef SQUIRRELJME_COMMON_H
#define SQUIRRELJME_COMMON_H

#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_COMMON_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Cache storage for virtual suites.
 *
 * @since 2023/12/15
 */
typedef struct sjme_jni_virtualSuite_cache
{
	/** Todo. */
	sjme_jint todo;
} sjme_jni_virtualSuite_cache;

/**
 * Caches for libraries.
 *
 * @since 2023/12/29
 */
typedef struct sjme_jni_virtualLibrary_cache
{
	/** Todo. */
	sjme_jint todo;
} sjme_jni_virtualLibrary_cache;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_COMMON_H
}
		#undef SJME_CXX_SQUIRRELJME_COMMON_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_COMMON_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_COMMON_H */
