/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#ifndef __SQUIRRELJME_H__
#define __SQUIRRELJME_H__

#include "jni.h"
#include "sjme/debug.h"

/** Initializing methods. */
jint JNICALL mleDebugInit(JNIEnv* env, jclass classy);
jint JNICALL mleDylibBaseObjectInit(JNIEnv* env, jclass classy);
jint JNICALL mleFormInit(JNIEnv* env, jclass classy);
jint JNICALL mleJarInit(JNIEnv* env, jclass classy);
jint JNICALL mleMathInit(JNIEnv* env, jclass classy);
jint JNICALL mleMidiInit(JNIEnv* env, jclass classy);
jint JNICALL mleNativeArchiveInit(JNIEnv* env, jclass classy);
jint JNICALL mleNativeScritchDylibInit(JNIEnv* env, jclass classy);
jint JNICALL mleNativeScritchInterfaceInit(JNIEnv* env, jclass classy);
jint JNICALL mleObjectInit(JNIEnv* env, jclass classy);
jint JNICALL mlePencilInit(JNIEnv* env, jclass classy);
jint JNICALL mleReflectionInit(JNIEnv* env, jclass classy);
jint JNICALL mleRuntimeInit(JNIEnv* env, jclass classy);
jint JNICALL mleTaskInit(JNIEnv* env, jclass classy);
jint JNICALL mleTerminalInit(JNIEnv* env, jclass classy);
jint JNICALL mleTypeInit(JNIEnv* env, jclass classy);
jint JNICALL mleThreadInit(JNIEnv* env, jclass classy);

/** Useful macros, structures, and functions for forwarding. */
// Stores forwarded information
typedef struct forwardMethod
{
	jclass xclass;
	jmethodID xmeth;	
} forwardMethod;

// Find forwarded method
forwardMethod JNICALL findForwardMethod(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type);

// Call static methods
void JNICALL forwardCallStaticVoid(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);
jint JNICALL forwardCallStaticInteger(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);
jlong JNICALL forwardCallStaticLong(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);
jobject JNICALL forwardCallStaticObject(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);
jboolean JNICALL forwardCallStaticBoolean(JNIEnv* env,
	const char* const classy, const char* const name, const char* const type,
	...);

#define FORWARD_init(funcName, forwardFuncs) \
	jint JNICALL funcName(JNIEnv* env, jclass classy) \
	{ \
		return (*env)->RegisterNatives(env, \
			(*env)->FindClass(env, FORWARD_CLASS), \
			forwardFuncs, sizeof(forwardFuncs) / sizeof(JNINativeMethod)); \
	}
	
#define FORWARD_stringy(x) #x

#define FORWARD_paste(x, y) x ## y

#define FORWARD_from(x) x
	
#define FORWARD_list(className, methodName) \
	{FORWARD_stringy(methodName), \
	FORWARD_from(FORWARD_paste(FORWARD_DESC_, methodName)), \
	(void*)Impl_mle_ ## className ## _ ## methodName}

#define FORWARD_IMPL_none()

#define FORWARD_IMPL_args(...) , __VA_ARGS__

#define FORWARD_IMPL_pass(...) , __VA_ARGS__

#define FORWARD_IMPL_none()

#define FORWARD_FUNC_NAME(className, methodName) \
	Impl_mle_ ## className ## _ ## methodName

#define FORWARD_IMPL_VOID(className, methodName, args, pass) \
	JNIEXPORT void JNICALL Impl_mle_ ## className ## _ ## methodName( \
		JNIEnv* env, jclass classy args) \
	{ \
		forwardCallStaticVoid(env, FORWARD_NATIVE_CLASS, \
			FORWARD_stringy(methodName), \
			FORWARD_from(FORWARD_paste(FORWARD_DESC_, methodName)) \
			pass); \
	}

#define FORWARD_IMPL(className, methodName, rtype, rjava, args, pass) \
	JNIEXPORT rtype JNICALL Impl_mle_ ## className ## _ ## methodName( \
		JNIEnv* env, jclass classy args) \
	{ \
		return FORWARD_paste(forwardCallStatic, rjava)(env, \
			FORWARD_NATIVE_CLASS, \
			FORWARD_stringy(methodName), \
			FORWARD_from(FORWARD_paste(FORWARD_DESC_, methodName)) \
			pass); \
	}

#define DESC_ARRAY(x) "[" x
#define DESC_CLASS(x) "L" x ";"
#define DESC_BOOLEAN "Z"
#define DESC_BYTE "B"
#define DESC_SHORT "S"
#define DESC_CHAR "C"
#define DESC_CHARACTER DESC_CHAR
#define DESC_INT "I"
#define DESC_INTEGER DESC_INT
#define DESC_LONG "J"
#define DESC_FLOAT "F"
#define DESC_DOUBLE "D"
#define DESC_VOID "V"
#define DESC_OBJECT DESC_CLASS("java/lang/Object")
#define DESC_STRING DESC_CLASS("java/lang/String")
#define DESC_BYTE_BUFFER DESC_CLASS("java/nio/ByteBuffer")

/**
 * Checks to see if a virtual machine call failed.
 *
 * @param env The Java environment.
 * @return If there is an exception.
 * @since 2023/12/29
 */
sjme_jboolean sjme_jni_checkVMException(JNIEnv* env);

/**
 * Directly map integer array.
 * 
 * @param env The Java environment.
 * @param buf The input array to map.
 * @param off The offset into the array.
 * @param len The length of the array.
 * @return The resultant raw object for the array.
 * @since 2024/04/24
 */
jintArray sjme_jni_mappedArrayInt(JNIEnv* env,
	jint* buf, jint off, jint len);

/**
 * Throws a @c MLECallError .
 *
 * @param env The current Java environment.
 * @param code The error code.
 * @since 2024/04/16
 */
void sjme_jni_throwMLECallError(JNIEnv* env, sjme_errorCode code);

/**
 * Throws the given throwable type.
 *
 * @param env The current Java environment.
 * @param code The error code.
 * @param type The type of exception to throw.
 * @since 2024/04/16
 */
void sjme_jni_throwThrowable(JNIEnv* env, sjme_errorCode code,
	sjme_lpcstr type);

/**
 * Throws a @c VMException .
 *
 * @param env The current Java environment.
 * @param code The error code.
 * @since 2023/12/08
 */
void sjme_jni_throwVMException(JNIEnv* env, sjme_errorCode code);

#endif /* __SQUIRRELJME_H__ */

