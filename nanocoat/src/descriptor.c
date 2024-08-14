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
#include "sjme/cleanup.h"

/**
 * Field type link, used for method type parsing.
 * 
 * @since 2024/02/23
 */
typedef struct sjme_desc_fieldTypeLink sjme_desc_fieldTypeLink;

struct sjme_desc_fieldTypeLink
{
	/** The previous link. */
	sjme_desc_fieldTypeLink* prev;
	
	/** The next link. */
	sjme_desc_fieldTypeLink* next;
	
	/** Field information. */
	sjme_desc_fieldType field;
};

static sjme_errorCode sjme_desc_interpretBinaryNameFixed(sjme_lpcstr inStr,
	sjme_jint inLen, sjme_lpcstr finalEnd, sjme_jint numSlash,
	sjme_desc_binaryName* result)
{
	sjme_errorCode error;
	sjme_lpcstr at, end, base, nextAt;
	sjme_jint c, identLen, identAt;
	sjme_desc_identifier* ident;
	
	if (inStr == NULL || finalEnd == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If a result is actually cared about rather than a parse check. */
	if (result != NULL)
	{
		/* Fill in basic details. */
		result->whole.pointer = inStr;
		result->whole.length = finalEnd - inStr;
		result->hash = sjme_string_hashN(result->whole.pointer,
			result->whole.length);
			
		/* Initialize list. */
		sjme_list_directInit(numSlash + 1, &result->identifiers,
			sjme_desc_identifier, 0);
	}
	
	/* Otherwise identifier is ignored. */
	else
	{
		/* Allocate. */
		ident = sjme_alloca(sizeof(*ident));
		if (ident == NULL)
			return SJME_ERROR_OUT_OF_MEMORY;
			
		/* Initialize. */
		memset(ident, 0, sizeof(*ident));
	}
	
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
		
		/* Which identifier is filled? */
		if (result != NULL)
			ident = &result->identifiers.elements[identAt];
		else
			memset(ident, 0, sizeof(*ident));
		
		/* Parse individual identifier fragment. */
		if (sjme_error_is(error = sjme_desc_interpretIdentifier(
			ident, base, identLen)))
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
	
	/* Which identifier is filled? */
	if (result != NULL)
		ident = &result->identifiers.elements[identAt];
	else
		memset(ident, 0, sizeof(*ident));
	
	/* Parse final one. */
	if (sjme_error_is(error = sjme_desc_interpretIdentifier(
		ident, base, identLen)))
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
	sjme_jint inLen, sjme_desc_fieldType* result, sjme_jboolean continueField,
	sjme_lpcstr *fieldEnd)
{
	sjme_errorCode error;
	sjme_jint typeCode, compAt, numSlash, strLen, subAt, c;
	sjme_jboolean isArray, isObject, isFinal, arrayFragment;
	sjme_desc_fieldTypeComponent* component;
	sjme_desc_fieldTypeComponent* parent;
	sjme_lpcstr strAt, strBase, finalEnd, lastAt, rootStrBase, seek;
	
	if (inStr == NULL || result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Component parsing loop. */
	isFinal = SJME_JNI_FALSE;
	strAt = inStr;
	lastAt = NULL;
	arrayFragment = SJME_JNI_FALSE;
	for (compAt = 0;; compAt++)
	{
		/* Reading into where? */
		component = &result->components[compAt];
		
		/* Decode single character which determines the type this is. */
		strBase = strAt;
		rootStrBase = strAt;
		typeCode = sjme_string_decodeChar(strAt, &strAt);
		
		/* There are more characters that are valid? */
		if (isFinal)
		{
			/* Last is at the base. */
			lastAt = strBase;
			
			/* Option to continue parsing fields? */
			if (continueField)
			{
				if (fieldEnd != NULL)
					*fieldEnd = strBase;
				break;
			}
			
			/* No more field. */
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
		
		/* Is this an array? */
		if (isArray)
		{
			/* We parsed an array, so handle actual fragment length later. */
			arrayFragment = SJME_JNI_TRUE;
			
			/* Set as array. */
			component->isArray = SJME_JNI_TRUE;
			
			/* Increase dimension count. */
			result->numDims += 1;
			
			/* Initialize just to base zero for now. */
			component->fragment.length = 0;
			
			/* Binary name is unspecified. */
			component->binaryName.pointer = NULL;
			component->binaryName.length = 0;
			
			/* Go up and increase the array size of each component. */
			for (subAt = 0; subAt <= compAt; subAt++)
				result->components[subAt].numDims += 1;
		}
		
		/* Is this an object? */
		else if (isObject)
		{
			/* Do not parse anymore, would be an error. */
			isFinal = SJME_JNI_TRUE;
			
			/* Determine the string length fragment. */
			strBase = strAt;
			for (seek = strBase; *seek != 0;)
			{
				/* Decode. */
				c = sjme_string_decodeChar(seek, &seek);
				if (c <= 0)
					return SJME_ERROR_INVALID_FIELD_TYPE;
				
				/* Stop at semicolon. */
				else if (c == ';')
					break;
			}
			
			/* The string length is more well known now. */
			strLen = (seek - strBase) - 1;
			finalEnd = seek - 1;
			
			/* Count the number of slashes. */
			numSlash = -1;
			if (sjme_error_is(error =
				sjme_desc_interpretBinaryNameNumSlash(strAt,
					strLen, &numSlash, NULL)) ||
				numSlash < 0)
				return sjme_error_defaultOr(error,
					SJME_ERROR_INVALID_FIELD_TYPE);
			
			/* Interpret binary name, result is actually ignored. */
			if (sjme_error_is(error = sjme_desc_interpretBinaryNameFixed(
				strBase, strLen, finalEnd, numSlash,
				NULL)))
				return sjme_error_defaultOr(error,
					SJME_ERROR_INVALID_FIELD_TYPE);
			
			/* Fill in binary name fragment. */
			component->binaryName.pointer = strBase;
			component->binaryName.length = strLen;
			
			/* Need ending semicolon. */
			strAt = finalEnd;
			if (';' != sjme_string_decodeChar(strAt, &strAt))
				return SJME_ERROR_INVALID_FIELD_TYPE;
			
			/* Fragment length is the base string component. */
			component->fragment.length = strAt - rootStrBase;
		}
		
		/* Primitive type. */
		else
		{
			/* Do not parse any further. */
			isFinal = SJME_JNI_TRUE;
			
			/* Fragment length is only a single character. */
			component->fragment.length = 1;
			
			/* Binary name is unspecified. */
			component->binaryName.pointer = NULL;
			component->binaryName.length = 0;
		}
	}
	
	/* Fixup array fragment length. */
	if (arrayFragment)
		for (compAt = result->numDims; compAt > 0; compAt--)
		{
			/* Get base component. */
			component = &result->components[compAt];
			parent = &result->components[compAt - 1];
			
			/* Calculate parent length, is always one higher as it contains */
			/* the current component along with the array marker. */
			parent->fragment.length = component->fragment.length + 1;
		}
		
	/* Base initialize of field. */
	result->whole.pointer = inStr;
	result->whole.length = lastAt - inStr;
	result->hash = sjme_string_hashN(result->whole.pointer,
		result->whole.length);
	
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

sjme_jint sjme_desc_compareBinaryNameP(
	sjme_attrInNullable const sjme_pointerLen* aPointerLen,
	sjme_attrInNullable const sjme_desc_binaryName* bName)
{
	sjme_jboolean aNull;
	
	/* Compare null. */
	aNull = (aPointerLen == NULL || aPointerLen->pointer == NULL);
	if (aNull || bName == NULL)
		return sjme_compare_null((aNull ? NULL : aPointerLen), bName);
	
	/* Normal compare. */
	return sjme_string_compareN(
		aPointerLen->pointer, aPointerLen->length,
		bName->whole.pointer, bName->whole.length);
}

sjme_jint sjme_desc_compareBinaryNamePS(
	sjme_attrInNullable const sjme_pointerLen* aPointerLen,
	sjme_attrInNullable sjme_lpcstr bString)
{
	sjme_jboolean aNull;
	
	/* Compare null. */
	aNull = (aPointerLen == NULL || aPointerLen->pointer == NULL);
	if (aNull || bString == NULL)
		return sjme_compare_null((aNull ? NULL : aPointerLen), bString);
	
	/* Normal compare. */
	return sjme_string_compareN(
		aPointerLen->pointer, aPointerLen->length,
		bString, strlen(bString));
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

sjme_jint sjme_desc_compareFieldC(
	sjme_attrInNullable const sjme_desc_fieldTypeComponent* aFieldComponent,
	sjme_attrInNullable const sjme_desc_fieldType* bField)
{
	/* Compare null. */
	if (aFieldComponent == NULL || bField == NULL)
		return sjme_compare_null(aFieldComponent, bField);
		
	/* Normal compare. */
	return sjme_string_compareN(
		aFieldComponent->fragment.pointer,
			aFieldComponent->fragment.length,
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
	sjme_attrOutNotNull sjme_desc_binaryName** outName,
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
	return sjme_alloc_copyWeak(inPool, resultLen,
		sjme_nvm_enqueueHandler, SJME_NVM_ENQUEUE_IDENTITY,
		(void**)outName, (void*)result, NULL);
}

sjme_errorCode sjme_desc_interpretClassName(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_desc_className** outName,
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
			inStr, strLen, &result->descriptor.field,
			SJME_JNI_FALSE, NULL)))
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
	return sjme_alloc_copyWeak(inPool, allocLen,
		sjme_nvm_enqueueHandler, SJME_NVM_ENQUEUE_IDENTITY,
		(void**)outName, (void*)result, NULL);
}

sjme_errorCode sjme_desc_interpretFieldType(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_desc_fieldType** outType,
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
		inStr, inLen, result, SJME_JNI_FALSE, NULL)))
		return sjme_error_defaultOr(error,
			SJME_ERROR_INVALID_FIELD_TYPE);
	
	/* Return copy of it. */
	return sjme_alloc_copyWeak(inPool, allocLen,
		sjme_nvm_enqueueHandler, SJME_NVM_ENQUEUE_IDENTITY,
		(void**)outType, (void*)result, NULL);
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
	outIdent->whole.pointer = (sjme_pointer)inStr;
	outIdent->whole.length = end - inStr;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_desc_interpretMethodType(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_desc_methodType** outType,
	sjme_attrInNotNull sjme_lpcstr inStr,
	sjme_attrInPositive sjme_jint inLen)
{
	sjme_errorCode error;
	sjme_jint c, fragmentLen, allocLen, fieldCount, fieldAt;
	sjme_lpcstr strAt, strBase;
	sjme_desc_fieldTypeLink* firstField;
	sjme_desc_fieldTypeLink* lastField;
	sjme_desc_fieldTypeLink* currentField;
	sjme_jboolean isReturn, stopNow;
	sjme_desc_methodType* result;
	sjme_desc_fieldTypeComponent* target;
	sjme_desc_fieldType* sourceField;
	sjme_desc_fieldTypeComponent* source;
	
	if (inPool == NULL || outType == NULL || inStr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inLen < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	else if (inLen == 0)
		return SJME_ERROR_INVALID_METHOD_TYPE;
	
	/* First character should be opening parenthesis. */
	strAt = inStr;
	c = sjme_string_decodeChar(strAt, &strAt);
	if (c != '(')
		return SJME_ERROR_INVALID_METHOD_TYPE;
	
	/* Parse each argument. */
	firstField = NULL;
	lastField = NULL;
	currentField = NULL;
	isReturn = SJME_JNI_FALSE;
	stopNow = SJME_JNI_FALSE;
	fieldCount = 0;
	for (;;)
	{
		/* Read in type. */
		strBase = strAt;
		c = sjme_string_decodeChar(strAt, &strAt);
		if ((!stopNow && c < 0) || (stopNow && c >= 0))
			return SJME_ERROR_INVALID_METHOD_TYPE;
		
		/* Stop processing? */
		if (stopNow)
			break;
		
		/* End of argument list? */
		if (c == ')')
		{
			/* Handle return value now. */
			isReturn = SJME_JNI_TRUE;
			
			/* Run loop again. */
			continue;
		}
			
		/* Maximum end of string. */
		fragmentLen = inLen - (strBase - inStr);
		
		/* Determine the allocation size. */
		allocLen = -1;
		if (sjme_error_is(error = sjme_desc_interpretFieldTypeAllocSize(
			strBase, fragmentLen, &allocLen)) ||
			allocLen < 0)
			return sjme_error_defaultOr(error,
				SJME_ERROR_INVALID_METHOD_TYPE);
		
		/* Allocate. */
		currentField = sjme_alloca(sizeof(*currentField) + allocLen);
		if (currentField == NULL)
			return SJME_ERROR_OUT_OF_MEMORY;
		
		/* Initialize. */
		memset(currentField, 0, allocLen);
		
		/* First link? */
		if (firstField == NULL)
		{
			firstField = currentField;
			lastField = currentField;
		}
		
		/* Link in otherwise. */
		else
		{
			lastField->next = currentField;
			currentField->prev = lastField;
			lastField = currentField;
		}
		
		/* Parse single field */
		if (sjme_error_is(error = sjme_desc_interpretFieldTypeFixed(
			strBase, fragmentLen, &currentField->field,
			SJME_JNI_TRUE, &strAt)))
			return sjme_error_defaultOr(error,
				SJME_ERROR_INVALID_METHOD_TYPE);
		
		/* Was a parsed field, so count up. */
		fieldCount++;
		
		/* If this was the return value, stop. */
		if (isReturn)
			stopNow = SJME_JNI_TRUE;
	}
	
	/* If there was never a return type, fail. */
	if (!isReturn)
		return SJME_ERROR_INVALID_METHOD_TYPE;
	
	/* Allocate result. */
	allocLen = SJME_SIZEOF_DESC_METHOD_TYPE(fieldCount);
	result = sjme_alloca(allocLen);
	if (result == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Initialize. */
	memset(result, 0, allocLen);
	
	/* Initialize field list. */
	if (sjme_error_is(error = sjme_list_directInit(fieldCount,
		&result->fields, sjme_desc_fieldTypeComponent, 0)))
		return sjme_error_default(error);
	
	/* Fill in all the various details. */
	result->whole.pointer = inStr;
	result->whole.length = strAt - inStr;
	result->hash = sjme_string_hashN(result->whole.pointer,
		result->whole.length);
	
	/* Set initial cell base. */
	result->returnCells = 0;
	result->argCells = 0;
	
	/* Go through and process each field. */
	currentField = firstField;
	for (fieldAt = 0; fieldAt < fieldCount;
		fieldAt++, currentField = currentField->next)
	{
		/* Source field to read from. */
		sourceField = &currentField->field;
		source = &sourceField->components[0];
		
		/* Which field is being modified? */
		isReturn = (fieldAt == fieldCount - 1);
		target = &result->fields.elements[(isReturn ? 0 : fieldAt + 1)];
		
		/* Everything can be copied over quickly. */
		memmove(target, source,
			sizeof(sjme_desc_fieldTypeComponent));
		
		/* Increase base cells. */
		if (isReturn)
			result->returnCells = source->cells;
		else
			result->argCells += source->cells;
	}
	
	/* Success! */
	return sjme_alloc_copyWeak(inPool, allocLen,
		sjme_nvm_enqueueHandler, SJME_NVM_ENQUEUE_IDENTITY,
		(void**)outType, (void*)result, NULL);
}
