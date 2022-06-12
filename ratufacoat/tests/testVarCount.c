/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "varcount.h"

/**
 * Tests variable counting.
 * 
 * @since 2022/06/12
 */
SJME_TEST_PROTOTYPE(testVarCount)
{
	if (SJME_VAR_COUNT() != 0)
		return FAIL_TEST(99);
	
	if (SJME_VAR_COUNT(1) != 1)
		return FAIL_TEST(1);
		
	if (SJME_VAR_COUNT(1, 2) != 2)
		return FAIL_TEST(2);
		
	if (SJME_VAR_COUNT(1, 2, 3) != 3)
		return FAIL_TEST(3);
	
	return PASS_TEST();
}
