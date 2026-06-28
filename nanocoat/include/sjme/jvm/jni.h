/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * JNI Compatibility header.
 * 
 * @since 2025/10/22
 */

#ifndef SJME_C_SQUIRRELJME_JNI_H
#define SJME_C_SQUIRRELJME_JNI_H

#include "sjme/config.h"
#include "sjme/jvm/jni_md.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_JNI_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Constructs a JNI version. */
#define SJME_JNI_VERSION(major, minor) \
	(((INT32_C(major)) << INT32_C(16)) | (INT32_C(minor)))

/** JNI 1.1. */
#define JNI_VERSION_1_1 SJME_JNI_VERSION(1, 1)

/** JNI 1.2. */
#define JNI_VERSION_1_2 SJME_JNI_VERSION(1, 2)

/** JNI 1.4. */
#define JNI_VERSION_1_4 SJME_JNI_VERSION(1, 4)

/** JNI 1.6. */
#define JNI_VERSION_1_6 SJME_JNI_VERSION(1, 6)

/** JNI 1.8. */
#define JNI_VERSION_1_8 SJME_JNI_VERSION(1, 8)

/** Java Virtual Machine Structure Pointer. */
typedef const struct JVMNativeInterface* JavaVM;

/** Java Native Interface Structure Pointer. */
typedef const struct JNINativeInterface* JNIEnv;

#pragma region(incompleteTypes)

/**
 * Simplifies incomplete types.
 *
 * @param x The name of the type.
 * @since 2026/06/16
 */
#define SJME_JNI_INCOMPLETE__(x) \
	typedef struct SJME_TOKEN_PASTE3(sjme_jni_incomplete_, x, __)* x

/** Object type. */
SJME_JNI_INCOMPLETE__(jobject);

/** Class type. */
SJME_JNI_INCOMPLETE__(jclass);

/** Throwable type. */
SJME_JNI_INCOMPLETE__(jthrowable);

/** String type. */
SJME_JNI_INCOMPLETE__(jstring);

/** Object array type. */
SJME_JNI_INCOMPLETE__(jobjectArray);

/** Byte array type. */
SJME_JNI_INCOMPLETE__(jbyteArray);

/** Boolean array type. */
SJME_JNI_INCOMPLETE__(jooleanArray);

/** Short array type. */
SJME_JNI_INCOMPLETE__(jshortArray);

/** Character array type. */
SJME_JNI_INCOMPLETE__(jcharArray);

/** Integer array type. */
SJME_JNI_INCOMPLETE__(jintArray);

/** Long array type. */
SJME_JNI_INCOMPLETE__(jlongArray);

/** Float array type. */
SJME_JNI_INCOMPLETE__(jfloatArray);

/** Double array type. */
SJME_JNI_INCOMPLETE__(jdoubleArray);

/** Method identifier. */
SJME_JNI_INCOMPLETE__(jmethodID);

/** Field identifier. */
SJME_JNI_INCOMPLETE__(jfieldID);

/* No longer needed. */
#undef SJME_JNI_INCOMPLETE__

#pragma endregion(incompleteTypes)
#pragma region(standardStructs)

/** Any value. */
typedef union jvalue
{
	/** Boolean. */
	jboolean z;

	/** Byte. */
	jbyte b;

	/** Character. */
	jchar c;

	/** Short. */
	jshort s;

	/** Integer. */
	jint i;

	/** Long. */
	jlong j;

	/** Float. */
	jfloat f;

	/** Double. */
	jdouble d;

	/** Object. */
	jobject l;
} jvalue;

/**
 * Stores a command line option.
 *
 * @since 2026/06/17
 */
typedef struct JavaVMOption
{
	/** The option string. */
	char* optionString;

	/** Any extra information for the option. */
	void* extraInfo;
} JavaVMOption;

/**
 * JVM initialization settings.
 *
 * @since 2026/06/17
 */
