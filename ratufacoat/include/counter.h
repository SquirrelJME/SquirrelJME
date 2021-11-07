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
#include "atomic.h"

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
	sjme_atomicInt count;
	
	/** The collector if this is to be GCed. */
	sjme_counterCollectFunction collect;
	
	/** The data pointer for collection. */
	void* collectData;
};

/**
 * Counts down the counter, if it reaches zero it will be collected.
 * 
 * @param counter The counter to count.
 * @param outActive Output to if the value is still valid, may be {@code NULL}.
 * @param error The error state.
 * @return If counting down was successful, or when collection was successful
 * when this reached zero, 
 * @since 2021/11/07
 */
sjme_jboolean sjme_counterDown(sjme_counter* counter, sjme_jboolean* outActive,
	sjme_error* error); 

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
