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
	NSApplication* currentApp;
	NSNotificationCenter* notifCenter;
	SJMESuperObject* super;
	NSDictionary* dict;
	SJMELoopExecute* loopExecuteInfo;

	if (inState == NULL || callback == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover application and super object. */
	currentApp =
		(NSApplication*)inState->common.handle[SJME_SUI_COCOA_H_NSAPP];
	super = (SJMESuperObject*)inState->common.handle[SJME_SUI_COCOA_H_SUPER];

	/* Setup dictionary parameters. */
	loopExecuteInfo = [SJMELoopExecute new];
	loopExecuteInfo->inState = inState;
	loopExecuteInfo->callback = callback;
	loopExecuteInfo->anything = anything;
	dict = [NSDictionary dictionaryWithObject:loopExecuteInfo
		forKey:@"loopExecuteInfo"];

	/* Post notification. */
	notifCenter = [NSNotificationCenter defaultCenter];
	[notifCenter
		postNotificationName:sjme_scritchui_cocoa_loopExecuteNotif
		object:super
		userInfo:dict];

	/* Success! */
	return SJME_ERROR_NONE;
}
