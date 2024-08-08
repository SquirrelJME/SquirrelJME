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

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_THREADS_FALLBACK)
	/* Clear pthreads. */
	#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
		#undef SJME_CONFIG_HAS_THREADS_PTHREAD
	#endif

	/* Clear Win32 threads. */ 
	#if defined(SJME_CONFIG_HAS_THREADS_WIN32)
		#undef SJME_CONFIG_HAS_THREADS_WIN32
	#endif
#endif

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
#include "sjme/atomic.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_MULTITHREAD_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/* clang-format off */

#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	/** A single thread. */
	typedef pthread_t sjme_thread;

	/* On these systems pthread_t is a pointer. */
	#if defined(SJME_CONFIG_HAS_MACOS) || \
		defined(SJME_CONFIG_HAS_EMSCRIPTEN)
		/** The type of a thread. */
		#define SJME_TYPEOF_BASIC_sjme_thread SJME_TYPEOF_BASIC_sjme_pointer
	
		/** Is a thread a pointer? */
		#define SJME_TYPEOF_IS_POINTER_sjme_thread 1
	#else
		/** The type of a thread. */
		#define SJME_TYPEOF_BASIC_sjme_thread SJME_TYPEOF_BASIC_sjme_intPointer
	
		/** Is a thread a pointer? */
		#define SJME_TYPEOF_IS_POINTER_sjme_thread 0
	#endif
	
	/** Thread result. */
	typedef sjme_pointer sjme_thread_result;
	
	/** Thread parameter. */
	typedef sjme_pointer sjme_thread_parameter;
	
	/** Null thread handle. */
	#define SJME_THREAD_NULL ((unsigned long)0)
	
	/** Error as thread result. */
	#define SJME_THREAD_RESULT(err) ((sjme_pointer)(err))

	/** Thread result cast to error. */
	#define SJME_THREAD_RESULT_AS_ERROR(result) \
		((sjme_errorCode)((sjme_intPointer)(result)))
	
	/** Calling convention to use for thread entry points. */
	#define SJME_THREAD_CONVENTION

	/** Thread memory barrier. */
	#define sjme_thread_barrier() __sync_synchronize()
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	/** A single thread. */
	typedef HANDLE sjme_thread;

	/** The type of a thread. */
	#define SJME_TYPEOF_BASIC_sjme_thread SJME_TYPEOF_BASIC_sjme_pointer

	/** Is a thread a pointer? */
	#define SJME_TYPEOF_IS_POINTER_sjme_thread 1
	
	/** Thread result. */
	typedef DWORD sjme_thread_result;
	
	/** Thread parameter. */
	typedef LPVOID sjme_thread_parameter;
	
	/** Null thread handle. */
	#define SJME_THREAD_NULL NULL
	
	/** Error as thread result. */
	#define SJME_THREAD_RESULT(err) ((DWORD)(err))

	/** Thread result cast to error. */
	#define SJME_THREAD_RESULT_AS_ERROR(result) ((sjme_errorCode)(result))
	
	/** Calling convention to use for thread entry points. */
	#define SJME_THREAD_CONVENTION __stdcall

	/** Thread memory barrier. */
	#define sjme_thread_barrier() MemoryBarrier()
#else
	/** Threads not supported. */
	typedef struct sjme_thread_unsupported
	{
		int unsupported;
	} sjme_thread_unsupported;
	
	/** A single thread. */
	typedef sjme_thread_unsupported* sjme_thread;

	/** The type of a thread. */
	#define SJME_TYPEOF_BASIC_sjme_thread SJME_TYPEOF_BASIC_sjme_pointer

	/** Is a thread a pointer? */
	#define SJME_TYPEOF_IS_POINTER_sjme_thread 1
	
	/** Thread result. */
	typedef int sjme_thread_result;
	
	/** Thread parameter. */
	typedef sjme_pointer sjme_thread_parameter;
	
	/** Null thread handle. */
	#define SJME_THREAD_NULL NULL
	
	/** Error as thread result. */
	#define SJME_THREAD_RESULT(err) ((int)(err))

	/** Thread result cast to error. */
	#define SJME_THREAD_RESULT_AS_ERROR(result) ((sjme_errorCode)(result))
	
	/** Calling convention to use for thread entry points. */
	#define SJME_THREAD_CONVENTION

	/** Thread memory barrier. */
	#define sjme_thread_barrier() do {} while(0)
#endif

/* clang-format on */

SJME_ATOMIC_DECLARE(sjme_thread, 0);

/**
 * Main thread function type.
 * 
 * @param anything Passed from @c sjme_thread_new .
 * @return Thread resultant value.
 * @since 2024/04/16
 */
typedef sjme_thread_result (SJME_THREAD_CONVENTION *sjme_thread_mainFunc)(
	sjme_attrInNullable sjme_thread_parameter anything);

/**
 * Basic spin lock.
 * 
 * @since 2024/07/19
 */
typedef struct sjme_thread_spinLock
{
	/** The thread that is currently poking this lock. */
	sjme_atomic_sjme_thread poke;
	
	/** The thread that owns this lock. */
	sjme_atomic_sjme_thread owner;
	
	/** Lock count. */
	sjme_atomic_sjme_jint count;
} sjme_thread_spinLock;

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
	sjme_attrInNullable sjme_pointer anything);

/**
 * Grabs a spin lock.
 * 
 * @param inLock The lock to grab. 
 * @return Any resultant error, if any.
 * @since 2024/07/19
 */
sjme_errorCode sjme_thread_spinLockGrab(sjme_thread_spinLock* inLock);

/**
 * Releases a spin lock.
 * 
 * @param inLock The lock to release. 
 * @param outCount Optional count after lock.
 * @return Any resultant error, if any.
 * @since 2024/07/19
 */
sjme_errorCode sjme_thread_spinLockRelease(
	sjme_attrInNotNull sjme_thread_spinLock* inLock,
	sjme_attrOutNullable sjme_jint* outCount);

/**
 * Yields execution.
 * 
 * @since 2024/04/17
 */
void sjme_thread_yield(void);

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
