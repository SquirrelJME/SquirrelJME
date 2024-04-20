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

/** Core dispatch functions for serial calls. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_serialFunctions =
{
	/* As normal. */
	.apiFlags = NULL,
	.componentSetPaintListener =
		sjme_scritchui_coreSerial_componentSetPaintListener,
	.containerAdd = sjme_scritchui_coreSerial_containerAdd,
	
	/* Loops are unchanged. */
	.loopExecute = sjme_scritchui_core_loopExecute,
	.loopExecuteWait = sjme_scritchui_core_loopExecuteWait,
	.loopIsInThread = sjme_scritchui_core_loopIsInThread,
	.loopIterate = sjme_scritchui_core_loopIterate,
	
	/* As normal. */
	.panelEnableFocus = sjme_scritchui_coreSerial_panelEnableFocus,
	.panelNew = sjme_scritchui_coreSerial_panelNew,
	.screenSetListener = sjme_scritchui_coreSerial_screenSetListener,
	.screens = sjme_scritchui_coreSerial_screens,
	.windowNew = sjme_scritchui_coreSerial_windowNew,
};

/** Core Function set for ScritchUI. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_coreFunctions =
{
	.apiFlags = NULL,
	.componentSetPaintListener = sjme_scritchui_core_componentSetPaintListener,
	.containerAdd = sjme_scritchui_core_containerAdd,
	.loopExecute = sjme_scritchui_core_loopExecute,
	.loopExecuteWait = sjme_scritchui_core_loopExecuteWait,
	.loopIsInThread = sjme_scritchui_core_loopIsInThread,
	.loopIterate = sjme_scritchui_core_loopIterate,
	.panelEnableFocus = sjme_scritchui_core_panelEnableFocus,
	.panelNew = sjme_scritchui_core_panelNew,
	.screenSetListener = sjme_scritchui_core_screenSetListener,
	.screens = sjme_scritchui_core_screens,
	.windowNew = sjme_scritchui_core_windowNew,
};

/** Internal functions for ScritchUI implementations. */
static const sjme_scritchui_internFunctions sjme_scritchUI_coreIntern =
{
	.getContainer = sjme_scritchui_core_intern_getContainer,
	.getPaintable = sjme_scritchui_core_intern_getPaintable,
	.mapScreen = sjme_scritchui_core_intern_mapScreen,
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
	
	/* Copy frontend over, if set. */
	if (initFrontEnd != NULL)
		memmove(&state->common.frontEnd, initFrontEnd,
			sizeof(*initFrontEnd));
	
	/* Perform API specific initialization. */
	error = SJME_ERROR_NOT_IMPLEMENTED;
	if (state->impl->apiInit == NULL ||
		sjme_error_is(error = state->impl->apiInit(state)))
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
