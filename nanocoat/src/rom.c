/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/rom.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/payload.h"
#include "sjme/romInternal.h"

sjme_errorCode sjme_rom_fromMerge(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite** outSuite,
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
	sjme_attrOutNotNull sjme_rom_suite** outSuite,
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
	sjme_attrOutNotNull sjme_rom_suite** outSuite,
	sjme_attrInNotNull const sjme_rom_suiteFunctions* inFunctions)
{
	sjme_rom_suiteInitCacheFunc initCacheFunc;
	sjme_rom_suite* result;
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

	/* Use result. */
	*outSuite = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_rom_resolveClassPathById(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInNotNull sjme_rom_suite* inSuite,
	sjme_attrInNotNull const sjme_list_sjme_jint* inIds,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_rom_resolveClassPathByName(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInNotNull sjme_rom_suite* inSuite,
	sjme_attrInNotNull const sjme_list_sjme_lpcstr* inNames,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
