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
		rect = [cocoaClip documentRect];

		/* Project coordinates. */
		outViewRect->s.x = rect.origin.x;
		outViewRect->s.y = rect.origin.y;
		outViewRect->d.width = abs((sjme_jint)rect.size.width);
		outViewRect->d.height = abs((sjme_jint)rect.size.height);
		inState->apiInThread->lafDpiProject(inState, inComponent,
			SJME_JNI_FALSE,
			&outViewRect->s.x, &outViewRect->s.y,
			&outViewRect->d.width, &outViewRect->d.height);

#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("NSScrollView.getFrameSize(%d, %d)",
			outViewRect->d.width, outViewRect->d.height);
#endif
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
	SJMEScrollPanel* cocoaScroll;
	sjme_jint areaW, areaH;

	if (inState == NULL || inComponent == NULL ||
		inViewArea == NULL || inViewPage == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Scroll panel. */
	if (inComponent->common.type == SJME_SCRITCHUI_TYPE_SCROLL_PANEL)
	{
		/* Obtain rect. */
		cocoaScroll = inComponent->common.handle[SJME_SUI_COCOA_H_NSVIEW];

		/* Project coordinates. */
		areaW = inViewArea->width;
		areaH = inViewArea->height;
		inState->apiInThread->lafDpiProject(inState, inComponent,
			SJME_JNI_TRUE,
			0, 0, &areaW, &areaH);

		/* Set new clip. */
		[cocoaScroll setFrameSize:NSMakeSize(areaW, areaH)];
		[cocoaScroll setNeedsDisplay:YES];

#if defined(SJME_CONFIG_DEBUG)
		/* Debug. */
		sjme_message("NSScrollView.setFrameSize(%d, %d)",
			areaW, areaH);
#endif
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
