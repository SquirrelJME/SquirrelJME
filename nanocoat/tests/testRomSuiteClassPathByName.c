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
#include "sjme/nvm/romInternal.h"
#include "test.h"
#include "unit.h"

static sjme_lpcstr testRomNames[3] =
{
	"squirrels.jar",
	"are.jar",
	"cute.jar"
};

static sjme_jboolean configRomSuiteClassPathByName(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWork* inCurrent)
{
	sjme_mock_configDataRomSuite* romSuite;
	sjme_mock_configDataRomLibrary* romLibrary;

	romLibrary = &inCurrent->data.romLibrary;
	romSuite = &inCurrent->data.romSuite;

	switch (inCurrent->type)
	{
		case SJME_MOCK_DO_TYPE_ROM_LIBRARY:
			romLibrary->id = inCurrent->indexType + 1;
			romLibrary->name = testRomNames[inCurrent->indexType];
			break;

		case SJME_MOCK_DO_TYPE_ROM_SUITE:
			if (sjme_error_is(sjme_list_newV(
				inState->allocPool, sjme_rom_library, 0, 3,
				&romSuite->cacheLibraries,
				inState->romLibraries[0],
				inState->romLibraries[1],
				inState->romLibraries[2])))
				return SJME_JNI_FALSE;
			break;
	}

	return SJME_JNI_TRUE;
}

/** Mock set for test. */
static const sjme_mock_configSet mockRomSuiteClassPathByName =
	{
		configRomSuiteClassPathByName,
		0,

		/* Mock calls. */
		{
			sjme_mock_doRomLibrary,
			sjme_mock_doRomLibrary,
			sjme_mock_doRomLibrary,
			sjme_mock_doRomSuite,
			NULL
		}
};

/**
 * Tests getting classpath entries by name.
 *
 * @since 2023/12/21
 */
SJME_TEST_DECLARE(testRomSuiteClassPathByName)
{
	sjme_mock mockState;
	sjme_rom_suite suite;
	sjme_list_sjme_lpcstr* forwardNames;
	sjme_list_sjme_lpcstr* backwardNames;
	sjme_list_sjme_rom_library* result;

	/* Initialize mocks. */
	memset(&mockState, 0, sizeof(mockState));
	if (!sjme_mock_act(test, &mockState,
			&mockRomSuiteClassPathByName, 0))
		return sjme_unit_fail(test, "Could not run mocks.");

	/* Calculate both forwards and backwards names. */
	if (sjme_error_is(sjme_list_newV(mockState.allocPool,
			sjme_lpcstr, 0, 3, &forwardNames,
			"squirrels.jar", "are.jar", "cute.jar")))
		return sjme_unit_fail(test, "Could not emit forward names?");
	if (sjme_error_is(sjme_list_newV(mockState.allocPool,
			sjme_lpcstr, 0, 3, &backwardNames,
			"cute.jar", "are.jar", "squirrels.jar")))
		return sjme_unit_fail(test, "Could not emit backwards names?");

	/* Get the suite. */
	suite = mockState.romSuites[0];

	/* Resolve forward names first. */
	result = NULL;
	if (sjme_error_is(sjme_rom_resolveClassPathByName(suite,
		forwardNames, &result)) || result == NULL)
		return sjme_unit_fail(test, "Could not resolve names?");

	/* The libraries must match! */
	sjme_unit_equalI(test, 3, result->length,
		"Length does not match?");
	sjme_unit_equalP(test, mockState.romLibraries[0], result->elements[0],
		"First incorrect?");
	sjme_unit_equalP(test, mockState.romLibraries[1], result->elements[1],
		"Second incorrect?");
	sjme_unit_equalP(test, mockState.romLibraries[2], result->elements[2],
		"Third incorrect?");

	/* Resolve backwards names last. */
	result = NULL;
	if (sjme_error_is(sjme_rom_resolveClassPathByName(suite,
			backwardNames, &result)) || result == NULL)
		return sjme_unit_fail(test, "Could not resolve reverse names?");

	/* The libraries must match! */
	sjme_unit_equalI(test, 3, result->length,
		"Reverse length does not match?");
	sjme_unit_equalP(test, mockState.romLibraries[0], result->elements[2],
		"Reverse first incorrect?");
	sjme_unit_equalP(test, mockState.romLibraries[1], result->elements[1],
		"Reverse second incorrect?");
	sjme_unit_equalP(test, mockState.romLibraries[2], result->elements[0],
		"Reverse third incorrect?");
		
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
