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

#if defined(SJME_CONFIG_HAS_THREADS_PTHREAD)

static void sjme_thread_pthreadResume(int signalId)
{
	/* No longer handle the signal, otherwise an infinite loop occurs. */
	signal(signalId, SIG_IGN);
}

sjme_errorCode sjme_thread_current(
	sjme_attrInOutNotNull sjme_thread* outThread)
{
	sjme_thread result;
	
	if (outThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Query. */
	result = pthread_self();
	if (result == 0 || result == SJME_THREAD_NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
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
	
	return pthread_equal(aThread, bThread);
}

sjme_errorCode sjme_thread_new(
	sjme_attrInOutNotNull sjme_thread* outThread,
	sjme_attrInNullable sjme_thread_id* outThreadId,
	sjme_attrInNotNull sjme_thread_mainFunc inMain,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	static sjme_jboolean signalInit;
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

	/* If the signal handler was not yet setup, then set it up. */
	if (!signalInit)
	{
		/* Register the signal handler. */
		signal(SIGUSR1, sjme_thread_pthreadResume);
		
		/* Is now setup. */
		signalInit = SJME_JNI_TRUE;
	}
	
	/* Setup new thread. */
	if (0 != pthread_create(&result, NULL,
		inMain, anything))
		return SJME_ERROR_CANNOT_CREATE;
	threadId = (sjme_thread_id)result;
	
	/* Success! */
	*outThread = result;
	if (outThreadId != NULL)
		*outThreadId = threadId;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_thread_wake(
	sjme_attrInNotNull sjme_thread inThread)
{
	if (inThread == SJME_THREAD_NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Send the user signal to the thread, to force wake it. */
	pthread_kill(inThread, SIGUSR1);

	return SJME_ERROR_NONE;
}

#if defined(SJME_CONFIG_HAS_THREADS_LIBRARY_YIELD)

void sjme_thread_yieldImpl(void)
{
	pthread_yield();
}

#endif

#endif
