/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchui.h"

static const sjme_scritchui_implFunctions sjme_scritchui_gtk3Functions =
{
	.apiInit = NULL,
	.choiceItemInsert = NULL,
	.choiceItemRemove = NULL,
	.choiceItemSetEnabled = NULL,
	.choiceItemSetImage = NULL,
	.choiceItemSetSelected = NULL,
	.choiceItemSetString = NULL,
	.componentFocusGrab = NULL,
	.componentFocusHas = NULL,
	.componentPosition = NULL,
	.componentRepaint = NULL,
	.componentRevalidate = NULL,
	.componentSetActivateListener = NULL,
	.componentSetInputListener = NULL,
	.componentSetPaintListener = NULL,
	.componentSetSizeListener = NULL,
	.componentSetVisibleListener = NULL,
	.componentSize = NULL,
	.containerAdd = NULL,
	.containerRemove = NULL,
	.containerSetBounds = NULL,
	.hardwareGraphics = NULL,
	.labelSetString = NULL,
	.lafDpiProject = NULL,
	.lafElementColor = NULL,
	.listNew = NULL,
	.loopExecute = NULL,
	.loopExecuteLater = NULL,
	.loopExecuteWait = NULL,
	.loopIterate = NULL,
	.menuBarNew = NULL,
	.menuInsert = NULL,
	.menuItemNew = NULL,
	.menuNew = NULL,
	.menuRemove = NULL,
	.panelEnableFocus = NULL,
	.panelNew = NULL,
	.screens = NULL,
	.scrollPanelNew = NULL,
	.viewGetView = NULL,
	.viewSetArea = NULL,
	.viewSetView = NULL,
	.viewSetViewListener = NULL,
	.windowContentMinimumSize = NULL,
	.windowNew = NULL,
	.windowSetCloseListener = NULL,
	.windowSetMenuBar = NULL,
	.windowSetVisible = NULL,
};

sjme_errorCode SJME_DYLIB_EXPORT SJME_SCRITCHUI_DYLIB_SYMBOL(gtk3)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable const sjme_scritchui_externalFunctions* externals,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui state;

	if (outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to core call. */
	state = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInit(inPool,
		&state,
		&sjme_scritchui_gtk3Functions, loopExecute, externals,
		initFrontEnd)) || state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}
