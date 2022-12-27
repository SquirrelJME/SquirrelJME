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

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_MEMORY_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

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
 * This is called when a memory pointer is freed and it has a callback.
 *
 * @param memPtr The pointer being freed.
 * @param memNode The memory node that refers to this pointer.
 * @param error Any resultant error.
 * @return If the free operation was a success, if this returns @c sjme_false
 * then the sjme_free() call will be cancelled.
 * @since 2022/12/10
 */
typedef sjme_jboolean (*sjme_freeCallback)(void* memPtr, sjme_memNode* memNode,
	sjme_error* error);

/**
 * Returns the memory node from the given pointer.
 *
 * @param inPtr The pointer to get the node from.
 * @param outNode The output node for the given pointer.
 * @param error If it could not be obtained.
 * @return If the node was successfully obtained.
 * @since 2022/12/10
 */
sjme_jboolean SJME_DEPRECATED(sjme_getMemNode)(void* inPtr,
	sjme_memNode** outNode, sjme_error* error);

/**
 * Allocates the given number of bytes.
 *
 * @param size The number of bytes to allocate or @c NULL if that failed.
 * @param freeCallback The freeCallback that is called on free of the object,
 * when the function sjme_free() is called.
 * @param error The error flag.
 * @since 2022/12/10
 */
void* SJME_DEPRECATED(sjme_mallocGc)(sjme_jint size,
	sjme_freeCallback freeCallback, sjme_error* error);

/**
 * Allocates the given number of bytes.
 *
 * @param size The number of bytes to allocate or @c NULL if that failed.
 * @param error The error flag.
 * @since 2019/06/07
 */
#define sjme_malloc(size, error) sjme_mallocGc(size, NULL, error)

/**
 * Re-allocates the given pointer.
 *
 * @param ptr The pointer to change in size.
 * @param size The number of bytes to allocate or @c NULL if that failed.
 * @param error The error flag.
 * @since 2021/03/08
 */
void* SJME_DEPRECATED(sjme_realloc)(void* ptr, sjme_jint size,
	sjme_error* error);

/**
 * Frees the given pointer, if there is a callback specified in the allocation
 * of the pointer then it will be called first before any actual freeing is
 * performed.
 *
 * @param p The pointer to free.
 * @param error The error flag.
 * @return if freeing was a success.
 * @since 2019/06/07
 */
sjme_jboolean SJME_DEPRECATED(sjme_free)(void* p, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_MEMORY_H
}
#undef SJME_CXX_SQUIRRELJME_MEMORY_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_MEMORY_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MEMORY_H */
