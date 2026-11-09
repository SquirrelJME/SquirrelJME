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

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_AssertionStatusDirectives(
	JNIEnv* env,
	jclass unused)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_DesiredAssertionStatus(
	JNIEnv* env,
	jclass unused,
	jclass classy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jlong SJME_JNI_LIB_FUNCTION JNICALL JVM_DTraceActivate(
	JNIEnv* env,
	jint version,
	jstring moduleName,
	jint providersCount,
	JVM_DTraceProvider* providers)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_JLONG;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_DTraceDispose(
	JNIEnv* env, 
	jlong activationHandle)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_DTraceGetVersion(
	JNIEnv* env)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_DTraceIsProbeEnabled(
	JNIEnv* env, 
	jmethodID method)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_DTraceIsSupported(
	JNIEnv* env)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_FindSignal(
	const char* name)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_InitAgentProperties(
	JNIEnv* env,
	jobject agentProps)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_RaiseSignal(
	jint signalId)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void* SJME_JNI_LIB_FUNCTION JNICALL JVM_RegisterSignal(
	jint signalId, 
	void* handler)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}
