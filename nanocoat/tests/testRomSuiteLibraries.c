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

/** Just easily returned accordingly. */
static sjme_list_sjme_rom_library testFakeSuiteList =
{
	.length = 0,
	.elementSize = sizeof(sjme_rom_library)
};

static sjme_errorCode testSuiteList(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibraries)
{
	*outLibraries = &testFakeSuiteList;
	return SJME_ERROR_NONE;
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
	sjme_rom_suite suite;
	sjme_list_sjme_rom_library* libraries;
	sjme_errorCode error;

	/* Initialize mocks. */
	memset(&mockState, 0, sizeof(mockState));
	if (!sjme_mock_act(test, &mockState,
		&mockRomSuiteLibraries, 0))
		return sjme_unit_fail(test, "Could not run mocks.");

	/* Get the suite we created. */
	suite = mockState.romSuites[0];

	/* Call the libraries list. */
	libraries = NULL;
	if (sjme_error_is(error = sjme_rom_suiteLibraries(suite,
		&libraries)) || libraries == NULL)
		return sjme_unit_fail(test, "Could not get libraries list: %d", error);

	/* The result should be our fake libraries. */
	sjme_unit_equalP(test, libraries, &testFakeSuiteList,
		"Different set of libraries returned?");

	/* The cache should get our libraries. */
	sjme_unit_equalP(test, suite->cache.libraries, &testFakeSuiteList,
		"Library list was not cached?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
