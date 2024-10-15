/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

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
 * Tests reading from a bit stream.
 *  
 * @since 2024/08/27 
 */
SJME_TEST_DECLARE(testBitStreamRead)
{
	sjme_bitStream_input input;
	sjme_bitStream_order order;
	sjme_jint i, actual;
	sjme_juint val;
	
	/* Open the bit stream. */
	input = NULL;
	if (sjme_error_is(test->error = sjme_bitStream_inputOpenMemory(
		test->pool, &input,
		testData, sizeof(testData))) ||
		input == NULL)
		return sjme_unit_fail(test, "Could not open bit stream?");
	
	/* Read in all bits. */
	for (i = 1; i <= 64; i++)
	{
		/* Zero is really 32. */
		actual = ((i % 32) == 0 ? 32 : i % 32);
		
		/* Write in both orders. */
		order = (i <= 32 ? SJME_BITSTREAM_LSB : SJME_BITSTREAM_MSB);
		
		/* Read in value. */
		val = INT32_MAX;
		if (sjme_error_is(test->error = sjme_bitStream_inputRead(
			input, order,
			&val, actual)))
			return sjme_unit_fail(test, "Could not read %d bits?", actual);
		
		/* Should match the number given in. */
		sjme_unit_equalI(test, actual, val,
			"Read value did not match? %d", actual); 
	}
	
	/* Close the bit stream. */
	if (sjme_error_is(test->error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(input))))
		return sjme_unit_fail(test, "Could not close the bit stream?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
