/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "format/pack.h"
#include "format/sqc.h"
#include "builtin.h"
#include "debug.h"
#include "counter.h"

/**
 * Tests that SQCs can be detected and opened.
 * 
 * @since 2021/09/19
 */
SJME_TEST_PROTOTYPE(testSqcDetect)
{
	sjme_packInstance* pack;
	sjme_sqcPackState* sqcPackState;
	sjme_sqcState* rawSqcState;
	sjme_jint libDx, entryDx;
	sjme_libraryInstance* lib;
	sjme_jboolean outActive;
	
	/* Needs built-in ROM to work properly. */
	if (sjme_builtInRomSize <= 0)
		return SKIP_TEST();
	
	/* Try opening the pack file. */
	if (!sjme_packOpen(&pack, sjme_builtInRomData, sjme_builtInRomSize,
		&shim->error))
		return FAIL_TEST(1);
	
	/* Wrong driver? */
	if (pack->driver != &sjme_packSqcDriver)
		return FAIL_TEST(2);
	
	/* Must point to the chunk data. */
	if (pack->format.chunk.data != sjme_builtInRomData)
		return FAIL_TEST(3);
	
	/* And must match the given size. */
	if (pack->format.chunk.size != sjme_builtInRomSize)
		return FAIL_TEST(4);
		
	/* There must be a base pack state. */
	sqcPackState = pack->state;
	if (sqcPackState == NULL)
		return FAIL_TEST(5);
	
	/* There needs to be a valid SQC state. */
	rawSqcState = &sqcPackState->sqcState;
	if (rawSqcState == NULL)
		return FAIL_TEST(6);
	
	/* Must be a valid class version. */
	if (rawSqcState->classVersion != SQC_CLASS_VERSION_20201129)
		return FAIL_TEST(7);
	
	/* Needs at least one property. */
	if (rawSqcState->numProperties <= 0)
		return FAIL_TEST(8);
	
	/* Check for at least one library. */
	if (pack->numLibraries <= 0)
		return FAIL_TEST(9);
	
	/* Go through an open every library, to check that it is valid. */
	for (libDx = 0; libDx < pack->numLibraries; libDx++)
	{
		/* Open the library. */
		lib = NULL;
		if (!sjme_packLibraryOpen(pack, &lib, libDx, &shim->error))
			return FAIL_TEST(100 + libDx);
		
		/* Must have been set. */
		if (lib == NULL)
			return FAIL_TEST(200 + libDx);
		
		/* Must be at this index. */
		if (lib->packIndex != libDx)
			return FAIL_TEST(300 + libDx);
		
		/* Must be within this pack. */
		if (lib->packOwner != pack)
			return FAIL_TEST(400 + libDx);
		
		/* Should have at least one entry. */
		if (lib->numEntries <= 0)
			return FAIL_TEST(500 + libDx);
		
		/* Go through every entry and attempt loading the data contained. */	
		for (entryDx = 0; entryDx < lib->numEntries; entryDx++)
		{
		}
		
		/* Clear up the library usage. */
		outActive = sjme_true;
		if (!sjme_counterDown(&lib->counter, &outActive, &shim->error))
			return FAIL_TEST(600 + libDx);
		
		/* Must be inactive, since we only used this once. */
		if (outActive != sjme_false)
			return FAIL_TEST(700 + libDx);
	}
	
	/* Cleanup at the end. */
	if (!sjme_packClose(pack, &shim->error))
		return FAIL_TEST(999);
	
	return PASS_TEST();
}
