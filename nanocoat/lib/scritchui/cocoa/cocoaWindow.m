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
#include "blobs/lex.tiff.h"

@implementation SJMEWindow : NSWindow
- (id)init
{
	NSRect rect;

	/* We do not care how big the window initially is. */
	rect.origin.x = 0;
	rect.origin.y = 0;
	rect.size.width = 32;
	rect.size.height = 32;

	/* Setup new window. */
	return [super initWithContentRect:rect
		styleMask:NSTitledWindowMask|NSMiniaturizableWindowMask|
			NSClosableWindowMask|NSResizableWindowMask
		backing:NSBackingStoreBuffered
		defer:NO];
}

- (BOOL)sjmeExecMenuItem:(id)what sjme_attrUsed
{
	SJMEMenuItem* cocoaMenuItem;
	sjme_scritchui inState;
	sjme_scritchui_uiWindow inWindow;
	sjme_scritchui_uiMenuItem menuItem;

	/* Recover item. */
	cocoaMenuItem = (SJMEMenuItem*)what;
	if (cocoaMenuItem == NULL)
		return NO;

	/* Recover state and window. */
	inWindow = self->scritchWindow;
	inState = inWindow->component.common.state;

	/* Only items can be activated. */
	if (cocoaMenuItem->scritchMenuKind->common.type ==
		SJME_SCRITCHUI_TYPE_MENU_ITEM)
	{
		if (sjme_error_is(inState->intern->menuItemActivate(
			inState, cocoaMenuItem->scritchMenuKind,
			cocoaMenuItem->scritchMenuKind)))
			return NO;
		return YES;
	}

	/* Not handled. */
	return NO;
}

- (void)windowDidResize:(NSNotification*)notif
{
	sjme_scritchui inState;
	sjme_scritchui_uiWindow inWindow;
	SJMEWindow* cocoaWindow;

	/* Recover. */
	inWindow = self->scritchWindow;
	inState = inWindow->component.common.state;

	/* Do nothing if not yet visible. */
	cocoaWindow = inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];
	if (![cocoaWindow isVisible])
		return;

	/* Perform recursive container update. */
	if (sjme_error_is(inState->implIntern->containerFraming(
		inState, SJME_SUI_CAST_COMPONENT(inWindow))))
		return;

	/* We need to update. */
	[self update];
}

- (BOOL)windowShouldClose:(id)sender
{
	sjme_errorCode error;
	sjme_scritchui_uiWindow window;
	sjme_scritchui_listener_close* infoCore;

	/* Recover window. */
	window = self->scritchWindow;

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

	/* Make it actually have a size, otherwise it is very tiny. */
	if (inWindow->component.state.isVisible ||
		inWindow->component.state.isUserVisible)
		[cocoaWindow setContentSize:size];

	/* This changed, so it needs to be updated. */
	[cocoaWindow update];

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
	cocoaWindow->scritchWindow = inWindow;

	/* Set icon for the window. */
	[cocoaWindow setMiniwindowImage:
		[[NSImage new] initWithData:[NSData
			dataWithBytesNoCopy:(void*)lex_tiff__bin
			length:lex_tiff__len
			freeWhenDone:NO]]];

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
	NSApplication* cocoaApp;

	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover window and bar, if any. */
	cocoaWindow = inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];
	cocoaMenu = (inMenuBar == NULL ? NULL :
		inMenuBar->menuKind.common.handle[SJME_SUI_COCOA_H_NSMENU]);

	/* Setting a new menu? */
	if (inMenuBar != NULL)
	{
		/* Recover bar. */
		cocoaMenu = inMenuBar->menuKind.common.handle[SJME_SUI_COCOA_H_NSMENU];

		/* This really only has an effect on older versions of macOS and */
		/* GNUstep. */
		[cocoaWindow setMenu:cocoaMenu];
	}

	/* Remove old menu. */
	else
		[cocoaWindow setMenu:nil];

	/* Set this as the global application menu, if visible. */
	cocoaApp = inState->common.handle[SJME_SUI_COCOA_H_NSAPP];
	if (cocoaMenu != NULL && (inWindow->component.state.isVisible ||
		inWindow->component.state.isUserVisible ||
		inWindow->component.state.settingVisible))
	{
		/* Global menu on macOs. */
		[cocoaApp setMainMenu:cocoaMenu];

		/* Set this to be the main menu for the application. */
#if SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 12, 0)
		[cocoaMenu setMain:YES];
#endif
	}
	else
		[cocoaApp setMainMenu:nil];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_windowSetVisible(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInValue sjme_jboolean isVisible)
{
	SJMEWindow* cocoaWindow;
	sjme_errorCode error;
	sjme_scritchui_dim size;
	NSRect scale;

	if (inState == NULL || inWindow == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover window. */
	cocoaWindow = inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Since we initially did not care how big the window is, before we */
	/* show it, we need to set an actual better size. */
	memset(&size, 0, sizeof(size));
	if (sjme_error_is(error = inState->intern->containerMaxSize(
		inState, SJME_SUI_CAST_CONTAINER(inWindow), &size)))
		return inState->implIntern->checkError(inState, error);

	/* Set resultant size. */
	if (size.width > 0 && size.height > 0)
	{
		/* Project correct frame size. */
		if (sjme_error_is(error = inState->apiInThread->lafDpiProject(
			inState, SJME_SUI_CAST_COMPONENT(inWindow),
			SJME_JNI_FALSE,
			NULL, NULL, &size.width, &size.height)))
			return sjme_error_default(error);

		/* Use this scale. */
		scale.origin.x = 0;
		scale.origin.y = 0;
		scale.size.width = size.width;
		scale.size.height = size.height;
		[cocoaWindow
			setFrame:scale
			display:NO
			animate:NO];
	}

	/* Change state accordingly. */
	[cocoaWindow center];
	[cocoaWindow setIsVisible:(isVisible ? true : false)];

	/* If now visible, bring to the front and also set menu. */
	if (isVisible)
	{
		/* Bring this to the front. */
		[cocoaWindow makeKeyAndOrderFront:cocoaWindow];

		/* Update menu, same logic as setting the menu bar. */
		if (sjme_error_is(error = sjme_scritchui_cocoa_windowSetMenuBar(
			inState, inWindow, inWindow->menuBar)))
			return inState->implIntern->checkError(inState,
				sjme_error_default(error));

		/* Frame the window. */
		if (sjme_error_is(error = inState->implIntern->containerFraming(
			inState, SJME_SUI_CAST_CONTAINER(inWindow))))
			return inState->implIntern->checkError(inState,
				sjme_error_default(error));
	}

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
