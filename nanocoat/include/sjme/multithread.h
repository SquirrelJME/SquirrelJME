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
 * @file
 * @since 2023/12/16
 */

#ifndef SJME_C_MULTITHREAD_H
#define SJME_C_MULTITHREAD_H

#include "sjme/config.h"
#include "sjme/error.h"

#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	#include <pthread.h>
	#include <signal.h>
#endif

#include "sjme/stdTypes.h"
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
/* clang-format off */ /* @formatter:off */

/** The thread identifier. */
typedef sjme_intPointer sjme_thread_id;
	
#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	/** A single thread. */
	typedef pthread_t sjme_alignPointer sjme_thread;

	/* On these systems pthread_t is a pointer. */
	#if defined(SJME_CONFIG_HAS_OS_MACOS) || \
		defined(SJME_CONFIG_HAS_OS_BSD_FAMILY) || \
		defined(SJME_CONFIG_HAS_OS_EMSCRIPTEN)
		/** The thread type. */
		#define SJME_TYPEOF_BASIC_sjme_thread SJME_TYPEOF_BASIC_sjme_pointer
	
		/** Is a thread a pointer? */
		#define SJME_TYPEOF_IS_POINTER_sjme_thread 1
	#else
		/** The thread type. */
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

	/** Thread parameter as a pointer. */
	#define SJME_THREAD_PARAM_POINTER(p) ((sjme_pointer)(p))
	
	/** Error as thread result. */
	#define SJME_THREAD_RESULT(err) ((sjme_pointer)(err))

	/** Thread result cast to error. */
	#define SJME_THREAD_RESULT_AS_ERROR(result) \
		((sjme_errorCode)((sjme_intPointer)(result)))
	
	/** Calling convention to use for thread entry points. */
	#define sjme_attrThreadCall
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	/** A single thread. */
	typedef DWORD sjme_thread;

	/** The thread type. */
	#define SJME_TYPEOF_BASIC_sjme_thread SJME_TYPEOF_BASIC_sjme_jint

	/** Is a thread a pointer? */
	#define SJME_TYPEOF_IS_POINTER_sjme_thread 0
	
	/** Thread result. */
	typedef DWORD sjme_thread_result;
	
	/** Thread parameter. */
	typedef LPVOID sjme_thread_parameter;
	
	/** Null thread handle. */
	#define SJME_THREAD_NULL 0

	/** Thread parameter as a pointer. */
	#define SJME_THREAD_PARAM_POINTER(p) ((sjme_pointer)(p))
	
	/** Error as thread result. */
	#define SJME_THREAD_RESULT(err) ((DWORD)(err))

	/** Thread result cast to error. */
	#define SJME_THREAD_RESULT_AS_ERROR(result) ((sjme_errorCode)(result))

	/** Bump Thread. */
	#define SJME_THREAD_BUMP(x) ((x) + 1)

	/** Unbump Thread. */
	#define SJME_THREAD_UNBUMP(x) ((x) - 1)
	
	/** Calling convention to use for thread entry points. */
	#define sjme_attrThreadCall WINAPI
#else
	/** A single thread. */
	typedef sjme_jint sjme_thread;

	/** The thread type. */
	#define SJME_TYPEOF_BASIC_sjme_thread SJME_TYPEOF_BASIC_sjme_int

	/** Is a thread a pointer? */
	#define SJME_TYPEOF_IS_POINTER_sjme_thread 0
	
	/** Thread result. */
	typedef sjme_jint sjme_thread_result;
	
	/** Thread parameter. */
	typedef sjme_pointer sjme_thread_parameter;
	
	/** Null thread handle. */
	#define SJME_THREAD_NULL 0

	/** Thread parameter as a pointer. */
	#define SJME_THREAD_PARAM_POINTER(p) ((sjme_pointer)(p))
	
	/** Error as thread result. */
	#define SJME_THREAD_RESULT(err) ((sjme_jint)(err))

	/** Thread result cast to error. */
	#define SJME_THREAD_RESULT_AS_ERROR(result) ((sjme_errorCode)(result))
	
	/** Calling convention to use for thread entry points. */
	#define sjme_attrThreadCall
#endif

/* clang-format on */ /* @formatter:on */
/*--------------------------------------------------------------------------*/
	
