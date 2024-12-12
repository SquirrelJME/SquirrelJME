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

static sjme_errorCode sjme_scritchui_cocoa_pencilDrawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
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

	/* Draw line. */
	[context DPSmoveto:x:y];
	[context DPSlineto:x + w:y];
	[context DPSstroke];

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

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view and context. */
	inState = g->common.state;
	nsView = g->frontEnd.wrapper;
	context = g->frontEnd.data;

	if (inState == NULL || nsView == NULL || nsView == nil || context == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Draw line. */
	[context DPSmoveto:x1:y1];
	[context DPSlineto:x2:y2];
	[context DPSstroke];

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
	CGFloat rr, gg, bb, aa;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover view and context. */
	inState = g->common.state;
	nsView = g->frontEnd.wrapper;
	context = g->frontEnd.data;

	if (inState == NULL || nsView == NULL || nsView == nil || context == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Convert and set color. */
	aa = ((argb >> 24) & 0xFF) / 255.0;
	rr = ((argb >> 16) & 0xFF) / 255.0;
	gg = ((argb >> 8) & 0xFF) / 255.0;
	bb = ((argb) & 0xFF) / 255.0;
	[context DPSsetalpha:aa];
	[context DPSsetrgbcolor:rr:gg:bb];

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

	/* Set new clip. */
	[context DPSinitclip];
	[context DPSrectclip:x:y:w:h];

	/* Success? */
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

	/* Only thin lines with no cap or joiners. */
	[context DPSsetlinewidth:1.0];
	[context DPSsetlinecap:1];
	[context DPSsetlinejoin:1];

	/* Set the new style. */
#if 0
	if (style == SJME_SCRITCHUI_PENCIL_STROKE_DOTTED)
		[context];
	else
		[context];
#endif

	/* Handled by line drawing function. */
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

