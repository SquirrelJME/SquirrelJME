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
#include "sjme/nvm/task.h"

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
	if (isClass == NULL || SJME_CL_I(isClass) == NULL ||
		!SJME_CL_I(isClass)->isArray)
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
	sjme_attrInPositive sjme_jint requireSpan)
{
	sjme_errorCode error;
	
	/* Check the array type first. */
	if (sjme_error_is(error = sjme_nvm_instance_arrayCheck(inArray,
		requireType)))
		return sjme_error_default(error);
	
	/* Index overflow/underflow? */
	if (requireIndex < 0 || requireSpan < 0 ||
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
	sjme_errorCode error;
	sjme_jint i;
	sjme_jobject copyRef;
	size_t copyLen;
	
	if (srcArray == NULL || destArray == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	if (length < 0 ||
		srcOff < 0 || (srcOff + length) < 0 ||
		destOff < 0 || (destOff + length) < 0)
		return SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS;
	
	/* Array access must be valid. */
	/* Check the opposite array's type. */
	if (sjme_error_is(error = sjme_nvm_instance_arrayCheckI(
		srcArray, destArray->e.type, srcOff, length)))
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_nvm_instance_arrayCheckI(
		destArray, srcArray->e.type, destOff, length)))
		return sjme_error_default(error);
	
	/* If this is an object array, copy over all values one by one. */
	if (srcArray->e.type == SJME_JAVA_TYPE_ID_OBJECT)
	{
		for (i = 0; i < length; i++)
		{
			/* Read the reference. */
			copyRef = NULL;
			if (sjme_error_is(error = sjme_nvm_vmField_cisGetS(
				&srcArray->e, i, 
				SJME_VLG_JOBJECT_P(&copyRef))))
				return sjme_error_default(error);
			
			/* Set the reference. */
			if (sjme_error_is(error = sjme_nvm_vmField_cisSetS(
				&destArray->e, i, commit, 
				SJME_VLS_JOBJECT(copyRef))))
				return sjme_error_default(error);
		}
	}
	
	/* Copy all values over. */
	else
	{
		copyLen = sjme_util_intToSize(sjme_nvm_typeMul[
			sjme_max(0, srcArray->e.type) % SJME_NUM_BASIC_TYPE_IDS] * length);
		memmove(&destArray->e, &srcArray->e, copyLen);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
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

sjme_errorCode sjme_nvm_instance_arrayGetO(
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_jobject* outValue)
{
	sjme_errorCode error;
	sjme_jvalueTyped value;

	/* Grab from the other call. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_instance_arrayGet(
		inArray, SJME_JAVA_TYPE_ID_OBJECT, index, &value)))
		return sjme_error_default(error);
	
	/* Copy over. */
	*outValue = value.v.l;
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
			contextThread, componentType, valueClass)))
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

sjme_errorCode sjme_nvm_instance_arraySetO(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jarray inArray,
	sjme_attrInPositive sjme_jint index,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrOutNotNull sjme_jobject inValue)
{
	sjme_errorCode error;
	sjme_jvalueTyped value;
	
	/* Set value. */
	memset(&value, 0, sizeof(value));
	value.t = SJME_JAVA_TYPE_ID_OBJECT;
	value.v.l = inValue;
	
	/* Forward call. */
	return sjme_nvm_instance_arraySet(contextThread,
		inArray, SJME_JAVA_TYPE_ID_OBJECT, index,
		commit, &value);
}

sjme_errorCode sjme_nvm_instance_fieldGet(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue)
{
	sjme_errorCode error;
	sjme_jvalueTyped value;
	
	if (contextThread == NULL || inField == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inObject == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;

	/* Read/write promotion is performed through this call. */
	/* Note that the class referred to by the field is used directly as */
	/* static fields can be "inherited" by subclasses. So we want to use */
	/* the class this field truly exists in. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_instance_fieldAccessStack(
		contextThread, NULL, inField, inObject, &value,
		SJME_JNI_FALSE)))
		return sjme_error_vmError(contextThread, error);
	
	/* Copy over. */
	memmove(outValue, &value, sizeof(*outValue));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_fieldGetO(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrInValue sjme_nvm_structType inStructType,
	sjme_attrOutNotNull sjme_pointer* outValue)
{
	sjme_errorCode error;
	sjme_jvalueTyped value;
	
	if (contextThread == NULL || inField == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inObject == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	if (inStructType != SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE &&
		!sjme_nvm_isObjectId(inStructType))
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Read in the value. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_instance_fieldGet(contextThread,
		inObject, inField, &value)))
		return sjme_error_default(error);
	
	/* If not null, check the type. */
	if (value.v.l != NULL)
		if (!sjme_nvm_isAR(value.v.l, inStructType))
			return SJME_ERROR_CLASS_CAST;
			
	/* Copy value over. */
	*outValue = value.v.l;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_instance_fieldSet(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNotNull sjme_jvalueTyped* inValue)
{
	if (contextThread == NULL || inField == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inObject == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
#if 0
		/* TODO FIXME: USE INSPECT FOR THIS! */

		/* Flag cause as initialized. */
		memset(&volatileStorage, 0, sizeof(volatileStorage));
		accessor = sjme_nvm_instance_fieldAccessorVI(
			SJME_AS_JOBJECT(toss), initCauseID, &volatileStorage);
		accessor->v.z = SJME_JNI_TRUE;

		/* Set actual cause now, and its check value directly. */
		accessor = sjme_nvm_instance_fieldAccessorVI(
			SJME_AS_JOBJECT(toss), causeID, &volatileStorage);
		if (sjme_error_is(error = sjme_nvm_vmField_cisSet(
			accessor, causeID->info->basicType,
			NULL, SJME_VLS_JOBJECT(toss))))
			return sjme_error_vmError(inThread, error);
		
		/* Commit. */
		volatileStorage.commit = SJME_JNI_TRUE;
		sjme_nvm_instance_fieldAccessorVI(SJME_AS_JOBJECT(toss), 
			initCauseID, &volatileStorage);
		
		/* TODO FIXME: USE INSPECT FOR THIS! */
#endif
}

sjme_errorCode sjme_nvm_instance_fieldSetI(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrInNotNull sjme_jint inValue)
{
	if (contextThread == NULL || inField == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inObject == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_instance_fieldSetO(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jobject inObject,
	sjme_attrInNotNull sjme_jfieldID inField,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNotNull sjme_jobject inValue)
{
	if (contextThread == NULL || inField == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inObject == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
