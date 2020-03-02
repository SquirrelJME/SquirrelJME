/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdint.h>

#include "jni.h"
#include "cc_squirreljme_jvm_Assembly.h"

// Recycle these methods because they do more advanced handling as needed
#define CAST_JOBJECT(v) \
	Java_cc_squirreljme_jvm_Assembly_pointerToObject(env, classy, (v))
#define CAST_JLONG(v) \
	Java_cc_squirreljme_jvm_Assembly_objectToPointer(env, classy, (v))

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_arrayLength__J
	(JNIEnv* env, jclass classy, jlong array)
{
	return Java_cc_squirreljme_jvm_Assembly_arrayLength__Ljava_lang_Object_2(
		env, classy, CAST_JOBJECT(array));
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_arrayLength__Ljava_lang_Object_2
	(JNIEnv* env, jclass classy, jobject array)
{
	jint len;

	// Is null pointer
	if (array == NULL)
		return -1;

	// Only if the given class is an actual array can we get the length of it
	if (env->IsInstanceOf(array, env->FindClass("[Ljava/lang/Object;")) ||
		env->IsInstanceOf(array, env->FindClass("[Z")) ||
		env->IsInstanceOf(array, env->FindClass("[B")) ||
		env->IsInstanceOf(array, env->FindClass("[S")) ||
		env->IsInstanceOf(array, env->FindClass("[C")) ||
		env->IsInstanceOf(array, env->FindClass("[I")) ||
		env->IsInstanceOf(array, env->FindClass("[J")) ||
		env->IsInstanceOf(array, env->FindClass("[F")) ||
		env->IsInstanceOf(array, env->FindClass("[D")))
		return env->GetArrayLength((jarray)array);

	// Not an array
	return -1;
}

JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_longPack
	(JNIEnv* env, jclass classy, jint hi, jint lo)
{
	return ((((jlong)hi) & INT64_C(0xFFFFFFFF)) << INT64_C(32)) |
		((((jlong)lo)) & INT64_C(0xFFFFFFFF));
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_longUnpackHigh
	(JNIEnv* env, jclass classy, jlong value)
{
	return (jint)(value >> INT64_C(32));
}

JNIEXPORT jint JNICALL Java_cc_squirreljme_jvm_Assembly_longUnpackLow
	(JNIEnv* env, jclass classy, jlong value)
{
	return (jint)(value & INT64_C(0xFFFFFFFF));
}

JNIEXPORT jlong JNICALL Java_cc_squirreljme_jvm_Assembly_objectToPointer
	(JNIEnv* env, jclass classy, jobject object)
{
	jobject global;

	// Null references are just zero
	if (object == NULL)
		return (jlong)0;

	// Create global reference to object first
	global = env->NewGlobalRef(object);
	if (global == NULL)
	{
		env->ThrowNew(env->FindClass("java/lang/OutOfMemoryError"),
			"Could not box object as global.");
		return 0;
	}

	// We return the pointer to our global object
	return (jlong)((intptr_t)global);
}

JNIEXPORT jobject JNICALL Java_cc_squirreljme_jvm_Assembly_pointerToObject
	(JNIEnv* env, jclass classy, jlong object)
{
	// A pointer of zero is just NULL
	if (object == (jlong)0)
		return NULL;

	// We will always be returning the global reference because this is an
	// object that has a lifetime outside of the class
	return (jobject)((intptr_t)object);
}