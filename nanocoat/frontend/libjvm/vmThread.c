/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	#define WIN32_LEAN_AND_MEAN 1
	
	#include <windows.h>
#endif

#include <jni.h>
#include <jvm.h>

#include "sjme/debug.h"

void JNICALL JVM_StartThread(JNIEnv* env, jobject thread)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_StopThread(JNIEnv* env, jobject thread, jobject exception)
{
	sjme_todo("Impl?");
}

jboolean JNICALL JVM_IsThreadAlive(JNIEnv* env, jobject thread)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_SuspendThread(JNIEnv* env, jobject thread)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_ResumeThread(JNIEnv* env, jobject thread)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_SetThreadPriority(JNIEnv* env, jobject thread, jint prio)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_Yield(JNIEnv* env, jclass threadClass)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_Sleep(JNIEnv* env, jclass threadClass, jlong millis)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_CurrentThread(JNIEnv* env, jclass threadClass)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_CountStackFrames(JNIEnv* env, jobject thread)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_Interrupt(JNIEnv* env, jobject thread)
{
	sjme_todo("Impl?");
}

jboolean JNICALL JVM_IsInterrupted(JNIEnv* env,
	jobject thread,
	jboolean clearInterrupted)
{
	sjme_todo("Impl?");
}

jboolean JNICALL JVM_HoldsLock(JNIEnv* env, jclass threadClass, jobject obj)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_DumpAllStacks(JNIEnv* env, jclass unused)
{
	sjme_todo("Impl?");
}

jobjectArray JNICALL JVM_GetAllThreads(JNIEnv* env, jclass dummy)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_SetNativeThreadName(JNIEnv* env,
	jobject jthread,
	jstring name)
{
	sjme_todo("Impl?");
}

jobjectArray JNICALL JVM_DumpThreads(JNIEnv* env,
	jclass threadClass,
	jobjectArray threads)
{
	sjme_todo("Impl?");
}

jintArray JNICALL JVM_GetThreadStateValues(JNIEnv* env, jint javaThreadState)
{
	sjme_todo("Impl?");
}

jobjectArray JNICALL JVM_GetThreadStateNames(JNIEnv* env,
	jint javaThreadState,
	jintArray values)
{
	sjme_todo("Impl?");
}
