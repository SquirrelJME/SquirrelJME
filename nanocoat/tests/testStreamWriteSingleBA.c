/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"
#include "sjme/stream.h"

static sjme_errorCode finishStreamWriteSingleBA(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_resultByteArray* result,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_jint i;
	sjme_test* test;

	/* Recover test. */
	test = (sjme_test*)result->whatever;

	/* The write count should be the buffer size. */
	sjme_unit_equalI(test,
		1, result->length,
		"Number of written bytes incorrect?");

	/* Was this value written? */
	sjme_unit_equalI(test, 123, result->array[0],
		"Value was not written?");

	/* Success! */
	return SJME_ERROR_NONE;
}

/**
 * Tests writing a single byte to the output.
 *  
 * @since 2024/01/09 
 */
SJME_TEST_DECLARE(testStreamWriteSingleBA)
{
	sjme_stream_output stream;

	/* Setup output stream. */
	stream = NULL;
	if (sjme_error_is(sjme_stream_outputOpenByteArray(test->pool,
		&stream, 2, finishStreamWriteSingleBA,
		test)) || stream == NULL)
		return sjme_unit_fail(test, "Could not open output stream.");

	/* Write single value. */
	if (sjme_error_is(sjme_stream_outputWriteSingle(stream,
		123)))
		return sjme_unit_fail(test, "Could not write output value.");

	/* Close stream. */
	if (sjme_error_is(sjme_closeable_closeUnRef(
		SJME_AS_CLOSEABLE(stream))))
		return sjme_unit_fail(test, "Could not close output stream.");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
