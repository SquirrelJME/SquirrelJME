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

#if SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 0, 0)
	#include <GNUstepGUI/GSDisplayServer.h>
#endif

NSString* const sjme_scritchui_cocoa_loopExecuteNotif =
	@"sjme_scritchui_cocoa_loopExecuteNotif";

@implementation SJMELoopExecute : NSObject
- (id)init
{
	return [super init];
}

@end

@implementation SJMESuperObject

- (id)init
{
	NSNotificationCenter* notifCenter;
	NSApplication* currentApp;

	/* Get the current application. */
	currentApp = [NSApplication sharedApplication];

	/* We want SquirrelJME to be activated because this is a UI! */
	/* Whatever we are running on, just drop it and set this. */
#if SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_6)
	[currentApp setActivationPolicy:NSApplicationActivationPolicyRegular];
#endif

	/* Set image for the application. */
	[currentApp setApplicationIconImage:
		[[NSImage new] initWithData:[NSData
			dataWithBytesNoCopy:(void*)lex_tiff__bin
			length:lex_tiff__len
			freeWhenDone:NO]]];

	/* The user is permitted to use the menu bar. */
	[NSMenu setMenuBarVisible:YES];

	/* Get the default notification center, to register loop observer. */
	notifCenter = [NSNotificationCenter defaultCenter];
	[notifCenter addObserver:self
		selector:@selector(sjmeLoopExecute:)
		name:sjme_scritchui_cocoa_loopExecuteNotif
		object:nil];

	/* Listen to the needed notifications. */
	[notifCenter addObserver:self
		selector:@selector(windowDidResize:)
		name:NSWindowDidResizeNotification
		object:nil];

	/* Return self. */
	return self;
}

- (void)sjmeLoopExecute:(NSNotification*)notif
{
	SJMELoopExecute* loopExecuteInfo;
	sjme_errorCode error;
	sjme_scritchui inState;
	NSThread* currentThread;
	NSThread* desireThread;

	/* Recover info. */
	loopExecuteInfo = [[notif userInfo] objectForKey:@"loopExecuteInfo"];
	inState = loopExecuteInfo->scritchState;

	/* Can only be on the main thread. */
	currentThread = [NSThread currentThread];
	desireThread = inState->common.handle[SJME_SUI_COCOA_H_NSTHREAD];
	if (currentThread != desireThread)
	{
		/* Debug. */
		NSLog(@"Notification is %@", notif);
		sjme_todo("Notification in wrong thread: %p != %p",
			currentThread, desireThread);
	}

	/* Execute the function. */
	error = SJME_THREAD_RESULT_AS_ERROR(
		loopExecuteInfo->callback(loopExecuteInfo->anything));

	/* Emit notice if it failed. */
	if (sjme_error_is(error))
		sjme_message("Loop execute failed: %d", error);
}

+ (void)postNotification:(NSNotification*)notif
{
    [[NSNotificationCenter defaultCenter] postNotification:notif];
}

- (void)windowDidResize:(NSNotification*)notif
{
	NSWindow* baseWindow;

	/* Determine if this is a window type we care about. */
	baseWindow = notif.object;
	if ([baseWindow class] != [SJMEWindow class])
		return;

	/* Forward to our own handler. */
	[((SJMEWindow*)baseWindow)windowDidResize:notif];
}

@end

sjme_errorCode sjme_scritchui_cocoa_intern_checkError(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_errorCode ifOkay)
{
	sjme_errorCode error;

	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	return ifOkay;
}

sjme_errorCode sjme_scritchui_cocoa_intern_containerFraming(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	sjme_errorCode error;
	sjme_scritchui_uiContainer container, subContainer;
	sjme_list_sjme_scritchui_uiComponent* components;
	sjme_scritchui_uiComponent subComponent;
	NSView* topView;
	NSView* subView;
	sjme_jint i, n, height, x, z, extraX, extraY;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get container. */
	container = NULL;
	if (sjme_error_is(error = inState->intern->getContainer(
		inState, inComponent, &container)) || container == NULL)
		return sjme_error_default(error);

	/* Get components for this container, ignore if there are none. */
	components = container->components;
	if (components == NULL || components->length <= 0)
		return SJME_ERROR_NONE;

	/* If this is a view, get the frame height for this. */
	topView = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];
	height = 0;
	if (topView != NULL)
		height = [topView frame].size.height;

	/* There may be extra spacing for windows or otherwise. */
	extraX = 0;
	extraY = 0;
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
		if (sjme_error_default(error = inState->implIntern->windowExtents(
			inState, SJME_SUI_CAST_WINDOW(inComponent),
			&extraX, &extraY)))
			return sjme_error_default(error);

	/* Set the position of each component, assuming they are valid. */
	for (i = 0, n = components->length; i < n; i++)
	{
		/* Skip missing components. */
		subComponent = components->elements[i];
		if (subComponent == NULL)
			continue;

		/* Get the view of this component, ignore if missing or hidden. */
		subView = subComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];
		if (subView == NULL || [subView isHiddenOrHasHiddenAncestor])
			continue;

		/* Set the origin position for the component. */
		x = subComponent->bounds.s.x + extraX;
