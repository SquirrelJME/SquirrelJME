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
#include "sjme/romInternal.h"

static sjme_errorCode testSuiteList(
	sjme_attrInNotNull const sjme_rom_suiteFunctions* functions,
	sjme_attrInNotNull sjme_rom_suite* targetSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library* outLibraries)
{
	sjme_todo("Implement this?");
	return 0;
}

static const sjme_rom_suiteFunctions testSuiteFunctions =
{
	.list = testSuiteList
};

/**
 * Tests that suite libraries can be obtained.
 *
 * @since 2023/12/21
 */
SJME_TEST_DECLARE(testRomSuiteLibraries)
{
	sjme_rom_suite testSuite;

	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