typedef struct JavaVMInitArgs
{
	/** The version identifier. */
	jint version;

	/** The number of command line arguments passed. */
	jint nOptions;

	/** The command line arguments passed. */
	JavaVMOption* options;

	/** Ignore unrecognized options? */
	jboolean ignoreUnrecognized;
} JavaVMInitArgs;

/**
 * Options used to attach to a thread.
 *
 * @since 2026/06/17
 */
typedef struct JavaVMAttachArgs
{
	/** The version identifier. */
	jint version;

	/** The name of the thread. */
	char* name;

	/** The thread group. */
	jobject group;
} JavaVMAttachArgs;

#pragma endregion(standardStructs)
#pragma region(jvmFunctions)
	
typedef sjme_undefinedExportFunction sjme_jni_TodoFunc;

typedef sjme_jni_TodoFunc sjme_jni_jniJvmDestroyJavaVMFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniJvmAttachCurrentThreadFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniJvmDetachCurrentThreadFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniJvmGetEnvFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniJvmAttachCurrentThreadAsDaemonFunc;

#pragma endregion(jvmFunctions)
#pragma region(jniFunctions)

typedef sjme_jni_TodoFunc sjme_jni_jniEnvAllocObjectFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallBooleanMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallBooleanMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallBooleanMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallByteMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallByteMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallByteMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallCharMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallCharMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallCharMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallDoubleMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallDoubleMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallDoubleMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallFloatMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallFloatMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallFloatMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallIntMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallIntMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallIntMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallLongMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallLongMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallLongMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualBooleanMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualBooleanMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualBooleanMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualByteMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualByteMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualByteMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualCharMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualCharMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualCharMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualDoubleMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualDoubleMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualDoubleMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualFloatMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualFloatMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualFloatMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualIntMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualIntMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualIntMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualLongMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualLongMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualLongMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualObjectMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualObjectMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualObjectMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualShortMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualShortMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualShortMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualVoidMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualVoidMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallNonvirtualVoidMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallObjectMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallObjectMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallObjectMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallShortMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallShortMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallShortMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticBooleanMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticBooleanMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticBooleanMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticByteMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticByteMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticByteMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticCharMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticCharMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticCharMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticDoubleMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticDoubleMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticDoubleMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticFloatMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticFloatMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticFloatMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticIntMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticIntMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticIntMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticLongMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticLongMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticLongMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticObjectMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticObjectMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticObjectMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticShortMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticShortMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticShortMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticVoidMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticVoidMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallStaticVoidMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallVoidMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallVoidMethodAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvCallVoidMethodVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvDefineClassFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvDeleteGlobalRefFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvDeleteLocalRefFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvDeleteWeakGlobalRefFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvEnsureLocalCapacityFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvExceptionCheckFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvExceptionClearFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvExceptionDescribeFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvExceptionOccurredFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvFatalErrorFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvFindClassFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvFromReflectedFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvFromReflectedMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetArrayLengthFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetBooleanArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetBooleanArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetBooleanFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetByteArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetByteArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetByteFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetCharArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetCharArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetCharFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetDirectBufferAddressFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetDirectBufferCapacityFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetDoubleArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetDoubleArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetDoubleFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetFieldIDFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetFloatArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetFloatArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetFloatFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetIntArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetIntArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetIntFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetJavaVMFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetLongArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetLongArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetLongFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetMethodIDFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetModuleFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetObjectArrayElementFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetObjectClassFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetObjectFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetObjectRefTypeFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetPrimitiveArrayCriticalFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetShortArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetShortArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetShortFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticBooleanFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticByteFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticCharFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticDoubleFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticFieldIDFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticFloatFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticIntFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticLongFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticMethodIDFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticObjectFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStaticShortFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStringCharsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStringCriticalFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStringLengthFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStringRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStringUTFCharsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStringUTFLengthFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetStringUTFRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetSuperclassFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvGetVersionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvIsAssignableFromFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvIsInstanceOfFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvIsSameObjectFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvMonitorEnterFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvMonitorExitFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewBooleanArrayFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewByteArrayFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewCharArrayFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewDirectByteBufferFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewDoubleArrayFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewFloatArrayFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewGlobalRefFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewIntArrayFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewLocalRefFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewLongArrayFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewObjectFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewObjectAFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewObjectArrayFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewObjectVFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewShortArrayFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewStringFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewStringUTFFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvNewWeakGlobalRefFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvPopLocalFrameFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvPushLocalFrameFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvRegisterNativesFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseBooleanArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseByteArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseCharArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseDoubleArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseFloatArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseIntArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseLongArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleasePrimitiveArrayCriticalFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseShortArrayElementsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseStringCharsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseStringCriticalFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvReleaseStringUTFCharsFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetBooleanArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetBooleanFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetByteArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetByteFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetCharArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetCharFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetDoubleArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetDoubleFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetFloatArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetFloatFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetIntArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetIntFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetLongArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetLongFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetObjectArrayElementFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetObjectFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetShortArrayRegionFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetShortFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetStaticBooleanFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetStaticByteFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetStaticCharFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetStaticDoubleFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetStaticFloatFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetStaticIntFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetStaticLongFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetStaticObjectFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvSetStaticShortFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvThrowFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvThrowNewFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvToReflectedFieldFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvToReflectedMethodFunc;
typedef sjme_jni_TodoFunc sjme_jni_jniEnvUnregisterNativesFunc;

