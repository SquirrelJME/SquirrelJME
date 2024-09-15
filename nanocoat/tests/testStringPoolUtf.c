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

static const sjme_lpcstr testUtf = "Squirrels!";

/**
 * Tests UTF string pooling.
 *  
 * @since 2024/09/14 
 */
SJME_TEST_DECLARE(testStringPoolUtf)
{
	sjme_stringPool stringPool;
	sjme_stringPool_string string, stringTwo;
	
	/* Create string pool. */
	stringPool = NULL;
	if (sjme_error_is(test->error = sjme_stringPool_new(
		test->pool, &stringPool)) ||
		stringPool == NULL)
		return sjme_unit_fail(test, "Could not create pool.");
	
	/* Locate string. */
	string = NULL;
	if (sjme_error_is(test->error = sjme_stringPool_locateUtf(
		stringPool, testUtf,
		&string)) || string == NULL)
		return sjme_unit_fail(test, "Could not locate string?");
	
	/* Check to make sure it is valid. */
	sjme_unit_equalI(test, 9, string->length,
		"Length incorrect?");
	sjme_unit_notEqualP(test, testUtf, string->seq.context,
		"Copy was not made?");
	
	/* Should be first. */
	sjme_unit_equalP(test, string, stringPool->strings->elements[0],
		"Not placed in first pool spot?");
		
	/* Locate string, again. */
	string = NULL;
	if (sjme_error_is(test->error = sjme_stringPool_locateUtf(
		stringPool, testUtf,
		&stringTwo)) || string == NULL)
		return sjme_unit_fail(test, "Could not locate string?");
	
	/* Should be the same. */
	sjme_unit_equalP(test, string, stringTwo,
		"Different string?");
	
	/* Close both. */
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(string))))
		return sjme_unit_fail(test, "Could not close first string?");
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(stringTwo))))
		return sjme_unit_fail(test, "Could not close second string?");
	
	/* Close string pool. */
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(stringPool))))
		return sjme_unit_fail(test, "Could not close pool.");
	
	/* Pass! */
	return SJME_TEST_RESULT_PASS;
}
