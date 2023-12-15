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