#pragma endregion(jniFunctions)

/**
 * Simplifies the massive amount of copy-paste for the JNI headers since it
 * is very nice for them to have clean typedefs.
 *
 * @param g The group this is in.
 * @param x The function name.
 * @since 2026/06/17
 */
#define SJME_JNI_FUNC__(group, x) \
	SJME_TOKEN_PASTE4(sjme_jni_jni, group, x, Func) x

/**
 * Native Java Virtual Machine Interface.
 *
 * Each member is a structure pointer with an implementation.
 *
 * @since 2026/06/17
 */
struct JVMNativeInterface
{
	/** Reserved for the JVM (0). */
	void* reserved0;

	/** Reserved for the JVM (1). */
	void* reserved1;

	/** Reserved for the JVM (2). */
	void* reserved2;

	/** Reserved for the JVM (3). */
	void* reserved3;
	
	/** @code DestroyJavaVM @endcode . */
	SJME_JNI_FUNC__(Jvm, DestroyJavaVM);

	/** @code AttachCurrentThread @endcode . */
	SJME_JNI_FUNC__(Jvm, AttachCurrentThread);

	/** @code DetachCurrentThread @endcode . */
	SJME_JNI_FUNC__(Jvm, DetachCurrentThread);

	/** @code GetEnv @endcode . */
	SJME_JNI_FUNC__(Jvm, GetEnv);

	/** @code AttachCurrentThreadAsDaemon @endcode . */
	SJME_JNI_FUNC__(Jvm, AttachCurrentThreadAsDaemon);
};

/**
 * Native Java Interface.
 *
 * Each member is a structure pointer with an implementation.
 *
 * @since 2026/06/17
 */
struct JNINativeInterface
{
	/** Reserved for the JVM (0). */
	void* reserved0;

	/** Reserved for the JVM (1). */
	void* reserved1;

	/** Reserved for the JVM (2). */
	void* reserved2;

	/** Reserved for the JVM (3). */
	void* reserved3;
	
	/** @code GetVersion @endcode . */
	SJME_JNI_FUNC__(Env, GetVersion);

	/** @code DefineClass @endcode . */
	SJME_JNI_FUNC__(Env, DefineClass);

	/** @code FindClass @endcode . */
	SJME_JNI_FUNC__(Env, FindClass);

	/** @code FromReflectedMethod @endcode . */
	SJME_JNI_FUNC__(Env, FromReflectedMethod);

