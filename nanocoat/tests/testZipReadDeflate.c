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
#include "unit.h"

#include "sample.zip.h"
#include "sample.txt.h"

#define TEST_MAX_SIZE 10240

/**
 * Tests reading of deflate data in ZIPs.
 * 
 * @since 2023/12/01
 */
SJME_TEST_DECLARE(testZipReadDeflate)
{
	sjme_errorCode error;
	sjme_zip zip;
	sjme_zip_entry entry;
	sjme_stream_input stream;
	sjme_jubyte read[TEST_MAX_SIZE];
	sjme_jint readCount;
	
	/* Open Zip. */
	zip = NULL;
	if (sjme_error_is(error = sjme_zip_openMemory(test->pool,
		&zip, sample_zip__bin, sample_zip__len) ||
		zip == NULL))
		return sjme_unit_fail(test, "Could not open zip: %d", error);
	
	/* Reference up, since the Zip is valid. */
	if (sjme_error_is(sjme_alloc_weakRef(zip, NULL)))
		return sjme_unit_fail(test, "Could not count up Zip?");
	
	/* Locate entry. */
	memset(&entry, 0, sizeof(entry));
	if (sjme_error_is(error = sjme_zip_locateEntry(zip,
		&entry, "sample.txt")))
		return sjme_unit_fail(test, "Could not locate entry: %d", error);
	
	/* Open entry stream. */
	stream = NULL;
	if (sjme_error_is(error = sjme_zip_entryRead(&entry,
		&stream)) || stream == NULL)
		return sjme_unit_fail(test, "Could not open stream: %d", error);
	
	/* Reference up, since the stream is valid. */
	if (sjme_error_is(sjme_alloc_weakRef(stream, NULL)))
		return sjme_unit_fail(test, "Could not count up stream?");
		
	/* Read as much as possible. */
	memset(read, 0, sizeof(read));
	readCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadFully(stream,
		&readCount, read, TEST_MAX_SIZE)) || readCount < 0)
		return sjme_unit_fail(test, "Could not read data: %d", error);
	
	/* Check data. */
	sjme_unit_equalI(test, sample_txt__len, readCount,
		"Read incorrect number of bytes?");
	sjme_unit_equalI(test,
		0, memcmp(read, sample_txt__bin, sample_txt__len),
		"Read incorrect data?");
	
	/* Close the entry. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(stream))))
		return sjme_unit_fail(test, "Could not close stream: %d", error);
	
	/* Close zip. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(zip))))
		return sjme_unit_fail(test, "Could not close zip: %d", error);
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
