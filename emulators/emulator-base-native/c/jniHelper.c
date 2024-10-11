/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <jni.h>

#include "squirreljme.h"
#include "sjme/debug.h"

sjme_jboolean sjme_jni_checkVMException(JNIEnv* env)
{
	/* Was there an exception? */
	if ((*env)->ExceptionCheck(env))
	{
		/* Debug print it. */
		(*env)->ExceptionDescribe(env);

		/* Did fail! */
		return SJME_JNI_TRUE;
	}

	return SJME_JNI_FALSE;
}

jintArray sjme_jni_mappedArrayInt(JNIEnv* env,
	jint* buf, jint off, jint len)
{
#if 0 
	/* We need this to get raw arrays. */
	byteBufferClassy = (*env)->FindClass(env, "java/nio/ByteBuffer");
	if (byteBufferClassy == NULL)
		sjme_die("No ByteBuffer?");
	
	/* Create a byte buffer around the buffer. */
	byteBuffer = (*env)->NewDirectByteBuffer(env,
		(void*)(((sjme_intPointer)buf) + ((sjme_intPointer)bufOff)), bufLen);
	if (byteBuffer == NULL)
		return SJME_ERROR_CANNOT_CREATE;
#endif
		
	sjme_todo("Impl?");
	return NULL;
}

void sjme_jni_throwMLECallError(JNIEnv* env, sjme_errorCode code)
{
	sjme_jni_throwThrowable(env, code,
		"cc/squirreljme/jvm/mle/exceptions/MLECallError");
}

void sjme_jni_throwThrowable(JNIEnv* env, sjme_errorCode code,
	sjme_lpcstr type)
{
#define BUF_SIZE 512
	jclass tossingClass;
	char buf[BUF_SIZE];

	/* Get the class where the exception is. */
	tossingClass = (*env)->FindClass(env, type);
	if (tossingClass == NULL)
	{
		sjme_die("Could not find exception class?");
		return;
	}

	/* Generate a message accordingly. */
	memset(buf, 0, sizeof(buf));
	snprintf(buf, BUF_SIZE - 1, "Native error: %d",
		(int)sjme_error_default(code));
	buf[BUF_SIZE - 1] = 0;

	/* Throw it. */
	if ((*env)->ThrowNew(env, tossingClass, buf) != 0)
		sjme_die("Could not throw a new throwable?");
#undef BUF_SIZE
}

void sjme_jni_throwVMException(JNIEnv* env, sjme_errorCode code)
{
	sjme_jni_throwThrowable(env, code,
		"cc/squirreljme/emulator/vm/VMException");
}

void* sjme_jni_recoverPointer(JNIEnv* env, sjme_lpcstr className,
	jobject instance)
{
	jclass classy;
	jclass baseClassy;
	jmethodID pointerMethod;
	
	/* Does not map. */
	if (instance == NULL)
		return NULL;
	
	/* Fail. */
	if (env == NULL || className == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return NULL;
	}

	/* Locate class. */
	classy = (*env)->FindClass(env, className);
	baseClassy = (*env)->FindClass(env, DESC_DYLIB_HAS_OBJECT_POINTER);
	if (classy == NULL || baseClassy == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_INVALID_CLASS_NAME);
		return NULL;
	}
	
	/* Incorrect type. */
	if (!(*env)->IsInstanceOf(env, instance, classy) ||
		!(*env)->IsInstanceOf(env, instance, baseClassy))
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_CLASS_CAST);
		return NULL;
	}
	
	/* Get pointer object method. */
	pointerMethod = (*env)->GetMethodID(env, baseClassy,
		"objectPointer", "()J");
	if (pointerMethod == NULL)
		sjme_die("No objectPointer() in instance?");
	
	/* Cast pencil data. */
	return (void*)((intptr_t)((*env)->CallLongMethod(
		env, instance, pointerMethod)));
}

sjme_scritchui_pencil sjme_jni_recoverPencil(JNIEnv* env, jobject g)
{
	/* Does not map. */
	if (g == NULL)
		return NULL;
	
	return (sjme_scritchui_pencil)sjme_jni_recoverPointer(env,
		DESC_DYLIB_PENCIL, g);
}

