/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/classy.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/access.h"
#include "sjme/nvm/instanceInspect.h"

#define SJME_NVM_NUM_XARRAY 8

static const sjme_basicTypeId sjme_nvm_byteCode_xArrayType
	[SJME_NVM_NUM_XARRAY] =
{
	SJME_JAVA_TYPE_ID_INTEGER,
	SJME_JAVA_TYPE_ID_LONG,
	SJME_JAVA_TYPE_ID_FLOAT,
	SJME_JAVA_TYPE_ID_DOUBLE,
	SJME_JAVA_TYPE_ID_OBJECT,
	SJME_JAVA_TYPE_ID_BOOLEAN_OR_BYTE,
	SJME_BASIC_TYPE_ID_CHARACTER,
	SJME_BASIC_TYPE_ID_SHORT,
};

static sjme_errorCode sjme_nvm_byteCode_slowNewArrayMultiSub(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jclass rootComponentType,
	sjme_attrInValue sjme_jint dims,
	sjme_attrInValue sjme_jint skip,
	sjme_attrInNotNull sjme_jvalueTyped* argV,
	sjme_attrInNotNull sjme_jvalueTyped* result)
{
	sjme_errorCode error;
	sjme_jint nextSkip, i, numElem, left;
	sjme_jarray baseArray;
	sjme_jclass componentType;
	sjme_jvalueTyped sub;
	
	if (inFrame == NULL || rootComponentType == NULL ||
		argV == NULL || result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (dims < 0 || skip < 0 || dims - skip < 0)
		return SJME_ERROR_INVALID_INSTRUCTION;

	/* How many elements are here? */
	numElem = argV[skip].v.i;
	if (numElem < 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_NEGATIVE_ARRAY_SIZE);

	/* How many dimensions are left? */
	left = dims - skip;
	
	/* Determine component type. */
	componentType = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadArray(
		SJME_F_CL(inFrame), &componentType, SJME_F_T(inFrame),
		rootComponentType, left)))
		return sjme_error_vmError(inFrame, error);

	/* Allocate base array. */
	baseArray = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(
		SJME_F_T(inFrame), &baseArray, componentType, numElem)))
		return sjme_error_vmError(inFrame, error);

	/* Need to allocate a sub-array? */
	if (left > 1)
	{
		/* Allocate sub arrays for each element slot. */
		nextSkip = skip + 1;
		for (i = 0; i < numElem; i++)
		{
			/* Perform sub-allocation logic. */
			memset(&sub, 0, sizeof(sub));
			if (sjme_error_is(error = sjme_nvm_byteCode_slowNewArrayMultiSub(
				inFrame, rootComponentType, dims, nextSkip, argV, &sub)))
				return sjme_error_vmError(inFrame, error);

			/* Store into this array. */
			if (sjme_error_is(error = sjme_nvm_vmField_cisSetS(
				&baseArray->e, i, NULL, SJME_VLS_JOBJECT(sub.v.l))))
				return sjme_error_vmError(inFrame, error);
		}
	}

	/* The result gets the base array. */
	result->t = SJME_JAVA_TYPE_ID_OBJECT;
	result->v.l = SJME_AS_JOBJECT(baseArray);
	return SJME_ERROR_NONE;
}

#pragma region(ArrayLength)
SJME_NVM_BYTECODE_SLOW(ArrayLength)
{
	sjme_jvalueTyped arrayValue, result;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Clear the commit. */
	memset(&commit, 0, sizeof(commit));

	/* Pop single object value. */
	memset(&arrayValue, 0, sizeof(arrayValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &arrayValue)))
		return sjme_error_vmError(inFrame, error);

	/* Must be an array. */
	if (sjme_error_is(error = sjme_nvm_instance_arrayCheck(
		SJME_AS_JARRAY(arrayValue.v.l),
		SJME_NUM_JAVA_TYPE_IDS)))
	{
		/* NPE? */
		if (sjme_error_isOneOf(error,
			SJME_ERROR_NULL_STACK_POINTER,
			0))
		{
			/* Emit exception. */
			if (sjme_error_is(error = sjme_nvm_task_frameEmitE(inFrame,
				error, NULL, "%s",
				(arrayValue.v.l == NULL ? "null" : "not-null"))))
				return sjme_error_vmError(inFrame, error);
		
			/* Leave early. */
			goto skip_tossed;
		}
		
		/* Failed otherwise. */
		return sjme_error_vmError(inFrame, error);
	}
	
	/* Push length onto the stack. */
	memset(&result, 0, sizeof(result));
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	result.v.i = SJME_AS_JARRAY(arrayValue.v.l)->e.length;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &result)))
		return sjme_error_vmError(inFrame, error);

