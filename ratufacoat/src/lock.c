/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#if defined(SQUIRRELJME_THREADS)
	#if defined(SQUIRRELJME_THREADS_WIN32)
		#include <windows.h>
	#elif defined(SQUIRRELJME_THREADS_PTHREAD)
		#include <pthread.h>
	#endif
#endif

#include "debug.h"
#include "lock.h"

/** The unlocked key value. */
#define SJME_UNLOCKED SJME_JINT_C(0)

/** The next locking key to use. */
static sjme_memIo_atomicInt sjme_nextLockKey;

/**
 * Attempts to create a memory barrier.
 *
 * @since 2022/12/10
 */
static void sjme_memoryBarrier(void)
{
#if defined(SQUIRRELJME_THREADS_WIN32)
	MemoryBarrier();
#elif defined(SJME_FEATURE_GCC) && !defined(SJME_FEATURE_OLD_GCC)
	__sync_synchronize();
#endif
}

/**
 * Attempts a lock shift.
 * 
 * @param lock The lock to lock.
 * @param keyFrom The key that should be in the lock.
 * @param keyTo The key to shift to.
 * @return Will return @c sjme_true on a successful lock.
 * @since 2022/04/01 
 */
static sjme_jboolean sjme_lockShift(sjme_spinLock* lock,
	sjme_jint keyFrom, sjme_jint keyTo)
{
	sjme_jint result;

	/* Try shifting the lock now. */
	sjme_memoryBarrier();
	result = sjme_memIo_atomicIntCompareThenSet(&lock->lock,
		keyFrom, keyTo);
	sjme_memoryBarrier();

	return result;
}

sjme_jboolean sjme_lock(sjme_spinLock* lock, sjme_spinLockKey* key,
	sjme_error* error)
{
	sjme_jint useKey;
	
	if (lock == NULL || key == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Get the next locking key to use. */
	useKey = sjme_memIo_atomicIntGetThenAdd(&sjme_nextLockKey, 1) + 1;
	
	/* Burn forever trying to use the given key. */
	while (!sjme_lockShift(lock, SJME_UNLOCKED, useKey))
		;
	
	/* Set key and return. */
	key->key = useKey;
	return sjme_true;
}

sjme_jboolean sjme_tryLock(sjme_spinLock* lock, sjme_spinLockKey* key,
	sjme_error* error)
{
	sjme_jint useKey;
	
	if (lock == NULL || key == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Get the next locking key to use. */
	useKey = sjme_memIo_atomicIntGetThenAdd(&sjme_nextLockKey, 1) + 1;
	
	/* Attempt only once to lock. */
	if (!sjme_lockShift(lock, SJME_UNLOCKED, useKey))
		return sjme_false;
	
	/* Set key and return. */
	key->key = useKey;
	return sjme_true;
}

sjme_jboolean sjme_unlock(sjme_spinLock* lock, sjme_spinLockKey* key,
	sjme_error* error)
{
	sjme_jint useKey;
	
	if (lock == NULL || key == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Make sure the unlocking key is valid before we try using it. */
	useKey = key->key;
	if (useKey == SJME_UNLOCKED)
	{
		sjme_setError(error, SJME_ERROR_INVALID_UNLOCK_KEY, useKey);
		
		return sjme_false;
	}
	
	/* Perform unlock. */
	if (!sjme_lockShift(lock, useKey, SJME_UNLOCKED))
	{
		sjme_setError(error, SJME_ERROR_NOT_LOCK_OWNER, useKey);
		
		return sjme_false;
	}
	
	/* Success! */
	return sjme_true;
}
