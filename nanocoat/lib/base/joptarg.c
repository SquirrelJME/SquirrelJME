/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/joptarg.h"

sjme_errorCode sjme_joptarg_parse(
	sjme_attrInValue sjme_joptarg_method method,
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNotNull sjme_joptarg_handlerFunc handler,
	sjme_attrInNullable sjme_pointer handlerData,
	sjme_attrInValue sjme_jint flags,
	sjme_attrInPositive sjme_jint argc,
	sjme_attrInNotNull const sjme_lpcstr* argv)
{
	if (handler == NULL || argv == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (argc < 0 || method < 0 || method >= SJME_OPTARG_NUM_METHODS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Fallback to default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_joptarg_parseLong(
	sjme_attrInValue sjme_joptarg_method method,
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNotNull sjme_joptarg_handlerFunc handler,
	sjme_attrInNullable sjme_pointer handlerData,
	sjme_attrInValue sjme_jint flags,
	sjme_attrInNotNull sjme_lpcstr argLine)
{
	if (handler == NULL || argLine == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (method < 0 || method >= SJME_OPTARG_NUM_METHODS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Fallback to default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
