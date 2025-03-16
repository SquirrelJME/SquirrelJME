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

@end

sjme_errorCode sjme_scritchui_cocoa_scrollPanelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiScrollPanel inScrollPanel,
	sjme_attrInNullable sjme_pointer ignored)
{
	SJMEScrollPanel* cocoaScroll;
	NSClipView* cocoaClip;

	if (inState == NULL || inScrollPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup new scroll panel. */
	cocoaScroll = [SJMEScrollPanel new];

	/* Need a clip view to cut content with. */
	cocoaClip = [[NSClipView new]
		initWithFrame:NSMakeRect(0, 0, 10, 10)];

	/* Store it. */
	inScrollPanel->component.common.handle[SJME_SUI_COCOA_H_NSVIEW] =
		cocoaScroll;
	inScrollPanel->component.common.handle[SJME_SUI_COCOA_H_NSVIEWB] =
		cocoaClip;
	cocoaScroll->scritchScroll = inScrollPanel;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
