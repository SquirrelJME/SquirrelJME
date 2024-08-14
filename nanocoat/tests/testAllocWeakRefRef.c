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
 * Tests counting up a weak reference.
 *  
 * @since 2024/07/08 
 */
SJME_TEST_DECLARE(testAllocWeakRefRef)
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
	
	/* Reference count should be one. */
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&weak->count), 1,
		"Reference count not one?");
	
	/* Reference again. */
	weakB = NULL;
	if (sjme_error_is(sjme_alloc_weakRefE(p, &weakB,
		NULL, NULL)))
		return sjme_unit_fail(test, "Could not re-ref weak?");
	
	/* These should be the same weak reference. */
	sjme_unit_equalP(test, weak, weakB,
		"Weak references not the same?");
	
	/* Reference count should be two. */
	sjme_unit_equalI(test, sjme_atomic_sjme_jint_get(&weak->count), 2,
		"Reference count not two?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
