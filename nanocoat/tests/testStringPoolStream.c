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

#define TEST_LEN 12

static const sjme_cchar testData[TEST_LEN] =
{
	0, 10, 'S', 'q', 'u', 'i',
	'r', 'r', 'e', 'l', 's', '!'
};

/**
 * Tests stream based string pooling.
 *  
 * @since 2024/09/14 
 */
SJME_TEST_DECLARE(testStringPoolStream)
{
	sjme_nvm_stringPool stringPool;
	sjme_nvm_stringPool_string string;
	sjme_stream_input stream;
	
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
	
	/* Open stream over the data. */
	stream = NULL;
	if (sjme_error_is(test->error = sjme_stream_inputOpenMemory(
		test->pool, &stream,
		&testData[0], TEST_LEN)) || stream == NULL)
		return sjme_unit_fail(test, "Could not open stream.");
	
	/* Locate string. */
	string = NULL;
	if (sjme_error_is(test->error = sjme_nvm_stringPool_locateStream(
		stringPool, stream,
		&string)) || string == NULL)
		return sjme_unit_fail(test, "Could not locate string?");
	
	/* We are using this, so count it. */
	if (sjme_error_is(test->error = sjme_alloc_weakRef(string,
		NULL)))
		return sjme_unit_fail(test, "Could not count string?");
	
	/* Close stream. */
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(stream))))
		return sjme_unit_fail(test, "Could not close stream.");
	
	/* Check to make sure it is valid. */
	sjme_unit_equalI(test, 10, string->length,
		"Length incorrect?");
	sjme_unit_equalI(test,
		0, memcmp("Squirrels!", string->seq.context,
			string->length),
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
