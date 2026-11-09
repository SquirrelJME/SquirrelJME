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

jint SJME_JNI_LIB_FUNCTION JNICALL JVM_CountStackFrames(
	JNIEnv* env,
	jobject thread)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jobject SJME_JNI_LIB_FUNCTION JNICALL JVM_CurrentThread(
	JNIEnv* env,
	jclass threadClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_DumpAllStacks(
	JNIEnv* env,
	jclass unused)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_DumpThreads(
	JNIEnv* env,
	jclass threadClass,
	jobjectArray threads)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetAllThreads(
	JNIEnv* env,
	jclass dummy)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jobjectArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetThreadStateNames(
	JNIEnv* env,
	jint javaThreadState,
	jintArray values)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jintArray SJME_JNI_LIB_FUNCTION JNICALL JVM_GetThreadStateValues(
	JNIEnv* env,
	jint javaThreadState)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NULL;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_HoldsLock(
	JNIEnv* env,
	jclass threadClass,
	jobject object)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_Interrupt(
	JNIEnv* env,
	jobject thread)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsInterrupted(
	JNIEnv* env,
	jobject thread,
	jboolean clearInterrupted)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

jboolean SJME_JNI_LIB_FUNCTION JNICALL JVM_IsThreadAlive(
	JNIEnv* env,
	jobject thread)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_ResumeThread(
	JNIEnv* env,
	jobject thread)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_SetNativeThreadName(
	JNIEnv* env,
	jobject thread,
	jstring name)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_SetThreadPriority(
	JNIEnv* env,
	jobject thread,
	jint priority)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_Sleep(
	JNIEnv* env,
	jclass threadClass,
	jlong millis)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_StartThread(
	JNIEnv* env,
	jobject thread)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_StopThread(
	JNIEnv* env,
	jobject thread,
	jobject exception)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_SuspendThread(
	JNIEnv* env,
	jobject thread)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}

void SJME_JNI_LIB_FUNCTION JNICALL JVM_Yield(
	JNIEnv* env,
	jclass threadClass)
{
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID;
}
