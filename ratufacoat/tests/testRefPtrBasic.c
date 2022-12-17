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

#if 0
static sjme_jint localRefPtrFunc(sjme_error* error)
{
	SJME_REFPTR_VAR_BEGIN()
		SJME_REFPTR_VAR(sjme_jint, boop);
	SJME_REFPTR_VAR_END();

	SJME_REFPTR_CODE_BEGIN()

	SJME_UNREF(boop) = 2;

	SJME_REFPTR_CODE_END(error)
}
#endif

SJME_TEST_PROTOTYPE(testRefPtrBasic)
{
#if 0
	localRefPtrFunc(&shim->error);
#endif

	return FAIL_TEST(1);
}
