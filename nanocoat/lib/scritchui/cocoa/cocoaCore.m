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

#if defined(SJME_CONFIG_DEBUG)
	#if SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_0)
		#include <Foundation/NSDebug.h>
	#elif SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 0, 0)
		#include <GNUstepBase/NSProcessInfo+GNUstepBase.h>
	#endif
#endif

#if defined(SJME_CONFIG_HAS_MACOS)
extern OSErr CPSGetCurrentProcess(ProcessSerialNumber* psn);

extern OSErr CPSSetProcessName(ProcessSerialNumber* psn, char* name);
#endif

static const sjme_scritchui_implFunctions sjme_scritchui_cocoaFunctions =
{
	.apiInit = sjme_scritchui_cocoa_apiInit,
	.choiceItemInsert = NULL,
	.choiceItemRemove = NULL,
	.choiceItemSetEnabled = NULL,
	.choiceItemSetImage = NULL,
	.choiceItemSetSelected = NULL,
	.choiceItemSetString = NULL,
	.componentFocusGrab = sjme_scritchui_cocoa_componentFocusGrab,
	.componentFocusHas = NULL,
	.componentPosition = NULL,
	.componentRepaint = sjme_scritchui_cocoa_componentRepaint,
	.componentRevalidate = sjme_scritchui_cocoa_componentRevalidate,
	.componentSetActivateListener = NULL,
	.componentSetInputListener = NULL,
	.componentSetPaintListener =
		sjme_scritchui_cocoa_componentSetPaintListener,
	.componentSetSizeListener = NULL,
	.componentSetVisibleListener = NULL,
	.componentSize = sjme_scritchui_cocoa_componentSize,
	.containerAdd = sjme_scritchui_cocoa_containerAdd,
	.containerRemove = sjme_scritchui_cocoa_containerRemove,
	.containerSetBounds = sjme_scritchui_cocoa_containerSetBounds,
	.hardwareGraphics = NULL,
	.labelSetString = sjme_scritchui_cocoa_labelSetString,
	.lafDpiProject = sjme_scritchui_cocoa_lafDpiProject,
	.lafElementColor = NULL,
	.listNew = NULL,
	.loopExecute = NULL,
	.loopExecuteLater = sjme_scritchui_cocoa_loopExecuteLater,
	.loopExecuteWait = NULL,
	.loopIterate = sjme_scritchui_cocoa_loopIterate,
	.menuBarNew = sjme_scritchui_cocoa_menuBarNew,
	.menuInsert = sjme_scritchui_cocoa_menuInsert,
	.menuItemNew = sjme_scritchui_cocoa_menuItemNew,
	.menuNew = sjme_scritchui_cocoa_menuNew,
	.menuRemove = sjme_scritchui_cocoa_menuRemove,
	.panelEnableFocus = sjme_scritchui_cocoa_panelEnableFocus,
	.panelNew = sjme_scritchui_cocoa_panelNew,
	.screens = sjme_scritchui_cocoa_screens,
	.scrollPanelNew = sjme_scritchui_cocoa_scrollPanelNew,
	.viewGetView = sjme_scritchui_cocoa_viewGetView,
	.viewSetArea = sjme_scritchui_cocoa_viewSetArea,
	.viewSetView = NULL,
	.viewSetViewListener = NULL,
	.windowContentMinimumSize = sjme_scritchui_cocoa_windowContentMinimumSize,
	.windowNew = sjme_scritchui_cocoa_windowNew,
	.windowSetCloseListener = NULL,
	.windowSetMenuBar = sjme_scritchui_cocoa_windowSetMenuBar,
	.windowSetVisible = sjme_scritchui_cocoa_windowSetVisible,
};

