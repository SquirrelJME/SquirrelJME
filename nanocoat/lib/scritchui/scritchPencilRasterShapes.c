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

sjme_errorCode sjme_scritchpen_corePrim_drawArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle)
{
	sjme_errorCode error;
	sjme_jint yz, yze;
	float centerX, centerY, radiusX, radiusY, startAngleRad, endAngleRad,
		angle;
	int steps, innerX, innerY, firstFillX, lastFillX, firstFillY, lastFillY;
	sjme_jboolean dot, dotFlip;
	sjme_scritchui_line* clipLine;
	sjme_scritchui_pencilDrawPixelFunc drawPixel;

	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	error = SJME_ERROR_NONE;

	dot = SJME_JNI_TRUE;
	dotFlip = (g->state.stroke == SJME_SCRITCHUI_PENCIL_STROKE_DOTTED);

	drawPixel = g->prim.drawPixel;

	/* Get clipping information. */
	clipLine = &g->state.clipLine;
	
	/* Java's coordinate system has positive angles moving counter-clockwise */
	arcAngle = -arcAngle;
	startAngle = -startAngle;
	

	/**
	 * This works similarly to Bresenham's midpoint circle algorithm. "steps" 
	 * dictates how many iterations are used to draw the circle. A bigger value
	 * will result in the same pixels being hit more times (and wasted cycles 
	 * since they'll be discarded later) but will guarantee a perfectly filled 
	 * outline, whereas a small value will result in gaps appearing in the 
	 * circle since less points will be sampled. The current value is a good
	 * balance between filling all positions on all kinds of shapes while 
	 * hitting as few pixels as possible.
	 */

	centerX = x + w / 2.0f;
	centerY = y + h / 2.0f;
	radiusX = w / 2.0f;
	radiusY = h / 2.0f;
	startAngleRad = startAngle * 0.017453292f;
	endAngleRad = ((startAngle + arcAngle) * 0.017453292f) 
	- startAngleRad;
	steps = fabs(arcAngle * ((w + h) / 2.0f) / 50.0f);
	innerX, innerY;

	firstFillX = lastFillX = firstFillY = lastFillY = -1;
	
	for (int i = 0; i < steps; i++) 
	{
		float angle = startAngleRad + (i * endAngleRad / steps);
		
		int innerX = round((centerX) + radiusX * cos(angle));
		int innerY = round((centerY) + radiusY * sin(angle));
		
		/* Make sure we're not drawing out of bounds. */
		if(innerX < clipLine->s.x || innerX >= clipLine->e.x
			|| innerY < clipLine->s.y || innerY >= clipLine->e.y)
			continue;

		/* We cannot paint the same pixel more than once (breaks alpha) */
		if((lastFillX == innerX && lastFillY == innerY) || 
			(firstFillX == innerX && firstFillY == innerY)) 
			continue;

		if(i == 0) 
		{ 
			firstFillY = innerY; 
			firstFillX = innerX; 
		}
		
		lastFillX = innerX;
		lastFillY = innerY;

		/* If style is DOTTED, rendering will paint and skip pixels 1 by 1. */
		if (dot)
			error |= drawPixel(g, innerX, innerY);
		dot ^= dotFlip;
	}

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Success? */
	return error;
	
fail_any:
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_corePrim_fillArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawHorizFunc drawHoriz;
	sjme_jint yz, yze, steps, innerX, innerY;
	sjme_jboolean hasAlpha = 0;
	float centerX, centerY, radiusX, radiusY, startAngleRad, endAngleRad,
		maxRad, angle;
	sjme_jboolean *filledPixels = NULL;
	sjme_scritchui_line* clipLine;
	sjme_scritchui_pencilDrawPixelFunc drawPixel;


	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	error = SJME_ERROR_NONE;

	/* Java's coordinate system has positive angles moving counter-clockwise */
	arcAngle = -arcAngle;
	startAngle = -startAngle;

	hasAlpha = g->hasAlpha;

	drawPixel = g->prim.drawPixel;

	/* Get clipping information. */
	clipLine = &g->state.clipLine;

	/** 
	 * Only allocate the alpha buffer if the color isn't opaque. Noticeably
	 * improves performance for opaque arcs.
	 *
	 * TODO: This can be optimized to take 1/8 of the usual memory size by
	 * manipulating each bit of the boolean array below, as a _Bool variable
	 * is often at least one whole byte in size.
	 */
	if(hasAlpha)
	{
		filledPixels = (sjme_jboolean *) sjme_alloca((w * h) *
			sizeof(sjme_jboolean));
		if (filledPixels == NULL)
		{
			error = sjme_error_outOfMemory(NULL, (w * h) *
				sizeof(sjme_jboolean));
			goto fail_any;
		}
		memset(filledPixels, 0, (w * h) * sizeof(sjme_jboolean));
	}

	/**
	 * This is just drawArc's Bresenham midpoint circle algorithm modified to 
	 * draw arcs from the center to the edge and check if a pixel was already
	 * painted before. Works great but is slow on larger transparent arcs.
	 * Trying to use a scanline approach could result in better performance due
	 * to the simpler checks (just go from yMax to yMin while making sure a y 
	 * position is never used twice) but doesn't seem to work right in some
	 * edge cases (negative angles, too wide/narrow arc angles, etc) so this
	 * one, albeit slow, is what we'll be going with at the moment.
	 * 
	 * At least it has the upside of generating far more stable arcs that fully
	 * match their outline compared to Java AWT's algorithm.
	 * 
	 * TODO: Optimize this later
	 */ 
	
	centerX = x + w / 2.0f;
	centerY = y + h / 2.0f;
	radiusX = w / 2.0f;
	radiusY = h / 2.0f;
	startAngleRad = startAngle * 0.017453292f;
	endAngleRad = ((startAngle + arcAngle) * 0.017453292f) 
	- startAngleRad;

	maxRad = radiusX > radiusY ? radiusX : radiusY;

	steps = fabs(arcAngle * ((w + h) / 2.0f) / 50.0f);
	innerX, innerY;

	for(int i = 0; i < steps; i++) 
	{
		angle = startAngleRad + (i * endAngleRad / steps);

		for(int j = 0; j < maxRad; j++) 
		{
			innerX = round(centerX + radiusX * cos(angle) * (j / maxRad));
			innerY = round(centerY + radiusY * sin(angle) * (j / maxRad));

			/*
			 * Make sure we're not drawing out of bounds. Or accessing the
			 * alpha buffer at an invalid position with innerX-x or innerY-y
			 */
			if(innerX < clipLine->s.x || innerX >= clipLine->e.x
				|| innerY < clipLine->s.y || innerY >= clipLine->e.y 
				|| innerX-x < 0 || innerY-y < 0)
				continue;

			/** 
			 * Only draw if opaque, or if the alpha buffer is not yet filled
			 * for the current position.
			 */
			if(hasAlpha ? !*(filledPixels + ((innerY-y) * w + innerX-x)) : 1)
			{
				if(hasAlpha)
					*(filledPixels + ((innerY-y) * w + innerX-x)) = 1;
				error |= drawPixel(g, innerX, innerY);
			}
		}
	}

	if(hasAlpha)
		sjme_alloca_free(filledPixels);

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
	
	/* Success? */
	return error;
	
fail_any:
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_corePrim_fillPolygon(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint xPoints[],
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInValue sjme_jint yPoints[],
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawHorizFunc drawHoriz;
	int ymin = 2147483647, ymax = -2147483648, intersectionCount = 0, xStart,
	xEnd, temp, dy, j;
	int intersections[nPoints];
	long ix;
	sjme_scritchui_line* clipLine;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	error = SJME_ERROR_NONE;

	if (!g->state.applyAlpha && g->impl->drawHorizSrc != NULL)
		drawHoriz = g->impl->drawHorizSrc;
	else if (g->state.applyAlpha && g->impl->drawHorizSrcOver != NULL)
		drawHoriz = g->impl->drawHorizSrcOver;
	else
		drawHoriz = g->prim.drawHoriz;

	/* Get clipping information. */
	clipLine = &g->state.clipLine;

	/**
	 * Filling polygons is done through the canonical Scan Line fill algorithm. 
	 * It works just like its description: Find the yMax and yMin of the
	 * polygon, calculate the intersections between each edge, sort 
	 * intersections by increasing X coordinate, then fill from top to bottom.
	 */

	for (int i = 0; i < nPoints; i++) 
	{
		if (yPoints[i+yOffset] < ymin) { ymin = yPoints[i+yOffset]; }
		if (yPoints[i+yOffset] > ymax) { ymax = yPoints[i+yOffset]; }
	}

	/* Clip ymin and ymax to the screen area if any vertex is outside */
	if(ymin + g->state.translateReal.y < clipLine->s.y) 
		ymin = clipLine->s.y - g->state.translateReal.y;
	
	if(ymax + g->state.translateReal.y >= clipLine->e.y) 
		ymax = clipLine->e.y - g->state.translateReal.y;

	for (int y = ymin; y < ymax; y++)
	{
		intersectionCount = 0;
		for (int i = 0; i < nPoints; i++)
		{
			j = (i + 1) % nPoints;
			if ((yPoints[i + yOffset] <= y && yPoints[j + yOffset] > y) ||
				(yPoints[j + yOffset] <= y && yPoints[i + yOffset] > y))
			{
				dy = yPoints[j + yOffset] - yPoints[i + yOffset];
				if (dy != 0)
				{
					ix = (long) xPoints[i + xOffset] * dy + (y - yPoints[i +
							yOffset]) * (long) (xPoints[j + xOffset] -
							xPoints[i + xOffset]);
					ix /= dy;
					intersections[intersectionCount++] = (int) ix;
				}
			}
		}

		for (int i = 0; i < intersectionCount - 1; i++)
		{
			for (int j = 0; j < intersectionCount - 1 - i; j++)
			{
				if (intersections[j] > intersections[j + 1])
				{
					temp = intersections[j];
					intersections[j] = intersections[j + 1];
					intersections[j + 1] = temp;
				}
			}
		}

		for (int i = 0; i < intersectionCount; i += 2)
		{
			if (i + 1 < intersectionCount)
			{
				xStart = fmax(intersections[i], clipLine->s.x);
				xEnd = fmin(intersections[i + 1], clipLine->e.x);
				/* Start > End is an invalid area we can just skip */
				if(xEnd > xStart)
					if (sjme_error_is(error = drawHoriz(g, xStart, y,
						xEnd-xStart)))
						break;
			}
		}
	}

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
	
	/* Success? */
	return error;
	
fail_any:
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_corePrim_fillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFillPolygonFunc fillPolygon;
	sjme_jint xPoints[3];
	sjme_jint yPoints[3];
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	error = SJME_ERROR_NONE;
	
	/* 
	 * A triangle is just a polygon with 3 vertices, so just call fillPolygon()
	 * to draw it like any other in Software mode, for consistency.
	 */
	xPoints[0] = x1;
	xPoints[1] = x2;
	xPoints[2] = x3;
	yPoints[0] = y1;
	yPoints[1] = y2;
	yPoints[2] = y3;

	fillPolygon = g->prim.fillPolygon;
	error |= sjme_scritchpen_core_fillPolygon(g, xPoints, 0, yPoints, 0, 3);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
	
	/* Success? */
	return error;
	
fail_any:
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_corePrim_drawRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_errorCode error;
	sjme_jint xw, yh;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Nothing to draw? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;
	
	/* Pre-calculate coordinates. */
	xw = x + w;
	yh = y + h;
	
	/* Clear error state. */
	error = SJME_ERROR_NONE;
	
	/* Draw horizontal spans first. */
	error |= g->prim.drawHoriz(g, x, y, w);
	error |= g->prim.drawHoriz(g, x, yh, w);
	
	/* Draw vertical spans. */
	error |= g->prim.drawLine(g, x, y, x, yh);
	error |= g->prim.drawLine(g, xw, y, xw, yh);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_corePrim_fillRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawHorizFunc drawHoriz;
	sjme_jint yz, yze;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	drawHoriz = g->prim.drawHoriz;
	for (yz = y, yze = y + h; yz < yze; yz++)
		if (sjme_error_is(error = drawHoriz(g, x, yz, w)))
			break;
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Success? */
	return error;
	
fail_any:
	
	return sjme_error_default(error);
}


sjme_errorCode sjme_scritchpen_core_drawArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawArcFunc drawArc;
	sjme_jint yz, yze;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	drawArc = g->prim.drawArc;
	error |= drawArc(g, x, y, w, h, startAngle, arcAngle);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_drawRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawRectFunc drawRect;
	sjme_jint yz, yze;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Cap width and height to 1 always. */
	if (w <= 0)
		w = 1;
	if (h <= 0)
		h = 1;
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	drawRect = g->prim.drawRect;
	error |= drawRect(g, x, y, w, h);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_drawRoundRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawArcFunc drawArc;
	sjme_scritchui_pencilDrawLineFunc drawLine;
	sjme_jint xw, yh, arcwby2, archby2;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	arcWidth = fabs(arcWidth);
	arcHeight = fabs(arcHeight);

	/**
	 * We'll be doing only even arc widths and heights, otherwise the borders
	 * will look off due to fractional rounding (java's AWT Graphics do allow
	 * for odd width/heights though)
	 */
	if(arcWidth  %2 != 0) { arcWidth++; }
	if(arcHeight %2 != 0) { arcHeight++; }
	
	/* The arcs cannot be larger then the rect's width/height*/
	if(arcWidth >= w) { arcWidth = w-1; }
	if(arcHeight >= h) { arcHeight = h-1; }

	/* Pre-calculate coordinates. */
	xw = x + w;
	yh = y + h;
	arcwby2 = (arcWidth/2);
	archby2 = (arcHeight/2);
	
	error = SJME_ERROR_NONE;
	
	
	/**
	 * Draw horizontal spans first, from / up to where the rounding happens. 
	 * Top line is drawn first, then the bottom one.
	 */
	drawLine = g->prim.drawLine;
	error |= drawLine(g, x + arcwby2 + 1, y, xw - arcwby2 - 2, y);
	error |= drawLine(g, x + arcwby2 + 1, yh, xw - arcwby2 - 2, yh);
	
	/**
	 * Draw vertical spans from / up to where the rounding happens. 
	 * Left line is drawn first, then the right one.
	 */
	error |= drawLine(g, x, y + archby2 + 1, x, yh - archby2 - 2);
	error |= drawLine(g, xw, y + archby2 + 1, xw, yh - archby2 - 2);
	
	/** 
	 * Then draw the Arcs which are the rect's corners. Order is as follows: 
	 * Top-left corner
	 * Top-right corner
	 * Bottom-left corner
	 * Bottom-right corner
	 */
	drawArc = g->prim.drawArc;
	error |= drawArc(g, x, y, arcWidth, arcHeight, 90, 90);
	error |= drawArc(g, xw - arcWidth - 1, y, arcWidth, arcHeight, 0, 90);
	error |= drawArc(g, x, yh - arcHeight - 1, arcWidth, arcHeight, 180, 90);
	error |= drawArc(g, xw - arcWidth - 1, yh - arcHeight - 1, arcWidth,
		arcHeight, 270, 90);

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);
	
	return sjme_error_default(error);
}



