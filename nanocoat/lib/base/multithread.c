/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_LINUX)
	#include <sched.h>
#endif

#include "sjme/multithread.h"
#include "sjme/debug.h"

void sjme_thread_barrier(void)
{
#if defined(SJME_CONFIG_HAS_GCC)
	__sync_synchronize();
#elif defined(SJME_CONFIG_HAS_WINDOWS)
	MemoryBarrier();
#endif
}

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
	if (0 != pthread_create(&result, NULL, inMain,
		anything))
		return SJME_ERROR_CANNOT_CREATE;
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	/* Setup new thread. */
	result = CreateThread(NULL, 0, inMain, anything, 0, NULL);
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

void sjme_thread_yield(void)
{
#if defined(SJME_CONFIG_HAS_LINUX)
	sched_yield();
#elif defined(SJME_CONFIG_HAS_THREADS_PTHREAD_MACOS);
#elif defined(SJME_CONFIG_HAS_THREADS_PTHREAD_BSD)
	pthread_yield();
#elif defined(SJME_CONFIG_HAS_THREADS_WIN32)
	SwitchToThread();
#endif
}
