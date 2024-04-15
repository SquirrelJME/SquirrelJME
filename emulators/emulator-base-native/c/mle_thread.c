/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

#define WAITFORUPDATE_DESC "(I)Z"

JNIEXPORT jint JNICALL Impl_mle_ThreadShelf_aliveThreadCount(JNIEnv* env,
	jclass classy, jboolean includeMain, jboolean includeDaemon)
{
	return forwardCallStaticInteger(env, 
		"cc/squirreljme/emulator/NativeThreadShelf",
		"aliveThreadCount", "(ZZ)I", includeMain, includeDaemon);
}

JNIEXPORT jobject JNICALL Impl_mle_ThreadShelf_currentJavaThread(JNIEnv* env,
	jclass classy)
{
	return forwardCallStaticObject(env, "java/lang/Thread",
		"currentThread", "()Ljava/lang/Thread;");
}

JNIEXPORT void JNICALL Impl_mle_ThreadShelf_javaThreadSetDaemon(JNIEnv* env,
	jclass classy, jobject javaThread)
{
	forwardCallStaticVoid(env, "cc/squirreljme/emulator/NativeThreadShelf",
		"javaThreadSetDaemon", "(Ljava/lang/Thread;)V", javaThread);
}
		
JNIEXPORT void JNICALL Impl_mle_ThreadShelf_setTrace(JNIEnv* env,
	jclass classy, jint fd, jobject string, jobject array)
{
	// Has no effect
}
		
JNIEXPORT jboolean JNICALL Impl_mle_ThreadShelf_waitForUpdate(JNIEnv* env,
	jclass classy, jint msWait)
{
	// Has no effect
	return forwardCallStaticBoolean(env, 
		"cc/squirreljme/emulator/NativeThreadShelf",
		"waitForUpdate", WAITFORUPDATE_DESC,
		msWait);
}

static const JNINativeMethod mleThreadMethods[] =
{
	{"aliveThreadCount", "(ZZ)I",
		(void*)Impl_mle_ThreadShelf_aliveThreadCount},
	{"currentJavaThread", "()Ljava/lang/Thread;",
		(void*)Impl_mle_ThreadShelf_currentJavaThread},
	{"javaThreadSetDaemon", "(Ljava/lang/Thread;)V",
		(void*)Impl_mle_ThreadShelf_javaThreadSetDaemon},
	{"setTrace", "(Ljava/lang/String;[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;)V",
		(void*)Impl_mle_ThreadShelf_setTrace},
	{"waitForUpdate", WAITFORUPDATE_DESC,
		(void*)Impl_mle_ThreadShelf_waitForUpdate},
};

jint JNICALL mleThreadInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/ThreadShelf"),
		mleThreadMethods, sizeof(mleThreadMethods) /
			sizeof(JNINativeMethod));
}
