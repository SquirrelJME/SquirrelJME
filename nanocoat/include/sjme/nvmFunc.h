/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * NanoCoat Virtual Machine functions.
 * 
 * @since 2023/12/08
 */

#ifndef SQUIRRELJME_NVMFUNC_H
#define SQUIRRELJME_NVMFUNC_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_NVMFUNC_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_nvm_arrayLength(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNullable sjme_jobject arrayInstance,
	sjme_attrOutNotNull sjme_jint* outLen)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_arrayLoadIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_basicTypeId primitiveType,
	sjme_attrInNullable sjme_jobject arrayInstance,
	sjme_attrInValue sjme_attrInPositive sjme_jint index)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_arrayStore(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_basicTypeId primitiveType,
	sjme_attrInNullable sjme_jobject arrayInstance,
	sjme_attrInValue sjme_attrInPositive sjme_jint index,
	sjme_attrInNotNull sjme_any* value)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_checkCast(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNullable sjme_jobject instance,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* type)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_countReferenceDown(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNullable sjme_jobject instance)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_fieldGetToTemp(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNullable sjme_jobject instance,
	sjme_attrInNotNull sjme_dynamic_linkage_data_fieldAccess* field)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_fieldPut(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNullable sjme_jobject instance,
	sjme_attrInNotNull sjme_dynamic_linkage_data_fieldAccess* field,
	sjme_attrInNotNull sjme_any* value)
	sjme_attrCheckReturn;

/**
 * Garbage collects an object.
 *
 * @param frame The frame this is collecting in.
 * @param instance The instance to be garbage collected.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/11/17
 */
sjme_errorCode sjme_nvm_gcObject(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNullable sjme_jobject instance)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_invoke(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_invokeNormal* method)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPopDouble(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint localIndex)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPopFloat(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint localIndex)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPopInteger(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint localIndex)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPopLong(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint localIndex)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPopReference(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint localIndex)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPushDouble(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint index)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPushFloat(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint index)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPushInteger(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint index)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPushLong(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint index)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localPushReference(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint index)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localReadInteger(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint index,
	sjme_attrOutNotNull sjme_jint* outValue)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_localWriteInteger(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_attrInPositive sjme_jint index,
	sjme_attrInValue sjme_jint value)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_lookupClassObjectIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* classLinkage)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_lookupStringIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_stringObject* stringLinkage)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_monitor(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNullable sjme_jobject instance,
	sjme_attrInValue sjme_jboolean isEnter)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_newArrayIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* componentType,
	sjme_attrInValue sjme_attrInPositive sjme_jint length)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_newInstanceIntoTemp(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* linkage)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_returnFromMethod(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNotNull sjme_any* value)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPopAny(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrOutNotNull sjme_any* output)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_stackPopAnyToTemp(
	sjme_attrInNotNull sjme_nvm_frame frame)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

sjme_jint sjme_nvm_stackPopInteger(
	sjme_attrInNotNull sjme_nvm_frame frame)
	sjme_attrOutNegativeOnePositive;

sjme_jobject sjme_nvm_stackPopReference(
	sjme_attrInNotNull sjme_nvm_frame frame)
	sjme_attrOutNullable;

sjme_errorCode sjme_nvm_stackPopReferenceThenThrow(
	sjme_attrInNotNull sjme_nvm_frame frame)
	sjme_attrCheckReturn;

sjme_tempIndex sjme_nvm_stackPopReferenceToTemp(
	sjme_attrInNotNull sjme_nvm_frame frame)
	sjme_attrOutNegativeOnePositive sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPushAny(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNotNull sjme_any* input)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPushAnyFromTemp(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInPositive sjme_tempIndex input)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPushDoubleParts(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_jint hi,
	sjme_attrInValue sjme_jint lo)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPushFloatRaw(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_jint rawValue)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPushInteger(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_jint value)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPushIntegerIsInstanceOf(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNullable sjme_jobject instance,
	sjme_attrInNotNull sjme_dynamic_linkage_data_classObject* type)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPushLongParts(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInValue sjme_jint hi,
	sjme_attrInValue sjme_jint lo)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPushReference(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInNullable sjme_jobject instance)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_stackPushReferenceFromTemp(
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInPositive sjme_tempIndex tempIndex)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_tempDiscard(
	sjme_attrInNotNull sjme_nvm_frame frame)
	sjme_attrCheckReturn;

sjme_errorCode sjme_nvm_throwExecute(
	sjme_attrInNotNull sjme_nvm_frame frame)
	sjme_attrCheckReturn;

/**
 * Returns the top-most frame in the thread.
 *
 * @param inThread The thread to get the top frame from.
 * @param outFrame The top most frame.
 * @return Returns @c SJME_JNI_TRUE on success where the thread is valid and it
 * has at least one frame.
 * @since 2023/11/11
 */
sjme_errorCode sjme_nvm_topFrame(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrOutNotNull sjme_nvm_frame outFrame)
	sjme_attrCheckReturn;

/**
 * Ticks the virtual machine.
 *
 * @param inState The state to tick, @c -1 means to tick forever.
 * @param maxTics The number of ticks to execute before returning.
 * @param isTerminated Optional output to check if the VM terminated.
 * @return Any resultant error, if any.
 * @since 2023/07/27
 */
sjme_errorCode sjme_nvm_tick(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInValue sjme_attrInPositive sjme_jint maxTics,
	sjme_attrOutNullable sjme_jboolean* isTerminated)
	sjme_attrCheckReturn;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_NVMFUNC_H
}
		#undef SJME_CXX_SQUIRRELJME_NVMFUNC_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_NVMFUNC_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_NVMFUNC_H */
