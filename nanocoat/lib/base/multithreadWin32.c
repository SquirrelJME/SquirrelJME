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

#if defined(SJME_CONFIG_HAS_THREADS_WIN32)

#if SJME_CONFIG_WINDOWS_VERSION_LEAST(SJME_CONFIG_WINDOWS_VERSION_8)
	#include <processthreadsapi.h>
#endif

sjme_errorCode sjme_thread_current(
	sjme_attrInOutNotNull sjme_thread* outThread)
{
	sjme_thread result;
	
	if (outThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Query the current thread ID, the main thread might be zero. */
	*outThread = SJME_THREAD_BUMP(GetCurrentThreadId());
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
	
	/* To prevent handle exhaustion, threads are identified solely by their */
	/* identifier. */
	return aThread == bThread;
}

sjme_errorCode sjme_thread_new(
	sjme_attrInOutNotNull sjme_thread* outThread,
	sjme_attrInNullable sjme_thread_id* outThreadId,
	sjme_attrInNotNull sjme_thread_mainFunc inMain,
	sjme_attrInNullable sjme_thread_parameter anything)
{
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
	
	/* No native support. */
	return SJME_ERROR_NONE;
}

#endif
