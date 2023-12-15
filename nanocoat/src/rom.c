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
	sjme_todo("Implement this?");
	return SJME_ERROR_UNKNOWN;
}
