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

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_AllocateNewArray(
	JNIEnv* env,
	jobject object,
	jclass currClass,
	jint length)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_AllocateNewObject(
	JNIEnv* env,
	jobject object,
	jclass currClass,
	jclass initClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_LatestUserDefinedLoader(
	JNIEnv* env)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jclass SJME_JNI_LIB_FUNCTION JNICALL JVM_LoadClass0(
	JNIEnv* env,
	jobject object,
	jclass currClass,
	jstring currClassName)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}
