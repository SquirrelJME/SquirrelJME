/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "memio/lock.h"
#include "memio/lockinternal.h"
#include "debug.h"

/** The unlocked key value. */
#define SJME_MEMIO_UNLOCKED SJME_JINT_C(0)

/** The next locking key to use. */
static sjme_memIo_atomicInt sjme_nextLockKey;

/**
 * Attempts to create a memory barrier.
 *
 * @since 2022/12/10
 */
static void sjme_memIo_memoryBarrier(void)
{
#if defined(SJME_THREADS_WIN32)
	MemoryBarrier();
#elif defined(SJME_FEATURE_GCC) && !defined(SJME_FEATURE_OLD_GCC)
	__sync_synchronize();
#endif
}

/**
 * Returns the next lock key to use.
 *
 * @return The next lock key to use.
 * @since 2023/04/01
 */
static sjme_jint sjme_memIo_lockNextLockKey(void)
{
	sjme_jint useKey;

	/* Get the next locking key to use. */
	useKey = SJME_MEMIO_UNLOCKED;
	while (useKey == SJME_MEMIO_UNLOCKED)
	{
		useKey = sjme_memIo_atomicIntGetThenAdd(&sjme_nextLockKey,
			1) + 1;
	}

	return useKey;
}

sjme_jboolean sjme_memIo_lock(sjme_memIo_spinLock* lock,
	sjme_memIo_spinLockKey* key, sjme_error* error)
{
#if defined(SJME_THREADS_PTHREAD)
	int errorCode;
#else
	sjme_memIo_threadId currentThreadId;
#endif
	sjme_jint useKey;
	
	if (lock == NULL || key == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Cannot use a key twice! */
	if (key->key != SJME_MEMIO_UNLOCKED)
		return sjme_setErrorF(error, SJME_ERROR_LOCK_KEY_IN_USE, 0);

	/* Get the next locking key to use. */
	useKey = sjme_memIo_lockNextLockKey();

#if defined(SJME_THREADS_PTHREAD)
	/* Lock and block, pthreads will halt the CPU accordingly. */
	/* Unlike tryLock(), this results in failure if non-zero. */
	if (0 != pthread_mutex_lock(&lock->mutex))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, errorCode);
#else
	/* We need to be the thread that owns this lock. */
	/* Yield in the lock until we obtain this. */
	currentThreadId = sjme_memIo_threadCurrentId();
	for (;;)
	{
		/* See if an unowned lock can become owned. */
		if (sjme_memIo_atomicIntPointerCompareThenSet(&lock->thread,
				SJME_MEMIO_INVALID_THREAD_ID, currentThreadId))
			break;

		/* If we are the actual owner then stop and continue. */
		if (sjme_memIo_atomicIntPointerCompareThenSet(&lock->thread,
				currentThreadId, currentThreadId))
			break;

		/* Yield for a moment to let other threads run. */
		sjme_memIo_threadYield();
	}
#endif

	/* Increase the lock count. */
	sjme_memIo_atomicIntGetThenAdd(&lock->count, 1);

	/* Set new key if currently unlocked and use that one. */
	sjme_memIo_atomicIntCompareThenSet(&lock->lock,
		SJME_MEMIO_UNLOCKED, useKey);
	key->key = sjme_memIo_atomicIntGet(&lock->lock);

	/* Memory barrier before we leave. */
	sjme_memIo_memoryBarrier();

	/* Success! */
	return sjme_true;
}

sjme_jboolean sjme_memIo_lockDestroy(sjme_memIo_spinLock* lock,
	sjme_error* error)
{
	if (lock == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

#if defined(SJME_THREADS_PTHREAD)
	/* Destroy pthread mutex. */
	if (0 != pthread_mutex_destroy(&lock->mutex))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, 0);

	/* Destroy the attributes. */
	if (0 != pthread_mutexattr_destroy(&lock->mutexAttr))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, 0);

	return sjme_true;
#else
	/* Does nothing. */
	return sjme_true;
#endif
}

sjme_jboolean sjme_memIo_lockInit(sjme_memIo_spinLock* lock,
	sjme_error* error)
{
	if (lock == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

#if defined(SJME_THREADS_PTHREAD)
	/* Initialize the attributes. */
	if (0 != pthread_mutexattr_init(&lock->mutexAttr))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, 0);

	/* Make recursive mutex. */
	if (0 != pthread_mutexattr_settype(&lock->mutexAttr,
		PTHREAD_MUTEX_RECURSIVE))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, 1);

	/* Initialize pthread mutex. */
	if (0 != pthread_mutex_init(&lock->mutex, &lock->mutexAttr))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, 2);

	return sjme_true;
#else
	/** Set the initial thread to be invalid. */
	sjme_memIo_atomicIntPointerSet(&lock->thread,
		SJME_MEMIO_INVALID_THREAD_ID);

	/* Success!. */
	return sjme_true;
#endif
}

