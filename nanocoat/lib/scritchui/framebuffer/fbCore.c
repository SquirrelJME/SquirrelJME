/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/framebuffer/fb.h"
#include "lib/scritchui/framebuffer/fbIntern.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/core/core.h"

const sjme_scritchui_implFunctions sjme_scritchui_fbFunctions =
{
	sjme_sm(.driverName, "fb"),
	sjme_sm(.apiInit, sjme_scritchui_fb_apiInit),
	sjme_sm(.choiceItemInsert, sjme_scritchui_fb_choiceItemInsert),
	sjme_sm(.choiceItemRemove, sjme_scritchui_fb_choiceItemRemove),
	sjme_sm(.choiceItemSetEnabled, sjme_scritchui_fb_choiceItemSetEnabled),
	sjme_sm(.choiceItemSetImage, sjme_scritchui_fb_choiceItemSetImage),
	sjme_sm(.choiceItemSetSelected, sjme_scritchui_fb_choiceItemSetSelected),
	sjme_sm(.choiceItemSetString, sjme_scritchui_fb_choiceItemSetString),
	sjme_sm(.componentFocusGrab, sjme_scritchui_fb_componentFocusGrab),
	sjme_sm(.componentFocusHas, sjme_scritchui_fb_componentFocusHas),
	sjme_sm(.componentPosition, sjme_scritchui_fb_componentPosition),
	sjme_sm(.componentRepaint, sjme_scritchui_fb_componentRepaint),
	sjme_sm(.componentRevalidate, sjme_scritchui_fb_componentRevalidate),
	sjme_sm(.componentSetActivateListener, NULL),
	sjme_sm(.componentSetInputListener, sjme_scritchui_fb_componentSetInputListener),
	sjme_sm(.componentSetPaintListener, sjme_scritchui_fb_componentSetPaintListener),
	sjme_sm(.componentSetSizeListener, sjme_scritchui_fb_componentSetSizeListener),
	sjme_sm(.componentSetVisibleListener,
		sjme_scritchui_fb_componentSetVisibleListener),
	sjme_sm(.componentSize, sjme_scritchui_fb_componentSize),
	sjme_sm(.containerAdd, sjme_scritchui_fb_containerAdd),
	sjme_sm(.containerRemove, sjme_scritchui_fb_containerRemove),
	sjme_sm(.containerSetBounds, sjme_scritchui_fb_containerSetBounds),
	sjme_sm(.fontScanSystem, sjme_scritchui_fb_fontScanSystem),
	sjme_sm(.hardwareGraphics, sjme_scritchui_fb_hardwareGraphics),
	sjme_sm(.labelSetString, sjme_scritchui_fb_labelSetString),
	sjme_sm(.lafDpiProject, sjme_scritchui_fb_lafDpiProject),
	sjme_sm(.lafElementColor, sjme_scritchui_fb_lafElementColor),
	sjme_sm(.lafMetric, sjme_scritchui_fb_lafMetric),
	sjme_sm(.listNew, sjme_scritchui_fb_listNew),
	sjme_sm(.loopExecute, sjme_scritchui_fb_loopExecute),
	sjme_sm(.loopExecuteLater, sjme_scritchui_fb_loopExecuteLater),
	sjme_sm(.loopExecuteWait, sjme_scritchui_fb_loopExecuteWait),
	sjme_sm(.loopIterate, sjme_scritchui_fb_loopIterate),
	sjme_sm(.menuBarNew, sjme_scritchui_fb_menuBarNew),
	sjme_sm(.menuInsert, sjme_scritchui_fb_menuInsert),
	sjme_sm(.menuItemNew, sjme_scritchui_fb_menuItemNew),
	sjme_sm(.menuNew, sjme_scritchui_fb_menuNew),
	sjme_sm(.menuRemove, sjme_scritchui_fb_menuRemove),
	sjme_sm(.panelEnableFocus, sjme_scritchui_fb_panelEnableFocus),
	sjme_sm(.panelNew, sjme_scritchui_fb_panelNew),
	sjme_sm(.screenGetBounds, sjme_scritchui_fb_screenGetBounds),
	sjme_sm(.screens, sjme_scritchui_fb_screens),
	sjme_sm(.scrollPanelNew, sjme_scritchui_fb_scrollPanelNew),
	sjme_sm(.viewGetView, sjme_scritchui_fb_viewGetView),
	sjme_sm(.viewSetArea, sjme_scritchui_fb_viewSetArea),
	sjme_sm(.viewSetView, sjme_scritchui_fb_viewSetView),
	sjme_sm(.viewSetViewListener, sjme_scritchui_fb_viewSetViewListener),
	sjme_sm(.windowContentMinimumSize, 
		sjme_scritchui_fb_windowContentMinimumSize),
	sjme_sm(.windowGetFrame, sjme_scritchui_fb_windowGetFrame),
	sjme_sm(.windowNew, sjme_scritchui_fb_windowNew),
	sjme_sm(.windowSetCloseListener, sjme_scritchui_fb_windowSetCloseListener),
	sjme_sm(.windowSetMenuBar, sjme_scritchui_fb_windowSetMenuBar),
	sjme_sm(.windowSetVisible, sjme_scritchui_fb_windowSetVisible),
};

