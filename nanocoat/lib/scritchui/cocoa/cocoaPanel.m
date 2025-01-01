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
	return (scritchPanel->enableFocus ? YES : NO);
}

- (BOOL)canBecomeKeyView
{
	/* Set if focus is enabled. */
	return (scritchPanel->enableFocus ? YES : NO);
}

- (BOOL)needsPanelToBecomeKey
{
	/* Set if focus is enabled. */
	return (scritchPanel->enableFocus ? YES : NO);
}

- (void)drawRect:(NSRect)dirtyRect
{
	sjme_errorCode error;
	sjme_scritchui inState;
	sjme_scritchui_uiPanel inPanel;
	sjme_scritchui_listener_paint* infoCore;
	sjme_scritchui_pencil pencil;
	sjme_jint x, y, w, h, vw, vh;
	sjme_frontEnd frontEnd;
	sjme_scritchui_pencilFont defaultFont;
	NSGraphicsContext* context;
	NSAffineTransform* matrix;

	/* Recover the panel. */
	inPanel = self->scritchPanel;
	inState = inPanel->component.common.state;

	/* Get listener info, ignore if there is none. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(&inPanel->paint, paint);
	if (infoCore->callback == NULL)
		goto skip_nothing;

	/* The dirty rect is in PDF space, it needs to be converted. */
	/* The super frame needs to be used as well. */
	x = dirtyRect.origin.x;
	y = dirtyRect.origin.y;
	w = dirtyRect.size.width;
	h = dirtyRect.size.height;
	if (sjme_error_is(error = inState->apiInThread->lafDpiProject(
		inState, SJME_SUI_CAST_COMPONENT(inPanel),
		SJME_JNI_TRUE, &x, &y, &w, &h)))
		goto fail_project;

	/* Recover graphics context. */
	context = [NSGraphicsContext currentContext];

	/* Save current state to restore for the super call. */
	[context saveGraphicsState];

	/* Setup frontend info. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	frontEnd.wrapper = self;
	frontEnd.data = (sjme_frontEndData)context;

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
		SJME_GFX_PIXEL_FORMAT_INT_RGB888,
		0, 0, w, h, w,
		defaultFont, &frontEnd)))
		goto fail_initPencil;

	/* Disable antialiasing, it looks horrible. */
	[context setShouldAntialias:NO];

	/* Initialize blank matrix. */
	matrix = [[NSAffineTransform alloc] init];

	/* Scale everything up so it fits better. */
	vw = 512;
	vh = 512;
	if (sjme_error_is(error = inState->apiInThread->lafDpiProject(
		inState, SJME_SUI_CAST_COMPONENT(inPanel),
		SJME_JNI_TRUE,
		&vw, &vh, NULL, NULL)))
		goto fail_project;
	[matrix scaleXBy:(512.0 / vw) yBy:(512.0 / vh)];

	/* Then flip the coordinates so they draw top down. */
	[matrix scaleXBy:1.0 yBy:-1.0];

	/* Since we did flip the coordinates, we need to shift the image */
	[matrix translateXBy:0.0
		yBy:-inPanel->component.bounds.d.height];

	/* Remove any difference from the frame's origin. */
	/* This makes it so when the origin changes, it does not clip the sides. */
	/* This does not need to be done on macOS, however GNUstep is bugged. */
#if SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 0, 0)
	[matrix translateXBy:-self.frame.origin.x
		yBy:-self.frame.origin.y];
#endif

	/* Use the new matrix. */
	[matrix set];

	/* The clipping area is set to the region that needs redrawing. */
	pencil->api->setClip(pencil, x, y, w, h);

	/* Forward to callback. */
	error = infoCore->callback(inState,
		(sjme_scritchui_uiComponent)inPanel,
		pencil,
		w, h, 0);

	/* Reset state. */
	pencil->api->setDefaults(pencil);

	/* Flush graphics. */
	[context flushGraphics];

	/* Restore state. */
	[context restoreGraphicsState];

	/* Make sure main drawing is performed. */
skip_nothing:
	[super drawRect:dirtyRect];

	/* Flush graphics. */
	[context flushGraphics];

	/* Failed? */
fail_noImageRep:
fail_noBuiltInFont:
fail_initPencil:
fail_draw:
fail_project:
	if (sjme_error_is(error))
		sjme_message("Native draw failed: %d", error);
}

- (id)initWithFrame:(NSRect)frame
{
	return [super initWithFrame:frame];
}

- (BOOL)isFlipped
{
	/* Flipped backed origin be the top-left, which is far easier. */
	/* However, it does not actually work on GNUstep so do not bother. */
	return NO;
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
	cocoaPanel->scritchPanel = inPanel;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