skip_tossed:
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(CheckCast)
SJME_NVM_BYTECODE_SLOW(CheckCast)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_jvalueTyped value;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Clear the commit. */
	memset(&commit, 0, sizeof(commit));

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&desireClass,
		SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Pop object from the stack. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPeek(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &value)))
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}
	
	/* If nothing is here, this always succeeds. */
	if (value.v.l == NULL)
		goto skip_isNull;

	/* Not a match? */
	/* b.getClass().isAssignableFrom(a.getClass()) == (a instanceof b) */
	if (sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(
			SJME_F_T(inFrame), desireClass, 
			SJME_O_C(value.v.l))))
	{
		/* Exception is something else? */
		if (!sjme_error_isOneOf(error,
			SJME_ERROR_CLASS_CAST, 0))
			return sjme_error_default(error);
		
		/* Emit exception. */
		if (sjme_error_is(error = sjme_nvm_task_frameEmitE(inFrame,
			error, NULL, "%s %s", "?", "?")))
			return sjme_error_vmError(inFrame, error);
	}
	
skip_isNull:
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(InstanceAccess)
SJME_NVM_BYTECODE_SLOW(InstanceAccess)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_jfieldID fieldId;
	sjme_jvalueTyped result;
	sjme_jvalueTyped instance;
	sjme_jboolean isPut;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Clear the commit. */
	memset(&commit, 0, sizeof(commit));

	/* Is this a get or a put? */
	isPut = (id == 181);

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_FIELD,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame), &desireClass, SJME_F_T(inFrame),
		SJME_P_M_C(entry)->seq, SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}
	
	/* Lookup field in the class. */
	fieldId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmField_idByNameType(
		desireClass, SJME_F_T(inFrame),
		SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq,
		&fieldId)) || fieldId == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Not an instance field? */
	if (SJME_NVM_ACC_IS(fieldId->flags, STATIC))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check access for calling this method. */
	if (sjme_error_is(error = sjme_nvm_access_checkFToF(
		inFrame, fieldId)))
	{
		/* Some other error? */
		if (error != SJME_ERROR_MEMBER_ACCESS_DENIED)
			return sjme_error_default(error);

		/* Emit throwable. */
		return sjme_error_vmError(inFrame,
			SJME_ERROR_CLASS_CHANGED);
	}

	/* Read in value to put. */
	memset(&result, 0, sizeof(result));
	if (isPut)
	{
		/* Cannot be final unless we are in a static initializer. */
		if (SJME_NVM_ACC_IS(fieldId->flags, FINAL))
		{
			/* Cannot write static final fields. */
			if (SJME_NVM_ACC_IS(fieldId->flags, STATIC))
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
			
			/* Completely different class? */
			if (sjme_atomic_g(sjme_jclass, &fieldId->member.inClass) !=
				inFrame->inClass)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);

			/* We must be in an instance initializer. */
			if (!SJME_NVM_FRAME_STATE_IS(inFrame->flags, INIT_INSTANCE))
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
		}

		/* Read in the value to write. */
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			fieldId->javaType, &commit, &result)))
			return sjme_error_vmError(inFrame, error);
		
		/* Make sure this can actually be stored there. */
		if (sjme_error_is(error = sjme_nvm_access_checkCompatibleField(
			SJME_F_T(inFrame), fieldId, &result)))
			return sjme_error_vmError(inFrame,
				SJME_ERROR_CLASS_CHANGED);
	}
	
	/* Read instance to act on. */
	memset(&instance, 0, sizeof(instance));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &instance)))
		return sjme_error_vmError(inFrame, error);

	/* Cannot be null. */
	if (instance.v.l == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Read/write promotion. */
	if (sjme_error_is(error = sjme_nvm_instance_fieldAccessStack(
		SJME_F_T(inFrame),
		&commit, fieldId, instance.v.l, &result, isPut)))
		return sjme_error_vmError(inFrame, error);

	/* Push result to the stack. */
	if (!isPut)
	{
		result.t = fieldId->javaType;
		if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
			inFrame, &commit, &result)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(InstanceOf)
SJME_NVM_BYTECODE_SLOW(InstanceOf)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jvalueTyped check, result;
	sjme_jclass desireClass;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Read in object to check. */
	memset(&check, 0, sizeof(check));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &check)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&desireClass,
		SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}

	/* Is this the given class? */
	memset(&result, 0, sizeof(result));
	if (check.v.l == NULL)
		result.v.i = SJME_JNI_FALSE;
	else
	{
		if (sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(
			SJME_F_T(inFrame),
			desireClass, SJME_O_C(check.v.l))))
		{
			if (error != SJME_ERROR_CLASS_CAST)
				return sjme_error_default(error);
			result.v.i = SJME_JNI_FALSE;
		}
		else
			result.v.i = SJME_JNI_TRUE;
	}

	/* Push result to the stack. */
	result.t = SJME_JAVA_TYPE_ID_INTEGER;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &result)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(Monitor)
