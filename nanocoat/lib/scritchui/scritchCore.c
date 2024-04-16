/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"

/** Core Function set for ScritchUI. */
static const sjme_scritchui_apiFunctions sjme_scritchUI_coreFunctions =
{
	.apiFlags = NULL,
	.apiInit = sjme_scritchui_core_apiInit,
	.componentSetPaintListener = sjme_scritchui_core_componentSetPaintListener,
	.loopExecute = sjme_scritchui_core_loopExecute,
	.loopIsInThread = sjme_scritchui_core_loopIsInThread,
	.loopIterate = sjme_scritchui_core_loopIterateFunc,
	.panelEnableFocus = sjme_scritchui_core_panelEnableFocus,
	.panelNew = sjme_scritchui_core_panelNew,
	.screenSetListener = sjme_scritchui_core_screenSetListenerFunc,
	.screens = sjme_scritchui_core_screens,
};

/** Internal functions for ScritchUI implementations. */
static const sjme_scritchui_internFunctions sjme_scritchUI_coreIntern =
{
	.mapScreen = sjme_scritchui_core_intern_mapScreen,
};

sjme_errorCode sjme_scritchui_core_apiFunctions(
	sjme_attrInOutNotNull const sjme_scritchui_apiFunctions** outApi)
{
	if (outApi == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Return the internal API. */
	*outApi = &sjme_scritchUI_coreFunctions;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_apiInit(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull const sjme_scritchui_apiFunctions* inApiFunc,
	sjme_attrInNotNull const sjme_scritchui_implFunctions* inImplFunc,
	sjme_attrInOutNotNull sjme_scritchui* outState)
{
	sjme_errorCode error;
	sjme_scritchui state;
	
	if (inPool == NULL || inApiFunc == NULL || inImplFunc == NULL ||
		outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* API missing? */
	if (inImplFunc->apiInit == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Allocate state. */
	state = NULL;
	if (sjme_error_is(error = sjme_alloc(inPool, sizeof(*state),
		&state)) || state == NULL)
		goto fail_alloc;
	
	/* Seed state. */
	state->pool = inPool;
	state->api = inApiFunc;
	state->intern = &sjme_scritchUI_coreIntern;
	state->impl = inImplFunc;
	
	/* Perform API specific initialization. */
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
