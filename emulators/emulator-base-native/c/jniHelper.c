/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
	jfieldID pointerField;
	
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
	baseClassy = (*env)->FindClass(env, DESC_DYLIB_BASE);
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
	
	/* Get the pointer data. */
	pointerField = (*env)->GetFieldID(env, baseClassy, "objectP", "J");
	if (pointerField == NULL)
		sjme_die("No objectP in DylibBaseObject?");
	
	/* Cast pencil data. */
	return (void*)((intptr_t)((*env)->GetLongField(
		env, instance, pointerField)));
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
