/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/rom.h"
#include "sjme/debug.h"
#include "sjme/romInternal.h"
#include "sjme/payload.h"

sjme_errorCode sjme_rom_combineSuites(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite** outSuite,
	sjme_attrInNotNull sjme_rom_suite** inSuites,
	sjme_attrInPositive sjme_jint numInSuites)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_UNKNOWN;
}

sjme_errorCode sjme_rom_makeVirtualSuite(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite** outSuite,
	sjme_attrInNotNull const sjme_rom_suiteFunctions* inFunctions)
{
	sjme_todo("Implement this?");
	return SJME_ERROR_UNKNOWN;
}

sjme_errorCode sjme_rom_scanPayload(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite** outSuite,
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
