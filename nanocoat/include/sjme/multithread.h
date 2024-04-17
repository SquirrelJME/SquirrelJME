/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Multithreaded support.
 * 
 * @since 2023/12/16
 */

#ifndef SQUIRRELJME_MULTITHREAD_H
#define SQUIRRELJME_MULTITHREAD_H

#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	#include <pthread.h>
	#include <errno.h>
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	#define WIN32_LEAN_AND_MEAN 1

	#include <windows.h>

	#undef WIN32_LEAN_AND_MEAN
#else
	#if !defined(SJME_CONFIG_HAS_THREADS_ATOMIC)
		#define SJME_CONFIG_HAS_THREADS_ATOMIC
	#endif
#endif

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MULTITHREAD_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	/** A single thread. */
	typedef pthread_t sjme_thread;
	
	/** Null thread handle. */
	#define SJME_THREAD_NULL ((unsigned long)0)
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	/** A single thread. */
	typedef HANDLE sjme_thread;
	
	/** Null thread handle. */
	#define SJME_THREAD_NULL NULL
#endif

/**
 * Main thread function type.
 * 
 * @param anything Passed from @c sjme_thread_new .
 * @return Any resultant error from the thread.
 * @since 2024/04/16
 */
typedef sjme_errorCode (*sjme_thread_mainFunc)(
	sjme_attrInNullable void* anything);

/**
 * Returns the current thread.
 * 
 * @param outThread The resultant thread.
 * @return On any errors if applicable.
 * @since 2024/04/16
 */
sjme_errorCode sjme_thread_current(
	sjme_attrInOutNotNull sjme_thread* outThread);

/**
 * Compares equality between two threads.
 * 
 * @param aThread The first thread.
 * @param bThread The second thread.
 * @return The resultant equality.
 * @since 2024/04/16
 */
sjme_jboolean sjme_thread_equal(
	sjme_attrInNullable sjme_thread aThread,
	sjme_attrInNullable sjme_thread bThread);

/**
 * Creates a new thread and immediately starts running it.
 * 
 * @param outThread The resultant thread.
 * @param inMain The main function for the thread.
 * @param anything Any value to pass to it.
 * @return Any error code if applicable.
 * @since 2024/04/16
 */
sjme_errorCode sjme_thread_new(
	sjme_attrInOutNotNull sjme_thread* outThread,
	sjme_attrInNotNull sjme_thread_mainFunc inMain,
	sjme_attrInNullable void* anything);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_MULTITHREAD_H
}
		#undef SJME_CXX_SQUIRRELJME_MULTITHREAD_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_MULTITHREAD_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_MULTITHREAD_H */
