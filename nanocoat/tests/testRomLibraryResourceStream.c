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
#include "hello.txt.h"

static const sjme_mock_configSet configRomLibraryResourceStream =
{
	NULL,
	0,

	{
		sjme_mock_doRomMockLibrary,
		NULL
	}
};

/**
 * Tests reading a resource a stream using the stream resource function of
 * a library, for when raw data cannot be accessed.
 *  
 * @since 2023/12/30 
 */
SJME_TEST_DECLARE(testRomLibraryResourceStream)
{
	sjme_rom_library library;
	sjme_mock mock;
	sjme_stream_input inputStream;
	sjme_jint readCount;
	sjme_jint bufLen;
	sjme_jubyte* buf;

	/* Initialize mocks. */
	memset(&mock, 0, sizeof(mock));
	if (!sjme_mock_act(test, &mock,
			&configRomLibraryResourceStream, 0))
		return sjme_unit_fail(test, "Could not initialize mocks");

	/* Get the library to test. */
	library = mock.romLibraries[0];

	/* Try to find a resource. */
	inputStream = NULL;
	if (sjme_error_is(sjme_rom_libraryResourceAsStream(library,
		&inputStream, "hello.txt")) || inputStream == NULL)
		return sjme_unit_fail(test, "Did not find resource?");

	/* Allocate target buffer. */
	bufLen = hello_txt__len + 1;
	buf = sjme_alloca(bufLen);
	memset(buf, 0, bufLen);

	/* Read way too many bytes. */
	readCount = -2;
	if (sjme_error_is(sjme_stream_inputRead(inputStream,
		&readCount, buf, bufLen)))
		return sjme_unit_fail(test, "Failed to read bytes?");

	/* Should be the bytes in the buffer, not the read attempt. */
	sjme_unit_equalI(test, readCount, hello_txt__len,
		"Read count incorrect?");

	/* Test that the actual bytes are correct. */
	sjme_unit_equalI(test, 0, memcmp(buf, hello_txt__bin, readCount),
		"Read bytes are not correct?");

	/* Just close the stream. */
	if (sjme_error_is(sjme_stream_inputClose(inputStream)))
		return sjme_unit_fail(test, "Could not close stream?");

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
