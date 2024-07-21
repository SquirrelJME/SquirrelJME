/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/core/core.h"

static const sjme_scritchui_implFunctions sjme_scritchui_motifFunctions =
{
	.apiInit = NULL,
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
	.listNew = NULL,
	.loopExecute = NULL,
	.loopExecuteLater = NULL,
	.loopExecuteWait = NULL,
	.panelEnableFocus = NULL,
	.panelNew = NULL,
	.screens = NULL,
	.windowContentMinimumSize = NULL,
	.windowNew = NULL,
	.windowSetCloseListener = NULL,
	.windowSetTitle = NULL,
	.windowSetVisible = NULL,
};

/**
 * Returns the Motif ScritchUI interface.
 * 
 * @param inPool The allocation pool used.
 * @param loopExecute The loop execution to run after init.
 * @param initFrontEnd Optional initial frontend data.
 * @param outState The newly created state.
 * @return The library interface.
 * @since 2024/07/16 
 */
sjme_errorCode SJME_DYLIB_EXPORT SJME_SCRITCHUI_DYLIB_SYMBOL(motif)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable sjme_frontEnd* initFrontEnd,
	sjme_attrInOutNotNull sjme_scritchui* outState)
{
	sjme_errorCode error;
	sjme_scritchui state;

	if (outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to core call. */
	state = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInit(inPool,
		&state,
		&sjme_scritchui_motifFunctions, loopExecute,
		initFrontEnd)) || state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}