	/** @code FromReflectedField @endcode . */
	SJME_JNI_FUNC__(Env, FromReflectedField);

	/** @code ToReflectedMethod @endcode . */
	SJME_JNI_FUNC__(Env, ToReflectedMethod);

	/** @code GetSuperclass @endcode . */
	SJME_JNI_FUNC__(Env, GetSuperclass);

	/** @code IsAssignableFrom @endcode . */
	SJME_JNI_FUNC__(Env, IsAssignableFrom);

	/** @code ToReflectedField @endcode . */
	SJME_JNI_FUNC__(Env, ToReflectedField);

	/** @code Throw @endcode . */
	SJME_JNI_FUNC__(Env, Throw);

	/** @code ThrowNew @endcode . */
	SJME_JNI_FUNC__(Env, ThrowNew);

	/** @code ExceptionOccurred @endcode . */
	SJME_JNI_FUNC__(Env, ExceptionOccurred);

	/** @code ExceptionDescribe @endcode . */
	SJME_JNI_FUNC__(Env, ExceptionDescribe);

	/** @code ExceptionClear @endcode . */
	SJME_JNI_FUNC__(Env, ExceptionClear);

	/** @code FatalError @endcode . */
	SJME_JNI_FUNC__(Env, FatalError);

	/** @code PushLocalFrame @endcode . */
	SJME_JNI_FUNC__(Env, PushLocalFrame);

	/** @code PopLocalFrame @endcode . */
	SJME_JNI_FUNC__(Env, PopLocalFrame);

	/** @code NewGlobalRef @endcode . */
	SJME_JNI_FUNC__(Env, NewGlobalRef);

	/** @code DeleteGlobalRef @endcode . */
	SJME_JNI_FUNC__(Env, DeleteGlobalRef);

	/** @code DeleteLocalRef @endcode . */
	SJME_JNI_FUNC__(Env, DeleteLocalRef);

	/** @code IsSameObject @endcode . */
	SJME_JNI_FUNC__(Env, IsSameObject);

	/** @code NewLocalRef @endcode . */
	SJME_JNI_FUNC__(Env, NewLocalRef);

	/** @code EnsureLocalCapacity @endcode . */
	SJME_JNI_FUNC__(Env, EnsureLocalCapacity);

	/** @code AllocObject @endcode . */
	SJME_JNI_FUNC__(Env, AllocObject);

	/** @code NewObject @endcode . */
	SJME_JNI_FUNC__(Env, NewObject);

	/** @code NewObjectV @endcode . */
	SJME_JNI_FUNC__(Env, NewObjectV);

	/** @code NewObjectA @endcode . */
	SJME_JNI_FUNC__(Env, NewObjectA);

	/** @code GetObjectClass @endcode . */
	SJME_JNI_FUNC__(Env, GetObjectClass);

	/** @code IsInstanceOf @endcode . */
	SJME_JNI_FUNC__(Env, IsInstanceOf);

	/** @code GetMethodID @endcode . */
	SJME_JNI_FUNC__(Env, GetMethodID);

