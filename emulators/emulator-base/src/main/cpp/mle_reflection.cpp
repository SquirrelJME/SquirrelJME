/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"

#define REFLECTION_CLASSNAME "cc/squirreljme/emulator/EmulatedReflectionShelf"

#define INVOKEMAIN_DESC "(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;[Ljava/lang/String;)V"
#define REGISTERLOADER_DESC "(Lcc/squirreljme/jvm/mle/callbacks/ReflectiveLoaderCallback;)V"

JNIEXPORT void JNICALL Impl_mle_ReflectionShelf_invokeMain(JNIEnv* env,
	jclass classy, jobject type, jobject args)
{
	forwardCallStaticVoid(env, REFLECTION_CLASSNAME,
		"invokeMain", INVOKEMAIN_DESC,
		type, args);
}

JNIEXPORT void JNICALL Impl_mle_ReflectionShelf_registerLoader(JNIEnv* env,
	jclass classy, jobject loader)
{
	forwardCallStaticVoid(env, REFLECTION_CLASSNAME,
		"registerLoader", REGISTERLOADER_DESC,
		loader);
}

static const JNINativeMethod mleReflectionMethods[] =
{
	{"invokeMain", INVOKEMAIN_DESC, (void*)Impl_mle_ReflectionShelf_invokeMain},
	{"registerLoader", REGISTERLOADER_DESC, (void*)Impl_mle_ReflectionShelf_registerLoader},
};

jint JNICALL mleReflectionInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/ReflectionShelf"),
		mleReflectionMethods, sizeof(mleReflectionMethods) /
			sizeof(JNINativeMethod));
}
