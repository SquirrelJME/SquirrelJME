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
	sjme_jint i, n, height, x, z;

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
		x = subComponent->bounds.s.x;
		z = (height - subComponent->bounds.d.height) +
			subComponent->bounds.s.y;
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
