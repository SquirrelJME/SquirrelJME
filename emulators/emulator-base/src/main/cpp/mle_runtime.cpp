/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#if defined(_WIN32) || defined(_WIN64)
	#include <windows.h>
	#include <libloaderapi.h>
#elif defined(__APPLE__)
	#include <mach-o/dyld.h>
	#include <stdint.h>
#elif defined(__linux__) || defined(__linux)
	#include <unistd.h>
	#include <stdint.h>
#endif

#include "squirreljme.h"

#define RUNTIME_CLASSNAME "cc/squirreljme/emulator/EmulatedRuntimeShelf"

#define RUNTIME_MEMORYPROFILE_DESC "()I"
#define RUNTIME_SYSTEMENV_DESC "(Ljava/lang/String;)Ljava/lang/String;"
#define RUNTIME_VMDESCRIPTION_DESC "(I)Ljava/lang/String;"
#define RUNTIME_VMSTATISTIC_DESC "(I)J"

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

JNIEXPORT jstring JNICALL Impl_mle_RuntimeShelf_vmDescription(
	JNIEnv* env, jclass classy, jint id)
{
#define NATIVE_EXEC_PATH_LEN 768
	char fileName[NATIVE_EXEC_PATH_LEN];
#if defined(__APPLE__)
	uint32_t fileNameLen;
#endif
	
	// Executable path of the VM binary (EXECUTABLE_PATH)
	if (id == 6)
	{
		// Clear buffer
		memset(fileName, 0, sizeof(fileName));
		
#if defined(_WIN32) || defined(_WIN64)
		GetModuleFileNameA(NULL, fileName, NATIVE_EXEC_PATH_LEN);
#elif defined(__APPLE__)
		fileNameLen = NATIVE_EXEC_PATH_LEN;
		_NSGetExecutablePath(fileName, &fileNameLen);
#elif defined(__linux__) || defined(__linux)
		readlink("/proc/self/exe", fileName, NATIVE_EXEC_PATH_LEN);
#endif
	
		// Convert to Java String if Valid
		if (fileName[0] != 0)
		{
			fileName[NATIVE_EXEC_PATH_LEN - 1] = 0;
			return env->NewStringUTF(fileName);
		}
	}
	
	return (jstring)forwardCallStaticObject(env, RUNTIME_CLASSNAME,
		"vmDescription", RUNTIME_VMDESCRIPTION_DESC,
		id);
#undef NATIVE_EXEC_PATH_LEN
}

JNIEXPORT jlong JNICALL Impl_mle_RuntimeShelf_vmStatistic(
	JNIEnv* env, jclass classy, jint id)
{
	return forwardCallStaticLong(env, RUNTIME_CLASSNAME,
		"vmStatistic", RUNTIME_VMSTATISTIC_DESC,
		id);
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

JNIEXPORT jobject JNICALL Impl_mle_RuntimeShelf_systemEnv(
	JNIEnv* env, jclass classy, jstring key)
{
	return forwardCallStaticObject(env, RUNTIME_CLASSNAME,
		"systemEnv", RUNTIME_SYSTEMENV_DESC,
		key);
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
	{"phoneModel", "()I", (void*)Impl_mle_RuntimeShelf_phoneModel},
	{"systemEnv", RUNTIME_SYSTEMENV_DESC, (void*)Impl_mle_RuntimeShelf_systemEnv},
	{"vmDescription", RUNTIME_VMDESCRIPTION_DESC, (void*)Impl_mle_RuntimeShelf_vmDescription},
	{"vmStatistic", RUNTIME_VMSTATISTIC_DESC, (void*)Impl_mle_RuntimeShelf_vmStatistic},
	{"vmType", "()I", (void*)Impl_mle_RuntimeShelf_vmType},
};

jint JNICALL mleRuntimeInit(JNIEnv* env, jclass classy)
{
	return env->RegisterNatives(
		env->FindClass("cc/squirreljme/jvm/mle/RuntimeShelf"),
		mleRuntimeMethods, sizeof(mleRuntimeMethods) /
			sizeof(JNINativeMethod));
}
