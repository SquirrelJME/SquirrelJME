/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/bytecode.h"
#include "sjme/util.h"
#include "sjme/nvm/instance.h"
#include "sjme/stdGone.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/loop.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/stdGone.h"

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
	inOutValue->t = valueType;
	switch (valueType)
	{
		case SJME_JAVA_TYPE_ID_INTEGER:
			inOutValue->v.i = stack->base.i[stackIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			inOutValue->v.j = stack->base.j[stackIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			inOutValue->v.f = stack->base.f[stackIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			inOutValue->v.d = stack->base.d[stackIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			inOutValue->v.l = stack->base.l[stackIndex];
			break;
			
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_frameHandler(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jobject tossed,
	sjme_attrInOutNotNull sjme_jboolean* handled,
	sjme_attrInOutNotNull sjme_nvm_byteCode_pcNew* pcNew)
{
	sjme_errorCode error;
	sjme_list_sjme_nvm_class_exceptionHandler* handlers;
	sjme_nvm_class_exceptionHandler* handler;
	sjme_nvm_vmClass_loader loader;
	sjme_jclass checkClass, tossedClass;
	sjme_jint i, n, pc;
	
	if (inFrame == NULL || tossed == NULL || handled == NULL || pcNew == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Are there no exceptions to actually handle? */
	handlers = inFrame->inCode->exceptions;
	if (handlers == NULL || handlers->length == 0)
		goto skip_notHandled;

	/* Used to speed things up slightly. */
	pc = inFrame->pc;
	loader = SJME_F_CL(inFrame);
	tossedClass = SJME_O_C(tossed);

	/* Go through exception handlers. */
	for (i = 0, n = handlers->length; i < n; i++)
	{
		/* Get the handler here. */
		handler = &handlers->elements[i];

		/* Out of range for this handler? */
		if (pc < handler->range.start || pc >= handler->range.end)
			continue;

		/* Load in target class. */
		checkClass = NULL;
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(loader,
			&checkClass, SJME_F_T(inFrame),
			handler->handles->descriptor->seq,
			SJME_JNI_TRUE)) || checkClass == NULL)
			return sjme_error_vmError(inFrame, error);
		
		/* Is this the matching handler? */
		if (tossedClass == checkClass || sjme_nvm_vmClass_isAssignableFrom(
			SJME_F_T(inFrame), tossedClass, checkClass))
		{
			/* We are handling this. */
			*handled = SJME_JNI_TRUE;

			/* Jump to elsewhere in the frame. */
			pcNew->type = SJME_NVM_BYTECODE_PC_ABSOLUTE;
			pcNew->popFrame = SJME_JNI_FALSE;
			pcNew->adjust = handler->handlerPc;

			/* Success! */
			return SJME_ERROR_NONE;
		}
	}
	
skip_notHandled:
	*handled = SJME_JNI_FALSE;
	pcNew->popFrame = SJME_JNI_TRUE;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_frameLocalAddr(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId localType,
	sjme_attrInPositive sjme_jint localIndex,
	sjme_attrOutNotNull sjme_pointer* outAddr)
{
	sjme_nvm_class_codePerType* perType;
	sjme_jint mappedSlot;
	
	if (inFrame == NULL || outAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (localType < 0 || localType >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (localIndex < 0 ||
		localIndex >= inFrame->inCode->perType[SJME_JAVA_TYPE_ID_ALL].locals)
		return sjme_error_vmError(inFrame, SJME_ERROR_LOCAL_INDEX_INVALID);
	
	/* Determine where this maps from for the read. */
	perType = &inFrame->inCode->perType[localType];
	mappedSlot = perType->localMap[localIndex];
	if (mappedSlot < 0 || mappedSlot > perType->locals)
		return sjme_error_vmError(inFrame, SJME_ERROR_TREAD_INDEX_INVALID);

	/* Directly access tread address. */
	return sjme_nvm_task_frameTreadAddr(inFrame,
		localType, mappedSlot, outAddr);
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

	if (inValue->t < 0 || inValue->t >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Is this wide? */
	isWide = SJME_TYPEID_IS_WIDE(inValue->t);

	/* Check for complete out of bounds. */
	if (localIndex < 0 ||
		((localIndex + (isWide ? 1 : 0)) >=
			inFrame->inCode->perType[SJME_JAVA_TYPE_ID_ALL].locals))
		return sjme_error_vmError(inFrame, SJME_ERROR_LOCAL_INDEX_INVALID);

	/* Is the index still valid on the tread? */
	perType = &inFrame->inCode->perType[inValue->t];
	mappedSlot = perType->localMap[localIndex];
	if (mappedSlot < 0 || mappedSlot >= perType->locals)
		return sjme_error_vmError(inFrame, SJME_ERROR_TREAD_INDEX_INVALID);

	/* Set tread value. */
	if (sjme_error_is(error = sjme_nvm_task_frameTreadSetT(inFrame,
		mappedSlot, inValue)))
		return sjme_error_vmError(inFrame, error);

	/* Replace order info. */
	stack = &inFrame->stack;
	stack->order[localIndex] = inValue->t;
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

sjme_errorCode sjme_nvm_task_frameStackClear(
	sjme_attrInNotNull sjme_nvm_frame inFrame)
{
	sjme_errorCode error;
	sjme_jvalueTyped temp;
	sjme_frame_frameStacks* stack;
	
	if (inFrame == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Keep draining the stack until nothing is left. */
	stack = &inFrame->stack;
	while (stack->orderTop - stack->orderFront)
	{
		/* Peek top value. */
		memset(&temp, 0, sizeof(temp));
		if (sjme_error_is(error = sjme_nvm_task_frameStackTop(inFrame,
			0, &temp, SJME_JNI_FALSE)))
			return sjme_error_vmError(inFrame, error);

		/* Pop it. */
		if (sjme_error_is(error = sjme_nvm_task_frameStackPop(inFrame,
			temp.t, &temp)))
			return sjme_error_vmError(inFrame, error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_frameStackPeek(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInNotNull sjme_jvalueTyped* outValue,
	sjme_attrInValue sjme_jboolean copiedElsewhere)
{
	sjme_errorCode error;
	sjme_jvalueTyped temp;

	/* Peek top value. */
	memset(&temp, 0, sizeof(temp));
	if (sjme_error_is(error = sjme_nvm_task_frameStackTop(inFrame,
		0, &temp, copiedElsewhere)))
		return sjme_error_vmError(inFrame, error);

	/* Must be the same type. */
	if (typeId != SJME_NUM_JAVA_TYPE_IDS && temp.t != typeId)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_INVALID_READ);

	/* Success! */
	memmove(outValue, &temp, sizeof(*outValue));
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
	sjme_javaTypeId topType;
	
	if (inFrame == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if ((typeId < 0 || typeId >= SJME_NUM_JAVA_TYPE_IDS) &&
		typeId != SJME_STACK_TYPE_NARROW &&
		typeId != SJME_STACK_TYPE_WIDE)
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
	topType = stack->order[newTop];
	if (topType != typeId)
	{
		/* Wanting neither wide nor narrow. */
		if (typeId != SJME_STACK_TYPE_NARROW &&
			typeId != SJME_STACK_TYPE_WIDE)
			return sjme_error_vmError(inFrame, SJME_ERROR_STACK_INVALID_READ);

		/* Wanting the incorrect type. */
		if (((typeId == SJME_JAVA_TYPE_ID_INTEGER ||
				typeId == SJME_JAVA_TYPE_ID_FLOAT ||
				typeId == SJME_JAVA_TYPE_ID_OBJECT) !=
					(typeId == SJME_STACK_TYPE_NARROW)) &&
			((typeId == SJME_JAVA_TYPE_ID_LONG ||
				typeId == SJME_JAVA_TYPE_ID_DOUBLE) !=
					(typeId == SJME_STACK_TYPE_WIDE)))
			return sjme_error_vmError(inFrame, SJME_ERROR_STACK_INVALID_READ);
	}

	/* Determine per type slot to remove. */
	perType = &stack->stack[topType];
	newPerTop = perType->top - 1;
	if (newPerTop < perType->front)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_UNDERFLOW);

	/* Read in value. */
	if (sjme_error_is(error = sjme_nvm_task_frameTreadGetT(
		inFrame, topType, newPerTop, outValue, SJME_JNI_TRUE)))
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
	isWide = SJME_TYPEID_IS_WIDE(inValue->t);
	pushCount = (isWide ? 2 : 1);
	if (stack->orderTop + pushCount > stack->orderLength)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_OVERFLOW);

	/* Will the per-type stack overflow? */
	perType = &stack->stack[inValue->t];
	if (perType->top + 1 > perType->length)
		return sjme_error_vmError(inFrame, SJME_ERROR_STACK_OVERFLOW);

	/* Place onto the order, mark top invalid if required. */
	stack->order[stack->orderTop++] = inValue->t;
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
	sjme_errorCode error;
	sjme_jvalueTyped value;
	
	if (inFrame == NULL || inClassName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Lookup class. */
	value.v.l = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoad(
		SJME_F_CL(inFrame), SJME_AS_JCLASSP(&value.v.l),
		SJME_F_T(inFrame),
		inClassName->seq, SJME_JNI_TRUE)) || value.v.l == NULL)
		return sjme_error_default(error);
	
	/* Push value. */
	value.t = SJME_JAVA_TYPE_ID_OBJECT;
	return sjme_nvm_task_frameStackPush(inFrame, &value);
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
	value.t = SJME_JAVA_TYPE_ID_OBJECT;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfP(
		SJME_F_T(inFrame),
		SJME_AS_JSTRINGP(&value.v.l), inString)) ||
		value.v.l == NULL)
		return sjme_error_vmError(inFrame, error);

	/* Count up string. */
	if (sjme_error_is(error = sjme_alloc_weakRef(value.v.l, NULL)))
		return sjme_error_vmError(inFrame, error);

	/* Push value. */
	return sjme_nvm_task_frameStackPush(inFrame, &value);
}

sjme_errorCode sjme_nvm_task_frameStackTop(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInPositive sjme_jint depth,
	sjme_attrOutNotNull sjme_jvalueTyped* outValue,
	sjme_attrInValue sjme_jboolean copiedElsewhere)
{
	sjme_errorCode error;
	sjme_frame_frameStacks* stack;
	sjme_jint newTop;
	sjme_javaTypeId readType;
	sjme_jvalueTyped temp;
	sjme_jint sub[SJME_NUM_JAVA_TYPE_IDS];
	
	if (inFrame == NULL || outValue == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (depth < 0)
		return SJME_ERROR_STACK_UNDERFLOW;

	/* Set initial position. */
	stack = &inFrame->stack;
	newTop = stack->orderTop;

	/* Keep eating depth. */
	memset(&sub, 0, sizeof(sub));
	while ((depth--) >= 0)
	{
		/* Bump down and check overflow */
		newTop--;
		if (newTop < stack->orderFront)
			return SJME_ERROR_STACK_UNDERFLOW;

		/* Wide? */
		if (stack->order[newTop] == SJME_JAVA_TYPE_ID_VOID)
			newTop--;

		/* Increase subtraction count for the given type, this is used */
		/* to locate the slot on the stack. */
		sub[stack->order[newTop]]++;
	}

	/* Copy out value. */
	readType = stack->order[newTop];
	memset(&temp, 0, sizeof(temp));
	if (sjme_error_is(error = sjme_nvm_task_frameTreadGetT(inFrame, readType,
		stack->stack[readType].top - sub[readType],
		&temp, SJME_JNI_FALSE)))
		return sjme_error_vmError(inFrame, error);
	
	/* If copied elsewhere, count object up. */
	if (copiedElsewhere && readType == SJME_JAVA_TYPE_ID_OBJECT &&
		temp.v.l != NULL)
		if (sjme_error_is(error = sjme_alloc_weakRef(temp.v.l, NULL)))
			return sjme_error_default(error);
	
	/* Success! */
	memmove(outValue, &temp, sizeof(*outValue));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_frameTreadAddr(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, SJME_NUM_JAVA_TYPE_IDS) sjme_javaTypeId typeId,
	sjme_attrInPositive sjme_jint typeIndex,
	sjme_attrOutNotNull sjme_pointer* outAddr)
{
	sjme_frame_frameStack* perType;
	
	if (inFrame == NULL || outAddr == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (typeId < 0 || typeId >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Check the tread index. */
	perType = &inFrame->stack.stack[typeId];
	if (typeIndex < 0 || typeIndex >= perType->length)
		return sjme_error_vmError(inFrame, SJME_ERROR_TREAD_INDEX_INVALID);
	
	/* Operating depends on the type. */
	switch (typeId)
	{
		case SJME_JAVA_TYPE_ID_INTEGER:
			(*outAddr) = &perType->base.i[typeIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			(*outAddr) = &perType->base.j[typeIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			(*outAddr) = &perType->base.f[typeIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			(*outAddr) = &perType->base.d[typeIndex];
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			(*outAddr) = &perType->base.l[typeIndex];
			break;
			
		default:
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_FIELD_TYPE);
	}

	/* Success! */
	return SJME_ERROR_NONE;
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
			outValue->v.i = perType->base.i[typeIndex];
		
			if (eraseOld)
				perType->base.i[typeIndex] = 0;
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			outValue->v.j = perType->base.j[typeIndex];
		
			if (eraseOld)
				memset(&perType->base.j[typeIndex], 0,
					sizeof(sjme_jlong));
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			outValue->v.f = perType->base.f[typeIndex];
		
			if (eraseOld)
				memset(&perType->base.f[typeIndex], 0,
					sizeof(sjme_jfloat));
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			outValue->v.d = perType->base.d[typeIndex];
		
			if (eraseOld)
				memset(&perType->base.d[typeIndex], 0,
					sizeof(sjme_jdouble));
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			/* Load into temporary as we may be erasing the value here. */
			tempObject = perType->base.l[typeIndex];

			/* Is the value in the tread being cleared? */
			if (eraseOld)
				perType->base.l[typeIndex] = NULL;

			/* Otherwise, we technically have a copy so count up. */
			else if (tempObject != NULL)
				if (sjme_error_is(error = sjme_alloc_weakRef(tempObject,
					NULL)))
					return sjme_error_default(error);

			outValue->v.l = tempObject;
			break;
			
		default:
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_FIELD_TYPE);
	}

	/* Success! */
	outValue->t = typeId;
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

	if (inValue->t < 0 || inValue->t >= SJME_NUM_JAVA_TYPE_IDS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Obtain the per type. */
	perType = &inFrame->stack.stack[inValue->t];

	/* Check the tread index. */
	if (typeIndex < 0 || typeIndex >= perType->length)
		return sjme_error_vmError(inFrame, SJME_ERROR_TREAD_INDEX_INVALID);
	
	/* Operating depends on the type. */
	switch (inValue->t)
	{
		case SJME_JAVA_TYPE_ID_INTEGER:
			perType->base.i[typeIndex] = inValue->v.i;
			break;
			
		case SJME_JAVA_TYPE_ID_LONG:
			perType->base.j[typeIndex] = inValue->v.j;
			break;
			
		case SJME_JAVA_TYPE_ID_FLOAT:
			perType->base.f[typeIndex] = inValue->v.f;
			break;
			
		case SJME_JAVA_TYPE_ID_DOUBLE:
			perType->base.d[typeIndex] = inValue->v.d;
			break;
			
		case SJME_JAVA_TYPE_ID_OBJECT:
			/* If there is an old value here, count it down. */
			if (sjme_error_is(error = sjme_nvm_instance_countDown(
				&perType->base.l[typeIndex],
				inValue->v.l)))
				return sjme_error_vmError(inFrame, error);

			/* Set. */
			perType->base.l[typeIndex] = inValue->v.l;
			break;
			
		default:
			return sjme_error_vmError(inFrame, SJME_ERROR_INVALID_FIELD_TYPE);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
