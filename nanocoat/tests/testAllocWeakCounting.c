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
 * Tests counting of weak references.
 *  
 * @since 2024/07/01 
 */
SJME_TEST_DECLARE(testAllocWeakCounting)
{
	sjme_alloc_weak weak;
	sjme_alloc_weak weakB;
	sjme_pointer p;
	
	/* Allocate weak reference. */
	p = NULL;
	weak = NULL;
	if (sjme_error_is(sjme_alloc_weakNew(test->pool, 512,
		NULL, NULL, &p, &weak)))
		return sjme_unit_fail(test, "Failed to allocate weak?");
	
	/* Reference count should be one. */
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&weak->count), 1,
		"Reference count not one?");
	
	/* Reference again. */
	weakB = NULL;
	if (sjme_error_is(sjme_alloc_weakRef(p, &weakB)))
		return sjme_unit_fail(test, "Could not re-ref weak?");
	
	/* Reference count should be two. */
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&weak->count), 2,
		"Reference count not two?");
	
	/* Delete weak reference. */
	if (sjme_error_is(sjme_alloc_weakDelete(&weakB)))
		return sjme_unit_fail(test, "Could not delete weak?");
	
	/* Since it was two, it should still be there. */
	sjme_unit_equalP(test, weakB, weak,
		"Second weak pointer changed?");
		
	/* Reference count should be one. */
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&weak->count), 1,
		"Reference count not one?");
	
	/* Delete weak reference again. */
	if (sjme_error_is(sjme_alloc_weakDelete(&weakB)))
		return sjme_unit_fail(test, "Could not delete weak?");
		
	/* Since it was one, it should be gone now. */
	sjme_unit_equalP(test, weakB, NULL,
		"Weak pointer on last delete not cleared?");
	
	/* This should be marked invalid. */
	sjme_unit_notEqualI(test,
		sjme_atomic_sjme_jint_get(&weak->valid), SJME_ALLOC_WEAK_VALID,
		"Weak reference not marked invalid?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
