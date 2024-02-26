/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"
#include "sjme/zip.h"
#include "mock.jar.h"

/**
 * Tests opening of Zip files.
 *
 * @since 2023/12/31
 */
SJME_TEST_DECLARE(testZipOpen)
{
	sjme_zip zip;

	/* Attempt open of Zip. */
	zip = NULL;
	if (sjme_error_is(sjme_zip_open(test->pool, &zip,
		mock_jar__bin, mock_jar__len)) || zip == NULL)
		return sjme_unit_fail(test, "Could not open Zip");

	/* Passed! */
	return SJME_TEST_RESULT_PASS;
}
