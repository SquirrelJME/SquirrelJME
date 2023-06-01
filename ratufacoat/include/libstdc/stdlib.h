/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Standard C Library.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_STDLIB_H
#define SQUIRRELJME_STDLIB_H

#include <stddef.h>

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_STDLIB_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if !defined(NULL)
	/** Null pointer. */
	#define NULL ((void*)0)
#endif

#if !defined(EXIT_FAILURE)
	/** Indicates a failure on exit. */
	#define EXIT_FAILURE 1
#endif

/**
 * Allocates multiple elements and clearing the data used.
 *
 * @param numElems The number of elements to allocate.
 * @param elemSize The size of each element.
 * @return The allocated pointer.
 * @since 2022/12/26
 */
void* calloc(size_t numElems, size_t elemSize);

/**
 * Exits the system.
 *
 * @param status Exit status.
 * @since 2022/12/26
 */
void exit(int status);

/**
 * Frees memory.
 *
 * @param ptr The pointer to free.
 * @since 2022/12/26
 */
void free(void* ptr);

/**
 * Allocates memory.
 *
 * @param size The number of bytes to allocate.
 * @return The allocated memory or @c NULL if not enough memory.
 * @since 2022/12/26
 */
void* malloc(size_t size);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_STDLIB_H
}
		#undef SJME_CXX_SQUIRRELJME_STDLIB_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_STDLIB_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STDLIB_H */