/* No bumping needed? */
#if !defined(SJME_THREAD_BUMP) && !defined(SJME_THREAD_UNBUMP)
	/** Bump Thread. */
	#define SJME_THREAD_BUMP(x) (x)

	/** Unbump Thread. */
	#define SJME_THREAD_UNBUMP(x) (x)
#endif

SJME_ATOMIC_DECLARE(sjme_thread, 0);

/**
 * Main thread function type.
 * 
 * @param anything Passed from @link sjme_thread_new @endlink .
 * @return Thread resultant value.
 * @since 2024/04/16
 */
typedef sjme_thread_result (sjme_attrThreadCall *sjme_thread_mainFunc)(
	sjme_attrInNullable sjme_thread_parameter anything);

/**
 * Basic spin lock.
 * 
 * @since 2024/07/19
 */
typedef volatile struct sjme_alignPointer sjme_thread_spinLock
{
	/** The thread that is currently poking this lock. */
	sjme_alignPointer sjme_atomic(sjme_thread) poke;
	
	/** The thread that owns this lock. */
	sjme_alignPointer sjme_atomic(sjme_thread) owner;
	
	/** Lock count. */
	sjme_alignPointer sjme_atomic(sjme_jint) count;
} sjme_thread_spinLock;

/**
 * Read/write lock.
 * 
 * @since 2024/10/22
 */
typedef volatile struct sjme_alignPointer sjme_thread_rwLock
{
	/** Pointer to the lock responsible for reading. */
	sjme_alignPointer sjme_thread_spinLock* read;
	
	/** The number of times writes are locked. */
	sjme_alignPointer sjme_atomic(sjme_jint) writeCount;
	
	/** The write specific lock. */
	sjme_alignPointer sjme_thread_spinLock write;
} sjme_thread_rwLock;

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
 * Returns the current thread.
 *
 * @return The resultant thread.
 * @since 2026/01/09
 */
sjme_thread sjme_thread_currentR(void);

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
 * @param outThreadId The resultant thread ID, is optional.
 * @param inMain The main function for the thread.
 * @param anything Any value to pass to it.
 * @return Any error code if applicable.
 * @since 2024/04/16
 */
sjme_errorCode sjme_thread_new(
	sjme_attrInOutNotNull sjme_thread* outThread,
	sjme_attrInNullable sjme_thread_id* outThreadId,
	sjme_attrInNotNull sjme_thread_mainFunc inMain,
	sjme_attrInNullable sjme_thread_parameter anything);

/**
 * Grabs the read lock.
 * 
 * @param inLock The lock to use. 
 * @return Any resultant error, if any.
 * @since 2024/10/22
 */
sjme_errorCode sjme_thread_rwLockGrabRead(
	sjme_attrInNotNull sjme_thread_rwLock* inLock);

/**
 * Grabs the write lock.
 * 
 * @param inLock The lock to use. 
 * @return Any resultant error, if any.
 * @since 2024/10/22
 */
sjme_errorCode sjme_thread_rwLockGrabWrite(
	sjme_attrInNotNull sjme_thread_rwLock* inLock);

/**
 * Releases the read lock.
 * 
 * @param inLock The lock to release.
 * @param outCount Optional count of the locks remaining.
 * @return Any resultant error, if any.
 * @since 2024/10/22
 */
sjme_errorCode sjme_thread_rwLockReleaseRead(
	sjme_attrInNotNull sjme_thread_rwLock* inLock,
	sjme_attrOutNullable sjme_jint* outCount);

/**
 * Releases the write lock.
 * 
 * @param inLock The lock to release.
 * @param outCount Optional count of the locks remaining.
 * @return Any resultant error, if any.
 * @since 2024/10/22
 */
sjme_errorCode sjme_thread_rwLockReleaseWrite(
	sjme_attrInNotNull sjme_thread_rwLock* inLock,
	sjme_attrOutNullable sjme_jint* outCount);

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
 * Sleeps for the given time duration.
 * 
 * @param millis The number of milliseconds to sleep for.
 * @param nanos The number of nanoseconds to sleep for.
 * @since 2025/05/16
 */
void sjme_thread_sleep(sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos);

/**
 * Wakes the given thread, if possible.
 * 
 * @param inThread The thread to wake.
 * @return Any resultant error, if any.
 * @since 2025/10/02
 */
sjme_errorCode sjme_thread_wake(
	sjme_attrInNotNull sjme_thread inThread);

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
