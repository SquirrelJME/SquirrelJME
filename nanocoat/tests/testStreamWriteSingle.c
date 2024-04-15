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

/**
 * Tests writing a single byte to the output.
 *  
 * @since 2024/01/09 
 */
SJME_TEST_DECLARE(testStreamWriteSingle)
{
	sjme_stream_output stream;
	sjme_jubyte value;

	/* Clear output first. */
	memset(&value, 0, sizeof(value));

	/* Setup output stream. */
	stream = NULL;
	if (sjme_error_is(sjme_stream_outputOpenMemory(test->pool,
		&stream, &value, 1)) || stream == NULL)
		return sjme_unit_fail(test, "Could not open output stream.");

	/* Write single value. */
	if (sjme_error_is(sjme_stream_outputWriteSingle(stream,
		123)))
		return sjme_unit_fail(test, "Could not write output value.");

	/* The write count should be the buffer size. */
	sjme_unit_equalI(test,
		1, stream->totalWritten,
		"Number of written bytes incorrect?");

	/* Was this value written? */
	sjme_unit_equalI(test, 123, value,
		"Value was not written?");

	/* Close stream. */
	if (sjme_error_is(sjme_stream_outputClose(stream, NULL)))
		return sjme_unit_fail(test, "Could not close output stream.");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
