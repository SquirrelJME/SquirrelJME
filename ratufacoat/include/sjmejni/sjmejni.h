/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME defined JNI interface.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_SJMEJNI_H
#define SQUIRRELJME_SJMEJNI_H

#include "ccfeatures.h"

#if defined(SJME_HAS_STDINT_H)
	#include <stdint.h>
#endif

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SJME_JNI_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_HAS_STDINT_H)
	/** Boolean. */
	typedef uint8_t sjme_jboolean;

	/** Byte. */
	typedef int8_t sjme_jbyte;

	/** Character. */
	typedef uint16_t sjme_jchar;

	/** Short. */
	typedef int16_t sjme_jshort;

	/** Integer. */
	typedef int32_t sjme_jint;

	/** Long. */
	typedef int64_t sjme_jlong;

	/** Float. */
	typedef float sjme_jfloat;

	/** Double. */
	typedef double sjme_jdouble;

#elif defined(SJME_FEATURE_MSVC)
	/** Boolean. */
	typedef unsigned __int8 sjme_jboolean;

	/** Byte. */
	typedef signed __int8 sjme_jbyte;

	/** Character. */
	typedef unsigned __int16 sjme_jchar;

	/** Short. */
	typedef signed __int16 sjme_jshort;

	/** Integer. */
	typedef signed __int32 sjme_jint;

	/** Long. */
	typedef signed __int64 sjme_jlong;

	/** Float. */
	typedef float sjme_jfloat;

	/** Double. */
	typedef double sjme_jdouble;

	/** Signed 8-bit constant. */
	#define INT8_C(x) x

	/** Signed 16-bit constant. */
	#define INT16_C(x) x

	/** Signed 32-bit constant. */
	#define INT32_C(x) x

	/** Unsigned 8-bit constant. */
	#define UINT8_C(x) x##U

	/** Unsigned 16-bit constant. */
	#define UINT16_C(x) x##U

	/** Unsigned 32-bit constant. */
	#define UINT32_C(x) x##U
#else
	#error No standard types are known.
#endif

/** Interface version 1.1. */
#define SJME_INTERFACE_VERSION_1_1 INT32_C(0x00010001)

/** Interface version 1.2. */
#define SJME_INTERFACE_VERSION_1_2 INT32_C(0x00010002)

/** Interface version 1.4. */
#define SJME_INTERFACE_VERSION_1_4 INT32_C(0x00010004)

/** Interface version 1.6. */
#define SJME_INTERFACE_VERSION_1_6 INT32_C(0x00010006)

/** No interface error. */
#define SJME_INTERFACE_ERROR_NONE INT32_C(0)

/** The virtual machine is detached. */
#define SJME_INTERFACE_ERROR_DETACHED INT32_C(-2)

/** The virtual machine version is incorrect. */
#define SJME_INTERFACE_ERROR_INVALID_VERSION INT32_C(-3)

/** Size type. */
typedef sjme_jint sjme_jsize;

/** False. */
#define SJME_FALSE INT8_C(0)

/** True. */
#define SJME_TRUE INT8_C(1)

/** Object type. */
typedef struct sjme_jobject* sjme_jobject;

/** Class type. */
typedef struct sjme_jobject* sjme_jclass;

/** String type. */
typedef struct sjme_jstring* sjme_jstring;

/** Throwable. */
typedef struct sjme_jthrowable* sjme_jthrowable;

/** Weak reference. */
typedef struct sjme_jweak* sjme_jweak;

/** Array type. */
typedef struct sjme_jarray* sjme_jarray;

/** Boolean array type. */
typedef struct sjme_jbooleanArray* sjme_jbooleanArray;

/** Byte array type. */
typedef struct sjme_jbyteArray* sjme_jbyteArray;

/** Character array type. */
typedef struct sjme_jcharArray* sjme_jcharArray;

/** Short array type. */
typedef struct sjme_jshortArray* sjme_jshortArray;

/** Integer array type. */
typedef struct sjme_jintArray* sjme_jintArray;

/** Long array type. */
typedef struct sjme_jlongArray* sjme_jlongArray;

/** Float array type. */
typedef struct sjme_jfloatArray* sjme_jfloatArray;

/** Double array type. */
typedef struct sjme_jdoubleArray* sjme_jdoubleArray;

/** Object array type. */
typedef struct sjme_jobjectArray* sjme_jobjectArray;

/**
 * Value type.
 *
 * @since 2022/12/11
 */
typedef union sjme_jvalue
{
	sjme_jboolean z;
	sjme_jbyte b;
	sjme_jchar c;
	sjme_jshort s;
	sjme_jint i;
	sjme_jlong j;
	sjme_jfloat f;
	sjme_jdouble d;
	sjme_jobject l;
} sjme_jvalue;

/** Field identifier. */
typedef struct sjme_fieldId* sjme_fieldId;

/** Method identifier. */
typedef struct sjme_methodId* sjme_methodId;

