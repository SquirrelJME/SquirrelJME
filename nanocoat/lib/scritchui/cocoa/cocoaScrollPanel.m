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

@implementation SJMEScrollPanel : NSScrollView
- (id)initWithFrame:(NSRect)frame
{
	return [super initWithFrame:frame];
}

- (BOOL)drawsBackground
{
	/* Do not render the background so that everything is always transparent */
	/* so that any control under this gets drawn is not obscured. */
	return NO;
}

- (BOOL)autohidesScrollers
{
	/* Always make the scrollbar visible. */
	return NO;
}

- (BOOL)hasVerticalScroller
{
	/* Always make the scrollbar visible. */
	return YES;
}

- (BOOL)hasHorizontalScroller
{
	/* Always make the scrollbar visible. */
	return YES;
}

- (BOOL) translatesAutoresizingMaskIntoConstraints
{
	/* Do not auto-resize constraints?? */
	return NO;
}

- (void)drawRect:(NSRect)dirtyRect
{
	/* Debug. */
	sjme_message("NSScrollView.drawRect() [%d, %d, %d, %d]",
		(int)dirtyRect.origin.x, (int)dirtyRect.origin.y,
		(int)dirtyRect.size.width, (int)dirtyRect.size.height);

	/* Make sure the super panel is drawn. */
	[super drawRect:dirtyRect];
}

@end

sjme_errorCode sjme_scritchui_cocoa_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScrollPanel inScrollPanel,
	sjme_attrInNullable sjme_pointer ignored)
{
	SJMEScrollPanel* cocoaScroll;

	if (inState == NULL || inScrollPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup new scroll panel. */
	cocoaScroll = [[SJMEScrollPanel new] initWithFrame:
		NSMakeRect(0, 0, 100, 100)];

	/* Store it. */
	inScrollPanel->component.common.handle[SJME_SUI_COCOA_H_NSVIEW] =
		cocoaScroll;
	cocoaScroll->scritchScroll = inScrollPanel;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
