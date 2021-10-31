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
#include "format/sqc.h"
#include "format/zip.h"
#include "debug.h"

/** The library drivers which are available for usage. */
static const sjme_libraryDriver* const sjme_libraryDrivers[] =
{
	&sjme_libraryZipDriver,
	&sjme_librarySqcDriver,
	
	NULL
};

/** Handler for library formats. */
static const sjme_formatHandler sjme_libraryFormatHandler =
{
	.driverOffsetOfDetect = offsetof(sjme_libraryDriver, detect),
	.driverOffsetOfInit = offsetof(sjme_libraryDriver, initInstance),
	.driverOffsetOfDestroy = offsetof(sjme_libraryDriver, destroyInstance),
	.driverList = (const void**)&sjme_libraryDrivers,
	.sizeOfInstance = sizeof(sjme_libraryInstance),
	.instanceOffsetOfFormat = offsetof(sjme_libraryInstance, format),
	.instanceOffsetOfState = offsetof(sjme_libraryInstance, state),
};

sjme_jboolean sjme_libraryClose(sjme_libraryInstance* instance,
	sjme_error* error)
{
	sjme_todo("Close library?");
}

sjme_jboolean sjme_libraryOpen(sjme_libraryInstance** outInstance,
	const void* data, sjme_jint size, sjme_error* error)
{
	/* Use common format handler. */
	if (!sjme_formatOpen(&sjme_libraryFormatHandler,
		(void**)outInstance, data, size, error))
	{
		sjme_setError(error, SJME_ERROR_UNKNOWN_LIBRARY_FORMAT,
			sjme_getError(error, SJME_ERROR_UNKNOWN));
		return sjme_false;
	}
	
	/* Copy the driver down. */
	(*outInstance)->driver = (*outInstance)->format.driver;
	
	/* All ready! */
	return sjme_true;
}
