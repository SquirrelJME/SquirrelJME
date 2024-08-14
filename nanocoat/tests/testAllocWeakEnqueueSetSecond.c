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

/**
 * Tests second set of enqueue.
 *  
 * @since 2024/07/08 
 */
SJME_TEST_DECLARE(testAllocWeakEnqueueSetSecond)
{
	sjme_alloc_weak weak;
	sjme_alloc_weak weakB;
	void* p;
	
	/* Allocate weak reference. */
	p = NULL;
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakNew(test->pool, 512,
		NULL, NULL, &p, &weak)))
		return sjme_unit_fail(test, "Failed to allocate weak?");
	
	/* The enqueues should be set. */
	sjme_unit_equalP(test, weak->enqueue, NULL,
		"Enqueue function was set?");
	sjme_unit_equalP(test, weak->enqueueData, NULL,
		"Enqueue data was was set?");
	
	/* Add enqueue and data in. */
	weakB = NULL;
	if (sjme_error_is(sjme_alloc_weakRefE(p, &weakB,
		testEnqueue, test)))
		return sjme_unit_fail(test, "Weak ref failed?");
	
	/* The enqueues should be set. */
	sjme_unit_equalP(test, weak->enqueue, testEnqueue,
		"Enqueue function not set?");
	sjme_unit_equalP(test, weak->enqueueData, test,
		"Enqueue data was not set?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
