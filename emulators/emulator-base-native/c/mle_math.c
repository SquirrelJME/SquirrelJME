/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include <stdint.h>

#include "squirreljme.h"

JNIEXPORT jlong JNICALL longPack(JNIEnv* env, jclass classy, jint al, jint ah)
{
	return (((jlong)al) & UINT64_C(0xFFFFFFFF)) |
		((((jlong)ah) & UINT64_C(0xFFFFFFFF)) << UINT64_C(32));
}

JNIEXPORT jint JNICALL longUnpackHigh(JNIEnv* env, jclass classy, jlong v)
{
	return (jint)(v >> UINT64_C(32));
}

JNIEXPORT jint JNICALL longUnpackLow(JNIEnv* env, jclass classy, jlong v)
{
	return (jint)(v);
}

static const JNINativeMethod mleMathMethods[] =
{
	{"longPack", "(II)J", (void*)longPack},
	{"longUnpackHigh", "(J)I", (void*)longUnpackHigh},
	{"longUnpackLow", "(J)I", (void*)longUnpackLow},
};

jint JNICALL mleMathInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/MathShelf"),
		mleMathMethods, sizeof(mleMathMethods) / sizeof(JNINativeMethod));
}
