/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/nvm/mle.h"

static const sjme_nvm_mle sjme_nvm_mleShelves[] =
{
	{NULL, NULL}
};

sjme_errorCode sjme_mle_mleCall(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_lpcstr className,
	sjme_attrInNotNull sjme_lpcstr methodName,
	sjme_attrInNotNull sjme_lpcstr methodType,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	if (inFrame == NULL || className == NULL || methodName == NULL ||
		methodType == NULL || argR == NULL || (argC > 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (argC < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Impl? %s:%s%s", className, methodName, methodType);
	return sjme_error_notImplemented(0);
}
