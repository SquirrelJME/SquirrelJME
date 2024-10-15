/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "test.h"
#include "proto.h"
#include "mock.h"
#include "unit.h"
#include "testCloseable.h"

static sjme_jint closeCount = 0;

static sjme_errorCode testHandler(
	sjme_attrInNotNull sjme_closeable closeable)
{
	if (closeable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Bump up count. */
	closeCount++;
	
	return SJME_ERROR_NONE;
}

/**
 * Tests double close of closeable, in oneshot mode.
 *  
 * @since 2024/09/28 
 */
SJME_TEST_DECLARE(testCloseableDoubleClose)
{
	sjme_closeable closing;
	sjme_alloc_weak weak;
	
	/* Make closeable. */
	if (sjme_error_is(test->error = testCloseable_new(test->pool,
		&closing, testHandler,
		SJME_JNI_FALSE)))
		return sjme_unit_fail(test, "Could not make closeable?");
		
	/* Count up. */
	if (sjme_error_is(test->error = sjme_alloc_weakRef(closing, NULL)))
		return sjme_unit_fail(test, "Could not count 1?");
	if (sjme_error_is(test->error = sjme_alloc_weakRef(closing, NULL)))
		return sjme_unit_fail(test, "Could not count 2?");
	
	/* Should not be closed. */
	sjme_unit_equalI(test, 0, closeCount,
		"Was closed???");
	
	/* Close it. */
	if (sjme_error_is(test->error = sjme_closeable_close(closing)))
		return sjme_unit_fail(test, "Failed closing once?");
	
	/* Only once. */
	sjme_unit_equalI(test, 1, closeCount,
		"Not closed?");
		
	/* Weak ref should still be valid */
	weak = NULL;
	if (sjme_error_is(test->error = sjme_alloc_weakRefGet(closing,
		&weak)))
		return sjme_unit_fail(test, "Could not get weak ref");
	sjme_unit_notEqualP(test, NULL, weak,
		"No weak reference returned?");
	sjme_unit_equalI(test, 1, sjme_atomic_sjme_jint_get(&weak->count),
		"Wrong count?");
	
	/* Close it, again! */
	if (sjme_error_is(test->error = sjme_closeable_close(closing)))
		return sjme_unit_fail(test, "Failed closing twice?");
	
	/* Still only once. */
	sjme_unit_equalI(test, 1, closeCount,
		"Close was called multiple times?");
		
	/* Should not be a weak reference, as it is now gone! */
	weak = NULL;
	test->error = sjme_alloc_weakRefGet(closing, &weak);
	sjme_unit_equalI(test, SJME_ERROR_NOT_WEAK_REFERENCE, test->error,
		"Get of weak did not fail?");
	sjme_unit_equalP(test, NULL, weak,
		"There was a weak pointer returned?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