	/** @code CallObjectMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallObjectMethod);

	/** @code CallObjectMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallObjectMethodV);

	/** @code CallObjectMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallObjectMethodA);

	/** @code CallBooleanMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallBooleanMethod);

	/** @code CallBooleanMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallBooleanMethodV);

	/** @code CallBooleanMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallBooleanMethodA);

	/** @code CallByteMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallByteMethod);

	/** @code CallByteMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallByteMethodV);

	/** @code CallByteMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallByteMethodA);

	/** @code CallCharMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallCharMethod);

	/** @code CallCharMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallCharMethodV);

	/** @code CallCharMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallCharMethodA);

	/** @code CallShortMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallShortMethod);

	/** @code CallShortMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallShortMethodV);

	/** @code CallShortMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallShortMethodA);

	/** @code CallIntMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallIntMethod);

	/** @code CallIntMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallIntMethodV);

	/** @code CallIntMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallIntMethodA);

	/** @code CallLongMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallLongMethod);

	/** @code CallLongMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallLongMethodV);

	/** @code CallLongMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallLongMethodA);

	/** @code CallFloatMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallFloatMethod);

	/** @code CallFloatMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallFloatMethodV);

	/** @code CallFloatMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallFloatMethodA);

	/** @code CallDoubleMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallDoubleMethod);

	/** @code CallDoubleMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallDoubleMethodV);

	/** @code CallDoubleMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallDoubleMethodA);

	/** @code CallVoidMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallVoidMethod);

	/** @code CallVoidMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallVoidMethodV);

	/** @code CallVoidMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallVoidMethodA);

	/** @code CallNonvirtualObjectMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualObjectMethod);

	/** @code CallNonvirtualObjectMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualObjectMethodV);

	/** @code CallNonvirtualObjectMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualObjectMethodA);

	/** @code CallNonvirtualBooleanMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualBooleanMethod);

	/** @code CallNonvirtualBooleanMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualBooleanMethodV);

	/** @code CallNonvirtualBooleanMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualBooleanMethodA);

	/** @code CallNonvirtualByteMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualByteMethod);

	/** @code CallNonvirtualByteMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualByteMethodV);

	/** @code CallNonvirtualByteMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualByteMethodA);

	/** @code CallNonvirtualCharMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualCharMethod);

	/** @code CallNonvirtualCharMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualCharMethodV);

	/** @code CallNonvirtualCharMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualCharMethodA);

	/** @code CallNonvirtualShortMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualShortMethod);

	/** @code CallNonvirtualShortMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualShortMethodV);

	/** @code CallNonvirtualShortMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualShortMethodA);

	/** @code CallNonvirtualIntMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualIntMethod);

	/** @code CallNonvirtualIntMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualIntMethodV);

	/** @code CallNonvirtualIntMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualIntMethodA);

	/** @code CallNonvirtualLongMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualLongMethod);

	/** @code CallNonvirtualLongMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualLongMethodV);

	/** @code CallNonvirtualLongMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualLongMethodA);

	/** @code CallNonvirtualFloatMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualFloatMethod);

	/** @code CallNonvirtualFloatMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualFloatMethodV);

	/** @code CallNonvirtualFloatMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualFloatMethodA);

	/** @code CallNonvirtualDoubleMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualDoubleMethod);

	/** @code CallNonvirtualDoubleMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualDoubleMethodV);

	/** @code CallNonvirtualDoubleMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualDoubleMethodA);

	/** @code CallNonvirtualVoidMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualVoidMethod);

	/** @code CallNonvirtualVoidMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualVoidMethodV);

	/** @code CallNonvirtualVoidMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallNonvirtualVoidMethodA);

	/** @code GetFieldID @endcode . */
	SJME_JNI_FUNC__(Env, GetFieldID);

	/** @code GetObjectField @endcode . */
	SJME_JNI_FUNC__(Env, GetObjectField);

	/** @code GetBooleanField @endcode . */
	SJME_JNI_FUNC__(Env, GetBooleanField);

	/** @code GetByteField @endcode . */
	SJME_JNI_FUNC__(Env, GetByteField);

	/** @code GetCharField @endcode . */
	SJME_JNI_FUNC__(Env, GetCharField);

	/** @code GetShortField @endcode . */
	SJME_JNI_FUNC__(Env, GetShortField);

	/** @code GetIntField @endcode . */
	SJME_JNI_FUNC__(Env, GetIntField);

	/** @code GetLongField @endcode . */
	SJME_JNI_FUNC__(Env, GetLongField);

	/** @code GetFloatField @endcode . */
	SJME_JNI_FUNC__(Env, GetFloatField);

	/** @code GetDoubleField @endcode . */
	SJME_JNI_FUNC__(Env, GetDoubleField);

	/** @code SetObjectField @endcode . */
	SJME_JNI_FUNC__(Env, SetObjectField);

	/** @code SetBooleanField @endcode . */
	SJME_JNI_FUNC__(Env, SetBooleanField);

