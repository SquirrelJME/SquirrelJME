/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/rom.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/nvm/payload.h"
#include "sjme/nvm/romInternal.h"
#include "sjme/util.h"
#include "sjme/zip.h"
#include "sjme/nvm/cleanup.h"

sjme_errorCode sjme_nvm_rom_resolveClassPathById(
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull const sjme_list_sjme_jint* inIds,
	sjme_attrOutNotNull sjme_list_sjme_nvm_rom_library** outLibs)
{
	sjme_list_sjme_nvm_rom_library* suiteLibs;
	sjme_errorCode error;
	sjme_jint length, i, numLibs, at, libId;
	sjme_nvm_rom_library* working;
	sjme_nvm_rom_library checkLibrary;

	if (inSuite == NULL || inIds == NULL || outLibs == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Debug. */
	sjme_message("sjme_nvm_rom_resolveClassPathById(%p, %p, %p)",
		inSuite, inIds, outLibs);

	/* How many are we looking for? */
	length = inIds->length;
	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Debug. */
	sjme_message("Looking for %d libraries...", length);

	/* Allocate temporary storage on the stack for the libraries we want. */
	working = sjme_alloca(sizeof(*working) * length);
	if (working == NULL)
		return sjme_error_outOfMemory(NULL, length);

	/* Make sure it is cleared. */
	memset(working, 0, sizeof(*working) * length);

	/* Debug. */
	sjme_message("Getting library list...");

	/* Obtain the list of libraries within the suite. */
	suiteLibs = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteLibraries(inSuite,
		&suiteLibs)) || suiteLibs == NULL)
		return sjme_error_default(error);

	/* Debug. */
	sjme_message("Done: %p!", suiteLibs);
	sjme_message("Found %d libraries.", suiteLibs->length);

	/* Go through and find the ones with matching IDs. */
	/* Order by library because there is likely to be more of those. */
	numLibs = suiteLibs->length;
	for (i = 0; i < numLibs; i++)
	{
		/* Which library is this? */
		checkLibrary = suiteLibs->elements[i];

#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("Looking at library #%d: %p",
			i, checkLibrary);
#endif

		/* Need to initialize the ID? */
		libId = checkLibrary->id;
		if (libId == 0)
		{
			/* No function? */
			if (inSuite->functions->libraryId == NULL)
				return SJME_ERROR_ILLEGAL_STATE;

			/* Get the library ID. */
			if (sjme_error_is(error = inSuite->functions->libraryId(
				inSuite, checkLibrary,
				&libId)))
				return sjme_error_default(error);

			/* Library ID function did not store it? */
			if (checkLibrary->id == 0)
				checkLibrary->id = libId;
		}

		/* Scan through the requested classpath for matches. */
		for (at = 0; at < length; at++)
			if (inIds->elements[at] == libId)
			{
				working[at] = checkLibrary;
				break;
			}
	}

	/* Scan through and fail if any are null, that is not found. */
	for (at = 0; at < length; at++)
		if (working[at] == NULL)
			return SJME_ERROR_LIBRARY_NOT_FOUND;

	/* Return the libraries which gets placed into a list as a copy. */
	return sjme_list_newA(inSuite->cache.common.allocPool,
		sjme_nvm_rom_library, 0, length, outLibs, working);
}

sjme_errorCode sjme_nvm_rom_resolveClassPathByName(
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull const sjme_list_sjme_lpcstr* inNames,
	sjme_attrOutNotNull sjme_list_sjme_nvm_rom_library** outLibs)
{
	sjme_list_sjme_nvm_rom_library* suiteLibs;
	sjme_errorCode error;
	sjme_jint length, i, at, hash, numSuiteLibs;
	sjme_nvm_rom_library* working;
	sjme_nvm_rom_library lib;
	sjme_jint* inHashes;

	if (inSuite == NULL || inNames == NULL || outLibs == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* How many are we looking for? */
	length = inNames->length;
	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Make sure there are no NULL libraries as input. */
	for (i = 0; i < length; i++)
		if (inNames->elements[i] == NULL)
			return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate temporary storage on the stack for the libraries we want. */
	working = sjme_alloca(sizeof(*working) * length);
	inHashes = sjme_alloca(sizeof(*inHashes) * length);
	if (working == NULL || inHashes == NULL)
		return sjme_error_outOfMemory(NULL, length);

	/* Clear memory. */
	memset(working, 0, sizeof(*working) * length);
	memset(inHashes, 0, sizeof(*inHashes) * length);

	/* First hash all the input libraries, so we can quickly scan through. */
	for (i = 0; i < length; i++)
		inHashes[i] = sjme_string_hash(inNames->elements[i]);

	/* Obtain the list of libraries within the suite. */
	suiteLibs = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteLibraries(inSuite,
		&suiteLibs) || suiteLibs == NULL))
		return sjme_error_default(error);

	/* Go through each library and get hash matches. */
	numSuiteLibs = suiteLibs->length;
	for (i = 0; i < numSuiteLibs; i++)
	{
		/* Get hash of this library. */
		lib = suiteLibs->elements[i];
		if (sjme_error_is(error = sjme_nvm_rom_libraryHash(lib,
			&hash)))
			return sjme_error_default(error);

		/* Look for match in output. */
		for (at = 0; at < length; at++)
			if (working[at] == NULL && inHashes[at] == hash &&
				0 == strcmp(inNames->elements[at], lib->name))
			{
#if defined(SJME_CONFIG_DEBUG)
				sjme_message("Found %s for %d.",
					inNames->elements[at], at);
#endif
				working[at] = lib;
				break;
			}
	}

	/* Scan through and fail if any are null, that is not found. */
	for (at = 0; at < length; at++)
		if (working[at] == NULL)
			return SJME_ERROR_LIBRARY_NOT_FOUND;

	/* Return the libraries which gets placed into a list as a copy. */
	return sjme_list_newA(inSuite->cache.common.allocPool,
		sjme_nvm_rom_library, 0, length, outLibs, working);
}
