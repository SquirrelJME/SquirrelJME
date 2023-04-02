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
#if defined(SQUIRRELJME_THREADS_WIN32)
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

#if !defined(SQUIRRELJME_THREADS_PTHREAD)
/**
 * Attempts a lock shift.
 * 
 * @param lock The lock to lock.
 * @param keyFrom The key that should be in the lock.
 * @param keyTo The key to shift to.
 * @return Will return @c sjme_true on a successful lock.
 * @since 2022/04/01 
 */
static sjme_jboolean sjme_memIo_lockShift(sjme_memIo_spinLock* lock,
	sjme_jint keyFrom, sjme_jint keyTo)
{
	sjme_jint result;

	/* Try shifting the lock now. */
	sjme_memIo_memoryBarrier();
	result = sjme_memIo_atomicIntCompareThenSet(&lock->lock,
		keyFrom, keyTo);
	sjme_memIo_memoryBarrier();

	return result;
}
#endif

sjme_jboolean sjme_memIo_lock(sjme_memIo_spinLock* lock,
	sjme_memIo_spinLockKey* key, sjme_error* error)
{
#if defined(SQUIRRELJME_THREADS_PTHREAD)
	int errorCode;
#endif
	sjme_jint useKey;
	
	if (lock == NULL || key == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Cannot use a key twice! */
	if (key->key != SJME_MEMIO_UNLOCKED)
		return sjme_setErrorF(error, SJME_ERROR_LOCK_KEY_IN_USE, 0);

	/* Get the next locking key to use. */
	useKey = sjme_memIo_lockNextLockKey();

#if defined(SQUIRRELJME_THREADS_PTHREAD)
	/* Lock and block, pthreads will halt the CPU accordingly. */
	/* Unlike tryLock(), this results in failure if non-zero. */
	if (0 != pthread_mutex_lock(&lock->mutex))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, errorCode);

	/* Increase the lock count. */
	sjme_memIo_atomicIntGetThenAdd(&lock->count, 1);

	/* Set new key if currently unlocked and use that one. */
	sjme_memIo_atomicIntCompareThenSet(&lock->lock,
		SJME_MEMIO_UNLOCKED, useKey);
	key->key = sjme_memIo_atomicIntGet(&lock->lock);

	/* Success! */
	return sjme_true;
#else
	/* Burn forever trying to use the given key. */
	while (!sjme_memIo_lockShift(lock, SJME_MEMIO_UNLOCKED,
		useKey))
		;
	
	/* Set key and return. */
	key->key = useKey;
	return sjme_true;
#endif
}

sjme_jboolean sjme_memIo_lockDestroy(sjme_memIo_spinLock* lock,
	sjme_error* error)
{
	if (lock == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

#if defined(SQUIRRELJME_THREADS_PTHREAD)
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

#if defined(SQUIRRELJME_THREADS_PTHREAD)
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
	/* Does nothing. */
	return sjme_true;
#endif
}

sjme_jboolean sjme_memIo_tryLock(sjme_memIo_spinLock* lock,
	sjme_memIo_spinLockKey* key,
	sjme_error* error)
{
#if defined(SQUIRRELJME_THREADS_PTHREAD)
	int errorCode;
#endif
	sjme_jint useKey;
	
	if (lock == NULL || key == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Cannot use a key twice! */
	if (key->key != SJME_MEMIO_UNLOCKED)
		return sjme_setErrorF(error, SJME_ERROR_LOCK_KEY_IN_USE, 0);

	/* Get the next locking key to use. */
	useKey = sjme_memIo_lockNextLockKey();

#if defined(SQUIRRELJME_THREADS_PTHREAD)
	/* Try locking. */
	errorCode = pthread_mutex_trylock(&lock->mutex);
	if (0 != errorCode)
	{
		/* If not busy, then just ignore. */
		if (errorCode != EBUSY)
			return sjme_setErrorF(error, SJME_ERROR_INVALID_LOCK, errorCode);

		/* Normal fail otherwise. */
		return sjme_false;
	}

	/* Increase the lock count only once. */
	sjme_memIo_atomicIntGetThenAdd(&lock->count, 1);

	/* Set new key if currently unlocked and use that one. */
	sjme_memIo_atomicIntCompareThenSet(&lock->lock,
		SJME_MEMIO_UNLOCKED, useKey);
	key->key = sjme_memIo_atomicIntGet(&lock->lock);

	/* Success! */
	return sjme_true;
#else
	/* Attempt only once to lock. */
	if (!sjme_memIo_lockShift(lock, SJME_MEMIO_UNLOCKED, useKey))
		return sjme_false;
	
	/* Set key and return. */
	key->key = useKey;
	return sjme_true;
#endif
}

sjme_jboolean sjme_memIo_unlock(sjme_memIo_spinLock* lock,
	sjme_memIo_spinLockKey* key, sjme_error* error)
{
#if defined(SQUIRRELJME_THREADS_PTHREAD)
	int errorCode;
	sjme_jint existingKey, currentCount;
	sjme_jboolean result;
#endif
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

#if defined(SQUIRRELJME_THREADS_PTHREAD)
	/* Default to failure. */
	result = sjme_false;

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

	/* Only perform unlock logic if the key matches. */
	existingKey = sjme_memIo_atomicIntGet(&lock->lock);
	if (existingKey == useKey)
	{
		/* Reduce count by one first, before do an unlock. */
		currentCount = sjme_memIo_atomicIntGetThenAdd(&lock->count,
			-1);

		/* We are the last to unlock, so clear the lock state in the key. */
		if (currentCount == 1)
			sjme_memIo_atomicIntSet(&lock->lock,
				SJME_MEMIO_UNLOCKED);

		/* Unlock the key for this once, from tryLock or lock. */
		if (0 != pthread_mutex_unlock(&lock->mutex))
			result = sjme_false;

		/* Otherwise is success here... */
		else
			result = sjme_true;
	}

	/* Set as not lock owner. */
	else
		sjme_setError(error, SJME_ERROR_NOT_LOCK_OWNER, existingKey);

	/* Always unlock before leaving, since we did do a lock. */
	if (0 != pthread_mutex_unlock(&lock->mutex))
		result = sjme_false;

	return result;
#else
	
	/* Perform unlock. */
	if (!sjme_memIo_lockShift(lock, useKey, SJME_MEMIO_UNLOCKED))
		return sjme_setErrorF(error, SJME_ERROR_NOT_LOCK_OWNER, useKey);

	/* Clear key value. */
	key->key = SJME_MEMIO_UNLOCKED;
	
	/* Success! */
	return sjme_true;
#endif
}
