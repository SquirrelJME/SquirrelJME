/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "hello.txt.h"
#include "mock.h"
#include "proto.h"
#include "test.h"
#include "unit.h"

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
	sjme_todo("Implement %s", __func__);
	return SJME_TEST_RESULT_FAIL;
}
