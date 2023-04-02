/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internal locking mechanics.
 * 
 * @since 2023/03/31
 */

#ifndef SQUIRRELJME_LOCKINTERNAL_H
#define SQUIRRELJME_LOCKINTERNAL_H

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_LOCKINTERNAL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

#if defined(SQUIRRELJME_THREADS)
	#if defined(SQUIRRELJME_THREADS_WIN32)
		#include <windows.h>
	#elif defined(SQUIRRELJME_THREADS_PTHREAD)
		#include <pthread.h>
		#include <errno.h>
	#endif
#endif

#include "memio/lock.h"

/*--------------------------------------------------------------------------*/

struct sjme_memIo_spinLock
{
	/** The lock value key. */
	sjme_memIo_atomicInt lock;

	/** The number of times locked, for recursive locks. */
	sjme_memIo_atomicInt count;

#if defined(SQUIRRELJME_THREADS_PTHREAD)
	/** The lock attributes. */
	pthread_mutexattr_t mutexAttr;

	/** The lock mutex. */
	pthread_mutex_t mutex;
#endif
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_LOCKINTERNAL_H
}
		#undef SJME_CXX_SQUIRRELJME_LOCKINTERNAL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_LOCKINTERNAL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LOCKINTERNAL_H */
