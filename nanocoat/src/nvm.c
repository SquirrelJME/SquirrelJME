/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "sjme/except.h"
#include "sjme/tread.h"

/**
 * Pops a generic value from the stack into a local variable.
 * 
 * @param frame The frame to pop from.
 * @param localIndex The local index to store to.
 * @param accessor The accessor used.
 * @param outOldLocalValue Optional output old local value.
 * @param outStackValue Optional output stack value.
 * @return Returns @c JNI_TRUE on success.
 * @since 2023/11/16
 */
static jboolean sjme_nvm_localPopGeneric(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInPositive volatile jint localIndex,
	sjme_attrInNotNull const sjme_nvm_frameTreadAccessor* accessor,
	sjme_attrOutNullable void* outOldLocalValue,
	sjme_attrOutNullable void* outStackValue)
{
	SJME_EXCEPT_VDEF;
	sjme_javaTypeId topType;
	sjme_nvm_frameStack* stack;
	sjme_nvm_frameTread* tread;
	const sjme_nvm_frameLocalMap* localMap;
	jbyte indexMapTo;
	void* valueAddr;
	
SJME_EXCEPT_WITH:
	if (frame == NULL || accessor == NULL)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_NULL_ARGUMENTS);
	
	/* Obtain the tread to access. */
	tread = NULL;
	if (!accessor->getTread(frame, accessor, &tread) || tread == NULL)
		SJME_EXCEPT_TOSS(SJME_ERROR_FRAME_MISSING_STACK_TREADS);
	
	/* Check to make sure they exist. */
	stack = frame->stack;
	localMap = frame->localMap;
	if (stack == NULL || tread == NULL || localMap == NULL)
		SJME_EXCEPT_TOSS(SJME_ERROR_FRAME_MISSING_STACK_TREADS);
	
	if (localIndex < 0 || localIndex >= localMap->max)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_LOCAL_INDEX_INVALID);
		
	if (stack->count <= 0 || tread->count <= tread->stackBaseIndex)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_STACK_UNDERFLOW);
	
	/* Get the type at the top to check if it is valid. */
	topType = stack->order[stack->count - 1];
	if (topType != accessor->typeId)
		SJME_EXCEPT_TOSS(accessor->errorInvalidTop);
	
	/* Copy the stack value to the local. */
	indexMapTo = localMap->maps[localIndex].to[accessor->typeId];
	valueAddr = NULL;
	if (!accessor->address(frame, accessor, tread, tread->count - 1,
		&valueAddr) || valueAddr == NULL)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_STACK_INVALID_READ);
	
	/* Store the old value in the local variable. */
	if (outOldLocalValue != NULL)
		if (!accessor->read(frame, accessor, tread, indexMapTo,
				outOldLocalValue))
			SJME_EXCEPT_TOSS(SJME_ERROR_CODE_LOCAL_INVALID_READ);
	
	/* Store the output stack value, if requested. */
	if (outStackValue != NULL)
		memmove(outStackValue, valueAddr, accessor->size);
	
	/* Write value directly from stack source address. */
	if (!accessor->write(frame, accessor, tread, indexMapTo, valueAddr))
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_LOCAL_INVALID_WRITE);
	
	/* Clear old stack value with zero value. */
	valueAddr = alloca(accessor->size);
	memset(valueAddr, 0, accessor->size);
	if (!accessor->write(frame, accessor, tread, tread->count - 1, valueAddr))
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_STACK_INVALID_WRITE);
	
	/* Clear and reduce stack counts. */
	stack->order[stack->count] = 0;
	stack->count--;
	tread->count--;
	
	/* Done. */
	return JNI_TRUE;
	
SJME_EXCEPT_FAIL:
	return sjme_except_gracefulDeath(
		"Invalid %s pop into %d within l:[0, %d] s:[0, %d].",
		accessor->name,
		(int)localIndex,
		(frame == NULL || frame->localMap == NULL ? -1 :
			frame->localMap->max),
		(frame == NULL || frame->stack == NULL ? -1 :
			frame->stack->count));
}

jboolean sjme_nvm_arrayLength(sjme_nvm_frame* frame,
	jobject arrayInstance, jint* outLen)
{
	sjme_todo("Implement");
	return -1;
}

