/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "format/format.h"
#include "format/pack.h"
#include "format/sqc.h"
#include "memory.h"
#include "engine/scafdef.h"

/** The pack drivers which are available for usage. */
static const sjme_packDriver* const sjme_packDrivers[] =
{
	&sjme_packSqcDriver,
	
	NULL
};

/** Handler for pack formats. */
static const sjme_formatHandler sjme_packFormatHandler =
{
	.driverOffsetOfDetect = offsetof(sjme_packDriver, detect),
	.driverOffsetOfInit = offsetof(sjme_packDriver, init),
	.driverOffsetOfDestroy = offsetof(sjme_packDriver, destroy),
	.driverList = (const void**)&sjme_packDrivers,
	.sizeOfInstance = sizeof(sjme_packInstance),
	.instanceOffsetOfFormat = offsetof(sjme_packInstance, format),
	.instanceOffsetOfState = offsetof(sjme_packInstance, state),
};

/**
 * Performs pack garbage collection.
 * 
 * @param counter The counter used.
 * @param error The error state.
 * @return If collection was successful.
 * @since 2021/11/07
 */
static sjme_jboolean sjme_packCollect(sjme_counter* counter, sjme_error* error)
{
	if (counter == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	return sjme_packClose(counter->dataPointer, error);
}

sjme_jboolean sjme_packClassPathFromCharStar(sjme_packInstance* pack,
	const char** classPath, sjme_classPath** result, sjme_error* error)
{
	sjme_todo("Implement this?");
	return sjme_false;
}

sjme_jboolean sjme_packClose(sjme_packInstance* instance,
	sjme_error* error)
{
	sjme_jboolean failingLib;
	sjme_jboolean badFree;
	sjme_jboolean badPackClose;
	sjme_jint numLibs;
	
	if (instance == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Make sure these are cleared! */
	failingLib = badFree = badPackClose = sjme_false;
	
	/* Close out any open libraries. */
	numLibs = instance->numLibraries;
	for (int i = 0; i < numLibs; i++)
	{
		/* Clear out the cache first. */
		sjme_libraryInstance* library = sjme_atomicPointerSetType(
			&instance->libraries[i], NULL, sjme_libraryInstance*);
		if (library == NULL)
			continue;
		
		/* Try closing the library. */
		if (!sjme_libraryClose(library, error))
			failingLib = sjme_true;
	}
	
	/* Wipe out libraries. */
	instance->numLibraries = -1;
	badFree |= !sjme_free(instance->libraries, error);
	instance->libraries = NULL;
	
	/* Perform normal pack close. */
	badPackClose |= !sjme_formatClose(&sjme_packFormatHandler, instance,
		error);
	
	/* Has there been a close failure? */
	return !failingLib && !badFree && !badPackClose;
}

sjme_jboolean sjme_packGetLauncherDetail(sjme_packInstance* packInstance,
	sjme_utfString** outMainClass, sjme_mainArgs** outArgs,
	sjme_classPath** outClassPath, sjme_error* error)
{
	if (packInstance == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Read the main class. */
	if (outMainClass != NULL)
		if (packInstance->driver->queryLauncherClass != NULL &&
			!packInstance->driver->queryLauncherClass(packInstance,
				outMainClass, error))
		{
			if (!sjme_hasError(error))
				sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 0);
			return sjme_false;
		}
	
	/* Read the arguments for the main class. */
	if (outArgs != NULL)
		if (packInstance->driver->queryLauncherArgs != NULL &&
			!packInstance->driver->queryLauncherArgs(packInstance,
				outArgs, error))
		{
			if (!sjme_hasError(error))
				sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 0);
			return sjme_false;
		}
	
	/* Read the class path for the main class. */
	if (outClassPath != NULL)
		if (packInstance->driver->queryLauncherClassPath != NULL &&
			!packInstance->driver->queryLauncherClassPath(packInstance,
				outClassPath, error))
		{
			if (!sjme_hasError(error))
				sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 0);
			return sjme_false;
		}
	
	/* Should have all read just fine. */
	return sjme_true;
}

sjme_jboolean sjme_packOpen(sjme_packInstance** outInstance,
	const void* data, sjme_jint size, sjme_error* error)
{
	sjme_jint numLibs;
	sjme_packInstance* instance;
	
	/* Use common format handler. */
	if (!sjme_formatOpen(&sjme_packFormatHandler,
		(void**)outInstance, data, size, error))
	{
		sjme_setError(error, SJME_ERROR_UNKNOWN_PACK_FORMAT,
			sjme_getError(error, SJME_ERROR_UNKNOWN));
		return sjme_false;
	}
	
	/* Copy the driver down. */
	instance = (*outInstance);
	(*outInstance)->driver = (*outInstance)->format.driver;
	
	/* Query the number of libraries to initialize the library cache. */
	numLibs = (instance->driver->queryNumLibraries == NULL ? -1 :
		instance->driver->queryNumLibraries(instance, error));
	instance->numLibraries = numLibs;
	
	/* Initialize the library cache, keep a minimum of a single byte as
	 * we cannot just allocate otherwise if there is nothing... */
	if (numLibs >= 0)
		instance->libraries = sjme_malloc(sjme_max(1,
			sizeof(*instance->libraries) * numLibs), error);
	
	/* Failed to initialize either? */
	if (numLibs < 0 || instance->libraries == NULL)
	{
		if (!sjme_packClose(instance, error))
			sjme_setError(error, SJME_ERROR_FAILED_TO_CLOSE_PACK,
				-1);
		
		sjme_setError(error, (numLibs < 0 ?
			SJME_ERROR_INVALID_NUM_LIBRARIES : SJME_ERROR_NO_MEMORY),
			sjme_getError(error, 0));
		
		return sjme_false;
	}
	
	/* Load flags regarding the library. */
	instance->flags = (instance->driver->queryPackFlags == NULL ? 0 :
		instance->driver->queryPackFlags(instance, error));
	
	/* Initialize the counter for garbage collection. */
	sjme_counterInit(&instance->counter, sjme_packCollect,
		instance, 0, error);
	
	/* All ready! */
	return sjme_true;
}

