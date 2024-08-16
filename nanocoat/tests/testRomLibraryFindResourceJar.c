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
#include "test.h"
#include "unit.h"

static sjme_jboolean funcRomLibraryFindResourceJar(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWork* inCurrent)
{
	if (inState == NULL || inCurrent == NULL)
		return SJME_JNI_FALSE;

	if (inCurrent->type == SJME_MOCK_DO_TYPE_ROM_MOCK_LIBRARY)
		inCurrent->data.romMockLibrary.isJar = SJME_JNI_TRUE;

	return SJME_JNI_TRUE;
}

static const sjme_mock_configSet configRomLibraryFindResourceJar =
{
	funcRomLibraryFindResourceJar,
	0,

	{
		sjme_mock_doRomMockLibrary,
		NULL
	}
};

/**
 * Tests finding a single resource.
 *  
 * @since 2023/12/30 
 */
SJME_TEST_DECLARE(testRomLibraryFindResourceJar)
{
	sjme_rom_library library;
	sjme_mock mock;
	sjme_stream_input inputStream;

	/* Initialize mocks. */
	memset(&mock, 0, sizeof(mock));
	if (!sjme_mock_act(test, &mock,
			&configRomLibraryFindResourceJar, 0))
		return sjme_unit_fail(test, "Could not initialize mocks");

	/* Get the library to test. */
	library = mock.romLibraries[0];

	/* Try to find a resource. */
	inputStream = NULL;
	if (sjme_error_is(sjme_rom_libraryResourceAsStream(library,
		&inputStream, "hello.txt")) || inputStream == NULL)
		return sjme_unit_fail(test, "Did not find resource?");

	/* Just close the stream. */
	if (sjme_error_is(sjme_closeable_closeUnRef(
		SJME_AS_CLOSEABLE(inputStream))))
		return sjme_unit_fail(test, "Could not close stream?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
