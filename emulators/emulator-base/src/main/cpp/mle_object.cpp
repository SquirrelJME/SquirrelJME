/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

JNIEXPORT jint JNICALL Impl_mle_ObjectShelf_arrayLength(JNIEnv* env,
	jclass classy, jarray array)
{
	jclass classClass;
	jmethodID classIsArrayId;

	if (array == NULL)
		return -1;

	// Determine if this is an array or not
	classClass = env->FindClass("java/lang/Class");
	classIsArrayId = env->GetMethodID(classClass, "isArray", "()Z");
	if (JNI_FALSE == env->CallBooleanMethod(env->GetObjectClass(array),
		classIsArrayId))
		return -1;

	return env->GetArrayLength(array);
}

static const JNINativeMethod mleObjectMethods[] =
{
	{"arrayLength", "(Ljava/lang/Object;)I",
		(void*)Impl_mle_ObjectShelf_arrayLength},
};

jint JNICALL mleObjectInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/ObjectShelf"),
		mleObjectMethods, sizeof(mleObjectMethods) /
			sizeof(JNINativeMethod));
}
