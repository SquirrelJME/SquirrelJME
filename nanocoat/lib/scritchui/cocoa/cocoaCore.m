/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/cocoa/cocoa.h"
#include "lib/scritchui/cocoa/cocoaIntern.h"

static const sjme_scritchui_implFunctions sjme_scritchui_cocoaFunctions =
{
	.apiInit = sjme_scritchui_cocoa_apiInit,
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
	.lafElementColor = NULL,
	.listNew = NULL,
	.loopExecute = NULL,
	.loopExecuteLater = sjme_scritchui_cocoa_loopExecuteLater,
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

static sjme_thread_result sjme_scritchui_cocoa_loopMain(
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_scritchui inState;
	NSRunLoop* runLoop;
	int argc;
	char** argv;

	/* Recover state. */
	inState = (sjme_scritchui)anything;
	if (inState == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_NULL_ARGUMENTS);

	/* Setup main arguments. */
	argc = 1;
	argv = sjme_alloca(argc * sizeof(*argv));
	if (argv == NULL)
		return SJME_THREAD_RESULT(SJME_ERROR_OUT_OF_MEMORY);

	argv[0] = "squirreljme";

	/* Obtain the Cocoa loop. */
	runLoop = [NSRunLoop currentRunLoop];

	/* Debug. */
	sjme_message("Before Cocoa main loop...");

	/* Run loop, once. */
	[runLoop runMode:NSDefaultRunLoopMode
		beforeDate:[NSDate dateWithTimeIntervalSinceNow:1]];

	/* Signal that we are ready after running once. */
	sjme_atomic_sjme_jint_set(&inState->loopThreadReady,
		1);

	/* Debug. */
	sjme_message("Before Cocoa NSApplicationMain()...");

	/* Run main application. */
	NSApplicationMain(argc, argv);

	/* Debug. */
	sjme_message("After Cocoa main loop?");

	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

sjme_errorCode SJME_DYLIB_EXPORT SJME_SCRITCHUI_DYLIB_SYMBOL(cocoa)(
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
		&sjme_scritchui_cocoaFunctions, loopExecute,
		initFrontEnd)) || state == NULL)
		return sjme_error_default(error);

	/* Success! */
	*outState = state;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_cocoa_apiInit(
	sjme_attrInNotNull sjme_scritchui inState)
{
	sjme_errorCode error;
	NSApplication* currentApp;

	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get the current application. */
	currentApp = NSApp;

	/* Debug. */
	sjme_message("Current NSApp: %p", currentApp);

	/* Debug. */
	sjme_message("Starting Cocoa event thread...");

	/* Start main Cocoa thread. */
	if (sjme_error_is(error = sjme_thread_new(
		&inState->loopThread,
		sjme_scritchui_cocoa_loopMain, inState)) ||
		inState->loopThread == SJME_THREAD_NULL)
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}
