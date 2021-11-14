/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "builtin.h"
#include "debug.h"
#include "crc.h"
#include "format/library.h"
#include "format/pack.h"
#include "format/sqc.h"

/**
 * Tests scanning of SQC JARs, reading all of the contents to ensure the
 * ROM can be properly read.
 * 
 * @since 2021/09/19
 */
SJME_TEST_PROTOTYPE(testSqcJarScan)
{
	sjme_packInstance* pack;
	sjme_jint libDx, entryDx;
	sjme_libraryInstance* lib;
	sjme_countableMemChunk* chunk;
	sjme_dataStream* stream;
	sjme_crcState crc;
	sjme_jint crcChunk, crcStream;

	/* Needs built-in ROM to work properly. */
	if (sjme_builtInRomSize <= 0)
		return SKIP_TEST();
	
	/* Try opening the pack file. */
	if (!sjme_packOpen(&pack, sjme_builtInRomData, sjme_builtInRomSize,
		&shim->error))
		return FAIL_TEST(1);
		
	/* Go through an open every library, to check that it is valid. */
	for (libDx = 0; libDx < pack->numLibraries; libDx++)
	{
		/* Open the library. */
		lib = NULL;
		if (!sjme_packLibraryOpen(pack, &lib, libDx, &shim->error))
			return FAIL_TEST_SUB(2, libDx);
		
		/* Scan through each entry. */
		for (entryDx = 0; entryDx < lib->numEntries; entryDx++)
		{
			/* Open the data as a memory chunk. */
			if (!sjme_libraryEntryChunk(lib, &chunk, entryDx, &shim->error))
				return FAIL_TEST_TRI(3, libDx, entryDx);
			
			/* Initialize CRC calculation. */
			memset(&crc, 0, sizeof(crc));
			if (!sjme_crcInitZip(&crc, &shim->error))
				return FAIL_TEST_TRI(4, libDx, entryDx);
			
			/* Perform the CRC calculation. */
			if (!sjme_crcOfferChunk(&crc, &chunk->chunk, 0,
				chunk->chunk.size, &shim->error))
				return FAIL_TEST_TRI(5, libDx, entryDx);
			
			/* Get the calculated CRC value. */
			crcChunk = -1;
			if (!sjme_crcChecksum(&crc, &crcChunk, &shim->error))
				return FAIL_TEST_TRI(6, libDx, entryDx);
			
			/* Close the memory chunk. */
			if (!sjme_counterDown(&chunk->count, NULL, &shim->error))
				return FAIL_TEST_TRI(7, libDx, entryDx);
			
			/* Open the data as a data stream. */
			if (!sjme_libraryEntryStream(lib, &stream, entryDx, &shim->error))
				return FAIL_TEST_TRI(8, libDx, entryDx);
				
			/* Initialize CRC calculation. */
			memset(&crc, 0, sizeof(crc));
			if (!sjme_crcInitZip(&crc, &shim->error))
				return FAIL_TEST_TRI(9, libDx, entryDx);
			
			/* Calculate the CRC of the chunk. */
			sjme_todo("Calculate entry CRC by stream?");
			
			/* Get the calculated CRC value. */
			crcStream = -2;
			if (!sjme_crcChecksum(&crc, &crcStream, &shim->error))
				return FAIL_TEST_TRI(11, libDx, entryDx);
			
			/* Close the data stream. */
			if (!sjme_counterDown(&stream->count, NULL, &shim->error))
				return FAIL_TEST_TRI(12, libDx, entryDx);
			
			/* These two CRCs should have been the same value. */
			if (crcChunk != crcStream)
				return FAIL_TEST_TRI(13, libDx, entryDx);
		}
		
		/* Clear up the library usage. */
		if (!sjme_counterDown(&lib->counter, NULL, &shim->error))
			return FAIL_TEST_SUB(14, libDx);
	}
		
	/* Cleanup at the end. */
	if (!sjme_packClose(pack, &shim->error))
		return FAIL_TEST(999);
	
	return PASS_TEST();
}
