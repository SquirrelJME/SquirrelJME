/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/listUtil.h"

const sjme_listUtil_buildFunctions sjme_listUtil_buildAToI =
{
};

const sjme_listUtil_buildFunctions sjme_listUtil_buildStrings =
{
};

sjme_errorCode sjme_listUtil_build(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_list_void** outList,
	sjme_attrInNegativeOnePositive sjme_jint limit,
	sjme_attrInNotNull const sjme_listUtil_buildFunctions* functions,
	sjme_attrInNotNull sjme_pointer source,
	sjme_attrInNullable sjme_intPointer* sourceParam)
{
	if (inPool == NULL || outList == NULL || functions == NULL ||
		source == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (limit < -1)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