sjme_scritchui_pencilFont sjme_jni_recoverFont(JNIEnv* env,
	jobject fontInstance)
{
	/* Does not map. */
	if (fontInstance == NULL)
		return NULL;
	
	return (sjme_scritchui_pencilFont)sjme_jni_recoverPointer(env,
		DESC_DYLIB_PENCILFONT, fontInstance);
}

sjme_errorCode sjme_jni_fillFrontEnd(JNIEnv* env, sjme_frontEnd* into,
	jobject ref)
{
	JavaVM* vm;
	
	if (env == NULL || into == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Store referenced VM. */
	vm = NULL;
	(*env)->GetJavaVM(env, &vm);
	into->data = vm;
	
	/* Need to reference an object? */
	if (ref != NULL)
		into->wrapper = (*env)->NewGlobalRef(env, ref);
	else
		into->wrapper = NULL;
	
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_recoverEnv(
	sjme_attrInOutNotNull JNIEnv** outEnv,
	sjme_attrInNotNull JavaVM* inVm)
{
	JNIEnv* env;
	jint jniError;
	
	if (outEnv == NULL || inVm == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try to get the environment for the current thread. */
	env = NULL;
	jniError = (*inVm)->GetEnv(inVm, (void**)&env, JNI_VERSION_1_1);
	if (jniError != JNI_OK || env == NULL)
		return SJME_ERROR_NO_JAVA_ENVIRONMENT;
	
	/* Success! */
	*outEnv = env;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_recoverEnvFrontEnd(
	sjme_attrInOutNotNull JNIEnv** outEnv,
	sjme_attrInNotNull const sjme_frontEnd* inFrontEnd)
{
	if (outEnv == NULL || inFrontEnd == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward. */
	return sjme_jni_recoverEnv(outEnv, inFrontEnd->data);
}

static sjme_errorCode sjme_jni_jstringCharAt(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrInPositive sjme_jint inIndex,
	sjme_attrOutNotNull sjme_jchar* outChar)
{
	sjme_errorCode error;
	JNIEnv* env;
	jstring string;
	const jchar* stringChars;
	jboolean isCopy;
	jint len;
	
	if (inSeq == NULL || outChar == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover env. */
	env = NULL;
	if (sjme_error_is(error = sjme_jni_recoverEnvFrontEnd(
		&env, &inSeq->frontEnd)) || env == NULL)
		return sjme_error_default(error);
	
	/* Get string. */
	string = inSeq->frontEnd.wrapper;
	
	/* Not within the string bounds? */
	len = (*env)->GetStringLength(env, string);
	if (inIndex < 0 || inIndex >= len)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Need to access characters just to read one, sadly. */
	isCopy = JNI_FALSE;
	stringChars = (*env)->GetStringChars(env, string, &isCopy);
	
	/* Copy character. */
	*outChar = stringChars[inIndex];
	
	/* Cleanup. */
	(*env)->ReleaseStringChars(env, string, stringChars);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_jni_jstringDelete(
	sjme_attrInNotNull sjme_charSeq* inSeq)
{
	sjme_errorCode error;
	JNIEnv* env;
	jstring string;
	
	if (inSeq == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover env. */
	env = NULL;
	if (sjme_error_is(error = sjme_jni_recoverEnvFrontEnd(
		&env, &inSeq->frontEnd)) || env == NULL)
		return sjme_error_default(error);
	
	/* Get string. */
	string = inSeq->frontEnd.wrapper;
	
	/* Remove global reference. */
	(*env)->DeleteGlobalRef(env, string);
	inSeq->frontEnd.wrapper = NULL;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_jni_jstringLength(
	sjme_attrInNotNull const sjme_charSeq* inSeq,
	sjme_attrOutNotNull sjme_jint* outLen)
{
	sjme_errorCode error;
	JNIEnv* env;
	jstring string;
	
	if (inSeq == NULL || outLen == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover env. */
	env = NULL;
	if (sjme_error_is(error = sjme_jni_recoverEnvFrontEnd(
		&env, &inSeq->frontEnd)) || env == NULL)
		return sjme_error_default(error);
	
	/* Get string. */
	string = inSeq->frontEnd.wrapper;
	
	/* Get string length. */
	*outLen = (*env)->GetStringLength(env, string);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static const sjme_charSeq_functions sjme_jni_jstringFunctions =
{
	.charAt = sjme_jni_jstringCharAt,
	.delete = sjme_jni_jstringDelete,
	.length = sjme_jni_jstringLength,
};

sjme_errorCode sjme_jni_jstringCharSeqStatic(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull sjme_charSeq* inOutSeq,
	sjme_attrInNotNull jstring inString)
{
	sjme_frontEnd frontEnd;
	sjme_errorCode error;
	
	if (env == NULL || inOutSeq == NULL || inString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Setup front end. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	if (sjme_error_is(error = sjme_jni_fillFrontEnd(env,
		&frontEnd, inString)))
		return sjme_error_default(error);
	
	/* Initialize via forward. */
	return sjme_charSeq_newStatic(
		inOutSeq, &sjme_jni_jstringFunctions,
		NULL,
		&frontEnd);
}

jlong sjme_jni_jlong(sjme_jlong value)
{
	return value.full;
}

sjme_errorCode sjme_jni_pushWeakLink(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jobject javaObject,
	sjme_attrInNotNull sjme_alloc_weak nativeWeak)
{
	jclass collectorClass;
	jmethodID pushMethod;
	
	if (env == NULL || javaObject == NULL || nativeWeak == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Find collector class. */
	collectorClass = (*env)->FindClass(env, DESC_DYLIB_COLLECTOR);
	if (collectorClass == NULL)
		return SJME_ERROR_JNI_EXCEPTION;
		
	/* Find push method. */
	pushMethod = (*env)->GetStaticMethodID(env, collectorClass,
		"__push", "(Ljava/lang/Object;J)V");
	if (pushMethod == NULL)
		return SJME_ERROR_JNI_EXCEPTION;
	
	/* Call it. */
	(*env)->CallStaticVoidMethod(env, collectorClass, pushMethod,
		javaObject, (jlong)nativeWeak);
	
	/* Check for failure. */
	if (sjme_jni_checkVMException(env))
		return SJME_ERROR_JNI_EXCEPTION;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_arrayType(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jobject array,
	sjme_attrOutNotNull sjme_basicTypeId* outType)
{
	jclass classy;
	
	if (env == NULL || array == NULL || outType == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if ((*env)->IsInstanceOf(env, array, (*env)->FindClass(env, "[Z")))
		*outType = SJME_BASIC_TYPE_ID_BOOLEAN;
	else if ((*env)->IsInstanceOf(env, array, (*env)->FindClass(env, "[B")))
		*outType = SJME_BASIC_TYPE_ID_BYTE;
	else if ((*env)->IsInstanceOf(env, array, (*env)->FindClass(env, "[S")))
		*outType = SJME_BASIC_TYPE_ID_SHORT;
	else if ((*env)->IsInstanceOf(env, array, (*env)->FindClass(env, "[C")))
		*outType = SJME_BASIC_TYPE_ID_CHARACTER;
	else if ((*env)->IsInstanceOf(env, array, (*env)->FindClass(env, "[I")))
		*outType = SJME_BASIC_TYPE_ID_INTEGER;
	else if ((*env)->IsInstanceOf(env, array, (*env)->FindClass(env, "[J")))
		*outType = SJME_BASIC_TYPE_ID_LONG;
	else if ((*env)->IsInstanceOf(env, array, (*env)->FindClass(env, "[F")))
		*outType = SJME_BASIC_TYPE_ID_FLOAT;
	else if ((*env)->IsInstanceOf(env, array, (*env)->FindClass(env, "[D")))
		*outType = SJME_BASIC_TYPE_ID_DOUBLE;
	else
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_arrayGetElements(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jobject array,
	sjme_attrOutNotNull sjme_pointer* rawBuf,
	sjme_attrOutNotNull jboolean* isCopy,
	sjme_attrOutNullable sjme_jint* typeSize)
{
	sjme_errorCode error;
	sjme_javaTypeId type;
	
	if (env == NULL || array == NULL || rawBuf == NULL || isCopy == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get array type. */
	type = -1;
	if (sjme_error_is(error = sjme_jni_arrayType(env, array,
		&type)) || type < 0)
		return sjme_error_default(error);
	
	/* Depends on the type. */
	switch (type)
	{
		case SJME_BASIC_TYPE_ID_BOOLEAN:
			*rawBuf = (*env)->GetBooleanArrayElements(env, array, isCopy);
			if (typeSize != NULL)
				*typeSize = 1;
			break;
			
		case SJME_BASIC_TYPE_ID_BYTE:
			*rawBuf = (*env)->GetByteArrayElements(env, array, isCopy);
			if (typeSize != NULL)
				*typeSize = 1;
			break;
			
		case SJME_BASIC_TYPE_ID_SHORT:
			*rawBuf = (*env)->GetShortArrayElements(env, array, isCopy);
			if (typeSize != NULL)
				*typeSize = 2;
			break;
			
		case SJME_BASIC_TYPE_ID_CHARACTER:
			*rawBuf = (*env)->GetCharArrayElements(env, array, isCopy);
			if (typeSize != NULL)
				*typeSize = 2;
			break;
			
		case SJME_BASIC_TYPE_ID_INTEGER:
			*rawBuf = (*env)->GetIntArrayElements(env, array, isCopy);
			if (typeSize != NULL)
				*typeSize = 4;
			break;
			
		case SJME_BASIC_TYPE_ID_LONG:
			*rawBuf = (*env)->GetLongArrayElements(env, array, isCopy);
			if (typeSize != NULL)
				*typeSize = 8;
			break;
			
		case SJME_BASIC_TYPE_ID_FLOAT:
			*rawBuf = (*env)->GetFloatArrayElements(env, array, isCopy);
			if (typeSize != NULL)
				*typeSize = 4;
			break;
			
		case SJME_BASIC_TYPE_ID_DOUBLE:
			*rawBuf = (*env)->GetDoubleArrayElements(env, array, isCopy);
			if (typeSize != NULL)
				*typeSize = 8;
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_arrayReleaseElements(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull jarray array,
	sjme_attrInNotNull sjme_pointer rawBuf)
{
	sjme_errorCode error;
	sjme_javaTypeId type;
	
	if (env == NULL || array == NULL || rawBuf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Get array type. */
	type = -1;
	if (sjme_error_is(error = sjme_jni_arrayType(env, array,
		&type)) || type < 0)
		return sjme_error_default(error);
	
	/* Depends on the type. */
	switch (type)
	{
		case SJME_BASIC_TYPE_ID_BOOLEAN:
			(*env)->ReleaseBooleanArrayElements(env, array, rawBuf, 0);
			break;
			
		case SJME_BASIC_TYPE_ID_BYTE:
			(*env)->ReleaseByteArrayElements(env, array, rawBuf, 0);
			break;
			
		case SJME_BASIC_TYPE_ID_SHORT:
			(*env)->ReleaseShortArrayElements(env, array, rawBuf, 0);
			break;
			
		case SJME_BASIC_TYPE_ID_CHARACTER:
			(*env)->ReleaseCharArrayElements(env, array, rawBuf, 0);
			break;
			
		case SJME_BASIC_TYPE_ID_INTEGER:
			(*env)->ReleaseIntArrayElements(env, array, rawBuf, 0);
			break;
			
		case SJME_BASIC_TYPE_ID_LONG:
			(*env)->ReleaseLongArrayElements(env, array, rawBuf, 0);
			break;
			
		case SJME_BASIC_TYPE_ID_FLOAT:
			(*env)->ReleaseFloatArrayElements(env, array, rawBuf, 0);
			break;
			
		case SJME_BASIC_TYPE_ID_DOUBLE:
			(*env)->ReleaseDoubleArrayElements(env, array, rawBuf, 0);
			break;
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}
