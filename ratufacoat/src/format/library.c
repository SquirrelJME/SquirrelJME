/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "format/detect.h"
#include "format/format.h"
#include "format/library.h"
#include "format/memfile.h"
#include "format/sqc.h"
#include "format/zip.h"
#include "debug.h"

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
{
	.driverOffsetOfDetect = offsetof(sjme_libraryDriver, detect),
	.driverOffsetOfInit = offsetof(sjme_libraryDriver, init),
	.driverOffsetOfDestroy = offsetof(sjme_libraryDriver, destroy),
	.driverList = (const void**)&sjme_libraryDrivers,
	.sizeOfInstance = sizeof(sjme_libraryInstance),
	.instanceOffsetOfFormat = offsetof(sjme_libraryInstance, format),
	.instanceOffsetOfState = offsetof(sjme_libraryInstance, state),
};


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
	
	return sjme_libraryClose(counter->collectData, error);
}

sjme_jboolean sjme_libraryClose(sjme_libraryInstance* instance,
	sjme_error* error)
{
	sjme_todo("Close library?");
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
	(*outInstance)->driver = (*outInstance)->format.driver;
	
	/* Initialize the counter for garbage collection. */
	instance->counter.collect = sjme_libraryCollect;
	instance->counter.collectData = instance;
	sjme_atomicIntGetThenAdd(&instance->counter.count, 1);
	
	/* All ready! */
	return sjme_true;
}