sjme_errorCode sjme_scritchpen_core_fillArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFillArcFunc fillArc;
	sjme_jint yz, yze;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	fillArc = g->prim.fillArc;
	error |= fillArc(g, x, y, w, h, startAngle, arcAngle);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_fillPolygon(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint xPoints[],
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInValue sjme_jint yPoints[],
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFillPolygonFunc fillPolygon;
	sjme_jint yz, yze;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. TODO: This */
	//sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	fillPolygon = g->prim.fillPolygon;
	error |= fillPolygon(g, xPoints, xOffset, yPoints, yOffset, nPoints);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_fillRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFillRectFunc fillRect;
	sjme_jint yz, yze;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Cap width and height to 1 always. */
	if (w <= 0)
		w = 1;
	if (h <= 0)
		h = 1;
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	fillRect = g->prim.fillRect;
	error |= fillRect(g, x, y, w, h);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_fillRoundRect(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInPositive sjme_jint arcWidth,
	sjme_attrInPositive sjme_jint arcHeight)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFillRectFunc fillRect;
	sjme_scritchui_pencilFillArcFunc fillArc;
	sjme_jint xw, yh, arcwby2, archby2;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	arcWidth = fabs(arcWidth);
	arcHeight = fabs(arcHeight);

	/**
	 * We'll be doing only even arc widths and heights, otherwise the borders
	 * will look off due to fractional rounding (java's AWT Graphics do allow
	 * for odd width/heights though)
	 */
	if(arcWidth  %2 != 0) { arcWidth++; }
	if(arcHeight %2 != 0) { arcHeight++; }
	
	/* The arcs cannot be larger than the rect's width/height*/
	if(arcWidth >= w) { arcWidth = w-1; }
	if(arcHeight >= h) { arcHeight = h-1; }
	
	/* Pre-calculate coordinates. */
	xw = x + w;
	yh = y + h;
	arcwby2 = (arcWidth/2);
	archby2 = (arcHeight/2);

	error = SJME_ERROR_NONE;
	
	/**
	 * Fill the main rectangle area in the following order:
	 * Middle part
	 * Left Side part
	 * Right Side part 
	 */
	fillRect = g->prim.fillRect;
	error |= fillRect(g, x + arcwby2+1, y, w - arcWidth - 2, h);
	error |= fillRect(g, x, y + archby2+1, arcwby2+1, h - arcHeight - 2);
	error |= fillRect(g, x + (w - arcwby2)-1, y + archby2+1, arcwby2+1,
		h - arcHeight - 2);

	/** 
	 * Then fill the Arcs which are the rect's corners. Order is as follows: 
	 * Top-left corner
	 * Top-right corner
	 * Bottom-left corner
	 * Bottom-right corner
	 */
	fillArc = g->prim.fillArc;
	error |= fillArc(g, x, y, arcWidth, arcHeight, 90, 90);
	error |= fillArc(g, xw - arcWidth - 1, y, arcWidth, arcHeight, 0, 90);
	error |= fillArc(g, x, yh - arcHeight - 1, arcWidth, arcHeight, 180, 90);
	error |= fillArc(g, xw - arcWidth - 1, yh - arcHeight - 1, arcWidth,
		arcHeight, 270, 90);

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_fillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFillTriangleFunc fillTriangle;
	sjme_jint yz, yze;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	fillTriangle = g->prim.fillTriangle;
	error = fillTriangle(g, x1, y1, x2, y2, x3, y3);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);
	
	return sjme_error_default(error);
}
