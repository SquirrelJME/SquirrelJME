/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "hello.txt.h"
#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"

#define EXPECTED_SIZE 23

static const sjme_jubyte expectedRawBin[EXPECTED_SIZE] =
{
	83,113,117,105,114,114,101,108,
	115,32,97,114,101,32,115,
	111,32,99,117,116,101,33,10
};

static sjme_jboolean funcRomLibraryRawData(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWork* inCurrent)
{
	sjme_mock_configDataRomLibrary* library;

	if (inState == NULL || inCurrent == NULL)
		return SJME_JNI_FALSE;

	library = &inCurrent->data.romLibrary;

	switch (inCurrent->type)
	{
		case SJME_MOCK_DO_TYPE_ROM_LIBRARY:
			library->name = "hello.txt";
			library->data = hello_txt__bin;
			library->length = hello_txt__len;
			break;
	}

	return SJME_JNI_TRUE;
}

static const sjme_mock_configSet configRomLibraryRawData =
{
	funcRomLibraryRawData,
	0,

	{
		sjme_mock_doRomLibrary,
		sjme_mock_doRomSuite,
		NULL
	}
};

/**
 * Tests accessing of raw data.
 *
 * @since 2023/12/30
 */
SJME_TEST_DECLARE(testRomLibraryRawData)
{
	sjme_errorCode error;
	sjme_nvm_rom_library library;
	sjme_mock mock;
	sjme_jint size;
	sjme_jubyte* raw;

	/* Initialize mocks. */
	memset(&mock, 0, sizeof(mock));
	if (!sjme_mock_act(test, &mock,
		&configRomLibraryRawData, 0))
		return sjme_unit_fail(test, "Could not initialize mocks");

	/* Get the library to access. */
	library = mock.romLibraries[0];

	/* Get the size of the file. */
	size = -1;
	if (sjme_error_is(sjme_nvm_rom_libraryRawSize(library, &size) ||
		size < 0))
		return sjme_unit_fail(test, "Could not read library size.");

	/* Allocate raw buffer. */
	raw = sjme_alloca(size);
	if (raw == NULL)
		return sjme_unit_fail(test, "Could not alloca buffer.");

	/* Make sure it is cleared first, use FFs to check for gaps. */
	memset(raw, 0xFF, size);

	/* Check that the size matches. */
	sjme_unit_equalI(test, size, EXPECTED_SIZE,
		"File size is not as the expected size?");

	/* Read in the raw data. */
	if (sjme_error_is(sjme_nvm_rom_libraryRawRead(library,
		raw, 0, size)))
		return sjme_unit_fail(test, "Could not read data.");

	/* Compare all the bytes, must be equal. */
	sjme_unit_equalI(test,
		0, memcmp(expectedRawBin, raw, EXPECTED_SIZE),
		"Data is wrong?");
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
