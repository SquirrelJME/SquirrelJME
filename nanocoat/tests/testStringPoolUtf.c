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
	sjme_nvm_stringPool stringPool;
	sjme_nvm_stringPool_string string, stringTwo;
	
	/* Create string pool. */
	stringPool = NULL;
	if (sjme_error_is(test->error = sjme_nvm_stringPool_new(
		test->pool, &stringPool)) ||
		stringPool == NULL)
		return sjme_unit_fail(test, "Could not create pool.");
	
	/* We are using this, so count it. */
	if (sjme_error_is(test->error = sjme_alloc_weakRef(stringPool,
		NULL)))
		return sjme_unit_fail(test, "Could not count string pool?");
	
	/* Locate string. */
	string = NULL;
	if (sjme_error_is(test->error = sjme_nvm_stringPool_locateUtf(
		stringPool, testUtf, -1,
		&string)) || string == NULL)
		return sjme_unit_fail(test, "Could not locate string?");
	
	/* We are using this, so count it. */
	if (sjme_error_is(test->error = sjme_alloc_weakRef(string,
		NULL)))
		return sjme_unit_fail(test, "Could not count string?");
	
	/* Check to make sure it is valid. */
	sjme_unit_equalI(test, 10, string->length,
		"Length incorrect?");
	sjme_unit_notEqualP(test, testUtf, string->seq.context,
		"Copy was not made?");
	sjme_unit_notEqualP(test, testUtf, &string->chars[0],
		"Copy was not made?");
	
	/* Should be first. */
	sjme_unit_equalP(test, string, stringPool->strings->elements[0],
		"Not placed in first pool spot?");
		
	/* Locate string, again. */
	stringTwo = NULL;
	if (sjme_error_is(test->error = sjme_nvm_stringPool_locateUtf(
		stringPool, testUtf, -1,
		&stringTwo)) || stringTwo == NULL)
		return sjme_unit_fail(test, "Could not locate second string?");
	
	/* We are using this, so count it. */
	if (sjme_error_is(test->error = sjme_alloc_weakRef(stringTwo,
		NULL)))
		return sjme_unit_fail(test, "Could not count second string?");
	
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
