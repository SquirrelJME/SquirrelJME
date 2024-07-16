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
#include "lib/scritchui/core/core.h"

const sjme_scritchui_implFunctions sjme_scritchui_fbFunctions =
{
	.apiInit = sjme_scritchui_fb_apiInit,
	.componentRepaint = NULL,
	.componentRevalidate = NULL,
	.componentSetInputListener = NULL,
	.componentSetPaintListener = NULL,
	.componentSetSizeListener = NULL,
	.componentSetVisibleListener = NULL,
	.componentSize = NULL,
	.containerAdd = NULL,
	.containerRemove = NULL,
	.containerSetBounds = NULL,
	.hardwareGraphics = NULL,
	.loopExecute = sjme_scritchui_fb_loopExecute,
	.loopExecuteLater = sjme_scritchui_fb_loopExecuteLater,
	.loopExecuteWait = sjme_scritchui_fb_loopExecuteWait,
	.panelEnableFocus = NULL,
	.panelNew = sjme_scritchui_fb_panelNew,
	.screens = NULL,
	.windowContentMinimumSize = NULL,
	.windowNew = NULL,
	.windowSetCloseListener = NULL,
	.windowSetVisible = NULL,
};

sjme_errorCode sjme_scritchui_fb_apiInit(
	sjme_attrInNotNull sjme_scritchui inState)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Debug. */
	sjme_message("Framebuffer wrapper initialized!");
	
	/* We need not do anything special. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_apiInitBase(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_scritchui* outState,
	sjme_attrInNotNull sjme_scritchui wrappedState,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui state;

	if (outState == NULL || wrappedState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to core call. */
	state = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInit(inPool,
		&state,
		&sjme_scritchui_fbFunctions, loopExecute,
		initFrontEnd, wrappedState)) || state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_fb_biMap(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_commonBase* topLevel,
	sjme_attrInNotNull sjme_scritchui_commonBase* wrapped)
{
	if (inState == NULL || topLevel == NULL || wrapped == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Map to wrapped item. */
	topLevel->handle = wrapped;
	
	/* Then map back to top level item. */
	wrapped->frontEnd.wrapper = topLevel;
	wrapped->frontEnd.data = inState;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