SJME_NVM_BYTECODE_SLOW(Monitor)
{
	sjme_jboolean isExit;
	sjme_jvalueTyped instance;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Entry or exit? */
	isExit = (id == 195);

	/* Get the object we are accessing. */
	memset(&instance, 0, sizeof(instance));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &instance)))
		return sjme_error_vmError(inFrame, error);
	
	/* Cannot be null. */
	if (instance.v.l == NULL)
		return SJME_ERROR_NULL_STACK_POINTER;

	/* Either lock or unlock the object. */
	if (!isExit)
	{
		if (sjme_error_is(error = sjme_nvm_instance_monitorEnter(
			SJME_F_T(inFrame), instance.v.l)))
			return sjme_error_vmError(inFrame, error);
	}
	else
	{
		if (sjme_error_is(error = sjme_nvm_instance_monitorExit(
			SJME_F_T(inFrame), instance.v.l)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(New)
SJME_NVM_BYTECODE_SLOW(New)
{
	SJME_NVM_BYTECODE_ENTRY;
	sjme_jint poolIndex;
	sjme_jvalueTyped result;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_nvm_frame_gcCommit commit;
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&desireClass,
		SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}

	/* Allocate new instance of the given object. */
	memset(&result, 0, sizeof(result));
	result.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_instance_objectNew(
		SJME_F_T(inFrame), -1, SJME_NVM_STRUCT_OBJECT_INSTANCE,
		&result.v.l, desireClass)) || result.v.l == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Setup commit. */
	memset(&commit, 0, sizeof(commit));
	
	/* Push allocated object instance to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &result)))
		return sjme_error_vmError(inFrame, error);
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(NewArray)
SJME_NVM_BYTECODE_SLOW(NewArray)
{
	sjme_jvalueTyped length, array;
	sjme_basicTypeId arrayType;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in array length. */
	memset(&length, 0, sizeof(length));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &length)))
		return sjme_error_vmError(inFrame, error);

	/* Length is not valid. */
	if (length.v.i < 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_NEGATIVE_ARRAY_SIZE);

	/* Map array type. */
	switch (relRawCode[1])
	{
		case 4:
			arrayType = SJME_BASIC_TYPE_ID_BOOLEAN;
			break;
		
		case 5:
			arrayType = SJME_BASIC_TYPE_ID_CHARACTER;
			break;
		
		case 6:
			arrayType = SJME_JAVA_TYPE_ID_FLOAT;
			break;
		
		case 7:
			arrayType = SJME_JAVA_TYPE_ID_DOUBLE;
			break;
		
		case 8:
			arrayType = SJME_BASIC_TYPE_ID_BYTE;
			break;
		
		case 9:
			arrayType = SJME_BASIC_TYPE_ID_SHORT;
			break;
		
		case 10:
			arrayType = SJME_JAVA_TYPE_ID_INTEGER;
			break;
		
		case 11:
			arrayType = SJME_JAVA_TYPE_ID_LONG;
			break;
		
		default:
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_INSTRUCTION);
	}

	/* Create new array. */
	memset(&array, 0, sizeof(array));
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNewT(
		SJME_F_T(inFrame), SJME_AS_JARRAYP(&array.v.l), arrayType,
		length.v.i)))
		return sjme_error_vmError(inFrame, error);

	/* Push to the stack. */
	array.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &array)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(NewArrayA)
