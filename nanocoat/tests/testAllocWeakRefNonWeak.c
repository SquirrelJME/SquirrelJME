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

#include "sjme/alloc.h"

/**
 * Tests referencing a non-weak pointer.
 * 
 * @since 2024/07/08 
 */
SJME_TEST_DECLARE(testAllocWeakRefNonWeak)
{
	sjme_errorCode error;
	sjme_alloc_weak weak;
	void* p;
	
	/* Allocate. */
	p = NULL;
	if (sjme_error_is(sjme_alloc(test->pool, 512, &p)) || p == NULL)
		return sjme_unit_fail(test, "Could not allocate block?");
	
	/* Create weak reference, fails because not weak. */
	weak = NULL;
	error = sjme_alloc_weakRef(p, &weak,
		NULL, NULL);
	
	/* Test conditions. */
	sjme_unit_equalP(test, weak, NULL,
		"A weak pointer was allocated?");
	sjme_unit_equalI(test, error, SJME_ERROR_NOT_WEAK_REFERENCE,
		"Incorrect error was set?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
