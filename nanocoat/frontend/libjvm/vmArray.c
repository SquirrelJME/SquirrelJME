/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

jint JNICALL JVM_GetArrayLength(JNIEnv* env, jobject arr)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_GetArrayElement(JNIEnv* env, jobject arr, jint index)
{
	sjme_todo("Impl?");
}

jvalue JNICALL JVM_GetPrimitiveArrayElement(JNIEnv* env,
	jobject arr,
	jint index,
	jint wCode)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_SetArrayElement(JNIEnv* env,
	jobject arr,
	jint index,
	jobject val)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_SetPrimitiveArrayElement(JNIEnv* env,
	jobject arr,
	jint index,
	jvalue v,
	unsigned char vCode)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_NewArray(JNIEnv* env, jclass eltClass, jint length)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_NewMultiArray(JNIEnv* env, jclass eltClass, jintArray dim)
{
	sjme_todo("Impl?");
}
