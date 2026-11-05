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

sjme_errorCode sjme_scritchui_cocoa_loopExecuteLater(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_thread_mainFunc callback,
	sjme_attrInNullable sjme_thread_parameter anything)
{
	sjme_errorCode error;
	sjme_jboolean inThread;
	NSApplication* currentApp;
	NSNotificationCenter* notifCenter;
	SJMESuperObject* superObj;
	NSDictionary* dict;
	SJMELoopExecute* loopExecuteInfo;
	NSThread* mainThread;
	NSNotification* notif;
	NSAutoreleasePool* autoPool;

	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover application and super object. */
	currentApp =
		(NSApplication*)inState->common.handle[SJME_SUI_COCOA_H_NSAPP];
	mainThread = (NSThread*)inState->common.handle[SJME_SUI_COCOA_H_NSTHREAD];
	superObj = (SJMESuperObject*)inState->common.handle[SJME_SUI_COCOA_H_SUPER];

	/* Are we in the event loop thread? */
	if (sjme_error_is(error = inState->api->loopIsInThread(inState,
		&inThread)))
		return sjme_error_default(error);

	/* Make sure a pool is setup. */
	autoPool = [NSAutoreleasePool allocWithZone:NSDefaultMallocZone()];

	/* Setup dictionary parameters. */
	loopExecuteInfo = [SJMELoopExecute new];
	loopExecuteInfo->scritchState = inState;
	loopExecuteInfo->callback = callback;
	loopExecuteInfo->anything = anything;

	dict = [NSDictionary dictionaryWithObject:loopExecuteInfo
		forKey:@"loopExecuteInfo"];

	/* Build notification. */
	notif = [NSNotification
		notificationWithName:sjme_scritchui_cocoa_loopExecuteNotif
		object:superObj
		userInfo:dict];

	/* If we are not, we need to post a notification via selector. */
	/* Or if we are not yet ready or fully wrapped yet. */
	if (!inThread || inState->wrappedState == NULL ||
		!sjme_atomic_sjme_jint_get(&inState->loopThreadReady))
	{
#if (SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_2) && \
	SJME_CONFIG_COCOA_VERSION_BEFORE(MAC_OS_X_VERSION_10_5)) || \
    (SJME_CONFIG_GNUSTEP_BASE_VERSION_LEAST(1, 5, 1) && \
	SJME_CONFIG_GNUSTEP_BASE_VERSION_BEFORE(1, 15, 4))
		/* Post notification. */
		[[super class] performSelectorOnMainThread:@selector(postNotification:)
			withObject:notif
			waitUntilDone:NO];

		/* Success? */
		return inState->implIntern->checkError(inState, SJME_ERROR_NONE);

#elif SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_5) || \
    SJME_CONFIG_GNUSTEP_BASE_VERSION_LEAST(1, 15, 4)
		/* Post notification. */
		[[superObj class] performSelector:@selector(postNotification:)
								 onThread:mainThread
							   withObject:notif
							waitUntilDone:NO];

		/* Success? */
		return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
#endif
	}

	/* Otherwise, we can post the notification directly. */
	[[NSNotificationCenter defaultCenter] postNotification:notif];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_loopIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInValue sjme_jboolean blocking,
	sjme_attrOutNullable sjme_jboolean* outHasTerminated)
{
	NSApplication* currentApp;
	NSEvent* event;

	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover the current application. */
	currentApp = inState->common.handle[SJME_SUI_COCOA_H_NSAPP];

	/* Set as finish launching. */
	[currentApp finishLaunching];

	/* Just say that windows needs updating, because let us be real */
	/* here, when does Windows not need updating? */
	[currentApp setWindowsNeedUpdate:YES];

	/* Get the next event. */
	for (;;)
	{
		/* Pop next event. */
		event = [currentApp
#if SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_15)
			nextEventMatchingMask:NSUIntegerMax
#else
			nextEventMatchingMask:NSAnyEventMask
#endif

#if SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_12)
			untilDate:[NSDate now]
#else
			untilDate:[NSDate dateWithTimeIntervalSinceNow:0]
#endif
			inMode:NSDefaultRunLoopMode
			dequeue:YES];

		/* Process event? */
		NSLog(@"Event %@", event);
		if (event != NULL)
			[currentApp sendEvent:event];
		else
			break;
	}

	/* Request that all windows be updated. */
	[currentApp updateWindows];

	/* Success! */
	return SJME_ERROR_NONE;
}
