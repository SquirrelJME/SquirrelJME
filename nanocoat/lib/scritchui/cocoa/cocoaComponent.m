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
	sjme_errorCode error;
	sjme_scritchui_uiWindow inWindow;
	SJMEWindow* cocoaWindow;
	NSView* cocoaView;

	if (inState == NULL || inComponent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Locate the window this is in. */
	inWindow = NULL;
	if (sjme_error_is(error = inState->intern->locateInWindow(inState,
		inComponent, &inWindow)))
		return sjme_error_default(error);

	/* If this is not in any window, then we cannot really grab focus. */
	if (inWindow == NULL)
		return inState->implIntern->checkError(inState, SJME_ERROR_NONE);

	/* Recover the view and window. */
	cocoaWindow = inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];
	cocoaView = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Attempt to make the given view the first responder. */
	[cocoaWindow makeFirstResponder:cocoaView];

	/* Success?. */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_componentFocusHas(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_jboolean* outHasFocus)
{
	sjme_errorCode error;
	sjme_scritchui_uiWindow inWindow;
	SJMEWindow* cocoaWindow;
	NSView* cocoaView;

	if (inState == NULL || inComponent == NULL || outHasFocus == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Locate the window this is in. */
	inWindow = NULL;
	if (sjme_error_is(error = inState->intern->locateInWindow(inState,
		inComponent, &inWindow)))
		return sjme_error_default(error);

	/* If this is not in any window, then we cannot really check focus. */
	if (inWindow == NULL)
		return inState->implIntern->checkError(inState, SJME_ERROR_NONE);

	/* Recover the view and window. */
	cocoaWindow = inWindow->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];
	cocoaView = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Does this actually have focus in the window? */
	*outHasFocus = ([[cocoaWindow firstResponder] isEqual:cocoaView]);

	/* Success?. */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
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

sjme_errorCode sjme_scritchui_cocoa_componentSize(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNullable sjme_jint* outWidth,
	sjme_attrOutNullable sjme_jint* outHeight)
{
	sjme_errorCode error;
	NSView* cocoaView;
	NSRect base;
	sjme_jint w, h;

	if (inState == NULL || inComponent == NULL ||
		(outWidth == NULL && outHeight == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	/* What is being adjusted? */
	cocoaView = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Are the frame coordinates in device or PDF space? */
	base = [cocoaView frame];
	w = abs((sjme_jint)base.size.width);
	h = abs((sjme_jint)base.size.height);

	/* Determine the actual component size. */
	if (sjme_error_is(error = inState->apiInThread->lafDpiProject(inState,
		inComponent,
		SJME_JNI_TRUE,
		0, 0, &w, &h)))
		return sjme_error_default(error);

	/* Return the resultant size. */
	if (outWidth != NULL)
		*outWidth = w;
	if (outHeight != NULL)
		*outHeight = h;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
