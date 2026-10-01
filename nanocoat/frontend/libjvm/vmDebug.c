/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/libjvm/commonJniJvm.h"

void* JNICALL JVM_RegisterSignal(jint sig, void* handler)
{
	sjme_todo("Impl?");
	return NULL;
}

jboolean JNICALL JVM_RaiseSignal(jint sig)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jint JNICALL JVM_FindSignal(const char* name)
{
	sjme_todo("Impl?");
	return 0;
}

jboolean JNICALL JVM_DesiredAssertionStatus(JNIEnv* env,
	jclass unused,
	jclass cls)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jobject JNICALL JVM_AssertionStatusDirectives(JNIEnv* env, jclass unused)
{
	sjme_todo("Impl?");
	return NULL;
}

jint JNICALL JVM_DTraceGetVersion(JNIEnv* env)
{
	sjme_todo("Impl?");
	return 0;
}

jlong JNICALL JVM_DTraceActivate(JNIEnv* env,
	jint version,
	jstring module_name,
	jint providers_count,
	JVM_DTraceProvider* providers)
{
	sjme_todo("Impl?");
	return 0;
}

jboolean JNICALL JVM_DTraceIsProbeEnabled(JNIEnv* env, jmethodID method)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

void JNICALL JVM_DTraceDispose(JNIEnv* env, jlong activation_handle)
{
	sjme_todo("Impl?");
}

jboolean JNICALL JVM_DTraceIsSupported(JNIEnv* env)
{
	sjme_todo("Impl?");
	return JNI_FALSE;
}

jobject JNICALL JVM_InitAgentProperties(JNIEnv* env, jobject agent_props)
{
	sjme_todo("Impl?");
	return NULL;
}
