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
	.driverOffsetOfInit = offsetof(sjme_packDriver, initInstance),
	.driverList = (const void**)&sjme_packDrivers,
	.sizeOfInstance = sizeof(sjme_packInstance),
	.instanceOffsetOfFormat = offsetof(sjme_packInstance, format),
	.instanceOffsetOfState = offsetof(sjme_packInstance, state),
};

sjme_jboolean sjme_packClose(sjme_packInstance* instance,
	sjme_error* error)
{
	if (instance == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	sjme_todo("Close?");
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
	
	/* Initialize the library cache. */
	if (numLibs >= 0)
		instance->libraries = sjme_malloc(
			sizeof(*instance->libraries) * numLibs, error);
	
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
	
	/* All ready! */
	return sjme_true;
}
