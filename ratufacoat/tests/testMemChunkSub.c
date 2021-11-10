/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "tests.h"
#include "memchunk.h"

/** Test message. */
static const uint8_t MESSAGE[] =
	"The quick gray squirrel jumps over the lazy red panda!";

/** The desired offset. */
#define DESIRE_OFF 10

/** The desired length. */
#define DESIRE_LEN 13

/** The actual message that is desired. */
static const uint8_t DESIRED[] =
	"gray squirrel";

/**
 * Tests splitting chunks.
 * 
 * @since 2021/11/09 
 */
SJME_TEST_PROTOTYPE(testMemChunkSub)
{
	sjme_memChunk test;
	sjme_memChunk sub;
	
	/* Clear these out. */
	memset(&test, 0, sizeof(test));
	memset(&sub, 0, sizeof(sub));
	
	/* Setup input chunk. */
	test.data = MESSAGE;
	test.size = sizeof(MESSAGE);
	
	/* Out of bounds reads???. */
	if (sjme_chunkSubChunk(&test, &sub, -1, 3, &shim->error))
		return FAIL_TEST(1);
	
	if (sjme_chunkSubChunk(&test, &sub, 3, -1, &shim->error))
		return FAIL_TEST(2);
	
	if (sjme_chunkSubChunk(&test, &sub, -4, -5, &shim->error))
		return FAIL_TEST(3);
	
	if (sjme_chunkSubChunk(&test, &sub, 3, 99, &shim->error))
		return FAIL_TEST(4);
	
	if (sjme_chunkSubChunk(&test, &sub, 99, 3, &shim->error))
		return FAIL_TEST(5);
	
	/* Get actual good fragment of the data. */
	if (!sjme_chunkSubChunk(&test, &sub, DESIRE_OFF, DESIRE_LEN, &shim->error))
		return FAIL_TEST(6);
	
	/* Did we read the correct pointer? */
	if (&MESSAGE[DESIRE_OFF] != sub.data)
		return FAIL_TEST(7);
	
	/* Did we set the correct size? */
	if (DESIRE_LEN != sub.size)
		return FAIL_TEST(8);
	
	/* Is the data just wrong? */
	if (memcmp(DESIRED, sub.data, sub.size) != 0)
		return FAIL_TEST(9);
	
	return PASS_TEST();
}
