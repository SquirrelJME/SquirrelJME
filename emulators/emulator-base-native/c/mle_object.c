/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "squirreljme.h"

// The class to forward to
#define OBJECTSHELF_CLASSNAME "cc/squirreljme/emulator/EmulatedObjectShelf"
 
#define ARRAY_FILL(javaType, desc) \
	JNIEXPORT void JNICALL Impl_mle_ObjectShelf_arrayFill_##javaType( \
		JNIEnv* env, jclass classy, \
		jarray array, jint off, jint len, javaType val) \
		{ \
			forwardCallStaticVoid(env, \
				OBJECTSHELF_CLASSNAME, \
				"arrayFill", desc, \
				array, off, len, val); \
		}

ARRAY_FILL(jboolean, "([ZIIZ)V");
ARRAY_FILL(jbyte, "([BIIB)V");
ARRAY_FILL(jshort, "([SIIS)V");
ARRAY_FILL(jchar, "([CIIC)V");
ARRAY_FILL(jint, "([IIII)V");
ARRAY_FILL(jlong, "([JIIJ)V");
ARRAY_FILL(jfloat, "([FIIF)V");
ARRAY_FILL(jdouble, "([DIID)V");

JNIEXPORT jint JNICALL Impl_mle_ObjectShelf_arrayLength(JNIEnv* env,
	jclass classy, jarray array)
{
	jclass classClass;
	jmethodID classIsArrayId;

	if (array == NULL)
		return -1;

	// Determine if this is an array or not
	classClass = (*env)->FindClass(env, "java/lang/Class");
	classIsArrayId = (*env)->GetMethodID(env, classClass, "isArray", "()Z");
	if (JNI_FALSE == (*env)->CallBooleanMethod(env,
		(*env)->GetObjectClass(env, array),
		classIsArrayId))
		return -1;

	return (*env)->GetArrayLength(env, array);
}

static const JNINativeMethod mleObjectMethods[] =
{
	{"arrayLength", "(Ljava/lang/Object;)I",
		(void*)Impl_mle_ObjectShelf_arrayLength},
	
	{"arrayFill", "([ZIIZ)V",
		(void*)Impl_mle_ObjectShelf_arrayFill_jboolean},
	{"arrayFill", "([BIIB)V",
		(void*)Impl_mle_ObjectShelf_arrayFill_jbyte},
	{"arrayFill", "([SIIS)V",
		(void*)Impl_mle_ObjectShelf_arrayFill_jshort},
	{"arrayFill", "([CIIC)V",
		(void*)Impl_mle_ObjectShelf_arrayFill_jchar},
	{"arrayFill", "([IIII)V",
		(void*)Impl_mle_ObjectShelf_arrayFill_jint},
	{"arrayFill", "([JIIJ)V",
		(void*)Impl_mle_ObjectShelf_arrayFill_jlong},
	{"arrayFill", "([FIIF)V",
		(void*)Impl_mle_ObjectShelf_arrayFill_jfloat},
	{"arrayFill", "([DIID)V",
		(void*)Impl_mle_ObjectShelf_arrayFill_jdouble},
};

jint JNICALL mleObjectInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/ObjectShelf"),
		mleObjectMethods, sizeof(mleObjectMethods) /
			sizeof(JNINativeMethod));
}
