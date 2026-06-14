/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include "squirreljme.h"
#include "lib/scritchui/scritchui.h"

/** The class being implemented. */
#define IMPL_CLASS "cc/squirreljme/emulator/scritchui/dylib/__NativeCallback__"
#define FORWARD_CLASS IMPL_CLASS

#define FORWARD_DESC___invoke DESC_METHOD(DESC_INT,  \
	DESC_LONG DESC_LONG DESC_LONG )

JNIEXPORT jint JNICALL FORWARD_FUNC_NAME(NativeScritchCallback, __invoke)
	(JNIEnv* env, jclass classy, jlong stateP, jlong callbackP,
		jlong anythingP)
{
	sjme_scritchui state;
	sjme_thread_mainFunc callback;
	sjme_thread_parameter anything;

	state = (sjme_scritchui)stateP;
	callback = (sjme_thread_mainFunc)callbackP;
	anything = (sjme_thread_parameter)anythingP;
	if (state == 0 || callback == 0)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return -1;
	}

	/* Invoke it. */
	return SJME_THREAD_RESULT_AS_ERROR(callback(anything));
}

static const JNINativeMethod mleNativeScritchCallbackMethods[] =
{
	FORWARD_list(NativeScritchCallback, __invoke),
};

FORWARD_init(mleNativeScritchCallbackInit, mleNativeScritchCallbackMethods)
