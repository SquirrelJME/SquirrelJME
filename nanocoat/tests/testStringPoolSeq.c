/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/stringPool.h"
#include "test.h"
#include "proto.h"
#include "mock.h"
#include "unit.h"

static const sjme_lpcstr testUtf = "Squirrels!";

/**
 * Tests sequence based string pool.
 *  
 * @since 2024/09/14 
 */
SJME_TEST_DECLARE(testStringPoolSeq)
{
	sjme_stringPool stringPool;
	sjme_stringPool_string string;
	sjme_charSeq seq;
	
	/* Create string pool. */
	stringPool = NULL;
	if (sjme_error_is(test->error = sjme_stringPool_new(
		test->pool, &stringPool)) ||
		stringPool == NULL)
		return sjme_unit_fail(test, "Could not create pool.");
	
	/* Open char sequence over the data. */
	memset(&seq, 0, sizeof(seq));
	if (sjme_error_is(test->error = sjme_charSeq_newUtfStatic(
		&seq, testUtf)))
		return sjme_unit_fail(test, "Could not create char sequence.");
	
	/* Locate string. */
	string = NULL;
	if (sjme_error_is(test->error = sjme_stringPool_locateSeq(
		stringPool, &seq,
		&string)) || string == NULL)
		return sjme_unit_fail(test, "Could not locate string?");
	
	/* Check to make sure it is valid. */
	sjme_unit_equalI(test, 9, string->length,
		"Length incorrect?");
	sjme_unit_notEqualP(test, testUtf, string->seq.context,
		"Copy was not made?");
	sjme_unit_notEqualI(test, 0, strcmp("Squirrels!", string->seq.context),
		"String not equal?");
	
	/* Should be first. */
	sjme_unit_equalP(test, string, stringPool->strings->elements[0],
		"Not placed in first pool spot?");
	
	/* Close string. */
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(string))))
		return sjme_unit_fail(test, "Could not close first string?");
	
	/* Close string pool. */
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(stringPool))))
		return sjme_unit_fail(test, "Could not close pool.");
	
	/* Pass! */
	return SJME_TEST_RESULT_PASS;
}
