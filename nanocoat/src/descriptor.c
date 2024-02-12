/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/descriptor.h"
#include "sjme/debug.h"

sjme_jboolean sjme_desc_classMatch(
	sjme_attrInNotNull const sjme_desc_className* inClass,
	sjme_attrInValue sjme_jboolean isFieldType, 
	sjme_attrInNotNull sjme_lpcstr string)
{
	sjme_todo("Implement this?");
	return SJME_JNI_FALSE;
}

sjme_jboolean sjme_desc_identifierMatch(
	sjme_attrInNotNull const sjme_desc_identifier* inIdentifier,
	sjme_attrInNotNull sjme_lpcstr string)
{
	sjme_jint strLen;
	
	/* Are these the same NULL? */
	if (inIdentifier == NULL || string == NULL)
		return (inIdentifier == NULL) == (string == NULL);
	
	/* Wrong string length? */
	strLen = strlen(string);
	if (strLen != inIdentifier->pointer.length)
		return SJME_JNI_FALSE;
	
	/* Compare actual values. */
	return ((0 == strncmp(inIdentifier->pointer.pointer,
		string, strLen)) ? SJME_JNI_TRUE : SJME_JNI_FALSE);
}

sjme_errorCode sjme_desc_interpretBinaryName(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_desc_binaryName** outName,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen)
{
	if (inPool == NULL || outName == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_desc_interpretClassName(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_desc_className** outName,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen)
{
	if (inPool == NULL || outName == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_desc_interpretFieldType(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_desc_fieldType** outType,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen)
{
	if (inPool == NULL || outType == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_desc_interpretIdentifier(
	sjme_attrOutNotNull sjme_desc_identifier* outIdent,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen)
{
	if (outIdent == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_desc_interpretMethodType(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_desc_methodType** outType,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen)
{
	if (inPool == NULL || outType == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
