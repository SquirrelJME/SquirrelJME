/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/romMeepSwm.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/romManifest.h"

static sjme_errorCode sjme_nvm_rom_swmLoadSingleDoJa(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInNotNull sjme_nvm_rom_swmLibrary* inOutDepend)
{
	sjme_errorCode error;
	sjme_nvm_rom_library jamLib;
	sjme_cchar jamName[SJME_MAX_FILE_NAME];
	sjme_jint n;

	if (allocPool == NULL || inSuite == NULL || inLibrary == NULL ||
		inOutDepend == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Copy base JAM name. */
	memset(&jamName, 0, sizeof(jamName));
	strncpy(jamName, inLibrary->name, SJME_MAX_FILE_NAME - 1);

	/* Determine if this ends in JAR or not. */
	/* If it does, replace. */
	n = (sjme_jint)strlen(jamName);
	if (n > 3 && 0 == strcasecmp(".jar", &jamName[n - 3]))
		jamName[n - 1] = (jamName[n - 1] == 'R' ? 'M' : 'm');

	/* Otherwise, just append. */
	else
		strncat(jamName, ".jam", SJME_MAX_FILE_NAME - 1);

	/* There needs to be an actual partner JAM along with the JAR. */
	jamLib = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_resolveLibraryByName(inSuite,
		jamName, &jamLib)) || jamLib == NULL)
	{
		if (error == SJME_ERROR_LIBRARY_NOT_FOUND)
			return SJME_ERROR_NOT_MATCHED;
		return sjme_error_default(error);
	}

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_nvm_rom_swmLoadSingleManifest(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInNotNull sjme_nvm_rom_swmLibrary* inOutDepend)
{
	sjme_errorCode error;
	sjme_jboolean hasManifest;
	sjme_stream_input inputStream;
	sjme_nvm_rom_manifestStep step;

	if (allocPool == NULL || inSuite == NULL || inLibrary == NULL ||
		inOutDepend == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Check if a manifest exists. */
	hasManifest = SJME_JNI_FALSE;
	if (sjme_error_is(error = sjme_nvm_rom_libraryResourceExists(
		inLibrary, &hasManifest, "META-INF/MANIFEST.MF")))
		return sjme_error_default(error);

	/* If there is no manifest, there is no point to continue. */
	if (!hasManifest)
		return SJME_ERROR_NOT_MATCHED;

	/* Open resource stream. */
	inputStream = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_libraryResourceAsStream(
		inLibrary, &inputStream, "META-INF/MANIFEST.MF")) ||
		inputStream == NULL)
		goto fail_openRc;

	/* Parse keys accordingly. */
	memset(&step, 0, sizeof(step));
	for (;;)
	{
		/* Parse the next key. */
		if (sjme_error_is(error = sjme_nvm_rom_manifestParseNext(
			inputStream, &step)))
		{
			/* Stop parsing on EOF. */
			if (error == SJME_ERROR_END_OF_FILE)
				break;

			/* Fail. */
			return sjme_error_default(error);
		}

		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Close the stream. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(inputStream))))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;

fail_openRc:
	if (inputStream != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(inputStream));

	return sjme_error_default(error);
}

static sjme_errorCode sjme_nvm_rom_swmLoadSingle(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInNotNull sjme_nvm_rom_swmLibrary* inOutDepend)
{
	sjme_errorCode error;

	if (allocPool == NULL || inSuite == NULL || inLibrary == NULL ||
		inOutDepend == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Load manifest based information from the Jar directly, or from a */
	/* discovered KJX prepended manifest. */
	if (sjme_error_is(error = sjme_nvm_rom_swmLoadSingleManifest(
		allocPool, inSuite, inLibrary, inOutDepend)))
	{
		if (error != SJME_ERROR_NOT_MATCHED)
			return sjme_error_default(error);
	}

	/* Load DoJa specific information. */
	if (sjme_error_is(error = sjme_nvm_rom_swmLoadSingleDoJa(
		allocPool, inSuite, inLibrary, inOutDepend)))
	{
		if (error != SJME_ERROR_NOT_MATCHED)
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_rom_swmLoad(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrOutNotNull sjme_nvm_rom_swmManager* outSwmManager)
{
	sjme_errorCode error;
	sjme_nvm_rom_swmManager result;
	sjme_list_sjme_nvm_rom_library* libraries;
	sjme_nvm_rom_library library;
	sjme_list(sjme_nvm_rom_swmLibrary)* suiteDepends;
	sjme_nvm_rom_swmLibrary* suiteDependency;
	sjme_jint i, n;

	if (allocPool == NULL || inSuite == NULL || outSwmManager == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate the result as it gets added into. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(allocPool,
		sizeof(*result), SJME_NVM_STRUCT_ROM_MEEP_SWM,
		SJME_AS_NVM_COMMONP(&result))) ||
		result == NULL)
		goto fail_allocResult;

	/* We need the library list to process everything. */
	libraries = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteLibraries(inSuite,
		&libraries)) || libraries == NULL)
		goto fail_suiteLibs;

	/* Allocate list of suite information. */
	suiteDepends = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		libraries->length, &suiteDepends, sjme_nvm_rom_swmLibrary, 0)) ||
		suiteDepends == NULL)
		goto fail_allocDepends;
	result->libraries = suiteDepends;

	/* Go through and process each library. */
	for (n = libraries->length, i = 0; i < n; i++)
	{
		/* Ignore blank library slots. */
		library = libraries->elements[i];
		if (library == NULL)
			continue;

		/* Set library here. */
		suiteDependency = &suiteDepends->elements[i];
		suiteDependency->library = library;

		/* Parse this library's dependency information. */
		if (sjme_error_is(error = sjme_nvm_rom_swmLoadSingle(allocPool,
			inSuite, library, suiteDependency)))
			goto fail_parseDepends;
	}

	/* Return the resultant dependency information. */
	*outSwmManager = result;
	return SJME_ERROR_NONE;

fail_parseDepends:
fail_suiteLibs:
fail_allocDepends:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_rom_swmResolve(
	sjme_attrInNotNull sjme_nvm_rom_swmManager swmManager,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_lpstr* outMainClass,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outMainArgs,
	sjme_attrOutNotNull sjme_list(sjme_jint)** outById,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outByName)
{
	if (swmManager == NULL || inLibrary == NULL || outMainClass == NULL ||
		outMainArgs == NULL || outById == NULL || outByName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
