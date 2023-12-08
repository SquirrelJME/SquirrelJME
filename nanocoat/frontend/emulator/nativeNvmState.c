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

#define SJME_CLASS_NVM_STATE cc_squirreljme_vm_nanocoat_NvmState

jlong SJME_JNI_METHOD(SJME_CLASS_NVM_STATE, _1_1nvmBoot)
	(JNIEnv* env, jclass classy, jlong poolPtr, jobject wrapper)
{
	sjme_nvm_state* state;
	sjme_errorCode error;

	/* Initialize new state. */
	state = NULL;
	if (SJME_IS_ERROR(error = sjme_nvm_boot(
		SJME_JLONG_TO_POINTER(sjme_alloc_pool*, poolPtr),
		NULL, &state, 0, NULL)) || state == NULL)
	{
		sjme_jni_throwVMException(env, error);
		return 0;
	}

	/* Set self reference object, we need a global reference for it. */
	state->frontEndWrapper = SJME_FRONT_END_WRAP(
		(*env)->NewGlobalRef(env, wrapper));

	/* Use pointer to the state. */
	return SJME_POINTER_TO_JLONG(state);
}