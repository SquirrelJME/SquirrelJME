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
 * Tests general creation of a weak reference.
 *  
 * @since 2024/07/01 
 */
SJME_TEST_DECLARE(testAllocWeakRef)
{
	sjme_alloc_weak weak;
	sjme_alloc_weak weakSecond;
	sjme_alloc_link* link;
	sjme_pointer p;
	
	/* Allocate weak reference. */
	p = NULL;
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakNew(test->pool, 512,
		NULL, NULL, &p, &weak)))
		return sjme_unit_fail(test, "Failed to allocate weak?");

	/* These should be set. */
	sjme_unit_notEqualP(test, p, NULL,
		"Did not set p?");
	sjme_unit_notEqualP(test, weak, NULL,
		"Did not set weak?");
	
	/* This should be marked valid. */
	sjme_unit_equalI(test,
		sjme_atomic_sjme_jint_get(&weak->valid), SJME_ALLOC_WEAK_VALID,
		"Weak reference not marked valid?");
	
	/* Get the block link. */
	link = NULL;
	if (sjme_error_is(sjme_alloc_getLink(p, &link)))
		return sjme_unit_fail(test, "Could not get block link?");
	
	/* Should be this pointer. */
	sjme_unit_equalP(test, weak->pointer, p,
		"Weak pointer not allocated pointer?");
	sjme_unit_equalP(test, weak->link, link,
		"Weak pointer not allocated link?");
	sjme_unit_equalP(test, link->weak, weak,
		"Link weak not allocated weak?");
	
	/* These should not be set. */
	sjme_unit_equalP(test, weak->enqueue, NULL,
		"Enqueue was set?");
	sjme_unit_equalP(test, weak->enqueueData, NULL,
		"Enqueue data was set?");
	
	/* Reference count should be zero. */
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&weak->count), 0,
		"Reference count not one?");
	
	/* Count up. */
	weakSecond = NULL;
	if (sjme_error_is(sjme_alloc_weakRef(p, &weakSecond)))
		return sjme_unit_fail(test, "Could not weak ref count up?");
	
	/* Weak reference should be returned. */
	sjme_unit_equalP(test, weak, weakSecond,
		"Different or un-passed weak pointer?");
	
	/* Reference count should be one. */
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&weak->count), 1,
		"Reference count not one?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
