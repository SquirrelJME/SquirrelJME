/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Direct memory functions.
 * 
 * @since 2023/01/01
 */

#ifndef SQUIRRELJME_MEMDIRECT_H
#define SQUIRRELJME_MEMDIRECT_H

#include "sjmejni/sjmejni.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MEMDIRECT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Frees memory that was directly allocated through the tagging system.
 *
 * @param inPtr The pointer to free.
 * @param error If free was not successful.
 * @return If freeing was successful.
 * @since 2022/12/27
 */
sjme_jboolean sjme_memDirectFree(void** inPtr, sjme_error* error);

/**
 * Allocates memory directly, not using the tagging system.
 *
 * @param outPtr The output pointer.
 * @param size The size to allocate.
 * @param error The error if allocation failed.
 * @return If the allocation succeeded or not.
 * @since 2022/12/20
 */
sjme_jboolean sjme_memDirectNew(void** outPtr, sjme_jsize size,
	sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MEMDIRECT_H
}
		#undef SJME_CXX_SQUIRRELJME_MEMDIRECT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMDIRECT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMDIRECT_H */
