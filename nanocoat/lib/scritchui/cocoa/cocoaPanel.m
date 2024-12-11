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

@implementation SJMEPanel : NSView
- (BOOL)acceptsFirstResponder
{
	/* Set if focus is enabled. */
	return (inPanel->enableFocus ? YES : NO);
}

- (BOOL)canBecomeKeyView
{
	/* Set if focus is enabled. */
	return (inPanel->enableFocus ? YES : NO);
}

- (BOOL)needsPanelToBecomeKey
{
	/* Set if focus is enabled. */
	return (inPanel->enableFocus ? YES : NO);
}

- (void)drawRect:(NSRect)dirtyRect
{
	NSRect dirtyBase, frameBase, imageRect;
	NSBitmapImageRep* imageRep;
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiPanel inPanel;
	sjme_scritchui_listener_paint* infoCore;
	sjme_scritchui_pencil pencil;
	sjme_jint x, y, w, h;
	sjme_frontEnd frontEnd;
	sjme_scritchui_pencilFont defaultFont;
	sjme_gfx_pixelFormat pixelFormat;

	/* Recover the panel. */
	inPanel = self->inPanel;
	inState = inPanel->component.common.state;

	/* Get listener info, ignore if there is none. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(&inPanel->paint, paint);
	if (infoCore->callback == NULL)
	{
		/* Debug. */
		sjme_message("Not drawing anything...");

		return;
	}

	/* The dirty rect is in PDF space, it needs to be converted. */
	dirtyBase = [self convertRectToBase:dirtyRect];
	frameBase = self.frame;

	/* Determine actual origin coordinates and view size. */
	x = dirtyBase.origin.x;
	y = dirtyBase.origin.y;
	w = dirtyBase.size.width;
	h = dirtyBase.size.height;

	/* Debug. */
	sjme_message("Cocoa draw (%d, %d) [%d, %d] (frame [%d, %d]",
		x, y, w, h,
		(int)frameBase.size.width, (int)frameBase.size.height);

	/* Save graphics state to restore it for later. */
	[NSGraphicsContext saveGraphicsState];

	/* Setup image representation. */
	imageRep = [self bitmapImageRepForCachingDisplayInRect:dirtyBase];
	if (imageRep == nil || imageRep == NULL)
		goto fail_noImageRep;

	/* Which pixel format is used? */
	switch (imageRep.bitsPerPixel)
	{
		case 32:
			pixelFormat = SJME_GFX_PIXEL_FORMAT_INT_RGB888;
			break;

		case 24:
			pixelFormat = SJME_GFX_PIXEL_FORMAT_BYTE3_RGB888;
			break;

		case 16:
			pixelFormat = SJME_GFX_PIXEL_FORMAT_SHORT_RGB565;
			break;

		case 8:
		default:
			pixelFormat = SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256;
			break;
	}

	/* Setup frontend info. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	frontEnd.wrapper = self;
	frontEnd.data = (sjme_frontEndData)1;/*imageRep;*/

	/* A default font is required. */
	defaultFont = NULL;
	if (sjme_error_is(error = inState->intern->fontBuiltin(inState,
		&defaultFont)) || defaultFont == NULL)
		goto fail_noBuiltInFont;

	/* Setup pencil for drawing. */
	pencil = &inPanel->paint.pencil;
	memset(pencil, 0, sizeof(*pencil));
	if (sjme_error_is(error = sjme_scritchpen_initStatic(
		pencil,
		inState,
		&sjme_scritchui_cocoa_pencilFunctions,
		NULL, NULL,
		pixelFormat,
		0, 0, w, h, w,
		defaultFont, &frontEnd)))
		goto fail_initPencil;

	/* The clipping area is set to the region that needs redrawing. */
	pencil->api->setClip(pencil,
		dirtyBase.origin.x, dirtyBase.origin.y,
		w, h);

	/* Forward to callback. */
	error = infoCore->callback(inState,
		(sjme_scritchui_uiComponent)inPanel,
		pencil,
		w, h, 0);

	/* Reset state. */
	pencil->api->setDefaults(pencil);

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_draw;

	/* Restore the previous state. */
	[NSGraphicsContext restoreGraphicsState];

	/* Make sure main drawing is performed. */
	[super drawRect:dirtyRect];

	/* Success! */
	return;

fail_noImageRep:
fail_noBuiltInFont:
fail_initPencil:
fail_draw:
	/* Restore previous state before failing. */
	[NSGraphicsContext restoreGraphicsState];

	/* Debug. */
	sjme_message("Native draw failed: %d", error);
}

- (id)initWithFrame:(NSRect)frame
{
	return [super initWithFrame:frame];
}

- (BOOL)isFlipped
{
	/* Flipped backed origin be the top-left, which is far easier. */
	return YES;
}

- (BOOL)isOpaque
{
	/* Always transparent! */
	return NO;
}

@end

sjme_errorCode sjme_scritchui_cocoa_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus)
{
	SJMEPanel* cocoaPanel;

	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover panel. */
	cocoaPanel =
		(SJMEPanel*)inPanel->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

sjme_errorCode sjme_scritchui_cocoa_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInNullable sjme_pointer ignored)
{
	SJMEPanel* cocoaPanel;

	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Setup new panel. */
	cocoaPanel = [SJMEPanel new];

	/* Store it. */
	inPanel->component.common.handle[SJME_SUI_COCOA_H_NSVIEW] = cocoaPanel;
	cocoaPanel->inPanel = inPanel;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
