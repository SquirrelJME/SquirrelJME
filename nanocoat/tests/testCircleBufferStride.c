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

#define TEST_COUNT 25

/**
 * Tests striding through a buffer, completely.
 *  
 * @since 2024/08/25 
 */
SJME_TEST_DECLARE(testCircleBufferStride)
{
	sjme_circleBuffer* buffer;
	sjme_circleBuffer_seekEnd seekPush, seekPop;
	sjme_jint whichSeek, val;
	sjme_jubyte byteVal;
	
	for (whichSeek = 0; whichSeek < 2; whichSeek++)
	{
		/* Which seek are we doing? */
		seekPush = (whichSeek == 0 ? SJME_CIRCLE_BUFFER_TAIL :
			SJME_CIRCLE_BUFFER_HEAD);
		seekPop = (seekPush == SJME_CIRCLE_BUFFER_TAIL ?
			SJME_CIRCLE_BUFFER_HEAD : SJME_CIRCLE_BUFFER_TAIL);
		
		/* Setup new buffer. */
		buffer = NULL;
		if (sjme_error_is(test->error = sjme_circleBuffer_new(
			&buffer,
			SJME_CIRCLE_BUFFER_QUEUE, TEST_BUF_SIZE)) ||
			buffer == NULL)
			return sjme_unit_fail(test, "Could not make buffer.");
		
		/* Push values onto the given end. */
		for (val = 0; val < TEST_COUNT; val++)
		{
			/* Push single value. */
			byteVal = val;
			if (sjme_error_is(test->error = sjme_circleBuffer_push(buffer,
				&byteVal, 1, seekPush)))
				return sjme_unit_fail(test, "Could not push %d?", val);
		}
		
		/* All should have been pushed. */
		sjme_unit_equalI(test, TEST_COUNT, buffer->ready,
			"Buffer ready did not have everything?");
		
		/* Pop all values */
		for (val = TEST_COUNT - 1; val >= 0; val--)
		{
			/* Push single value. */
			byteVal = 255;
			if (sjme_error_is(test->error = sjme_circleBuffer_pop(buffer,
				&byteVal, 1, seekPop)))
				return sjme_unit_fail(test, "Could not pop %d?", val);
			
			/* Must be the same value. */
			sjme_unit_equalI(test, byteVal, val,
				"Value popped did not match?");
		}
		
		/* All should have been popped. */
		sjme_unit_equalI(test, 0, buffer->ready,
			"Not everything was removed from the buffer?");
		
		/* Destroy the buffer. */
		if (sjme_error_is(test->error = sjme_circleBuffer_destroy(buffer)))
			return sjme_unit_fail(test, "Could not destroy buffer?");
	}
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
