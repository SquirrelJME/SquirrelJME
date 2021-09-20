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

/**
 * Tests that SQCs can be detected and opened.
 * 
 * @since 2021/09/19
 */
SJME_TEST_PROTOTYPE(testSqcDetect)
{
	sjme_packInstance* pack;
	
	/* Needs built-in ROM to work properly. */
	if (sjme_builtInRomSize <= 0)
		return SKIP_TEST();
	
	/* Try opening the pack file. */
	if (!sjme_packOpen(&pack, sjme_builtInRomData, sjme_builtInRomSize,
		NULL))
		return FAIL_TEST(1);
	
	/* Wrong driver? */
	if (pack->driver != &sjme_packSqcDriver)
		return FAIL_TEST(2);
	
	return PASS_TEST();
}
