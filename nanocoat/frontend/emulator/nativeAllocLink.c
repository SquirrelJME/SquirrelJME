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
#include "frontend/emulator/jniHelper.h"

void SJME_JNI_METHOD(SJME_CLASS_ALLOC_LINK, _1_1read)
	(JNIEnv* env, jclass classy, jlong blockPtr, jint at,
		jbyteArray buf, jint off, jint len)
{
	/* Use the get operation to "put" the bytes from native memory. */
	(*env)->SetByteArrayRegion(env, buf, off, len,
		SJME_JLONG_TO_POINTER(jbyte*, blockPtr + at));
}

jint SJME_JNI_METHOD(SJME_CLASS_ALLOC_LINK, _1_1size)
	(JNIEnv* env, jclass classy, jlong linkPtr)
{
	if (linkPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Get the allocation size. */
	return SJME_JLONG_TO_POINTER(sjme_alloc_link*, linkPtr)->allocSize;
}

void SJME_JNI_METHOD(SJME_CLASS_ALLOC_LINK, _1_1write)
	(JNIEnv* env, jclass classy, jlong blockPtr, jint at,
		jbyteArray buf, jint off, jint len)
{
	/* Use the get operation to "get" the bytes into the native memory. */
	(*env)->GetByteArrayRegion(env, buf, off, len,
		SJME_JLONG_TO_POINTER(jbyte*, blockPtr + at));
}
