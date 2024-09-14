/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/stringPool.h"
#include "test.h"
#include "proto.h"
#include "mock.h"
#include "unit.h"

/**
 * Basic initialization and destroy of string pool.
 *  
 * @since 2024/09/14 
 */
SJME_TEST_DECLARE(testStringPool)
{
	sjme_stringPool stringPool;
	
	/* Create string pool. */
	stringPool = NULL;
	if (sjme_error_is(test->error = sjme_stringPool_new(
		test->pool, &stringPool)) ||
		stringPool == NULL)
		return sjme_unit_fail(test, "Could not create pool.");
	
	/* These should be properly set. */
	sjme_unit_notEqualP(test, NULL, stringPool->strings,
		"Strings not set?");
	sjme_unit_greaterEqualI(test, 1, stringPool->strings->length,
		"Initial strings length zero?");
	
	/* Close string pool. */
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(stringPool))))
		return sjme_unit_fail(test, "Could not close pool.");
	
	/* Pass! */
	return SJME_TEST_RESULT_PASS;
}
