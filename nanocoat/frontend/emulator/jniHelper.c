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

#include "frontend/emulator/jniHelper.h"
#include "sjme/debug.h"

void sjme_jni_throwVMException(JNIEnv* env, sjme_errorCode code)
{
#define BUF_SIZE 512
	jclass tossingClass;
	char buf[BUF_SIZE];

	/* Get the class where the exception is. */
	tossingClass = (*env)->FindClass(env,
		"cc/squirreljme/emulator/vm/VMException");
	if (tossingClass == NULL)
	{
		sjme_die("Could not find exception class?");
		return;
	}

	/* Generate a message accordingly. */
	memset(buf, 0, sizeof(buf));
	snprintf(buf, BUF_SIZE - 1, "Native error: %d",
		(int)code);
	buf[BUF_SIZE - 1] = 0;

	/* Throw it. */
	if ((*env)->ThrowNew(env, tossingClass, buf) != 0)
		sjme_die("Could not throw a new throwable?");
#undef BUF_SIZE
}
