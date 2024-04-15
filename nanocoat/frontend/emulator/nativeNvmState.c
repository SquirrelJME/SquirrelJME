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

jlong SJME_JNI_METHOD(SJME_CLASS_NVM_STATE, _1_1nvmBoot)
	(JNIEnv* env, jclass classy, jlong poolPtr, jlong reservedPtr,
		jobject wrapper, jlong paramPtr)
{
	sjme_nvm_state* state;
	sjme_errorCode error;

	if (poolPtr == 0 || reservedPtr == 0 || wrapper == NULL || paramPtr == 0)
	{
		sjme_jni_throwVMException(env, SJME_ERROR_NULL_ARGUMENTS);
		return 0;
	}

	/* Initialize new state. */
	state = NULL;
	if (sjme_error_is(error = sjme_nvm_boot(
		SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr),
		SJME_JLONG_TO_POINTER(sjme_alloc_pool*, reservedPtr),
		SJME_JLONG_TO_POINTER(const sjme_nvm_bootParam*, paramPtr),
		&state)) || state == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Set self reference object, we need a global reference for it. */
	state->frontEnd.data = env;
	state->frontEnd.wrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, wrapper));

	/* Use pointer to the state. */
	return SJME_POINTER_TO_JLONG(state);
}