/**
 * Virtual machine initialization options.
 *
 * @since 2022/12/11
 */
typedef struct sjme_vmInitOption
{
	/** The option specified on the launch of the virtual machine. */
	char* optionString;

	/** Extra argument information to the option. */
	void* extraInfo;
} sjme_vmInitOption;

/**
 * Initialization arguments for the virtual machine.
 *
 * @since 2022/12/11
 */
typedef struct sjme_vmInitArgs
{
	/** The requesting virtual machine version. */
	sjme_jint version;

	/** The number of specified options. */
	sjme_jint nOptions;

	/** Virtual machine options. */
	sjme_vmInitOption* options;

	/** Ignore unrecognized options? */
	sjme_jboolean ignoreUnrecognized;
} sjme_vmInitArgs;

/** Virtual machine functions. */
typedef struct sjme_vmFunctions* sjme_vmFunctions;

/** Virtual machine state. */
typedef struct sjme_vmState* sjme_vmState;

/** Declare function pointer. */
#define SJME_FUNC_PTR__(x) (*x)

/** Function: Destroy Java VM. */
#define SJME_FUNCTION_DESTROY_JAVA_VM(funcName) \
	sjme_jint funcName(sjme_vmState* vm)

/**
 * Virtual machine state.
 *
 * @since 2022/12/11
 */
struct sjme_vmState
{
	/** Reserved, do not use. */
	void* reserved0;

	/** Reserved, do not use. */
	void* reserved1;

	/** Reserved, do not use. */
	void* reserved2;

	/**
	 * Destroys the virtual machine.
	 *
	 * @param vm The virtual machine to destroy.
	 * @return
	 * @since 2022/12/11
	 */
	SJME_FUNCTION_DESTROY_JAVA_VM(SJME_FUNC_PTR__(DestroyJavaVM))
#if 0
	AttachCurrentThread,
	DetachCurrentThread,
	GetEnv,
	AttachCurrentThreadAsDaemon
#endif
};

/**
 * Virtual machine functions.
 *
 * @since 2022/12/11
 */
struct sjme_vmFunctions
{
	/** Reserved, do not use. */
	void* reserved0;

	/** Reserved, do not use. */
	void* reserved1;

	/** Reserved, do not use. */
	void* reserved2;

