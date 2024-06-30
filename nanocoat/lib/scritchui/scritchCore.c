/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/core/coreSerial.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"

/** Window manager information. */
static const sjme_scritchui_wmInfo sjme_scritchUI_coreWmInfo =
{
	.defaultTitle = "SquirrelJME",
	.xwsClass = "squirreljme",
};

/** Core dispatch functions for serial calls. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_serialFunctions =
{
	/* As normal. */
	.apiFlags = NULL,
	.componentRepaint = sjme_scritchui_coreSerial_componentRepaint,
	.componentRevalidate = sjme_scritchui_coreSerial_componentRevalidate,
	.componentSetInputListener =
		sjme_scritchui_coreSerial_componentSetInputListener,
	.componentSetPaintListener =
		sjme_scritchui_coreSerial_componentSetPaintListener,
	.componentSetSizeListener =
		sjme_scritchui_coreSerial_componentSetSizeListener,
	.componentSetVisibleListener =
		sjme_scritchui_coreSerial_componentSetVisibleListener,
	.componentSize = sjme_scritchui_coreSerial_componentSize,
	.containerAdd = sjme_scritchui_coreSerial_containerAdd,
	.containerSetBounds = sjme_scritchui_coreSerial_containerSetBounds,
	.fontBuiltin = sjme_scritchui_coreSerial_fontBuiltin,
	.fontDerive = sjme_scritchui_coreSerial_fontDerive,
	
	/* Loops are unchanged. */
	.loopExecute = sjme_scritchui_core_loopExecute,
	.loopExecuteLater = sjme_scritchui_core_loopExecuteLater,
	.loopExecuteWait = sjme_scritchui_core_loopExecuteWait,
	.loopIsInThread = sjme_scritchui_core_loopIsInThread,
	.loopIterate = sjme_scritchui_core_loopIterate,
	
	/* As normal. */
	.panelEnableFocus = sjme_scritchui_coreSerial_panelEnableFocus,
	.panelNew = sjme_scritchui_coreSerial_panelNew,
	.screenSetListener = sjme_scritchui_coreSerial_screenSetListener,
	.screens = sjme_scritchui_coreSerial_screens,
	.windowContentMinimumSize =
		sjme_scritchui_coreSerial_windowContentMinimumSize,
	.windowNew = sjme_scritchui_coreSerial_windowNew,
	.windowSetCloseListener = sjme_scritchui_coreSerial_windowSetCloseListener,
	.windowSetVisible = sjme_scritchui_coreSerial_windowSetVisible,
};

/** Core Function set for ScritchUI. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_coreFunctions =
{
	.apiFlags = NULL,
	.componentRepaint = sjme_scritchui_core_componentRepaint,
	.componentRevalidate = sjme_scritchui_core_componentRevalidate,
	.componentSetInputListener = sjme_scritchui_core_componentSetInputListener,
	.componentSetPaintListener = sjme_scritchui_core_componentSetPaintListener,
	.componentSetSizeListener = sjme_scritchui_core_componentSetSizeListener,
	.componentSetVisibleListener =
		sjme_scritchui_core_componentSetVisibleListener,
	.componentSize = sjme_scritchui_core_componentSize,
	.containerAdd = sjme_scritchui_core_containerAdd,
	.containerSetBounds = sjme_scritchui_core_containerSetBounds,
	.fontBuiltin = sjme_scritchui_core_fontBuiltin,
	.fontDerive = sjme_scritchui_core_fontDerive,
	.loopExecute = sjme_scritchui_core_loopExecute,
	.loopExecuteLater = sjme_scritchui_core_loopExecuteLater,
	.loopExecuteWait = sjme_scritchui_core_loopExecuteWait,
	.loopIsInThread = sjme_scritchui_core_loopIsInThread,
	.loopIterate = sjme_scritchui_core_loopIterate,
	.panelEnableFocus = sjme_scritchui_core_panelEnableFocus,
	.panelNew = sjme_scritchui_core_panelNew,
	.screenSetListener = sjme_scritchui_core_screenSetListener,
	.screens = sjme_scritchui_core_screens,
	.windowContentMinimumSize = sjme_scritchui_core_windowContentMinimumSize,
	.windowNew = sjme_scritchui_core_windowNew,
	.windowSetCloseListener = sjme_scritchui_core_windowSetCloseListener,
	.windowSetVisible = sjme_scritchui_core_windowSetVisible,
};

/** Internal functions for ScritchUI implementations. */
static const sjme_scritchui_internFunctions sjme_scritchUI_coreIntern =
{
	.getContainer = sjme_scritchui_core_intern_getContainer,
	.getPaintable = sjme_scritchui_core_intern_getPaintable,
	.initComponent = sjme_scritchui_core_intern_initComponent,
	.mapScreen = sjme_scritchui_core_intern_mapScreen,
	.updateVisibleContainer =
		sjme_scritchui_core_intern_updateVisibleContainer,
	.updateVisibleComponent =
		sjme_scritchui_core_intern_updateVisibleComponent,
	.updateVisibleWindow = sjme_scritchui_core_intern_updateVisibleWindow,
};

sjme_errorCode sjme_scritchui_core_apiInit(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
	sjme_attrInOutNotNull sjme_scritchui* outState)
{
	sjme_errorCode error;
	sjme_scritchui state;
	
	if (inPool == NULL || inImplFunc == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inImplFunc->apiInit == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Allocate state. */
	state = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool, sizeof(*state),
		&state)) || state == NULL)
		goto fail_alloc;
	
	/* Seed state. */
	state->pool = inPool;
	state->api = &sjme_scritchUI_serialFunctions;
	state->apiInThread = &sjme_scritchUI_coreFunctions;
	state->intern = &sjme_scritchUI_coreIntern;
	state->impl = inImplFunc;
	state->wmInfo = &sjme_scritchUI_coreWmInfo;
	state->nanoTime = sjme_nal_default_nanoTime;
	
	/* Copy frontend over, if set. */
	if (initFrontEnd != NULL)
		memmove(&state->common.frontEnd, initFrontEnd,
			sizeof(*initFrontEnd));
	
	/* Perform API specific initialization. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (sjme_error_is(error = state->impl->apiInit(state)))
		goto fail_apiInit;
	
	/* Return resultant state. */
	*outState = state;
	return SJME_ERROR_NONE;

fail_apiInit:
fail_alloc:
	if (state != NULL)
	{
		sjme_alloc_free(state);
		state = NULL;
	}
	
	return sjme_error_default(error);
}
