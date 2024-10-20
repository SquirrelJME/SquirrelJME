/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

#include <string.h>

#include "squirreljme.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

/** The class being implemented. */
#define IMPL_CLASS "cc/squirreljme/emulator/scritchui/dylib/DylibBaseObject"
#define FORWARD_CLASS IMPL_CLASS

#define FORWARD_DESC___bind "(" \
	DESC_LONG DESC_OBJECT ")" DESC_VOID

JNIEXPORT void JNICALL FORWARD_FUNC_NAME(DylibBaseObject, __bind)
	(JNIEnv* env, jclass classy, jlong componentP, jobject bindTo)
{
	sjme_scritchui_uiComponent component;
	sjme_errorCode error;
	
	if (componentP == 0 || bindTo == NULL)
	{
		sjme_jni_throwMLECallError(env, SJME_ERROR_NULL_ARGUMENTS);
		return;
	}
	
	/* Get component back. */
	component = (sjme_scritchui_uiComponent)componentP;
	
	/* Set forward states. */
	if (sjme_error_is(error = sjme_jni_fillFrontEnd(env,
		&component->common.frontEnd, bindTo)))
		sjme_jni_throwMLECallError(env, error);
}

static const JNINativeMethod mleDylibBaseObjectMethods[] =
{
	FORWARD_list(DylibBaseObject, __bind),
};

FORWARD_init(mleDylibBaseObjectInit,
	mleDylibBaseObjectMethods)
