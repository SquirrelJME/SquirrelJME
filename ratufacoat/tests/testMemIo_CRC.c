/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "debug.h"
#include "memio/crc.h"

/** Test message. */
static const uint8_t MESSAGE[] =
	"The quick gray squirrel jumps over the lazy red panda!";

/** The expected end result CRC. */
#define EXPECTED_CRC SJME_JUINT_C(0xFCC0045B)

/**
 * Tests that CRC calculation is correct.
 * 
 * @since 2021/11/13
 */
SJME_TEST_PROTOTYPE(testMemIo_CRC)
{
	sjme_crcState crc;
	sjme_memChunk chunk;
	sjme_juint calcValue;
	
	/* Initialize CRC calculation. */
	memset(&crc, 0, sizeof(crc));
	if (!sjme_crcInitZip(&crc, &shim->error))
		return FAIL_TEST(1);
	
	/* Perform the CRC calculation, -1 to remove NUL. */
	if (!sjme_crcOfferDirect(&crc, (void*)MESSAGE,
		sizeof(MESSAGE) - 1, &shim->error))
		return FAIL_TEST(2);
	
	/* Get the calculated CRC value. */
	calcValue = -1;
	if (!sjme_crcChecksum(&crc, &calcValue,
		&shim->error))
		return FAIL_TEST(3);
	
	/* Was the CRC value calculated incorrectly? */
	if (calcValue != EXPECTED_CRC)
	{
		sjme_message("Wanted %08x but got %08x",
			EXPECTED_CRC, calcValue);
		return FAIL_TEST(4);
	}
		
	/* Setup memory chunk for testing. */
	memset(&chunk, 0, sizeof(chunk));
	chunk.data = MESSAGE;
	chunk.size = sizeof(MESSAGE) - 1;
	
	/* Initialize CRC calculation for memory chunks. */
	memset(&crc, 0, sizeof(crc));
	if (!sjme_crcInitZip(&crc, &shim->error))
		return FAIL_TEST(5);
		
	/* Perform the CRC calculation. */
	if (!sjme_crcOfferChunk(&crc, &chunk, 0, chunk.size,
		&shim->error))
		return FAIL_TEST(6);
	
	/* Get the calculated CRC value. */
	calcValue = -1;
	if (!sjme_crcChecksum(&crc, &calcValue,
		&shim->error))
		return FAIL_TEST(7);
	
	/* Was the CRC value calculated incorrectly? */
	if (calcValue != EXPECTED_CRC)
		return FAIL_TEST(8);
	
	return PASS_TEST();
}
