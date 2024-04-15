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

static const sjme_mock_configSet configRomLibraryResourceNotFound =
{
	NULL,
	0,

	{
		sjme_mock_doRomMockLibrary,
		NULL
	}
};

/**
 * Tests not finding a resource.
 *  
 * @since 2023/12/30 
 */
SJME_TEST_DECLARE(testRomLibraryResourceNotFound)
{
	sjme_rom_library library;
	sjme_mock mock;
	sjme_errorCode error;
	sjme_stream_input inputStream;

	/* Initialize mocks. */
	memset(&mock, 0, sizeof(mock));
	if (!sjme_mock_act(test, &mock,
			&configRomLibraryResourceNotFound, 0))
		return sjme_unit_fail(test, "Could not initialize mocks");

	/* Get the library to test. */
	library = mock.romLibraries[0];

	/* Try to find a resource that does not exist. */
	inputStream = NULL;
	error = sjme_rom_libraryResourceAsStream(library,
		&inputStream, "nope");

	/* Must be resource not found. */
	sjme_unit_equalI(test, error, SJME_ERROR_RESOURCE_NOT_FOUND,
		"Resource was found or other error?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