sjme_tempIndex sjme_nvm_arrayLoadIntoTemp(sjme_nvm_frame* frame,
	sjme_basicTypeId primitiveType,
	jobject arrayInstance,
	jint index)
{
	sjme_todo("Implement");
	return -1;
}
	
jboolean sjme_nvm_arrayStore(sjme_nvm_frame* frame,
	sjme_basicTypeId primitiveType,
	jobject arrayInstance,
	jint index,
	sjme_any* value)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}
	
jboolean sjme_nvm_checkCast(sjme_nvm_frame* frame,
	jobject instance,
	sjme_dynamic_linkage_data_classObject* type)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}
	
jboolean sjme_nvm_countReferenceDown(sjme_nvm_frame* frame,
	jobject instance)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}
	
sjme_tempIndex sjme_nvm_fieldGetToTemp(sjme_nvm_frame* frame,
	jobject instance,
	sjme_dynamic_linkage_data_fieldAccess* field)
{
	sjme_todo("Implement");
	return -1;
}

jboolean sjme_nvm_fieldPut(sjme_nvm_frame* frame,
	jobject instance,
	sjme_dynamic_linkage_data_fieldAccess* field,
	sjme_any* value)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_gcObject(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNullable jobject instance)
{
	SJME_EXCEPT_VDEF;
	sjme_nvm_state* state;

SJME_EXCEPT_WITH:
	if (frame == NULL || instance == NULL)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_NULL_ARGUMENTS);
	
	/* Must be zero! */
	if (instance->refCount != 0)
		SJME_EXCEPT_TOSS(SJME_ERROR_OBJECT_REFCOUNT_NOT_ZERO);
	
	/* Call GC hook, if any. */
	state = frame->inThread->inState;
	if (state->hooks != NULL && state->hooks->gc != NULL)
		if (!state->hooks->gc(frame, instance))
			SJME_EXCEPT_TOSS(SJME_ERROR_OBJECT_GC_CANCELLED);
	
	/* TODO: Implement actual GC... */
	sjme_message("Actually implement object GC of %p...", instance);
	
	/* Success! */
	return JNI_TRUE;

SJME_EXCEPT_FAIL:
	return sjme_except_gracefulDeath("Could not GC %p.",
		instance);
}

jboolean sjme_nvm_invoke(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_invokeNormal* method)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jint sjme_nvm_localLoadInteger(sjme_nvm_frame* frame,
	jint index)
{
	sjme_todo("Implement");
	return -1;
}

jboolean sjme_nvm_localPopDouble(sjme_nvm_frame* frame,
	jint localIndex)
{
	return sjme_nvm_localPopGeneric(frame, localIndex,
		sjme_nvm_frameTreadAccessorByType(
			SJME_JAVA_TYPE_ID_DOUBLE),
			NULL, NULL);
}

jboolean sjme_nvm_localPopFloat(sjme_nvm_frame* frame,
	jint localIndex)
{
	return sjme_nvm_localPopGeneric(frame, localIndex,
		sjme_nvm_frameTreadAccessorByType(
			SJME_JAVA_TYPE_ID_FLOAT),
			NULL, NULL);
}

jboolean sjme_nvm_localPopInteger(sjme_nvm_frame* frame,
	volatile jint localIndex)
{
	return sjme_nvm_localPopGeneric(frame, localIndex,
		sjme_nvm_frameTreadAccessorByType(
			SJME_JAVA_TYPE_ID_INTEGER),
			NULL, NULL);
}

jboolean sjme_nvm_localPopLong(sjme_nvm_frame* frame,
	jint localIndex)
{
	return sjme_nvm_localPopGeneric(frame, localIndex,
		sjme_nvm_frameTreadAccessorByType(
			SJME_JAVA_TYPE_ID_LONG),
			NULL, NULL);
}

jboolean sjme_nvm_localPopReference(sjme_nvm_frame* frame,
	jint localIndex)
{
	SJME_EXCEPT_VDEF;
	jobject oldLocalValue, stackValue;

SJME_EXCEPT_WITH:
	/* Read in the stack values. */
	oldLocalValue = NULL;
	stackValue = NULL;
	if (!sjme_nvm_localPopGeneric(frame, localIndex,
		sjme_nvm_frameTreadAccessorByType(
			SJME_JAVA_TYPE_ID_OBJECT),
			&oldLocalValue, &stackValue))
		SJME_EXCEPT_TOSS(SJME_ERROR_INVALID_REFERENCE_POP);
	
	/* Need to refcount the old stack value? */
	/* Always happens, even if they are the same reference because now there */
	/* is none there. */
	if (oldLocalValue != NULL)
		if (--oldLocalValue->refCount <= 0)
			if (!sjme_nvm_gcObject(frame, oldLocalValue))
				SJME_EXCEPT_TOSS(SJME_ERROR_COULD_NOT_GC_OBJECT);
	
	/* Success! */
	return JNI_TRUE;

SJME_EXCEPT_FAIL:
	return sjme_except_gracefulDeath("Could not refcount objects.");
}

