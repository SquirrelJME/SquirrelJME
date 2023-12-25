/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "proto.h"
#include "test.h"
#include "sjme/util.h"

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
	sjme_jchar out[MAX_LEN];
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
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
