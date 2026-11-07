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

#pragma region(atomics)

#define sjme_atomic_type sjme_thread
#include "sjme/atomicTemplateC.h" /* NOLINT */

#pragma endregion(atomics)

#if defined(SJME_CONFIG_ONLY_THREAD_SINGLE)
	/** The number of loops to wait for until heavier yield occurs. */
	#define SJME_THREAD_YIELD_LIMIT 0
#else
	/** The number of loops to wait for until heavier yield occurs. */
	#define SJME_THREAD_YIELD_LIMIT 32768
#endif

sjme_thread sjme_thread_currentR(void)
{
	sjme_thread result;

	result = SJME_THREAD_NULL;
	if (sjme_error_is(sjme_thread_current(&result)))
		return SJME_THREAD_NULL;

	return result;
}

sjme_errorCode sjme_noOptimize sjme_keepOrder sjme_thread_rwLockGrabRead(
	sjme_attrInNotNull sjme_thread_rwLock* inLock)
{
	sjme_errorCode error;
	sjme_thread_spinLock* readLock;
	sjme_jint writeCount, cycle;
	sjme_jboolean lockStep;
	
	/* LCOV_EXCL_START */
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The read lock is required. */
	readLock = inLock->read;
	if (readLock == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Attempt locking constantly. */
	cycle = 0;
	for (lockStep = SJME_JNI_FALSE;; lockStep = !lockStep)
	{
		/* Grab the read lock. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(readLock)))
			return sjme_error_default(error);
		
		/* The write count determines if we cannot get this lock. */
		writeCount = sjme_atomic_g(sjme_jint, &inLock->writeCount);
			
		/* Release the read lock. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(readLock,
			NULL)))
			return sjme_error_default(error);
		
		/* The write lock has been grabbed. */
		if (writeCount != 0)
		{
			/* Let other threads run, every other cycle. */
			if ((++cycle) > SJME_THREAD_YIELD_LIMIT && lockStep)
				sjme_thread_yield();
			
			/* Try again... */
			continue;
		}
		
		/* Otherwise stop. */
		break;
	}
		
	/* LCOV_EXCL_END */
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_noOptimize sjme_keepOrder sjme_thread_rwLockGrabWrite(
	sjme_attrInNotNull sjme_thread_rwLock* inLock)
{
	sjme_errorCode error;
	sjme_thread_spinLock* readLock;
	sjme_jint writeCount;
	
	/* LCOV_EXCL_START */
	
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
	
	/* LCOV_EXCL_END */
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_noOptimize sjme_keepOrder sjme_thread_rwLockReleaseRead(
	sjme_attrInNotNull sjme_thread_rwLock* inLock,
	sjme_attrOutNullable sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_thread_spinLock* readLock;
	
	/* LCOV_EXCL_START */
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The read lock is required. */
	readLock = inLock->read;
	if (readLock == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* LCOV_EXCL_END */
	
	/* This is a no-op because we only access the read lock by locking and */
	/* checking the write lock. */
	if (outCount != NULL)
		*outCount = 0;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_noOptimize sjme_keepOrder sjme_thread_rwLockReleaseWrite(
	sjme_attrInNotNull sjme_thread_rwLock* inLock,
	sjme_attrOutNullable sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_thread_spinLock* readLock;
	sjme_jint writeCount, actualWrite;
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* LCOV_EXCL_START */
	
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
	
	/* LCOV_EXCL_END */
	
	/* Success! */
	if (outCount != NULL)
		*outCount = actualWrite;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_noOptimize sjme_keepOrder sjme_thread_spinLockGrab(
	sjme_thread_spinLock* inLock)
{
	sjme_errorCode error;
	sjme_thread current, lockOwner;
	sjme_jboolean lockStep, ownSelf, ownNone, ownOther, gotPeek;
	sjme_jint cycle, prevCount;
	
	/* LCOV_EXCL_START */
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* We need the current thread. */
	current = SJME_THREAD_NULL;
	if (sjme_error_is(error = sjme_thread_current(
		&current)) || current == SJME_THREAD_NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_THREAD_STATE);
	
	/* Initialize. */
	ownSelf = SJME_JNI_FALSE;
	ownNone = SJME_JNI_FALSE;
	ownOther = SJME_JNI_FALSE;
	
	/* This is done in a loop until we own the lock. */
	cycle = 0;
	prevCount = INT32_MIN;
	for (lockStep = SJME_JNI_FALSE;; lockStep = !lockStep)
	{
		/* Get the current lock owner, lazily. */
		lockOwner = sjme_atomic_gl(sjme_thread, &inLock->owner);
		
		/* What is the lock ownership state? */
		ownNone = sjme_thread_equal(lockOwner,
			SJME_THREAD_NULL);
		ownSelf = sjme_thread_equal(lockOwner, current);
		ownOther = (!ownNone && !ownSelf);
		
		/* Grab the peek lock if we feel like we could own this lock. */
		gotPeek = SJME_JNI_FALSE;
		if (!ownOther && (ownNone || ownSelf))
			gotPeek = sjme_atomic_cs(sjme_thread, 
				&inLock->poke, SJME_THREAD_NULL, current);
		
		/* If we did not get the peek, we need to retry. */
		if (!gotPeek)
		{
			/* Lightweight lock every other step. */
			if (lockStep)
				sjme_atomic_light();
			
			/* Every other time, let other threads run after a limit. */
			if ((++cycle) > SJME_THREAD_YIELD_LIMIT && lockStep)
				sjme_thread_yield();
			
			/* Try again. */
			continue;
		}
		
		/* Try to set owner from none. */
		if (ownNone)
			ownNone = sjme_atomic_cs(sjme_thread, &inLock->owner,
				SJME_THREAD_NULL, current);
		
		/* Try to set owner from self. */
		if (ownNone || ownSelf)
			ownSelf = sjme_atomic_cs(sjme_thread, &inLock->owner,
				current, current);
		
		/* We actually own the lock, so count up. */
		prevCount = INT32_MIN;
		if (ownSelf)
			prevCount = sjme_atomic_ga(sjme_jint, &inLock->count, 1);
		
		/* Clear the peek lock. */
		sjme_atomic_cs(sjme_thread, &inLock->poke,
			current, SJME_THREAD_NULL);
		
		/* If the previous count changed, then we know we grabbed this. */
		if (prevCount != INT32_MIN)
			break;
	}
		
	/* Heavy memory barrier if this is the first time locking this lock. */
	if (ownSelf && prevCount == 0)
		sjme_atomic_heavy();
	
	/* LCOV_EXCL_END */
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_noOptimize sjme_keepOrder sjme_thread_spinLockRelease(
	sjme_attrInNotNull sjme_thread_spinLock* inLock,
	sjme_attrOutNullable sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_thread current, lockOwner;
	sjme_jboolean ownSelf, lockStep, oneCount;
	sjme_jint count, cycle;
	
	/* LCOV_EXCL_START */
	
	if (inLock == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* We need the current thread. */
	current = SJME_THREAD_NULL;
	if (sjme_error_is(error = sjme_thread_current(
		&current)) || current == SJME_THREAD_NULL)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_THREAD_STATE);
	
	/* Get the current lock owner, lazily. */
	lockOwner = sjme_atomic_gl(sjme_thread, &inLock->owner);
	
	/* Someone else owns this thread, or something is very wrong */
	/* with the system memory. */
	ownSelf = sjme_thread_equal(lockOwner, current);
	if (!ownSelf)
		return SJME_ERROR_INVALID_THREAD_STATE;
	
	/* Determine if we really do own the lock. */
	ownSelf = sjme_atomic_cs(sjme_thread, &inLock->owner,
	current, current);
	if (!ownSelf)
		return SJME_ERROR_INVALID_THREAD_STATE;
	
	/* If we do own the lock, count down. */
	count = INT32_MIN;
	if (ownSelf)
	{
		/* Does this lock have a count of one already? */
		oneCount = sjme_atomic_cs(sjme_jint, &inLock->count,
			1, 1);
		
		/* Otherwise we can count down. */
		if (!oneCount)
			count = sjme_atomic_ga(sjme_jint, &inLock->count, -1);
		
		/* If we count down to zero, then we no longer own the lock. */
		else
		{
			/* Clear count. */
			count = sjme_atomic_s(sjme_jint, &inLock->count, 0);
			
			/* Clear owner. */
			sjme_atomic_cs(sjme_thread, &inLock->owner,
				current, SJME_THREAD_NULL);
		}
	}
	
	/* If we are releasing our last lock, do a heavy barrier */
	if (ownSelf && count != INT32_MIN && count <= 1)
		sjme_atomic_heavy();
	
	/* Give the lock count that is left. */
	if (outCount != NULL)
	{
		if (count > 0)
			*outCount = count - 1;
		else
			*outCount = 0;
	}
	
	/* LCOV_EXCL_END */
	
	return SJME_ERROR_NONE;
}

void sjme_thread_sleep(sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos)
{
	/* LCOV_EXCL_START */
	
	/* Fallback to NAL handler, if available. */
	if (sjme_nal_default.threadSleep != NULL)
		sjme_nal_default.threadSleep(millis, nanos);
	
	/* LCOV_EXCL_END */
}

void sjme_thread_yield(void)
{
	/* LCOV_EXCL_START */
	
#if defined(SJME_CONFIG_HAS_THREADS_LIBRARY_YIELD)
	/* Has library based yield? */
	sjme_thread_yieldImpl();
#else
	/* Fallback to NAL handler, if available. */
	if (sjme_nal_default.threadYield != NULL)
		sjme_nal_default.threadYield();
#endif
	
	/* LCOV_EXCL_END */
}
