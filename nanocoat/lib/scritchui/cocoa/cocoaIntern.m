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

@end

@implementation SJMESuperObject

- (void)sjmeLoopExecute:(NSNotification*)notif
{
	SJMELoopExecute* loopExecuteInfo;
	sjme_thread_result result;
	sjme_errorCode error;

	/* Debug. */
	sjme_message("Cocoa loopExecute!");

	/* Recover info. */
	loopExecuteInfo = [[notif userInfo] objectForKey:@"loopExecuteInfo"];

	/* Execute the function. */
	result = loopExecuteInfo->callback(loopExecuteInfo->anything);
	error = SJME_THREAD_RESULT_AS_ERROR(result);

	/* Error? */
	if (sjme_error_is(error))
		NSLog(@"Loop execute %@ failed: %d",
			notif, error);
}

/**
 * Initialization method.
 *
 * @return Always @c self .
 * @since 2024/08/16
 */
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

@end
