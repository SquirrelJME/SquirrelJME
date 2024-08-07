/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "test.h"
#include "proto.h"
#include "mock.h"
#include "unit.h"
#include "sjme/multithread.h"

/**
 * Tests spinlocks.
 *  
 * @since 2024/07/31 
 */
SJME_TEST_DECLARE(testSpinLock)
{
	sjme_errorCode error;
	sjme_thread_spinLock lock;
	sjme_jint count;
	
	/* Zero out lock state. */
	memset(&lock, 0, sizeof(lock));
	
	/* Lock. */
	error = sjme_thread_spinLockGrab(&lock);
	sjme_unit_equalI(test, error, SJME_ERROR_NONE,
		"Spin lock grab failed?");
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&lock.count), 1,
		"Spin lock count not one?");
		
	/* Lock. */
	error = sjme_thread_spinLockGrab(&lock);
	sjme_unit_equalI(test, error, SJME_ERROR_NONE,
		"Spin lock grab again failed?");
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&lock.count), 2,
		"Spin lock count not two?");
	
	/* Unlock. */
	count = -2;
	error = sjme_thread_spinLockRelease(&lock, &count);
	sjme_unit_equalI(test, error, SJME_ERROR_NONE,
		"Spin lock release failed?");
	sjme_unit_equalI(test, count, 1,
		"Unlock count is not one?");
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&lock.count), 1,
		"Spin lock count not one?");
		
	/* Unlock. */
	count = -2;
	error = sjme_thread_spinLockRelease(&lock, &count);
	sjme_unit_equalI(test, error, SJME_ERROR_NONE,
		"Spin lock release again failed?");
	sjme_unit_equalI(test, count, 0,
		"Unlock count is not zero?");
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&lock.count), 0,
		"Spin lock count not zero?");
	
	/* Test done. */
	return SJME_TEST_RESULT_PASS;
}
