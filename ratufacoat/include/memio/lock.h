/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * This implements a basic spin lock.
 * 
 * @since 2022/03/27
 */

#ifndef SQUIRRELJME_LOCK_H
#define SQUIRRELJME_LOCK_H

#include "memio/atomic.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_LOCK_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This represents a spinlock which burns the CPU until the lock is obtained
 * 
 * @since 2022/03/27
 */
typedef struct sjme_memIo_spinLock
{
	/** The lock value. */
	sjme_memIo_atomicInt lock;
} sjme_memIo_spinLock;

/**
 * The key used to unlock a spinlock.
 * 
 * @since 2022/03/27
 */
typedef struct sjme_memIo_spinLockKey
{
	sjme_jint key;
} sjme_memIo_spinLockKey;

/**
 * Locks the given lock, this will block and burn CPU until a lock can be
 * obtained.
 * 
 * @param lock The lock that will be locked.
 * @param key The key that is generated after a lock is successful.
 * @param error Any resultant error state.
 * @return Will return @c sjme_true on a successful lock, otherwise
 * will return @c sjme_false if the parameters are not correct.
 * @since 2022/03/29
 */
sjme_jboolean sjme_memIo_lock(sjme_memIo_spinLock* lock,
	sjme_memIo_spinLockKey* key, sjme_error* error);

/**
 * Attempts to lock the given lock, if it cannot be done then this will
 * return @c sjme_false.
 * 
 * @param lock The lock that will be locked.
 * @param key The key that is generated after a lock is successful.
 * @param error Any resultant error state.
 * @return Will return @c sjme_true when the lock was obtained, otherwise
 * @c sjme_false will be returned instead.
 * @since 2022/04/01
 */
sjme_jboolean sjme_memIo_tryLock(sjme_memIo_spinLock* lock,
	sjme_memIo_spinLockKey* key, sjme_error* error);

/**
 * Unlocks the given lock with the specified key.
 * 
 * @param lock The lock to be unlocked with the given key.
 * @param key The key that is used to unlock the lock.
 * @param error Any resultant error state.
 * @return Will return @c sjme_true when the unlock was successful, otherwise
 * an error will occur.
 * @since 2022/03/30
 */
sjme_jboolean sjme_memIo_unlock(sjme_memIo_spinLock* lock,
	sjme_memIo_spinLockKey* key, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_LOCK_H
}
		#undef SJME_CXX_SQUIRRELJME_LOCK_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_LOCK_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LOCK_H */