sjme_jboolean sjme_memIo_tryLock(sjme_memIo_spinLock* lock,
	sjme_memIo_spinLockKey* key,
	sjme_error* error)
{
#if defined(SJME_THREADS_PTHREAD)
	int errorCode;
#else
	sjme_memIo_threadId currentThreadId;
#endif
	sjme_jint useKey;
	
	if (lock == NULL || key == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Cannot use a key twice! */
	if (key->key != SJME_MEMIO_UNLOCKED)
		return sjme_setErrorF(error, SJME_ERROR_LOCK_KEY_IN_USE, 0);

	/* Get the next locking key to use. */
	useKey = sjme_memIo_lockNextLockKey();

#if defined(SJME_THREADS_PTHREAD)
	/* Try locking. */
	errorCode = pthread_mutex_trylock(&lock->mutex);
	if (0 != errorCode)
	{
		/* If not busy, then fail here. */
		if (errorCode != EBUSY)
			return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, errorCode);

		/* Normal fail otherwise. */
		return sjme_false;
	}
#else
	/* We need to be the thread that owns this lock. */
	/* If it fails, someone else already owns the lock. */
	currentThreadId = sjme_memIo_threadCurrentId();
	if (!sjme_memIo_atomicIntPointerCompareThenSet(&lock->thread,
			SJME_MEMIO_INVALID_THREAD_ID, currentThreadId))
	{
		/* Are we the owner or not? */
		if (!sjme_memIo_atomicIntPointerCompareThenSet(&lock->thread,
				currentThreadId, currentThreadId))
			return sjme_false;
	}
#endif

	/* Increase the lock count only once. */
	sjme_memIo_atomicIntGetThenAdd(&lock->count, 1);

	/* Set new key if currently unlocked and use that one. */
	sjme_memIo_atomicIntCompareThenSet(&lock->lock,
		SJME_MEMIO_UNLOCKED, useKey);
	key->key = sjme_memIo_atomicIntGet(&lock->lock);

	/* Memory barrier before we leave. */
	sjme_memIo_memoryBarrier();

	/* Success! */
	return sjme_true;
}

sjme_jboolean sjme_memIo_unlock(sjme_memIo_spinLock* lock,
	sjme_memIo_spinLockKey* key, sjme_error* error)
{
#if defined(SQUIRRELJME_THREADS_PTHREAD)
	int errorCode;
#else
	sjme_memIo_threadId currentThreadId;
#endif
	sjme_jint existingKey, currentCount;
	sjme_jboolean result;
	sjme_jint useKey;
	
	if (lock == NULL || key == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Make sure the unlocking key is valid before we try using it. */
	useKey = key->key;
	if (useKey == SJME_MEMIO_UNLOCKED)
	{
		sjme_setError(error, SJME_ERROR_INVALID_UNLOCK_KEY, useKey);

		return sjme_false;
	}

	/* Default to failure. */
	result = sjme_false;

#if defined(SQUIRRELJME_THREADS_PTHREAD)
	/* Lock before we unlock, because we will check the key. */
	errorCode = pthread_mutex_trylock(&lock->mutex);
	if (0 != errorCode)
	{
		/* If the lock is busy, chances are another thread owns it. */
		if (errorCode == EBUSY)
			sjme_setError(error, SJME_ERROR_NOT_LOCK_OWNER, existingKey);

		/* Otherwise another failure. */
		return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, 0);
	}
#else
	/* Perform an atomic check, keep same value if matched, */
	/* this is so that we know we are the owner of this lock. */
	currentThreadId = sjme_memIo_threadCurrentId();
	if (!sjme_memIo_atomicIntPointerCompareThenSet(&lock->thread,
			currentThreadId, currentThreadId))
		sjme_setError(error, SJME_ERROR_NOT_LOCK_OWNER, currentThreadId);
#endif

	/* Only perform unlock logic if the key matches. */
	existingKey = sjme_memIo_atomicIntGet(&lock->lock);
	if (existingKey == useKey)
	{
		/* Reduce count by one first, before do an unlock. */
		currentCount = sjme_memIo_atomicIntGetThenAdd(&lock->count,
			-1);

		/* We are the last to unlock, so clear the lock state in the key. */
		if (currentCount == 1)
		{
			/* Clear key and set as unlocked. */
			sjme_memIo_atomicIntSet(&lock->lock,
				SJME_MEMIO_UNLOCKED);

#if !defined(SQUIRRELJME_THREADS_PTHREAD)
			/* We no longer own this lock, so just say as such. */
			sjme_memIo_atomicIntPointerSet(&lock->thread,
				SJME_MEMIO_INVALID_THREAD_ID);
#endif
		}

#if defined(SQUIRRELJME_THREADS_PTHREAD)
		/* Unlock the key for this once, from tryLock or lock. */
		if (0 != pthread_mutex_unlock(&lock->mutex))
			result = sjme_false;

		/* Otherwise is success here... */
		else
			result = sjme_true;
#else
		/* Result passes here. */
		result = sjme_true;
#endif
	}

	/* Set as not lock owner. */
	else
		sjme_setError(error, SJME_ERROR_NOT_LOCK_OWNER, existingKey);

#if defined(SQUIRRELJME_THREADS_PTHREAD)
	/* Always unlock before leaving, since we did do a lock. */
	if (0 != pthread_mutex_unlock(&lock->mutex))
		result = sjme_false;
#endif

	/* Memory barrier before we leave. */
	sjme_memIo_memoryBarrier();

	/* Use whatever result from the unlock we just performed. */
	return result;
}
