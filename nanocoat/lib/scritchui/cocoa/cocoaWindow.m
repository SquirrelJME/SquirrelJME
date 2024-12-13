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

@implementation SJMEWindow : NSWindow
- (id)init
{
	NSRect rect;

	/* We do not care how big the window initially is. */
	rect.origin.x = 0;
	rect.origin.y = 0;
	rect.size.width = 1;
	rect.size.height = 1;

	/* Setup new window. */
	return [super initWithContentRect:rect
		styleMask:NSTitledWindowMask|NSMiniaturizableWindowMask|
			NSClosableWindowMask|NSResizableWindowMask
		backing:NSBackingStoreBuffered
		defer:NO];
}

- (BOOL)windowShouldClose:(id)sender
{
	sjme_errorCode error;
	sjme_scritchui_uiWindow window;
	sjme_scritchui_listener_close* infoCore;

	/* Recover window. */
	window = self->inWindow;

	/* Get listener info, if no callback never close. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(window, close);
	if (infoCore->callback == NULL)
		return NO;

	/* Invoke callback. */
	if (sjme_error_is(error = infoCore->callback(
		window->component.common.state, window)))
	{
		/* Only if cancelled. */
		if (error == SJME_ERROR_CANCEL_WINDOW_CLOSE)
			return NO;
	}

	/* Always close at this point. */
	return YES;
}

@end

sjme_errorCode sjme_scritchui_cocoa_windowContentMinimumSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJMEWindow* cocoaWindow;
	NSSize size;

	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover window. */
	cocoaWindow = inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Set size accordingly. */
	size.width = width;
	size.height = height;
	[cocoaWindow setContentMinSize:size];
	[cocoaWindow setMinSize:size];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_pointer ignored)
{
	SJMEWindow* cocoaWindow;

	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Create new window. */
	cocoaWindow = [SJMEWindow new];

	/* Store it. */
	inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW] = cocoaWindow;
	cocoaWindow->inWindow = inWindow;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_windowSetMenuBar(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNullable sjme_scritchui_uiMenuBar inMenuBar)
{
	SJMEWindow* cocoaWindow;
	SJMEMenu* cocoaMenu;

	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover window. */
	cocoaWindow = inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Setting a menu? */
	if (inMenuBar != NULL)
	{
		/* Recover bar. */
		cocoaMenu = inMenuBar->menuKind.common.handle[SJME_SUI_COCOA_H_NSVIEW];

		/* Set it. */
		[cocoaWindow setMenu:cocoaMenu];
	}

	/* Clear it otherwise. */
	else
	{
		[cocoaWindow setMenu:nil];
	}

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	SJMEWindow* cocoaWindow;

	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover window. */
	cocoaWindow = inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Change state accordingly. */
	[cocoaWindow center];
	[cocoaWindow setIsVisible:(isVisible ? true : false)];

	/* If now visible, bring to the front. */
	if (isVisible)
		[cocoaWindow makeKeyAndOrderFront:cocoaWindow];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
