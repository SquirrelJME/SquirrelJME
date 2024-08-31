/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "test.h"
#include "proto.h"
#include "mock.h"
#include "unit.h"

#include "deflLit.bin.h"
#include "deflFixedA.bin.h"
#include "deflDyn.bin.h"
#include "deflDynFiltered.bin.h"
#include "squirrel.txt.h"

typedef struct testDataInfo
{
	const sjme_jubyte (*deflBin)[];
	
	const sjme_jint* deflLen;
	
	const sjme_jubyte (*resultBin)[];
	
	const sjme_jint* resultLen;
} testDataInfo;

static const testDataInfo testData[] =
{
	{&defllit_bin__bin,
		&defllit_bin__len,
		&squirrel_txt__bin,
		&squirrel_txt__len},
		
	{&deflfixeda_bin__bin,
		&deflfixeda_bin__len,
		&squirrel_txt__bin,
		&squirrel_txt__len},
		
	{&defldyn_bin__bin,
		&defldyn_bin__len,
		&squirrel_txt__bin,
		&squirrel_txt__len},
		
	{&defldynfiltered_bin__bin,
		&defldynfiltered_bin__len,
		&squirrel_txt__bin,
		&squirrel_txt__len},
	
	{NULL}
};

#define BUF_SIZE 4096

/**
 * Tests data inflation/decompression with the standard Zip Deflate algorithm.
 *  
 * @since 2024/08/27 
 */
SJME_TEST_DECLARE(testInflate)
{
	const testDataInfo* info;
	sjme_stream_input inflate;
	sjme_stream_input compressed;
	sjme_jubyte buf[BUF_SIZE];
	sjme_jint readLen;
	
	/* Go through all data chunks. */
	for (info = &testData[0]; info->deflBin != NULL; info++)
	{
		/* Open compressed stream first. */
		compressed = NULL;
		if (sjme_error_is(test->error = sjme_stream_inputOpenMemory(
			test->pool, &compressed,
			info->deflBin, *(info->deflLen))) ||
			compressed == NULL)
			return sjme_unit_fail(test, "Could not open raw stream.");
		
		/* Then the inflation stream. */
		inflate = NULL;
		if (sjme_error_is(test->error = sjme_stream_inputOpenInflate(
			test->pool, &inflate,
			compressed)) || inflate == NULL)
			return sjme_unit_fail(test, "Could not open compressed stream.");
		
		/* Decompress all data. */
		readLen = INT32_MAX;
		memset(buf, 0, sizeof(buf));
		if (sjme_error_is(test->error = sjme_stream_inputReadFully(
			inflate, &readLen, buf, BUF_SIZE)))
			return sjme_unit_fail(test, "Could not read full data.");
		
		/* Everything should match. */
		sjme_unit_equalI(test, *(info->resultLen), readLen,
			"Incorrect number of bytes decompressed?");
		sjme_unit_equalI(test,
			0, memcmp(buf, info->resultBin, readLen),
			"Data was not decompressed properly?");
		
		/* Close the inflate stream. */
		if (sjme_error_is(test->error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inflate))))
			return sjme_unit_fail(test, "Could not close inflate stream.");
	} 
	
	/* Success! */
	return SJME_TEST_RESULT_PASS;
}
