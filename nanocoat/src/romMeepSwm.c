/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/romMeepSwm.h"

sjme_errorCode sjme_nvm_rom_swmLoad(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrOutNotNull sjme_nvm_rom_swmManager* outSwmManager)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_rom_swmResolve(
	sjme_attrInNotNull sjme_nvm_rom_swmManager swmManager,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_lpstr* outMainClass,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outMainArgs,
	sjme_attrOutNotNull sjme_list(sjme_jint)** outById,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outByName)
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
