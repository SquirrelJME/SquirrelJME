/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include <string.h>

#include "sjme/nvm/rom.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/nvm/payload.h"
#include "sjme/nvm/romInternal.h"
#include "sjme/util.h"
#include "sjme/zip.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/listUtil.h"

/** The prefix for release ROMs. */
#define SJME_NVM_ROM_PREFIX_RELEASE "SQUIRRELJME.SQC"

/** The prefix for debug ROMs. */
#define SJME_NVM_ROM_PREFIX_DEBUG "SQUIRRELJME-DEBUG.SQC"

/** The entry name for the suites list. */
#define SJME_NVM_ROM_SUITES_LIST "suites.list"

/** Launcher main class. */
#define SJME_NVM_ROM_LAUNCHER_MAIN "launcher.main"

/** Launcher main arguments. */
#define SJME_NVM_ROM_LAUNCHER_ARGS "launcher.args"

/** Launcher classpath. */
#define SJME_NVM_ROM_LAUNCHER_PATH "launcher.path"

/** The release name. */
#define SJME_NVM_ROM_SUITES_LIST_RELEASE \
	SJME_NVM_ROM_PREFIX_RELEASE "/" SJME_NVM_ROM_SUITES_LIST

/** The debug name. */
#define SJME_NVM_ROM_SUITES_LIST_DEBUG \
	SJME_NVM_ROM_PREFIX_DEBUG "/" SJME_NVM_ROM_SUITES_LIST

static sjme_errorCode sjme_nvm_rom_zipSuiteDefaultLaunch(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrOutNotNull sjme_lpstr* outMainClass,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outMainArgs,
	sjme_attrOutNotNull sjme_list(sjme_jint)** outById,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outByName)
{
#define BUF_SIZE 256
#define LOCATE_SIZE 128
	sjme_errorCode error;
	sjme_zip zip;
	sjme_zip_entry zipEntry;
	sjme_stream_input inputStream;
	sjme_jint valid;
	sjme_cchar buf[BUF_SIZE];
	sjme_lpstr str;
	sjme_list(sjme_lpstr)* strings;
	sjme_list(sjme_jint)* ints;
	sjme_lpcstr clutterPrefix;
	sjme_cchar locate[LOCATE_SIZE];
	
	if (allocPool == NULL || inSuite == NULL || outMainClass == NULL ||
		outMainArgs == NULL || outById == NULL || outByName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover Zip. */
	zip = inSuite->handle;

	/* Which prefix is used? */
	clutterPrefix = (inSuite->clutterLevel == SJME_NVM_BOOT_CLUTTER_RELEASE ?
		SJME_NVM_ROM_PREFIX_RELEASE : SJME_NVM_ROM_PREFIX_DEBUG);
	
	/* launcher.main */
	memset(locate, 0, sizeof(locate));
	snprintf(locate, LOCATE_SIZE - 1, "%s/%s",
		clutterPrefix, SJME_NVM_ROM_LAUNCHER_MAIN);
	
	/* These are available from three entries essentially */
	memset(&zipEntry, 0, sizeof(zipEntry));
	if (!sjme_error_is(sjme_zip_locateEntry(zip,
		&zipEntry, locate)))
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
		
		/* Duplicate main class. */
		str = NULL;
		if (sjme_error_is(error = sjme_alloc_strdup(
			allocPool, &str, (sjme_lpcstr)&buf[2])) || str == NULL)
			return sjme_error_default(error);
		
		/* Give it. */
		*outMainClass = str;
		
		/* Close. */
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inputStream))))
			return sjme_error_default(error);
	}
	
	/* launcher.args */
	memset(locate, 0, sizeof(locate));
	snprintf(locate, LOCATE_SIZE - 1, "%s/%s",
		clutterPrefix, SJME_NVM_ROM_LAUNCHER_ARGS);
	
	/* Locate the launcher arguments. */
	memset(&zipEntry, 0, sizeof(zipEntry));
	if (!sjme_error_is(sjme_zip_locateEntry(zip,
		&zipEntry, locate)))
	{
		/* Open entry. */
		inputStream = NULL;
		if (sjme_error_is(error = sjme_zip_entryRead(&zipEntry,
			&inputStream)) || inputStream == NULL)
			return sjme_error_default(error);
		
		/* Parse strings. */
		strings = NULL;
		if (sjme_error_is(error = sjme_listUtil_binListUtf(
			allocPool, &strings,
			inputStream)) || strings == NULL)
			return sjme_error_default(error);
		
		/* Give it. */
		*outMainArgs = strings;
		
		/* Close. */
		if (sjme_error_is(error = sjme_closeable_close(
			SJME_AS_CLOSEABLE(inputStream))))
			return sjme_error_default(error);
	}
	
	/* launcher.path */
	memset(locate, 0, sizeof(locate));
	snprintf(locate, LOCATE_SIZE - 1, "%s/%s",
		clutterPrefix, SJME_NVM_ROM_LAUNCHER_PATH);

	/* Locate the launcher classpath. */
	memset(&zipEntry, 0, sizeof(zipEntry));
	if (!sjme_error_is(sjme_zip_locateEntry(zip,
		&zipEntry, locate)))
	{
		/* Open entry. */
		inputStream = NULL;
		if (sjme_error_is(error = sjme_zip_entryRead(&zipEntry,
			&inputStream)) || inputStream == NULL)
			return sjme_error_default(error);
			
		/* Parse integers. */
		ints = NULL;
		if (sjme_error_is(error = sjme_listUtil_binListInt(
			allocPool, SJME_AS_LISTP_VOID(&ints),
			inputStream)) || ints == NULL)
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
#undef LOCATE_SIZE
}

