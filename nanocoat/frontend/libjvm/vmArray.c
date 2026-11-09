/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"
#include "frontend/libjvm/internals.h"

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_GetArrayElement(
	JNIEnv* env, 
	jobject array,
	jint index)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_GetArrayLength(
	JNIEnv* env, 
	jobject array)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jvalue SJME_JNI_LIB_FUNCTION JNICALL JVM_GetPrimitiveArrayElement(
	JNIEnv* env,
	jobject array,
	jint index,
	jint wCode)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_TYPED(jvalue);
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_NewArray(
	JNIEnv* env, 
	jclass elementType, 
	jint length)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_NewMultiArray(
	JNIEnv* env,
	jclass elementType,
	jintArray dim)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_SetArrayElement(
	JNIEnv* env,
	jobject array,
	jint index,
	jobject val)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_SetPrimitiveArrayElement(
	JNIEnv* env,
	jobject array,
	jint index,
	jvalue val,
	unsigned char vCode)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}
