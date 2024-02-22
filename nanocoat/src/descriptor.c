/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/descriptor.h"
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
	sjme_errorCode error;
	sjme_lpcstr at, end, base, finalEnd, nextAt;
	sjme_desc_binaryName* result;
	sjme_jint resultLen, c, identLen, identAt, numSlash;
	
	if (inPool == NULL || outName == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	else if (inLen == 0)
		return SJME_ERROR_INVALID_BINARY_NAME;
	
	/* Count the number of slashes. */
	numSlash = 0;
	for (at = inStr, end = at + inLen; at < end;)
	{
		/* Decode character. */
		c = sjme_string_decodeChar(at, &at);
		if (c < 0)
			return SJME_ERROR_INVALID_BINARY_NAME;
		
		/* If a slash it will then get split. */
		if (c == '/')
			numSlash++;
	}
	
	/* Store the end for faster final decode. */
	finalEnd = end;
	
	/* Allocate. */
	resultLen = SJME_SIZEOF_DESC_BINARY_NAME(numSlash + 1);
	result = sjme_alloca(resultLen);
	
	/* Check that it is valid. */
	if (result == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
		
	/* Initialize. */
	memset(result, 0, resultLen);
	
	/* Fill in basic details. */
	result->whole.pointer = inStr;
	result->whole.length = end - inStr;
	result->hash = sjme_string_hashN(result->whole.pointer,
		result->whole.length);
	
	/* Initialize list. */
	sjme_list_directInit(numSlash + 1, &result->identifiers,
		sjme_desc_identifier, 0);
	
	/* Parse individual identifiers, if there are none then this */
	/* will be skipped completely. */
	identAt = 0;
	nextAt = NULL;
	for (at = inStr, base = at, end = at + inLen;
		at < end && identAt < numSlash; at = nextAt)
	{
		/* Decode character. */
		c = sjme_string_decodeChar(at, &nextAt);
		if (c < 0)
			return SJME_ERROR_INVALID_BINARY_NAME;
			
		/* Only split on slashes. */
		if (c != '/')
			continue;
		
		/* If a slash it will then get split and an identifier parsed. */
		identLen = at - base;
		if (identLen <= 0)
			return SJME_ERROR_INVALID_BINARY_NAME;
		
		/* Parse individual identifier fragment. */
		if (sjme_error_is(error = sjme_desc_interpretIdentifier(
			&result->identifiers.elements[identAt],
			base, identLen)))
			return sjme_error_defaultOr(error,
				SJME_ERROR_INVALID_BINARY_NAME);
		
		/* Move identifier and next base up. */
		base = at + 1;
		identAt++;
	}
	
	/* The end fragment is not valid if empty. */
	identLen = finalEnd - base;
	if (identLen <= 0)
		return SJME_ERROR_INVALID_BINARY_NAME;
	
	/* Parse final one. */
	if (sjme_error_is(error = sjme_desc_interpretIdentifier(
		&result->identifiers.elements[identAt],
		base, identLen)))
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_BINARY_NAME);
	
	/* Return a copy. */
	return sjme_alloc_copy(inPool, resultLen, outName,
		result);
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
	sjme_lpcstr at, end;
	sjme_jint c;
	
	if (outIdent == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	else if (inLen == 0)
		return SJME_ERROR_INVALID_IDENTIFIER;
		
	/* Check input for invalid characters. */
	for (at = inStr, end = at + inLen; at < end;)
	{
		/* Decode character. */
		c = sjme_string_decodeChar(at, &at);
		if (c < 0)
			return SJME_ERROR_INVALID_IDENTIFIER;
		
		/* Is the character not valid? */
		if (c == '.' || c == ';' || c == '[' || c == '/')
			return SJME_ERROR_INVALID_IDENTIFIER;
	}
	
	/* Cannot be blank. */
	if (end == inStr)
		return SJME_ERROR_INVALID_IDENTIFIER;
	
	/* Fill in info. */
	memset(outIdent, 0, sizeof(*outIdent));
	outIdent->hash = sjme_string_hashN(inStr, inLen);
	outIdent->pointer.pointer = inStr;
	outIdent->pointer.length = end - inStr;
	
	/* Success! */
	return SJME_ERROR_NONE;
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
