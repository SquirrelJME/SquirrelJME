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
	.panelNew = sjme_scritchui_cocoa_panelNew,
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
	NSApplication* currentApp;
	NSThread* currentThread;
	NSRunLoop* currentLoop;
	SJMESuperObject* super;
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

	/* Debug. */
	sjme_message("Before Cocoa NSApplicationMain()...");

	/* Setup new application and store its handle for later. */
	currentApp = [NSApplication sharedApplication];
	inState->common.handle[SJME_SUI_COCOA_H_NSAPP] = currentApp;

	/* Get our current thread as well, as a NSThread. */
	currentThread = [NSThread currentThread];
	inState->common.handle[SJME_SUI_COCOA_H_NSTHREAD] = currentThread;

	/* Debug. */
	sjme_message("Main NSThread is %p", currentThread);

	/* Get the current run loop. */
	currentLoop = [NSRunLoop currentRunLoop];
	inState->common.handle[SJME_SUI_COCOA_H_NSRUNLOOP] = currentLoop;

	/* Setup super object. */
	super = [SJMESuperObject new];
	inState->common.handle[SJME_SUI_COCOA_H_SUPER] = super;

	/* Debug. */
	sjme_message("Created NSApplication %p!", currentApp);

	/* Need to call thread specific initializer? */
	/* Usually this is for binding a thread to a JavaVM. */
	if (inState->loopThreadInit != NULL)
		inState->loopThreadInit(inState);

	/* Because we created this, we are ready now! */
	sjme_atomic_sjme_jint_set(&inState->loopThreadReady, 1);

	/* Debug. */
	sjme_message("Before Cocoa main loop...");

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
	NSThread* mainThread;

	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get the current application. */
	currentApp = NSApp;

	/* Debug. */
	sjme_message("Current NSApp: %p", currentApp);

	/* If there is no NSApp, then we are running our own stuff and are not */
	/* embedded into another application. */
	if (currentApp == NULL)
	{
		/* Debug. */
		sjme_message("Starting Cocoa event thread...");

		/* Start main Cocoa thread. */
		if (sjme_error_is(error = sjme_thread_new(
			&inState->loopThread,
			sjme_scritchui_cocoa_loopMain, inState)) ||
			inState->loopThread == SJME_THREAD_NULL)
			return sjme_error_default(error);
	}

	/* Otherwise, we post to this one. */
	else
	{
		/* Store for later usage. */
		inState->common.handle[SJME_SUI_COCOA_H_NSAPP] = currentApp;

		/* We know the main thread. */
		mainThread = [NSThread mainThread];
		inState->common.handle[SJME_SUI_COCOA_H_NSTHREAD];

		/* But we need to actually get the pthread_t of the main thread */
		/* and then set loop ready from there... */
		sjme_todo("Inject into already existing NSApplication?");

#if 0
		/* Because we are using an existing application, we are ready */
		/* immediately, we just need to get the right thread! */
		inState->loopThread = [NSThread mainThread];
		sjme_atomic_sjme_jint_set(&inState->loopThreadReady, 1);
#endif
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
