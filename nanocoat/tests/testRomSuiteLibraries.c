/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "mock.h"
#include "proto.h"
#include "sjme/romInternal.h"
#include "test.h"
#include "unit.h"

static sjme_errorCode testSuiteList(
	sjme_attrInNotNull const sjme_rom_suiteFunctions* functions,
	sjme_attrInNotNull sjme_rom_suite* targetSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library* outLibraries)
{
	sjme_todo("Implement this?");
	return 0;
}

static sjme_jboolean configRomSuiteLibraries(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWork* inCurrent)
{
	sjme_mock_configDataRomSuite* romSuite;

	romSuite = &inCurrent->data.romSuite;

	if (inCurrent->type == SJME_MOCK_DO_TYPE_ROM_SUITE)
		romSuite->functions.list = testSuiteList;

	return SJME_JNI_TRUE;
}

/** Mock set for test. */
static const sjme_mock_configSet mockRomSuiteLibraries =
	{
		configRomSuiteLibraries,
		0,

		/* Mock calls. */
		{
			sjme_mock_doRomSuite,
			NULL
		}
};

/**
 * Tests that suite libraries can be obtained.
 *
 * @since 2023/12/21
 */
SJME_TEST_DECLARE(testRomSuiteLibraries)
{
	sjme_mock mockState;
	sjme_rom_suite* suite;

	/* Initialize mocks. */
	memset(&mockState, 0, sizeof(mockState));
	if (!sjme_mock_act(test, &mockState,
		&mockRomSuiteLibraries, 0))
		return sjme_unitFail(test, "Could not run mocks.");

	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
