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

static sjme_errorCode sjme_desc_interpretBinaryNameFixed(sjme_lpcstr inStr,
	sjme_jint inLen, sjme_lpcstr finalEnd, sjme_jint numSlash,
	sjme_desc_binaryName* result)
{
	sjme_errorCode error;
	sjme_lpcstr at, end, base, nextAt;
	sjme_jint c, identLen, identAt;
	
	if (inStr == NULL || finalEnd == NULL || result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Fill in basic details. */
	result->whole.pointer = inStr;
	result->whole.length = finalEnd - inStr;
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
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_desc_interpretBinaryNameNumSlash(sjme_lpcstr inStr,
	sjme_jint inLen, sjme_jint* outNumSlash, sjme_lpcstr* finalEnd)
{
	sjme_jint c, numSlash;
	sjme_lpcstr at, end;
	
	if (inStr == NULL || outNumSlash == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
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
	if (finalEnd != NULL)
		*finalEnd = end;
	
	/* Success! */
	*outNumSlash = numSlash;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_desc_interpretFieldTypeAllocSize(sjme_lpcstr inStr,
	sjme_jint inLen, sjme_jint* outAllocLen)
{
	sjme_errorCode error;
	sjme_jint typeCode, allocLen, subLen;
	sjme_lpcstr next;
	
	if (inStr == NULL || outAllocLen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Decode the type code. */
	next = inStr;
	typeCode = sjme_string_decodeChar(next, &next);
	if (typeCode <= 0)
		return SJME_ERROR_INVALID_FIELD_TYPE;
	
	/* Base size with the single base component. */
	allocLen = sizeof(sjme_desc_fieldType) +
		sizeof(sjme_desc_fieldTypeComponent);
	
	/* If an array, count the number of dimensions... */
	while (typeCode == '[')
	{
		/* Add component. */
		allocLen += sizeof(sjme_desc_fieldTypeComponent);
		
		/* Read in next code. */
		typeCode = sjme_string_decodeChar(next, &next);
		if (typeCode <= 0)
			return SJME_ERROR_INVALID_FIELD_TYPE;
		
		/* Stop if not an array. */
		if (typeCode != '[')
			break;
	}
	
	/* Binary name? */
	if (typeCode == 'L')
	{
		/* Determine the number of identifiers used. */
		subLen = -1;
		if (sjme_error_is(error = sjme_desc_interpretBinaryNameNumSlash(
			next, inLen - (next - inStr) - 1, &subLen,
			NULL)) || subLen < 0)
			return sjme_error_defaultOr(error,
				SJME_ERROR_INVALID_FIELD_TYPE);
		
		/* Add in. */
		allocLen += SJME_SIZEOF_DESC_BINARY_NAME(subLen + 1) +
			sizeof(sjme_desc_fieldTypeComponent);
	} 
	
	/* Success! */
	*outAllocLen = allocLen;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_desc_interpretFieldTypeFixed(sjme_lpcstr inStr,
	sjme_jint inLen, sjme_desc_fieldType* result)
{
	sjme_errorCode error;
	sjme_jint typeCode, compAt, numSlash, strLen;
	sjme_jboolean isArray, isObject, isFinal;
	sjme_desc_fieldTypeComponent* component;
	sjme_lpcstr strAt, strBase, finalEnd;
	
	if (inStr == NULL || result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Base common initialize for the whole value. */
	result->whole.pointer = inStr;
	result->whole.length = inLen;
	result->hash = sjme_string_hashN(result->whole.pointer,
		result->whole.length);
		
	/* Component parsing loop. */
	isFinal = SJME_JNI_FALSE;
	strAt = inStr;
	for (compAt = 0;; compAt++)
	{
		/* Reading into where? */
		component = &result->components[compAt];
		
		/* Decode single character which determines the type this is. */
		strBase = strAt;
		typeCode = sjme_string_decodeChar(strAt, &strAt);
		
		/* There are more characters that are valid? */
		if (isFinal)
		{
			if (typeCode >= 0)
				return SJME_ERROR_INVALID_FIELD_TYPE;
			break;
		}
		
		/* Handle based on the type code. */
		isArray = SJME_JNI_FALSE;
		isObject = SJME_JNI_FALSE;
		switch (typeCode)
		{
				/* Single type codes. */
			case 'V':
				component->javaType = SJME_JAVA_TYPE_ID_VOID;
				break;
				
			case 'Z':
			case 'B':
				component->javaType = SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE;
				break;
				
			case 'S':
			case 'C':
				component->javaType = SJME_JAVA_TYPE_ID_SHORT_OR_CHAR;
				break;
				
			case 'I':
				component->javaType = SJME_JAVA_TYPE_ID_INTEGER;
				break;
				
			case 'J':
				component->javaType = SJME_JAVA_TYPE_ID_LONG;
				break;
				
			case 'F':
				component->javaType = SJME_JAVA_TYPE_ID_FLOAT;
				break;
				
			case 'D':
				component->javaType = SJME_JAVA_TYPE_ID_DOUBLE;
				break;
			
				/* Longer type codes. */
			case 'L':
				isObject = SJME_JNI_TRUE;
			case '[':
				isArray = !isObject;
				component->javaType = SJME_JAVA_TYPE_ID_OBJECT;
				break;
			
			default:
				return SJME_ERROR_INVALID_FIELD_TYPE;
		}
		
		/* Determine cell size. */
		if (component->javaType == SJME_JAVA_TYPE_ID_VOID)
			component->cells = 0;
		else if (component->javaType == SJME_JAVA_TYPE_ID_LONG ||
			component->javaType == SJME_JAVA_TYPE_ID_DOUBLE)
			component->cells = 2;
		else
			component->cells = 1;
		
		/* The fragment is the remaining string. */
		component->fragment.pointer = strBase;
		component->fragment.length = inLen - (strBase - inStr);
		
		/* Is this an array? */
		if (isArray)
		{
			/* Set as array. */
			component->isArray = SJME_JNI_TRUE;
			
			/* Increase dimension count. */
			result->numDims += 1;
		}
		
		/* Is this an object? */
		else if (isObject)
		{
			/* Do not parse anymore, would be an error. */
			isFinal = SJME_JNI_TRUE;
			
			/* Determine the string length fragment. */
			strBase = strAt;
			strLen = inLen - (strBase - inStr) - 1;
			
			/* Count the number of slashes. */
			finalEnd = NULL;
			numSlash = -1;
			if (sjme_error_is(error =
				sjme_desc_interpretBinaryNameNumSlash(strAt,
					strLen, &numSlash, &finalEnd)) ||
				numSlash < 0)
				return sjme_error_defaultOr(error,
					SJME_ERROR_INVALID_FIELD_TYPE);
			
			/* Interpret binary name. */
			if (sjme_error_is(error = sjme_desc_interpretBinaryNameFixed(
				strBase, strLen, finalEnd, numSlash,
				&component->objectType[0])))
				return sjme_error_defaultOr(error,
					SJME_ERROR_INVALID_FIELD_TYPE);
			
			/* Need ending semicolon. */
			strAt = finalEnd;
			if (';' != sjme_string_decodeChar(strAt, &strAt))
				return SJME_ERROR_INVALID_FIELD_TYPE;
		}
		
		/* Primitive type. */
		else
		{
			/* Do not parse any further. */
			isFinal = SJME_JNI_TRUE;
		}
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_jint sjme_desc_compareBinaryName(
	sjme_attrInNullable const sjme_desc_binaryName* aName,
	sjme_attrInNullable const sjme_desc_binaryName* bName)
{
	/* Compare null. */
	if (aName == NULL || bName == NULL)
		return sjme_compare_null(aName, bName);
	
	/* Normal compare. */
	return sjme_string_compareN(
		aName->whole.pointer, aName->whole.length,
		bName->whole.pointer, bName->whole.length);
}

sjme_jint sjme_desc_compareBinaryNameS(
	sjme_attrInNullable const sjme_desc_binaryName* aName,
	sjme_attrInNullable sjme_lpcstr bString)
{
	sjme_jint strLen;
	
	/* Compare null. */
	if (aName == NULL || bString == NULL)
		return sjme_compare_null(aName, bString);
	
	/* Wrong string length? */
	strLen = strlen(bString);
	if (strLen != aName->whole.length)
		return SJME_JNI_FALSE;
	
	/* Compare actual values. */
	return strncmp(aName->whole.pointer, bString, strLen);
}

sjme_jint sjme_desc_compareClass(
	sjme_attrInNullable const sjme_desc_className* aClass,
	sjme_attrInNullable const sjme_desc_className* bClass)
{
	/* Compare null. */
	if (aClass == NULL || bClass == NULL)
		return sjme_compare_null(aClass, bClass);
		
	/* Normal compare. */
	return sjme_string_compareN(
		aClass->whole.pointer, aClass->whole.length,
		bClass->whole.pointer, bClass->whole.length);
}

sjme_jint sjme_desc_compareClassS(
	sjme_attrInNullable const sjme_desc_className* aClass,
	sjme_attrInNullable sjme_lpcstr bString)
{
	/* Compare null. */
	if (aClass == NULL || bString == NULL)
		return sjme_compare_null(aClass, bString);
	
	/* Normal compare. */
	return sjme_string_compareN(
		aClass->whole.pointer, aClass->whole.length,
		bString, strlen(bString));
}

sjme_jint sjme_desc_compareField(
	sjme_attrInNullable const sjme_desc_fieldType* aField,
	sjme_attrInNullable const sjme_desc_fieldType* bField)
{
	/* Compare null. */
	if (aField == NULL || bField == NULL)
		return sjme_compare_null(aField, bField);
		
	/* Normal compare. */
	return sjme_string_compareN(
		aField->whole.pointer, aField->whole.length,
		bField->whole.pointer, bField->whole.length);
}

sjme_jint sjme_desc_compareFieldS(
	sjme_attrInNullable const sjme_desc_fieldType* aField,
	sjme_attrInNullable sjme_lpcstr bString)
{
	/* Compare null. */
	if (aField == NULL || bString == NULL)
		return sjme_compare_null(aField, bString);
	
	/* Normal compare. */
	return sjme_string_compareN(
		aField->whole.pointer, aField->whole.length,
		bString, strlen(bString));
}

sjme_jint sjme_desc_compareIdentifier(
	sjme_attrInNullable const sjme_desc_identifier* aIdent,
	sjme_attrInNullable const sjme_desc_identifier* bIdent)
{
	/* Compare null. */
	if (aIdent == NULL || bIdent == NULL)
		return sjme_compare_null(aIdent, bIdent);
		
	/* Normal compare. */
	return sjme_string_compareN(
		aIdent->whole.pointer, aIdent->whole.length,
		bIdent->whole.pointer, bIdent->whole.length);
}

sjme_jint sjme_desc_compareIdentifierS(
	sjme_attrInNullable const sjme_desc_identifier* aIdent,
	sjme_attrInNullable sjme_lpcstr bString)
{
	sjme_jint strLen;
	
	/* Compare null. */
	if (aIdent == NULL || bString == NULL)
		return sjme_compare_null(aIdent, bString);
	
	/* Compare by string. */
	return sjme_string_compareN(
		aIdent->whole.pointer, aIdent->whole.length,
		bString, strlen(bString));
}

sjme_jint sjme_desc_compareMethod(
	sjme_attrInNullable const sjme_desc_methodType* aMethod,
	sjme_attrInNullable const sjme_desc_methodType* bMethod)
{
	/* Compare null. */
	if (aMethod == NULL || bMethod == NULL)
		return sjme_compare_null(aMethod, bMethod);
		
	/* Normal compare. */
	return sjme_string_compareN(
		aMethod->whole.pointer, aMethod->whole.length,
		bMethod->whole.pointer, bMethod->whole.length);
}

sjme_jint sjme_desc_compareMethodS(
	sjme_attrInNullable const sjme_desc_methodType* aMethod,
	sjme_attrInNullable sjme_lpcstr bString)
{
	/* Compare null. */
	if (aMethod == NULL || bString == NULL)
		return sjme_compare_null(aMethod, bString);
	
	/* Normal compare. */
	return sjme_string_compareN(
		aMethod->whole.pointer, aMethod->whole.length,
		bString, strlen(bString));
}

sjme_errorCode sjme_desc_interpretBinaryName(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull const sjme_desc_binaryName** outName,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen)
{
	sjme_errorCode error;
	sjme_lpcstr finalEnd;
	sjme_desc_binaryName* result;
	sjme_jint resultLen, numSlash;
	
	if (inPool == NULL || outName == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	else if (inLen == 0)
		return SJME_ERROR_INVALID_BINARY_NAME;
	
	/* Count the number of slashes. */
	finalEnd = NULL;
	numSlash = 0;
	if (sjme_error_is(error = sjme_desc_interpretBinaryNameNumSlash(inStr,
		inLen, &numSlash, &finalEnd)))
		return sjme_error_default(error);
	
	/* Allocate. */
	resultLen = SJME_SIZEOF_DESC_BINARY_NAME(numSlash + 1);
	result = sjme_alloca(resultLen);
	
	/* Check that it is valid. */
	if (result == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
		
	/* Initialize. */
	memset(result, 0, resultLen);
	
	/* Parse fixed binary name. */
	if (sjme_error_is(error = sjme_desc_interpretBinaryNameFixed(
		inStr, inLen, finalEnd, numSlash, result)))
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
	sjme_errorCode error;
	sjme_jint c, numSlash, allocLen, strLen;
	sjme_desc_className* result;
	sjme_lpcstr finalEnd;
	
	if (inPool == NULL || outName == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	else if (inLen == 0)
		return SJME_ERROR_INVALID_CLASS_NAME;
	
	/* Get actual string length. */
	strLen = sjme_string_lengthN(inStr, inLen);
	
	/* Is this an array? Interpret as field. */
	c = sjme_string_decodeChar(inStr, NULL);
	if (c == '[')
	{
		/* Determine field size. */
		allocLen = -1;
		if (sjme_error_is(error = sjme_desc_interpretFieldTypeAllocSize(
			inStr, strLen, &allocLen)) || allocLen < 0)
			return sjme_error_defaultOr(error,
				SJME_ERROR_INVALID_CLASS_NAME);
	}
	
	/* Otherwise, interpret as binary name. */
	else
	{
		/* Determine slashes for resultant size. */
		numSlash = -1;
		finalEnd = NULL;
		if (sjme_error_is(error = sjme_desc_interpretBinaryNameNumSlash(
			inStr, strLen, &numSlash, &finalEnd)) ||
			numSlash < 0)
			return sjme_error_defaultOr(error,
				SJME_ERROR_INVALID_CLASS_NAME);
		
		/* Allocate this much. */
		allocLen = SJME_SIZEOF_DESC_BINARY_NAME(numSlash + 1);
	}
	
	/* Add class base. */
	allocLen += sizeof(sjme_desc_className);
	
	/* Allocate. */
	result = sjme_alloca(allocLen);
	if (result == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Initialize. */
	memset(result, 0, allocLen);
	
	/* Fill in basic details. */
	result->whole.pointer = inStr;
	result->whole.length = strLen;
	result->hash = sjme_string_hashN(result->whole.pointer,
		result->whole.length);
		
	/* Parse array. */
	if (c == '[')
	{
		/* Parse. */
		if (sjme_error_is(error = sjme_desc_interpretFieldTypeFixed(
			inStr, strLen, &result->descriptor.field)))
			return sjme_error_defaultOr(error,
				SJME_ERROR_INVALID_CLASS_NAME);
		
		/* Is a field. */
		result->isField = SJME_JNI_TRUE;
	}
	
	/* Parse binary name. */
	else
	{
		/* Parse. */
		if (sjme_error_is(error = sjme_desc_interpretBinaryNameFixed(
			inStr, strLen, finalEnd, numSlash,
			&result->descriptor.binary)))
			return sjme_error_defaultOr(error,
				SJME_ERROR_INVALID_CLASS_NAME);
	}
	
	/* Return a copy. */
	return sjme_alloc_copy(inPool, allocLen, outName,
		result);
}

sjme_errorCode sjme_desc_interpretFieldType(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull const sjme_desc_fieldType** outType,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen)
{
	sjme_errorCode error;
	sjme_jint strLen, typeCode, allocLen;
	sjme_desc_fieldType* result;
	
	if (inPool == NULL || outType == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	else if (inLen == 0)
		return SJME_ERROR_INVALID_FIELD_TYPE;
	
	/* Need the length of string to determine what possible type this is. */
	strLen = sjme_string_lengthN(inStr, inLen);
	if (strLen <= 0)
		return SJME_ERROR_INVALID_FIELD_TYPE;

	/* How many bytes are needed for allocation? */
	allocLen = -1;
	if (sjme_error_is(error = sjme_desc_interpretFieldTypeAllocSize(inStr,
		inLen, &allocLen)) || allocLen <= 0)
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_FIELD_TYPE);
	
	/* Allocate result. */
	result = sjme_alloca(allocLen);
	if (result == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Initialize. */
	memset(result, 0, allocLen);
	
	/* Load field, recursively. */
	if (sjme_error_is(error = sjme_desc_interpretFieldTypeFixed(
		inStr, inLen, result)))
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_FIELD_TYPE);
	
	/* Return copy of it. */
	return sjme_alloc_copy(inPool, allocLen, outType,
		result);
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
	outIdent->whole.pointer = inStr;
	outIdent->whole.length = end - inStr;
	
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
	
	if (inLen < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	else if (inLen == 0)
		return SJME_ERROR_INVALID_METHOD_TYPE;
		
	sjme_todo("Implement this?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
