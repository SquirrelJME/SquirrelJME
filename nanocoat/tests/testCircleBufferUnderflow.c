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

#include "sjme/circleBuffer.h"

#define TEST_BUF_SIZE 32

#define TEST_SUB_COUNT 3

#define TEST_TOTAL_COUNT 6

static const sjme_jubyte testRight[TEST_SUB_COUNT] =
{
	'a', 'b', 'c'
};

static const sjme_jubyte testLeft[TEST_SUB_COUNT] =
{
	'0', '1', '2'
};

static const sjme_jubyte testEverything[TEST_TOTAL_COUNT] =
{
	'0', '1', '2', 'a', 'b', 'c'
};

/**
 * Tests under-flowing a circle buffer.
 *  
 * @since 2024/08/25 
 */
SJME_TEST_DECLARE(testCircleBufferUnderflow)
{
	sjme_circleBuffer* buffer;
	sjme_jubyte everything[TEST_TOTAL_COUNT];
	
	/* Setup new buffer. */
	buffer = NULL;
	if (sjme_error_is(test->error = sjme_circleBuffer_new(&buffer,
		SJME_CIRCLE_BUFFER_QUEUE, TEST_BUF_SIZE)) ||
		buffer == NULL)
		return sjme_unit_fail(test, "Could not make buffer.");
	
	/* Push to back of buffer. */
	if (sjme_error_is(test->error = sjme_circleBuffer_push(buffer,
		testRight, TEST_SUB_COUNT,
		SJME_CIRCLE_BUFFER_TAIL)))
		return sjme_unit_fail(test, "Could not push to tail?");
		
	/* Push to front of buffer. */
	if (sjme_error_is(test->error = sjme_circleBuffer_push(buffer,
		testLeft, TEST_SUB_COUNT,
		SJME_CIRCLE_BUFFER_HEAD)))
		return sjme_unit_fail(test, "Could not push to head?");
	
	/* Get everything from the buffer. */
	memset(everything, 0, sizeof(everything));
	if (sjme_error_is(test->error = sjme_circleBuffer_get(buffer,
		everything, TEST_TOTAL_COUNT,
		SJME_CIRCLE_BUFFER_HEAD, 0)))
		return sjme_unit_fail(test, "Could not read head everything?");
	sjme_unit_equalI(test,
		0, memcmp(everything, testEverything, TEST_TOTAL_COUNT),
		"Everything head data mismatched?");
	
	/* Get everything from the buffer, again. */
	memset(everything, 0, sizeof(everything));
	if (sjme_error_is(test->error = sjme_circleBuffer_get(buffer,
		everything, TEST_TOTAL_COUNT,
		SJME_CIRCLE_BUFFER_TAIL, 0)))
		return sjme_unit_fail(test, "Could not read tail everything?");
	sjme_unit_equalI(test,
		0, memcmp(everything, testEverything, TEST_TOTAL_COUNT),
		"Everything tail data mismatched?");
	
	/* Get half the buffer. */
	memset(everything, 0, sizeof(everything));
	if (sjme_error_is(test->error = sjme_circleBuffer_get(buffer,
		everything, TEST_SUB_COUNT,
		SJME_CIRCLE_BUFFER_HEAD, 0)))
		return sjme_unit_fail(test, "Could not read head half head?");
	sjme_unit_equalI(test,
		0, memcmp(everything, testLeft, TEST_SUB_COUNT),
		"Half head data mismatched?");
	
	/* Get half the buffer, again. */
	memset(everything, 0, sizeof(everything));
	if (sjme_error_is(test->error = sjme_circleBuffer_get(buffer,
		everything, TEST_SUB_COUNT,
		SJME_CIRCLE_BUFFER_TAIL, 0)))
		return sjme_unit_fail(test, "Could not read half tail?");
	sjme_unit_equalI(test,
		0, memcmp(everything, testRight, TEST_SUB_COUNT),
		"Half tail data mismatched?");
	
	/* Destroy the buffer. */
	if (sjme_error_is(test->error = sjme_circleBuffer_destroy(buffer)))
		return sjme_unit_fail(test, "Could not destroy buffer?");
	
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
