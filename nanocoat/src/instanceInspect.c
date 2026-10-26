/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/instance.h"
#include "sjme/nvm/instanceInspect.h"
#include "sjme/nvm/cleanup.h"

static sjme_jboolean sjme_nvm_instance_arrayCheckAlias(
	sjme_attrInNotNull sjme_jclass componentType,
	sjme_attrInValue sjme_basicTypeId arrayType)
{
	if (componentType == NULL)
		return SJME_JNI_FALSE;
	
	/* Boolean and byte are considered the same. */
	if (arrayType == SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE)
		if (componentType->arrayTypeId == SJME_BASIC_TYPE_ID_BOOLEAN ||
			componentType->arrayTypeId == SJME_BASIC_TYPE_ID_BYTE)
			return SJME_JNI_TRUE;

	return componentType->arrayTypeId == arrayType;
}

sjme_errorCode sjme_nvm_instance_arrayCheck(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInValue sjme_basicTypeId requireType)
{
	sjme_jclass isClass, componentType;
	
	/* Requested type is not valid? */
	if (requireType < 0 ||
		requireType == SJME_STACK_TYPE_NARROW ||
		requireType == SJME_STACK_TYPE_WIDE ||
		requireType > SJME_NUM_BASIC_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (inArray == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	/* Is this even an array structure? */
	if (!sjme_nvm_isAR(inArray, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return SJME_ERROR_CLASS_CAST;
	
	/* The type information says that this is not an array? */
	isClass = sjme_atomic_g(sjme_jclass, &inArray->object.isClass);
	if (isClass == NULL || isClass->info == NULL ||
		!isClass->info->isArray)
		return SJME_ERROR_CLASS_CAST;
	
	/* Is the component type wrong? If we care that is... */
	if (requireType != SJME_NUM_BASIC_TYPE_IDS &&
		requireType != SJME_NUM_JAVA_TYPE_IDS)
	{
		/* Get the component type. */
		componentType = sjme_atomic_g(sjme_jclass,
			&isClass->componentType);
		if (componentType == NULL)
			return SJME_ERROR_ARRAY_STORE;
		
		/* The component type is incorrect? */
		if (!sjme_nvm_instance_arrayCheckAlias(componentType,
			requireType))
			return SJME_ERROR_ARRAY_STORE;
	}
	
	/* Array access is entirely valid. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_arrayCheckI(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInValue sjme_basicTypeId requireType,
	sjme_attrInPositive sjme_jint requireIndex,
	sjme_attrInPositiveNonZero sjme_jint requireSpan)
{
	sjme_errorCode error;
	
	/* Check the array type first. */
	if (sjme_error_is(error = sjme_nvm_instance_arrayCheck(inArray,
		requireType)))
		return sjme_error_default(error);
	
	/* Index overflow/underflow? */
	if (requireIndex < 0 || requireSpan <= 0 ||
		(requireIndex + requireSpan) < 0)
		return SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS;
	
	/* Actual index is out of bounds. */
	if (requireIndex >= inArray->e.length ||
		(requireIndex + requireSpan) > inArray->e.length)
		return SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS;
	
	/* Array access is entirely valid. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_arrayCopyJ(
	sjme_attrInNotNull sjme_jarray srcArray,
	sjme_attrInPositive sjme_jint srcOff,
	sjme_attrInNotNull sjme_jarray destArray,
	sjme_attrInPositive sjme_jint destOff,
	sjme_attrInPositive sjme_jint length,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit)
{
	if (srcArray == NULL || destArray == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	if (length < 0 ||
		srcOff < 0 || (srcOff + length) < 0 ||
		destOff < 0 || (destOff + length) < 0)
		return SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_instance_arrayGet(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInValue sjme_basicTypeId requireType,
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue)
{
	sjme_errorCode error;
	sjme_jvalueTyped result;
	
	/* Check for valid access. */
	if (sjme_error_is(error = sjme_nvm_instance_arrayCheckI(inArray,
		requireType, index, 1)))
		return sjme_error_default(error);
	
	/* Read in the value. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(error = sjme_nvm_vmField_cisGetS(
		&inArray->e, index, SJME_VLG_JVALUE_TYPED_P(&result))))
		return sjme_error_default(error);
	
	/* Return it. */
	memmove(outValue, &result, sizeof(*outValue));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_arrayGetString(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_charSeq* outValue,
	sjme_attrInValue sjme_jboolean dup)
{
	if (inArray == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	if (outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (!sjme_nvm_isAR(inArray, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return SJME_ERROR_CLASS_CAST;
	
	if (index < 0 || index >= inArray->e.length)
		return SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_instance_arraySet(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInValue sjme_basicTypeId requireType,
	sjme_attrInPositive sjme_jint index,
	sjme_attrInNotNull sjme_nvm_frame_gcCommit* commit,
	sjme_attrOutNotNull sjme_jvalueTyped* inValue)
{
	sjme_errorCode error;
	sjme_jclass isClass, componentType, valueClass;
	
	if (contextThread == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	/* Check for valid access. */
	if (sjme_error_is(error = sjme_nvm_instance_arrayCheckI(inArray,
		requireType, index, 1)))
		return sjme_error_default(error);
	
	/* If an object is being stored here, it needs to be checked. */
	isClass = sjme_atomic_g(sjme_jclass, &inArray->object.isClass);
	componentType = sjme_atomic_g(sjme_jclass,
		&isClass->componentType);
	if ((inValue->t == SJME_JAVA_TYPE_ID_OBJECT ||
			componentType->typeId == SJME_JAVA_TYPE_ID_OBJECT) &&
		inValue->v.l != NULL)
	{
		/* Get the value class. */
		valueClass = sjme_atomic_g(sjme_jclass, &inValue->v.l->isClass);
		if (valueClass == NULL)
			return SJME_ERROR_ILLEGAL_STATE;
		
		/* The value must be able to be stored here. */
		if (sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(
			contextThread, valueClass, componentType)))
		{
			/* It cannot, so map to an array store error. */
			if (error == SJME_ERROR_CLASS_CAST)
				return SJME_ERROR_ARRAY_STORE;
			
			/* Otherwise, not good. */
			return sjme_error_default(error);
		}
	}
	
	/* Store the actual value. */
	if (sjme_error_is(error = sjme_nvm_vmField_cisSetS(
		&inArray->e, index, commit,
		SJME_VLS_JVALUE_TYPED_P(inValue))))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
