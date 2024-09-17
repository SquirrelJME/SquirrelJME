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
#include "sjme/cleanup.h"

sjme_errorCode sjme_rom_suiteDefaultLaunch(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrOutNotNull sjme_lpstr* outMainClass,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outMainArgs,
	sjme_attrOutNotNull sjme_list_sjme_jint** outById,
	sjme_attrOutNotNull sjme_list_sjme_lpstr** outByName)
{
	sjme_errorCode error;
	
	if (inPool == NULL || inSuite == NULL || outMainClass == NULL ||
		outMainArgs == NULL || outById == NULL || outByName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not supported internally? */
	if (inSuite->functions->defaultLaunch == NULL)
		return SJME_ERROR_UNSUPPORTED_OPERATION;
	
	/* Lock suite. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inSuite->common.lock)))
		return sjme_error_default(error);
	
	/* Forward call. */
	error = inSuite->functions->defaultLaunch(inPool,
		inSuite, outMainClass, outMainArgs, outById, outByName);
		
	/* Unlock suite. */
	if (sjme_error_is(sjme_thread_spinLockRelease(
		&inSuite->common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
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

sjme_errorCode sjme_rom_suiteLibraries(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs)
{
	sjme_rom_suiteCache* cache;
	sjme_rom_suiteListLibrariesFunc listFunc;
	sjme_list_sjme_rom_library* result;
	sjme_errorCode error;
	sjme_jint i, n;

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
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Lock suite. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inSuite->common.lock)))
		return sjme_error_default(error);

	/* Call the list function. */
	result = NULL;
	if (sjme_error_is(error = listFunc(inSuite,
		&result)) || result == NULL)
		goto fail_list;

	/* Store it within the cache. */
	cache->libraries = result;
	
	/* All of these must be valid libraries. */
	for (i = 0, n = result->length; i < n; i++)
		if (result->elements[i] == NULL)
		{
			error = SJME_ERROR_LIBRARY_NOT_FOUND;
			goto fail_missingLib;
		}
	
	/* Unlock suite. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inSuite->common.lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	*outLibs = result;
	return SJME_ERROR_NONE;

fail_missingLib:
fail_list:
	/* Release lock before failing. */
	if (sjme_error_is(sjme_thread_spinLockRelease(
		&inSuite->common.lock, NULL)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_rom_suiteNew(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNotNull const sjme_rom_suiteFunctions* inFunctions,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_rom_suite result;
	sjme_errorCode error;

	if (pool == NULL || outSuite == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* These functions are required. */
	if (inFunctions->init == NULL ||
		inFunctions->libraryId == NULL ||
		inFunctions->list == NULL ||
		inFunctions->loadLibrary == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
		
	/* Allocate resultant suite. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(pool,
		sizeof(*result), NULL, NULL,
		(void**)&result, NULL)) || result == NULL)
		goto fail_alloc;
	
	/* Common initialize. */
	if (sjme_error_is(error = sjme_nvm_initCommon(
		SJME_AS_NVM_COMMON(result),
		SJME_NVM_STRUCT_ROM_SUITE)))
		goto fail_commonInit;
	
	/* Setup result. */
	result->cache.common.allocPool = pool;
	result->functions = inFunctions;
	
	/* Copy front end data? */
	if (copyFrontEnd != NULL)
		memmove(&result->common.frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	
	/* Call initializer. */
	if (sjme_error_is(error = inFunctions->init(result, data)))
		goto fail_init;
	
	/* Success! */
	*outSuite = result;
	return SJME_ERROR_NONE;

fail_refUp:
fail_init:
fail_commonInit:
fail_alloc:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	
	return sjme_error_default(error);
}
