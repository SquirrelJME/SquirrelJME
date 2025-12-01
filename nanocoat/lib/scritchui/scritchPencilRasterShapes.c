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

#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	#define SJME_ANGLE_RAD 0.017453292f
#endif

sjme_errorCode sjme_scritchpen_corePrim_drawArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle)
{
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	sjme_errorCode error;
	float centerX, centerY, radiusX, radiusY, startAngleRad, endAngleRad;
	float angle;
	sjme_jint steps, innerX, innerY, firstFillX, lastFillX, firstFillY, i;
	sjme_jint lastFillY;
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

	centerX = (x + w / 2.0f);
	centerY = (y + h / 2.0f);
	radiusX = (w / 2.0f);
	radiusY = (h / 2.0f);
	startAngleRad = (startAngle * SJME_ANGLE_RAD);
	endAngleRad = ((startAngle + arcAngle) * SJME_ANGLE_RAD) -
		startAngleRad;
	steps = abs(arcAngle * ((w + h) / 2.0f) / 50.0f);
	innerX, innerY;
	
	firstFillX = round(centerX + radiusX * cos(startAngleRad));
	firstFillY = round(centerY + radiusY * sin(startAngleRad));
	lastFillX = -1;
	lastFillY = -1;

	/* Make sure we're not drawing out of bounds. */
	if(firstFillX >= clipLine->s.x || firstFillX < clipLine->e.x ||
		firstFillY >= clipLine->s.y || firstFillY < clipLine->e.y)
	{
		/* If style is DOTTED, rendering will paint and skip pixels 1 by 1. */
		if (dot)
			error |= drawPixel(g, firstFillX, firstFillY);
		dot ^= dotFlip;
	}
	
	/* First pixel was alread drawn (if not OOB), so start from step 1. */
	for (i = 1; i < steps; i++) 
	{
		angle = startAngleRad + ((i * endAngleRad) / steps);
		
		innerX = round(centerX + radiusX * cos(angle));
		innerY = round(centerY + radiusY * sin(angle));
		
		if(innerX < clipLine->s.x || innerX >= clipLine->e.x ||
			innerY < clipLine->s.y || innerY >= clipLine->e.y)
			continue;

		/* We cannot paint the same pixel more than once (breaks alpha) */
		if((lastFillX == innerX && lastFillY == innerY) || 
			(firstFillX == innerX && firstFillY == innerY)) 
			continue;
		
		lastFillX = innerX;
		lastFillY = innerY;

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
#else
	sjme_todo("Fixed Point DrawArc Impl?");
	return sjme_error_notImplemented(0);
#endif
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
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	sjme_errorCode error;
	sjme_scritchui_pencilDrawHorizFunc drawHoriz;
	sjme_jint steps, innerX, innerY, filledZ, i, j, allocSize;
	sjme_jboolean hasAlpha = SJME_JNI_FALSE;
	float centerX, centerY, radiusX, radiusY, startAngleRad, endAngleRad;
	float maxRad, angle;
	sjme_jboolean* filledPixels;
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
	 * improves performance for opaque arcs. 8 pixels of information are packed
	 * in a single boolean/byte, noticeably reducing memory usage.
	 */
	if(hasAlpha)
	{
		/* We add 1 to the allocation size just so ceil() isn't needed */
		allocSize = (w * h) + 1;
		filledPixels = (sjme_jboolean *) sjme_alloca(allocSize);
		if (filledPixels == NULL)
		{
			error = sjme_error_outOfMemory(NULL, allocSize);
			goto fail_any;
		}
		memset(filledPixels, 0, allocSize);
	}
	
	centerX = x + w / 2.0f;
	centerY = y + h / 2.0f;
	radiusX = w / 2.0f;
	radiusY = h / 2.0f;
	startAngleRad = startAngle * SJME_ANGLE_RAD;
	endAngleRad = ((startAngle + arcAngle) * SJME_ANGLE_RAD) - startAngleRad;

	maxRad = radiusX > radiusY ? radiusX : radiusY;

	steps = abs(arcAngle * ((w + h) / 2.0f) / 50.0f);
	innerX, innerY;

	for(i = 0; i < steps; i++) 
	{
		angle = startAngleRad + (i * endAngleRad / steps);

		for(j = 0; j < maxRad; j++) 
		{
			innerX = round(centerX + radiusX * cos(angle) * (j / maxRad));
			innerY = round(centerY + radiusY * sin(angle) * (j / maxRad));
			filledZ = ((innerY-y) * w + innerX-x);
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
			if(hasAlpha ? (filledPixels[filledZ >> 3] &
				(1 << (7 - filledZ & 7))) : SJME_JNI_TRUE)
			{
				if(hasAlpha)
					filledPixels[filledZ >> 3] |= (1 << (7 - filledZ & 7));
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
#else
	sjme_todo("Fixed Point fillArc Impl?");
	return sjme_error_notImplemented(0);
#endif
}

sjme_errorCode sjme_scritchpen_corePrim_fillPolygon(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jint* xPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull sjme_jint* yPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawHorizFunc drawHoriz;
	sjme_jint ymin = INT32_MAX, ymax = INT32_MIN, intersectionCount = 0;
	sjme_jint xStart, xEnd, temp, dy, i, j, y;
	sjme_jint intersections[nPoints];
	sjme_jlongNative ix;
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

	for (i = 0; i < nPoints; i++) 
	{
		if (yPoints[i+yOffset] < ymin) 
			ymin = yPoints[i+yOffset];
		if (yPoints[i+yOffset] > ymax) 
			ymax = yPoints[i+yOffset];
	}

	/* Clip ymin and ymax to the screen area if any vertex is outside */
	if(ymin + g->state.translateReal.y < clipLine->s.y) 
		ymin = clipLine->s.y - g->state.translateReal.y;
	
	if(ymax + g->state.translateReal.y >= clipLine->e.y) 
		ymax = clipLine->e.y - g->state.translateReal.y;

	for (y = ymin; y < ymax; y++)
	{
		intersectionCount = 0;
		for (i = 0; i < nPoints; i++)
		{
			j = (i + 1) % nPoints;
			if ((yPoints[i + yOffset] <= y && yPoints[j + yOffset] > y) ||
				(yPoints[j + yOffset] <= y && yPoints[i + yOffset] > y))
			{
				dy = yPoints[j + yOffset] - yPoints[i + yOffset];
				if (dy != 0)
				{
					ix = (sjme_jlongNative) xPoints[i + xOffset] * dy +
						(y - yPoints[i + yOffset]) *
						(sjme_jlongNative) (xPoints[j + xOffset] -
						xPoints[i + xOffset]);
					ix /= dy;
					intersections[intersectionCount++] = (sjme_jint) ix;
				}
			}
		}

		for (i = 0; i < intersectionCount - 1; i++)
		{
			for (j = 0; j < intersectionCount - 1 - i; j++)
			{
				if (intersections[j] > intersections[j + 1])
				{
					temp = intersections[j];
					intersections[j] = intersections[j + 1];
					intersections[j + 1] = temp;
				}
			}
		}

		for (i = 0; i < intersectionCount; i += 2)
		{
			if (i + 1 < intersectionCount)
			{
				xStart = max(intersections[i], clipLine->s.x);
				xEnd = min(intersections[i + 1], clipLine->e.x);
				/* Start > End is an invalid area we can just skip */
				if(xEnd > xStart)
					if (sjme_error_is(error |= drawHoriz(g, xStart, y,
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
		if (sjme_error_is(error |= drawHoriz(g, x, yz, w)))
			break;
	
	/* Failed? */
	if (sjme_error_is(error))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
	
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
	
	/* Nothing to draw? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;

	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	drawArc = g->prim.drawArc;
	if (sjme_error_is(error |= g->prim.drawArc(g, x, y, w, h,
		startAngle, arcAngle)))
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

sjme_errorCode sjme_scritchpen_core_drawPolyline(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jint* xPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull sjme_jint* yPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawLineFunc drawLine;
	sjme_jint i;

	if (g == NULL || xPoints == NULL || yPoints == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if(xOffset < 0 || yOffset < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);

	for(i = 0; i < nPoints; i++) 
	{
		sjme_scritchpen_coreUtil_applyTranslate(g, &xPoints[xOffset+i],
			&yPoints[yOffset+1]);
	}

	error = SJME_ERROR_NONE;
	
	/** 
	 * Drawing a polyline means basically drawing the edges (lines) between
	 * each pair of vertices that compose said polyline.
	 */ 
	drawLine = g->prim.drawLine;
	for(int i=0; i < nPoints; i++)
	{
		if(i == nPoints-1) 
			drawLine(g, xPoints[xOffset+i], yPoints[yOffset+i],
				xPoints[xOffset], yPoints[yOffset]);
		else 
			drawLine(g, xPoints[xOffset+i], yPoints[yOffset+i],
				xPoints[xOffset+i+1], yPoints[yOffset+i+1]);
	}

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

	/* Nothing to draw? */
	if (w < 0 || h < 0)
		return SJME_ERROR_NONE;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	drawRect = g->prim.drawRect;
	if (sjme_error_is(error |= g->prim.drawRect(g, x, y, w, h)))
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
	
	/* Nothing to draw? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;

	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	arcWidth = abs(arcWidth);
	arcHeight = abs(arcHeight);

	/**
	 * We'll be doing only even arc widths and heights, otherwise the borders
	 * will look off due to fractional rounding (java's AWT Graphics do allow
	 * for odd width/heights though)
	 */
	if(arcWidth  %2 != 0)
		arcWidth++;
	if(arcHeight %2 != 0)
		arcHeight++;
	
	/* The arcs cannot be larger then the rect's width/height*/
	if(arcWidth >= w)
		arcWidth = w-1;
	if(arcHeight >= h)
		arcHeight = h-1;

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

sjme_errorCode sjme_scritchpen_core_drawTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawLineFunc drawLine;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);

	error = SJME_ERROR_NONE;
	
	/** Raster the triangle's outline by drawing lines between its vertices */
	drawLine = g->prim.drawLine;
	error |= drawLine(g, x1, y1, x2, y2);
	error |= drawLine(g, x2, y2, x3, y3);
	error |= drawLine(g, x3, y3, x1, y1);

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

	if(w <= 0 || h <= 0)
		return SJME_ERROR_NONE;
		
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
	sjme_attrInNotNull sjme_jint* xPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull sjme_jint* yPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFillPolygonFunc fillPolygon;
	sjme_jint i;

	if (g == NULL || xPoints == NULL || yPoints == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if(xOffset < 0 || yOffset < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	for(i = 0; i < nPoints; i++) 
	{
		sjme_scritchpen_coreUtil_applyTranslate(g, &xPoints[xOffset+i],
			&yPoints[yOffset+1]);
	}
	
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

	/* Nothing to draw? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
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

	/* Nothing to draw? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	arcWidth = abs(arcWidth);
	arcHeight = abs(arcHeight);

	/**
	 * We'll be doing only even arc widths and heights, otherwise the borders
	 * will look off due to fractional rounding (java's AWT Graphics do allow
	 * for odd width/heights though)
	 */
	if(arcWidth  %2 != 0)
		arcWidth++;
	if(arcHeight %2 != 0)
		arcHeight++;
	
	/* The arcs cannot be larger than the rect's width/height*/
	if(arcWidth >= w)
		arcWidth = w-1;
	if(arcHeight >= h)
		arcHeight = h-1;
	
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
