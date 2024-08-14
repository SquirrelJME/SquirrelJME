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
	cache = &inSuite->cache;
	if (cache->common.allocPool == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Has this been processed already? */
	if (cache->libraries != NULL)
	{
		/* Debug. */
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
	if (sjme_error_is(error = listFunc(inSuite,
		&result)) || result == NULL)
		return sjme_error_default(error);

	/* Store it within the cache. */
	cache->libraries = result;

	/* Success! */
	*outLibs = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_rom_suiteFromMerge(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull sjme_rom_suite* inSuites,
	sjme_attrInPositive sjme_jint numInSuites)
{
	if (pool == NULL || outSuite == NULL || inSuites == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (numInSuites < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Implement this?");
	return SJME_ERROR_UNKNOWN;
}

sjme_errorCode sjme_rom_suiteFromPayload(
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

sjme_errorCode sjme_rom_suiteFromZipSeekable(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull sjme_seekable seekable)
{
	if (pool == NULL || outSuite == NULL || seekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_rom_suiteNew(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull const sjme_rom_suiteFunctions* inFunctions,
	sjme_attrInNullable const sjme_frontEnd* inFrontEnd)
{
	sjme_rom_suiteinitFunc initFunc;
	sjme_rom_suite result;
	sjme_errorCode error;

	if (pool == NULL || outSuite == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* There needs to be a cache initializer. */
	initFunc = inFunctions->init;
	if (initFunc == NULL)
		return SJME_ERROR_INVALID_ARGUMENT;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);

#if 0
	/* Allocate resultant suite. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(pool,
		SJME_SIZEOF_SUITE_CORE_N(inFunctions->uncommonTypeSize),
		sjme_nvm_enqueueHandler, SJME_NVM_ENQUEUE_IDENTITY,
		&result, NULL)) || result == NULL)
		return sjme_error_default(error);

	/* Setup some basic cache details. */
	result->functions = inFunctions;
	result->cache.common.allocPool = pool;
	result->cache.common.uncommonSize = inFunctions->uncommonTypeSize;

	/* Seed front end information? */
	if (inFrontEnd != NULL)
		result->cache.common.frontEnd = *inFrontEnd;

	/* Initialize cache. */
	if (sjme_error_is(error = initFunc(result)))
	{
		/* Cleanup bad pointer. */
		sjme_alloc_free(result);

		return sjme_error_default(error);
	}

	/* Initialize fields. */
	result->functions = inFunctions;
	result->cache.common.allocPool = pool;

	/* Use result. */
	*outSuite = result;
	return SJME_ERROR_NONE;
#endif
}
