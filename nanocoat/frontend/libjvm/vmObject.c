/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>
#include <jvm.h>

#include "sjme/debug.h"

jint JNICALL JVM_IHashCode(JNIEnv* env, jobject obj)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_MonitorWait(JNIEnv* env, jobject obj, jlong ms)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_MonitorNotify(JNIEnv* env, jobject obj)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_MonitorNotifyAll(JNIEnv* env, jobject obj)
{
	sjme_todo("Impl?");
}

jobject JNICALL JVM_Clone(JNIEnv* env, jobject obj)
{
	sjme_todo("Impl?");
}

void* JNICALL JVM_RawMonitorCreate(void)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_RawMonitorDestroy(void* mon)
{
	sjme_todo("Impl?");
}

jint JNICALL JVM_RawMonitorEnter(void* mon)
{
	sjme_todo("Impl?");
}

void JNICALL JVM_RawMonitorExit(void* mon)
{
	sjme_todo("Impl?");
}
