/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"

/**
 * Sanity check to make sure boolean is the correct sized type.
 *
 * @since 2022/12/18
 */
SJME_TEST_PROTOTYPE(testBooleanEnumSize)
{
	if (sizeof(sjme_jboolean) != 1)
		return FAIL_TEST(sizeof(sjme_jboolean));
	return PASS_TEST();
}
