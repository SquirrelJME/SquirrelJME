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

static sjme_errorCode sjme_scritchui_gtk2_loopMain(
	sjme_attrInNullable void* anything)
{
	sjme_scritchui state;
	
	if (anything == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Restore state. */
	state = (sjme_scritchui)anything;
	
	/* Initialize, we do not care for the arguments. */
	gtk_init(0, NULL);
	
	/* Run main loop. */
	gtk_main();
	
	/* Success! */
	return SJME_ERROR_NONE;
}

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
	sjme_errorCode error;
	sjme_scritchui state;

	if (outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward to core call. */
	state = NULL;
	if (sjme_error_is(error = sjme_scritchui_core_apiInit(inPool,
		&sjme_scritchUI_gtkFunctions,
		&state)) || state == NULL)
		return sjme_error_default(error);
	
	/* Start main GTK thread. */
	if (sjme_error_is(error = sjme_thread_new(&state->loopThread,
		sjme_scritchui_gtk2_loopMain, state)) ||
		state->loopThread == SJME_THREAD_NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
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
