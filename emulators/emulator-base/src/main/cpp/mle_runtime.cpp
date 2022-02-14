/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

JNIEXPORT void JNICALL Impl_mle_RuntimeShelf_garbageCollect(
	JNIEnv* env, jclass classy)
{
	// Does nothing
}

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_lineEnding(JNIEnv*, jclass)
{
#if defined(_WIN32)
	return 3;
#else
	return 1;
#endif
}

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_memoryProfile(JNIEnv*, jclass)
{
	// The value is normal
	return 0;
}

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_phoneModel(JNIEnv*, jclass)
{
	// Just be a generic device here
	return 0;
};

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_vmType(JNIEnv*, jclass)
{
	// The value 1 is Java SE type
	return 1;
}

static const JNINativeMethod mleRuntimeMethods[] =
{
	{"garbageCollect", "()V", (void*)Impl_mle_RuntimeShelf_garbageCollect},
	{"lineEnding", "()I", (void*)Impl_mle_RuntimeShelf_lineEnding},
	{"memoryProfile", "()I", (void*)Impl_mle_RuntimeShelf_memoryProfile},
	{"phoneModel", "()I", (void*)Impl_mle_RuntimeShelf_phoneModel},
	{"vmType", "()I", (void*)Impl_mle_RuntimeShelf_vmType},
};

jint JNICALL mleRuntimeInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/RuntimeShelf"),
		mleRuntimeMethods, sizeof(mleRuntimeMethods) /
			sizeof(JNINativeMethod));
}
