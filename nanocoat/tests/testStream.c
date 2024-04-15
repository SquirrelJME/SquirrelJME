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

#define READ_BUF 4

static const sjme_jubyte testData[NUM_BYTES] =
{
	2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
	13, 14, 15, 16, 17
};

/**
 * Tests that streams work.
 *  
 * @since 2023/12/30 
 */
SJME_TEST_DECLARE(testStream)
{
	sjme_stream_input inputStream;
	sjme_jint readCount, cycles;
	sjme_jubyte buf[READ_BUF];
	sjme_jubyte valAt, i;

	/* Setup input stream. */
	inputStream = NULL;
	if (sjme_error_is(sjme_stream_inputOpenMemory(test->pool,
		&inputStream, testData, NUM_BYTES)) ||
		inputStream == NULL)
		return sjme_unit_fail(test, "Could not open input stream.");

	/* Read until EOF. */
	valAt = 2;
	for (cycles = 0;; cycles++)
	{
		/* Clear read buffer. */
		memset(buf, 0, sizeof(buf));

		/* Read in more data. */
		readCount = -2;
		if (sjme_error_is(sjme_stream_inputRead(inputStream,
			&readCount, buf, READ_BUF)) || readCount < -1)
			sjme_unit_fail(test, "Failed read?");

		/* EOF? */
		if (readCount == -1)
			break;

		/* Should have read said bytes. */
		sjme_unit_equalI(test, READ_BUF, readCount,
			"Did not read correct number of bytes?");

		/* Values in buffer should match. */
		for (i = 0; i < READ_BUF; i++)
			sjme_unit_equalI(test, valAt++, buf[i],
				"Incorrectly read value?");
	}

	/* There should have been this many cycles. */
	sjme_unit_equalI(test, cycles, NUM_BYTES / READ_BUF,
		"Incorrect number of read cycles?");

	/* Close the stream. */
	if (sjme_error_is(sjme_stream_inputClose(inputStream)))
		return sjme_unit_fail(test, "Could not close stream?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
