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

void SJME_JNI_LIB_FUNCTION JNICALL JVM_ArrayCopy(
	JNIEnv* env,
	jclass ignored,
	jobject src,
	jint srcOff,
	jobject dest,
	jint destOff,
	jint length)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jlong SJME_JNI_LIB_FUNCTION JNICALL JVM_CurrentTimeMillis(
	JNIEnv* env,
	jclass ignored)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JLONG;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_InitProperties(
	JNIEnv* env,
	jobject p)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jlong SJME_JNI_LIB_FUNCTION JNICALL JVM_NanoTime(
	JNIEnv* env,
	jclass ignored)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JLONG;
}
