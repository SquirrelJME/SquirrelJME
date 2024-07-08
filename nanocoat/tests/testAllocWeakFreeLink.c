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
		NULL, NULL, &p, &weak)))
		return sjme_unit_fail(test, "Failed to allocate weak?");
		
	/* Get the block link. */
	link = NULL;
	if (sjme_error_is(sjme_alloc_getLink(p, &link)))
		return sjme_unit_fail(test, "Could not get block link?");
	
	/* Free the underlying block. */
	if (sjme_error_is(sjme_alloc_free(p)))
		return sjme_unit_fail(test, "Could not free block?");
	
	/* Reading invalid memory, but the weak should be cleared. */
	sjme_unit_notEqualP(test, link->weak, weak,
		"Weak ref in link not cleared?");
	
	/* The weak ref should still have a count. */
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&weak->count), 1,
		"Weak reference has no count?");
	
	/* These should be cleared out. */
	sjme_unit_equalP(test, weak->pointer, NULL,
		"Pointer not cleared?");
	sjme_unit_equalP(test, weak->link, NULL,
		"Link not cleared?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
