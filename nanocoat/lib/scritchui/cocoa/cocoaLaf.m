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

sjme_errorCode sjme_scritchui_cocoa_lafDpiProject(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_uiComponent inContext,
	sjme_attrInValue sjme_jboolean toBase,
	sjme_attrInNullable sjme_jint* inOutX,
	sjme_attrInNullable sjme_jint* inOutY,
	sjme_attrInNullable sjme_jint* inOutW,
	sjme_attrInNullable sjme_jint* inOutH)
{
	NSView* view;
	NSWindow* window;
	NSRect rect;

#if (SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_4) && \
	SJME_CONFIG_COCOA_VERSION_BEFORE(MAC_OS_X_VERSION_10_8)) || \
	SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 13, 0)
	double scale;
#endif

	if (inState == NULL || (inOutX == NULL && inOutY == NULL &&
		inOutW == NULL && inOutH == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

#if SJME_CONFIG_COCOA_VERSION_BEFORE(MAC_OS_X_VERSION_10_4) && \
	SJME_CONFIG_GNUSTEP_GUI_VERSION_BEFORE(0, 13, 0)
	/* This does not exist before 10.4 or GNUstep 0.13, so do nothing. */
	return SJME_ERROR_NONE;

#else
	/* Without a context, we cannot perform any translation. */
	if (inContext == NULL)
		return SJME_ERROR_NONE;

	/* Recover view, if there is none then ignore. */
	if (inContext->common.type == SJME_SCRITCHUI_TYPE_WINDOW)
	{
		window = (NSWindow*)inContext->common.handle[SJME_SUI_COCOA_H_NSVIEW];
		view = [window contentView];
	}
	else
	{
		view = inContext->common.handle[SJME_SUI_COCOA_H_NSVIEW];
		window = [view window];
	}

	/* There is no such view? */
	if (view == NULL)
		return SJME_ERROR_NONE;

	/* Perform point conversion. */
	memset(&rect, 0, sizeof(rect));
	if (inOutX != NULL)
		rect.origin.x = *inOutX;
	if (inOutY != NULL)
		rect.origin.y = *inOutY;
	if (inOutW != NULL)
		rect.size.width = *inOutW;
	if (inOutH != NULL)
		rect.size.height = *inOutH;

	/* Load in the scale factor for the window, if any. */
#if (SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_4) && \
	SJME_CONFIG_COCOA_VERSION_BEFORE(MAC_OS_X_VERSION_10_8)) || \
	SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 13, 0)
	scale = 0.0;
	if (window != NULL)
		scale = [window userSpaceScaleFactor];
#endif

#if (SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_4) && \
	SJME_CONFIG_COCOA_VERSION_BEFORE(MAC_OS_X_VERSION_10_5)) || \
	(SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 13, 0) && \
	SJME_CONFIG_GNUSTEP_GUI_VERSION_BEFORE(0, 20, 0))
	if (window == NULL || scale != 1.0)
	{
		if (toBase)
		{
			rect.origin.x *= scale;
			rect.origin.y *= scale;
			rect.size.width *= scale;
			rect.size.height *= scale;
		}
		else
		{
			rect.origin.x /= scale;
			rect.origin.y /= scale;
			rect.size.width /= scale;
			rect.size.height /= scale;
		}
	}
#elif (SJME_CONFIG_COCOA_VERSION_LEAST(MAC_OS_X_VERSION_10_5) && \
	SJME_CONFIG_COCOA_VERSION_BEFORE(MAC_OS_X_VERSION_10_8)) || \
	SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 20, 0)
	if (window == NULL || scale != 1.0)
	{
		if (toBase)
			rect = [view convertRectToBase:rect];
		else
			rect = [view convertRectFromBase:rect];
	}
#else
	if (toBase)
		rect = [view convertRectToBacking:rect];
	else
		rect = [view convertRectFromBacking:rect];
#endif

	/* Return the result of the translation. */
	if (inOutX != NULL)
		*inOutX = rect.origin.x;
	if (inOutY != NULL)
		*inOutY = rect.origin.y;
	if (inOutW != NULL)
		*inOutW = rect.size.width;
	if (inOutH != NULL)
		*inOutH = rect.size.height;

	/* Success! */
	return SJME_ERROR_NONE;
#endif
}
