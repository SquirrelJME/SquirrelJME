/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Threading support.
 * 
 * @since 2023/04/01
 */

#ifndef SQUIRRELJME_THREAD_H
#define SQUIRRELJME_THREAD_H

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_THREAD_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

#include "sjmejni/sjmejni.h"

/*--------------------------------------------------------------------------*/

/** The identity of a thread. */
typedef sjme_jpointer sjme_memIo_threadId;

/** A thread which is not valid. */
#define SJME_MEMIO_INVALID_THREAD_ID ((sjme_memIo_threadId)-1)

/** Alternative invalid thread ID. */
#define SJME_MEMIO_INVALID_THREAD_ID_ALT ((sjme_memIo_threadId)-2)

/**
 * Returns the current thread ID.
 *
 * @return The ID of the current thread.
 * @since 2023/04/01
 */
sjme_memIo_threadId sjme_memIo_threadCurrentId(void);

/**
 * Yields the current thread to let another run.
 *
 * @since 2023/04/01
 */
void sjme_memIo_threadYield(void);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_THREAD_H
}
		#undef SJME_CXX_SQUIRRELJME_THREAD_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_THREAD_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_THREAD_H */
