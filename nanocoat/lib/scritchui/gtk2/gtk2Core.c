/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/gtk2/gtk2Intern.h"
#include "lib/scritchui/core/core.h"
#include "sjme/dylib.h"

/** GTK Function set for Scritch UI. */
static const sjme_scritchui_implFunctions sjme_scritchui_gtkFunctions =
{
	.apiInit = sjme_scritchui_gtk2_apiInit,
	.componentRepaint = sjme_scritchui_gtk2_componentRepaint,
	.componentRevalidate = sjme_scritchui_gtk2_componentRevalidate,
	.componentSetInputListener = sjme_scritchui_gtk2_componentSetInputListener,
	.componentSetPaintListener = sjme_scritchui_gtk2_componentSetPaintListener,
	.componentSetSizeListener = sjme_scritchui_gtk2_componentSetSizeListener,
	.componentSetVisibleListener = NULL,
	.componentSize = sjme_scritchui_gtk2_componentSize,
	.containerAdd = sjme_scritchui_gtk2_containerAdd,
	.containerSetBounds = sjme_scritchui_gtk2_containerSetBounds,
	.loopExecute = NULL,
	.loopExecuteLater = sjme_scritchui_gtk2_loopExecuteLater,
	.loopExecuteWait = NULL,
	.panelEnableFocus = sjme_scritchui_gtk2_panelEnableFocus,
	.panelNew = sjme_scritchui_gtk2_panelNew,
	.screens = sjme_scritchui_gtk2_screens,
	.windowContentMinimumSize = sjme_scritchui_gtk2_windowContentMinimumSize,
	.windowNew = sjme_scritchui_gtk2_windowNew,
	.windowSetCloseListener = sjme_scritchui_gtk2_windowSetCloseListenerFunc,
	.windowSetVisible = sjme_scritchui_gtk2_windowSetVisible,
};

/** Internal implementation functions. */
static const sjme_scritchui_implInternFunctions
	sjme_scritchui_gtkInternFunctions =
{
	.disconnectSignal = sjme_scritchui_gtk2_intern_disconnectSignal,
	.reconnectSignal = sjme_scritchui_gtk2_intern_reconnectSignal,
	.widgetInit = sjme_scritchui_gtk2_intern_widgetInit,
};

static sjme_thread_result sjme_scritchui_gtk2_loopMain(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui state;
	int argc;
	char** argv;
	
	if (anything == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);
	
	/* Restore state. */
	state = (sjme_scritchui)anything;
	
	/* Enable debug options. */
	argc = 5;
	argv = sjme_alloca(argc * sizeof(*argv));
	argv[0] = "squirreljme";
	argv[1] = "--gtk-debug";
	argv[2] = "misc,modules,geometry";
	argv[3] = "--gdk-debug";
	argv[4] = "misc,events,draw,eventloop";
	
	/* Initialize, we do not care for the arguments. */
	gtk_init(&argc, &argv);
	
	/* Need to call thread specific initializer? */
	/* Usually this is for binding a thread to a JavaVM. */
	if (state->loopThreadInit != NULL)
		state->loopThreadInit(state);
	
	/* Debug. */
	sjme_message("GTK Main Loop...");
	
	/* Before we go into the main loop, signal it is ready. */
	sjme_atomic_sjme_jint_set(&state->loopThreadReady, 1);
	
	/* Run main loop. */
	gtk_main();
	
	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

/**
 * Returns the GTK ScritchUI interface.
 * 
 * @param inPool The allocation pool used.
 * @param loopExecute The loop execution to run after init.
 * @param initFrontEnd Optional initial frontend data.
 * @param outState The newly created state.
 * @return The library interface.
 * @since 2024/03/29 
 */
sjme_errorCode SJME_DYLIB_EXPORT SJME_SCRITCHUI_DYLIB_SYMBOL(gtk2)(
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
		&state, &sjme_scritchui_gtkFunctions, loopExecute,
		initFrontEnd)) || state == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_gtk2_apiInit(
	sjme_attrInNotNull sjme_scritchui inState)
{
	sjme_errorCode error;
	
	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Internal functions to use specifically for GTK. */
	inState->implIntern = &sjme_scritchui_gtkInternFunctions;
	
	/* This is a standard desktop. */
	inState->wmType = SJME_SCRITCHUI_WM_TYPE_STANDARD_DESKTOP;
		
	/* Debug. */
	sjme_message("GTK thread setup...");
	
	/* Start main GTK thread. */
	if (sjme_error_is(error = sjme_thread_new(
		&inState->loopThread,
		sjme_scritchui_gtk2_loopMain, inState)) ||
		inState->loopThread == SJME_THREAD_NULL)
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
