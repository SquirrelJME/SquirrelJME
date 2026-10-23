/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/task.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/classy.h"
#include "sjme/debug.h"
#include "sjme/util.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/cleanup.h"

sjme_errorCode sjme_nvm_class_calcMethodArgs(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_charSeq typeDesc,
	sjme_attrInNotNull sjme_jint* outArgC,
	sjme_attrInNotNull sjme_javaTypeId** outArgT,
	sjme_attrInNotNull sjme_javaTypeId* outArgR)
{
#define SJME_MAX_ARGS 65
	sjme_errorCode error;
	sjme_javaTypeId args[SJME_MAX_ARGS];
	sjme_javaTypeId* result;
	sjme_cchar d;
	sjme_jint argAt, i, n;
	sjme_jboolean arrayScope, returnScope, returnDid;
	sjme_charSeq_it it;
	
	if (allocPool == NULL || typeDesc == NULL ||
		outArgC == NULL || outArgT == NULL || outArgR == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Quick void method, such as a default constructor. */
	if (sjme_charSeq_equalsUtfR(typeDesc, "()V"))
	{
		*outArgC = 0;
		*outArgT = NULL;
		*outArgR = SJME_JAVA_TYPE_ID_VOID;
		return SJME_ERROR_NONE;
	}

	/* Setup iterator. */
	memset(&it, 0, sizeof(it));
	if (sjme_error_is(error = sjme_charSeq_itNew(typeDesc, 0, &it)))
		return sjme_error_default(error);

	/* Must start with parenthesis. */
	if (it.pp(&it) != '(')
		return sjme_error_vmError(NULL, SJME_ERROR_INVALID_METHOD_TYPE);

	/* Init state. */
	argAt = 0;
	arrayScope = SJME_JNI_FALSE;
	returnScope = SJME_JNI_FALSE;
	returnDid = SJME_JNI_FALSE;
	memset(args, 0, sizeof(args));

	/* Argument handling loop. */
	for (;;)
	{
		/* Type would overflow? */
		if (argAt >= SJME_MAX_ARGS)
			return sjme_error_vmError(NULL,
				SJME_ERROR_INVALID_METHOD_TYPE);
		
		/* Which type? */
		switch (it.pp(&it))
		{
				/* Integer and promotions. */
			case 'Z':
			case 'B':
			case 'S':
			case 'C':
			case 'I':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
				}
			
				/* Declare integer (or object if array). */
				args[argAt++] = (arrayScope ? SJME_JAVA_TYPE_ID_OBJECT :
					SJME_JAVA_TYPE_ID_INTEGER);
				arrayScope = SJME_JNI_FALSE;
				break;

				/* Long. */
			case 'J':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
				}
			
				/* Declare long (or object if array). */
				args[argAt++] = (arrayScope ? SJME_JAVA_TYPE_ID_OBJECT :
					SJME_JAVA_TYPE_ID_LONG);
				arrayScope = SJME_JNI_FALSE;
				break;

				/* Float. */
			case 'F':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
				}
			
				/* Declare float (or object if array). */
				args[argAt++] = (arrayScope ? SJME_JAVA_TYPE_ID_OBJECT :
					SJME_JAVA_TYPE_ID_FLOAT);
				arrayScope = SJME_JNI_FALSE;
				break;

				/* Double. */
			case 'D':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
				}
			
				/* Declare double (or object if array). */
				args[argAt++] = (arrayScope ? SJME_JAVA_TYPE_ID_OBJECT :
					SJME_JAVA_TYPE_ID_DOUBLE);
				arrayScope = SJME_JNI_FALSE;
				break;

				/* Arrays. */
			case '[':
				/* Enter the array scope. */
				arrayScope = SJME_JNI_TRUE;
				break;

				/* Object. */
			case 'L':
				if (returnScope)
				{
					if (returnDid)
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);
					returnDid = SJME_JNI_TRUE;
					
				}

				/* Keep going until ending `;`. */
				for (d = it.d(&it);; d = it.pp(&it))
				{
					/* Straight up invalid. */
					if (d == '.' || d == '[' || d == '\0')
						return sjme_error_vmError(NULL, 
							SJME_ERROR_INVALID_METHOD_TYPE);

					/* End of type. */
					if (d == ';')
						break;
				}

				/* Declare object. */
				args[argAt++] = SJME_JAVA_TYPE_ID_OBJECT;
				arrayScope = SJME_JNI_FALSE;
				break;

				/* End of arguments. */
			case ')':
				/* Never valid for arrays or for return types. */
				if (returnScope || arrayScope)
					return sjme_error_vmError(NULL, 
						SJME_ERROR_INVALID_METHOD_TYPE);

				returnScope = SJME_JNI_TRUE;
				break;

				/* Void type. */
			case 'V':
				/* Only valid for return types and never arrays. */
				if (!returnScope || arrayScope)
					return sjme_error_vmError(NULL, 
						SJME_ERROR_INVALID_METHOD_TYPE);

				/* This is only ever the case for return types. */
				returnDid = SJME_JNI_TRUE;

				/* Set special void type. */
				args[argAt++] = SJME_JAVA_TYPE_ID_VOID;
				break;

				/* NUL is only invalid when not properly at the end. */
			case '\0':
				if (returnDid)
					break;
				return sjme_error_vmError(NULL,
					SJME_ERROR_INVALID_METHOD_TYPE);
				
				/* Invalid. */
			default:
				return sjme_error_vmError(NULL,
					SJME_ERROR_INVALID_METHOD_TYPE);
		}

		/* True end of descriptor, with NUL. */
		if (it.d(&it) == '\0')
			break;
	}

	/* Cannot end on array or miss a return type. */
	if (arrayScope || !returnScope || argAt <= 0)
		return sjme_error_vmError(NULL, SJME_ERROR_INVALID_METHOD_TYPE);

	/* Return type is always the last type. */
	*outArgR = args[argAt - 1];

	/* Allocate result. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(allocPool,
		sizeof(*result) * argAt, (sjme_pointer*)&result)) || result == NULL)
		return sjme_error_default(error);

	/* Fill in other arguments. */
	for (i = 0, n = argAt - 1; i < n; i++)
		result[i] = args[i];

	/* Success! */
	*outArgC = n;
	*outArgT = result;
	return SJME_ERROR_NONE;
