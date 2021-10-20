/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "error.h"
#include "format/detect.h"
#include "format/format.h"
#include "memory.h"

sjme_jboolean sjme_formatOpen(const sjme_formatHandler* handler,
	void** outInstance, const void* data, sjme_jint size,
	sjme_error* error)
{
	const void* tryDriver;
	void* instance;
	sjme_formatInstance* formatInstance;
	sjme_formatInitInstanceFunction initFunc;
	
	/* Try to detect the format using the common means. */
	if (!sjme_detectFormat(data, size, &tryDriver,
		handler->driverList, handler->driverOffsetOfDetect, error))
	{
		sjme_setError(error, SJME_ERROR_UNKNOWN_FORMAT,
			0);
		return sjme_false;
	}
	
	/* Allocate instance data. */
	instance = sjme_malloc(handler->sizeOfInstance, error);
	if (instance == NULL)
		return sjme_false;
	
	/* Setup instance parameters. */
	formatInstance = &sjme_unoffsetof(sjme_formatInstance,
		handler->instanceOffsetOfFormat, instance);
	formatInstance->driver = tryDriver;
	formatInstance->chunk.data = data;
	formatInstance->chunk.size = size;
	
	/* Get the function used for initialization. */
	initFunc = sjme_unoffsetof(sjme_formatInitInstanceFunction,
		handler->driverOffsetOfInit, tryDriver);
	
	/* Try to initialize the driver, if that fails then oops. */
	if (initFunc == NULL || !initFunc(instance, error))
	{
		/* Before we free, set this away. */
		sjme_setError(error, SJME_ERROR_BAD_DRIVER_INIT,
			initFunc == NULL);
			
		sjme_free(instance, error);
		
		return sjme_false;
	}
	
	/* Use this instance. */
	*outInstance = instance;
	return sjme_true;
}