	/** Reserved, do not use. */
	void* reserved3;

#if 0
	GetVersion,
	DefineClass,
	FindClass,
	FromReflectedMethod,
	FromReflectedField,
	ToReflectedMethod,
	GetSuperclass,
	IsAssignableFrom,
	ToReflectedField,
	Throw,
	ThrowNew,
	ExceptionOccurred,
	ExceptionDescribe,
	ExceptionClear,
	FatalError,
	PushLocalFrame,
	PopLocalFrame,
	NewGlobalRef,
	DeleteGlobalRef,
	DeleteLocalRef,
	IsSameObject,
	NewLocalRef,
	EnsureLocalCapacity,
	AllocObject,
	NewObject,
	NewObjectV,
	NewObjectA,
	GetObjectClass,
	IsInstanceOf,
	GetMethodID,
	CallObjectMethod,
	CallObjectMethodV,
	CallObjectMethodA,
	CallBooleanMethod,
	CallBooleanMethodV,
	CallBooleanMethodA,
	CallByteMethod,
	CallByteMethodV,
	CallByteMethodA,
	CallCharMethod,
	CallCharMethodV,
	CallCharMethodA,
	CallShortMethod,
	CallShortMethodV,
	CallShortMethodA,
	CallIntMethod,
	CallIntMethodV,
	CallIntMethodA,
	CallLongMethod,
	CallLongMethodV,
	CallLongMethodA,
	CallFloatMethod,
	CallFloatMethodV,
	CallFloatMethodA,
	CallDoubleMethod,
	CallDoubleMethodV,
	CallDoubleMethodA,
	CallVoidMethod,
	CallVoidMethodV,
	CallVoidMethodA,
	CallNonvirtualObjectMethod,
	CallNonvirtualObjectMethodV,
	CallNonvirtualObjectMethodA,
	CallNonvirtualBooleanMethod,
	CallNonvirtualBooleanMethodV,
	CallNonvirtualBooleanMethodA,
	CallNonvirtualByteMethod,
	CallNonvirtualByteMethodV,
	CallNonvirtualByteMethodA,
	CallNonvirtualCharMethod,
	CallNonvirtualCharMethodV,
	CallNonvirtualCharMethodA,
	CallNonvirtualShortMethod,
	CallNonvirtualShortMethodV,
	CallNonvirtualShortMethodA,
	CallNonvirtualIntMethod,
	CallNonvirtualIntMethodV,
	CallNonvirtualIntMethodA,
	CallNonvirtualLongMethod,
	CallNonvirtualLongMethodV,
	CallNonvirtualLongMethodA,
	CallNonvirtualFloatMethod,
	CallNonvirtualFloatMethodV,
	CallNonvirtualFloatMethodA,
	CallNonvirtualDoubleMethod,
	CallNonvirtualDoubleMethodV,
	CallNonvirtualDoubleMethodA,
	CallNonvirtualVoidMethod,
	CallNonvirtualVoidMethodV,
	CallNonvirtualVoidMethodA,
	GetFieldID,
	GetObjectField,
	GetBooleanField,
	GetByteField,
	GetCharField,
	GetShortField,
	GetIntField,
	GetLongField,
	GetFloatField,
	GetDoubleField,
	SetObjectField,
	SetBooleanField,
	SetByteField,
	SetCharField,
	SetShortField,
	SetIntField,
	SetLongField,
	SetFloatField,
	SetDoubleField,
	GetStaticMethodID,
	CallStaticObjectMethod,
	CallStaticObjectMethodV,
	CallStaticObjectMethodA,
	CallStaticBooleanMethod,
	CallStaticBooleanMethodV,
	CallStaticBooleanMethodA,
	CallStaticByteMethod,
	CallStaticByteMethodV,
	CallStaticByteMethodA,
	CallStaticCharMethod,
	CallStaticCharMethodV,
	CallStaticCharMethodA,
	CallStaticShortMethod,
	CallStaticShortMethodV,
	CallStaticShortMethodA,
	CallStaticIntMethod,
	CallStaticIntMethodV,
	CallStaticIntMethodA,
	CallStaticLongMethod,
	CallStaticLongMethodV,
	CallStaticLongMethodA,
	CallStaticFloatMethod,
	CallStaticFloatMethodV,
	CallStaticFloatMethodA,
	CallStaticDoubleMethod,
	CallStaticDoubleMethodV,
	CallStaticDoubleMethodA,
	CallStaticVoidMethod,
	CallStaticVoidMethodV,
	CallStaticVoidMethodA,
	GetStaticFieldID,
	GetStaticObjectField,
	GetStaticBooleanField,
	GetStaticByteField,
	GetStaticCharField,
	GetStaticShortField,
	GetStaticIntField,
	GetStaticLongField,
	GetStaticFloatField,
	GetStaticDoubleField,
	SetStaticObjectField,
	SetStaticBooleanField,
	SetStaticByteField,
	SetStaticCharField,
	SetStaticShortField,
	SetStaticIntField,
	SetStaticLongField,
	SetStaticFloatField,
	SetStaticDoubleField,
	NewString,
	GetStringLength,
	GetStringChars,
	ReleaseStringChars,
	NewStringUTF,
	GetStringUTFLength,
	GetStringUTFChars,
	ReleaseStringUTFChars,
	GetArrayLength,
	NewObjectArray,
	GetObjectArrayElement,
	SetObjectArrayElement,
	NewBooleanArray,
	NewByteArray,
	NewCharArray,
	NewShortArray,
	NewIntArray,
	NewLongArray,
	NewFloatArray,
	NewDoubleArray,
	GetBooleanArrayElements,
	GetByteArrayElements,
	GetCharArrayElements,
	GetShortArrayElements,
	GetIntArrayElements,
	GetLongArrayElements,
	GetFloatArrayElements,
	GetDoubleArrayElements,
	ReleaseBooleanArrayElements,
	ReleaseByteArrayElements,
	ReleaseCharArrayElements,
	ReleaseShortArrayElements,
	ReleaseIntArrayElements,
	ReleaseLongArrayElements,
	ReleaseFloatArrayElements,
	ReleaseDoubleArrayElements,
	GetBooleanArrayRegion,
	GetByteArrayRegion,
	GetCharArrayRegion,
	GetShortArrayRegion,
	GetIntArrayRegion,
	GetLongArrayRegion,
	GetFloatArrayRegion,
	GetDoubleArrayRegion,
	SetBooleanArrayRegion,
	SetByteArrayRegion,
	SetCharArrayRegion,
	SetShortArrayRegion,
	SetIntArrayRegion,
	SetLongArrayRegion,
	SetFloatArrayRegion,
	SetDoubleArrayRegion,
	RegisterNatives,
	UnregisterNatives,
	MonitorEnter,
	MonitorExit,
	GetJavaVM,
	GetStringRegion,
	GetStringUTFRegion,
	GetPrimitiveArrayCritical,
	ReleasePrimitiveArrayCritical,
	GetStringCritical,
	ReleaseStringCritical,
	NewWeakGlobalRef,
	DeleteWeakGlobalRef,
	ExceptionCheck,
	NewDirectByteBuffer,
	GetDirectBufferAddress,
	GetDirectBufferCapacity,
	GetObjectRefType
#endif
};

/* Clear this. */
#undef SJME_FUNC_PTR__

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SJME_JNI_H
}
		#undef SJME_CXX_SQUIRRELJME_SJME_JNI_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SJME_JNI_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SJMEJNI_H */