SJME_NVM_BYTECODE_SLOW(NewArrayA)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass componentType;
	sjme_jvalueTyped length;
	sjme_jvalueTyped array;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Determine the referenced class. */
	componentType = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&componentType,
		SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq,
		SJME_JNI_TRUE)) || componentType == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}
	
	/* Read in array length. */
	memset(&length, 0, sizeof(length));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &length)))
		return sjme_error_vmError(inFrame, error);

	/* Length is not valid. */
	if (length.v.i < 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_NEGATIVE_ARRAY_SIZE);

	/* Create new array. */
	memset(&array, 0, sizeof(array));
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(
		SJME_F_T(inFrame), SJME_AS_JARRAYP(&array.v.l), componentType,
		length.v.i)))
		return sjme_error_vmError(inFrame, error);

	/* Push to the stack. */
	array.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&commit, &array)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(NewArrayMulti)
SJME_NVM_BYTECODE_SLOW(NewArrayMulti)
{
	sjme_nvm_class_poolEntry* entry;
	sjme_javaTypeId* argT;
	sjme_jvalueTyped* argV;
	sjme_jint poolIndex, i, dims;
	sjme_jvalueTyped result;
	sjme_jclass rootComponentType;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Lookup the root component type. */
	rootComponentType = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame), &rootComponentType, SJME_F_T(inFrame),
		SJME_P_C_N(entry)->seq, SJME_JNI_TRUE)) ||
		rootComponentType == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, that is a class load happened. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}

	/* How many dimensions to read? */
	dims = relRawCode[3] & 0xFF;
	if (dims <= 0)
		return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_INSTRUCTION);

	/* Allocate argument storage. */
	argT = sjme_alloca(sizeof(*argT) * dims);
	argV = sjme_alloca(sizeof(*argV) * dims);
	if (argT == NULL || argV == NULL)
		return sjme_error_outOfMemory(NULL, 0);
	memset(argT, 0, sizeof(*argT) * dims);
	memset(argV, 0, sizeof(*argV) * dims);

	/* Set all types to integer. */
	for (i = 0; i < dims; i++)
		argT[i] = SJME_JAVA_TYPE_ID_INTEGER;

	/* Setup commit. */
	memset(&commit, 0, sizeof(commit));

	/* Pop all dimensions at once. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPopA(inFrame,
		NULL, dims, argT, argV)))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Recursively allocate sub-dimensions. */
	memset(&result, 0, sizeof(result));
	if (sjme_error_is(error = sjme_nvm_byteCode_slowNewArrayMultiSub(
		inFrame, rootComponentType, dims, 0, argV, &result)))
		return sjme_error_vmError(inFrame, error);

	/* Push final result to the stack. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &result)))
		return sjme_error_vmError(inFrame, error);
	
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(StaticAccess)
SJME_NVM_BYTECODE_SLOW(StaticAccess)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_jclass desireClass;
	sjme_jfieldID fieldId;
	sjme_jvalueTyped value;
	sjme_jboolean isPut;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Is this a get or a put? */
	isPut = (id == 179);

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_FIELD,
		0)))
		return sjme_error_vmError(inFrame, error);
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame),
		&desireClass,
		SJME_F_T(inFrame),
		SJME_P_M_C(entry)->seq,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Check for recycle, class load and/or static constructor. */
	if (sjme_nvm_byteCode_checkRecycleR(inFrame))
	{
		pcNew->type = SJME_NVM_BYTECODE_PC_RECYCLE;
		return SJME_ERROR_NONE;
	}
	
	/* Lookup field in the class. */
	fieldId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmField_idByNameType(
		desireClass, SJME_F_T(inFrame),
		SJME_NVM_CLASS_MEMBER_STATIC,
		SJME_JNI_TRUE,
		SJME_P_M_N(entry)->seq,
		SJME_P_M_T(entry)->seq,
		&fieldId)) || fieldId == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Not a static field? */
	if (!SJME_NVM_ACC_IS(fieldId->flags, STATIC))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check access for calling this method. */
	if (sjme_error_is(error = sjme_nvm_access_checkFToF(
		inFrame, fieldId)))
		return sjme_error_vmError(inFrame,
			SJME_ERROR_CLASS_CHANGED);
	
	/* Read in value to put. */
	memset(&value, 0, sizeof(value));
	memset(&commit, 0, sizeof(commit));
	if (isPut)
	{
		/* Cannot be final unless we are in a static initializer. */
		if (SJME_NVM_ACC_IS(fieldId->flags, FINAL))
		{
			/* Cannot write instance final fields. */
			if (!SJME_NVM_ACC_IS(fieldId->flags, STATIC))
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
			
			/* Completely different class? */
			if (sjme_atomic_g(sjme_jclass, &fieldId->member.inClass) !=
				inFrame->inClass)
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);

			/* We must be in a static initializer. */
			if (!SJME_NVM_FRAME_STATE_IS(inFrame->flags, INIT_STATIC))
				return sjme_error_vmError(inFrame,
					SJME_ERROR_MEMBER_ACCESS_DENIED);
		}

		/* Read in the value to write. */
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			fieldId->javaType, &commit, &value)))
			return sjme_error_vmError(inFrame, error);
		
		/* Make sure this can actually be stored there. */
		if (sjme_error_is(error = sjme_nvm_access_checkCompatibleField(
			SJME_F_T(inFrame), fieldId, &value)))
			return sjme_error_vmError(inFrame, error);
	}
	
	/* Read/write promotion. */
	/* Note that the class referred to by the field is used directly as */
	/* static fields can be "inherited" by subclasses. So we want to use */
	/* the class this field truly exists in. */
	if (sjme_error_is(error = sjme_nvm_instance_fieldAccessStack(
		SJME_F_T(inFrame),
		&commit, fieldId, SJME_AS_JOBJECT(sjme_atomic_g(sjme_jclass,
			&fieldId->member.inClass)), &value, isPut)))
		return sjme_error_vmError(inFrame, error);
	
	/* Push result to the stack. */
	if (!isPut)
	{
		/* Place onto the stack. */
		value.t = fieldId->javaType;
		if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
			inFrame, &commit, &value)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(Throw)
