/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "proto.h"
#include "test.h"

/**
 * Tests a test that does nothing except skip.
 *
 * @since 2023/12/07
 */
SJME_TEST_DECLARE(testTestNoOpSkip)
{
	return SJME_TEST_RESULT_SKIP;
}
