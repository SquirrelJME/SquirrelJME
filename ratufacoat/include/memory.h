/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Native memory functions.
 * 
 * @since 2021/02/28
 */

#ifndef SQUIRRELJME_MEMORY_H
#define SQUIRRELJME_MEMORY_H

#include "sjmerc.h"
#include "atomic.h"
#include "datatype.h"
#include "error.h"

/*--------------------------------------------------------------------------*/

/**
 * This represents a single node within all of the memory that has been
 * allocated and is being managed by SquirrelJME.
 * 
 * @since 2022/02/20
 */
typedef struct sjme_memNode sjme_memNode;

/**
 * Statistics on memory allocation.
 * 
 * @since 2022/02/20
 */
typedef struct sjme_memStat
{
	/** The number of nodes currently allocated. */
	sjme_atomicInt totalNodes;
	
	/** The number of bytes currently allocated. */
	sjme_atomicInt totalBytes;
} sjme_memStat;

/** The current global memory statistics. */
extern sjme_memStat sjme_memStats;

/**
 * Allocates the given number of bytes.
 *
 * @param size The number of bytes to allocate or @c NULL if that failed.
 * @param error The error flag.
 * @since 2019/06/07
 */
void* sjme_malloc(sjme_jint size, sjme_error* error);

/**
 * Re-allocates the given pointer.
 *
 * @param ptr The pointer to change in size.
 * @param size The number of bytes to allocate or @c NULL if that failed.
 * @param error The error flag.
 * @since 2021/03/08
 */
void* sjme_realloc(void* ptr, sjme_jint size, sjme_error* error);

/**
 * Frees the given pointer.
 *
 * @param p The pointer to free.
 * @param error The error flag.
 * @return if freeing was a success.
 * @since 2019/06/07
 */
sjme_jboolean sjme_free(void* p, sjme_error* error);

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_MEMORY_H */