static const sjme_scritchui_implInternFunctions
	sjme_scritchui_cocoaInternFunctions =
{
	.checkError = sjme_scritchui_cocoa_intern_checkError,
	.containerFraming = sjme_scritchui_cocoa_intern_containerFraming,
	.eventKey = sjme_scritchui_cocoa_intern_eventKey,
	.eventMouse = sjme_scritchui_cocoa_intern_eventMouse,
	.windowExtents = sjme_scritchui_cocoa_intern_windowExtents,
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
	NSApplicationMain(argc, (const char**)argv);

	/* Debug. */
	sjme_message("After Cocoa main loop?");

	/* Success! */
	return SJME_THREAD_RESULT(SJME_ERROR_NONE);
}

sjme_errorCode SJME_SCRITCHUI_DYLIB_SYMBOL_DECLARE(cocoa)(
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
		&sjme_scritchui_cocoaFunctions, loopExecute,
		externals, initFrontEnd)) || state == NULL)
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
	NSThread* currentThread;
	NSRunLoop* currentLoop;
	SJMESuperObject* super;
#if defined(SJME_CONFIG_HAS_MACOS)
	ProcessSerialNumber psn;
#endif

	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Reset main application name. */
#if defined(SJME_CONFIG_HAS_MACOS)
	memset(&psn, 0, sizeof(psn));
	if (CPSGetCurrentProcess(&psn) == noErr)
		CPSSetProcessName(&psn, "SquirrelJME");
#endif

	/* Declare ourselves accordingly. */
	[[NSProcessInfo processInfo] setProcessName:@"SquirrelJME"];

	/* Get the current application. */
	currentApp = NSApp;

	/* Set internal functions. */
	inState->implIntern = &sjme_scritchui_cocoaInternFunctions;

	/* Be as verbose as possible on debug builds */
#if defined(SJME_CONFIG_DEBUG)
#if SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_0)
	NSDebugEnabled = YES;
#elif SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 0, 0)
	[[NSProcessInfo processInfo] setDebugLoggingEnabled:YES];
#endif
#endif

	/* These specific bugs affects Cocoa. */
	inState->bugs.windowVisibilityUnknown = SJME_JNI_TRUE;

	/* If there is no NSApp, then we are running our own stuff and are not */
	/* embedded into another application. */
	if (currentApp == NULL)
	{
		/* Debug. */
		sjme_message("Starting Cocoa event thread...");

		/* Start main Cocoa thread. */
		if (sjme_error_is(error = sjme_thread_new(
			&inState->loopThread,
			&inState->loopThreadId,
			sjme_scritchui_cocoa_loopMain, inState)) ||
			inState->loopThread == SJME_THREAD_NULL)
			return sjme_error_default(error);
	}

	/* Otherwise, we post to this one. */
	else
	{
		/* In this mode we need to manually poll events. */
		inState->bugs.manualEventPoll = SJME_JNI_TRUE;

		/* Indicate that we finished launching, so everything pops up. */
		[currentApp finishLaunching];

		/* Debug. */
		sjme_message("Attempting NSMain integration...");

		/* Refer to this thread. */
		sjme_thread_current(&inState->loopThread);

		/* Setup super object as early as possible for notifications. */
		super = [SJMESuperObject new];
		inState->common.handle[SJME_SUI_COCOA_H_SUPER] = super;

		/* Store for later usage. */
		inState->common.handle[SJME_SUI_COCOA_H_NSAPP] = currentApp;

		/* We know the main thread. */
		currentThread = [NSThread currentThread];
		inState->common.handle[SJME_SUI_COCOA_H_NSTHREAD] = currentThread;

		/* Get the current run loop. */
		currentLoop = [NSRunLoop currentRunLoop];
		inState->common.handle[SJME_SUI_COCOA_H_NSRUNLOOP] = currentLoop;

		/* Need to call thread specific initializer? */
		/* Usually this is for binding a thread to a JavaVM. */
		if (inState->loopThreadInit != NULL)
			inState->loopThreadInit(inState);

		/* Set as ready, since we technically already have the event loop. */
		sjme_atomic_sjme_jint_set(&inState->loopThreadReady, 1);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}
