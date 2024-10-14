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

	/* Get the default notification center, to register loop observer. */
	notifCenter = [NSNotificationCenter defaultCenter];
	[notifCenter addObserver:self
		selector:@selector(sjmeLoopExecute:)
		name:sjme_scritchui_cocoa_loopExecuteNotif
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
	inState = loopExecuteInfo->inState;

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

+ (void)postNotification:(NSNotification *)notif
{
    [[NSNotificationCenter defaultCenter] postNotification:notif];
}

@end
