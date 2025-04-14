/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "sjme/nvm/allocSizeOf.h"
#include "frontend/emulator/jniHelper.h"

jint SJME_JNI_METHOD(SJME_CLASS_ALLOC_SIZEOF, _1_1size)
	(JNIEnv* env, jclass classy, jint id, jint count)
{
	sjme_jint result;
	sjme_errorCode error;

	/* Calculate the size. */
	result = -1;
	if (sjme_error_is(error = sjme_alloc_sizeOf(id, count, &result)) ||
		id < 0)
	{
		sjme_jni_throwVMException(env, error);
		return -1;
	}

	/* Use the given result. */
	return result;
}
