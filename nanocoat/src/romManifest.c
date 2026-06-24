/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/romManifest.h"

sjme_errorCode sjme_nvm_rom_manifestParseNext(
	sjme_attrInNotNull sjme_stream_input inputStream,
	sjme_attrInOutNotNull sjme_nvm_rom_manifestStep* inOutStep)
{
	if (inOutStep == NULL || inputStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}