	/** @code SetByteField @endcode . */
	SJME_JNI_FUNC__(Env, SetByteField);

	/** @code SetCharField @endcode . */
	SJME_JNI_FUNC__(Env, SetCharField);

	/** @code SetShortField @endcode . */
	SJME_JNI_FUNC__(Env, SetShortField);

	/** @code SetIntField @endcode . */
	SJME_JNI_FUNC__(Env, SetIntField);

	/** @code SetLongField @endcode . */
	SJME_JNI_FUNC__(Env, SetLongField);

	/** @code SetFloatField @endcode . */
	SJME_JNI_FUNC__(Env, SetFloatField);

	/** @code SetDoubleField @endcode . */
	SJME_JNI_FUNC__(Env, SetDoubleField);

	/** @code GetStaticMethodID @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticMethodID);

	/** @code CallStaticObjectMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticObjectMethod);

	/** @code CallStaticObjectMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticObjectMethodV);

	/** @code CallStaticObjectMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticObjectMethodA);

	/** @code CallStaticBooleanMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticBooleanMethod);

	/** @code CallStaticBooleanMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticBooleanMethodV);

	/** @code CallStaticBooleanMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticBooleanMethodA);

	/** @code CallStaticByteMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticByteMethod);

	/** @code CallStaticByteMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticByteMethodV);

	/** @code CallStaticByteMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticByteMethodA);

	/** @code CallStaticCharMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticCharMethod);

	/** @code CallStaticCharMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticCharMethodV);

	/** @code CallStaticCharMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticCharMethodA);

	/** @code CallStaticShortMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticShortMethod);

	/** @code CallStaticShortMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticShortMethodV);

	/** @code CallStaticShortMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticShortMethodA);

	/** @code CallStaticIntMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticIntMethod);

	/** @code CallStaticIntMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticIntMethodV);

	/** @code CallStaticIntMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticIntMethodA);

	/** @code CallStaticLongMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticLongMethod);

	/** @code CallStaticLongMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticLongMethodV);

	/** @code CallStaticLongMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticLongMethodA);

	/** @code CallStaticFloatMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticFloatMethod);

	/** @code CallStaticFloatMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticFloatMethodV);

	/** @code CallStaticFloatMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticFloatMethodA);

	/** @code CallStaticDoubleMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticDoubleMethod);

	/** @code CallStaticDoubleMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticDoubleMethodV);

	/** @code CallStaticDoubleMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticDoubleMethodA);

	/** @code CallStaticVoidMethod @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticVoidMethod);

	/** @code CallStaticVoidMethodV @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticVoidMethodV);

	/** @code CallStaticVoidMethodA @endcode . */
	SJME_JNI_FUNC__(Env, CallStaticVoidMethodA);

	/** @code GetStaticFieldID @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticFieldID);

	/** @code GetStaticObjectField @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticObjectField);

	/** @code GetStaticBooleanField @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticBooleanField);

	/** @code GetStaticByteField @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticByteField);

	/** @code GetStaticCharField @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticCharField);

	/** @code GetStaticShortField @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticShortField);

	/** @code GetStaticIntField @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticIntField);

	/** @code GetStaticLongField @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticLongField);

	/** @code GetStaticFloatField @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticFloatField);

	/** @code GetStaticDoubleField @endcode . */
	SJME_JNI_FUNC__(Env, GetStaticDoubleField);

	/** @code SetStaticObjectField @endcode . */
	SJME_JNI_FUNC__(Env, SetStaticObjectField);

	/** @code SetStaticBooleanField @endcode . */
	SJME_JNI_FUNC__(Env, SetStaticBooleanField);

	/** @code SetStaticByteField @endcode . */
	SJME_JNI_FUNC__(Env, SetStaticByteField);

	/** @code SetStaticCharField @endcode . */
	SJME_JNI_FUNC__(Env, SetStaticCharField);

