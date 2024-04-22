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
