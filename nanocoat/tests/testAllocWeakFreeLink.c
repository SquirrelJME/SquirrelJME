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
	sjme_unit_equalZ(test, isBlockFree, SJME_JNI_TRUE,
		"Was not from block free?");
	
	/* Cancel instead. */
	return SJME_ERROR_NONE;
}

/**
 * Tests freeing a link and any changes to the resulting weak link.
 *  
 * @since 2024/07/01 
 */
SJME_TEST_DECLARE(testAllocWeakFreeLink)
{
	sjme_alloc_weak* weak;
	sjme_alloc_link* link;
	void* p;
	
	/* Allocate weak reference. */
	p = NULL;
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakNew(test->pool, 512,
		testEnqueue, weak, &p, &weak)))
		return sjme_unit_fail(test, "Failed to allocate weak?");
		
	/* Get the block link. */
	link = NULL;
	if (sjme_error_is(sjme_alloc_getLink(p, &link)))
		return sjme_unit_fail(test, "Could not get block link?");
	
	/* Free the underlying block. */
	test->global = NULL;
	if (sjme_error_is(sjme_alloc_free(p)))
		return sjme_unit_fail(test, "Could not free block?");
	
	/* The weak ref should still be valid. */
	sjme_unit_equalI(test,
		sjme_atomic_sjme_jint_get(&weak->valid), SJME_ALLOC_WEAK_VALID,
		"Weak reference now invalid?");
	
	/* Enqueue should have been called. */
	sjme_unit_equalP(test, test->global, test,
		"Enqueue was not called?");
	
	/* Reading invalid memory, but the weak should be cleared. */
	sjme_unit_notEqualP(test, link->weak, weak,
		"Weak ref in link not cleared?");
	
	/* The weak ref should still have a count. */
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&weak->count), 1,
		"Weak reference has incorrect count?");
	
	/* These should be cleared out. */
	sjme_unit_equalP(test, weak->pointer, NULL,
		"Pointer not cleared?");
	sjme_unit_equalP(test, weak->link, NULL,
		"Link not cleared?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
