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
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"

static sjme_errorCode sjme_scritchui_cocoa_pencilInitLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull NSBezierPath* path)
{
	if (g == NULL || path == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	CGFloat dots[2];

	/* Set default properties for the line. */
	[path setLineWidth:1.0];
	[path setLineCapStyle:NSSquareLineCapStyle];
	[path setLineJoinStyle:NSMiterLineJoinStyle];

	/* There is no global setting of dotted lines, so each line needs */
	/* to manually get this style specified. */
	if (g->state.stroke == SJME_SCRITCHUI_PENCIL_STROKE_DOTTED)
	{
		/* Setup dot pattern. */
		dots[0] = 1.0;
		dots[1] = 1.0;

		/* Set stroke. */
		[path setLineDash:dots count:2 phase:0.0];
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_cocoa_pencilStep(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull NSGraphicsContext* context,
	sjme_attrInValue sjme_jboolean in)
{
	sjme_scritchui_rect* clip;

	if (g == NULL || context == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* If going in, store state and set the clip. */
	if (in)
	{
		/* Saves the state so we can remove the clipping. */
		[context saveGraphicsState];

		/* Set new clip. */
		clip = &g->state.clip;
		NSRectClip(NSMakeRect(clip->s.x, clip->s.y,
			clip->d.width, clip->d.height));
	}

	/* Otherwise, restore the old state. */
	else
	{
		/* This removes the old clipping. */
		[context restoreGraphicsState];
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_cocoa_pencilDrawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	sjme_scritchui inState;
	NSView* nsView;
	NSGraphicsContext* context;
	NSBezierPath* path;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view and context. */
	inState = g->common.state;
	nsView = g->frontEnd.wrapper;
	context = g->frontEnd.data;

	if (inState == NULL || nsView == NULL || nsView == nil || context == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Initialize path. */
	path = [NSBezierPath bezierPath];
	sjme_scritchui_cocoa_pencilInitLine(g, path);

	/* Draw line. */
	[path moveToPoint:NSMakePoint(x, y)];
	[path lineToPoint:NSMakePoint(x + w, y)];
	[path stroke];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_cocoa_pencilDrawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	sjme_scritchui inState;
	NSView* nsView;
	NSGraphicsContext* context;
	NSBezierPath* path;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view and context. */
	inState = g->common.state;
	nsView = g->frontEnd.wrapper;
	context = g->frontEnd.data;

	if (inState == NULL || nsView == NULL || nsView == nil || context == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Initialize path. */
	path = [NSBezierPath bezierPath];
	sjme_scritchui_cocoa_pencilInitLine(g, path);

	/* Draw line. */
	[path moveToPoint:NSMakePoint(x1, y1)];
	[path lineToPoint:NSMakePoint(x2, y2)];
	[path stroke];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_cocoa_pencilRawScanGet(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) sjme_pointer outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_scritchui inState;
	NSView* nsView;
	NSBitmapImageRep* imageRep;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view and image representation. */
	inState = g->common.state;
	nsView = g->frontEnd.wrapper;
	imageRep = g->frontEnd.data;
	if (inState == NULL || nsView == NULL || nsView == nil ||
		imageRep == NULL || imageRep == nil)
		return SJME_ERROR_ILLEGAL_STATE;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_scritchui_cocoa_pencilRawScanPutPure(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrInNotNullBuf(inLen) sjme_cpointer srcRaw,
	sjme_attrInPositiveNonZero sjme_jint srcRawLen,
	sjme_attrInPositiveNonZero sjme_jint srcNumPixels)
{
	sjme_scritchui inState;
	NSView* nsView;
	NSBitmapImageRep* imageRep;

	NSPoint a, b;
	NSBezierPath* path;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view and image representation. */
	inState = g->common.state;
	nsView = g->frontEnd.wrapper;
	imageRep = g->frontEnd.data;
	if (inState == NULL || nsView == NULL || nsView == nil ||
		imageRep == NULL || imageRep == nil)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Setup line details, then draw. */
	path = [NSBezierPath bezierPath];
	a.x = x;
	a.y = y;
	[path moveToPoint:a];
	b.x = x;
	b.y = y + srcNumPixels;
	[path lineToPoint:b];
	[path setLineWidth:1.0];
	/*[path setLineDash:1.0:1.0];*/
	[path stroke];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_cocoa_pencilSetAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb)
{
	sjme_scritchui inState;
	NSView* nsView;
	NSGraphicsContext* context;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view and context. */
	inState = g->common.state;
	nsView = g->frontEnd.wrapper;
	context = g->frontEnd.data;

	if (inState == NULL || nsView == NULL || nsView == nil || context == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Extract color components and set. */
	[[NSColor colorWithDeviceRed:(((argb >> 16) & 0xFF) / 255.0)
		green:(((argb >> 8) & 0xFF) / 255.0)
		blue:(((argb) & 0xFF) / 255.0)
		alpha:(((argb) & 0xFF) / 255.0)] set];

	/* Success? */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_cocoa_pencilSetClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_scritchui inState;
	NSView* nsView;
	NSGraphicsContext* context;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view and context. */
	inState = g->common.state;
	nsView = g->frontEnd.wrapper;
	context = g->frontEnd.data;

	if (inState == NULL || nsView == NULL || nsView == nil || context == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Does nothing, the clipping is set for each actual drawing operation. */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

static sjme_errorCode sjme_scritchui_cocoa_pencilSetStrokeStyle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		sjme_scritchui_pencilStrokeMode style)
{
	sjme_scritchui inState;
	NSView* nsView;
	NSGraphicsContext* context;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view and context. */
	inState = g->common.state;
	nsView = g->frontEnd.wrapper;
	context = g->frontEnd.data;

	if (inState == NULL || nsView == NULL || nsView == nil || context == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Cocoa has a default style but anything can change it, so do nothing. */
	return inState->implIntern->checkError(inState, SJME_ERROR_NONE);
}

const sjme_scritchui_pencilImplFunctions sjme_scritchui_cocoa_pencilFunctions =
{
	.copyArea = NULL,
	.drawHorizSrc = sjme_scritchui_cocoa_pencilDrawHoriz,
	.drawHorizSrcOver = NULL,
	.drawLineSrc = sjme_scritchui_cocoa_pencilDrawLine,
	.drawLineSrcOver = NULL,
	.drawPixelSrc = NULL,
	.drawPixelSrcOver = NULL,
	.rawScanGet = sjme_scritchui_cocoa_pencilRawScanGet,
	.rawScanPutPure = sjme_scritchui_cocoa_pencilRawScanPutPure,
	.setAlphaColor = sjme_scritchui_cocoa_pencilSetAlphaColor,
	.setBlendingMode = NULL,
	.setClip = sjme_scritchui_cocoa_pencilSetClip,
	.setStrokeStyle = sjme_scritchui_cocoa_pencilSetStrokeStyle,
};

