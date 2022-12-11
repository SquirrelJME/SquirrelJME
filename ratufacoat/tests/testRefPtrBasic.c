/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "tests.h"
#include "refptr/refptr.h"

static sjme_jint localRefPtrFunc(void)
{
	SJME_REFPTR_BEGIN;



	SJME_REFPTR_END;
}

SJME_TEST_PROTOTYPE(testRefPtrBasic)
{
	localRefPtrFunc();

	return FAIL_TEST(1);
}