#undef SJME_MAX_ARGS
}

sjme_errorCode sjme_nvm_class_descriptorFieldSlots(
	sjme_attrInNotNull sjme_charSeq inDesc,
	sjme_attrOutNotNull sjme_jint* outSlots,
	sjme_attrInOutNullable sjme_jint* atP)
{
	sjme_jint at, result;
	sjme_jchar c;
	sjme_jboolean latched, done;
	
	if (inDesc == NULL || outSlots == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Use base at or use new one? */
	at = (atP != NULL ? (*atP) : 0);

	/* Read in type character and process it. */
	result = -1;
	for (latched = done = SJME_JNI_FALSE; !done;)
	{
		c = sjme_charSeq_charAtR(inDesc, at++);
		switch (c)
		{
			/* Array. */
			case '[':
				/* Arrays are always single slot. */
				/* However for arrays, we need to skip the bracket and */
				/* handle more of them. */
				result = 1;
				latched = SJME_JNI_TRUE;
				break;

				/* Object. */
			case 'L':
				/* Find ending ;. */
				while (c != ';')
				{
					c = sjme_charSeq_charAtR(inDesc, at++);
					if (c == 0 || c == ')')
						return SJME_ERROR_INVALID_FIELD_TYPE;
				}
				
				if (!latched)
					result = 1;
				done = SJME_JNI_TRUE;
				break;
			
				/* Double slot. */
			case 'J':
			case 'D':
				if (!latched)
					result = 2;
				done = SJME_JNI_TRUE;
				break;
			
			/* Single slot. */
			case 'Z':
			case 'B':
			case 'S':
			case 'C':
			case 'I':
			case 'F':
				if (!latched)
					result = 1;
				done = SJME_JNI_TRUE;
				break;

				/* Void type. */
			case 'V':
				/* If latched, means this was an array of void. */
				if (latched)
					return SJME_ERROR_INVALID_FIELD_TYPE;
				result = 0;
				done = SJME_JNI_TRUE;
				break;

			default:
				return SJME_ERROR_INVALID_FIELD_TYPE;
		}
	}

	/* Should not occur. */
	if (result < 0)
		return SJME_ERROR_INVALID_FIELD_TYPE;

	/* Store resultant at? */
	if (atP != NULL)
		*atP = at;

	/* Return resultant values. */
	*outSlots = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_class_descriptorMethodSlots(
	sjme_attrInNotNull sjme_charSeq inDesc,
	sjme_attrOutNotNull sjme_jint* outArgSlots,
	sjme_attrOutNotNull sjme_jint* outRvSlots)
{
	sjme_errorCode error;
	sjme_jint total, at, sub;
	sjme_jchar c;
	
	if (inDesc == NULL || outArgSlots == NULL || outRvSlots == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Must start with parenthesis. */
	at = 0;
	if ('(' != sjme_charSeq_charAtR(inDesc, at++))
		return SJME_ERROR_INVALID_METHOD_TYPE;
	
	/* Read until ending parenthesis. */
	total = 0;
	for (;;)
	{
		/* Read in next character. */
		c = sjme_charSeq_charAtR(inDesc, at);

		/* Invalid? */
		if (c == 0)
			return SJME_ERROR_INVALID_METHOD_TYPE;

		/* End of arguments? */
		if (c == ')')
		{
			/* Bump up as reading the field descriptor increments this. */
			at++;
			break;
		}

		/* Count field slots. */
		sub = -1;
		if (sjme_error_is(error = sjme_nvm_class_descriptorFieldSlots(
			inDesc, &sub, &at)) || sub < 0)
			return sjme_error_defaultOr(error, SJME_ERROR_INVALID_METHOD_TYPE);

		/* Add up. */
		total += sub;
	}

	/* Count return value slots. */
	sub = -1;
	if (sjme_error_is(error = sjme_nvm_class_descriptorFieldSlots(
		inDesc, &sub, &at)) || sub < 0)
		return sjme_error_defaultOr(error, SJME_ERROR_INVALID_METHOD_TYPE);

	/* Success! */
	*outArgSlots = total;
	*outRvSlots = sub;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_class_descriptorToType(
	sjme_attrInNotNull sjme_charSeq desc,
	sjme_attrOutNullable sjme_javaTypeId* outJavaType,
	sjme_attrOutNullable sjme_basicTypeId* outBasicType,
	sjme_attrOutNullable sjme_extendedTypeId* outExtendedType)
{
	sjme_javaTypeId javaType;
	sjme_basicTypeId basicType;
	sjme_extendedTypeId extendedType;
	
	if (outJavaType == NULL || desc == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (sjme_charSeq_equalsUtfR(desc, "Z"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_BASIC_TYPE_ID_BOOLEAN;
		extendedType = SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "B"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_BASIC_TYPE_ID_BYTE;
		extendedType = SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "S"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_BASIC_TYPE_ID_SHORT;
		extendedType = SJME_JAVA_TYPE_ID_SHORT_OR_CHAR;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "C"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_BASIC_TYPE_ID_CHARACTER;
		extendedType = SJME_JAVA_TYPE_ID_SHORT_OR_CHAR;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "I"))
	{
		javaType = SJME_JAVA_TYPE_ID_INTEGER;
		basicType = SJME_JAVA_TYPE_ID_INTEGER;
		extendedType = SJME_JAVA_TYPE_ID_INTEGER;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "J"))
	{
		javaType = SJME_JAVA_TYPE_ID_LONG;
		basicType = SJME_JAVA_TYPE_ID_LONG;
		extendedType = SJME_JAVA_TYPE_ID_LONG;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "F"))
	{
		javaType = SJME_JAVA_TYPE_ID_FLOAT;
		basicType = SJME_JAVA_TYPE_ID_FLOAT;
		extendedType = SJME_JAVA_TYPE_ID_FLOAT;
	}
	else if (sjme_charSeq_equalsUtfR(desc, "D"))
	{
		javaType = SJME_JAVA_TYPE_ID_DOUBLE;
		basicType = SJME_JAVA_TYPE_ID_DOUBLE;
		extendedType = SJME_JAVA_TYPE_ID_DOUBLE;
	}
	else if (sjme_charSeq_charAtIs(desc, 0, '[') ||
		(sjme_charSeq_charAtIs(desc, 0, 'L') &&
			sjme_charSeq_charAtIs(desc, desc->length - 1,
				';')))
	{
		javaType = SJME_JAVA_TYPE_ID_OBJECT;
		basicType = SJME_JAVA_TYPE_ID_OBJECT;
		extendedType = SJME_JAVA_TYPE_ID_OBJECT;
	}
	
	/* Not valid. */
	else
		return sjme_error_vmError(NULL, SJME_ERROR_INVALID_METHOD_TYPE);
	
	/* Success! */
	if (outJavaType != NULL)
		*outJavaType = javaType;
	if (outBasicType != NULL)
		*outBasicType = basicType;
	if (outExtendedType != NULL)
		*outExtendedType = extendedType;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_class_parseAttribute(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inConstPool,
	sjme_attrInNotNull sjme_nvm_stringPool inStringPool,
	sjme_attrInNotNull const sjme_nvm_class_parseAttributeHandler* handlers,
	sjme_attrInNotNull sjme_pointer context,
	sjme_attrInNotNull sjme_charSeq attrName,
	sjme_attrInPositive sjme_jint attrLen)
{
	sjme_errorCode error, errorC;
	sjme_jubyte* attrData;
	sjme_jint readCount;
	const sjme_nvm_class_parseAttributeHandler* at;
	sjme_stream_input attrStream;
	
	if (allocPool == NULL || inStream == NULL || inConstPool == NULL ||
		inStringPool == NULL || handlers == NULL || context == NULL ||
		attrName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (attrLen < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Allocate buffer to read in the data. */
	attrData = sjme_alloca(attrLen);
	if (attrData == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(attrData, 0, attrLen);
	
	/* Read in everything. */
	readCount = -1;
	if (sjme_error_is(error = sjme_stream_inputReadFully(
		inStream, &readCount, attrData, attrLen)) ||
		readCount < 0)
		return sjme_error_default(error);
	
	/* Find and call handler for this. */
	for (at = handlers; at->name != NULL && at->handler != NULL; at++)
		if (sjme_charSeq_equalsUtfR(attrName, at->name))
		{
			/* Load stream over the data. */
			attrStream = NULL;
			if (sjme_error_is(error = sjme_stream_inputOpenMemory(
				allocPool, &attrStream,
				attrData, attrLen)) || attrStream == NULL)
				return sjme_error_default(error);
			
			/* Process it. */
			error = at->handler(allocPool, inConstPool, inStringPool, context,
				attrName, attrStream, attrData, attrLen);
			
			/* Close stream. */
			if (sjme_error_is(errorC = sjme_closeable_close(
				SJME_AS_CLOSEABLE(attrStream))))
				return sjme_error_defaultOr(error, errorC);
			
			/* Failed? */
			if (sjme_error_is(error))
				return sjme_error_default(error);
			return error;
		}
	
#if defined(SJME_CONFIG_DEBUG_VERBOSE)
	/* Debug. */
	sjme_message("No handler for attribute %s.", attrName);
#endif
	
	/* None found, so ignore it. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_class_readPoolRefIndex(
	sjme_attrInNotNull sjme_stream_input inStream,
	sjme_attrInNotNull sjme_nvm_class_poolInfo inClassPool,
	sjme_attrInPositiveNonZero sjme_nvm_class_poolType desireType,
	sjme_attrInValue sjme_jboolean canNull,
	sjme_attrOutNotNull sjme_nvm_class_poolEntry** outEntry)
{
	sjme_errorCode error;
	sjme_jshort index;
	sjme_nvm_class_poolEntry* result;
	
	if (inStream == NULL || inClassPool == NULL || outEntry == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Read in index. */
	index = -1;
	if (sjme_error_is(error = sjme_stream_inputReadValueJS(
		inStream, &index)))
		return sjme_error_default(error);
	
	/* Not a valid index? */
	if (index <= 0 || index >= inClassPool->pool->length)
	{
		/* Can be zero index for nothing, however. */
		if (index == 0 && canNull)
		{
			*outEntry = NULL;
			return SJME_ERROR_NONE;
		}
		
		return SJME_ERROR_INVALID_CLASS_POOL_INDEX;
	}
	
	/* Must be the desired type. */
	result = &inClassPool->pool->elements[index];
	if (result->type != desireType)
		return SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE;
	
	/* Success! */
	*outEntry = result;
	return SJME_ERROR_NONE;
}