#if SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 0, 0)
		z = 0;
		if (subComponent->bounds.d.height >= height)
			z = 0;
		else
#endif
			z = (height - subComponent->bounds.d.height) +
				subComponent->bounds.s.y + (-extraY);
		[subView setFrameOrigin:NSMakePoint(x, z)];
		[subView setNeedsDisplay:YES];

		/* If this is a sub-container, then recursively frame it as well. */
		subContainer = NULL;
		if (!sjme_error_is(error = inState->intern->getContainer(
			inState, inComponent, &subContainer)) &&
			subContainer != NULL)
			if (sjme_error_is(error = inState->implIntern->containerFraming(
				inState, subComponent)))
				return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_cocoa_intern_eventMouse(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull NSEvent* inEvent,
	sjme_attrInValue sjme_jint typeMask,
	sjme_attrInValue sjme_jint buttonId)
{
	sjme_scritchui_listener_input* infoCore;
	sjme_scritchinput_event fill;
	NSPoint cursorPos;

	if (inState == NULL || inComponent == NULL || inEvent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get listener info. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(inComponent, input);

	/* No actual input listener? */
	if (infoCore->callback == NULL)
		return SJME_ERROR_NONE;

	/* Get the global cursor position. */
	cursorPos = [NSEvent mouseLocation];

	/* Fill in event details. */
	memset(&fill, 0, sizeof(fill));
	fill.type = typeMask;
	fill.time.full = [inEvent timestamp];
	fill.data.mouseButton.x = cursorPos.x;
	fill.data.mouseButton.y = cursorPos.y;
	if (buttonId >= 0)
		fill.data.mouseButton.button = buttonId;

	/* Forward handler. */
	return infoCore->callback(inState, inComponent, &fill);
}

sjme_errorCode sjme_scritchui_cocoa_intern_windowExtents(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiWindow inWindow,
	sjme_attrInNotNull sjme_jint* outX,
	sjme_attrInNotNull sjme_jint* outY)
{
	NSView* cocoaView;
#if SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 0, 0)
	sjme_jint extraX, extraY;
	GSDisplayServer* server;
	float sl, sr, st, sb;
#elif SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_0)
	NSWindow* cocoaWindow;
	NSRect frameRect;
	NSRect contentRect;
#endif

	if (inState == NULL || inWindow == NULL || outX == NULL || outY == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view. */
	cocoaView = inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];

#if SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 0, 0)
	/* Recover extents. */
	extraX = 0;
	extraY = 0;
	if ([cocoaView class] == [SJMEWindow class])
	{
		/* Get the window server for this window, if available. */
		server = GSServerForWindow((NSWindow*)cocoaView);
		if (server != NULL && [server handlesWindowDecorations])
		{
			/* Grab style offsets. */
			/* There is no documentation on what the last style parameter */
			/* actually means, however 1 appears to work. */
			sl = 0;
			sr = 0;
			st = 0;
			sb = 0;
			[server styleoffsets:&sl:&sr:&st:&sb:1];

			/* Offset accordingly. */
			extraX = sl;
			extraY = st + sb;
		}

		/* Add extra height needed for the menu. */
		extraY += [[cocoaView menu] menuBarHeight];
	}

	/* Return whatever result. */
	*outX = extraX;
	*outY = extraY;
	return SJME_ERROR_NONE;
#elif SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_0)
	/* If this is a window, then determine the content offsets. */
	if ([cocoaView class] == [SJMEWindow class])
	{
		cocoaWindow = ((NSWindow*)cocoaView);

		/* Map to the content rect, then determine the offsets. */
		frameRect = cocoaWindow.frame;
		contentRect = [cocoaWindow contentRectForFrameRect:frameRect];
		*outX = abs((sjme_jint)(frameRect.origin.x - contentRect.origin.x)) +
			abs((sjme_jint)(frameRect.size.width - contentRect.size.width));
		*outY = abs((sjme_jint)(frameRect.origin.y - contentRect.origin.y)) +
			abs((sjme_jint)(frameRect.size.height - contentRect.size.height));

		sjme_message("Extent: (%d, %d)", *outX, *outY);
	}

	/* Not a window, so it does not get mapped. */
	else
	{
		*outX = 0;
		*outY = 0;
	}

	/* Success! */
	return SJME_ERROR_NONE;
#else
	/* Not needed anywhere else. */
	*outX = 0;
	*outY = 0;

	return SJME_ERROR_NONE;
#endif
}
