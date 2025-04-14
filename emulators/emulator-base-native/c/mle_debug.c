/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdlib.h>

#include "squirreljme.h"

// The class to forward to
#define DEBUGSHELF_CLASSNAME "cc/squirreljme/emulator/EmulatedDebugShelf"

#define DEBUGSHELF_BREAKPOINT_DESC "()V"
#define DEBUGSHELF_POINTCLASS_DESC "(Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;)Ljava/lang/String;"
#define DEBUGSHELF_TRACESTACK_DESC "()[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;"

JNIEXPORT void Impl_mle_DebugShelf_breakpoint(
	JNIEnv* env, jclass classy)
{
	return forwardCallStaticVoid(env, DEBUGSHELF_CLASSNAME,
		"breakpoint", DEBUGSHELF_BREAKPOINT_DESC);
}

JNIEXPORT jobjectArray JNICALL Impl_mle_DebugShelf_getThrowableTrace(
	JNIEnv* env, jclass classy, jobject thrown)
{
	return (*env)->NewObjectArray(env, 0, (*env)->FindClass(env,
		"cc/squirreljme/jvm/mle/brackets/TracePointBracket"), NULL);
}

JNIEXPORT jobject JNICALL Impl_mle_DebugShelf_pointClass(
	JNIEnv* env, jclass classy, jobject trace)
{
	return forwardCallStaticObject(env, DEBUGSHELF_CLASSNAME,
		"pointClass", DEBUGSHELF_POINTCLASS_DESC, trace);
}

JNIEXPORT jobject JNICALL Impl_mle_DebugShelf_traceStack(
	JNIEnv* env, jclass classy)
{
	return forwardCallStaticObject(env, DEBUGSHELF_CLASSNAME,
		"traceStack", DEBUGSHELF_TRACESTACK_DESC);
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
	{"breakpoint", DEBUGSHELF_BREAKPOINT_DESC,
		(void*)Impl_mle_DebugShelf_breakpoint},
	{"getThrowableTrace", "(Ljava/lang/Throwable;)[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;",
		(void*)Impl_mle_DebugShelf_getThrowableTrace},
	{"pointClass", DEBUGSHELF_POINTCLASS_DESC, (void*)Impl_mle_DebugShelf_pointClass},
	{"traceStack", DEBUGSHELF_TRACESTACK_DESC, (void*)Impl_mle_DebugShelf_traceStack},
	{"verbose", "(I)I",
		(void*)Impl_mle_DebugShelf_verbose},
	{"verboseInternalThread", "(I)I",
		(void*)Impl_mle_DebugShelf_verbose},
	{"verboseStop", "(I)V",
		(void*)Impl_mle_DebugShelf_verboseStop},
};

jint JNICALL mleDebugInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/DebugShelf"),
		mleDebugMethods, sizeof(mleDebugMethods) /
			sizeof(JNINativeMethod));
}
