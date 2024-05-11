/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <gdk/gdk.h>

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"

static sjme_errorCode sjme_scritchui_gtk2_pencilCopyArea(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilDrawChars(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilDrawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilDrawXRGB32Region(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull int* data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anch,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilFillRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilFillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb)
{
	GdkGC* gdk;
	GdkColor color;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover context. */
	gdk = (GdkGC*)g->frontEnd.data;
	
	/* Setup color, note that GTK is 16-bit. */
	memset(&color, 0, sizeof(color));
	color.red = (argb >> 16) & 0xFF;
	color.green = (argb >> 8) & 0xFF;
	color.blue = (argb) & 0xFF;
	
	/* Shift up for 16-bit. */
	color.red |= color.red << 8;
	color.green |= color.green << 8;
	color.blue |= color.blue << 8;
	
	/* Set colors. */
	gdk_gc_set_rgb_fg_color(gdk, &color);
	gdk_gc_set_rgb_bg_color(gdk, &color);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetBlendingMode(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		sjme_scritchui_pencilBlendingMode mode)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetDefaultFont(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetFont(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_lpcstr name,
	sjme_attrInValue sjme_jint style,
	sjme_attrInPositiveNonZero sjme_jint pixelSize)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetStrokeStyle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		sjme_scritchui_pencilStrokeMode style)
{
	GdkGC* gdk;
	GdkLineStyle gdkStyle;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover context. */
	gdk = (GdkGC*)g->frontEnd.data;
	
	/* Which style is used? */
	if (style == SJME_SCRITCHUI_PENCIL_STROKE_DOTTED)
		gdkStyle = GDK_LINE_ON_OFF_DASH;
	else
		gdkStyle = GDK_LINE_SOLID;
	
	/* Set style. */
	gdk_gc_set_line_attributes(gdk, 1,
		gdkStyle, GDK_CAP_BUTT,
		GDK_JOIN_MITER);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

const sjme_scritchui_pencilImplFunctions sjme_scritchui_gtk2_pencilFunctions =
{
	.copyArea = sjme_scritchui_gtk2_pencilCopyArea,
	.drawChars = sjme_scritchui_gtk2_pencilDrawChars,
	.drawLine = sjme_scritchui_gtk2_pencilDrawLine,
	.drawXRGB32Region = sjme_scritchui_gtk2_pencilDrawXRGB32Region,
	.fillRect = sjme_scritchui_gtk2_pencilFillRect,
	.fillTriangle = sjme_scritchui_gtk2_pencilFillTriangle,
	.setAlphaColor = sjme_scritchui_gtk2_pencilSetAlphaColor,
	.setBlendingMode = sjme_scritchui_gtk2_pencilSetBlendingMode,
	.setClip = sjme_scritchui_gtk2_pencilSetClip,
	.setDefaultFont = sjme_scritchui_gtk2_pencilSetDefaultFont,
	.setFont = sjme_scritchui_gtk2_pencilSetFont,
	.setStrokeStyle = sjme_scritchui_gtk2_pencilSetStrokeStyle,
};
