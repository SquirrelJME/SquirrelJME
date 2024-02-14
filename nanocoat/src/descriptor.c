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
#include "sjme/util.h"

sjme_jint sjme_desc_compareBinaryName(
	sjme_attrInNullable const sjme_desc_binaryName* aName,
	sjme_attrInNullable const sjme_desc_binaryName* bName)
{
	/* Compare null. */
	if (aName == NULL || bName == NULL)
		return sjme_compare_null(aName, bName);
		
	sjme_todo("Implement this?");
	return -999;
}

sjme_jint sjme_desc_compareBinaryNameS(
	sjme_attrInNullable const sjme_desc_binaryName* aName,
	sjme_attrInNullable sjme_lpcstr bString)
{
	/* Compare null. */
	if (aName == NULL || bString == NULL)
		return sjme_compare_null(aName, bString);
	
	sjme_todo("Implement this?");
	return -999;
}

sjme_jint sjme_desc_compareClass(
	sjme_attrInNullable const sjme_desc_className* aClass,
	sjme_attrInNullable const sjme_desc_className* bClass)
{
	/* Compare null. */
	if (aClass == NULL || bClass == NULL)
		return sjme_compare_null(aClass, bClass);
		
	sjme_todo("Implement this?");
	return -999;
}

sjme_jint sjme_desc_compareClassS(
	sjme_attrInNullable const sjme_desc_className* aClass,
	sjme_attrInValue sjme_jboolean bIsFieldType, 
	sjme_attrInNullable sjme_lpcstr bString)
{
	/* Compare null. */
	if (aClass == NULL || bString == NULL)
		return sjme_compare_null(aClass, bString);
	
	sjme_todo("Implement this?");
	return -999;
}

sjme_jint sjme_desc_compareField(
	sjme_attrInNullable const sjme_desc_fieldType* aField,
	sjme_attrInNullable const sjme_desc_fieldType* bField)
{
	/* Compare null. */
	if (aField == NULL || bField == NULL)
		return sjme_compare_null(aField, bField);
		
	sjme_todo("Implement this?");
	return -999;
}

sjme_jint sjme_desc_compareFieldS(
	sjme_attrInNullable const sjme_desc_fieldType* aField,
	sjme_attrInNullable sjme_lpcstr bString)
{
	/* Compare null. */
	if (aField == NULL || bString == NULL)
		return sjme_compare_null(aField, bString);
	
	sjme_todo("Implement this?");
	return -999;
}

sjme_jint sjme_desc_compareIdentifier(
	sjme_attrInNullable const sjme_desc_identifier* aIdent,
	sjme_attrInNullable const sjme_desc_identifier* bIdent)
{
	/* Compare null. */
	if (aIdent == NULL || bIdent == NULL)
		return sjme_compare_null(aIdent, bIdent);
		
	sjme_todo("Implement this?");
	return -999;
}

sjme_jint sjme_desc_compareIdentifierS(
	sjme_attrInNullable const sjme_desc_identifier* aIdent,
	sjme_attrInNullable sjme_lpcstr bString)
{
	sjme_jint strLen;
	
	/* Compare null. */
	if (aIdent == NULL || bString == NULL)
		return sjme_compare_null(aIdent, bString);
	
	/* Wrong string length? */
	strLen = strlen(bString);
	if (strLen != aIdent->pointer.length)
		return SJME_JNI_FALSE;
	
	/* Compare actual values. */
	return strncmp(aIdent->pointer.pointer, bString, strLen);
}

sjme_jint sjme_desc_compareMethod(
	sjme_attrInNullable const sjme_desc_methodType* aMethod,
	sjme_attrInNullable const sjme_desc_methodType* bMethod)
{
	/* Compare null. */
	if (aMethod == NULL || bMethod == NULL)
		return sjme_compare_null(aMethod, bMethod);
		
	sjme_todo("Implement this?");
	return -999;
}

sjme_jint sjme_desc_compareMethodS(
	sjme_attrInNullable const sjme_desc_methodType* aMethod,
	sjme_attrInNullable sjme_lpcstr bString)
{
	/* Compare null. */
	if (aMethod == NULL || bString == NULL)
		return sjme_compare_null(aMethod, bString);
	
	sjme_todo("Implement this?");
	return -999;
}

sjme_errorCode sjme_desc_interpretBinaryName(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull const sjme_desc_binaryName** outName,
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
	sjme_attrOutNotNull const sjme_desc_className** outName,
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
	sjme_attrOutNotNull const sjme_desc_fieldType** outType,
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
	sjme_attrOutNotNull const sjme_desc_methodType** outType,
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
