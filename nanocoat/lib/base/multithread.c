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
#include "sjme/native.h"
#include "sjme/debug.h"

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
		writeCount = sjme_atomic_g(sjme_jint, &inLock->writeCount);
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
	writeCount = sjme_atomic_ga(sjme_jint, &inLock->writeCount,
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
	writeCount = sjme_atomic_ga(sjme_jint, &inLock->writeCount,
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
		while (SJME_JNI_FALSE == sjme_atomic_cs(sjme_thread, 
			&inLock->poke, SJME_THREAD_NULL, current))
		{
			sjme_atomic_barrier();
			sjme_thread_yield();
			sjme_atomic_barrier();
		}
		
		/* We own the lock already, or we just owned it, so count up. */
		if (sjme_atomic_cs(sjme_thread, &inLock->owner,
			current, current) ||
			sjme_atomic_cs(sjme_thread, &inLock->owner,
				SJME_THREAD_NULL, current))
		{
			sjme_atomic_ga(sjme_jint, &inLock->count, 1);
			
			keepSpinning = SJME_JNI_FALSE;
		}
		
		/* Clear the peek lock. */
		sjme_atomic_cs(sjme_thread, &inLock->poke,
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
	while (SJME_JNI_FALSE == sjme_atomic_cs(sjme_thread, 
		&inLock->poke, SJME_THREAD_NULL, current))
	{
		sjme_atomic_barrier();
		sjme_thread_yield();
		sjme_atomic_barrier();
	}
	
	/* We own the lock hopefully, so count down. */
	count = -1;
	if ((owned = sjme_atomic_cs(sjme_thread, &inLock->owner,
		current, current)))
	{
		/* If we count down to zero, then we no longer own the lock. */
		if ((count = sjme_atomic_ga(sjme_jint, &inLock->count,
			-1)) <= 1)
		{
			sjme_atomic_s(sjme_thread, &inLock->owner,
				SJME_THREAD_NULL);
			sjme_atomic_s(sjme_jint, &inLock->count,
				(count = 0));
		}
	}
	
	/* Clear the peek lock. */
	sjme_atomic_cs(sjme_thread, &inLock->poke,
		current, SJME_THREAD_NULL);
		
	/* Do this just for good measure for the wierd CPUs. */
	sjme_atomic_barrier();

#if defined(SJME_CONFIG_DEBUG)
	/* Do we not own the lock? */
	if (!owned)
		sjme_message("Lock %p owner %p is not %p",
			inLock, sjme_atomic_g(sjme_thread, &inLock->owner), current);
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
	/* Fallback to NAL handler, if available. */
	if (sjme_nal_default.threadSleep != NULL)
		sjme_nal_default.threadSleep(millis, nanos);
}

void sjme_thread_yield(void)
{
#if defined(SJME_CONFIG_HAS_THREADS_LIBRARY_YIELD)
	/* Has library based yield? */
	sjme_thread_yieldImpl();
#else
	/* Fallback to NAL handler, if available. */
	if (sjme_nal_default.threadYield != NULL)
		sjme_nal_default.threadYield();
#endif
}
