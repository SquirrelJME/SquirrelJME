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

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_Clone(
	JNIEnv* env, 
	jobject object)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_IHashCode(
	JNIEnv* env,
	jobject object)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_MonitorNotify(
	JNIEnv* env,
	jobject object)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_MonitorNotifyAll(
	JNIEnv* env, 
	jobject object)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_MonitorWait(
	JNIEnv* env,
	jobject object, 
	jlong millis)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void* SJME_JNI_LIB_FUNCTION JNICALL JVM_RawMonitorCreate(void)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_RawMonitorDestroy(
	void* rawMonitor)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_RawMonitorEnter(
	void* rawMonitor)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_RawMonitorExit(
	void* rawMonitor)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}
	
