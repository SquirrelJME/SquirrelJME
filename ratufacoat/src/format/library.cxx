/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "error.h"
#include "format/library.h"
#include "format/format.h"
#include "format/library.h"
#include "format/memfile.h"
#include "format/sqc.h"
#include "format/zip.h"

/** The library drivers which are available for usage. */
static const sjme_libraryDriver* const sjme_libraryDrivers[] =
{
	&sjme_librarySqcDriver,
	&sjme_libraryZipDriver,

	/* Fallback driver, so anything that fails above is a plain file. */
	&sjme_libraryMemFileDriver,

	NULL
};

/** Handler for library formats. */
static const sjme_formatHandler sjme_libraryFormatHandler =
	SJME_DESIGNATED(sjme_formatHandler,
		s_.driverOffsetOfDetect = offsetof(sjme_libraryDriver, detect),
		s_.driverOffsetOfInit = offsetof(sjme_libraryDriver, init),
		s_.driverOffsetOfDestroy = offsetof(sjme_libraryDriver, destroy),
		s_.driverList = (const void**)&sjme_libraryDrivers,
		s_.sizeOfInstance = sizeof(sjme_libraryInstance),
		s_.instanceOffsetOfFormat = offsetof(sjme_libraryInstance, format),
		s_.instanceOffsetOfState = offsetof(sjme_libraryInstance, state)
	);

/**
 * Wraps a chunk to a stream.
 *
 * @param libInstance The library.
 * @param outStream The output stream.
 * @param index The index to open.
 * @param error The error state.
 * @return If opening was a success or not.
 * @since 2021/12/16
 */
static sjme_jboolean sjme_libraryWrapChunkToStream(
	sjme_libraryInstance* libInstance, sjme_dataStream** outStream,
	sjme_jint index, sjme_error* error)
{
	sjme_countableMemChunk* outChunk;

	if (libInstance == NULL || outStream == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	if (index < 0 || index >= libInstance->numEntries)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, index);

		return sjme_false;
	}

	/* No ability to read chunks? Do not infinite recurse, just fail here. */
	if (libInstance->driver->entryChunk == NULL)
	{
		sjme_setError(error, SJME_ERROR_NOT_IMPLEMENTED, 0);

		return sjme_false;
	}

	/* Open memory chunk to the entry data. */
	outChunk = NULL;
	if (!libInstance->driver->entryChunk(libInstance, &outChunk, index, error))
	{
		if (!sjme_hasError(error))
			sjme_setError(error, SJME_ERROR_INVALID_JAR_FILE, index);

		return sjme_false;
	}

	/* Wrap the chunk accordingly. */
	return sjme_streamFromChunkCounted(outStream, outChunk, 0,
		outChunk->chunk.size, sjme_false, error);
}

/**
 * Wraps a stream to a chunk.
 *
 * @param libInstance The library.
 * @param outChunk The output chunk.
 * @param index The index to open.
 * @param error The error state.
 * @return If opening was a success or not.
 * @since 2021/12/16
 */
static sjme_jboolean sjme_libraryWrapStreamToChunk(
	sjme_libraryInstance* libInstance, sjme_countableMemChunk** outChunk,
	sjme_jint index, sjme_error* error)
{
	if (libInstance == NULL || outChunk == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	if (index < 0 || index >= libInstance->numEntries)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, index);

		return sjme_false;
	}

	/* No ability to read streams? Do not infinite recurse, just fail here. */
	if (libInstance->driver->entryStream == NULL)
	{
		sjme_setError(error, SJME_ERROR_NOT_IMPLEMENTED, 0);

		return sjme_false;
	}

	sjme_todo("Implement this?");

	return sjme_false;
}

/**
 * Performs library garbage collection.
 *
 * @param counter The counter used.
 * @param error The error state.
 * @return If collection was successful.
 * @since 2021/11/07
 */
static sjme_jboolean sjme_libraryCollect(sjme_counter* counter,
	sjme_error* error)
{
	if (counter == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	return sjme_libraryClose(
		(sjme_libraryInstance*)counter->dataPointer, error);
}

sjme_jboolean sjme_libraryClose(sjme_libraryInstance* instance,
	sjme_error* error)
{
	sjme_packInstance* packOwner;
	sjme_jint packIndex;
	sjme_jboolean closeOkay;

	if (instance == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	/* Perform initial close of library. */
	packOwner = (sjme_packInstance*)instance->packOwner;
	packIndex = instance->packIndex;
	if (packOwner != NULL)
		if (!sjme_packLibraryMarkClosed(packOwner,
			instance, packIndex,
			sjme_false, error))
		{
			sjme_setError(error, sjme_getError(error,
				SJME_ERROR_BAD_PACK_LIB_CLOSE), 0);

			return sjme_false;
		}

	/* Perform any closing required by the driver. */
	closeOkay = sjme_formatClose(&sjme_libraryFormatHandler, instance,
		error);

	/* After doing everything, perform a post complete to the pack to free
	 * any potential resources. */
	if (packOwner != NULL)
		if (!sjme_packLibraryMarkClosed(packOwner, NULL,
			packIndex, sjme_true, error))
		{
			sjme_setError(error, sjme_getError(error,
				SJME_ERROR_BAD_PACK_LIB_CLOSE), 0);

			return sjme_false;
		}

	/* Closing complete, assuming it even worked... */
	return closeOkay;
}

sjme_jboolean sjme_libraryEntryChunk(sjme_libraryInstance* libInstance,
	sjme_countableMemChunk** outChunk, sjme_jint index, sjme_error* error)
{
	if (libInstance == NULL || outChunk == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	if (index < 0 || index >= libInstance->numEntries)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, index);

		return sjme_false;
	}

	/* If not supported, go through a wrapper to get this entry. */
	if (libInstance->driver->entryChunk == NULL)
		return sjme_libraryWrapStreamToChunk(libInstance, outChunk,
			index, error);

	/* Use library implementation of this function. */
	return libInstance->driver->entryChunk(libInstance, outChunk,
		index, error);
}

sjme_jboolean sjme_libraryEntryStream(sjme_libraryInstance* libInstance,
	sjme_dataStream** outStream, sjme_jint index, sjme_error* error)
{
	if (libInstance == NULL || outStream == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);

		return sjme_false;
	}

	if (index < 0 || index >= libInstance->numEntries)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, index);

		return sjme_false;
	}

	/* If not supported, go through a wrapper to get this entry. */
	if (libInstance->driver->entryStream == NULL)
		return sjme_libraryWrapChunkToStream(libInstance, outStream,
			index, error);

	/* Use library implementation of this function. */
	return libInstance->driver->entryStream(libInstance, outStream,
		index, error);
}

sjme_jboolean sjme_libraryOpen(sjme_libraryInstance** outInstance,
	const void* data, sjme_jint size, sjme_error* error)
{
	sjme_libraryInstance* instance;

	/* Use common format handler. */
	if (!sjme_formatOpen(&sjme_libraryFormatHandler,
		(void**)outInstance, data, size, error))
	{
		sjme_setError(error, SJME_ERROR_UNKNOWN_LIBRARY_FORMAT,
			sjme_getError(error, SJME_ERROR_UNKNOWN));
		return sjme_false;
	}

	/* Copy the driver down. */
	instance = (*outInstance);
	(*outInstance)->driver =
		(const sjme_libraryDriver*)(*outInstance)->format.driver;

	/* Initialize the counter for garbage collection. */
	sjme_counterInit(&instance->counter, sjme_libraryCollect,
		instance, 0, error);

	/* All ready! */
	return sjme_true;
}
