/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "sjme/except.h"

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
	jint index)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_localPopFloat(sjme_nvm_frame* frame,
	jint index)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_localPopInteger(sjme_nvm_frame* frame,
	jint index)
{
	SJME_EXCEPT_VDEF;
	int x;
	sjme_basicTypeId topType;
	sjme_nvm_frameStack* stack;
	sjme_nvm_frameTread* tread;
	
SJME_EXCEPT_WITH:
	if (frame == NULL)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_NULL_ARGUMENTS);
		
	tread = frame->treads[SJME_JAVA_TYPE_ID_INTEGER];
	stack = frame->stack;
	if (stack == NULL || tread == NULL)
		SJME_EXCEPT_TOSS(SJME_ERROR_FRAME_MISSING_STACK_TREADS);
	
	if (index < 0 || index >= frame->maxLocals)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_LOCAL_INDEX_INVALID);
		
	if (stack->count <= 0 || tread->count <= tread->stackBaseIndex)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_STACK_UNDERFLOW);
	
	/* Get the type at the top to check if it is valid. */
	topType = stack->order[stack->count];
	if (topType != SJME_JAVA_TYPE_ID_INTEGER)
		SJME_EXCEPT_TOSS(SJME_ERROR_CODE_TOP_NOT_INTEGER);
	
	sjme_todo("Implement");
	return JNI_FALSE;
	
SJME_EXCEPT_FAIL:
	return sjme_except_gracefulDeath(
		"Invalid int pop into %d within l:[0, %d] s:[0, %d].",
		(int)index,
		(frame == NULL ? -1 : frame->maxLocals),
		(frame == NULL ? -1 : frame->stack->count));
}

jboolean sjme_nvm_localPopLong(sjme_nvm_frame* frame,
	jint index)
{
	sjme_todo("Implement");
	return JNI_FALSE;
}

jboolean sjme_nvm_localPopReference(sjme_nvm_frame* frame,
	jint index)
{
	sjme_todo("Implement");
	return JNI_FALSE;
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
