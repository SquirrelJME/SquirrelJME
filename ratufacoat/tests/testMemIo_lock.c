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
	sjme_memIo_spinLockKey badKey;

	/* Clear first. */
	memset(&lock, 0, sizeof(lock));

	/* Locking should succeed. */
	if (!sjme_memIo_tryLock(&lock, &key, &shim->error))
		return FAIL_TEST(1);

	/* Locking again should fail. */
	if (sjme_memIo_tryLock(&lock, &key, &shim->error))
		return FAIL_TEST(2);

	/* The lock value should be the same as the key. */
	if (key.key != sjme_memIo_atomicIntGet(&lock.lock))
		return FAIL_TEST(3);

	/* Should not be able to lock with a bad key. */
	badKey.key = key.key ^ 1337;
	if (sjme_memIo_unlock(&lock, &badKey, &shim->error))
		return FAIL_TEST(4);

	/* Should be the bad lock key error. */
	if (sjme_getError(&shim->error, 0) != SJME_ERROR_NOT_LOCK_OWNER)
		return FAIL_TEST_SUB(4, 1);

	/* Unlock should succeed. */
	if (!sjme_memIo_unlock(&lock, &key, &shim->error))
		return FAIL_TEST(5);

	/* Unlock should fail after trying with it already unlocked. */
	if (sjme_memIo_unlock(&lock, &key, &shim->error))
		return FAIL_TEST(6);

	return PASS_TEST();
}
