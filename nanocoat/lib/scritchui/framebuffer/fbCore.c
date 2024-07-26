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
#include "lib/scritchui/framebuffer/fbIntern.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/core/core.h"

const sjme_scritchui_implFunctions sjme_scritchui_fbFunctions =
{
	.apiInit = sjme_scritchui_fb_apiInit,
	.choiceItemInsert = sjme_scritchui_fb_choiceItemInsert,
	.choiceItemRemove = sjme_scritchui_fb_choiceItemRemove,
	.choiceItemSetEnabled = sjme_scritchui_fb_choiceItemSetEnabled,
	.choiceItemSetImage = sjme_scritchui_fb_choiceItemSetImage,
	.choiceItemSetSelected = sjme_scritchui_fb_choiceItemSetSelected,
	.choiceItemSetString = sjme_scritchui_fb_choiceItemSetString,
	.componentRepaint = sjme_scritchui_fb_componentRepaint,
	.componentRevalidate = sjme_scritchui_fb_componentRevalidate,
	.componentSetInputListener = sjme_scritchui_fb_componentSetInputListener,
	.componentSetPaintListener = sjme_scritchui_fb_componentSetPaintListener,
	.componentSetSizeListener = sjme_scritchui_fb_componentSetSizeListener,
	.componentSetVisibleListener =
		sjme_scritchui_fb_componentSetVisibleListener,
	.componentSize = sjme_scritchui_fb_componentSize,
	.containerAdd = sjme_scritchui_fb_containerAdd,
	.containerRemove = sjme_scritchui_fb_containerRemove,
	.containerSetBounds = sjme_scritchui_fb_containerSetBounds,
	.hardwareGraphics = NULL,
	.labelSetString = sjme_scritchui_fb_labelSetString,
	.listNew = sjme_scritchui_fb_listNew,
	.loopExecute = sjme_scritchui_fb_loopExecute,
	.loopExecuteLater = sjme_scritchui_fb_loopExecuteLater,
	.loopExecuteWait = sjme_scritchui_fb_loopExecuteWait,
	.menuBarNew = sjme_scritchui_fb_menuBarNew,
	.menuInsert = sjme_scritchui_fb_menuInsert,
	.menuItemNew = sjme_scritchui_fb_menuItemNew,
	.menuNew = sjme_scritchui_fb_menuNew,
	.menuRemove = sjme_scritchui_fb_menuRemove,
	.panelEnableFocus = sjme_scritchui_fb_panelEnableFocus,
	.panelNew = sjme_scritchui_fb_panelNew,
	.screens = sjme_scritchui_fb_screens,
	.windowContentMinimumSize = sjme_scritchui_fb_windowContentMinimumSize,
	.windowNew = sjme_scritchui_fb_windowNew,
	.windowSetCloseListener = sjme_scritchui_fb_windowSetCloseListener,
	.windowSetMenuBar = sjme_scritchui_fb_windowSetMenuBar,
	.windowSetVisible = sjme_scritchui_fb_windowSetVisible,
};

static const struct sjme_scritchui_implInternFunctions sjme_scritchui_fbInter =
{
	.render = sjme_scritchui_fb_intern_render,
	.renderInScroll = sjme_scritchui_fb_intern_renderInScroll,
};

sjme_errorCode sjme_scritchui_fb_apiInit(
	sjme_attrInNotNull sjme_scritchui inState)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Debug. */
	sjme_message("Framebuffer wrapper initialized!");
	
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
	wrapped->frontEnd.wrapper = topLevel;
	wrapped->frontEnd.data = inState;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_biSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull sjme_scritchui_listener_void* infoCore,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(void),
	sjme_attrInOutNotNull sjme_frontEnd* wrappedFrontEnd)
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
		wrappedFrontEnd->wrapper = inComponent;
		wrappedFrontEnd->data = inState;
	}
	
	/* Make sure front end is copied or cleared as well. */
	if (copyFrontEnd != NULL)
		memmove(&infoCore->frontEnd, copyFrontEnd,
			sizeof(infoCore->frontEnd));
	else
		memset(&infoCore->frontEnd, 0, sizeof(infoCore->frontEnd));
	
	/* Success! */
	/* The caller should set the target wrapped listener. */
	return SJME_ERROR_NONE;
}
