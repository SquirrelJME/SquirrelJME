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
	sjme_sm(.driverName, "motif"),
	sjme_sm(.apiInit, NULL),
	sjme_sm(.choiceItemInsert, NULL),
	sjme_sm(.choiceItemRemove, NULL),
	sjme_sm(.choiceItemSetEnabled, NULL),
	sjme_sm(.choiceItemSetImage, NULL),
	sjme_sm(.choiceItemSetSelected, NULL),
	sjme_sm(.choiceItemSetString, NULL),
	sjme_sm(.componentFocusGrab, NULL),
	sjme_sm(.componentFocusHas, NULL),
	sjme_sm(.componentPosition, NULL),
	sjme_sm(.componentRepaint, NULL),
	sjme_sm(.componentRevalidate, NULL),
	sjme_sm(.componentSetActivateListener, NULL),
	sjme_sm(.componentSetInputListener, NULL),
	sjme_sm(.componentSetPaintListener, NULL),
	sjme_sm(.componentSetSizeListener, NULL),
	sjme_sm(.componentSetVisibleListener, NULL),
	sjme_sm(.componentSize, NULL),
	sjme_sm(.containerAdd, NULL),
	sjme_sm(.containerRemove, NULL),
	sjme_sm(.containerSetBounds, NULL),
	sjme_sm(.fontScanSystem, NULL),
	sjme_sm(.hardwareGraphics, NULL),
	sjme_sm(.labelSetString, NULL),
	sjme_sm(.lafDpiProject, NULL),
	sjme_sm(.lafElementColor, NULL),
	sjme_sm(.lafMetric, NULL),
	sjme_sm(.listNew, NULL),
	sjme_sm(.loopExecute, NULL),
	sjme_sm(.loopExecuteLater, NULL),
	sjme_sm(.loopExecuteWait, NULL),
	sjme_sm(.loopIterate, NULL),
	sjme_sm(.menuBarNew, NULL),
	sjme_sm(.menuInsert, NULL),
	sjme_sm(.menuItemNew, NULL),
	sjme_sm(.menuNew, NULL),
	sjme_sm(.menuRemove, NULL),
	sjme_sm(.panelEnableFocus, NULL),
	sjme_sm(.panelNew, NULL),
	sjme_sm(.screenGetBounds, NULL),
	sjme_sm(.screens, NULL),
	sjme_sm(.scrollPanelNew, NULL),
	sjme_sm(.viewGetView, NULL),
	sjme_sm(.viewSetArea, NULL),
	sjme_sm(.viewSetView, NULL),
	sjme_sm(.viewSetViewListener, NULL),
	sjme_sm(.windowContentMinimumSize, NULL),
	sjme_sm(.windowGetFrame, NULL),
	sjme_sm(.windowNew, NULL),
	sjme_sm(.windowSetCloseListener, NULL),
	sjme_sm(.windowSetFlags, NULL),
	sjme_sm(.windowSetMenuBar, NULL),
	sjme_sm(.windowSetState, NULL),
	sjme_sm(.windowSetVisible, NULL),
};

sjme_errorCode SJME_SCRITCHUI_DYLIB_SYMBOL_DECLARE(motif)(
	sjme_attrInNotNull sjme_alloc_pool inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState,
	sjme_attrInNullable sjme_thread_mainFunc loopExecute,
	sjme_attrInNullable const sjme_scritchui_externalFunctions* externals,
	sjme_attrInNullable sjme_frontEndBindable* initFrontEnd)
{
	sjme_errorCode error;
	sjme_scritchui state;

	if (outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to core call. */
	state = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInit(inPool,
		&state,
		&sjme_scritchui_motifFunctions, loopExecute, externals,
		initFrontEnd)) || state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}
