/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>
#include <string.h>

#include "sjme/alloc.h"
#include "sjme/list.h"
#include "frontend/emulator/jniHelper.h"

jlong SJME_JNI_METHOD(SJME_CLASS_FLAT_LIST, _1_1flatten)
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
	utfStrings = sjme_alloca(sizeof(*utfStrings) * arrayLen);
	isCopies = sjme_alloca(sizeof(*isCopies) * arrayLen);
	
	if (utfStrings == NULL || isCopies == NULL)
	{
		sjme_jni_throwVMException(env, sjme_error_outOfMemory(NULL, arrayLen));
		return 0;
	}

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
	if (sjme_error_is(error) || result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Success. */
	return SJME_POINTER_TO_JLONG(result);
}

jlong SJME_JNI_METHOD(SJME_CLASS_FLAT_LIST, _1_1fromArrayI)
	(JNIEnv* env, jclass classy, jlong poolPtr, jintArray javaInts)
{
	sjme_jint arrayLen;
	jboolean copied;
	jint* primitiveInts;
	sjme_errorCode error;
	sjme_list_sjme_jint* result;

	/* Get length of the input array. */
	arrayLen = (*env)->GetArrayLength(env, javaInts);

	/* Get pointer to elements in the array. */
	copied = JNI_FALSE;
	primitiveInts = (*env)->GetIntArrayElements(env, javaInts, &copied);
	
	/* Setup new array. */
	result = NULL;
	error = sjme_list_newA(
		SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr),
		sjme_jint, 0, arrayLen, &result, primitiveInts);

	/* Make sure they are freed. */
	(*env)->ReleaseIntArrayElements(env, javaInts, primitiveInts, JNI_ABORT);
	
	/* Failed? */
	if (sjme_error_is(error))
		sjme_jni_throwVMException(env, error);
	
	/* Use the given result. */
	return SJME_POINTER_TO_JLONG(result);
}

jlong SJME_JNI_METHOD(SJME_CLASS_FLAT_LIST, _1_1fromArrayP)
	(JNIEnv* env, jclass classy, jlong poolPtr, jlongArray inPtrs)
{
	sjme_jint arrayLen, i;
	jboolean copied;
	jlong* longPtrs;
	sjme_pointer* primPtrs;
	sjme_errorCode error;
	sjme_list_sjme_jint* result;

	/* Get length of the input array. */
	arrayLen = (*env)->GetArrayLength(env, inPtrs);

	/* Get pointer to elements in the array. */
	copied = JNI_FALSE;
	longPtrs = (*env)->GetLongArrayElements(env, inPtrs, &copied);
	
	/* Allocate. */
	primPtrs = sjme_alloca(sizeof(*primPtrs) * arrayLen);
	
	if (primPtrs == NULL)
	{
		sjme_jni_throwVMException(env, sjme_error_outOfMemory(NULL, arrayLen));
		return 0;
	}

	/* We cannot just use long here, we need to map down such as on 32-bit. */
	memset(primPtrs, 0, sizeof(*primPtrs) * arrayLen);
	for (i = 0; i < arrayLen; i++)
		primPtrs[i] = SJME_JLONG_TO_POINTER(sjme_pointer, longPtrs[i]);

	/* Make sure they are freed. */
	(*env)->ReleaseLongArrayElements(env, inPtrs, longPtrs, JNI_ABORT);

	/* Setup new array. */
	result = NULL;
	error = sjme_list_newA(
		SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr),
		sjme_pointer, 0, arrayLen, &result, primPtrs);

	/* Failed? */
	if (sjme_error_is(error))
		sjme_jni_throwVMException(env, error);

	/* Use the given result. */
	return SJME_POINTER_TO_JLONG(result);
}
