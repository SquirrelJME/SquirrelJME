/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Java JNI compatible header.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_JNI_H
#define SQUIRRELJME_JNI_H

#include "sjmejni/sjmejni.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_JNI_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** JNI Version 1.1. */
#define JNI_VERSION_1_1 SJME_INTERFACE_VERSION_1_1

/** JNI Version 1.2. */
#define JNI_VERSION_1_2 SJME_INTERFACE_VERSION_1_2

/** JNI Version 1.4. */
#define JNI_VERSION_1_4 SJME_INTERFACE_VERSION_1_4

/** JNI Version 1.6. */
#define JNI_VERSION_1_6 SJME_INTERFACE_VERSION_1_6

/** No error. */
#define JNI_OK SJME_JNI_ERROR_NONE

/** General JNI error. */
#define JNI_ERR SJME_JNI_ERROR_UNKNOWN

/** The virtual machine is detached. */
#define JNI_EDETACHED SJME_JNI_ERROR_DETACHED

/** The virtual machine version is incorrect. */
#define JNI_EVERSION SJME_JNI_ERROR_INVALID_VERSION

/** No memory remains. */
#define JNI_ENOMEM SJME_JNI_ERROR_NO_MEMORY

/** Invalid argument. */
#define JNI_EINVAL SJME_JNI_ERROR_VM_INVALID_ARGUMENT

/** Boolean. */
typedef sjme_jboolean jboolean;

/** Byte. */
typedef sjme_jbyte jbyte;

/** Character. */
typedef sjme_jchar jchar;

/** Short. */
typedef sjme_jshort jshort;

/** Integer. */
typedef sjme_jint jint;

/** Long. */
typedef sjme_jlong jlong;

/** Float. */
typedef sjme_jfloat jfloat;

/** Double. */
typedef sjme_jdouble jdouble;

/** Size type. */
typedef jint jsize;

/** False. */
#define JNI_FALSE sjme_false

/** True. */
#define JNI_TRUE sjme_true

/** Value type. */
typedef sjme_jvalue jvalue;

/** Object type. */
typedef sjme_jobject jobject;

/** Class type. */
typedef sjme_jobject jclass;

/** String type. */
typedef sjme_jstring jstring;

/** Throwable. */
typedef sjme_jthrowable jthrowable;

/** Weak reference. */
typedef sjme_jweakReference jweak;

/** Array type. */
typedef sjme_jarray jarray;

/** Boolean array type. */
typedef sjme_jbooleanArray jbooleanArray;

/** Byte array type. */
typedef sjme_jbyteArray jbyteArray;

/** Character array type. */
typedef sjme_jcharArray jcharArray;

/** Short array type. */
typedef sjme_jshortArray jshortArray;

/** Integer array type. */
typedef sjme_jintArray jintArray;

/** Long array type. */
typedef sjme_jlongArray jlongArray;

/** Float array type. */
typedef sjme_jfloatArray jfloatArray;

/** Double array type. */
typedef sjme_jdoubleArray jdoubleArray;

/** Object array type. */
typedef sjme_jobjectArray jobjectArray;

/** Field Identifier. */
typedef sjme_vmField fieldID;

/** Method Identifier. */
typedef sjme_vmMethod methodID;

/** Virtual machine initialization arguments. */
#define JavaVMInitArgs sjme_vmInitArgs

/** Virtual machine options. */
#define JavaVMOption sjme_vmInitOption

/** Interface functions for the virtual machine. */
#define JNINativeInterface sjme_vmThread

/** Virtual machine interface. */
typedef struct JNINativeInterface* JNIEnv;

/** Invocation interface. */
#define JNIInvokeInterface sjme_vmState

/** Virtual machine state. */
typedef struct JNIInvokeInterface* JavaVM;

/** Object reference types. */
#define jobjectRefType sjme_jobjectReferenceType

/** Invalid reference type. */
#define JNIInvalidRefType SJME_JOBJECT_REF_TYPE_INVALID

/** Local reference type. */
#define JNILocalRefType SJME_JOBJECT_REF_TYPE_LOCAL

/** Global reference type. */
#define JNIGlobalRefType SJME_JOBJECT_REF_TYPE_GLOBAL

/** Weak global reference type. */
#define JNIWeakGlobalRefType SJME_JOBJECT_REF_TYPE_WEAK_GLOBAL

/** Native registration. */
#define JNINativeMethod sjme_vmRegisterNative

jint JNI_GetDefaultJavaVMInitArgs(void* vmArgs);

jint JNI_GetCreatedJavaVMs(JavaVM** vmBuf, jsize bufLen, jsize* nVMs);

jint JNI_CreateJavaVM(JavaVM** pVm, void** pEnv, void* vmArgs);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_JNI_H
}
		#undef SJME_CXX_SQUIRRELJME_JNI_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_JNI_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_JNI_H */
