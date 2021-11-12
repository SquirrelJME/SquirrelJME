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
			return FAIL_TEST(100 + libDx);
		
		/* Scan through each entry. */
		for (entryDx = 0; entryDx < lib->numEntries; entryDx++)
		{
		}
		
		/* Clear up the library usage. */
		if (!sjme_counterDown(&lib->counter, NULL, &shim->error))
			return FAIL_TEST(200 + libDx);
	}
		
	/* Cleanup at the end. */
	if (!sjme_packClose(pack, &shim->error))
		return FAIL_TEST(999);
	
	return PASS_TEST();
}
