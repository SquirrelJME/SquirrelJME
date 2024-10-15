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

sjme_errorCode sjme_scritchui_cocoa_containerAdd(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiContainer inContainerData,
	sjme_attrInNotNull sjme_scritchui_uiComponent addComponent)
{
	SJMEWindow* cocoaWindow;
	NSView* cocoaView;

	if (inState == NULL || inContainer == NULL || inContainerData == NULL ||
		addComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* What is being added? */
	cocoaView = addComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Adding to a window? */
	if (inContainer->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
	{
		/* Recover the window. */
		cocoaWindow = inContainer->common.handle[SJME_SUI_COCOA_H_NSVIEW];

		/* Use this as the view. */
		[cocoaWindow.contentView addSubview:cocoaView];
		[cocoaView setNeedsDisplay:true];
	}

	else
	{
		sjme_todo("Impl? %d", inContainer->common.type);
		return sjme_error_notImplemented(inContainer->common.type);
	}

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_containerSetBounds(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inContainer,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	SJMEWindow* cocoaWindow;
	NSView* cocoaView;
	NSRect rect;

	if (inState == NULL || inContainer == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* What is being adjusted? */
	cocoaView = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Set bounds for the view itself, only considers size. */
	rect.origin.x = 0;
	rect.origin.y = 0;
	rect.size.width = width;
	rect.size.height = height;
	[cocoaView setBounds:rect];

	/* Then set bounds for the frame. */
	rect.origin.x = x;
	rect.origin.y = y;
	rect.size.width = width;
	rect.size.height = height;
	[cocoaView setFrame:rect];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
