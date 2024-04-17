/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/core/core.h"

/** GTK Function set for Scritch UI. */
static const sjme_scritchui_implFunctions sjme_scritchUI_gtkFunctions =
{
	.apiInit = sjme_scritchui_gtk2_apiInit,
	.componentSetPaintListener = sjme_scritchui_gtk2_componentSetPaintListener,
	.loopSoftPoll = NULL,
	.panelEnableFocus = sjme_scritchui_gtk2_panelEnableFocus,
	.panelNew = sjme_scritchui_gtk2_panelNew,
	.screens = sjme_scritchui_gtk2_screens,
	.windowNew = sjme_scritchui_gtk2_windowNew,
};

/**
 * Returns the GTK ScritchUI interface.
 * 
 * @param outState The newly created state.
 * @return The library interface.
 * @since 2024/03/29 
 */
sjme_errorCode SJME_SCRITCHUI_DYLIB_SYMBOL(gtk2)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_scritchui* outState)
{
	if (outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to core call. */
	return sjme_scritchui_core_apiInit(inPool,
	   &sjme_scritchUI_gtkFunctions,
	   outState);
}

sjme_errorCode sjme_scritchui_gtk2_apiInit(
	sjme_attrInNotNull sjme_scritchui inState)
{
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* This is a standard desktop. */
	inState->wmType = SJME_SCRITCHUI_WM_TYPE_STANDARD_DESKTOP;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
