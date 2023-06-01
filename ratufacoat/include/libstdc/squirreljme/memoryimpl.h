/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Memory implementation.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_MEMORYIMPL_H
#define SQUIRRELJME_MEMORYIMPL_H

#include <stddef.h>

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MEMORYIMPL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Exits the system.
 *
 * @param status Exit status.
 * @since 2022/12/26
 */
void sjme_stdcExit(int status);

/**
 * Frees memory.
 *
 * @param ptr The pointer to free.
 * @since 2022/12/26
 */
void sjme_stdcFree(void* ptr);

/**
 * Allocates memory.
 *
 * @param size The number of bytes to allocate.
 * @return The allocated memory or @c NULL if not enough memory.
 * @since 2022/12/26
 */
void* sjme_stdcMalloc(size_t size);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MEMORYIMPL_H
}
		#undef SJME_CXX_SQUIRRELJME_MEMORYIMPL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMORYIMPL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMORYIMPL_H */
