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

static sjme_jboolean funcRomLibraryResourceStreamJar(
	sjme_attrInNotNull sjme_mock* inState,
	sjme_attrInNotNull sjme_mock_configWork* inCurrent)
{
	if (inState == NULL || inCurrent == NULL)
		return SJME_JNI_FALSE;

	if (inCurrent->type == SJME_MOCK_DO_TYPE_ROM_MOCK_LIBRARY)
		inCurrent->data.romMockLibrary.isJar = SJME_JNI_TRUE;

	return SJME_JNI_TRUE;
}

static const sjme_mock_configSet configRomLibraryResourceStreamJar =
{
	funcRomLibraryResourceStreamJar,
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
SJME_TEST_DECLARE(testRomLibraryResourceStreamJar)
{
	sjme_errorCode error;
	sjme_rom_library library;
	sjme_mock mock;
	sjme_stream_input inputStream;
	sjme_jint readCount, i;
	sjme_jint bufLen;
	sjme_jubyte* buf;

	/* Initialize mocks. */
	memset(&mock, 0, sizeof(mock));
	if (!sjme_mock_act(test, &mock,
			&configRomLibraryResourceStreamJar, 0))
		return sjme_unit_fail(test, "Could not initialize mocks");

	/* Get the library to test. */
	library = mock.romLibraries[0];

	/* Try to find a resource. */
	inputStream = NULL;
	if (sjme_error_is(error = sjme_rom_libraryResourceAsStream(library,
		&inputStream, "hello.txt")) || inputStream == NULL)
		return sjme_unit_fail(test, "Did not find resource? %d",
			error);

	/* Allocate target buffer. */
	bufLen = hello_txt__len + 20;
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
	for (i = 0; i < readCount; i++)
		sjme_message("b[%d] %c ?= %c", i, buf[i], hello_txt__bin[i]);
	sjme_unit_equalI(test, 0, memcmp(buf, hello_txt__bin, readCount),
		"Read bytes are not correct?");

	/* Just close the stream. */
	if (sjme_error_is(sjme_closeable_close(
		SJME_AS_CLOSEABLE(inputStream))))
		return sjme_unit_fail(test, "Could not close stream?");
	
	/* Close the library. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(library))))
		return sjme_unit_fail(test, "Could not close library?: %d", error);

	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
