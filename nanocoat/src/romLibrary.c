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
#include "sjme/listUtil.h"

/** How much the class info list should grow by. */
#define SJME_NVM_ROM_CLASS_INFO_GROW 16

static sjme_errorCode sjme_nvm_rom_libraryCacheClassCheck(
	sjme_attrInNotNull sjme_list_sjme_pointer* inList,
	sjme_attrInPositive sjme_jint checkIndex,
	sjme_attrInNotNull sjme_pointer checkP,
	sjme_attrInValue sjme_jint againstI,
	sjme_attrInValue sjme_pointer againstP)
{
	sjme_nvm_class_info info;
	
	info = checkP;
	if (inList == NULL || info == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (againstI == info->fileNameHash &&
		strcmp(againstP, info->fileName) == 0)
		return SJME_ERROR_NONE;
	
	return SJME_ERROR_NOT_MATCHED;
}

sjme_errorCode sjme_nvm_rom_libraryCacheClass(
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_nvm_class_info* outClassInfo,
	sjme_attrInNotNull sjme_lpcstr fileName)
{
	sjme_errorCode error;
	sjme_jboolean exists;
	sjme_jint freeSlot;
	sjme_nvm_class_info maybe;
	sjme_list_sjme_nvm_class_info* classInfos;
	sjme_stream_input stream;
	sjme_lpstr dupFileName;
	
	if (inLibrary == NULL || outClassInfo == NULL || fileName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Query for existence first. */
	exists = SJME_JNI_FALSE;
	if (sjme_error_is(error = sjme_nvm_rom_libraryResourceExists(
		inLibrary, &exists, fileName)))
		return sjme_error_default(error);
	
	/* Not in here, so we can just never bother. */
	if (!exists)
		return SJME_ERROR_RESOURCE_NOT_FOUND;
	
	/* Lock the library to see if we already cached this. */
	if (sjme_error_is(error = sjme_thread_rwLockGrabRead(
		&inLibrary->rwLock)))
		return sjme_error_default(error);
	
	/* Find matching info item. */
	freeSlot = -1;
	maybe = NULL;
	classInfos = inLibrary->classInfos;
	if (sjme_error_is(error = sjme_listUtil_findItemWeak(
		SJME_AS_LIST_POINTER(classInfos),
		&freeSlot, (sjme_pointer*)&maybe,
		sjme_nvm_rom_libraryCacheClassCheck,
		sjme_string_hash(fileName),
		(sjme_pointer)fileName)))
		goto fail_findItem;
	
	/* Found something? */
	if (maybe != NULL)
		goto skip_foundInfo;
	
	/* Grab the write lock. */
	if (sjme_error_is(error = sjme_thread_rwLockGrabWrite(
		&inLibrary->rwLock)))
		goto fail_releaseGrab;
	
	/* The free slot might have been taken by something else if we got */
	/* unlucky in the lock cycle. */
	if (sjme_error_is(error = sjme_listUtil_findFree(
		SJME_AS_LIST_POINTER(classInfos), &freeSlot)))
		goto fail_findFree;
	
	/* Open as stream. */
	stream = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_libraryResourceAsStream(
		inLibrary, &stream, fileName)))
		goto fail_openRc;
	
	/* Parse class information. */
	if (sjme_error_is(error = sjme_nvm_class_parse(
		inLibrary->allocPool,
		stream, inLibrary->stringPool,
		&maybe)))
		goto fail_parseClass;
	
	/* Can close the stream now. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(stream))))
		goto fail_closeRc;
	stream = NULL;
	
	/* Duplicate file name. */
	dupFileName = NULL;
	if (sjme_error_is(error = sjme_alloc_strdup(inLibrary->allocPool,
		&dupFileName, fileName)) ||
		dupFileName == NULL)
		goto fail_dupName;
	
	/* Reference for keeping. */
	if (sjme_error_is(error = sjme_alloc_weakRef(maybe, NULL)))
		goto fail_countUp;
	
	/* File name is needed for caching. */
	maybe->fileName = dupFileName;
	maybe->fileNameHash = sjme_string_hash(dupFileName);
	
	/* Store info in for later caching. */
	classInfos->elements[freeSlot] = maybe;
	
	/* Release the write lock. */
	if (sjme_error_is(error = sjme_thread_rwLockReleaseWrite(
		&inLibrary->rwLock, NULL)))
		goto fail_releaseWrite;
	
	/* Release read lock. */
skip_foundInfo:
	if (sjme_error_is(error = sjme_thread_rwLockReleaseRead(
		&inLibrary->rwLock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outClassInfo = maybe;
	return SJME_ERROR_NONE;
	
fail_countUp:
fail_dupName:
	if (dupFileName != NULL)
	{
		sjme_alloc_free(dupFileName);
		dupFileName = NULL;
	}
fail_closeRc:
fail_parseClass:
fail_openRc:
	if (stream != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(stream));
		stream = NULL;
	}
fail_findFree:
	/* Release write lock before failing. */
	sjme_thread_rwLockReleaseWrite(&inLibrary->rwLock, NULL);
	
fail_findItem:
fail_releaseGrab:
fail_releaseWrite:
	/* Release read lock before failing. */
	sjme_thread_rwLockReleaseRead(&inLibrary->rwLock, NULL);

	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_rom_libraryHash(
	sjme_attrInNotNull sjme_nvm_rom_library library,
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

sjme_errorCode sjme_nvm_rom_libraryNew(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNotNull sjme_nvm_rom_library* outLibrary,
	sjme_attrInNotNull sjme_lpcstr libName,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInNotNull const sjme_nvm_rom_libraryFunctions* inFunctions,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_errorCode error;
	sjme_nvm_rom_library result;
	sjme_list_sjme_nvm_class_info* classInfos;
	sjme_nvm_stringPool stringPool;

	if (allocPool == NULL || outLibrary == NULL || inFunctions == NULL ||
		libName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Required. */
	if (inFunctions->init == NULL ||
		inFunctions->close == NULL)
		return sjme_error_notImplemented(0);
	
	/* Allocate class information list. */
	classInfos = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		SJME_NVM_ROM_CLASS_INFO_GROW,
		&classInfos, sjme_nvm_class_info, 0)) || classInfos == NULL)
		goto fail_allocInfos;
	
	/* Allocate string pool. */
	stringPool = NULL;
	if (sjme_error_is(error = sjme_nvm_stringPool_new(allocPool,
		&stringPool)) || stringPool == NULL)
		goto fail_allocStringPool;
	
	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(
		(sjme_nvm)allocPool,
		sizeof(*result), SJME_NVM_STRUCT_ROM_LIBRARY,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_alloc;
	
	/* Setup result. */
	result->allocPool = allocPool;
	result->functions = inFunctions;
	result->rwLock.read = &result->common.lock;
	result->classInfos = classInfos;
	result->stringPool = stringPool;
	
	/* Copy front end? */
	if (copyFrontEnd != NULL)
		memmove(&result->common.frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	result->common.frontEnd.bindType = SJME_FRONTEND_BINDLESS;
	
	/* Call initializer. */
	if (sjme_error_is(error = inFunctions->init(result, data)))
		goto fail_init;
	
	/* Set library name. */
	result->name = NULL;
	if (sjme_error_is(error = sjme_alloc_strdup(allocPool,
		(sjme_lpstr*)&result->name, libName)))
		goto fail_strdup;
	
	/* Success! */
	*outLibrary = result;
	return SJME_ERROR_NONE;

fail_refUp:
fail_strdup:
fail_init:
fail_commonInit:
fail_alloc:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
fail_allocStringPool:
	if (stringPool != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(stringPool));
fail_allocInfos:
	if (classInfos != NULL)
		sjme_alloc_free(classInfos);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_rom_libraryRawRead(
	sjme_attrInNotNull sjme_nvm_rom_library library,
	sjme_attrOutNotNullBuf(length) sjme_pointer destPtr,
	sjme_attrInPositive sjme_jint srcPos,
	sjme_attrInPositive sjme_jint length)
{
	/* This is just an alias for the other. */
	return sjme_nvm_rom_libraryRawReadIter(
		library, destPtr, 0, srcPos, 0, length);
}

sjme_errorCode sjme_nvm_rom_libraryRawReadIter(
	sjme_attrInNotNull sjme_nvm_rom_library library,
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
	if (library->checkedRawAccess &&
		!library->validRawAccess)
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
	if (sjme_error_is(error = sjme_nvm_rom_libraryRawSize(library,
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

sjme_errorCode sjme_nvm_rom_libraryRawSize(
	sjme_attrInNotNull sjme_nvm_rom_library library,
	sjme_attrOutNotNull sjme_jint* outSize)
{
	sjme_jint result;
	sjme_errorCode error;

	if (library == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Do we already know this will not work? */
	if (library->checkedRawAccess &&
		!library->validRawAccess)
		return SJME_ERROR_UNSUPPORTED_OPERATION;

	/* Size was already determined? */
	if (library->rawSize > 0)
		return library->rawSize;

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
	if (!library->checkedRawAccess)
	{
		library->checkedRawAccess = SJME_JNI_TRUE;
		library->validRawAccess = SJME_JNI_FALSE;
	}

	/* Not supported! */
	return SJME_ERROR_UNSUPPORTED_OPERATION;
}

sjme_errorCode sjme_nvm_rom_libraryResourceAsStream(
	sjme_attrInNotNull sjme_nvm_rom_library library,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_lpcstr rcName)
{
	sjme_nvm_rom_libraryResourceStreamFunc resourceFunc;
	sjme_stream_input result;
	sjme_errorCode error;

	if (library == NULL || outStream == NULL || rcName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* These must be set. */
	if (library->functions == NULL ||
		library->functions->resourceStream == NULL)
		return sjme_error_notImplemented(0);
	
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

sjme_errorCode sjme_nvm_rom_libraryResourceExists(
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jboolean* outExists,
	sjme_attrInNotNull sjme_lpcstr rcName)
{
	sjme_errorCode error;
	
	if (inLibrary == NULL || outExists == NULL || rcName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Is there a function to quickly check if it exists? */
	if (inLibrary->functions->resourceExists != NULL)
		return inLibrary->functions->resourceExists(inLibrary,
			outExists, rcName);
	
	/* Otherwise we need to open the actual stream to it. */
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
