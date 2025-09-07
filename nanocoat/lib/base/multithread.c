/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <time.h>
#include <string.h>

#include "sjme/config.h"
#include "sjme/multithread.h"

#if defined(SJME_CONFIG_HAS_OS_LINUX)
	#include <sched.h>
#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
	#if SJME_CONFIG_WINDOWS_VERSION_LEAST(SJME_CONFIG_WINDOWS_VERSION_8)
		#include <processthreadsapi.h>
	#endif
	
	#include <windows.h>
#endif

#include "sjme/debug.h"

#if defined(SJME_CONFIG_ONLY_THREAD_SINGLE)
/** The only available thread. */
static const sjme_thread sjme_singleCurrent;
#endif

sjme_errorCode sjme_thread_current(
	sjme_attrInOutNotNull sjme_thread* outThread)
{
	sjme_thread result;
	
	if (outThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Clear. */
	result = SJME_THREAD_NULL;
		
#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	/* Query. */
	result = pthread_self();
	if (result == 0 || result == SJME_THREAD_NULL)
		return SJME_ERROR_ILLEGAL_STATE;
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	/* Query the current thread ID, the main thread might be zero. */
	result = SJME_THREAD_BUMP(GetCurrentThreadId());
#elif defined(SJME_CONFIG_ONLY_THREAD_SINGLE)
	/* Threading is not supported, so always refer to a virtual ID. */
	result = sjme_singleCurrent;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
	
	/* Use given result. */
	*outThread = result;
	return SJME_ERROR_NONE;
}

sjme_jboolean sjme_thread_equal(
	sjme_attrInNullable sjme_thread aThread,
	sjme_attrInNullable sjme_thread bThread)
{
#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
#endif
	
	if ((aThread == SJME_THREAD_NULL) != (bThread == SJME_THREAD_NULL))
		return SJME_JNI_FALSE;
	
	else if (aThread == SJME_THREAD_NULL && bThread == SJME_THREAD_NULL)
		return SJME_JNI_TRUE;
	
#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	return pthread_equal(aThread, bThread);
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	/* To prevent handle exhaustion, threads are identified solely by their */
	/* identifier. */
	return aThread == bThread;
#else
	return aThread == bThread;
#endif
}

sjme_errorCode sjme_thread_new(
	sjme_attrInOutNotNull sjme_thread* outThread,
	sjme_attrInNullable sjme_thread_id* outThreadId,
	sjme_attrInNotNull sjme_thread_mainFunc inMain,
	sjme_attrInNullable sjme_thread_parameter anything)
{
#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
#endif
	sjme_thread result;
	sjme_thread_id threadId;

	if (outThread == NULL || inMain == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Clear first. */
	result = SJME_THREAD_NULL;
	threadId = 0;

	/* Emit barrier. */
	sjme_atomic_barrier();
	sjme_thread_yield();
	sjme_atomic_barrier();

#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	/* Setup new thread. */
	if (0 != pthread_create(&result, NULL,
		inMain, anything))
		return SJME_ERROR_CANNOT_CREATE;
	threadId = (sjme_thread_id)result;
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	/* Setup new thread. */
	result = SJME_THREAD_NULL;
	threadId = (sjme_thread_id)CreateThread(NULL, 0,
		(LPTHREAD_START_ROUTINE)inMain,
		anything, 0, &result);
	if (threadId == 0 || result == SJME_THREAD_NULL)
	{
		SetLastError(0);
		return SJME_ERROR_CANNOT_CREATE;
	}

	/* Windows requires thread bumping. */
	result = SJME_THREAD_BUMP(result);
#elif defined(SJME_CONFIG_ONLY_THREAD_SINGLE)
	/* Threading not supported. */
	return SJME_ERROR_CANNOT_CREATE;
#else
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
	
	/* Success! */
	*outThread = result;
	if (outThreadId != NULL)
		*outThreadId = threadId;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_thread_rwLockGrabRead(
	sjme_attrInNotNull sjme_thread_rwLock* inLock)
{
	sjme_errorCode error;
	sjme_thread_spinLock* readLock;
	sjme_jint writeCount;
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The read lock is required. */
	readLock = inLock->read;
	if (readLock == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Attempt locking constantly. */
	for (;;)
	{
		/* Grab the read lock. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(readLock)))
			return sjme_error_default(error);
		
		/* The write count determines if we cannot get this lock. */
		sjme_atomic_barrier();
		writeCount = sjme_atomic_sjme_jint_get(&inLock->writeCount);
		sjme_atomic_barrier();
			
		/* Release the read lock. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(readLock,
			NULL)))
			return sjme_error_default(error);
		
		/* The write lock has been grabbed. */
		if (writeCount != 0)
		{
			/* Let other threads run. */
			sjme_atomic_barrier();
			sjme_thread_yield();
			sjme_atomic_barrier();
			
			/* Try again... */
			continue;
		}
		
		/* Otherwise stop. */
		break;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_thread_rwLockGrabWrite(
	sjme_attrInNotNull sjme_thread_rwLock* inLock)
{
	sjme_errorCode error;
	sjme_thread_spinLock* readLock;
	sjme_jint writeCount;
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The read lock is required. */
	readLock = inLock->read;
	if (readLock == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Grab the read lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(readLock)))
		return sjme_error_default(error);
		
	/* Bump up the write lock count. */
	writeCount = sjme_atomic_sjme_jint_getAdd(&inLock->writeCount,
		1);
		
	/* Grab the write lock next. */
	if (writeCount <= 0)
	{
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&inLock->write)))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_thread_rwLockReleaseRead(
	sjme_attrInNotNull sjme_thread_rwLock* inLock,
	sjme_attrOutNullable sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_thread_spinLock* readLock;
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The read lock is required. */
	readLock = inLock->read;
	if (readLock == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* This is a no-op because we only access the read lock by locking and */
	/* checking the write lock. */
	if (outCount != NULL)
		*outCount = 0;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_thread_rwLockReleaseWrite(
	sjme_attrInNotNull sjme_thread_rwLock* inLock,
	sjme_attrOutNullable sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_thread_spinLock* readLock;
	sjme_jint writeCount, actualWrite;
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The read lock is required. */
	readLock = inLock->read;
	if (readLock == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Release the write lock. */
	actualWrite = 0;
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inLock->write, &actualWrite)))
		return sjme_error_default(error);
	
	/* Lower the write count. */
	writeCount = sjme_atomic_sjme_jint_getAdd(&inLock->writeCount,
		-1);
	
	/* Is the write lock completely clear now? If so release the read lock. */
	if (writeCount <= 1)
		if (sjme_error_is(error = sjme_thread_spinLockRelease(
			readLock, NULL)))
			return sjme_error_default(error);
	
	/* Success! */
	if (outCount != NULL)
		*outCount = actualWrite;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_thread_spinLockGrab(sjme_thread_spinLock* inLock)
{
	sjme_errorCode error;
	sjme_thread current;
	sjme_jboolean keepSpinning;
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* We need the current thread. */
	current = SJME_THREAD_NULL;
	if (sjme_error_is(error = sjme_thread_current(
		&current)) || current == SJME_THREAD_NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_THREAD_STATE);
	
	/* This is done in a loop until we own the lock. */
	for (keepSpinning = SJME_JNI_TRUE; keepSpinning;)
	{
		/* Grab the peek lock. */
		while (SJME_JNI_FALSE == sjme_atomic_sjme_thread_compareSet(
			&inLock->poke, SJME_THREAD_NULL, current))
		{
			sjme_atomic_barrier();
			sjme_thread_yield();
			sjme_atomic_barrier();
		}
		
		/* We own the lock already, or we just owned it, so count up. */
		if (sjme_atomic_sjme_thread_compareSet(&inLock->owner,
			current, current) ||
			sjme_atomic_sjme_thread_compareSet(&inLock->owner,
				SJME_THREAD_NULL, current))
		{
			sjme_atomic_sjme_jint_getAdd(&inLock->count, 1);
			
			keepSpinning = SJME_JNI_FALSE;
		}
		
		/* Clear the peek lock. */
		sjme_atomic_sjme_thread_compareSet(&inLock->poke,
			current, SJME_THREAD_NULL);
	}
		
	/* Do this just for good measure for the wierd CPUs. */
	sjme_atomic_barrier();
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_thread_spinLockRelease(
	sjme_attrInNotNull sjme_thread_spinLock* inLock,
	sjme_attrOutNullable sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_thread current;
	sjme_jboolean owned;
	sjme_jint count;
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* We need the current thread. */
	current = SJME_THREAD_NULL;
	if (sjme_error_is(error = sjme_thread_current(
		&current)) || current == SJME_THREAD_NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_THREAD_STATE);
	
	/* Grab the peek lock. */
	while (SJME_JNI_FALSE == sjme_atomic_sjme_thread_compareSet(
		&inLock->poke, SJME_THREAD_NULL, current))
	{
		sjme_atomic_barrier();
		sjme_thread_yield();
		sjme_atomic_barrier();
	}
	
	/* We own the lock hopefully, so count down. */
	count = -1;
	if ((owned = sjme_atomic_sjme_thread_compareSet(&inLock->owner,
		current, current)))
	{
		/* If we count down to zero, then we no longer own the lock. */
		if ((count = sjme_atomic_sjme_jint_getAdd(&inLock->count,
			-1)) <= 1)
		{
			sjme_atomic_sjme_thread_set(&inLock->owner,
				SJME_THREAD_NULL);
			sjme_atomic_sjme_jint_set(&inLock->count,
				(count = 0));
		}
	}
	
	/* Clear the peek lock. */
	sjme_atomic_sjme_thread_compareSet(&inLock->poke,
		current, SJME_THREAD_NULL);
		
	/* Do this just for good measure for the wierd CPUs. */
	sjme_atomic_barrier();

#if defined(SJME_CONFIG_DEBUG)
	/* Do we not own the lock? */
	if (!owned)
		sjme_message("Lock %p owner %p is not %p",
			inLock, sjme_atomic_sjme_thread_get(&inLock->owner), current);
#endif
	
	/* Give the lock count that is left. */
	if (outCount != NULL)
	{
		if (count > 0)
			*outCount = count - 1;
		else
			*outCount = 0;
	}
	
	return SJME_ERROR_NONE;
}

void sjme_thread_sleep(sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos)
{
#if defined(SJME_CONFIG_HAS_THREADS_WIN32)
	LARGE_INTEGER baseTime;
#elif defined(SJME_CONFIG_HAS_OS_POSIX)
	struct timespec request;
	sjme_jint seconds, mod;
#endif
	
	/* Yield instead. */
	if (millis <= 0 && nanos <= 0)
	{
		sjme_thread_yield();
		return;
	}
	
#if defined(SJME_CONFIG_HAS_THREADS_WIN32)
	/* Sleep for the given number of milliseconds. */
	if (millis > 0)
		Sleep(millis);

	/* Burn the CPU to consume the nanoseconds. */
	QueryPerformanceCounter(&baseTime);
	while (nanos > 0)
		nanos = 0; /* TODO */
	
#elif defined(SJME_CONFIG_HAS_OS_POSIX)
	/* Calculate seconds. */
	seconds = millis / 1000;
	mod = millis % 1000;

	/* Sleep for the given amount of time. */
	request.tv_sec = seconds;
	request.tv_nsec = nanos + (mod * 1000000);
	nanosleep(&request, NULL);
	
#else
#endif
}

void sjme_thread_yield(void)
{
#if defined(SJME_CONFIG_HAS_OS_LINUX)
	sched_yield();
#elif defined(SJME_CONFIG_HAS_THREADS_PTHREAD_MACOS)
	/* macOS has none. */
#elif defined(SJME_CONFIG_HAS_THREADS_PTHREAD_BSD)
	pthread_yield();
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_NT_4)
	if (!SwitchToThread())
		SetLastError(0);
#else
	Sleep(0);
#endif
#endif
}
