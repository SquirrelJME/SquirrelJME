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

static const sjme_basicTypeId sjme_nvm_byteCode_xArrayType[8] =
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

static sjme_errorCode sjme_nvm_byteCode_slowInvokeAny(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInRange(0, SJME_NVM_NUM_METHOD_CALL_TYPE)
		sjme_nvm_methodCallType callType,
	sjme_attrInNotNull sjme_charSeq binaryName,
	sjme_attrInNotNull sjme_charSeq methodName,
	sjme_attrInNotNull sjme_charSeq methodType)
{
	sjme_errorCode error;
	sjme_jclass classy;
	sjme_nvm_frame newFrame;
	sjme_jint argC;
	sjme_jvalueTyped* argV;
	sjme_jvalueTyped* argVParam;
	sjme_jmethodID methodId;
	sjme_nvm_class_methodInfo target;
	sjme_jvalueTyped mleArgR;
	sjme_jboolean callOkay, isStatic;

	if (inFrame == NULL || binaryName == NULL || methodName == NULL ||
		methodType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (callType < 0 || callType >= SJME_NVM_NUM_METHOD_CALL_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Locate target class. */
	classy = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		inFrame->inThread->inTask->classLoader,
		&classy,
		inFrame->inThread,
		binaryName,
		SJME_JNI_TRUE)) || classy == NULL)
		return sjme_error_vmError(inFrame, error);
	
	/* Locate method to execute, it is required to be found. */
	methodId = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
		classy, inFrame->inThread,
		instanceType, SJME_JNI_TRUE,
		methodName, methodType, &methodId)) || methodId == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Check permissions to call the target. */
	callOkay = SJME_JNI_FALSE;
	if (sjme_error_is(error = sjme_nvm_instance_checkPermission(
		inFrame->inClass, SJME_AS_JMEMBERID(methodId), &callOkay)) ||
		!callOkay)
		return sjme_error_vmError(inFrame, sjme_error_defaultOr(error,
			SJME_ERROR_CLASS_CHANGED));

	/* Get the non-virtual target info. */
	target = methodId->info[callType];

	/* Static-ness is wrong? */
	isStatic = target->flags.member.isStatic;
	if (isStatic && instanceType != SJME_NVM_CLASS_MEMBER_STATIC &&
		callType != SJME_NVM_CALL_NON_VIRTUAL)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Allocate pushed arguments. */
	argC = target->argC + (!isStatic ? 1 : 0);
	argV = sjme_alloca(sizeof(*argV) * (argC + 2));
	if (argV == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Pull in stack arguments for the call. */
	argVParam = (!isStatic ? &argV[1] : argV);
	if (target->argC != 0)
		if (sjme_error_is(error = sjme_nvm_task_frameStackPopA(
			inFrame, target->argC, target->argT, argVParam)))
			return sjme_error_vmError(inFrame, error);

	/* Pop instance. */
	if (!isStatic)
	{
		/* Pop. */
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(
			inFrame, SJME_JAVA_TYPE_ID_OBJECT, &argV[0])))
			return sjme_error_vmError(inFrame, error);

		/* Cannot be null. */
		if (argV[0].value.l == NULL)
			return sjme_error_vmError(inFrame,
				SJME_ERROR_INVALID_REFERENCE_POP);
	}

	/* If native, perform an MLE call. */
	if (target->flags.native && isStatic)
	{
		/* Perform the native call. */
		memset(&mleArgR, 0, sizeof(mleArgR));
		mleArgR.type = SJME_JAVA_TYPE_ID_VOID;
		if (sjme_error_is(error = sjme_mle_mleCall(inFrame,
			classy->binaryName,
			target->name->seq,
			target->type->seq,
			&mleArgR,
			argC, argV)))
			return sjme_error_vmError(inFrame, error);

		/* Wrong type? */
		if (mleArgR.type != target->argR)
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_METHOD_TYPE);

		/* Is there a return value being pushed to the stack? */
		if (mleArgR.type != SJME_JAVA_TYPE_ID_VOID)
		{
			/* Count up if an object. */
			if (mleArgR.type == SJME_JAVA_TYPE_ID_OBJECT &&
				mleArgR.value.l != NULL)
				if (sjme_error_is(error = sjme_alloc_weakRef(
					mleArgR.value.l, NULL)))
					return sjme_error_vmError(inFrame, error);
			
			/* Push */
			if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
				inFrame, &mleArgR)))
				return sjme_error_vmError(inFrame, error);
		}
	}

	/* Enter new stack frame for the target method, or at least try. */
	else
	{
		newFrame = NULL;
		if (sjme_error_is(error = sjme_nvm_task_threadEnter(
			inFrame->inThread,
			&newFrame,
			methodId,
			callType,
			argC, argV)) || newFrame == NULL)
			return sjme_error_vmError(inFrame, error);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_BYTECODE_SLOW(ArrayLength)
{
	sjme_jarray array;
	sjme_jvalueTyped value, result;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 1;

	/* Pop single object value. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &value)))
		return sjme_error_vmError(inFrame, error);

	/* Cannot be null. */
	if (value.value.l == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Must be an array type. */
	array = SJME_AS_JARRAY(value.value.l);
	if (!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE))
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Push length onto the stack. */
	memset(&result, 0, sizeof(result));
	result.type = SJME_JAVA_TYPE_ID_INTEGER;
	result.value.i = array->length;
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(inFrame,
		&result)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(CheckCast)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_class_poolEntryClass* classRef;
	sjme_jclass desireClass;
	sjme_charSeq binaryName;
	sjme_jvalueTyped value;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_CLASS,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Which class are we going for? */
	classRef = &entry->classRef;
	binaryName = classRef->descriptor->seq;
	
	/* Locate target class. */
	desireClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		inFrame->inThread->inTask->classLoader,
		&desireClass,
		inFrame->inThread,
		binaryName,
		SJME_JNI_TRUE)) || desireClass == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Pop object from the stack. */
	memset(&value, 0, sizeof(value));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPeek(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &value, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, error);

	/* Not a match? */
	/* b.getClass().isAssignableFrom(a.getClass()) == (a instanceof b) */
	if (value.value.l != NULL &&
		!(value.value.l->isClass == desireClass ||
		sjme_nvm_vmClass_isAssignableFrom(desireClass,
			value.value.l->isClass)))
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(InvokeStatic)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_class_poolEntryMember* member;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_METHOD,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Perform the invocation. */
	member = &entry->member;
	if (sjme_error_is(error = sjme_nvm_byteCode_slowInvokeAny(inFrame,
		SJME_NVM_CLASS_MEMBER_STATIC,
		SJME_NVM_CALL_NON_VIRTUAL,
		member->inClass->descriptor->seq,
		member->nameAndType->name->seq,
		member->nameAndType->descriptor->seq)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(InvokeVirtual)
{
	sjme_jint poolIndex;
	sjme_nvm_class_poolEntry* entry;
	sjme_nvm_class_poolEntryMember* member;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	/* Read in pool reference. */
	poolIndex = sjme_big_ushort(*sjme_util_memUnaligned16(&relRawCode[1]));
	if (sjme_error_is(error = sjme_nvm_task_framePool(
		inFrame, poolIndex, &entry,
		SJME_NVM_CLASS_POOL_TYPE_METHOD,
		0)))
		return sjme_error_vmError(inFrame, error);

	/* Perform the invocation. */
	member = &entry->member;
	if (sjme_error_is(error = sjme_nvm_byteCode_slowInvokeAny(inFrame,
		SJME_NVM_CLASS_MEMBER_INSTANCE,
		SJME_NVM_CALL_VIRTUAL,
		member->inClass->descriptor->seq,
		member->nameAndType->name->seq,
		member->nameAndType->descriptor->seq)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(New)
{
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 3;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}

SJME_NVM_BYTECODE_SLOW(XALoad)
{
	sjme_jvalueTyped arrayValue;
	sjme_jvalueTyped indexValue;
	sjme_jvalueTyped pushValue;
	sjme_jarray array;
	sjme_jint index;
	sjme_basicTypeId arrayType;
	sjme_jclass componentType;
	SJME_NVM_BYTECODE_SLOW_ENTRY;

	/* PC adjustment. */
	pcNew->adjust = 1;

	/* Read in index and array. */
	memset(&indexValue, 0, sizeof(indexValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_INTEGER, &indexValue)))
		return sjme_error_vmError(inFrame, error);
	memset(&arrayValue, 0, sizeof(arrayValue));
	if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
		SJME_JAVA_TYPE_ID_OBJECT, &arrayValue)))
		return sjme_error_vmError(inFrame, error);

	/* Must not be null. */
	array = SJME_AS_JARRAY(arrayValue.value.l);
	if (array == NULL)
		return sjme_error_vmError(inFrame, SJME_ERROR_NULL_STACK_POINTER);

	/* Make sure the array is actually valid. */
	arrayType = sjme_nvm_byteCode_xArrayType[id - 46];
	componentType = sjme_atomic_sjme_jclass_get(
		&array->object.isClass->componentType);
	if (!sjme_nvm_isAR(array, SJME_NVM_STRUCT_ARRAY_INSTANCE) ||
		!array->object.isClass->info->isArray ||
		componentType == NULL || componentType->arrayTypeId != arrayType)
		return sjme_error_vmError(inFrame, SJME_ERROR_CLASS_CHANGED);

	/* Check bounds. */
	index = indexValue.value.i;
	if (index < 0 || index >= array->length)
		return sjme_error_vmError(inFrame,
			SJME_ERROR_ARRAY_INDEX_OUT_OF_BOUNDS);

	/* Load value to push. */
	memset(&pushValue, 0, sizeof(pushValue));
	pushValue.type = componentType->typeId;
	switch (componentType->arrayTypeId)
	{
		case SJME_BASIC_TYPE_ID_BOOLEAN:
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
			
		case SJME_BASIC_TYPE_ID_BYTE:
			pushValue.value.i =
				((sjme_jint)array->elements.b[index]) & INT32_C(0xFF);
			if ((pushValue.value.i & INT32_C(0x80)) != 0)
				pushValue.value.i |= INT32_C(0xFFFFFF00);
			break;
			
		case SJME_BASIC_TYPE_ID_SHORT:
			pushValue.value.i =
				((sjme_jint)array->elements.s[index]) & INT32_C(0xFFFF);
			if ((pushValue.value.i & INT32_C(0x8000)) != 0)
				pushValue.value.i |= INT32_C(0xFFFF0000);
			break;
			
		case SJME_BASIC_TYPE_ID_CHARACTER:
			pushValue.value.i =
				((sjme_jint)array->elements.c[index]) & INT32_C(0xFFFF);
			break;
			
		case SJME_JAVA_TYPE_ID_INTEGER:
			pushValue.value.i = array->elements.i[index];
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			pushValue.value.j = array->elements.j[index];
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			pushValue.value.f = array->elements.f[index];
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			pushValue.value.d = array->elements.d[index];
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			pushValue.value.l = array->elements.l[index];

			/* Count up if not null as it is now on the stack. */
			if (pushValue.value.l != NULL)
				if (sjme_error_is(error = sjme_alloc_weakRef(
					pushValue.value.l, NULL)))
					return sjme_error_vmError(inFrame, error);
			break;

		default:
			return sjme_error_vmError(inFrame,
				SJME_ERROR_STACK_INVALID_WRITE);
	}

	/* Push. */
	if (sjme_error_is(error = sjme_nvm_task_frameStackPush(
		inFrame, &pushValue)))
		return sjme_error_vmError(inFrame, error);
	
	/* Success? */
	SJME_NVM_BYTECODE_SLOW_EXIT;
}
