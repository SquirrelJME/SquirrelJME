/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <jni.h>

#include "frontend/emulator/jniHelper.h"
#include "sjme/boot.h"
#include "sjme/alloc.h"

jlong SJME_JNI_METHOD(SJME_CLASS_NVM_BOOT_PARAM, _1_1allocBootParam)
	(JNIEnv* env, jclass classy, jlong poolPtr)
{
	sjme_nvm_bootParam* result;
	sjme_errorCode error;

	if (poolPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Attempt allocation. */
	result = NULL;
	error = SJME_ERROR_UNKNOWN;
	if (SJME_IS_ERROR(error = sjme_alloc(
		SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr),
		sizeof(*result), (void**)&result)) || result == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	return SJME_POINTER_TO_JLONG(result);
}