	/** @code SetStaticShortField @endcode . */
	SJME_JNI_FUNC__(Env, SetStaticShortField);

	/** @code SetStaticIntField @endcode . */
	SJME_JNI_FUNC__(Env, SetStaticIntField);

	/** @code SetStaticLongField @endcode . */
	SJME_JNI_FUNC__(Env, SetStaticLongField);

	/** @code SetStaticFloatField @endcode . */
	SJME_JNI_FUNC__(Env, SetStaticFloatField);

	/** @code SetStaticDoubleField @endcode . */
	SJME_JNI_FUNC__(Env, SetStaticDoubleField);

	/** @code NewString @endcode . */
	SJME_JNI_FUNC__(Env, NewString);

	/** @code GetStringLength @endcode . */
	SJME_JNI_FUNC__(Env, GetStringLength);

	/** @code GetStringChars @endcode . */
	SJME_JNI_FUNC__(Env, GetStringChars);

	/** @code ReleaseStringChars @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseStringChars);

	/** @code NewStringUTF @endcode . */
	SJME_JNI_FUNC__(Env, NewStringUTF);

	/** @code GetStringUTFLength @endcode . */
	SJME_JNI_FUNC__(Env, GetStringUTFLength);

	/** @code GetStringUTFChars @endcode . */
	SJME_JNI_FUNC__(Env, GetStringUTFChars);

	/** @code ReleaseStringUTFChars @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseStringUTFChars);

	/** @code GetArrayLength @endcode . */
	SJME_JNI_FUNC__(Env, GetArrayLength);

	/** @code NewObjectArray @endcode . */
	SJME_JNI_FUNC__(Env, NewObjectArray);

	/** @code GetObjectArrayElement @endcode . */
	SJME_JNI_FUNC__(Env, GetObjectArrayElement);

	/** @code SetObjectArrayElement @endcode . */
	SJME_JNI_FUNC__(Env, SetObjectArrayElement);

	/** @code NewBooleanArray @endcode . */
	SJME_JNI_FUNC__(Env, NewBooleanArray);

	/** @code NewByteArray @endcode . */
	SJME_JNI_FUNC__(Env, NewByteArray);

	/** @code NewCharArray @endcode . */
	SJME_JNI_FUNC__(Env, NewCharArray);

	/** @code NewShortArray @endcode . */
	SJME_JNI_FUNC__(Env, NewShortArray);

	/** @code NewIntArray @endcode . */
	SJME_JNI_FUNC__(Env, NewIntArray);

	/** @code NewLongArray @endcode . */
	SJME_JNI_FUNC__(Env, NewLongArray);

	/** @code NewFloatArray @endcode . */
	SJME_JNI_FUNC__(Env, NewFloatArray);

	/** @code NewDoubleArray @endcode . */
	SJME_JNI_FUNC__(Env, NewDoubleArray);

	/** @code GetBooleanArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, GetBooleanArrayElements);

	/** @code GetByteArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, GetByteArrayElements);

	/** @code GetCharArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, GetCharArrayElements);

	/** @code GetShortArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, GetShortArrayElements);

	/** @code GetIntArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, GetIntArrayElements);

	/** @code GetLongArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, GetLongArrayElements);

	/** @code GetFloatArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, GetFloatArrayElements);

	/** @code GetDoubleArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, GetDoubleArrayElements);

	/** @code ReleaseBooleanArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseBooleanArrayElements);

	/** @code ReleaseByteArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseByteArrayElements);

	/** @code ReleaseCharArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseCharArrayElements);

	/** @code ReleaseShortArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseShortArrayElements);

	/** @code ReleaseIntArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseIntArrayElements);

	/** @code ReleaseLongArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseLongArrayElements);

	/** @code ReleaseFloatArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseFloatArrayElements);

	/** @code ReleaseDoubleArrayElements @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseDoubleArrayElements);

	/** @code GetBooleanArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetBooleanArrayRegion);

	/** @code GetByteArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetByteArrayRegion);

	/** @code GetCharArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetCharArrayRegion);

	/** @code GetShortArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetShortArrayRegion);

	/** @code GetIntArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetIntArrayRegion);

	/** @code GetLongArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetLongArrayRegion);

	/** @code GetFloatArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetFloatArrayRegion);

	/** @code GetDoubleArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetDoubleArrayRegion);

	/** @code SetBooleanArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, SetBooleanArrayRegion);

	/** @code SetByteArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, SetByteArrayRegion);

	/** @code SetCharArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, SetCharArrayRegion);

	/** @code SetShortArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, SetShortArrayRegion);

	/** @code SetIntArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, SetIntArrayRegion);

	/** @code SetLongArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, SetLongArrayRegion);

	/** @code SetFloatArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, SetFloatArrayRegion);

	/** @code SetDoubleArrayRegion @endcode . */
	SJME_JNI_FUNC__(Env, SetDoubleArrayRegion);

