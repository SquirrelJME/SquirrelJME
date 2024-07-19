/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/multithread.h"

#if defined(SJME_CONFIG_HAS_LINUX)
	#include <sched.h>
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	#include <processthreadsapi.h>
#endif

#include "sjme/debug.h"

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
	/* Query. */
	result = GetCurrentThread();
	if (result == NULL || result == SJME_THREAD_NULL)
		return SJME_ERROR_ILLEGAL_STATE;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
	
	/* Use given result. */
	*outThread = result;
	return SJME_ERROR_NONE;
}

sjme_jboolean sjme_thread_equal(
	sjme_attrInNullable sjme_thread aThread,
	sjme_attrInNullable sjme_thread bThread)
{
	if ((aThread == SJME_THREAD_NULL) != (bThread == SJME_THREAD_NULL))
		return SJME_JNI_FALSE;
	
	else if (aThread == SJME_THREAD_NULL && bThread == SJME_THREAD_NULL)
		return SJME_JNI_TRUE;
	
#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	return pthread_equal(aThread, bThread);
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	return GetThreadId(aThread) == GetThreadId(bThread);
#else
	return aThread == bThread;
#endif
}

sjme_errorCode sjme_thread_new(
	sjme_attrInOutNotNull sjme_thread* outThread,
	sjme_attrInNotNull sjme_thread_mainFunc inMain,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_thread result;

	if (outThread == NULL || inMain == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Clear first. */
	result = SJME_THREAD_NULL;

#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)
	/* Setup new thread. */
	if (0 != pthread_create(&result, NULL,
		inMain, anything))
		return SJME_ERROR_CANNOT_CREATE;
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	/* Setup new thread. */
	result = CreateThread(NULL, 0, (LPTHREAD_START_ROUTINE)inMain,
		anything, 0, NULL);
	if (result == NULL || result == SJME_THREAD_NULL)
		return SJME_ERROR_CANNOT_CREATE;
#else
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
#endif
	
	/* Success! */
	*outThread = result;
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
			sjme_thread_barrier();
			sjme_thread_yield();
			sjme_thread_barrier();
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
	sjme_thread_barrier();
	sjme_thread_yield();
	sjme_thread_barrier();
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_thread_spinLockRelease(sjme_thread_spinLock* inLock)
{
	sjme_errorCode error;
	sjme_thread current;
	sjme_jboolean owned;
	
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
		sjme_thread_barrier();
		sjme_thread_yield();
		sjme_thread_barrier();
	}
	
	/* We own the lock hopefully, so count down. */
	if ((owned = sjme_atomic_sjme_thread_compareSet(&inLock->owner,
		current, current)))
	{
		/* If we count down to zero, then we no longer own the lock. */
		if (sjme_atomic_sjme_jint_getAdd(&inLock->count,
			-1) <= 1)
		{
			sjme_atomic_sjme_thread_set(&inLock->owner,
				SJME_THREAD_NULL);
			sjme_atomic_sjme_jint_getAdd(&inLock->count, 0);
		}
	}
	
	/* Clear the peek lock. */
	sjme_atomic_sjme_thread_compareSet(&inLock->poke,
		current, SJME_THREAD_NULL);
		
	/* Do this just for good measure for the wierd CPUs. */
	sjme_thread_barrier();
	sjme_thread_yield();
	sjme_thread_barrier();
	
	/* Success, unless we do not own the lock. */
	if (!owned)
		return SJME_ERROR_NOT_LOCK_OWNER;
	return SJME_ERROR_NONE;
}

void sjme_thread_yield(void)
{
#if defined(SJME_CONFIG_HAS_LINUX)
	sched_yield();
#elif defined(SJME_CONFIG_HAS_THREADS_PTHREAD_MACOS)
	/* macOS has none. */
#elif defined(SJME_CONFIG_HAS_THREADS_PTHREAD_BSD)
	pthread_yield();
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	SwitchToThread();
#endif
}
