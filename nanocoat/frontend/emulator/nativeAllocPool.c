/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "sjme/alloc.h"
#include "sjme/list.h"
#include "frontend/emulator/jniHelper.h"

jlong SJME_JNI_METHOD(SJME_CLASS_ALLOC_POOL, _1_1alloc)
	(JNIEnv* env, jclass classy, jlong poolPtr, jint size)
{
	sjme_alloc_pool* pool;
	sjme_errorCode error;
	void* result;

	if (poolPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Get the pool to allocate in. */
	pool = SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr);

	/* Attempt allocation. */
	result = NULL;
	error = SJME_ERROR_UNKNOWN;
	if (SJME_IS_ERROR(error = sjme_alloc(pool, size, &result)) ||
		result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Return the allocated block. */
	return SJME_POINTER_TO_JLONG(result);
}

jlong SJME_JNI_METHOD(SJME_CLASS_ALLOC_POOL, _1_1flatten)
	(JNIEnv* env, jclass classy, jlong poolPtr, jobjectArray javaStrings)
{
	sjme_errorCode error;
	sjme_jint arrayLen;
	sjme_list_sjme_lpcstr* result;
	sjme_lpcstr* utfStrings;
	jboolean* isCopies;
	sjme_jint i;
	jstring string;

	/* Get length of the input array. */
	arrayLen = (*env)->GetArrayLength(env, javaStrings);

	/* Allocate an array that can get the pointers for flattening. */
	utfStrings = alloca(sizeof(*utfStrings) * arrayLen);
	isCopies = alloca(sizeof(*isCopies) * arrayLen);

	/* Fill in string values accordingly. */
	for (i = 0; i < arrayLen; i++)
	{
		/* Get the string here, ignore nulls. */
		string = (*env)->GetObjectArrayElement(env, javaStrings, i);
		if (string == NULL)
			utfStrings[i] = NULL;

		/* Load in string. */
		else
			utfStrings[i] = (*env)->GetStringUTFChars(env, string,
				&isCopies[i]);
	}

	/* Perform native call, handle error later. */
	result = NULL;
	error = sjme_list_flattenArgCV(
		SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr),
		&result, arrayLen, utfStrings);

	/* Cleanup any resultant strings. */
	for (i = 0; i < arrayLen; i++)
	{
		/* Get the string here, ignore nulls. */
		string = (*env)->GetObjectArrayElement(env, javaStrings, i);
		if (string == NULL)
			continue;

		/* Release characters accordingly. */
		(*env)->ReleaseStringUTFChars(env, string, utfStrings[i]);
	}

	/* Fail? */
	if (SJME_IS_ERROR(error) || result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Success. */
	return SJME_POINTER_TO_JLONG(result);
}

jlong SJME_JNI_METHOD(SJME_CLASS_ALLOC_POOL, _1_1getLink)
	(JNIEnv* env, jclass classy, jlong blockPtr)
{
	sjme_errorCode error;
	sjme_alloc_link* result;

	if (blockPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Get the link. */
	result = NULL;
	error = SJME_ERROR_UNKNOWN;
	if (SJME_IS_ERROR(error = sjme_alloc_getLink(
		SJME_JLONG_TO_POINTER(void*, blockPtr), &result)) ||
		result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	return SJME_POINTER_TO_JLONG(result);
}

jlong SJME_JNI_METHOD(SJME_CLASS_ALLOC_POOL, _1_1poolMalloc)
	(JNIEnv* env, jclass classy, jint size, jobject this)
{
	sjme_alloc_pool* result;
	sjme_errorCode error;

	/* Attempt pool allocation. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc_poolInitMalloc(&result,
		size)) || result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0L;
	}

	/* Set self reference object, we need a global reference for it. */
	result->frontEnd.data = env;
	result->frontEnd.wrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, this));

	return SJME_POINTER_TO_JLONG(result);
}

jlong SJME_JNI_METHOD(SJME_CLASS_ALLOC_POOL, _1_1poolStatic)
	(JNIEnv* env, jclass classy, jlong addrPtr, jint size, jobject this)
{
	sjme_alloc_pool* result;
	sjme_errorCode error;

	/* Attempt pool initialize. */
	result = NULL;
	if (SJME_IS_ERROR(error = sjme_alloc_poolInitStatic(&result,
	  SJME_JLONG_TO_POINTER(void*, addrPtr), size)) || result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0L;
	}

	/* Set self reference object, we need a global reference for it. */
	result->frontEnd.data = env;
	result->frontEnd.wrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, this));

	return SJME_POINTER_TO_JLONG(result);
}

