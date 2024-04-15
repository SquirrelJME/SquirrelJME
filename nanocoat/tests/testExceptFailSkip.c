/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "proto.h"
#include "test.h"
#include "sjme/except.h"
#include "unit.h"

static sjme_errorCode skippedExcept(sjme_test* test,
	volatile sjme_exceptTrace* trace)
{
	SJME_EXCEPT_VDEF;

SJME_EXCEPT_WITH(trace):
	SJME_EXCEPT_TOSS(SJME_ERROR_TOP_NOT_LONG);

	sjme_unit_fail(test, "Skipped point should not be reached.");

SJME_EXCEPT_FAIL:
	/* Should be set. */
	sjme_unit_equalI(test, exceptTraceE_sjme, SJME_ERROR_TOP_NOT_LONG,
		"Skipped error code was not set?");

	/* Success otherwise. */
	SJME_EXCEPT_DONE_RETURN_ERROR();
}

static sjme_errorCode nestedExcept(sjme_test* test,
	volatile sjme_exceptTrace* trace)
{
	SJME_EXCEPT_VDEF;

SJME_EXCEPT_WITH(trace):
	if (sjme_error_is(skippedExcept(test, trace)))
		SJME_EXCEPT_TOSS_SAME();

	sjme_unit_fail(test, "Nested point should not be reached.");

SJME_EXCEPT_FAIL:
	/* Should be set. */
	sjme_unit_equalI(test, exceptTraceE_sjme, SJME_ERROR_TOP_NOT_LONG,
		"Nested error code was not set?");

	/* Success otherwise. */
	SJME_EXCEPT_DONE_RETURN_ERROR();
}

/**
 * Tests skipped exception handling.
 *
 * @since 2023/12/08
 */
SJME_TEST_DECLARE(testExceptFailSkip)
{
	SJME_EXCEPT_VDEF;
	volatile sjme_exceptTrace* trace;

	/* Setup test. */
	trace = NULL;

	/* Fail. */
	exceptTraceE_sjme = 666;
SJME_EXCEPT_WITH(trace):
	if (sjme_error_is(nestedExcept(test, trace)))
		SJME_EXCEPT_TOSS_SAME();

	/* Should hopefully not be reached. */
	sjme_unit_fail(test, "Should not be reached here?");

SJME_EXCEPT_FAIL:
	/* Should be set. */
	sjme_unit_equalI(test, exceptTraceE_sjme, SJME_ERROR_TOP_NOT_LONG,
		"Error code was not set?");

	/* Success otherwise. */
	SJME_EXCEPT_DONE(SJME_TEST_RESULT_PASS);
}
