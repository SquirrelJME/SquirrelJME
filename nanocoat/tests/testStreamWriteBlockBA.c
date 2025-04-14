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

#define CHUNK_SIZE 4

#define NUM_CHUNKS 8

#define TOTAL_BYTES (CHUNK_SIZE * NUM_CHUNKS)

static sjme_errorCode finishStreamWriteBlockBA(
	sjme_attrInNotNull sjme_stream_output stream,
	sjme_attrInNotNull sjme_stream_resultByteArray* result,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_jint i;
	sjme_test* test;

	/* Recover test. */
	test = (sjme_test*)result->whatever;

	sjme_unit_equalP(test, data, test,
		"Test data was not passed?");

	/* Each chunk should match! */
	for (i = 0; i < NUM_CHUNKS; i++)
		sjme_unit_equalI(test,
			0, memcmp(test->global, &result->array[(CHUNK_SIZE * i)],
				CHUNK_SIZE),
			"Chunk %d did not match?", i);

	/* Success! */
	return SJME_ERROR_NONE;
}

/**
 * Tests writing to a block of memory.
 *  
 * @since 2024/01/09 
 */
SJME_TEST_DECLARE(testStreamWriteBlockBA)
{
	sjme_stream_output stream;
	sjme_jint i;
	sjme_jubyte chunk[CHUNK_SIZE];

	/* Set global. */
	test->global = chunk;

	/* Initialize input chunk data. */
	for (i = 0; i < CHUNK_SIZE; i++)
		chunk[i] = i + 1;

	/* Open stream. */
	stream = NULL;
	if (sjme_error_is(sjme_stream_outputOpenByteArray(test->pool,
		&stream, 2, finishStreamWriteBlockBA,
		test)) || stream == NULL)
		return sjme_unit_fail(test, "Could not open output stream.");

	/* Write the buffer sequence for each chunk. */
	for (i = 0; i < NUM_CHUNKS; i++)
		if (sjme_error_is(sjme_stream_outputWrite(stream,
			&chunk, sizeof(chunk))))
			return sjme_unit_fail(test, "Could not write chunk %d?", i);

	/* Close stream. */
	if (sjme_error_is(sjme_closeable_close(
		SJME_AS_CLOSEABLE(stream))))
		return sjme_unit_fail(test, "Could not close output stream.");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
