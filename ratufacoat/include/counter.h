/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Contains a counter implementation for garbage collection.
 * 
 * @since 2021/11/07
 */

#ifndef SQUIRRELJME_COUNTER_H
#define SQUIRRELJME_COUNTER_H

#include "sjmerc.h"
#include "memio/atomic.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_COUNTER_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * A counter which tracks items that can be collected.
 * 
 * @since 2021/11/07
 */
typedef struct sjme_counter sjme_counter;

/**
 * Function to be called for when a counted object is to be collected.
 * 
 * @param counter The counter to be collected.
 * @param error The error state.
 * @return If collection was successful.
 * @since 2021/11/07
 */
typedef sjme_jboolean (*sjme_counterCollectFunction)(sjme_counter* counter,
	sjme_error* error);

/**
 * A counter which tracks items that can be collected.
 * 
 * @since 2021/11/07
 */
struct sjme_counter
{
	/** The current count. */
	sjme_memIo_atomicInt count;
	
	/** The collector if this is to be GCed. */
	sjme_counterCollectFunction collect;
	
	/** A data pointer for collection. */
	void* dataPointer;
	
	/** An integer value for collection. */
	sjme_jint dataInteger;
};

/**
 * Counts down the counter, if it reaches zero it will be collected.
 * 
 * @param counter The counter to count.
 * @param outActive Output to if the value is still valid, may be @c NULL.
 * @param error The error state.
 * @return If counting down was successful, or when collection was successful
 * when this reached zero, 
 * @since 2021/11/07
 */
sjme_jboolean sjme_counterDown(sjme_counter* counter, sjme_jboolean* outActive,
	sjme_error* error);

/**
 * Initializes the given counter with the counter values.
 * 
 * @param counter The counter to initialize.
 * @param collectFunc The collection function to use.
 * @param dataPointer The data pointer, is optional and may be any value.
 * @param dataInteger The data integer, is optional and may be any value.
 * @param error The error state.
 * @return If the counter was successfully incremented.
 * @since 2021/12/16
 */
sjme_jboolean sjme_counterInit(sjme_counter* counter,
	sjme_counterCollectFunction collectFunc,
	void* dataPointer, int dataInteger, sjme_error* error);

/**
 * Increases the reference counter.
 * 
 * @param counter The counter to increment.
 * @param error The error state.
 * @return If the counter was successfully incremented.
 * @since 2021/12/16
 */
sjme_jboolean sjme_counterUp(sjme_counter* counter, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_COUNTER_H
}
#undef SJME_CXX_SQUIRRELJME_COUNTER_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_COUNTER_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_COUNTER_H */