static const struct sjme_scritchui_implInternFunctions sjme_scritchui_fbInter =
{
	sjme_sm(.lightweightInit, sjme_scritchui_fb_intern_lightweightInit),
	sjme_sm(.logicalButton, sjme_scritchui_fb_intern_logicalButton),
	sjme_sm(.refresh, sjme_scritchui_fb_intern_refresh),
	sjme_sm(.render, sjme_scritchui_fb_intern_render),
	sjme_sm(.renderInScroll, sjme_scritchui_fb_intern_renderInScroll),
};

sjme_errorCode sjme_scritchui_fb_apiInit(
	sjme_attrInNotNull sjme_scritchui inState)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Debug. */
	sjme_message("Framebuffer wrapper initialized!");
	
	/* Copy any native bugs, and clear ones that make no sense for this. */
	memmove(&inState->bugs, &inState->wrappedState->bugs,
		sizeof(inState->bugs));
	inState->bugs.windowVisibilityUnknown = SJME_JNI_FALSE;

	/* Copy all LAF platform flags. */
	inState->platformFlags = inState->wrappedState->platformFlags;
	
	/* Set internal implementation functions. */
	inState->implIntern = &sjme_scritchui_fbInter;
	
	/* We need not do anything special. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_biMap(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommonBase* topLevel,
	sjme_attrInNotNull sjme_scritchui_uiCommonBase* wrapped)
{
	if (inState == NULL || topLevel == NULL || wrapped == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Map to wrapped item. */
	topLevel->handle[SJME_SUI_FB_H_WRAPPED] = wrapped;
	
	/* Then map back to top level item. */
	wrapped->frontEnd.base.wrapper = topLevel;
	wrapped->frontEnd.base.data = inState;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_biSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(void),
	sjme_attrInOutNotNull sjme_frontEndBindable* wrappedFrontEnd)
{
	if (inState == NULL || inComponent == NULL || infoCore == NULL ||
		wrappedFrontEnd == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* When a listener set occurs, we will be setting our own core listener */
	/* information with the callback we want to call. When we call the */
	/* wrapped side, that will call into our core listener that to the */
	/* wrapped side is the user listener. So we can store the listener */
	/* information in our own core side. */
	infoCore->callback = inListener;
	
	/* The callback will be in the wrapped state's context, so we need */
	/* to be able to get the top level context. */
	if (inListener == NULL)
	{
		infoCore->extra = (sjme_intPointer)NULL;
		
		/* Clear as this is not used. */
		memset(wrappedFrontEnd, 0, sizeof(*wrappedFrontEnd));
	}
	else
	{
		infoCore->extra = (sjme_intPointer)inComponent;
		
		/* Put in information so the wrapped code can find the top level. */
		wrappedFrontEnd->base.wrapper = inComponent;
		wrappedFrontEnd->base.data = inState;
	}
	
	/* Make sure front end is copied or cleared as well. */
	if (copyFrontEnd != NULL)
		sjme_frontEnd_copy(&infoCore->frontEnd, copyFrontEnd);
	else
		memset(&infoCore->frontEnd, 0, sizeof(infoCore->frontEnd));
	
	/* Success! */
	/* The caller should set the target wrapped listener. */
	return SJME_ERROR_NONE;
}
