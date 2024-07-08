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
	sjme_attrInNotNull sjme_alloc_weak* weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree)
{
	sjme_test* test;
	
	test = data;
	
	test->global = data;
	
	/* Should not be free block free. */
	sjme_unit_equalZ(test, isBlockFree, SJME_JNI_FALSE,
		"Was from block free?");
	
	return SJME_ERROR_NONE;
}

/**
 * Tests freeing a weak reference by freeing the weak reference.
 * 
 * @since 2024/07/01 
 */
SJME_TEST_DECLARE(testAllocWeakDelete)
{
	sjme_alloc_weak* weak;
	sjme_alloc_link* link;
	sjme_alloc_link* weakLink;
	void* p;
	
	/* Allocate weak reference. */
	p = NULL;
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakNew(test->pool, 512,
		testEnqueue, test, &p, &weak)))
		return sjme_unit_fail(test, "Failed to allocate weak?");

	/* Get the block link. */
	link = NULL;
	if (sjme_error_is(sjme_alloc_getLink(p, &link)))
		return sjme_unit_fail(test, "Could not get block link?");
		
	/* Get the weak link. */
	weakLink = NULL;
	if (sjme_error_is(sjme_alloc_getLink(weak, &weakLink)))
		return sjme_unit_fail(test, "Could not get weak link?");
	
	/* Delete it. */
	test->global = NULL;
	if (sjme_error_is(sjme_alloc_weakDelete(&weak)))
		return sjme_unit_fail(test, "Could not delete weak?");
	
	/* Should be cleared and enqueue called. */
	sjme_unit_equalP(test, test->global, test,
		"Enqueue was not called?");
	sjme_unit_equalP(test, weak, NULL,
		"Weak reference not set to NULL?");
	
	/* Weak reference should be invalid. */
	sjme_unit_notEqualI(test,
		sjme_atomic_sjme_jint_get(&weak->valid), SJME_ALLOC_WEAK_VALID,
		"Weak reference not marked invalid?");
	
	/* Block and weak links should be free too. */
	sjme_unit_notEqualI(test, link->space, SJME_ALLOC_POOL_SPACE_USED,
		"Block link is used?");
	sjme_unit_notEqualI(test, weakLink->space, SJME_ALLOC_POOL_SPACE_USED,
		"Weak link is used?");
		
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
