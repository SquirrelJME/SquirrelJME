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
	return [super init];
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

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
