/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/rom.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/payload.h"
#include "sjme/romInternal.h"
#include "sjme/util.h"
#include "sjme/zip.h"
#include "sjme/cleanup.h"
#include "sjme/listUtil.h"

static sjme_errorCode sjme_rom_zipSuiteDefaultLaunch(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrOutNotNull sjme_lpstr* outMainClass,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outMainArgs,
	sjme_attrOutNotNull sjme_list_sjme_jint** outById,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outByName)
{
#define BUF_SIZE 256
	sjme_errorCode error;
	sjme_zip zip;
	sjme_zip_entry zipEntry;
	sjme_stream_input inputStream;
	sjme_jint valid;
	sjme_cchar buf[BUF_SIZE];
	sjme_lpstr str;
	sjme_list_sjme_lpstr* strings;
	sjme_list_sjme_jint* ints;
	
	if (inPool == NULL || inSuite == NULL || outMainClass == NULL ||
		outMainArgs == NULL || outById == NULL || outByName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover Zip. */
	zip = inSuite->handle;
	
	/* These are available from three entries essentially */
	/* launcher.main */
	memset(&zipEntry, 0, sizeof(zipEntry));
	if (!sjme_error_is(sjme_zip_locateEntry(zip,
		&zipEntry, "SQUIRRELJME.SQC/launcher.main")))
	{
		/* Open entry. */
		inputStream = NULL;
		if (sjme_error_is(error = sjme_zip_entryRead(&zipEntry,
			&inputStream)) || inputStream == NULL)
			return sjme_error_default(error);
		
		/* Read everything in. */
		memset(buf, 0, sizeof(buf));
		valid = INT32_MAX;
		if (sjme_error_is(error = sjme_stream_inputReadFully(
			inputStream, &valid,
			buf, BUF_SIZE - 1)) || valid == INT32_MAX)
			return sjme_error_default(error);
		
		/* Strip ending whitespace. */
		if (sjme_error_is(error = sjme_util_lpstrTrimEnd(buf,
			BUF_SIZE - 1)))
			return sjme_error_default(error);
		
		/* Duplicate main class. */
		str = NULL;
		if (sjme_error_is(error = sjme_alloc_strdup(
			inPool, &str, (sjme_lpcstr)&buf[0])) || str == NULL)
			return sjme_error_default(error);
		
		/* Give it. */
		*outMainClass = str;
		
		/* Close. */
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inputStream))))
			return sjme_error_default(error);
	}
	
	/* launcher.args */
	memset(&zipEntry, 0, sizeof(zipEntry));
	if (!sjme_error_is(sjme_zip_locateEntry(zip,
		&zipEntry, "SQUIRRELJME.SQC/launcher.args")))
	{
		/* Open entry. */
		inputStream = NULL;
		if (sjme_error_is(error = sjme_zip_entryRead(&zipEntry,
			&inputStream)) || inputStream == NULL)
			return sjme_error_default(error);
		
		/* Parse strings. */
		strings = NULL;
		if (sjme_error_is(error = sjme_listUtil_readAllLines(
			inPool, &strings,
			inputStream)) || 
			strings == NULL)
			return sjme_error_default(error);
		
		/* Give it. */
		*outMainArgs = strings;
		
		/* Close. */
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inputStream))))
			return sjme_error_default(error);
	}
	
	/* launcher.path */
	memset(&zipEntry, 0, sizeof(zipEntry));
	if (!sjme_error_is(sjme_zip_locateEntry(zip,
		&zipEntry, "SQUIRRELJME.SQC/launcher.path")))
	{
		/* Open entry. */
		inputStream = NULL;
		if (sjme_error_is(error = sjme_zip_entryRead(&zipEntry,
			&inputStream)) || inputStream == NULL)
			return sjme_error_default(error);
			
		/* Parse integers. */
		ints = NULL;
		if (sjme_error_is(error = sjme_listUtil_mapAllLines(
			inPool, SJME_AS_LISTP_VOID(&ints),
			inputStream, NULL, NULL)) || 
			ints == NULL)
			return sjme_error_default(error);
		
		/* Give it. */
		*outById = ints;
		
		/* Close. */
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inputStream))))
			return sjme_error_default(error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
#undef BUF_SIZE
}

static sjme_errorCode sjme_rom_zipSuiteInit(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrInNullable sjme_pointer data)
{
	if (inSuite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set handle, which is the Zip itself. */
	inSuite->handle = data;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_rom_zipSuiteLibraryId(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jint* outId)
{
	if (inSuite == NULL || inLibrary == NULL || outId == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_rom_zipSuiteListLibraries(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibraries)
{
	if (inSuite == NULL || outLibraries == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_rom_zipSuiteLoadLibrary()
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

/** Functions for Zip based suites. */
static sjme_rom_suiteFunctions sjme_rom_zipSuiteFunctions =
{
	.defaultLaunch = sjme_rom_zipSuiteDefaultLaunch,
	.init = sjme_rom_zipSuiteInit,
	.libraryId = sjme_rom_zipSuiteLibraryId,
	.list = sjme_rom_zipSuiteListLibraries,
	.loadLibrary = sjme_rom_zipSuiteLoadLibrary,
};

sjme_errorCode sjme_rom_suiteFromZipSeekable(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull sjme_seekable seekable)
{
	sjme_errorCode error;
	sjme_zip zip;
	sjme_rom_suite result;
	
	if (pool == NULL || outSuite == NULL || seekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try opening as a Zip file. */
	zip = NULL;
	if (sjme_error_is(error = sjme_zip_openSeekable(pool, &zip,
		seekable)) || zip == NULL)
		return sjme_error_default(error);
	
	/* Setup new suite. */
	result = NULL;
	if (sjme_error_is(error = sjme_rom_suiteNew(pool,
		&result, zip,
		&sjme_rom_zipSuiteFunctions, NULL)) ||
		result == NULL)
		goto fail_suiteNew;
	
	/* Count up Zip as we are using it. */
	if (sjme_error_is(error = sjme_alloc_weakRef(zip, NULL)))
		goto fail_refUp;
	
	/* Success! */
	*outSuite = result;
	return SJME_ERROR_NONE;
	
fail_refUp:
fail_suiteNew:
	/* Close the zip before failing. */
	sjme_closeable_close(SJME_AS_CLOSEABLE(zip));
	
	return sjme_error_default(error);
}
