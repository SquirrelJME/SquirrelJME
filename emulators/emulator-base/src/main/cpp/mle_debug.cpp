/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

JNIEXPORT jobjectArray JNICALL Impl_mle_DebugShelf_getThrowableTrace(
	JNIEnv* env, jclass classy, jobject thrown)
{
	return env->NewObjectArray(0, env->FindClass(
		"cc/squirreljme/jvm/mle/brackets/TracePointBracket"), NULL);
}

JNIEXPORT jobjectArray JNICALL Impl_mle_DebugShelf_traceStack(
	JNIEnv* env, jclass classy)
{
	return env->NewObjectArray(0, env->FindClass(
		"cc/squirreljme/jvm/mle/brackets/TracePointBracket"), NULL);
}

JNIEXPORT jint JNICALL Impl_mle_DebugShelf_verbose(
	JNIEnv* env, jclass classy, jint flags)
{
	// No capability exists here
	return 0;
}

JNIEXPORT void JNICALL Impl_mle_DebugShelf_verboseStop(
	JNIEnv* env, jclass classy, jint code)
{
	// No capability exists here
}

static const JNINativeMethod mleDebugMethods[] =
{
	{"getThrowableTrace", "(Ljava/lang/Throwable;)[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;",
		(void*)Impl_mle_DebugShelf_getThrowableTrace},
	{"traceStack", "()[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;",
		(void*)Impl_mle_DebugShelf_traceStack},
	{"verbose", "(I)I",
		(void*)Impl_mle_DebugShelf_verbose},
	{"verboseInternalThread", "(I)I",
		(void*)Impl_mle_DebugShelf_verbose},
	{"verboseStop", "(I)V",
		(void*)Impl_mle_DebugShelf_verboseStop},
};

jint JNICALL mleDebugInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/DebugShelf"),
		mleDebugMethods, sizeof(mleDebugMethods) /
			sizeof(JNINativeMethod));
}
