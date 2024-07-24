/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiTypes.h"

static sjme_errorCode sjme_scritchui_fb_listenerClose(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow)
{
	sjme_scritchui topState;
	sjme_scritchui_uiWindow topWindow;
	sjme_scritchui_listener_close* infoCore;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get owning state and component. */
	topState = inWindow->component.common.frontEnd.data;
	topWindow = inWindow->component.common.frontEnd.wrapper;
	
	/* Get target listener. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(topWindow, close);

	/* Forward call. */
	return infoCore->callback(topState, topWindow);
}

sjme_errorCode sjme_scritchui_fb_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward call. */
	return wrappedState->apiInThread->windowContentMinimumSize(
		wrappedState,
		wrappedWindow, width, height);
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
	if (sjme_error_is(error = wrappedState->apiInThread->windowNew(
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
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	sjme_frontEnd wrappedFrontEnd;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Set listener information. */
	memset(&wrappedFrontEnd, 0, sizeof(wrappedFrontEnd));
	if (sjme_error_is(error = sjme_scritchui_fb_biSetListener(
		inState, inWindow,
		(sjme_scritchui_listener_void*)
			&SJME_SCRITCHUI_LISTENER_CORE(inWindow, close),
		(sjme_scritchui_voidListenerFunc)inListener,
		copyFrontEnd, &wrappedFrontEnd)))
		return sjme_error_default(error);
		
	/* Have wrapped handler call our wrapped listener. */
	return wrappedState->apiInThread->windowSetCloseListener(
		wrappedState,
		wrappedWindow,
		(inListener == NULL ? NULL :
			sjme_scritchui_fb_listenerClose), 
			&wrappedFrontEnd);
}
	
sjme_errorCode sjme_scritchui_fb_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	sjme_scritchui_uiMenuBar wrappedMenuBar;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	if (inMenuBar != NULL)
		wrappedMenuBar = inMenuBar->menuKind.common
			.handle[SJME_SUI_FB_H_WRAPPED];
	else
		wrappedMenuBar = NULL;
	
	/* Forward call. */
	return wrappedState->apiInThread->windowSetMenuBar(wrappedState,
		wrappedWindow, wrappedMenuBar);
}

sjme_errorCode sjme_scritchui_fb_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NONE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	
	/* Forward call. */
	return wrappedState->apiInThread->windowSetVisible(wrappedState,
		wrappedWindow, isVisible);
}
