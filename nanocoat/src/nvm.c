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

static jboolean sjme_nvm_localPopGeneric(sjme_nvm_frame* frame,
	volatile jint localIndex, const sjme_nvm_frameTreadAccessor* accessor)
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
	topType = stack->order[stack->count];
	if (topType != accessor->typeId)
		SJME_EXCEPT_TOSS(accessor->errorInvalidTop);
	
	/* Copy the stack value to the local. */
	indexMapTo = localMap->maps[localIndex].to[SJME_JAVA_TYPE_ID_INTEGER];
	valueAddr = NULL;
	if (!accessor->address(frame, accessor, tread, tread->count - 1,
		&valueAddr) || valueAddr == NULL)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_STACK_INVALID_READ);
	
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
			SJME_JAVA_TYPE_ID_DOUBLE));
}

jboolean sjme_nvm_localPopFloat(sjme_nvm_frame* frame,
	jint localIndex)
{
	return sjme_nvm_localPopGeneric(frame, localIndex,
		sjme_nvm_frameTreadAccessorByType(
			SJME_JAVA_TYPE_ID_FLOAT));
}

jboolean sjme_nvm_localPopInteger(sjme_nvm_frame* frame,
	volatile jint localIndex)
{
	return sjme_nvm_localPopGeneric(frame, localIndex,
		sjme_nvm_frameTreadAccessorByType(
			SJME_JAVA_TYPE_ID_INTEGER));
}

jboolean sjme_nvm_localPopLong(sjme_nvm_frame* frame,
	jint localIndex)
{
	return sjme_nvm_localPopGeneric(frame, localIndex,
		sjme_nvm_frameTreadAccessorByType(
			SJME_JAVA_TYPE_ID_LONG));
}

jboolean sjme_nvm_localPopReference(sjme_nvm_frame* frame,
	jint localIndex)
{
	return sjme_nvm_localPopGeneric(frame, localIndex,
		sjme_nvm_frameTreadAccessorByType(
			SJME_JAVA_TYPE_ID_OBJECT));
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
