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

#define NUM_BYTES 16

#define READ_BYTES 50

static const sjme_jubyte testBytes[NUM_BYTES] =
{
	2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
	12, 13, 14, 15, 16, 17,
};

/**
 * Tests reading more bytes than what is available.
 *  
 * @since 2023/12/31 
 */
SJME_TEST_DECLARE(testStreamOverRead)
{
	sjme_stream_input inputStream;
	sjme_jint readCount;
	sjme_jint available;
	sjme_jubyte buf[READ_BYTES];

	/* Open stream. */
	if (sjme_error_is(sjme_stream_inputOpenMemory(test->pool,
			&inputStream, testBytes, NUM_BYTES)))
		return sjme_unit_fail(test, "Could not open input stream.");

	/* Get available bytes. */
	available = -2;
	if (sjme_error_is(sjme_stream_inputAvailable(inputStream,
		&available)) || available < 0)
		return sjme_unit_fail(test, "Could not get available bytes?");

	/* The number of available bytes should be the full size. */
	sjme_unit_equalI(test, available, NUM_BYTES,
		"Available bytes incorrect?");

	/* Read way too many bytes. */
	memset(buf, 0, sizeof(buf));
	readCount = -2;
	if (sjme_error_is(sjme_stream_inputRead(inputStream,
		&readCount, buf, READ_BYTES)))
		return sjme_unit_fail(test, "Failed to read bytes?");

	/* Should be the bytes in the buffer, not the read attempt. */
	sjme_unit_equalI(test, readCount, NUM_BYTES,
		"Read count incorrect?");

	/* Test that the actual bytes are correct. */
	sjme_unit_equalI(test, 0, memcmp(buf, testBytes, readCount),
		"Read bytes are not correct?");

	/* Close the stream. */
	if (sjme_error_is(sjme_closeable_close(
		SJME_AS_CLOSEABLE(inputStream))))
		return sjme_unit_fail(test, "Could not close stream?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
