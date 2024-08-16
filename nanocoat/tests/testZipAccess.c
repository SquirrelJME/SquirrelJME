/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "proto.h"
#include "test.h"
#include "sjme/zip.h"
#include "mock.jar.h"
#include "unit.h"

/**
 * Tests access of ZIP files.
 * 
 * @since 2023/11/29
 */
SJME_TEST_DECLARE(testZipAccess)
{
	sjme_zip zip;
	sjme_errorCode error;
	sjme_zip_entry entry;

	/* Attempt open of Zip. */
	zip = NULL;
	if (sjme_error_is(sjme_zip_openMemory(test->pool, &zip,
		mock_jar__bin, mock_jar__len)) || zip == NULL)
		return sjme_unit_fail(test, "Could not open Zip");
	
	/* Locate entry. */
	memset(&entry, 0, sizeof(entry));
	if (sjme_error_is(error = sjme_zip_locateEntry(zip,
		&entry, "hello.txt")))
		return sjme_unit_fail(test, "Could not find entry: %d", error);
	
	/* Some basic checks on it. */
	sjme_unit_equalP(test, zip, entry.zip,
		"Does not point to owning Zip?");
	sjme_unit_equalI(test, 0, strcmp(entry.name, "hello.txt"),
		"Name does not match? Was: %s", entry.name);
	sjme_unit_greaterEqualI(test, entry.versionMadeBy, 1,
		"Version made by invalid?");
	sjme_unit_greaterEqualI(test, entry.versionNeeded, 1,
		"Version needed invalid?");
	sjme_unit_greaterEqualI(test, entry.uncompressedSize, 1,
		"Uncompressed size invalid?");
	sjme_unit_greaterEqualI(test, entry.compressedSize, 1,
		"Compressed size invalid?");
	sjme_unit_greaterEqualI(test, entry.offset, 1,
		"Offset invalid?");
	sjme_unit_notEqualI(test, entry.uncompressedCrc, 0,
		"CRC not specified?");
	
	/* Close it. */
	if (sjme_error_is(error = sjme_closeable_closeUnRef(
		SJME_AS_CLOSEABLE(zip))))
		return sjme_unit_fail(test, "Could not close Zip: %d", error);

	/* Passed! */
	return SJME_TEST_RESULT_PASS;
}
