/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "memio/lock.h"

/**
 * Tests memory locks.
 *
 * @since 2023/03/26
 */
SJME_TEST_PROTOTYPE(testMemIo_lock)
{
	sjme_memIo_spinLock lock;
	sjme_memIo_spinLockKey key;
	sjme_memIo_spinLockKey freshKey;
	sjme_memIo_spinLockKey badKey;

	/* Initialize first. */
	memset(&lock, 0, sizeof(lock));
	if (!sjme_memIo_lockInit(&lock, &shim->error))
		return FAIL_TEST(1);

	/* Locking should succeed. */
	memset(&key, 0, sizeof(key));
	if (!sjme_memIo_lock(&lock, &key, &shim->error))
		return FAIL_TEST(2);

	/* Locking again with an active key should be a failure. */
	if (sjme_memIo_tryLock(&lock, &key, &shim->error))
		return FAIL_TEST(3);

	/* Locking again with a fresh key should be a success. */
	memset(&freshKey, 0, sizeof(freshKey));
	if (!sjme_memIo_tryLock(&lock, &freshKey, &shim->error))
		return FAIL_TEST(4);

	/* The lock value should be the same as the key. */
	if (key.key != sjme_memIo_atomicIntGet(&lock.lock))
		return FAIL_TEST(5);

	/* Should not be able to lock with a bad key. */
	badKey.key = key.key ^ 1337;
	if (sjme_memIo_unlock(&lock, &badKey, &shim->error))
		return FAIL_TEST(6);

	/* Should be the bad lock key error. */
	if (sjme_getError(&shim->error, 0) != SJME_ERROR_NOT_LOCK_OWNER)
		return FAIL_TEST_SUB(6, 1);

	/* Unlock should succeed. */
	if (!sjme_memIo_unlock(&lock, &key, &shim->error))
		return FAIL_TEST(7);

	/* Unlock should succeed again, because locked twice. */
	if (!sjme_memIo_unlock(&lock, &freshKey, &shim->error))
		return FAIL_TEST(8);

	/* Unlock should fail after trying with it already unlocked. */
	if (sjme_memIo_unlock(&lock, &key, &shim->error))
		return FAIL_TEST(9);

	/* Destroy lock. */
	if (!sjme_memIo_lockDestroy(&lock, &shim->error))
		return FAIL_TEST(10);

	return PASS_TEST();
}