static sjme_errorCode sjme_nvm_rom_zipSuiteInit(
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNullable sjme_pointer data)
{
	sjme_errorCode error;
	sjme_zip zip;
	sjme_zip_entry entry;
	sjme_jboolean noRelease, noDebug;
	
	if (inSuite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set handle, which is the Zip itself. */
	zip = data;
	inSuite->handle = zip;

	/* By default, assume both exist unless otherwise determined. */
	noRelease = SJME_JNI_FALSE;
	noDebug = SJME_JNI_FALSE;

	/* Check to see which clutter levels are available in the ROM, then */
	/* we can more easily determine if we have to switch. */
	/* Release. */
	memset(&entry, 0, sizeof(entry));
	if (sjme_error_is(error = sjme_zip_locateEntry(zip, &entry,
		SJME_NVM_ROM_SUITES_LIST_RELEASE)))
	{
		if (error != SJME_ERROR_FILE_NOT_FOUND)
			return sjme_error_default(error);

		/* Does not exist. */
		noRelease = SJME_JNI_TRUE;
	}

	/* Debug. */
	memset(&entry, 0, sizeof(entry));
	if (sjme_error_is(error = sjme_zip_locateEntry(zip, &entry,
		SJME_NVM_ROM_SUITES_LIST_DEBUG)))
	{
		if (error != SJME_ERROR_FILE_NOT_FOUND)
			return sjme_error_default(error);

		/* Does not exist. */
		noDebug = SJME_JNI_TRUE;
	}

	/* Both cannot be true, this is not a valid ROM! */
	if (noDebug && noRelease)
		return SJME_ERROR_INVALID_ROM;

	/* We want debug but there is no debug? */
	else if (inSuite->clutterLevel == SJME_NVM_BOOT_CLUTTER_DEBUG &&
		noDebug)
		inSuite->clutterLevel = SJME_NVM_BOOT_CLUTTER_RELEASE;

	/* We want release but there is no release? */
	else if (inSuite->clutterLevel == SJME_NVM_BOOT_CLUTTER_RELEASE &&
		noRelease)
		inSuite->clutterLevel = SJME_NVM_BOOT_CLUTTER_DEBUG;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_rom_zipSuiteLibraryId(
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jint* outId)
{
	sjme_list(sjme_nvm_rom_library)* libs;
	sjme_jint i, n;
	
	if (inSuite == NULL || inLibrary == NULL || outId == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* This needs to be known. */
	libs = inSuite->libraries;
	if (inSuite->libraries == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Find the right one, its ID is just its index. */
	for (i = 0, n = libs->length; i < n; i++)
		if (inLibrary == libs->elements[i])
		{
			*outId = i;
			return SJME_ERROR_NONE;
		}
	
	/* Failed. */
	return SJME_ERROR_LIBRARY_NOT_FOUND;
}

static sjme_errorCode sjme_nvm_rom_zipSuiteListLibraries(
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrOutNotNull sjme_list(sjme_nvm_rom_library)** outLibraries)
{
#define LOCATE_SIZE 128
	sjme_errorCode error;
	sjme_zip zip;
	sjme_zip_entry zipEntry;
	sjme_stream_input inputStream;
	sjme_alloc_pool allocPool;
	sjme_list(sjme_lpstr)* suiteNames;
	sjme_list(sjme_nvm_rom_library)* result;
	sjme_nvm_rom_library lib;
	sjme_jint n, i;
	sjme_cchar prefix[SJME_MAX_PATH];
	sjme_lpcstr clutterPrefix;
	sjme_cchar locate[LOCATE_SIZE];
	
	if (inSuite == NULL || outLibraries == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover Zip. */
	allocPool = inSuite->allocPool;
	zip = inSuite->handle;
	
	/* Initialize. */
	inputStream = NULL;
	suiteNames = NULL;
	result = NULL;

	/* Which prefix is used? */
	clutterPrefix = (inSuite->clutterLevel == SJME_NVM_BOOT_CLUTTER_RELEASE ?
		SJME_NVM_ROM_PREFIX_RELEASE : SJME_NVM_ROM_PREFIX_DEBUG);
	
	/* suites.list */
	memset(locate, 0, sizeof(locate));
	snprintf(locate, LOCATE_SIZE - 1, "%s/%s",
		clutterPrefix, SJME_NVM_ROM_SUITES_LIST);

	/* Locate it. */
	memset(&zipEntry, 0, sizeof(zipEntry));
	if (sjme_error_is(error = sjme_zip_locateEntry(zip,
		&zipEntry, locate)))
		return sjme_error_default(error);
	
	/* Open entry. */
	if (sjme_error_is(error = sjme_zip_entryRead(&zipEntry,
		&inputStream)) || inputStream == NULL)
		goto fail_openEntry;
	
	/* Load entry strings. */
	if (sjme_error_is(error = sjme_listUtil_binListUtf(allocPool,
		&suiteNames, inputStream)) || suiteNames == NULL)
		goto fail_loadNames;
	
	/* Close entry, we do not need it anymore. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(inputStream))))
		goto fail_closeEntry;
	inputStream = NULL;
	
	/* Setup target library list */
	n = suiteNames->length;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		n, &result, sjme_nvm_rom_library, 0)) || result == NULL)
		goto fail_allocResult;
	
	/* Load in each library with its own Zip variant. */
	for (i = 0; i < n; i++)
	{
		/* Determine prefix to be used. */
		memset(prefix, 0, sizeof(prefix));
		snprintf(prefix, SJME_MAX_PATH - 1,
			"%s/%s",
			clutterPrefix,
			suiteNames->elements[i]);
		
		/* Load in single library with the specified prefix. */
		lib = NULL;
		if (sjme_error_is(error = sjme_nvm_rom_libraryFromZip(
			allocPool, &lib, suiteNames->elements[i],
			prefix, zip)))
			goto fail_loadLibrary;
		
		/* Use it! */
		result->elements[i] = lib;
	}
	
	/* We no longer need the names. */
	if (sjme_error_is(error = sjme_alloc_free(suiteNames)))
		goto fail_freeSuiteNames;
	suiteNames = NULL;
	
	/* Success! */
	*outLibraries = result;
	return SJME_ERROR_NONE;
	
fail_freeSuiteNames:
fail_loadLibrary:
fail_allocResult:
	if (result != NULL)
	{
		/* Make sure all libraries are removed. */
		for (i = 0, n = result->length; i < n; i++)
			if (result->elements[i] != NULL)
				sjme_closeable_close(
					SJME_AS_CLOSEABLE(result->elements[i]));
		
		/* Then free the actual result. */
		sjme_alloc_free(result);
		result = NULL;
	}
fail_closeEntry:
fail_loadNames:
	if (suiteNames != NULL)
		sjme_alloc_free(suiteNames);
	suiteNames = NULL;
	
fail_openEntry:
	if (inputStream != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(inputStream));
	inputStream = NULL;
	
	return sjme_error_default(error);
#undef LOCATE_SIZE
}

static sjme_errorCode sjme_nvm_rom_zipSuiteLoadLibrary()
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

/** Functions for Zip based suites. */
static sjme_nvm_rom_suiteFunctions sjme_nvm_rom_zipSuiteFunctions =
{
	sjme_sm(.defaultLaunch, sjme_nvm_rom_zipSuiteDefaultLaunch),
	sjme_sm(.init, sjme_nvm_rom_zipSuiteInit),
	sjme_sm(.libraryId, sjme_nvm_rom_zipSuiteLibraryId),
	sjme_sm(.list, sjme_nvm_rom_zipSuiteListLibraries),
	sjme_sm(.loadLibrary, sjme_nvm_rom_zipSuiteLoadLibrary),
};

sjme_errorCode sjme_nvm_rom_suiteFromZipSeekable(
	sjme_attrInNotNull sjme_alloc_pool pool,
	sjme_attrOutNotNull sjme_nvm_rom_suite* outSuite,
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrInValue sjme_nvm_bootClutterLevel clutterLevel)
{
	sjme_errorCode error;
	sjme_zip zip;
	sjme_nvm_rom_suite result;
	
	if (pool == NULL || outSuite == NULL || seekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try opening as a Zip file. */
	zip = NULL;
	if (sjme_error_is(error = sjme_zip_openSeekable(pool, &zip,
		seekable)) || zip == NULL)
		return sjme_error_default(error);
	
	/* Setup new suite. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteNew(pool,
		&result, zip,
		&sjme_nvm_rom_zipSuiteFunctions, clutterLevel,
		NULL)) ||
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
