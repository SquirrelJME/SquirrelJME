/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/bytecode.h"
#include "sjme/debug.h"

sjme_errorCode sjme_nvm_byteCode_illegalInstruction(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode)
{
	if (inFrame == NULL || relRawCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Illegal? %d", relRawCode[0]);
	return sjme_error_notImplemented(relRawCode[0]);
}

sjme_errorCode sjme_nvm_byteCode_notImplemented(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode)
{
	if (inFrame == NULL || relRawCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl? %d", relRawCode[0]);
	return sjme_error_notImplemented(relRawCode[0]);
}