SJME_NVM_BYTECODE_SLOW(Throw)
{
	sjme_jvalueTyped toss;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Read in object to toss. */
	memset(&toss, 0, sizeof(toss));
	memset(&commit, 0, sizeof(commit));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &toss)))
		return sjme_error_vmError(inFrame, error);

	/* Cannot be null. */
	if (toss.v.l == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Set thrown exception. */
	if (!sjme_atomic_cs(sjme_jobject, &SJME_F_T(inFrame)->tossed,
		NULL, toss.v.l))
		return sjme_error_vmError(inFrame, SJME_ERROR_DOUBLE_TOSS);

	/* Set the toss level very high as we are not running any implicit */
	/* constructors! */
	sjme_atomic_s(sjme_jint, &SJME_F_T(inFrame)->tossedLevel, INT32_MAX);

	/* Count up since it is now also in tossed. */
	if (sjme_error_is(error = sjme_nvm_instance_countUp(toss.v.l)))
		return sjme_error_vmError(inFrame, error);

	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(XALoad)
SJME_NVM_BYTECODE_SLOW(XALoad)
{
	sjme_jvalueTyped arrayValue;
	sjme_jvalueTyped indexValue;
	sjme_jvalueTyped pushValue;
	sjme_basicTypeId arrayType;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;
	
	/* Reset the commit. */
	memset(&commit, 0, sizeof(commit));
	
	/* Which array type must this be? */
	arrayType = sjme_nvm_byteCode_xArrayType[
		abs(id - 46) % SJME_NVM_NUM_XARRAY];

	/* Read in index and array. */
	memset(&indexValue, 0, sizeof(indexValue));
	memset(&arrayValue, 0, sizeof(arrayValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &indexValue)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &arrayValue)))
		return sjme_error_vmError(inFrame, error);

	/* Read from the array. */
	memset(&pushValue, 0, sizeof(pushValue));
	if (sjme_error_is(error = sjme_nvm_instance_arrayGet(
		SJME_AS_JARRAY(arrayValue.v.l),
		arrayType, indexValue.v.i, &pushValue)))
	{
		/* Is this an applicable error? */
		if (sjme_error_isOneOf(error,
			SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS,
			SJME_ERROR_NULL_STACK_POINTER,
			0))
		{
			/* Emit exception. */
			if (sjme_error_is(error = sjme_nvm_task_frameEmitE(inFrame,
				error, NULL, "%s %" PRId32 "",
				(SJME_AS_JARRAY(arrayValue.v.l) == NULL ?
					"null" : "not-null"),
				indexValue.v.i)))
				return sjme_error_vmError(inFrame, error);
		
			/* Leave early. */
			goto skip_tossed;
		}
		
		/* This is not good. */
		return sjme_error_default(error);
	}
	
	/* Push. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &commit, &pushValue)))
		return sjme_error_vmError(inFrame, error);

skip_tossed:
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()

#pragma region(XAStore)
SJME_NVM_BYTECODE_SLOW(XAStore)
{
	sjme_jvalueTyped popValue;
	sjme_jvalueTyped arrayValue;
	sjme_jvalueTyped indexValue;
	sjme_basicTypeId arrayType;
	sjme_javaTypeId promoteType;
	sjme_nvm_frame_gcCommit commit;
	SJME_NVM_BYTECODE_ENTRY;

	/* Determine the type to read from the stack and to store to the array. */
	arrayType = sjme_nvm_byteCode_xArrayType[
		abs(id - 79) % SJME_NVM_NUM_XARRAY];
	promoteType = sjme_nvm_typePromote[arrayType];
	
	/* Clear the commit. */
	memset(&commit, 0, sizeof(commit));

	/* Read in value, index, and array. */
	memset(&popValue, 0, sizeof(popValue));
	memset(&indexValue, 0, sizeof(indexValue));
	memset(&arrayValue, 0, sizeof(arrayValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		promoteType, &commit, &popValue)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &commit, &indexValue)))
		return sjme_error_vmError(inFrame, error);
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &commit, &arrayValue)))
		return sjme_error_vmError(inFrame, error);
	
	/* Write to the array. */
	if (sjme_error_is(error = sjme_nvm_instance_arraySet(
		SJME_F_T(inFrame), SJME_AS_JARRAY(arrayValue.v.l),
		arrayType, indexValue.v.i, &commit, &popValue)))
	{
		/* Is this an applicable error? */
		if (sjme_error_isOneOf(error,
			SJME_ERROR_ARRAY_STORE,
			SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS,
			SJME_ERROR_NULL_STACK_POINTER,
			0))
		{
			/* Emit exception. */
			if (sjme_error_is(error = sjme_nvm_task_frameEmitE(inFrame,
				error, NULL, "%s %" PRId32 "",
				(SJME_AS_JARRAY(arrayValue.v.l) == NULL ?
					"null" : "not-null"),
				indexValue.v.i)))
				return sjme_error_vmError(inFrame, error);
		
			/* Leave early. */
			goto skip_tossed;
		}
		
		/* This is not good. */
		return sjme_error_default(error);
	}
	
skip_tossed:
	/* Commit GC. */
	if (sjme_error_is(error = sjme_nvm_task_frameCommit(inFrame, &commit)))
		return sjme_error_vmError(inFrame, error);
		
	/* Success? */
	SJME_NVM_BYTECODE_EXIT;
}
#pragma endregion()
