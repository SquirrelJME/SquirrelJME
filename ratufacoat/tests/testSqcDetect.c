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

/**
 * Tests that SQCs can be detected and opened.
 * 
 * @since 2021/09/19
 */
SJME_TEST_PROTOTYPE(testSqcDetect)
{
	sjme_packInstance* pack;
	sjme_sqcState* sqcState;
	
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
	
	/* There needs to be a valid state. */
	sqcState = pack->state;
	if (sqcState == NULL)
		return FAIL_TEST(5);
	
	/** Must be a valid class version. */
	if (sqcState->classVersion != SQC_CLASS_VERSION_20201129)
		return FAIL_TEST(6);
	
	/** Needs at least one property. */
	if (sqcState->numProperties <= 0)
		return FAIL_TEST(7);
	
	return PASS_TEST();
}
