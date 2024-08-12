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

static const sjme_mock_configSet configRomLibraryFindResource =
{
	NULL,
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
SJME_TEST_DECLARE(testRomLibraryFindResource)
{
	sjme_rom_library library;
	sjme_mock mock;
	sjme_stream_input inputStream;

	/* Initialize mocks. */
	memset(&mock, 0, sizeof(mock));
	if (!sjme_mock_act(test, &mock,
			&configRomLibraryFindResource, 0))
		return sjme_unit_fail(test, "Could not initialize mocks");

	/* Get the library to test. */
	library = mock.romLibraries[0];

	/* Try to find a resource. */
	inputStream = NULL;
	if (sjme_error_is(sjme_rom_libraryResourceAsStream(library,
		&inputStream, "hello.txt")) || inputStream == NULL)
		return sjme_unit_fail(test, "Did not find resource?");

	/* Just close the stream. */
	if (sjme_error_is(sjme_closeable_close(inputStream)))
		return sjme_unit_fail(test, "Could not close stream?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
