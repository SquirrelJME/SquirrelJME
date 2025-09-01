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
	sjme_frontEndBindable frontEnd;
	sjme_scritchui_pencilFont defaultFont;
	sjme_scritchui_uiPaintable paintable;
	NSGraphicsContext* context;
	NSAffineTransform* matrix;

	/* Recover the panel. */
	inPanel = self->scritchPanel;
	inState = inPanel->component.common.state;

	/* Recover graphics context. */
	context = [NSGraphicsContext currentContext];

	/* Is this paintable? */
	paintable = NULL;
	if (sjme_error_is(error = inState->intern->getPaintable(inState,
		SJME_SUI_CAST_COMPONENT(inPanel),
		&paintable)) || paintable == NULL)
		goto skip_nothing;

	/* Get listener info, ignore if there is none. */
	infoCore = &SJME_SCRITCHUI_LISTENER_CORE(paintable, paint);
	if (infoCore->callback == NULL)
	{
		error = SJME_ERROR_NO_LISTENER;
		goto skip_nothing;
	}

	sjme_message("RealPaint? %p (%p) %p %p", inPanel,
		inPanel->component.common.frontEnd.base.wrapper,
		inPanel->paint.listeners[0].paint.callback,
		inPanel->paint.listeners[1].paint.callback);

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

	/* Save current state to restore for the super call. */
	[context saveGraphicsState];

	/* Setup frontend info. */
	memset(&frontEnd, 0, sizeof(frontEnd));
	frontEnd.base.wrapper = self;
	frontEnd.base.data = (sjme_frontEndData)context;

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
#if SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 0, 0)
		SJME_GFX_PIXEL_FORMAT_INT_BGR888,
#else
		SJME_GFX_PIXEL_FORMAT_INT_RGB888,
#endif
		0, 0, w, h, w,
		defaultFont, &frontEnd)))
		goto fail_initPencil;

	/* Disable antialiasing, it looks horrible. */
	[context setShouldAntialias:NO];

	/* Initialize blank matrix. */
	matrix = [[NSAffineTransform alloc] init];

	/* It appears that GNUstep when rendering is relative to the window */
	/* irrespective as to where the actual panel is. This means that even */
	/* if this panel happens to be framed, the frame origin will still be */
	/* based on the window coordinates, which technically is accurate except */
	/* that it does not match the behavior of macOS. */
#if SJME_CONFIG_GNUSTEP_GUI_VERSION_LEAST(0, 0, 0)
	[matrix translateXBy:self.frame.origin.x
		yBy:self.frame.origin.y];
#endif

	/* Scale everything up so it fits and can be seen. */
	vw = 512;
	vh = 512;
	if (sjme_error_is(error = inState->apiInThread->lafDpiProject(
		inState, SJME_SUI_CAST_COMPONENT(inPanel),
		SJME_JNI_TRUE,
		&vw, &vh, NULL, NULL)))
		goto fail_project;
	[matrix scaleXBy:(512.0 / vw) yBy:(512.0 / vh)];

	/* Then flip the coordinates so they draw top down. */
	/* The panel should still be in the same position due to framing. */
	[matrix scaleXBy:1.0 yBy:-1.0];
	[matrix translateXBy:0.0
		yBy:-inPanel->component.bounds.d.height];

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

	/* Not failed here. */
	return;

	/* Failed? */
fail_noImageRep:
fail_noBuiltInFont:
fail_initPencil:
fail_draw:
fail_project:
	if (sjme_error_is(error))
		sjme_message("Native draw failed: %d", error);
}

/** Generic Cocoa key handling. */
#define SJME_SCRITCHUI_COCOA_KEY(typeFlags) \
	sjme_scritchui inState; \
	sjme_scritchui_uiPanel inPanel; \
	 \
	/* Recover the panel. */ \
	inPanel = self->scritchPanel; \
	inState = inPanel->component.common.state; \
	 \
	/* Call unified handler. */ \
	inState->implIntern->eventKey(inState, \
		SJME_SUI_CAST_COMPONENT(inPanel), \
		event, (typeFlags))

- (void)flagsChanged:(NSEvent*)event
{
	/* TODO: Handle this. */
}

- (id)initWithFrame:(NSRect)frame
{
	return [super initWithFrame:frame];
}

- (BOOL)isFlipped
{
	/* Flipped backed origin be the top-left, which is far easier. */
	/* However, it does not actually work on GNUstep so do not bother. */
	/* Also, framing completely breaks if used as windows cannot be flipped. */
	return NO;
}

- (BOOL)isOpaque
{
	/* Always transparent! */
	return NO;
}

- (void)keyDown:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_KEY(SJME_SCRITCHINPUT_TYPE_KEY_PRESSED);
}

- (void)keyUp:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_KEY(SJME_SCRITCHINPUT_TYPE_KEY_RELEASED);
}

/** Generic Cocoa mouse handling. */
#define SJME_SCRITCHUI_COCOA_MOUSE(typeFlags, button) \
	sjme_scritchui inState; \
	sjme_scritchui_uiPanel inPanel; \
	 \
	/* Recover the panel. */ \
	inPanel = self->scritchPanel; \
	inState = inPanel->component.common.state; \
	 \
	/* Call unified handler. */ \
	inState->implIntern->eventMouse(inState, \
		SJME_SUI_CAST_COMPONENT(inPanel), \
		event, (typeFlags), (button) + 1)

- (void)mouseDown:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED, 0);
}

- (void)mouseDragged:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED |
		SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION, 0);
}

- (void)mouseEntered:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION, -1);
}

- (void)mouseExited:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION, -1);
}

- (void)mouseMoved:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION, -1);
}

- (void)mouseUp:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED,
		0);
}

- (void)otherMouseDown:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED,
		2 + [event buttonNumber]);
}

- (void)otherMouseDragged:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED |
		SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION,
		2 + [event buttonNumber]);
}

- (void)otherMouseUp:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED,
		2 + [event buttonNumber]);
}

- (void)rightMouseDown:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED, 1);
}

- (void)rightMouseDragged:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_PRESSED |
		SJME_SCRITCHINPUT_TYPE_MOUSE_MOTION, 1);
}

- (void)rightMouseUp:(NSEvent*)event
{
	SJME_SCRITCHUI_COCOA_MOUSE(SJME_SCRITCHINPUT_TYPE_MOUSE_BUTTON_RELEASED,
		1);
}

@end

sjme_errorCode sjme_scritchui_cocoa_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus,
	sjme_attrInValue sjme_jboolean defaultFocus)
{
	sjme_errorCode error;
	SJMEPanel* cocoaPanel;

	if (inState == NULL || inPanel == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover panel. */
	cocoaPanel =
		(SJMEPanel*)inPanel->component.common.handle[SJME_SUI_COCOA_H_NSVIEW];

	/* Make sure this is set now as Cocoa needs it early. */
	inPanel->enableFocus = enableFocus;

	/* Grab focus? */
	if (enableFocus && defaultFocus)
		if (sjme_error_is(error = inState->apiInThread->componentFocusGrab(
			inState, SJME_SUI_CAST_COMPONENT(inPanel))))
			return sjme_error_default(error);

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

	/* Link together. */
	inPanel->component.common.handle[SJME_SUI_COCOA_H_NSVIEW] = cocoaPanel;
	cocoaPanel->scritchPanel = inPanel;

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}
