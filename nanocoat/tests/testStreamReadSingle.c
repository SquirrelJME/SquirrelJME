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
 * Tests reading a single character.
 *  
 * @since 2024/01/01 
 */
SJME_TEST_DECLARE(testStreamReadSingle)
{
	sjme_stream_input inputStream;
	sjme_jint readCount;
	sjme_jint single;
	sjme_juint at;

	/* Setup input stream. */
	inputStream = NULL;
	if (sjme_error_is(sjme_stream_inputOpenMemory(test->pool,
		&inputStream, testData, NUM_BYTES)) ||
		inputStream == NULL)
		return sjme_unit_fail(test, "Could not open input stream.");

	/* Read until EOF. */
	for (at = 0;; at++)
	{
		/* Read in next byte. */
		single = -2;
		if (sjme_error_is(sjme_stream_inputReadSingle(inputStream,
			&single)) || single < -1)
			return sjme_unit_fail(test, "Could not read single byte.");

		/* EOS? */
		if (at == NUM_BYTES)
		{
			sjme_unit_equalI(test, single, -1,
				"End of stream not marked?");
			break;
		}

		/* Should not occur normally. */
		else if (at > NUM_BYTES)
		{
			sjme_unit_fail(test, "More bytes in the buffer?");
			break;
		}

		/* Make sure it matches. */
		sjme_unit_equalI(test, testData[at], single,
			"Bytes did not match?");
	}

	/* Close the stream. */
	if (sjme_error_is(sjme_closeable_close(inputStream)))
		return sjme_unit_fail(test, "Could not close stream?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
