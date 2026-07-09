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
	topState = inWindow->component.common.frontEnd.base.data;
	topWindow = inWindow->component.common.frontEnd.base.wrapper;
	
	if (topState == NULL || topWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Get target listener. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(topWindow, close);

	/* Forward call. */
	return infoCore->callback(topState, topWindow);
}

static sjme_errorCode sjme_scritchui_fb_listenerMenuItemActivate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_uiMenuKind activatedItem)
{
	sjme_scritchui topState;
	sjme_scritchui_uiWindow topWindow;
	sjme_scritchui_uiMenuKind topMenu;
	sjme_scritchui_listener_menuItemActivate* infoUser;
	
	if (inState == NULL || inWindow == NULL || activatedItem == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get owning state and component. */
	topState = inWindow->component.common.frontEnd.base.data;
	topWindow = inWindow->component.common.frontEnd.base.wrapper;
	topMenu = activatedItem->common.frontEnd.base.wrapper;
	
	if (topState == NULL || topWindow == NULL || topMenu == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Get target listener. */
	infoUser = &SJME_SCRITCHUI_LISTENER_USER(topWindow, menuItemActivate);

	/* Forward call, if it exists, otherwise ignore. */
	if (infoUser->callback != NULL)
		return infoUser->callback(topState, topWindow,
			topMenu);
	return SJME_ERROR_NONE;
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
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	
	if (wrappedState == NULL || wrappedWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward call. */
	return wrappedState->apiInThread->windowContentMinimumSize(
		wrappedState,
		wrappedWindow, width, height);
}

sjme_errorCode sjme_scritchui_fb_windowGetFrame(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrOutNullable sjme_scritchui_dim* contentSize,
	sjme_attrOutNullable sjme_scritchui_rect* frameBound,
	sjme_attrOutNullable sjme_scritchui_rect* contentBound)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow inWindow, wrappedWindow;
	
	if (inState == NULL || inContainer == NULL ||
		(contentSize == NULL && frameBound == NULL && contentBound == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover the window. */
	inWindow = SJME_SUI_CAST_WINDOW(inContainer);
	
	if (inWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow = inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	
	if (wrappedState == NULL || wrappedWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward to container logic. */
	return wrappedState->apiInThread->containerGetFrame(
		wrappedState, SJME_SUI_CAST_COMPONENT(wrappedWindow),
		contentSize, frameBound, contentBound);
}

sjme_errorCode sjme_scritchui_fb_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_pointer ignored)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	
	if (wrappedState == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Create a wrapped panel. */
	wrappedWindow = NULL;
	if (sjme_error_is(error = wrappedState->apiInThread->windowNew(
		wrappedState, &wrappedWindow)) ||
		wrappedWindow == NULL)
		return sjme_error_default(error);
	
	/* Map front ends. */
	if (sjme_error_is(error = sjme_scritchui_fb_biMap(
		inState, SJME_SUI_CAST_COMMON(inWindow),
		SJME_SUI_CAST_COMMON(wrappedWindow))))
		return sjme_error_default(error);
	
	/* We need to wrap menu activation for this window. */
	if (sjme_error_is(error =
		wrappedState->apiInThread->windowSetMenuItemActivateListener(
			wrappedState, wrappedWindow,
			sjme_scritchui_fb_listenerMenuItemActivate,
			NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_windowSetFlags(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_jint setFlags,
	sjme_attrOutNullable sjme_jint* actualFlags)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	
	if (wrappedState == NULL || wrappedWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward call. */
	return wrappedState->apiInThread->windowSetFlags(wrappedState,
		wrappedWindow, setFlags, actualFlags);
}

sjme_errorCode sjme_scritchui_fb_windowSetCloseListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(close))
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	sjme_frontEndBindable wrappedFrontEnd;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	
	if (wrappedState == NULL || wrappedWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Set listener information. */
	memset(&wrappedFrontEnd, 0, sizeof(wrappedFrontEnd));
	if (sjme_error_is(error = sjme_scritchui_fb_biSetListener(
		inState, SJME_SUI_CAST_COMPONENT(inWindow),
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
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	wrappedMenuBar = (inMenuBar != NULL ? inMenuBar->menuKind.common
		.handle[SJME_SUI_FB_H_WRAPPED] : NULL);
	
	if (wrappedState == NULL || wrappedWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward call. */
	return wrappedState->apiInThread->windowSetMenuBar(wrappedState,
		wrappedWindow, wrappedMenuBar);
}

sjme_errorCode sjme_scritchui_fb_windowSetState(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_scritchui_windowState setState,
	sjme_attrOutNullable sjme_scritchui_windowState* actualState)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (setState < 0 || setState >= SJME_SCRITCHUI_WINDOW_NUM_STATES)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	
	if (wrappedState == NULL || wrappedWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward call. */
	return wrappedState->apiInThread->windowSetState(wrappedState,
		wrappedWindow, setState, actualState);
}

sjme_errorCode sjme_scritchui_fb_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	sjme_scritchui wrappedState;
	sjme_scritchui_uiWindow wrappedWindow;
	
	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapped state. */
	wrappedState = inState->wrappedState;
	wrappedWindow =
		inWindow->component.common.handle[SJME_SUI_FB_H_WRAPPED];
	
	if (wrappedState == NULL || wrappedWindow == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward call. */
	return wrappedState->apiInThread->windowSetVisible(wrappedState,
		wrappedWindow, isVisible);
}
