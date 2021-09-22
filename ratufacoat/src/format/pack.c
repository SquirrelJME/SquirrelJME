/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "format/pack.h"
#include "format/sqc.h"
#include "memory.h"

/** The pack drivers which are available for usage. */
const sjme_packDriver* const sjme_packDrivers[] =
{
	&sjme_packSqcDriver,
	
	NULL
};

sjme_jboolean sjme_packOpen(sjme_packInstance** outInstance,
	const void* data, sjme_jint size, sjme_error* error)
{
	const sjme_packDriver* tryDriver;
	sjme_packInstance* instance;
	
	/* Try to detect the format using the common means. */
	if (!sjme_detectFormat(data, size,
		(const void**)&tryDriver, (const void**)sjme_packDrivers,
		offsetof(sjme_packDriver, detect), error))
	{
		sjme_setError(error, SJME_ERROR_UNKNOWN_PACK_FORMAT,
			0);
		return sjme_false;
	}
	
	/* Allocate instance data. */
	instance = sjme_malloc(sizeof(*instance), error);
	if (instance == NULL)
		return sjme_false;
	
	/* Setup parameters for it. */
	instance->driver = tryDriver;
	instance->data = data;
	instance->size = size;
	
	/* Try to initialize the driver, if that fails then oops. */
	if (instance->driver->initInstance == NULL ||
		!instance->driver->initInstance(instance))
	{
		sjme_free(instance, error);
		
		sjme_setError(error, SJME_ERROR_BAD_DRIVER_INIT, 0);
		return sjme_false;
	}
	
	/* Use this instance. */
	*outInstance = instance;
	return sjme_true;
}
