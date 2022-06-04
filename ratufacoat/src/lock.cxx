/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "lock.h"

/** The unlocked key value. */
#define SJME_UNLOCKED SJME_JINT_C(0)

/** The next locking key to use. */
static sjme_atomicInt sjme_nextLockKey;

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
	return sjme_atomicIntCompareThenSet(&lock->lock,
		keyFrom, keyTo);
}

sjme_jboolean sjme_lock(sjme_spinLock* lock, sjme_spinLockKey* key,
	sjme_error* error)
{
	sjme_jint useKey;

	if (lock == NULL || key == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	/* Get the next locking key to use. */
	useKey = sjme_atomicIntGetThenAdd(&sjme_nextLockKey, 1) + 1;

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
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	/* Get the next locking key to use. */
	useKey = sjme_atomicIntGetThenAdd(&sjme_nextLockKey, 1) + 1;

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
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	/* Make sure the unlocking key is valid before we try using it. */
	useKey = key->key;
	if (useKey == SJME_UNLOCKED)
	{
		sjme_setError(error, SJME_INVALID_UNLOCK_KEY, useKey);

		return sjme_false;
	}

	/* Perform unlock. */
	if (!sjme_lockShift(lock, useKey, SJME_UNLOCKED))
	{
		sjme_setError(error, SJME_NOT_LOCK_OWNER, useKey);

		return sjme_false;
	}

	/* Success! */
	return sjme_true;
}