	/** @code RegisterNatives @endcode . */
	SJME_JNI_FUNC__(Env, RegisterNatives);

	/** @code UnregisterNatives @endcode . */
	SJME_JNI_FUNC__(Env, UnregisterNatives);

	/** @code MonitorEnter @endcode . */
	SJME_JNI_FUNC__(Env, MonitorEnter);

	/** @code MonitorExit @endcode . */
	SJME_JNI_FUNC__(Env, MonitorExit);

	/** @code GetJavaVM @endcode . */
	SJME_JNI_FUNC__(Env, GetJavaVM);

	/** @code GetStringRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetStringRegion);

	/** @code GetStringUTFRegion @endcode . */
	SJME_JNI_FUNC__(Env, GetStringUTFRegion);

	/** @code GetPrimitiveArrayCritical @endcode . */
	SJME_JNI_FUNC__(Env, GetPrimitiveArrayCritical);

	/** @code ReleasePrimitiveArrayCritical @endcode . */
	SJME_JNI_FUNC__(Env, ReleasePrimitiveArrayCritical);

	/** @code GetStringCritical @endcode . */
	SJME_JNI_FUNC__(Env, GetStringCritical);

	/** @code ReleaseStringCritical @endcode . */
	SJME_JNI_FUNC__(Env, ReleaseStringCritical);

	/** @code NewWeakGlobalRef @endcode . */
	SJME_JNI_FUNC__(Env, NewWeakGlobalRef);

	/** @code DeleteWeakGlobalRef @endcode . */
	SJME_JNI_FUNC__(Env, DeleteWeakGlobalRef);

	/** @code ExceptionCheck @endcode . */
	SJME_JNI_FUNC__(Env, ExceptionCheck);

	/** @code NewDirectByteBuffer @endcode . */
	SJME_JNI_FUNC__(Env, NewDirectByteBuffer);

	/** @code GetDirectBufferAddress @endcode . */
	SJME_JNI_FUNC__(Env, GetDirectBufferAddress);

	/** @code GetDirectBufferCapacity @endcode . */
	SJME_JNI_FUNC__(Env, GetDirectBufferCapacity);

	/** @code GetObjectRefType @endcode . */
	SJME_JNI_FUNC__(Env, GetObjectRefType);

	/** @code GetModule @endcode . */
	SJME_JNI_FUNC__(Env, GetModule);
};

/* No longer needed. */
#undef SJME_JNI_FUNC__

/**
 * Creates a new Java Virtual Machine.
 *
 * @param pvm The resultant virtual machine.
 * @param penv The output environment.
 * @param args The arguments to the virtual machine creation.
 * @return If successful, @c JNI_OK .
 * @since 2025/06/25
 */
jint JNICALL JNI_CreateJavaVM(
	sjme_attrOutNotNull JavaVM** pvm,
	sjme_attrOutNotNull void** penv,
	sjme_attrInNotNull void* args);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_JNI_H
}
#undef SJME_CXX_SQUIRRELJME_JNI_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_JNI_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_JNI_H */
