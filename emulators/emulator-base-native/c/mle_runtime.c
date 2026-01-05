/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdlib.h>

#include "squirreljme.h"
#include "sjme/path.h"

#define RUNTIME_CLASSNAME "cc/squirreljme/emulator/EmulatedRuntimeShelf"

#define RUNTIME_MEMORYPROFILE_DESC "()I"
#define RUNTIME_SYSTEMENV_DESC "(Ljava/lang/String;)Ljava/lang/String;"
#define RUNTIME_VMDESCRIPTION_DESC "(I)Ljava/lang/String;"
#define RUNTIME_VMSTATISTIC_DESC "(I)J"

#define FORWARD_CLASS "cc/squirreljme/jvm/mle/RuntimeShelf"
#define FORWARD_NATIVE_CLASS RUNTIME_CLASSNAME

#define FORWARD_DESC_compatibilityId \
	DESC_METHOD(DESC_BOOLEAN, DESC_INT)
#define FORWARD_DESC_browseLocal \
	DESC_METHOD(DESC_VOID, DESC_BOOLEAN DESC_STRING)

JNIEXPORT jboolean JNICALL FORWARD_FUNC_NAME(Runtime, compatibilityId)
	(JNIEnv* env, jclass classy, jint id)
{
	/* For now everything returns false. */
	return JNI_FALSE;
}

FORWARD_IMPL_VOID(Runtime, browseLocal,
	FORWARD_IMPL_args(jboolean create, jstring path),
	FORWARD_IMPL_pass(create, path))

JNIEXPORT void JNICALL Impl_mle_RuntimeShelf_garbageCollect(
	JNIEnv* env, jclass classy)
{
	// Does nothing
}

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_lineEnding(
	JNIEnv* env, jclass classy)
{
#if defined(_WIN32)
	return 3;
#else
	return 1;
#endif
}

JNIEXPORT jstring JNICALL Impl_mle_RuntimeShelf_vmDescription(
	JNIEnv* env, jclass classy, jint id)
{
	sjme_errorCode error;
	char fileName[SJME_MAX_PATH];

	// Executable path of the VM binary (EXECUTABLE_PATH)
	if (id == 6)
	{
		// Use NAL API
		memset(fileName, 0, sizeof(fileName));
		if (sjme_nal_default.execPath != NULL)
			if (sjme_error_is(error = sjme_nal_default.execPath(fileName,
				SJME_MAX_PATH)))
			{
				// Invalidate the path, do not return any string
				memset(fileName, 0, sizeof(fileName));

				// Setup exception to be thrown
				sjme_jni_throwMLECallError(env, error);
			}

		// Convert to Java String if Valid
		if (fileName[0] != 0)
		{
			fileName[SJME_MAX_PATH - 1] = 0;
			return (*env)->NewStringUTF(env, fileName);
		}

		// Not a valid executable path
		return NULL;
	}

	return (jstring)forwardCallStaticObject(env, RUNTIME_CLASSNAME,
		"vmDescription", RUNTIME_VMDESCRIPTION_DESC,
		id);
}

JNIEXPORT jlong JNICALL Impl_mle_RuntimeShelf_vmStatistic(
	JNIEnv* env, jclass classy, jint id)
{
	return forwardCallStaticLong(env, RUNTIME_CLASSNAME,
		"vmStatistic", RUNTIME_VMSTATISTIC_DESC,
		id);
}

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_memoryProfile(
	JNIEnv* env, jclass classy)
{
	// The value is normal
	return 0;
}

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_phoneModel(
	JNIEnv* env, jclass classy)
{
	// Just be a generic device here
	return 0;
};

JNIEXPORT jobject JNICALL Impl_mle_RuntimeShelf_systemEnv(
	JNIEnv* env, jclass classy, jstring key)
{
	return forwardCallStaticObject(env, RUNTIME_CLASSNAME,
		"systemEnv", RUNTIME_SYSTEMENV_DESC,
		key);
}

JNIEXPORT jint JNICALL Impl_mle_RuntimeShelf_vmType(
	JNIEnv* env, jclass classy)
{
	// The value 1 is Java SE type
	return 1;
}

static const JNINativeMethod mleRuntimeMethods[] =
{
	FORWARD_list(Runtime, browseLocal),
	FORWARD_list(Runtime, compatibilityId),
	{"garbageCollect", "()V", (void*)Impl_mle_RuntimeShelf_garbageCollect},
	{"lineEnding", "()I", (void*)Impl_mle_RuntimeShelf_lineEnding},
	{"memoryProfile", RUNTIME_MEMORYPROFILE_DESC, (void*)Impl_mle_RuntimeShelf_memoryProfile},
	{"phoneModel", "()I", (void*)Impl_mle_RuntimeShelf_phoneModel},
	{"systemEnv", RUNTIME_SYSTEMENV_DESC, (void*)Impl_mle_RuntimeShelf_systemEnv},
	{"vmDescription", RUNTIME_VMDESCRIPTION_DESC, (void*)Impl_mle_RuntimeShelf_vmDescription},
	{"vmStatistic", RUNTIME_VMSTATISTIC_DESC, (void*)Impl_mle_RuntimeShelf_vmStatistic},
	{"vmType", "()I", (void*)Impl_mle_RuntimeShelf_vmType},
};

jint JNICALL mleRuntimeInit(JNIEnv* env, jclass classy)
{
	return (*env)->RegisterNatives(env,
		(*env)->FindClass(env, "cc/squirreljme/jvm/mle/RuntimeShelf"),
		mleRuntimeMethods, sizeof(mleRuntimeMethods) /
			sizeof(JNINativeMethod));
}
