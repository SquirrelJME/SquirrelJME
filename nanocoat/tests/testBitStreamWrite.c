/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "test.h"
#include "proto.h"
#include "mock.h"
#include "unit.h"
#include "sjme/bitStream.h"

/** Test bit sequence. */
static const sjme_jubyte testData[132] =
{
	29, 21, 227U, 128U, 144U, 64, 129U,
	5, 48, 64, 3, 112, 0, 30, 0,
	16, 0, 17, 0, 36, 0, 152U,
	0, 0, 5, 0, 84, 0, 0, 11,
	0, 224U, 2, 0, 128U, 1, 0,
	144U, 1, 0, 64, 3, 0, 128U,
	13, 0, 0, 112, 0, 0, 64, 
	7, 0, 0, 240U, 0, 0, 0, 62,
	0, 0, 0, 32, 0, 0, 0, 
	179U, 80, 12, 14, 1, 18, 40,
	64, 3, 12, 128U, 5, 224U, 
	0, 240U, 0, 8, 0, 16, 1,
	64, 2, 0, 50, 0, 160U, 0, 
	0, 84, 0, 0, 13, 0, 128U, 
	14, 0, 128U, 1, 0, 0, 
	19, 0, 0, 44, 0, 0, 
	96, 3, 0, 0, 14, 0, 
	0, 192U, 5, 0, 0, 240U,
	0, 0, 0, 248U, 0, 0, 
	0, 4 
};

/**
 * Tests writing to a bit stream.
 *  
 * @since 2024/08/27 
 */
SJME_TEST_DECLARE(testBitStreamWrite)
{
	sjme_stream_output baos;
	sjme_bitStream_output output;
	sjme_bitStream_order order;
	sjme_stream_resultByteArray result;
	sjme_jint i;

	/* Output target byte array. */
	baos = NULL;
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(test->error = sjme_stream_outputOpenByteArrayTo(
		test->pool, &baos,
		512, &result)) || baos == NULL)
		return sjme_unit_fail(test, "Could not open output byte array.");
	
	/* Open the bit stream. */
	output = NULL;
		if (sjme_error_is(test->error = sjme_bitStream_outputOpenStream(
		test->pool, &output,
		baos, SJME_JNI_TRUE)) ||
		output == NULL)
		return sjme_unit_fail(test, "Could not open bit stream?");
	
	/* Write all bits. */
	for (i = 1; i <= 64; i++)
	{
		/* Write in both orders. */
		order = (i <= 32 ? SJME_BITSTREAM_LSB : SJME_BITSTREAM_MSB);
		
		/* Write value. */
		if (sjme_error_is(test->error = sjme_bitStream_outputWrite(
			output, order,
			i % 32, i % 32)))
			return sjme_unit_fail(test, "Could not write %d bits?", i % 32);
	}
	
	/* Close the bit stream. */
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(output))))
		return sjme_unit_fail(test, "Could not close the bit stream?");
	
	/* There should be data. */
	sjme_unit_notEqualP(test, NULL, result.array,
		"There was no output array?");
	
	/* Should result in a match. */
	sjme_unit_equalI(test, sizeof(testData), result.length,
		"Length did not match?");
	sjme_unit_equalI(test,
		0, memcmp(testData, result.array, result.length),
		"Written contents did not match?");
	
	/* Free the array. */
	if (sjme_error_is(test->error = sjme_alloc_free(result.array)))
		return sjme_unit_fail(test, "Could not free the array?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
