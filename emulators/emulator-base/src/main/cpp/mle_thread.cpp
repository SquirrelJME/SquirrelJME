/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

JNIEXPORT jint JNICALL Impl_mle_ThreadShelf_aliveThreadCount(JNIEnv* env,
	jclass classy, jboolean includeMain, jboolean includeDaemon)
{
	return forwardCallStaticInteger(env,
		"cc/squirreljme/emulator/NativeThreadShelf",
		"aliveThreadCount", "(ZZ)I", includeMain, includeDaemon);
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

static const JNINativeMethod mleThreadMethods[] =
{
	{"aliveThreadCount", "(ZZ)I",
		(void*)Impl_mle_ThreadShelf_aliveThreadCount},
	{"javaThreadSetDaemon", "(Ljava/lang/Thread;)V",
		(void*)Impl_mle_ThreadShelf_javaThreadSetDaemon},
	{"setTrace", "(Ljava/lang/String;[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;)V",
		(void*)Impl_mle_ThreadShelf_setTrace},
};

jint JNICALL mleThreadInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/ThreadShelf"),
		mleThreadMethods, sizeof(mleThreadMethods) /
			sizeof(JNINativeMethod));
}
