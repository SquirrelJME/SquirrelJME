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

static sjme_errorCode testEnqueue(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree)
{
	return SJME_ERROR_NONE;
}

static sjme_errorCode testEnqueueOther(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree)
{
	return SJME_ERROR_NONE;
}

/**
 * Tests first set of enqueue.
 *  
 * @since 2024/07/08 
 */
SJME_TEST_DECLARE(testAllocWeakEnqueueSetFirst)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;
	sjme_alloc_weak weakB;
	void* p;
	
	/* Allocate weak reference. */
	p = NULL;
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakNew(test->pool, 512,
		testEnqueue, test, &p, &weak)))
		return sjme_unit_fail(test, "Failed to allocate weak?");
	
	/* The enqueues should be set. */
	sjme_unit_equalP(test, weak->enqueue, testEnqueue,
		"Enqueue function not set?");
	sjme_unit_equalP(test, weak->enqueueData, test,
		"Enqueue data was not set?");
	
	/* Referencing a weak with the same enqueue should not fail. */
	weakB = NULL;
	error = sjme_alloc_weakRef(p, &weakB,
		testEnqueue, test);
	sjme_unit_equalI(test, error, SJME_ERROR_NONE,
		"Set of same enqueue failed?");
	
	/* Changing the data should fail. */
	error = sjme_alloc_weakRef(p, &weakB,
		testEnqueue, p);
	sjme_unit_equalI(test, error, SJME_ERROR_ENQUEUE_ALREADY_SET,
		"Set of different data passed?");
	
	/* Changing the function should fail. */
	error = sjme_alloc_weakRef(p, &weakB,
		testEnqueueOther, test);
	sjme_unit_equalI(test, error, SJME_ERROR_ENQUEUE_ALREADY_SET,
		"Set of different data passed?");
	
	/* Changing both should fail. */
	error = sjme_alloc_weakRef(p, &weakB,
		testEnqueueOther, p);
	sjme_unit_equalI(test, error, SJME_ERROR_ENQUEUE_ALREADY_SET,
		"Set of different both passed?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
