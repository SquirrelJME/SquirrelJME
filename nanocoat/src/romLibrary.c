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

sjme_errorCode sjme_rom_libraryNew(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_library* outLibrary,
	sjme_attrInNotNull sjme_lpcstr libName,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNotNull const sjme_rom_libraryFunctions* inFunctions,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_rom_library result;

	if (pool == NULL || outLibrary == NULL || inFunctions == NULL ||
		libName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Required. */
	if (inFunctions->init == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(pool,
		sizeof(*result), sjme_nvm_enqueueHandler, NULL,
		(void**)&result, NULL)) || result == NULL)
		goto fail_alloc;
	
	/* Setup result. */
	result->common.type = SJME_NVM_STRUCTTYPE_ROM_LIBRARY;
	result->functions = inFunctions;
	
	/* Copy front end? */
	if (copyFrontEnd != NULL)
		memmove(&result->common.frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	
	/* Call initializer. */
	if (sjme_error_is(error = inFunctions->init(result, data)))
		goto fail_init;
	
	/* Set library name. */
	result->name = NULL;
	if (sjme_error_is(error = sjme_alloc_strdup(pool, &result->name, libName)))
		goto fail_strdup;
	
	/* Library is valid now, so count up. */
	if (sjme_error_is(error = sjme_alloc_weakRef(result, NULL)))
		goto fail_refUp;
	
	/* Success! */
	*outLibrary = result;
	return SJME_ERROR_NONE;

fail_refUp:
fail_strdup:
fail_init:
fail_alloc:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_rom_libraryRawRead(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNullBuf(length) sjme_pointer destPtr,
	sjme_attrInPositive sjme_jint srcPos,
	sjme_attrInPositive sjme_jint length)
{
	/* This is just an alias for the other. */
	return sjme_rom_libraryRawReadIter(
		library, destPtr, 0, srcPos, 0, length);
}

sjme_errorCode sjme_rom_libraryRawReadIter(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNullBuf(length) sjme_pointer destPtr,
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

	/* Do we already know this will not work? */
	if (library->cache.checkedRawAccess &&
		!library->cache.validRawAccess)
		return SJME_ERROR_UNSUPPORTED_OPERATION;

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
	libSize = -2;
	if (sjme_error_is(error = sjme_rom_libraryRawSize(library,
		&libSize)) || libSize < 0)
	{
		if (libSize == -1)
			return sjme_error_defaultOr(error,
				SJME_ERROR_UNSUPPORTED_OPERATION);
		return sjme_error_default(error);
	}

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
		(sjme_pointer)(((uintptr_t)destPtr) + destOffset),
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

	/* Do we already know this will not work? */
	if (library->cache.checkedRawAccess &&
		!library->cache.validRawAccess)
		return SJME_ERROR_UNSUPPORTED_OPERATION;

	/* Size was already determined? */
	if (library->cache.size > 0)
		return library->cache.size;

	/* Native handler must be valid! */
	if (library->functions->rawSize == NULL)
		goto fail_unsupported;

	/* Call native handler. */
	result = -2;
	if (sjme_error_is(error = library->functions->rawSize(
		library, &result)) || result < 0)
	{
		if (result == -1)
			goto fail_unsupported;
		return sjme_error_default(error);
	}

	/* Return result. */
	*outSize = result;
	return SJME_ERROR_NONE;

fail_unsupported:
	/* Cache whether this is supported, so we need not bother? */
	if (!library->cache.checkedRawAccess)
	{
		library->cache.checkedRawAccess = SJME_JNI_TRUE;
		library->cache.validRawAccess = SJME_JNI_FALSE;
	}

	/* Not supported! */
	return SJME_ERROR_UNSUPPORTED_OPERATION;
}

sjme_errorCode sjme_rom_libraryResourceAsStream(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_lpcstr rcName)
{
	sjme_rom_libraryResourceStreamFunc resourceFunc;
	sjme_stream_input result;
	sjme_errorCode error;

	if (library == NULL || outStream == NULL || rcName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* These must be set. */
	if (library->functions == NULL ||
		library->functions->resourceStream == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Lock library. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&library->common.lock)))
		return sjme_error_default(error);

	/* Get the resource function. */
	resourceFunc = library->functions->resourceStream;

	/* Ask for the resource. */
	/* Remember to remove any starting slash, because internally everything */
	/* is treated as absolute. */
	result = NULL;
	if (sjme_error_is(error = resourceFunc(library,
		&result,
		(rcName[0] == '/' ? rcName + 1 : rcName))) ||
		result == NULL)
		goto fail_locateResource;
	
	/* Unlock library. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&library->common.lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	*outStream = result;
	return SJME_ERROR_NONE;
	
	/* Unlock library. */
fail_locateResource:
	sjme_thread_spinLockRelease(&library->common.lock, NULL);
	
	return sjme_error_default(error);
}
