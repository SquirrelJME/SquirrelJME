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

static sjme_errorCode sjme_jni_releaseFrontEnd(
	sjme_attrInNotNull sjme_pointer owner,
	sjme_attrInOutNotNull sjme_frontEndBindable* frontEnd,
	sjme_attrOutNotNull sjme_pointer* resultData,
	sjme_attrInValue sjme_frontEnd_bindAction action)
{
	sjme_errorCode error;
	JNIEnv* env;
	jweak ref;

	if (owner == NULL || frontEnd == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover the front end information. */
	if (sjme_error_is(error = sjme_jni_recoverEnvFrontEnd(
		&env, frontEnd)))
		return sjme_error_default(error);

	/* Delete global reference filled in via the front end filler. */
	ref = frontEnd->base.wrapper;
	if (action == SJME_FRONTEND_RELEASE)
		if (ref != NULL)
		{
			frontEnd->base.wrapper = NULL;
			(*env)->DeleteGlobalRef(env, ref);
		}

	/* Success! */
	return SJME_ERROR_NONE;
}

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

sjme_errorCode sjme_jni_setIntArray(JNIEnv* env, jintArray array,
	jint offset, jint len, ...)
{
	jint* values;
	sjme_jint i;
	va_list args;

	if (env == NULL || array == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (offset < 0 || len < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Pointless? */
	if (len == 0)
		return SJME_ERROR_NONE;

	/* Allocate values. */
	values = sjme_alloca(sizeof(*values) * len);
	if (values == NULL)
		return sjme_error_outOfMemory(NULL, len);

	/* Clear all values. */
	memset(values, 0, sizeof(*values) * len);

	/* Read in. */
	va_start(args, len);
	for (i = 0; i < len; i++)
		values[i] = va_arg(args, jint);
	va_end(args);

	/* Set array values. */
	(*env)->SetIntArrayRegion(env, array, offset, len, values);

	/* Cleanup. */
	sjme_alloca_free(values);

	/* Success! */
	return SJME_ERROR_NONE;
}

void sjme_jni_throwMLECallError(JNIEnv* env, sjme_errorCode code)
{
	jclass tossingClass;
	jmethodID methodId;
	jobject tossing;

	if (env == NULL)
		return;

	/* Get the class where the exception is. */
	tossingClass = (*env)->FindClass(env,
		"cc/squirreljme/jvm/mle/exceptions/MLECallError");
	if (tossingClass == NULL)
	{
		sjme_die("Could not find exception class?");
		return;
	}

	/* Find constructor. */
	methodId = (*env)->GetMethodID(env, tossingClass, "<init>",
		"(I)V");
	if (methodId == NULL)
	{
		sjme_die("Could not find exception constructor?");
		return;
	}

	/* Make new instance. */
	tossing = (*env)->NewObject(env, tossingClass, methodId, code);
	if (tossing == NULL)
	{
		sjme_die("Could not create throwable to toss?");
		return;
	}

	/* Throw it. */
	if ((*env)->Throw(env, tossing) != 0)
		sjme_die("Could not throw MLECallError?");
}

void sjme_jni_throwNullPointerException(JNIEnv* env)
{
	if (env == NULL)
		return;

	sjme_jni_throwThrowable(env, SJME_ERROR_NULL_ARGUMENTS,
		"java/lang/NullPointerException");
}

void sjme_jni_throwThrowable(JNIEnv* env, sjme_errorCode code,
	sjme_lpcstr type)
{
#define BUF_SIZE 512
	jclass tossingClass;
	char buf[BUF_SIZE];

	if (env == NULL || type == NULL)
		return;

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
	if (env == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return NULL;
	}

	if (className == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_INVALID_CLASS_NAME);
		return NULL;
	}

	/* Locate class. */
	classy = (*env)->FindClass(env, className);
	baseClassy = (*env)->FindClass(env, DESC_DYLIB_HAS_OBJECT_POINTER);
	if (classy == NULL || baseClassy == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NO_CLASS);
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

sjme_errorCode sjme_jni_fillFrontEnd(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull sjme_frontEndBindable* into,
	sjme_attrInNullable jobject ref)
{
	JavaVM* vm;

	if (env == NULL || into == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Store referenced VM. */
	vm = NULL;
	(*env)->GetJavaVM(env, &vm);
	into->base.data = vm;

	/* Need to reference an object? */
	into->bindHandler = sjme_jni_releaseFrontEnd;
	if (ref != NULL)
		into->base.wrapper = (*env)->NewGlobalRef(env, ref);
	else
		into->base.wrapper = NULL;

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

sjme_errorCode sjme_jni_recoverEnvThis(
	sjme_attrInOutNotNull JNIEnv** outEnv)
{
	JavaVM* vm;
	jint resultLen;

	vm = NULL;
	if (JNI_OK != JNI_GetCreatedJavaVMs(&vm, 1, &resultLen) ||
		resultLen == 0)
		return SJME_JNI_FALSE;

	return sjme_jni_recoverEnv(outEnv, vm);
}

sjme_errorCode sjme_jni_recoverEnvFrontEnd(
	sjme_attrInOutNotNull JNIEnv** outEnv,
	sjme_attrInNotNull const sjme_frontEndBindable* inFrontEnd)
{
	if (outEnv == NULL || inFrontEnd == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Forward. */
	return sjme_jni_recoverEnv(outEnv, inFrontEnd->base.data);
}

static sjme_jchar sjme_jni_jstringCharAt(
	sjme_attrInNotNull sjme_charSeq inSeq,
	sjme_attrInPositive sjme_jint inIndex)
{
	JNIEnv* env;
	jstring string;
	const jchar* stringChars;
	jboolean isCopy;
	jint len;
	jchar outChar;

	if (inSeq == NULL)
		return 0;

	/* Recover env. */
	env = NULL;
	if (sjme_error_is(sjme_jni_recoverEnvFrontEnd(
		&env, SJME_AS_FE_BINDABLEP(&inSeq->data.function.frontEnd))) ||
		env == NULL)
		return 0;

	/* Get string. */
	string = inSeq->data.function.frontEnd.base.wrapper;

	/* Not within the string bounds? */
	len = (*env)->GetStringLength(env, string);
	if (inIndex < 0 || inIndex >= len)
		return 0;

	/* Need to access characters just to read one, sadly. */
	isCopy = JNI_FALSE;
	stringChars = (*env)->GetStringChars(env, string, &isCopy);

	/* Copy character. */
	outChar = stringChars[inIndex];

	/* Cleanup. */
	(*env)->ReleaseStringChars(env, string, stringChars);

	/* Success! */
	return outChar;
}

static sjme_errorCode sjme_jni_jstringLength(
	sjme_attrInNotNull sjme_charSeq inSeq,
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
		&env, SJME_AS_FE_BINDABLEP(&inSeq->data.function.frontEnd))) ||
		env == NULL)
		return sjme_error_default(error);

	/* Get string. */
	string = inSeq->data.function.frontEnd.base.wrapper;

	/* Get string length. */
	*outLen = (*env)->GetStringLength(env, string);

	/* Success! */
	return SJME_ERROR_NONE;
}

static const sjme_charSeq_functions sjme_jni_jstringFunctions =
{
	sjme_sm(.charAt, sjme_jni_jstringCharAt),
	sjme_sm(.length, sjme_jni_jstringLength),
};

sjme_errorCode sjme_jni_charSeq(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInOutNotNull sjme_charSeqStatic* inOutSeq,
	sjme_attrInNotNull jstring inString)
{
	sjme_frontEndBindable frontEnd;
	sjme_errorCode error;

	if (env == NULL || inOutSeq == NULL || inString == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup front end. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	if (sjme_error_is(error = sjme_jni_fillFrontEnd(env,
		&frontEnd, inString)))
		return sjme_error_default(error);

	/* Initialize via forward. */
	memset(inOutSeq, 0, sizeof(*inOutSeq));
	return sjme_charSeq_newFunctionStatic(
		inOutSeq, &sjme_jni_jstringFunctions, &frontEnd);
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
		return sjme_error_fatal(SJME_ERROR_INVALID_ARGUMENT);

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
			return sjme_error_fatal(SJME_ERROR_INVALID_ARGUMENT);
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
			return sjme_error_fatal(SJME_ERROR_INVALID_ARGUMENT);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_fontParamFromFlat(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFontParam* destParams,
	sjme_attrInNotNull jintArray srcFlat)
{
	sjme_errorCode error;
	jint* raw;
	jboolean isCopy;

	if (env == NULL || inState == NULL ||
		destParams == NULL || srcFlat == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState->intern == NULL || inState->intern->fontParamFromFlat == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);

	/* Get array elements. */
	raw = NULL;
	isCopy = JNI_FALSE;
	if (sjme_error_is(error = sjme_jni_arrayGetElements(env, srcFlat,
		(sjme_pointer*)&raw, &isCopy, NULL)))
		return sjme_error_default(error);

	/* Map. */
	memset(destParams, 0, sizeof(*destParams));
	if (sjme_error_is(error = inState->intern->fontParamFromFlat(
		inState,
		destParams,
		(const sjme_jint*)raw, 0,
		(*env)->GetArrayLength(env, srcFlat))))
		return sjme_error_default(error);

	/* Release array. */
	sjme_jni_arrayReleaseElements(env, srcFlat, raw);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_jni_fontParamToFlat(
	sjme_attrInNotNull JNIEnv* env,
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull jintArray destFlat,
	sjme_attrOutNotNull const sjme_scritchui_pencilFontParam* srcParams)
{
	sjme_errorCode error;
	jint* raw;
	jboolean isCopy;

	if (env == NULL || inState == NULL ||
		srcParams == NULL || destFlat == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inState->intern == NULL || inState->intern->fontParamToFlat == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);

	/* Get array elements. */
	raw = NULL;
	isCopy = JNI_FALSE;
	if (sjme_error_is(error = sjme_jni_arrayGetElements(env, destFlat,
		(sjme_pointer*)&raw, &isCopy, NULL)))
		return sjme_error_default(error);

	/* Map. */
	if (sjme_error_is(error = inState->intern->fontParamToFlat(
		inState,
		srcParams,
		(sjme_jint*)raw, 0,
		(*env)->GetArrayLength(env, destFlat))))
		return sjme_error_default(error);

	/* Release array. */
	sjme_jni_arrayReleaseElements(env, destFlat, raw);

	/* Success! */
	return SJME_ERROR_NONE;
}