sjme_jboolean sjme_packClassPathMapper(sjme_packInstance* packInstance,
	sjme_classPath* outClassPath, sjme_jint index, sjme_jint targetLibIndex,
	sjme_error* error)
{
	sjme_libraryInstance* outLib;
	
	if (packInstance == NULL || outClassPath == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Debugging. */
#if defined(SJME_DEBUG)
	sjme_message("Mapping class path at %d to %d...",
		index, targetLibIndex);
#endif
	
	if (index < 0 || index >= outClassPath->count ||
		targetLibIndex < 0 || targetLibIndex >= packInstance->numLibraries)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, index + targetLibIndex);
		return sjme_false;
	}

	/* Open the library that this belongs to. */
	outLib = NULL;
	if (!sjme_packLibraryOpen(packInstance, &outLib,
		targetLibIndex, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 0);
		return sjme_false;
	}
	
	/* Since we have it, store it! */
	outClassPath->libraries[index] = outLib;
	
	/* Success! */
	return sjme_true;
}

sjme_jboolean sjme_packLibraryMarkClosed(sjme_packInstance* packInstance,
	sjme_libraryInstance* libInstance, sjme_jint index,
	sjme_jboolean postComplete, sjme_error* error)
{
	if (packInstance == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Within bounds? */
	if (index < 0 || index >= packInstance->numLibraries)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, index);
		
		return sjme_false;
	}
	
	/* Operations before we perform destruction. */
	if (!postComplete)
	{
		/* Remove the library from the cache list, if it matches.
		 * If we have an instance, only clear the instance if it matches
		 * since the library could have been opened just again.
		 * We always clear this first since we are about to invalidate it,
		 * if we happen to immediately open after we can just continue
		 * along while we cleanup this one. */
		if (libInstance != NULL)
			sjme_atomicPointerCompareThenSet(
				&packInstance->libraries[index],
				libInstance, NULL);
		else
			sjme_atomicPointerSet(&packInstance->libraries[index],
				NULL);
	}
	
	/* If there is no function for marking closed, then just ignore this. */
	if (packInstance->driver->libraryMarkClosed == NULL)
		return sjme_true;
	
	/* Call the library handler. */
	return packInstance->driver->libraryMarkClosed(packInstance, index,
		postComplete, error);
}

sjme_jboolean sjme_packLibraryOpen(sjme_packInstance* packInstance,
	sjme_libraryInstance** outLibrary, sjme_jint index, sjme_error* error)
{
	sjme_libraryInstance* lib;
	sjme_libraryInstance* oldLib;
	sjme_memChunk chunk;
	
	if (packInstance == NULL || outLibrary == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Attempting to open an invalid library? */
	if (index < 0 || index >= packInstance->numLibraries)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, 0);
		
		return sjme_false;
	}
	
	/* Has this library already been cached? */
	lib = sjme_atomicPointerGet(&packInstance->libraries[index]);
	if (lib != NULL)
	{
		/* Reference the library and count it up, we are using it. */
		*outLibrary = lib;
		if (!sjme_counterUp(&lib->counter, error))
			return sjme_false;
		
		return sjme_true;
	}
	
	/* Attempt to load the given library. */
	memset(&chunk, 0, sizeof(chunk));
	if (packInstance->driver->locateChunk != NULL &&
		!packInstance->driver->locateChunk(packInstance, &chunk, index, error))
	{
		if (!sjme_hasError(error))
			sjme_setError(error, SJME_ERROR_BAD_LOAD_LIBRARY, index);
		
		return sjme_false;
	}
	
	/* There was no actual data for this chunk, we cannot load it! */
	if (chunk.data == NULL || chunk.size <= 0)
	{
		sjme_setError(error, SJME_ERROR_BAD_LOAD_LIBRARY, index);
		
		return sjme_false;
	}
	
	/* Open the library from the chunk. */
	if (!sjme_libraryOpen(&lib, chunk.data, chunk.size,
		error))
	{
		if (!sjme_hasError(error))
			sjme_setError(error, SJME_ERROR_BAD_LOAD_LIBRARY, -index);
		
		return sjme_false;
	}
	
	/* Initialize references back to this pack in the library. */
	lib->packOwner = packInstance;
	lib->packIndex = index;
	
	/* Cache the chunk for later usage. */
	oldLib = sjme_atomicPointerSet(&packInstance->libraries[index],
		lib);
	if (oldLib != NULL)
		sjme_message("There was an old library used here: %p?", oldLib);
	
	/* Debugging. */
#if defined(SJME_DEBUG)
	sjme_message("Opened non-cached library by index %d: %s",
		lib->packIndex, (lib->name != NULL ? lib->name->chars : NULL));
#endif
	
	/* This library is available for usage. */
	*outLibrary = lib;
	return sjme_true;
}
