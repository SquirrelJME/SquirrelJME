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
#include "sjme/seekable.h"

static const sjme_jbyte test_magic[8] =
{
	0x01, 0x23, 0x45, 0x67,
	0x89, 0xAB, 0xCD, 0xEF
};

static const sjme_jbyte test_result8[8] =
{
	0xEF, 0xCD, 0xAB, 0x89,
	0x67, 0x45, 0x23, 0x01
};

static const sjme_jbyte test_result4[8] =
{
	0x67, 0x45, 0x23, 0x01,
	0xEF, 0xCD, 0xAB, 0x89
};

static const sjme_jbyte test_result2[8] =
{
	0x23, 0x01, 0x67, 0x45,
	0xAB, 0x89, 0xEF, 0xCD
};

/**
 * Tests that seekable reversing is correct.
 *  
 * @since 2024/08/13 
 */
SJME_TEST_DECLARE(testSeekableReverse)
{
	sjme_seekable seekable;
	const sjme_jbyte* desire;
	sjme_jbyte read[8];
	sjme_jint wordSize, b;
	
	/* Setup seekable over it. */
	seekable = NULL;
	if (sjme_error_is(sjme_seekable_openMemory(test->pool,
		&seekable, &test_magic[0], 8)) ||
		seekable == NULL)
		return sjme_unit_fail(test, "Could not open seekable");
	
	/* Read the various sizes. */
	for (wordSize = 8; wordSize >= 2; wordSize /= 2)
	{
		/* Read in reverse. */
		memset(read, 0, sizeof(read));
		if (sjme_error_is(sjme_seekable_readReverse(seekable, wordSize,
			read, 0, 8)))
			return sjme_unit_fail(test, "Could not read %d.", wordSize);
		
		/* Depends on the word size. */
		if (wordSize == 8)
			desire = test_result8;
		else if (wordSize == 4)
			desire = test_result4;
		else
			desire = test_result2;
		
		/* Debug. */
		for (b = 0; b < 8; b++)
			sjme_message("[%d] %02x ?= %02x",
				b, read[b] & 0xFF, desire[b] & 0xFF);
		
		/* Should be all the same. */
		for (b = 0; b < 8; b++)
			sjme_unit_equalI(test, read[b], desire[b],
				"Failed match for word %d, index %d", wordSize, b);
	}
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
