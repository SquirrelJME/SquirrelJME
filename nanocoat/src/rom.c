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

sjme_errorCode sjme_rom_fromMerge(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull sjme_rom_suite** inSuites,
	sjme_attrInPositive sjme_jint numInSuites)
{
	if (pool == NULL || outSuite == NULL || inSuites == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (numInSuites < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Implement this?");
	return SJME_ERROR_UNKNOWN;
}

sjme_errorCode sjme_rom_fromPayload(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull const sjme_payload_config* payloadConfig)
{
	sjme_jint i, numActive, numLibraries;

	if (pool == NULL || outSuite == NULL || payloadConfig == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Count the number of active ROMs. */
	numActive = 0;
	numLibraries = 0;
	for (i = 0; i < SJME_NVM_PAYLOAD_MAX_ROMS; i++)
		if (payloadConfig->roms[i].isActive)
		{
			/* Count up! */
			numActive++;

			/* Anything that is a library we need to build a container. */
			if (payloadConfig->roms[i].isLibrary)
				numLibraries++;
		}

	/* If there is nothing active then nothing needs to be done. */
	if (numActive == 0)
	{
		*outSuite = NULL;
		return SJME_ERROR_NONE;
	}

	sjme_todo("Implement this?");
	return SJME_ERROR_UNKNOWN;
}

sjme_errorCode sjme_rom_newSuite(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull const sjme_rom_suiteFunctions* inFunctions)
{
	sjme_rom_suiteInitCacheFunc initCacheFunc;
	sjme_rom_suite result;
	sjme_errorCode error;

	if (pool == NULL || outSuite == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* There needs to be a cache initializer. */
	initCacheFunc = inFunctions->initCache;
	if (initCacheFunc == NULL)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Allocate resultant suite. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc(pool, sizeof(*result),
		&result)) || result == NULL)
		return error;

	/* Initialize cache. */
	if (SJME_IS_ERROR(error = initCacheFunc(inFunctions,
		pool, result)) || result->cache == NULL)
	{
		/* Cleanup bad pointer. */
		sjme_alloc_free(result);

		return error;
	}

	/* Initialize fields. */
	result->functions = inFunctions;
	result->cache->common.allocPool = pool;

	/* Use result. */
	*outSuite = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_rom_resolveClassPathById(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrInNotNull const sjme_list_sjme_jint* inIds,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs)
{
	sjme_list_sjme_rom_library* suiteLibs;
	sjme_errorCode error;
	sjme_jint length, i, numLibs, at, libId;
	sjme_rom_library* working;
	sjme_rom_library checkLibrary;

	if (inSuite == NULL || inIds == NULL || outLibs == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* How many are we looking for? */
	length = inIds->length;
	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Allocate temporary storage on the stack for the libraries we want. */
	working = alloca(sizeof(*working) * length);
	if (working == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Make sure it is cleared. */
	memset(working, 0, sizeof(*working) * length);

	/* Obtain the list of libraries within the suite. */
	suiteLibs = NULL;
	if (SJME_IS_ERROR(error = sjme_rom_suiteLibraries(inSuite,
		&suiteLibs) || suiteLibs == NULL))
		return error;

	/* Go through and find the ones with matching IDs. */
	/* Order by library because there is likely to be more of those. */
	numLibs = suiteLibs->length;
	for (i = 0; i < numLibs; i++)
	{
		/* Which library is this? */
		checkLibrary = suiteLibs->elements[i];
		libId = checkLibrary->id;

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
	return sjme_list_newA(inSuite->cache->common.allocPool,
		sjme_rom_library, 0, length, outLibs, working);
}

sjme_errorCode sjme_rom_resolveClassPathByName(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrInNotNull const sjme_list_sjme_lpcstr* inNames,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs)
{
	sjme_list_sjme_rom_library* suiteLibs;
	sjme_errorCode error;
	sjme_jint length, i;
	sjme_rom_library* result;

	if (inSuite == NULL || inNames == NULL || outLibs == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* How many are we looking for? */
	length = inNames->length;
	if (length < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Allocate temporary storage on the stack for the libraries we want. */
	result = alloca(sizeof(*result) * length);
	if (result == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Obtain the list of libraries within the suite. */
	suiteLibs = NULL;
	if (SJME_IS_ERROR(error = sjme_rom_suiteLibraries(inSuite,
		&suiteLibs) || suiteLibs == NULL))
		return error;

	/*sjme_string_hash*/

	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_rom_suiteLibraries(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs)
{
	sjme_rom_suiteCache* cache;
	sjme_rom_suiteListLibrariesFunc listFunc;
	sjme_list_sjme_rom_library* result;
	sjme_errorCode error;

	if (inSuite == NULL || outLibs == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be a valid cache. */
	cache = inSuite->cache;
	if (cache == NULL || cache->common.allocPool == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Has this been processed already? */
	if (cache->libraries != NULL)
	{
		sjme_message("Using existing cache: %p", cache->libraries);

		*outLibs = cache->libraries;
		return SJME_ERROR_NONE;
	}

	/* Check list function. */
	listFunc = inSuite->functions->list;
	if (listFunc == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Call the list function. */
	result = NULL;
	if (SJME_IS_ERROR(error = listFunc(inSuite,
		&result)) || result == NULL)
		return error;

	/* Store it within the cache. */
	cache->libraries = result;

	/* Success! */
	*outLibs = result;
	return SJME_ERROR_NONE;
}
