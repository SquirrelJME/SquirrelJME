/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "proto.h"
#include "sjme/util.h"
#include "test.h"
#include "unit.h"

/** The max string length. */
#define MAX_LEN 32

/** The number of strings to test. */
#define NUM_STRINGS 5

/** The input test strings. */
typedef struct testString
{
	/** The input string to decode. */
	sjme_jbyte in[MAX_LEN];

	/** The resultant output. */
	sjme_jint out;
} testString;

/** The actual test strings. */
static const testString testStrings[NUM_STRINGS] =
{
	{
		{0x53, 0x71, 0x75, 0x65, 0x61, 0x6b, 0},
		6
	},
	{
		{0x61, 0xc0, 0x80, 0x62, 0},
		3
	},
	{
		{0x43, 0x7a, 0x65, 0xc5, 0x9b, 0xc4,
			0x87, 0},
		5
	},

	{
		{0xe4, 0xbd, 0xa0, 0xe5, 0xa5, 0xbd, 0},
		2
	},

	/* Invalid sequence. */
	{
		{0xbd, 0xa0, 0xe5, 0xa5, 0xbd, 0},
		-1
	}
};

SJME_TEST_DECLARE(testStringLength)
{
	const testString* testing;
	sjme_jint i, len;
	sjme_lpcstr string;

	/* Test all the strings. */
	for (i = 0; i < NUM_STRINGS; i++)
	{
		/* Which string are we testing? */
		testing = &testStrings[i];
		string = (sjme_lpcstr)testing->in;

		/* Calculate hash. */
		len = sjme_string_length(string);

		/* Was it calculated correctly? */
		sjme_unitEqualI(test, len, testing->out,
			"Input %d has invalid result?", i);
	}

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}

