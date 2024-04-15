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
#include "sjme/except.h"
#include "test.h"
#include "unit.h"

/**
 * Tests failing of the exception handler.
 * 
 * @since 2023/11/28
 */
SJME_TEST_DECLARE(testExceptFail)
{
	SJME_EXCEPT_VDEF;
	volatile sjme_exceptTrace* trace;

	/* Setup test. */
	trace = NULL;

	/* Fail. */
	exceptTraceE_sjme = 666;
SJME_EXCEPT_WITH(trace):
	SJME_EXCEPT_TOSS(SJME_ERROR_TOP_NOT_LONG);

	/* Should hopefully not be reached. */
	sjme_unit_fail(test, "Should not be reached here?");
	
SJME_EXCEPT_FAIL:
	sjme_unit_equalI(test, exceptTraceE_sjme, SJME_ERROR_TOP_NOT_LONG,
		"Error code was not set?");

	/* Success otherwise. */
	SJME_EXCEPT_DONE(SJME_TEST_RESULT_PASS);
}
