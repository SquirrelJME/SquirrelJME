/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_lineEnding(JNIEnv*, jclass)
{
#if defined(_WIN32)
	return 3;
#else
	return 1;
#endif
}

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_vmType(JNIEnv*, jclass)
{
	// The value 1 is Java SE type
	return 1;
}

static const JNINativeMethod mleRuntimeMethods[] =
{
	{"lineEnding", "()I", (void*)Impl_mle_RuntimeShelf_lineEnding},
	{"vmType", "()I", (void*)Impl_mle_RuntimeShelf_vmType},
};

jint JNICALL mleRuntimeInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/RuntimeShelf"),
		mleRuntimeMethods, sizeof(mleRuntimeMethods) /
			sizeof(JNINativeMethod));
}
