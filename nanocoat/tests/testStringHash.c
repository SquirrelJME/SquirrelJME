/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

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
		0x94115238
	},
	{
		{0x61, 0xc0, 0x80, 0x62, 0},
		0x00016c83
	},
	{
		{0x43, 0x7a, 0x65, 0xc5, 0x9b, 0xc4,
			0x87, 0},
		0x03e9423a
	},
	{
		{0xe4, 0xbd, 0xa0, 0xe5, 0xa5, 0xbd, 0},
		0x0009f61d
	},

	/* Invalid sequence. */
	{
		{0x61, 0x80, 0x62, 0},
		-1
	}
};

/**
 * Tests that the hashcode for a given string is correct.
 *
 * @since 2023/12/25
 */
SJME_TEST_DECLARE(testStringHash)
{
	const testString* testing;
	sjme_jint i, hash, limitHash;
	sjme_lpcstr string;

	/* Test all the strings. */
	for (i = 0; i < NUM_STRINGS; i++)
	{
		/* Which string are we testing? */
		testing = &testStrings[i];
		string = (sjme_lpcstr)testing->in;

		/* Calculate hash. */
		hash = sjme_string_hash(string);

		/* Was it calculated correctly? */
		sjme_unit_equalI(test, hash, testing->out,
			"Input %d has invalid result?", i);
		
		/* Calculate hash with limit. */
		limitHash = sjme_string_hashN(string, strlen(string));
		
		/* Should be the same. */
		sjme_unit_equalI(test, hash, limitHash,
			"Input %d has different hash than limit hash?", i);
	}

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}

