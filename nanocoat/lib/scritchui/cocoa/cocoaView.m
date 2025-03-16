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

sjme_errorCode sjme_scritchui_cocoa_viewGetView(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrOutNotNull sjme_scritchui_rect* outViewRect)
{
	NSClipView* cocoaClip;
	NSRect rect;

	if (inState == NULL || inComponent == NULL || outViewRect == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Scroll panel. */
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_SCROLL_PANEL)
	{
		/* Obtain rect. */
		cocoaClip = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEWB];
		rect = [cocoaClip documentVisibleRect];

		/* Project coordinates. */
		outViewRect->s.x = rect.origin.x;
		outViewRect->s.y = rect.origin.y;
		outViewRect->d.width = rect.size.width;
		outViewRect->d.height = rect.size.height;
		inState->apiInThread->lafDpiProject(inState, inComponent,
			SJME_JNI_TRUE,
			&outViewRect->s.x, &outViewRect->s.y,
			&outViewRect->d.width, &outViewRect->d.height);
	}

	/* Not implemented! */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_viewSetArea(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewArea,
	sjme_attrInNotNull const sjme_scritchui_dim* inViewPage)
{
	NSClipView* cocoaClip;
	sjme_jint areaW, areaH, pageW, pageH;

	if (inState == NULL || inComponent == NULL ||
		inViewArea == NULL || inViewPage == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Scroll panel. */
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_SCROLL_PANEL)
	{
		/* Obtain rect. */
		cocoaClip = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEWB];

		/* Project coordinates. */
		areaW = inViewArea->width;
		areaH = inViewArea->height;
		pageW = inViewPage->width;
		pageH = inViewPage->height;
		inState->apiInThread->lafDpiProject(inState, inComponent,
			SJME_JNI_FALSE,
			0, 0, &areaW, &areaH);
		inState->apiInThread->lafDpiProject(inState, inComponent,
			SJME_JNI_FALSE,
			0, 0, &pageW, &pageH);

		/* Set new clip. */
		[cocoaClip displayRect:NSMakeRect(0, 0, pageW, pageH)];
	}

	/* Not implemented! */
	else
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
