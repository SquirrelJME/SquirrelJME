/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"

/** Empty buffer. */
static const sjme_jubyte emptyBuffer[1] =
{
	'x'
};

/**
 * Tests reading of an empty stream.
 *
 * @since 2023/12/31 
 */
SJME_TEST_DECLARE(testStreamEmpty)
{
	sjme_stream_input inputStream;
	sjme_jint result;

	/* Open stream. */
	if (SJME_IS_ERROR(sjme_stream_inputOpenMemory(test->pool,
		&inputStream, emptyBuffer, 0)))
		return sjme_unitFail(test, "Could not open input stream.");

	/* Try to read a single byte, it should indicate EOS. */
	result = 999;
	if (SJME_IS_ERROR(sjme_stream_inputReadSingle(inputStream,
		&result)))
		return sjme_unitFail(test, "Could not read single byte.");

	/* Should be EOF. */
	sjme_unitEqualI(test, -1, result,
		"Incorrect read byte?");

	/* Close the stream. */
	if (SJME_IS_ERROR(sjme_stream_inputClose(inputStream)))
		return sjme_unitFail(test, "Could not close stream?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