jboolean sjme_nvm_localPushDouble(sjme_nvm_frame* frame,
	jint index)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_localPushFloat(sjme_nvm_frame* frame,
	jint index)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_localPushInteger(sjme_nvm_frame* frame,
	jint index)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_localPushLong(sjme_nvm_frame* frame,
	jint index)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_localPushReference(sjme_nvm_frame* frame,
	jint index)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_localReadInteger(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInValue sjme_attrInPositive jint index,
	sjme_attrOutNotNull jint* outValue)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}
	
jboolean sjme_nvm_localStoreInteger(sjme_nvm_frame* frame,
	jint index,
	jint value)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

sjme_tempIndex sjme_nvm_lookupClassObjectIntoTemp(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_classObject* classObjectLinkage)
{
	sjme_todo("Implement");
	return -1;
}
	
sjme_tempIndex sjme_nvm_lookupStringIntoTemp(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_stringObject* stringObjectLinkage)
{
	sjme_todo("Implement");
	return -1;
}

jboolean sjme_nvm_monitor(sjme_nvm_frame* frame,
	jobject instance,
	jboolean isEnter)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

sjme_tempIndex sjme_nvm_newArrayIntoTemp(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_classObject* componentType,
	jint length)
{
	sjme_todo("Implement");
	return -1;
}

sjme_tempIndex sjme_nvm_newInstanceIntoTemp(sjme_nvm_frame* frame,
	sjme_dynamic_linkage_data_classObject* linkage)
{
	sjme_todo("Implement");
	return -1;
}
	
jboolean sjme_nvm_returnFromMethod(sjme_nvm_frame* frame,
	sjme_any* value)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_stackPopAny(sjme_nvm_frame* frame,
	sjme_any* output)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

sjme_tempIndex sjme_nvm_stackPopAnyToTemp(sjme_nvm_frame* frame)
{
	sjme_todo("Implement");
	return -1;
}

jint sjme_nvm_stackPopInteger(sjme_nvm_frame* frame)
{
	sjme_todo("Implement");
	return -1;
}

jobject sjme_nvm_stackPopReference(sjme_nvm_frame* frame)
{
	sjme_todo("Implement");
	return NULL;
}

jboolean sjme_nvm_stackPopReferenceThenThrow(sjme_nvm_frame* frame)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

sjme_tempIndex sjme_nvm_stackPopReferenceToTemp(sjme_nvm_frame* frame)
{
	sjme_todo("Implement");
	return -1;
}

jboolean sjme_nvm_stackPushAny(sjme_nvm_frame* frame,
	sjme_any* input)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_stackPushAnyFromTemp(sjme_nvm_frame* frame,
	sjme_tempIndex input)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_stackPushDoubleParts(sjme_nvm_frame* frame,
	jint hi,
	jint lo)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_stackPushFloatRaw(sjme_nvm_frame* frame,
	jint rawValue)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_stackPushInteger(sjme_nvm_frame* frame,
	jint value)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_stackPushIntegerIsInstanceOf(sjme_nvm_frame* frame,
	jobject instance,
	sjme_dynamic_linkage_data_classObject* type)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_stackPushLongParts(sjme_nvm_frame* frame,
	jint hi,
	jint lo)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}
	
jboolean sjme_nvm_stackPushReference(sjme_nvm_frame* frame,
	jobject instance)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_stackPushReferenceFromTemp(sjme_nvm_frame* frame,
	sjme_tempIndex tempIndex)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_tempDiscard(sjme_nvm_frame* frame)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_throwExecute(sjme_nvm_frame* frame)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_topFrame(
	sjme_attrInNotNull sjme_nvm_thread* inThread,
	sjme_attrOutNotNull sjme_nvm_frame* outFrame)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}
