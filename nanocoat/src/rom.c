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

sjme_errorCode sjme_rom_libraryHash(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNull sjme_jint* outHash)
{
	if (library == NULL || outHash == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Does it need to be calculated? */
	if (library->nameHash == 0)
	{
		/* This should always be set. */
		if (library->name == NULL)
			return SJME_ERROR_ILLEGAL_STATE;

		/* Calculate the hash. */
		library->nameHash = sjme_string_hash(library->name);
	}

	/* Copy it in. */
	*outHash = library->nameHash;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_rom_libraryRawRead(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNullBuf(length) void* destPtr,
	sjme_attrInPositive sjme_jint srcPos,
	sjme_attrInPositive sjme_jint length)
{
	/* This is just an alias for the other. */
	return sjme_rom_libraryRawReadIter(
		library, destPtr, 0, srcPos, 0, length);
}

sjme_errorCode sjme_rom_libraryRawReadIter(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNullBuf(length) void* destPtr,
	sjme_attrInPositive sjme_jint destOffset,
	sjme_attrInPositive sjme_jint srcPos,
	sjme_attrInPositive sjme_jint srcOffset,
	sjme_attrInPositive sjme_jint length)
{
	uintptr_t rawDestPtr;
	sjme_errorCode error;
	sjme_jint libSize;

	if (library == NULL || destPtr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Check all the bounds variants possible, for overflow as well. */
	rawDestPtr = (uintptr_t)destPtr;
	if (destOffset < 0 || srcPos < 0 || srcOffset < 0 || length < 0 ||
		(destOffset + length) < 0 || (srcPos + length) < 0 ||
		(srcOffset + length) < 0 || (srcPos + srcOffset + length) < 0 ||
		(rawDestPtr + destOffset) < rawDestPtr ||
		(rawDestPtr + length) < rawDestPtr ||
		(rawDestPtr + destOffset + length) < rawDestPtr)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Get the raw size of the target library. */
	libSize = -1;
	if (SJME_IS_ERROR(error = sjme_rom_libraryRawSize(library,
		&libSize)) || libSize < 0)
		return SJME_DEFAULT_ERROR(error);

	/* Check bounds of the size to ensure it is correct. */
	if (length > libSize || (srcPos + length) > libSize ||
		(srcPos + srcOffset) > libSize ||
		(srcPos + srcOffset + length) > libSize)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Check native library handler. */
	if (library->functions->rawData == NULL)
		return SJME_ERROR_UNSUPPORTED_OPERATION;

	/* Call native library handler, which takes simpler arguments. */
	return library->functions->rawData(library,
		(void*)(((uintptr_t)destPtr) + destOffset),
		srcPos + srcOffset, length);
}

sjme_errorCode sjme_rom_libraryRawSize(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNull sjme_jint* outSize)
{
	sjme_jint result;
	sjme_errorCode error;

	if (library == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Size was already determined? */
	if (library->cache.size > 0)
		return library->cache.size;

	/* Native handler must be valid! */
	if (library->functions->rawSize == NULL)
		return SJME_ERROR_UNSUPPORTED_OPERATION;

	/* Call native handler. */
	result = -1;
	if (SJME_IS_ERROR(error = library->functions->rawSize(
		library, &result)) || result < 0)
		return SJME_DEFAULT_ERROR(error);

	/* Return result. */
	*outSize = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_rom_newLibrary(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_library* outLibrary,
	sjme_attrInNotNull const sjme_rom_libraryFunctions* inFunctions,
	sjme_attrInNullable const sjme_frontEnd* inFrontEnd)
{
	sjme_rom_libraryInitCacheFunc initCacheFunc;
	sjme_rom_library result;
	sjme_errorCode error;

	if (pool == NULL || outLibrary == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* There needs to be a cache initializer. */
	initCacheFunc = inFunctions->initCache;
	if (initCacheFunc == NULL)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Allocate resultant library. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc(pool,
		SJME_SIZEOF_LIBRARY_CORE_N(inFunctions->uncommonTypeSize),
		&result)) || result == NULL)
		return SJME_DEFAULT_ERROR(error);

	/* Setup some basic cache details. */
	result->functions = inFunctions;
	result->cache.common.allocPool = pool;
	result->cache.common.uncommonSize = inFunctions->uncommonTypeSize;

	/* Seed front end information? */
	if (inFrontEnd != NULL)
		result->cache.common.frontEnd = *inFrontEnd;

	/* Initialize cache. */
	if (SJME_IS_ERROR(error = initCacheFunc(result)))
	{
		/* Cleanup bad pointer. */
		sjme_alloc_free(result);

		return SJME_DEFAULT_ERROR(error);
	}

	/* Initialize fields. */
	result->functions = inFunctions;
	result->cache.common.allocPool = pool;

	/* Use result. */
	*outLibrary = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_rom_newSuite(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull const sjme_rom_suiteFunctions* inFunctions,
	sjme_attrInNullable const sjme_frontEnd* inFrontEnd)
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
	if (SJME_IS_ERROR(error = sjme_alloc(pool,
		SJME_SIZEOF_SUITE_CORE_N(inFunctions->uncommonTypeSize),
		&result)) || result == NULL)
		return SJME_DEFAULT_ERROR(error);

	/* Setup some basic cache details. */
	result->functions = inFunctions;
	result->cache.common.allocPool = pool;
	result->cache.common.uncommonSize = inFunctions->uncommonTypeSize;

	/* Seed front end information? */
	if (inFrontEnd != NULL)
		result->cache.common.frontEnd = *inFrontEnd;

	/* Initialize cache. */
	if (SJME_IS_ERROR(error = initCacheFunc(result)))
	{
		/* Cleanup bad pointer. */
		sjme_alloc_free(result);

		return SJME_DEFAULT_ERROR(error);
	}

	/* Initialize fields. */
	result->functions = inFunctions;
	result->cache.common.allocPool = pool;

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

	/* Debug. */
	sjme_message("sjme_rom_resolveClassPathById(%p, %p, %p)",
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
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Make sure it is cleared. */
	memset(working, 0, sizeof(*working) * length);

	/* Debug. */
	sjme_message("Getting library list...");

	/* Obtain the list of libraries within the suite. */
	suiteLibs = NULL;
	if (SJME_IS_ERROR(error = sjme_rom_suiteLibraries(inSuite,
		&suiteLibs)) || suiteLibs == NULL)
		return SJME_DEFAULT_ERROR(error);

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
			if (SJME_IS_ERROR(error = inSuite->functions->libraryId(
				inSuite, checkLibrary, &libId)))
				return SJME_DEFAULT_ERROR(error);

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
		sjme_rom_library, 0, length, outLibs, working);
}

sjme_errorCode sjme_rom_resolveClassPathByName(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrInNotNull const sjme_list_sjme_lpcstr* inNames,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs)
{
	sjme_list_sjme_rom_library* suiteLibs;
	sjme_errorCode error;
	sjme_jint length, i, at, hash, numSuiteLibs;
	sjme_rom_library* working;
	sjme_rom_library lib;
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
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Clear memory. */
	memset(working, 0, sizeof(*working) * length);
	memset(inHashes, 0, sizeof(*inHashes) * length);

	/* First hash all the input libraries, so we can quickly scan through. */
	for (i = 0; i < length; i++)
		inHashes[i] = sjme_string_hash(inNames->elements[i]);

	/* Obtain the list of libraries within the suite. */
	suiteLibs = NULL;
	if (SJME_IS_ERROR(error = sjme_rom_suiteLibraries(inSuite,
		&suiteLibs) || suiteLibs == NULL))
		return SJME_DEFAULT_ERROR(error);

	/* Go through each library and get hash matches. */
	numSuiteLibs = suiteLibs->length;
	for (i = 0; i < numSuiteLibs; i++)
	{
		/* Get hash of this library. */
		lib = suiteLibs->elements[i];
		if (SJME_IS_ERROR(error = sjme_rom_libraryHash(lib,
			&hash)))
			return SJME_DEFAULT_ERROR(error);

		/* Look for match in output. */
		for (at = 0; at < length; at++)
			if (working[at] == NULL && inHashes[at] == hash &&
				0 == strcmp(inNames->elements[at], lib->name))
				working[at] = lib;
	}

	/* Scan through and fail if any are null, that is not found. */
	for (at = 0; at < length; at++)
		if (working[at] == NULL)
			return SJME_ERROR_LIBRARY_NOT_FOUND;

	/* Return the libraries which gets placed into a list as a copy. */
	return sjme_list_newA(inSuite->cache.common.allocPool,
		sjme_rom_library, 0, length, outLibs, working);
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
	if (SJME_IS_ERROR(error = listFunc(inSuite,
		&result)) || result == NULL)
		return SJME_DEFAULT_ERROR(error);

	/* Store it within the cache. */
	cache->libraries = result;

	/* Success! */
	*outLibs = result;
	return SJME_ERROR_NONE;
}
