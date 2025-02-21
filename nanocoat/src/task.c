/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/util.h"
#include "sjme/nvm/instance.h"
#include "sjme/stdGone.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/loop.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/cleanup.h"

/** The number of tasks to grow by. */
#define SJME_NVM_TASK_GROW 4

/** The number of threads to grow by. */
#define SJME_NVM_THREAD_GROW 8

/** The size of the thread stack. */
#define SJME_NVM_THREAD_STACK_SIZE 32768

/** Type size multiplier. */
static const sjme_jint sjme_nvm_typeMul[SJME_NUM_JAVA_TYPE_IDS] =
{
	sizeof(sjme_jint),
	sizeof(sjme_jlong),
	sizeof(sjme_jfloat),
	sizeof(sjme_jdouble),
	sizeof(sjme_jobject),
};

static sjme_errorCode sjme_nvm_task_countObjectDown(
	sjme_attrInNotNull sjme_jobject* oldP,
	sjme_attrInNotNull sjme_jobject newV)
{
	sjme_errorCode error;
	sjme_jobject oldObject;
	sjme_jboolean validObject, noSelfGc;
	sjme_alloc_weak weak;

	if (oldP == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If the old is the same as new, then do not count if the count would */
	/* result in the object being GCed before it was set. */
	oldObject = *oldP;
	noSelfGc = SJME_JNI_FALSE;
	if (oldObject != NULL && newV != NULL && oldObject == newV)
	{
		/* Only consider valid weak reference. */
		weak = NULL;
		if (sjme_error_is(error = sjme_alloc_weakRefGet(oldObject, &weak)))
		{
			if (error != SJME_ERROR_NOT_WEAK_REFERENCE)
				return sjme_error_default(error);
		}

		/* Do not self GC if it would end up freeing the object before it */
		/* could be set. */
		noSelfGc = (sjme_atomic_sjme_jint_get(&weak->count) <= 1);
	}

	/* Count down if the old object exists, or in the case as above. */
	if (oldObject != NULL && !noSelfGc)
	{
		/* Is this object actually valid? */
		validObject = SJME_JNI_FALSE;
		if (sjme_error_is(error = sjme_nvm_isA(oldObject,
			SJME_NVM_STRUCT_OBJECT_INSTANCE,
			&validObject)))
			return sjme_error_default(error);

		/* Count it down. */
		if (sjme_error_is(error = sjme_alloc_weakUnRef(oldObject)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_task_stackReframe(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_class_methodInfo targetInfo)
{
	sjme_errorCode error;
	sjme_nvm_class_codeInfo code;
	sjme_frame_threadStacks* store;
	sjme_frame_frameStacks* stack;
	sjme_nvm_class_codePerType* perType;
	sjme_frame_frameStack* typeStack;
	sjme_jint i;
	sjme_intPointer typeOff[SJME_NUM_CODE_TYPE_IDS];
	sjme_pointer storeBase;
	
	if (inState == NULL || inThread == NULL || inFrame == NULL ||
		targetInfo == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get source and target framing information. */
	store = &inThread->stack;
	stack = &inFrame->stack;

	/* Make sure it is cleared beforehand. */
	memset(stack, 0, sizeof(*stack));

	/* The ordering information can be taken directly from the code info. */
	code = targetInfo->code;
	stack->orderFront = code->perType[SJME_JAVA_TYPE_ID_ALL].locals;
	stack->orderTop = stack->orderFront;
	stack->orderLength = stack->orderFront +
		code->perType[SJME_JAVA_TYPE_ID_ALL].stack;

	/* Determine initial offset to store ordering information. */
	typeOff[0] = sjme_util_alignTo(
		sizeof(*stack->order) * stack->orderLength,
		sizeof(sjme_pointer));

	/* Determine the totals for each type. */
	for (i = 0; i < SJME_NUM_JAVA_TYPE_IDS; i++)
	{
		perType = &code->perType[i];
		typeStack = &stack->stack[i];

		/* Determine totals for per types. */
		typeStack->front = perType->locals;
		typeStack->top = typeStack->front;
		typeStack->length = typeStack->front + perType->stack;

		/* The offset for the next type is the total storage for this type. */
		typeOff[i + 1] = sjme_util_alignTo(
			sjme_util_alignTo(typeOff[i], sjme_nvm_typeMul[i]) +
			(sjme_nvm_typeMul[i] * typeStack->length),
			sizeof(sjme_pointer));
	}

	/* Is there enough memory to even allocate this big of a stack? */
	if (store->storageTop + typeOff[SJME_JAVA_TYPE_ID_ALL] > store->storageLen)
		return SJME_ERROR_OUT_OF_MEMORY;

	/* Grab a chunk of the stack. */
	storeBase = SJME_POINTER_OFFSET(store->storage, store->storageTop);
	stack->storageClaim = typeOff[SJME_JAVA_TYPE_ID_ALL];
	store->storageTop += typeOff[SJME_JAVA_TYPE_ID_ALL];

	/* Setup pointers. */
	stack->order = SJME_POINTER_OFFSET(storeBase, 0);
	for (i = 0; i < SJME_NUM_JAVA_TYPE_IDS; i++)
		stack->stack[i].base.base = SJME_POINTER_OFFSET(storeBase, typeOff[i]);

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_task_valueCompose(
	sjme_attrInOutNotNull sjme_jvalueTyped* inOutValue,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId valueType,
	sjme_attrInPositive sjme_jint stackIndex,
	sjme_attrInNotNull sjme_frame_frameStack* stack)
{
	if (inOutValue == NULL || stack == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (valueType < 0 || valueType >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (stackIndex < 0 || stackIndex >= stack->length)
		return SJME_ERROR_TREAD_INDEX_INVALID;

	/* Mapping index depends on the type. */
	inOutValue->type = valueType;
	switch (valueType)
	{
		case SJME_JAVA_TYPE_ID_INTEGER:
			inOutValue->value.i = stack->base.jints[stackIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			inOutValue->value.j = stack->base.jlongs[stackIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			inOutValue->value.f = stack->base.jfloats[stackIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			inOutValue->value.d = stack->base.jdoubles[stackIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			inOutValue->value.l = stack->base.jobjects[stackIndex];
			break;
			
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_frameLocalPush(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInValue sjme_javaTypeId localType,
	sjme_attrInPositive sjme_jint localIndex)
{
	sjme_errorCode error;
	sjme_nvm_class_codePerType* perType;
	sjme_jint mappedSlot;
	sjme_jvalueTyped value;
	
	if (inFrame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (localType < 0 || localType >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (localIndex < 0 ||
		localIndex >= inFrame->inCode->perType[SJME_JAVA_TYPE_ID_ALL].locals)
		return sjme_error_vmError(inFrame, SJME_ERROR_LOCAL_INDEX_INVALID);

	/* The local variable is of the wrong type. */
	if (inFrame->stack.order[localIndex] != localType)
		return sjme_error_vmError(inFrame, SJME_ERROR_LOCAL_INVALID_READ);

	/* Determine where this maps from for the read. */
	perType = &inFrame->inCode->perType[localType];
	mappedSlot = perType->localMap[localIndex];
	if (mappedSlot < 0 || mappedSlot > perType->locals)
		return sjme_error_vmError(inFrame, SJME_ERROR_TREAD_INDEX_INVALID);

	/* Forward to stack push. */
	if (sjme_error_is(error = sjme_nvm_task_valueCompose(&value,
		localType, mappedSlot, &inFrame->stack.stack[localType])))
		return sjme_error_vmError(inFrame, error);
	return sjme_nvm_task_frameStackPush(inFrame, &value);
}

sjme_errorCode sjme_nvm_task_frameLocalSetL(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint localIndex,
	sjme_attrInNotNull const sjme_jvalueTyped* inValue)
{
	sjme_errorCode error;
	sjme_jboolean isWide;
	sjme_nvm_class_codePerType* perType;
	sjme_jint mappedSlot;
	sjme_frame_frameStacks* stack;
	
	if (inFrame == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inValue->type < 0 || inValue->type >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Is this wide? */
	isWide = SJME_TYPEID_IS_WIDE(inValue->type);

	/* Check for complete out of bounds. */
	if (localIndex < 0 ||
		((localIndex + (isWide ? 1 : 0)) >=
			inFrame->inCode->perType[SJME_JAVA_TYPE_ID_ALL].locals))
		return sjme_error_vmError(inFrame, SJME_ERROR_LOCAL_INDEX_INVALID);

	/* Is the index still valid on the tread? */
	perType = &inFrame->inCode->perType[inValue->type];
	mappedSlot = perType->localMap[localIndex];
	if (mappedSlot < 0 || mappedSlot >= perType->locals)
		return sjme_error_vmError(inFrame, SJME_ERROR_TREAD_INDEX_INVALID);

	/* Set tread value. */
	if (sjme_error_is(error = sjme_nvm_task_frameTreadSetT(inFrame,
		mappedSlot, inValue)))
		return sjme_error_vmError(inFrame, error);

	/* Replace order info. */
	stack = &inFrame->stack;
	stack->order[localIndex] = inValue->type;
	if (isWide)
		stack->order[localIndex + 1] = SJME_JAVA_TYPE_ID_VOID;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_framePool(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositiveNonZero sjme_jint poolIndex,
	sjme_attrOutNotNull sjme_nvm_class_poolEntry** outEntry,
	sjme_attrInRange(0, SJME_NUM_CLASS_POOL_TYPE)
		sjme_nvm_class_poolType inType,
	sjme_attrInRange(0, SJME_NUM_CLASS_POOL_TYPE)
		sjme_nvm_class_poolType inTypeB,
	...)
{
	sjme_list_sjme_nvm_class_poolEntry* pool;
	sjme_nvm_class_poolEntry* result;
	sjme_jint argType;
	va_list arg;
	
	if (inFrame == NULL || outEntry == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Is the index valid? */
	pool = inFrame->pool->pool;
	if (poolIndex <= 0 || poolIndex >= pool->length)
		return sjme_error_vmError(inFrame,
			SJME_ERROR_INVALID_CLASS_POOL_INDEX);

	/* Get entry here, check for base validity. */
	result = &pool->elements[poolIndex];
	if (result->type == inType)
		goto skip_success;

	/* Check second validity, if not zero. */
	if (inTypeB == 0)
		goto fail_notMatched;
	else if (result->type == inTypeB)
		goto skip_success;

	/* Check continual multi-type checks, until zero */
	for (va_start(arg, inTypeB);;)
	{
		/* Read in. */
		argType = va_arg(arg, int);

		/* Not matched? */
		if (argType == 0)
		{
			va_end(arg);
			goto fail_notMatched;
		}

		/* Matched? */
		if (result->type == argType)
		{
			va_end(arg);
			break;
		}
	}
	
skip_success:
	*outEntry = result;
	return SJME_ERROR_NONE;

fail_notMatched:
	return sjme_error_vmError(inFrame,
		SJME_ERROR_WRONG_CLASS_POOL_INDEX_TYPE);
}

sjme_errorCode sjme_nvm_task_frameStackPeek(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInNotNull sjme_jvalueTyped* outValue,
	sjme_attrInValue sjme_jboolean copiedElsewhere)
{
	sjme_errorCode error;
	sjme_frame_frameStacks* stack;
	sjme_jboolean isWide;
	sjme_jint peekTop, peekPerTop;
	sjme_frame_frameStack* perType;
	sjme_jvalueTyped tempValue;
	
	if (inFrame == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (typeId < 0 || typeId >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Is this wide? */
	isWide = SJME_TYPEID_IS_WIDE(typeId);

	/* Determine top of the stack, check for underflow. */
	stack = &inFrame->stack;
	peekTop = stack->orderTop - (isWide ? 2 : 1);
	if (peekTop < stack->orderFront)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_UNDERFLOW);

	/* Is wide and very top is wrong. */
	if (isWide && stack->order[peekTop + 1] != SJME_JAVA_TYPE_ID_VOID)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_INVALID_READ);

	/* Top of the stack is the wrong type? */
	if (stack->order[peekTop] != typeId)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_INVALID_READ);

	/* Determine per type slot to peek. */
	perType = &stack->stack[typeId];
	peekPerTop = perType->top - 1;
	if (peekPerTop < perType->front)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_UNDERFLOW);

	/* Read in value. */
	memset(&tempValue, 0, sizeof(tempValue));
	if (sjme_error_is(error = sjme_nvm_task_frameTreadGetT(
		inFrame, typeId, peekPerTop, &tempValue, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, sjme_error_defaultOr(error,
			SJME_ERROR_STACK_INVALID_READ));

	/* If copied elsewhere, count object up. */
	if (copiedElsewhere && typeId == SJME_JAVA_TYPE_ID_OBJECT &&
		tempValue.value.l != NULL)
		if (sjme_error_is(error = sjme_alloc_weakRef(tempValue.value.l, NULL)))
			return sjme_error_default(error);

	/* Success! */
	memmove(outValue, &tempValue, sizeof(*outValue));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_frameStackPop(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInNotNull sjme_jvalueTyped* outValue)
{
	sjme_errorCode error;
	sjme_frame_frameStacks* stack;
	sjme_jboolean isWide;
	sjme_jint newTop, newPerTop;
	sjme_frame_frameStack* perType;
	
	if (inFrame == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (typeId < 0 || typeId >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Is this wide? */
	isWide = SJME_TYPEID_IS_WIDE(typeId);

	/* Determine new top of the stack, check for underflow. */
	stack = &inFrame->stack;
	newTop = stack->orderTop - (isWide ? 2 : 1);
	if (newTop < stack->orderFront)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_UNDERFLOW);

	/* Is wide and very top is wrong. */
	if (isWide && stack->order[newTop + 1] != SJME_JAVA_TYPE_ID_VOID)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_INVALID_READ);

	/* Top of the stack is the wrong type? */
	if (stack->order[newTop] != typeId)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_INVALID_READ);

	/* Determine per type slot to remove. */
	perType = &stack->stack[typeId];
	newPerTop = perType->top - 1;
	if (newPerTop < perType->front)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_UNDERFLOW);

	/* Read in value. */
	if (sjme_error_is(error = sjme_nvm_task_frameTreadGetT(
		inFrame, typeId, newPerTop, outValue, SJME_JNI_TRUE)))
		return sjme_error_vmError(inFrame, sjme_error_defaultOr(error,
			SJME_ERROR_STACK_INVALID_READ));

	/* Remove from stack, from the main and on the per-type. */
	/* Cleanup any values as well. */
	stack->order[newTop] = SJME_JAVA_TYPE_ID_VOID;
	stack->orderTop = newTop;
	perType->top = newPerTop;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_frameStackPopA(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNotNullBuf(argC) sjme_javaTypeId* argT,
	sjme_attrInNotNullBuf(argC) sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_jint i;
	
	if (inFrame == NULL || argT == NULL || argV == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (argC < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Always pop from the end first. */
	for (i = argC - 1; i >= 0; i--)
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			argT[i], &argV[i])))
			return sjme_error_vmError(inFrame, error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_frameStackPush(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jvalueTyped* inValue)
{
	sjme_frame_frameStacks* stack;
	sjme_frame_frameStack* perType;
	sjme_jint pushCount, at;
	sjme_jboolean isWide;
	
	if (inFrame == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Will the stack overflow? */
	stack = &inFrame->stack;
	isWide = SJME_TYPEID_IS_WIDE(inValue->type);
	pushCount = (isWide ? 2 : 1);
	if (stack->orderTop + pushCount > stack->orderLength)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_OVERFLOW);

	/* Will the per-type stack overflow? */
	perType = &stack->stack[inValue->type];
	if (perType->top + 1 > perType->length)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_OVERFLOW);

	/* Place onto the order, mark top invalid if required. */
	stack->order[stack->orderTop++] = inValue->type;
	if (isWide)
		stack->order[stack->orderTop++] = SJME_JAVA_TYPE_ID_VOID;

	/* Take slot in the per-type stack. */
	at = perType->top++;
	
	/* Forward call. */
	return sjme_nvm_task_frameTreadSetT(inFrame,
		at, inValue);
}

sjme_errorCode sjme_nvm_task_frameStackPushClassPD(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_stringPool_string inClassName)
{
	if (inFrame == NULL || inClassName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_task_frameStackPushStringP(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_nvm_stringPool_string inString)
{
	sjme_errorCode error;
	sjme_jvalueTyped value;
	
	if (inFrame == NULL || inString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Load in string. */
	memset(&value, 0, sizeof(value));
	value.type = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfP(
		inFrame->inThread,
		SJME_AS_NVM_JSTRINGP(&value.value.l), inString)) ||
		value.value.l == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Count up string. */
	if (sjme_error_is(error = sjme_alloc_weakRef(value.value.l, NULL)))
		return sjme_error_vmError(inFrame, error);

	/* Push value. */
	return sjme_nvm_task_frameStackPush(inFrame, &value);
}

sjme_errorCode sjme_nvm_task_frameTreadGetT(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInPositive sjme_jint typeIndex,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue,
	sjme_attrInValue sjme_jboolean eraseOld)
{
	sjme_errorCode error;
	sjme_frame_frameStack* perType;
	sjme_jobject tempObject;
	
	if (inFrame == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (typeId < 0 || typeId >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Obtain the per type. */
	perType = &inFrame->stack.stack[typeId];

	/* Check the tread index. */
	if (typeIndex < 0 || typeIndex >= perType->length)
		return sjme_error_vmError(inFrame, SJME_ERROR_TREAD_INDEX_INVALID);
	
	/* Operating depends on the type. */
	switch (typeId)
	{
		case SJME_JAVA_TYPE_ID_INTEGER:
			outValue->value.i = perType->base.jints[typeIndex];
		
			if (eraseOld)
				perType->base.jints[typeIndex] = 0;
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			outValue->value.j = perType->base.jlongs[typeIndex];
		
			if (eraseOld)
				memset(&perType->base.jlongs[typeIndex], 0,
					sizeof(sjme_jlong));
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			outValue->value.f = perType->base.jfloats[typeIndex];
		
			if (eraseOld)
				memset(&perType->base.jfloats[typeIndex], 0,
					sizeof(sjme_jfloat));
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			outValue->value.d = perType->base.jdoubles[typeIndex];
		
			if (eraseOld)
				memset(&perType->base.jdoubles[typeIndex], 0,
					sizeof(sjme_jdouble));
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			/* Load into temporary as we may be erasing the value here. */
			tempObject = perType->base.jobjects[typeIndex];

			/* Is the value in the tread being cleared? */
			if (eraseOld)
				perType->base.jobjects[typeIndex] = NULL;

			/* Otherwise, we technically have a copy so count up. */
			else if (tempObject != NULL)
				if (sjme_error_is(error = sjme_alloc_weakRef(tempObject,
					NULL)))
					return sjme_error_default(error);

			outValue->value.l = tempObject;
			break;
			
		default:
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_FIELD_TYPE);
	}

	/* Success! */
	outValue->type = typeId;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_frameTreadSetT(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint typeIndex,
	sjme_attrInNotNull const sjme_jvalueTyped* inValue)
{
	sjme_errorCode error;
	sjme_frame_frameStack* perType;
	
	if (inFrame == NULL || inValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inValue->type < 0 || inValue->type >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Obtain the per type. */
	perType = &inFrame->stack.stack[inValue->type];

	/* Check the tread index. */
	if (typeIndex < 0 || typeIndex >= perType->length)
		return sjme_error_vmError(inFrame, SJME_ERROR_TREAD_INDEX_INVALID);
	
	/* Operating depends on the type. */
	switch (inValue->type)
	{
		case SJME_JAVA_TYPE_ID_INTEGER:
			perType->base.jints[typeIndex] = inValue->value.i;
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			perType->base.jlongs[typeIndex] = inValue->value.j;
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			perType->base.jfloats[typeIndex] = inValue->value.f;
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			perType->base.jdoubles[typeIndex] = inValue->value.d;
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			/* If there is an old value here, count it down. */
			if (sjme_error_is(error = sjme_nvm_task_countObjectDown(
				&perType->base.jobjects[typeIndex],
				inValue->value.l)))
				return sjme_error_vmError(inFrame, error);

			/* Set. */
			perType->base.jobjects[typeIndex] = inValue->value.l;
			break;
			
		default:
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_FIELD_TYPE);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_stackTrace(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_nvm_frame frame;
	sjme_jint i, instructionId;
	sjme_jclass lastClass, nowClass;
	sjme_nvm_class_codeInfo nowCode;
	sjme_nvm_class_methodInfo nowMethod;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Empty? Do nothing. */
	if (inThread->numFrames == 0)
		return SJME_ERROR_NONE;

	/* The compact SquirrelJME format is in the following form: */
 	/*  | IN java.lang.Class (Class.java) */
  	/*  |- .whatever:(Lboop;)V @0h (:181 INVOKEINTERFACE@15) */

	/* Start from the top of the stack. */
	lastClass = NULL;
	for (i = inThread->numFrames - 1; i >=0; i--)
	{
		/* Which frame is this? */
		frame = inThread->frames->elements[i];

		/* Did the class change? */
		/* | IN java.lang.Class (Class.java) */
		nowClass = frame->inClass;
		if (nowClass != lastClass)
			sjme_messageB(" | IN %s (%s)",
				nowClass->binaryName, "<UNKNOWN>");

		/* Print method trace. */
		/*  |- .whatever:(Lboop;)V @0h (:181 INVOKEINTERFACE@15) */
		nowCode = frame->inCode;
		nowMethod = (nowCode != NULL ? frame->inCode->inMethod : NULL);
		instructionId = (nowCode != NULL && frame->pc >= 0 &&
			frame->pc < nowCode->rawCodeLen ?
			nowCode->rawCode[frame->pc] & 0xFF : -1);
		if (nowCode == NULL || nowMethod == NULL)
			sjme_messageB(" | PURE VIRTUAL");
		else
			sjme_messageB(" | .%s:%s @%xh (:%d #%02x@%d)",
				&nowMethod->name->chars[0],
				&nowMethod->type->chars[0],
				frame->pc,
				-1,
				instructionId,
				frame->pc);

		/* Set for next run. */
		lastClass = nowClass;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_taskNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull const sjme_nvm_task_taskNewConfig* startConfig,
	sjme_attrOutNullable sjme_nvm_task* outTask)
{
	sjme_errorCode error;
	sjme_list_sjme_nvm_task* tasks;
	sjme_list_sjme_nvm_thread* threads;
	sjme_jint i, n, freeSlot;
	sjme_nvm_task result;
	sjme_nvm_thread mainThread;
	sjme_nvm_vmClass_loader classLoader;
	sjme_nvm_taskStrings strings;

	if (inState == NULL || startConfig == NULL || outTask == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (startConfig->mainClass == NULL || startConfig->classPath == NULL ||
		startConfig->classPath->length <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Start Main: %s", startConfig->mainClass);

	if (startConfig->mainArgs != NULL)
		for (i = 0; i < startConfig->mainArgs->length; i++)
			sjme_message("Start Arg[%d]: %s",
				i, startConfig->mainArgs->elements[i]);

	if (startConfig->sysProps != NULL)
		for (i = 0; i < startConfig->sysProps->length; i++)
			sjme_message("Start SysProp[%d]: %s",
				i, startConfig->sysProps->elements[i]);
#endif
	
	/* Lock state for task access. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inState->common.lock)))
		return sjme_error_default(error);
	
	/* Find a free slot to claim for a new task. */
	freeSlot = -1;
	tasks = inState->tasks;
	if (tasks != NULL)
		for (i = 0, n = tasks->length; i < n; i++)
			if (tasks->elements[0] == NULL)
			{
				freeSlot = -1;
				break;
			}
	
	/* Need to allocate or grow the task list? */
	if (freeSlot < 0)
	{
		/* Either allocate or grow. */
		if (tasks == NULL)
		{
			/* Allocate. */
			if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
				SJME_NVM_TASK_GROW, &tasks, sjme_nvm_task, 0)) ||
				tasks == NULL)
				goto fail_allocTasks;
			
			/* Free slot is always at the start. */
			freeSlot = 0;
		}
		else
		{
			/* Copy everything over. */
			if (sjme_error_is(error = sjme_list_copy(inState->allocPool,
				tasks->length + SJME_NVM_TASK_GROW, tasks, &tasks,
				sjme_nvm_task, 0)))
				goto fail_allocTasks;
			
			/* Free slot is always at the end. */
			freeSlot = tasks->length;
			
			/* Destroy old list. */
			if (sjme_error_is(error = sjme_alloc_free(inState->tasks)))
				goto fail_allocTasks;
			inState->tasks = NULL;
		}
		
		/* Store for later usage. */
		inState->tasks = tasks;
	}
	
	/* Allocate new task to start. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState,
		sizeof(*result), SJME_NVM_STRUCT_TASK,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;

	/* Allocate strings. */
	strings = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState,
		sizeof(*result), SJME_NVM_STRUCT_TASK_STRINGS,
		SJME_AS_NVM_COMMONP(&strings))) || strings == NULL)
		goto fail_allocStrings;
	
	/* Initialize a new class loader for the current classpath. */
	classLoader = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderNew(
		inState, &classLoader,
		startConfig->classPath)) || classLoader == NULL)
		goto fail_initClassLoader;
	
	/* Refer to owning state and set identifier. */
	result->inState = inState;
	result->classLoader = classLoader;
	result->id = 1 + sjme_atomic_sjme_jint_getAdd(
		&inState->nextTaskId, 1);
	result->strings = strings;
	
	/* All new tasks are considered alive. */
	result->status = SJME_NVM_TASK_STATUS_ALIVE;
	
	/* Setup thread storage. */
	if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
		SJME_NVM_THREAD_GROW, &threads, sjme_nvm_thread, 0)) ||
		threads == NULL)
		goto fail_allocThreads;
	result->threads = threads;
	
	/* Task is considered valid now, so store it in. */
	tasks->elements[freeSlot] = result;
	
	/* Lock state on the task. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&result->common.lock)))
		goto fail_preLockBeforeRelease;
	
	/* Unlock state, we no longer need to keep the state locked since we */
	/* are now in the task list and others will really only care if we */
	/* are even alive or not. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inState->common.lock, NULL)))
		goto fail_stateLockRelease;
	
	/* Setup main thread, all threads start in java.lang.__Start__! */
	mainThread = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadNew(result,
		&mainThread, "main")) || mainThread == NULL)
		goto fail_taskNewThread;
	
	/* The main thread of any task is always implicitly started. */
	if (sjme_error_is(error = sjme_nvm_task_threadStart(mainThread)))
		goto fail_startMain;
	
	/* Release task specific lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&result->common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outTask = result;
	return SJME_ERROR_NONE;
	
	/* In-state locks. */
fail_preLockBeforeRelease:
fail_allocThreads:
fail_initClassLoader:
fail_allocStrings:
	if (strings != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(strings));
		strings = NULL;
	}
fail_allocResult:
	if (result != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
		result = NULL;
	}
fail_allocTasks:
	/* Unlock before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&inState->common.lock, NULL));
	
	return sjme_error_default(error);

	/* Post state lock, when accessing state is no longer needed. */
fail_startMain:
fail_taskNewThread:
fail_stateLockRelease:
	/* Unlock task before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&result->common.lock, NULL));
	
fail_other:
	if (result != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
		result = NULL;
	}
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_threadEnter(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jmethodID inMethod,
	sjme_attrInRange(0, SJME_NVM_NUM_METHOD_CALL_TYPE)
		sjme_nvm_methodCallType callType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_nvm_class_methodInfo targetInfo;
	sjme_jint i, n, dx;
	sjme_nvm_frame result;
	
	if (inThread == NULL || outFrame == NULL || inMethod == NULL ||
		(argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (callType < 0 || callType >= SJME_NVM_NUM_METHOD_CALL_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover target info. */
	targetInfo = inMethod->info[callType];
	if (targetInfo == NULL)
		return sjme_error_vmError(inThread, SJME_ERROR_UNBOUND_METHOD);

	/* No code loaded? */
	if (targetInfo->code == NULL)
		return sjme_error_vmError(inThread, SJME_ERROR_PURE_VIRTUAL_CALL);
	
	/* Argument count mismatch? */
	if (argC != targetInfo->argC)
		return sjme_error_vmError(inThread,
			SJME_ERROR_ARGUMENT_COUNT_MISMATCH);

	/* Argument type mismatch? */
	for (i = 0, n = argC; i < n; i++)
		if (argV[i].type != targetInfo->argT[i])
			return sjme_error_vmError(inThread,
				SJME_ERROR_ARGUMENT_TYPE_MISMATCH);
	
	/* Grab a frame from the thread's frame pool. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadFrameNext(
		inThread, &result)) || result == NULL)
		return sjme_error_vmError(inThread, error);

	/* Perform stack and thread re-framing. */
	if (sjme_error_is(error = sjme_nvm_task_stackReframe(
		inThread->state, inThread, result, targetInfo)))
		return sjme_error_vmError(inThread, error);

	/* Set frame details, needed for local set. */
	result->inClass = inMethod->inClass;
	result->inThread = inThread;
	result->inCode = targetInfo->code;
	result->pool = targetInfo->code->inMethod->inClass->pool;
	
	/* Setup initial locals, which are copied in from arguments. */
	for (i = 0, dx = 0, n = argC; i < n;
		i++, (dx += (argV[i].type == SJME_JAVA_TYPE_ID_LONG ||
			argV[i].type == SJME_JAVA_TYPE_ID_DOUBLE) ? 2 : 1))
		if (sjme_error_is(error = sjme_nvm_task_frameLocalSetL(
			result, dx, &argV[i])))
			return sjme_error_vmError(inThread, error);
	
	/* Set frame as active. */
	inThread->numFrames++;

	/* Success! */
	*outFrame = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_threadEnterA(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_lpcstr inClass,
	sjme_attrInRange(0, SJME_ERROR_INVALID_ARGUMENT)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_nvm_task inTask;
	sjme_jclass foundClass;
	
	if (inThread == NULL || outFrame == NULL || inClass == NULL ||
		inName == NULL || inType == NULL || (argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* There must be a task. */
	inTask = inThread->inTask;
	if (inTask == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Need to find the class first. */
	foundClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		inTask->classLoader, &foundClass,
		inThread, inClass, SJME_JNI_TRUE)))
		return sjme_error_vmError(inThread, error);
	
	/* Forward to other call. */
	return sjme_nvm_task_threadEnterC(
		inThread, outFrame, foundClass, instanceType,
		inName, inType, argC, argV);
}

sjme_errorCode sjme_nvm_task_threadEnterC(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame,
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrInRange(0, SJME_ERROR_INVALID_ARGUMENT)
		sjme_nvm_class_instanceType instanceType,
	sjme_attrInNotNull sjme_lpcstr inName,
	sjme_attrInNotNull sjme_lpcstr inType,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_errorCode error;
	sjme_jmethodID id;
	
	if (inThread == NULL || outFrame == NULL || inClass == NULL ||
		inName == NULL || inType == NULL || (argC != 0 && argV == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (instanceType < 0 || instanceType >= SJME_NVM_CLASS_NUM_INSTANCE_TYPE)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Locate method to execute, since we are calling it, it is required. */
	id = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_methodIDByNameType(
		inClass, inThread, instanceType, SJME_JNI_TRUE, inName,
		inType, &id)) || id == NULL)
		return sjme_error_vmError(inThread, error);
	
	/* Forward to implementation. */
	return sjme_nvm_task_threadEnter(inThread, outFrame,
		id, SJME_NVM_CALL_NON_VIRTUAL, argC, argV);
}

sjme_errorCode sjme_nvm_task_threadFrameNext(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame)
{
#define SJME_NVM_FRAME_GROW_SIZE 8
	sjme_errorCode error;
	sjme_nvm_frame result;
	
	if (inThread == NULL || outFrame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need to allocate more frames? */
	if (inThread->frames == NULL ||
		inThread->numFrames >= inThread->frames->length)
		if (sjme_error_default(error = sjme_list_replace(
			inThread->inTask->inState->allocPool,
			inThread->numFrames + SJME_NVM_FRAME_GROW_SIZE,
			&inThread->frames, sjme_nvm_frame, 0)))
			return sjme_error_default(error);
	
	/* "Pop" and init/clear frame. */
	result = inThread->frames->elements[inThread->numFrames];
	if (result != NULL)
		memset(result, 0, sizeof(*result));
	else
	{
		/* Allocate new blank frame. */
		if (sjme_error_is(error = sjme_nvm_alloc(inThread->state,
			sizeof(*result), SJME_NVM_STRUCT_FRAME,
			SJME_AS_NVM_COMMONP(&result))) || result == NULL)
			return sjme_error_default(error);

		/* Store in this slot, forever. */
		inThread->frames->elements[inThread->numFrames] = result;
	}

	/* Success! */
	*outFrame = result;
	return SJME_ERROR_NONE;
#undef SJME_NVM_FRAME_GROW_SIZE
}

sjme_errorCode sjme_nvm_task_threadNew(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrOutNotNull sjme_nvm_thread* outThread,
	sjme_attrInNotNull sjme_lpcstr threadName)
{
	sjme_errorCode error;
	sjme_nvm_thread result;
	sjme_nvm_frame firstFrame;
	sjme_nvm inState;
	sjme_jint freeSlot, i, n;
	sjme_pointer storage;
	
	if (inTask == NULL || outThread == NULL || threadName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate stack storage. */
	storage = NULL;
	inState = inTask->inState;
	if (sjme_error_is(error = sjme_alloc(inState->allocPool,
		SJME_NVM_THREAD_STACK_SIZE, &storage)) || storage == NULL)
		goto fail_allocStorage;
	
	/* Allocate thread structure. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState, sizeof(*result),
		SJME_NVM_STRUCT_THREAD, SJME_AS_NVM_COMMONP(&result))))
		goto fail_allocResult;
	
	/* Lock state on the task. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inTask->common.lock)))
		goto fail_lock;
	
	/* Find free slot in the thread list. */
	freeSlot = -1;
	for (i = 0, n = inTask->threads->length; i < n; i++)
		if (inTask->threads->elements[i] == NULL)
		{
			freeSlot = i;
			break;
		}
	
	/* Need to grow the list? */
	if (freeSlot < 0)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Fill out basic details. */
	result->state = inState;
	result->schedule = SJME_NVM_THREAD_NUM_SCHEDULE_MODE;
	result->inTask = inTask;
	result->threadId = 1 + sjme_atomic_sjme_jint_getAdd(
		&inState->nextThreadId, 1);
	result->stack.storage = storage;
	result->stack.storageLen = SJME_NVM_THREAD_STACK_SIZE;
	
	/* All new threads are considered initially sleeping. */
	result->status = SJME_NVM_THREAD_STATUS_SLEEPING;
	
	/* All threads have an initial frame within java.lang.__Start__. */
	firstFrame = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadEnterA(
		result, &firstFrame,
		"java/lang/__Start__",
		SJME_NVM_CLASS_MEMBER_STATIC,
		"__main", "()V",
		0, NULL)))
		goto fail_enterFrame;
	
	/* Store thread for future referencing. */
	inTask->threads->elements[freeSlot] = result;
	
	/* Release task specific lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inTask->common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outThread = result;
	return SJME_ERROR_NONE;
	
fail_enterFrame:
	if (firstFrame != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(firstFrame));
	
	/* Unlock before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&inTask->common.lock, NULL));
fail_lock:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
fail_allocStorage:
	sjme_alloc_free(storage);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_threadStart(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Threads can only be started once! */
	if (inThread->start != SJME_NVM_THREAD_START_NEVER)
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* There must be frames. */
	if (inThread->numFrames <= 0)
		return SJME_ERROR_INVALID_THREAD_STATE;

	/* Set thread as started and in the run state. */
	inThread->start = SJME_NVM_THREAD_START_STANDARD;
	inThread->status = SJME_NVM_THREAD_STATUS_RUNNING;

	/* Schedule the thread for execution. */
	if (sjme_error_is(error = sjme_nvm_loop_schedule(inThread->state,
		inThread)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_threadStringValueOfCS(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jstring* outString,
	sjme_attrInValue sjme_jboolean isIntern,
	sjme_attrInNotNull sjme_charSeq* inSeq)
{
#define SJME_INTERN_GROW 32
	sjme_errorCode error;
	sjme_nvm_taskStrings strings;
	sjme_list_sjme_jstring* interns;
	sjme_jstring* blankIntern;
	sjme_jstring result;
	sjme_jint hash, length, i, n;
	
	if (inThread == NULL || outString == NULL || inSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Calculate the hash/length of the string. */
	hash = 0;
	if (sjme_error_is(error = sjme_charSeq_hash(inSeq, &hash)))
		return sjme_error_default(error);

	length = 0;
	if (sjme_error_is(error = sjme_charSeq_length(inSeq, &length)))
		return sjme_error_default(error);

	/* If interned, we need to lock on all the strings. */
	strings = inThread->inTask->strings;
	interns = strings->interns;
	blankIntern = NULL;
	if (isIntern)
	{
		/* Lock on the interned strings. */
		if (sjme_error_is(error = sjme_thread_spinLockGrab(
			&strings->common.lock)))
			return sjme_error_default(error);

		/* See if there are any potential string matches. */
		if (interns != NULL)
			for (i = 0, n = interns->length; i < n; i++)
			{
				/* Ignore blank strings. */
				result = interns->elements[i];
				if (result == NULL)
				{
					if (blankIntern == NULL)
						blankIntern = &interns->elements[i];
					continue;
				}

				/* Different hash/length? Ignore. */
				if (hash != result->hashCode || length != result->length)
					continue;
				
				sjme_todo("Impl?");
				return sjme_error_notImplemented(0);
			}
	}

	/* Setup string object. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inThread->state,
		sizeof(*result), SJME_NVM_STRUCT_STRING_INSTANCE,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocStringInstance;

	/* Set string properties. */
	result->hashCode = hash;
	result->length = length;

	/* Reference string pool directly. */
	if (sjme_nvm_isAR(inSeq->frontEnd.wrapper,
		SJME_NVM_STRUCT_STRING_POOL_STRING))
	{
		/* Count up. */
		if (sjme_error_is(error = sjme_alloc_weakRef(inSeq->frontEnd.wrapper,
			NULL)))
			goto fail_countPoolString;

		/* Reference. */
		result->seq = inSeq;
	}

	/* Otherwise... */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Final intern setup. */
	if (isIntern)
	{
		/* Need to grow the intern list? */
		if (blankIntern == NULL)
		{
			/* Reallocate list. */
			n = (interns == NULL ? 0 : interns->length);
			if (sjme_error_is(error = sjme_list_replace(
				inThread->state->allocPool,
				n + SJME_INTERN_GROW,
				&strings->interns,
				sjme_jstring, 0)) || strings->interns == NULL)
				goto fail_replaceList;

			/* Place at end. */
			interns = strings->interns;
			blankIntern = &interns->elements[n];
		}
		
		/* Set slot here. */
		*blankIntern = result;
		
		/* Release. */
		if (sjme_error_is(error = sjme_thread_spinLockRelease(
			&strings->common.lock, NULL)))
			return sjme_error_default(error);
	}

	/* Success! */
	*outString = result;
	return SJME_ERROR_NONE;

fail_replaceList:
fail_countPoolString:
fail_allocStringInstance:
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#undef SJME_INTERN_GROW
}

sjme_errorCode sjme_nvm_task_threadStringValueOfP(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_jstring* outString,
	sjme_attrInNotNull sjme_nvm_stringPool_string inPool)
{
	if (inThread == NULL || outString == NULL || inPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Forward implementation. */
	return sjme_nvm_task_threadStringValueOfCS(inThread,
		outString, SJME_JNI_TRUE, &inPool->seq);
}
