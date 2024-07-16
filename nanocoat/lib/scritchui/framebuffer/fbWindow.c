/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

sjme_errorCode sjme_scritchui_fb_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_fb_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	/* Create a wrapped panel. */
	wrappedWindow = NULL;
	if (sjme_error_is(error = wrappedState->api->windowNew(
		wrappedState, &wrappedWindow)) ||
		wrappedWindow == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, inWindow, wrappedWindow)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_windowSetCloseListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(close))
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_fb_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
