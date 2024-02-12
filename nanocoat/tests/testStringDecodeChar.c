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
#define NUM_STRINGS 10

/** The input test strings. */
typedef struct testString
{
	/** The input string to decode. */
	sjme_jbyte in[MAX_LEN];

	/** The resultant output. */
	sjme_jint out[MAX_LEN];
} testString;

/** The actual test strings. */
static const testString testStrings[NUM_STRINGS] =
{
	{
		{0},
		{-1},
	},

	{
		{0x53, 0x71, 0x75, 0x65, 0x61, 0x6b, 0},
		{0x53, 0x71, 0x75, 0x65, 0x61, 0x6b, -1}
	},

	{
		{0x61, 0xc0, 0x80, 0x62, 0},
		{0x61, 0x00, 0x62, -1}
	},

	{
		{0x43, 0x7a, 0x65, 0xc5, 0x9b, 0xc4, 0x87,
			0},
		{0x43, 0x7a, 0x65, 0x15b, 0x107, -1}
	},

	{
		{0xe4, 0xbd, 0xa0, 0xe5, 0xa5, 0xbd, 0},
		{0x4f60, 0x597d, -1}
	},

	/* Invalid sequence. */
	{
		{0x61, 0x80, 0x62, 0},
		{0x61, -1}
	},

	/* Invalid sequence. */
	{
		{0x61, 0xc0, 0x62, 0},
		{0x61, -1}
	},

	/* Invalid sequence. */
	{
		{0xbd, 0xa0, 0xe5, 0xa5, 0xbd, 0},
		{-1}
	},

	/* Invalid sequence. */
	{
		{0xe4, 0xa0, 0xe5, 0xa5, 0xbd, 0},
		{-1}
	},

	/* Invalid sequence. */
	{
		{0xe4, 0xbd, 0xe5, 0xa5, 0xbd, 0},
		{-1}
	}
};

/**
 * Tests the decoding of characters within modified-UTF style strings, using
 * the function @c sjme_string_decodeChar .
 *
 * @since 2023/12/25
 */
SJME_TEST_DECLARE(testStringDecodeChar)
{
	const testString* testing;
	sjme_jint i, charIndex;
	sjme_jint c;
	sjme_lpcstr string;
	sjme_lpcstr p;

	/* Test all the strings. */
	for (i = 0; i < NUM_STRINGS; i++)
	{
		/* Which string are we testing? */
		testing = &testStrings[i];
		string = (sjme_lpcstr)testing->in;

		/* Decode every character and check the input matches. */
		charIndex = 0;
		for (p = string; *p != 0; charIndex++)
		{
			/* These should never happen. */
			if (charIndex >= MAX_LEN ||
				((uintptr_t)p - (uintptr_t)string) >= MAX_LEN)
				sjme_unit_fail(test, "Reading input/output too far in?");

			/* Decode. */
			c = sjme_string_decodeChar(p, &p);

			/* Must match the character index. */
			sjme_unit_equalI(test, c, testing->out[charIndex],
				"For input %d with output #%d, invalid result?",
				i, charIndex);

			/* End sequence? */
			if (c == -1)
				break;
		}
	}

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
