/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdlib.h>

#if defined(_WIN32) || defined(_WIN64)
	#include <windows.h>
	#include <libloaderapi.h>
#elif defined(__APPLE__)
	#include <mach-o/dyld.h>
	#include <stdint.h>
#elif defined(__linux__) || defined(__linux)
	#include <unistd.h>
	#include <stdint.h>
#elif defined(__FreeBSD__)
	#include <sys/stat.h>
	#include <sys/sysctl.h>
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
#define NATIVE_EXEC_PATH_LEN 768
	char fileName[NATIVE_EXEC_PATH_LEN];
	
#if defined(__APPLE__)
	uint32_t fileNameLen;
#elif defined(__FreeBSD__)
	struct stat* statBuf;
	int sysCtlInput[4];
	size_t fileNameLen;
#elif defined(__sun) || defined(__illumos__)
	const char* bip;
#endif
	
	// Executable path of the VM binary (EXECUTABLE_PATH)
	if (id == 6)
	{
		// Clear buffer
		memset(fileName, 0, sizeof(fileName));
		
		// Use native system APIs to get this information
#if defined(_WIN32) || defined(_WIN64)
		GetModuleFileNameA(NULL, fileName, NATIVE_EXEC_PATH_LEN);
#elif defined(__APPLE__)
		fileNameLen = NATIVE_EXEC_PATH_LEN;
		_NSGetExecutablePath(fileName, &fileNameLen);
#elif defined(__linux__) || defined(__linux)
		readlink("/proc/self/exe", fileName, NATIVE_EXEC_PATH_LEN);
#elif defined(__NetBSD__) || defined(__DragonFly__)
		readlink("/proc/curproc/file", fileName, NATIVE_EXEC_PATH_LEN);
#elif defined(__FreeBSD__)
		memset(&statBuf, 0, sizeof(statBuf));
		if (stat("/proc/curproc/file", &statBuf) == 0)
			readlink("/proc/curproc/file", fileName, NATIVE_EXEC_PATH_LEN);
		else
		{
			// Setup systemctl input
			sysCtlInput[0] = CTL_KERN;
			sysCtlInput[1] = KERN_PROC;
			sysCtlInput[2] = KERN_PROC_PATHNAME;
			sysCtlInput[3] = -1;
			
			// Perform the call
			fileNameLen = NATIVE_EXEC_PATH_LEN;
			sysctl(mib, 4, fileName, &fileNameLen, NULL, 0);
		}
#elif defined(__sun) || defined(__illumos__)
		bip = getexecname();
		if (bip != NULL)
			strncpy(fileName, bip, NATIVE_EXEC_PATH_LEN - 1);
#endif
	
		// Convert to Java String if Valid
		if (fileName[0] != 0)
		{
			fileName[NATIVE_EXEC_PATH_LEN - 1] = 0;
			return (*env)->NewStringUTF(env, fileName);
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
