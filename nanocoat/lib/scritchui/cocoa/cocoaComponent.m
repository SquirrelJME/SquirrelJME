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

sjme_errorCode sjme_scritchui_cocoa_componentFocusGrab(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Does nothing because you cannot forcibly grab focus. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_cocoa_componentRepaint(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInPositiveNonZero sjme_jint width,
	sjme_attrInPositiveNonZero sjme_jint height)
{
	NSView* cocoaView;
	NSRect rect;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view, as everything is a view. */
	cocoaView = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Mark area as dirty. */
	memset(&rect, 0, sizeof(rect));
	rect.origin.x = x;
	rect.origin.y = y;
	rect.size.width = width;
	rect.size.height = height;
	[cocoaView setNeedsDisplayInRect:
		rect];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_componentRevalidate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent)
{
	NSView* cocoaView;
	NSRect rect;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cannot be done on windows. */
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
		return inState->implIntern->checkError(inState, SJME_ERROR_NONE);

	/* Recover view, as everything is a view. */
	cocoaView = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Set that it needs updating. */
	rect.origin.x = 0;
	rect.origin.y = 0;
	rect.size.width = inComponent->bounds.d.width;
	rect.size.height = inComponent->bounds.d.height;
	[cocoaView setNeedsDisplayInRect:rect];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	SJME_SCRITCHUI_SET_LISTENER_ARGS(paint))
{
	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing needs to be done here as drawRect gets called automatically. */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
