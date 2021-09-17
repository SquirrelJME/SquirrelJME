/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

#define RUNTIME_CLASSNAME "cc/squirreljme/emulator/EmulatedRuntimeShelf"

#define RUNTIME_MEMORYPROFILE_DESC "()I"
#define RUNTIME_VMDESCRIPTION_DESC "(I)Ljava/lang/String;"

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
	// Normal memory profile
	return 0;
}

JNIEXPORT jstring JNICALL Impl_mle_RuntimeShelf_vmDescription(
	JNIEnv* env, jclass classy, jint id)
{
	return (jstring)forwardCallStaticObject(env, RUNTIME_CLASSNAME,
		"vmDescription", RUNTIME_VMDESCRIPTION_DESC,
		id);
}

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_vmType(JNIEnv*, jclass)
{
	// The value 1 is Java SE type
	return 1;
}

static const JNINativeMethod mleRuntimeMethods[] =
{
	{"garbageCollect", "()V", (void*)Impl_mle_RuntimeShelf_garbageCollect},
	{"lineEnding", "()I", (void*)Impl_mle_RuntimeShelf_lineEnding},
	{"memoryProfile", RUNTIME_MEMORYPROFILE_DESC, (void*)Impl_mle_RuntimeShelf_memoryProfile},
	{"vmDescription", RUNTIME_VMDESCRIPTION_DESC, (void*)Impl_mle_RuntimeShelf_vmDescription},
	{"vmType", "()I", (void*)Impl_mle_RuntimeShelf_vmType},
};

jint JNICALL mleRuntimeInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/RuntimeShelf"),
		mleRuntimeMethods, sizeof(mleRuntimeMethods) /
			sizeof(JNINativeMethod));
}
