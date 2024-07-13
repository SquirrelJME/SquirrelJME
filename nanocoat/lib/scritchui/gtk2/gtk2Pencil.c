/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/gtk2/gtk2.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"

static sjme_errorCode sjme_scritchui_gtk2_pencilRawScanGet(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint x,
	sjme_attrInPositive sjme_jint y,
	sjme_attrOutNotNullBuf(inLen) void* outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_errorCode error;
	GdkDrawable* drawable;
	GdkPixbuf* pix;
	GdkGC* gc;
	sjme_jint pixelBytes, limit;
	
	if (g == NULL || outData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover context. */
	drawable = (GdkDrawable*)g->frontEnd.wrapper;
	gc = (GdkGC*)g->frontEnd.data;
	
	/* Get region from the server. */
	pix = gdk_pixbuf_new(GDK_COLORSPACE_RGB, FALSE,
		32, inNumPixels, 1);
	gdk_pixbuf_get_from_drawable(pix, drawable, NULL,
		x, y, 0, 0,
		inNumPixels, 1);
		
	/* Determine the number of pixels to be drawn. */
	pixelBytes = -1;
	limit = -1;
	if (sjme_error_is(error = g->util->rawScanBytes(g,
		inNumPixels, inDataLen,
		&pixelBytes, &limit)) ||
		pixelBytes < 0 || limit < 0)
		return sjme_error_default(error);
	
	/* Read from the pixbuf directly. */
	memmove(outData, gdk_pixbuf_get_pixels(pix), limit);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilRawScanPutPure(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint inX,
	sjme_attrInPositive sjme_jint inY,
	sjme_attrInNotNullBuf(inLen) const void* inData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	GdkDrawable* drawable;
	GdkGC* gc;
	
	if (g == NULL || inData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover context. */
	drawable = (GdkDrawable*)g->frontEnd.wrapper;
	gc = (GdkGC*)g->frontEnd.data;
	
	/* Just draw it like if it were an image. */
	gdk_draw_rgb_32_image(drawable, gc,
		inX, inY, inNumPixels, 1,
		GDK_RGB_DITHER_NONE,
		inData, inDataLen);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb)
{
	GdkGC* gc;
	GdkColor color;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover context. */
	gc = (GdkGC*)g->frontEnd.data;
	
	/* Setup color, note that GTK is 16-bit. */
	/* We ignore alpha as that is not supported by GTK. */
	memset(&color, 0, sizeof(color));
	color.red = (argb >> 16) & 0xFF;
	color.green = (argb >> 8) & 0xFF;
	color.blue = (argb) & 0xFF;
	
	/* Shift up for 16-bit. */
	color.red |= color.red << 8;
	color.green |= color.green << 8;
	color.blue |= color.blue << 8;
	
	/* Set colors. */
	gdk_gc_set_rgb_fg_color(gc, &color);
	gdk_gc_set_rgb_bg_color(gc, &color);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	GdkGC* gc;
	GdkRectangle rect;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover context. */
	gc = (GdkGC*)g->frontEnd.data;
	
	/* Fill in clip. */
	memset(&rect, 0, sizeof(rect));
	rect.x = x;
	rect.y = y;
	rect.width = w;
	rect.height = h;
	
	/* Set clipping bounds. */
	gdk_gc_set_clip_rectangle(gc, &rect);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_gtk2_pencilSetStrokeStyle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		sjme_scritchui_pencilStrokeMode style)
{
	GdkGC* gc;
	GdkLineStyle gdkStyle;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover context. */
	gc = (GdkGC*)g->frontEnd.data;
	
	/* Which style is used? */
	if (style == SJME_SCRITCHUI_PENCIL_STROKE_DOTTED)
		gdkStyle = GDK_LINE_ON_OFF_DASH;
	else
		gdkStyle = GDK_LINE_SOLID;
	
	/* Set style. */
	gdk_gc_set_line_attributes(gc, 1,
		gdkStyle, GDK_CAP_BUTT,
		GDK_JOIN_MITER);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

const sjme_scritchui_pencilImplFunctions sjme_scritchui_gtk2_pencilFunctions =
{
	.copyArea = NULL,
	.rawScanGet = sjme_scritchui_gtk2_pencilRawScanGet,
	.rawScanPutPure = sjme_scritchui_gtk2_pencilRawScanPutPure,
	.setAlphaColor = sjme_scritchui_gtk2_pencilSetAlphaColor,
	.setBlendingMode = NULL,
	.setClip = sjme_scritchui_gtk2_pencilSetClip,
	.setStrokeStyle = sjme_scritchui_gtk2_pencilSetStrokeStyle,
};
