/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/util.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/coreRaster.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"

sjme_errorCode sjme_scritchpen_corePrim_drawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	sjme_errorCode error;
	sjme_scritchui_line* clipLine;
	sjme_jint ex;
	sjme_jint* rgbScan;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	if (w < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine end coordinate. */
	ex = x + w;
	
	/* Out of bounds? */
	clipLine = &g->state.clipLine;
	if (x >= g->width || x >= clipLine->e.x ||
		y < clipLine->s.y || y >= clipLine->e.y)
		return SJME_ERROR_NONE;
	
	/* Clip into bounds. */
	if (x < clipLine->s.x)
		x = clipLine->s.x;
	if (x < 0)
		x = 0;
	
	/* Clip end into bounds. */
	if (ex > clipLine->e.x)
		ex = clipLine->e.x;
	if (ex > g->width)
		ex = g->width;
	
	/* Outside the clip? */
	w = ex - x;
	if (w <= 0 || y < 0 || y > g->height ||
		y < clipLine->s.y || y > clipLine->e.y)
		return SJME_ERROR_NONE;
	
	/* SRC or SRC_OVER in hardware supported? Use if so... */
	if (!g->state.applyAlpha && g->impl->drawHorizSrc != NULL)
		return g->impl->drawHorizSrc(g, x, y, w);
	else if (g->state.applyAlpha && g->impl->drawHorizSrcOver != NULL)
		return g->impl->drawHorizSrcOver(g, x, y, w);
	
	/* Allocate buffer. */
	rgbScan = sjme_alloca(sizeof(*rgbScan) * w);
	if (rgbScan == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Fill RGB buffer. */
	memset(rgbScan, 0, sizeof(*rgbScan) * w);
	if (sjme_error_is(error = g->util->rgbScanFill(g,
		rgbScan, 0, w,
		g->state.color.argb)))
		return sjme_error_default(error);
	
	/* Put onto image, we do not multiply alpha as the alpha in source */
	/* is already the correct alpha value and this is not an image. */
	return g->util->rgbScanPut(g, x, y,
		rgbScan, w,
		g->state.applyAlpha,
		SJME_JNI_FALSE, 0);
}

/** Clipping above. */
#define SJME_CLIP_ABOVE 1

/** Clipping below. */
#define SJME_CLIP_BELOW 2

/** Clip right. */
#define SJME_CLIP_RIGHT 4

/** Clip left. */
#define SJME_CLIP_LEFT 8

static sjme_errorCode sjme_scritchpen_corePrim_drawPixelThunk(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	/* Use horizontal line drawing. */
	return g->prim.drawHoriz(g, x, y, 1);
}

static sjme_jint lineClipCsOut(sjme_jint x, sjme_jint y,
	sjme_jint csx, sjme_jint csy,
	sjme_jint cex, sjme_jint cey)
{
	sjme_jint rv;
	
	/* Clear mask. */
	rv = 0;
	
	/* Clips above or below? */
	if (y > cey)
		rv |= SJME_CLIP_ABOVE;
	else if (y < csy)
		rv |= SJME_CLIP_BELOW;
	
	/* Clips right or left? */
	if (x > cex)
		rv |= SJME_CLIP_RIGHT;
	else if (x < csx)
		rv |= SJME_CLIP_LEFT;
	
	return rv;
}

static sjme_jboolean lineClip(sjme_jint* x1, sjme_jint* y1,
	sjme_jint* x2, sjme_jint* y2,
	sjme_jint clipsx, sjme_jint clipex,
	sjme_jint clipsy, sjme_jint clipey)
{
	sjme_jint outa, outb, boop, dx, dy;
	
	// Perform Cohen-Sutherland line clipping
	for (;;)
	{
		// Determine points that lie outside the box
		outa = lineClipCsOut((*x1), (*y1),
			clipsx, clipsy, clipex - 1,clipey - 1);
		outb = lineClipCsOut((*x2), (*y2), clipsx,
			clipsy, clipex - 1, clipey - 1);
		
		// Both points are outside the box, do nothing
		if ((outa & outb) != 0)
			return SJME_JNI_FALSE;
		
		// Both points are inside the box, use this line
		if (outa == 0 && outb == 0)
			return SJME_JNI_TRUE;
		
		// Only the second point is outside, swap the points so that the
		// first point is outside and the first is not
		if (outa == 0)
		{
			// Swap X
			boop = (*x1);
			(*x1) = (*x2);
			(*x2) = boop;
			
			// Swap Y
			boop = (*y1);
			(*y1) = (*y2);
			(*y2) = boop;
			
			// Swap clip flags
			boop = outb;
			outb = outa;
			outa = boop;
		}
		
		// The point is clipped
		// Differences of points
		dx = (*x2) - (*x1);
		dy = (*y2) - (*y1);
		
		// Clips above the box
		if ((outa & SJME_CLIP_ABOVE) != 0)
		{
			(*x1) += dx * (clipey - (*y1)) / dy;
			(*y1) = clipey - 1;
		}
	
		// Clips below
		else if ((outa & SJME_CLIP_BELOW) != 0)
		{
			(*x1) += dx * (clipsy - (*y1)) / dy;
			(*y1) = clipsy;
		}
	
		// Clips the right side
		else if ((outa & SJME_CLIP_RIGHT) != 0)
		{
			(*y1) += dy * (clipex - (*x1)) / dx;
			(*x1) = clipex - 1;
		}
	
		// Clips the left side
		else if ((outa & SJME_CLIP_LEFT) != 0)
		{
			(*y1) += dy * (clipsx - (*x1)) / dx;
			(*x1) = clipsx;
		}
	}
	
	/* Should not be hit. */
	return SJME_JNI_FALSE;
}

static void lineNormal(sjme_jint* x1, sjme_jint* y1,
	sjme_jint* x2, sjme_jint* y2)
{
	sjme_jint t;
	
	if (*x2 < *x1)
	{
		t = *x1;
		*x1 = *x2;
		*x2 = t;
		
		t = *y1;
		*y1 = *y2;
		*y2 = t;
	}
}

sjme_errorCode sjme_scritchpen_corePrim_drawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	sjme_errorCode error;
	sjme_jint clipsx, clipex, clipsy, clipey;
	register sjme_jint dx, dy, xp1, yp1, xp2, yp2, n, d, na, np, cp, x, y;
	sjme_scritchui_pencilDrawPixelFunc pixelFunc;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if 0
	/* Debug. */
	sjme_message("line(%d, %d, %d, %d)? alpha=%d src=%p srcOver=%p",
		x1, y1, x2, y2, g->state.applyAlpha,
		g->impl->drawLineSrc,
		g->impl->drawLineSrcOver);
#endif
	
	/* Normalize coordinates. */
	lineNormal(&x1, &y1, &x2, &y2);
	
	/* SRC or SRC_OVER in hardware supported? Use if so... */
	if (!g->state.applyAlpha && g->impl->drawLineSrc != NULL)
		return g->impl->drawLineSrc(g, x1, y1, x2, y2);
	else if (g->state.applyAlpha && g->impl->drawLineSrcOver != NULL)
		return g->impl->drawLineSrcOver(g, x1, y1, x2, y2);
	
	/* Using code I wrote 15 years ago is great! */
	/* Get clip bounds. */
	clipsx = g->state.clipLine.s.x;
	clipex = g->state.clipLine.e.x;
	clipsy = g->state.clipLine.s.y;
	clipey = g->state.clipLine.e.y;
	
	/* Clip line and make sure it is drawn. */
	if (!lineClip(&x1, &y1, &x2, &y2, clipsx, clipex, clipsy, clipey))
		return SJME_ERROR_NONE;
		
	/* Normalize coordinates. */
	lineNormal(&x1, &y1, &x2, &y2);
	
	/* Determine the pixel function to use. */
	if (!g->state.applyAlpha && g->impl->drawPixelSrc != NULL)
		pixelFunc = g->impl->drawPixelSrc;
	else if (g->state.applyAlpha && g->impl->drawPixelSrcOver != NULL)
		pixelFunc = g->impl->drawPixelSrcOver;
	else
		pixelFunc = sjme_scritchpen_corePrim_drawPixelThunk;
	
	/* Copy Position */
	x = x1;
	y = y1;
	
	/* Calculate Draw */
	/* Get Difference */
	dx = x2 - x1;
	dy = y2 - y1;
	
	if (dx < 0)
		dx = -dx;
	if (dy < 0)
		dy = -dy;
	
	/* X Increase/Decrease */
	if (x2 >= x1)
		xp1 = xp2 = 1;
	else
		xp1 = xp2 = -1;
		
	/* Y Increase/Decrease */
	if (y2 >= y1)
		yp1 = yp2 = 1;
	else
		yp1 = yp2 = -1;
	
	/* At least 1 x for every y */
	if (dx >= dy)
	{
		xp1 = 0;
		yp2 = 0;
		d = dx;
		n = dx >> 1;
		na = dy;
		np = dx;
	}
	
	/* At least 1 y for every x */
	else
	{
		xp2 = 0;
		yp1 = 0;
		d = dy;
		n = dy >> 1;
		na = dx;
		np = dy;
	}
		
	/* Now Draw */
	error = SJME_ERROR_NONE;
	for (cp = 0; cp <= np; cp++)
	{
		/* Put Pixel */
		error |= pixelFunc(g, x, y);
		
		n += na;
		
		if (n >= d)
		{
			n -= d;
			x += xp1;
			y += yp1;
		}
		
		x += xp2;
		y += yp2;
	}
	
	/* Success? */
	return error;
}

sjme_errorCode sjme_scritchpen_corePrim_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* SRC or SRC_OVER in hardware supported? Use if so... */
	if (!g->state.applyAlpha && g->impl->drawPixelSrc != NULL)
		return g->impl->drawPixelSrc(g, x, y);
	else if (g->state.applyAlpha && g->impl->drawPixelSrcOver != NULL)
		return g->impl->drawPixelSrcOver(g, x, y);
	
	/* Use horizontal line drawing. */
	return g->prim.drawHoriz(g, x, y, 1);
}

sjme_errorCode sjme_scritchpen_core_drawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Use primitive. */
	if (sjme_error_is(error = g->prim.drawHoriz(g, x, y, w)))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_drawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Actually a horizontal line? Use faster drawing. */
	if (y1 == y2)
		return g->api->drawHoriz(g, x1, y1, x2 - x1);
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
		
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x1, &y1);
	sjme_scritchpen_coreUtil_applyTranslate(g, &x2, &y2);
	
	/* Use primitive. */
	if (sjme_error_is(error = g->prim.drawLine(g, x1, y1, x2, y2)))
		goto fail_any;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Use primitive. */
	if (sjme_error_is(error = g->prim.drawPixel(g, x, y)))
		goto fail_any;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}
