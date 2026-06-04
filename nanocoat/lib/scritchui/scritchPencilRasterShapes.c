/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

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

static sjme_errorCode sjme_scritchpen_core_clipPolygon(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint nPoints,
	sjme_attrInNotNull sjme_scritchui_line* clipLine)
{
	if (g == NULL || inXPoints == NULL || inYPoints == NULL ||
		clipLine == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_attrOptimize sjme_scritchpen_corePrim_drawArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle)
{
	sjme_errorCode error;
	sjme_jint steps, innerX, innerY, firstFillX, lastFillX, firstFillY, i;
	sjme_jint lastFillY;
	sjme_jboolean dot, dotFlip;
	sjme_scritchui_line* clipLine;
	sjme_scritchui_pencilDrawPixelFunc drawPixel;
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	float centerX, centerY, radiusX, radiusY, startAngleRad, endAngleRad;
	float angle;
#endif

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

	/* DrawArc draws an arc of [w+1,h+1] size*/
	w += 1;
	h += 1;

#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	/* This works similarly to Bresenham's midpoint circle algorithm. */
	/* "steps" dictates how many iterations are used to draw the circle. A */
	/* bigger value will result in the same pixels being hit more times (and */
	/* wasted cycles since they'll be discarded later) but will guarantee a */
	/* perfectly filled outline, whereas a small value will result in gaps */
	/* appearing in the circle since less points will be sampled. The */
	/* current value is a good balance between filling all positions on all */
	/* kinds of shapes while hitting as few pixels as possible. */

	centerX = (x + w / 2.0f);
	centerY = (y + h / 2.0f);
	radiusX = (w / 2.0f);
	radiusY = (h / 2.0f);
	startAngleRad = (startAngle * SJME_ANGLE_RAD);
	endAngleRad = ((startAngle + arcAngle) * SJME_ANGLE_RAD) -
		startAngleRad;
	steps = fabs(arcAngle * ((w + h) / 2.0f) / 50.0f);
	
	firstFillX = round(centerX + radiusX * cos(startAngleRad));
	firstFillY = round(centerY + radiusY * sin(startAngleRad));
	lastFillX = -1;
	lastFillY = -1;

	/* Make sure we're not drawing out of bounds. */
	if (firstFillX >= clipLine->s.x || firstFillX < clipLine->e.x ||
		firstFillY >= clipLine->s.y || firstFillY < clipLine->e.y)
	{
		/* If style is DOTTED, rendering will paint and skip pixels 1 by 1. */
		if (dot)
			error |= drawPixel(g, firstFillX, firstFillY);
		dot ^= dotFlip;
	}
	
	/* First pixel was already drawn (if not OOB), so start from step 1. */
	for (i = 1; i < steps; i++) 
	{
		angle = startAngleRad + ((i * endAngleRad) / steps);
		
		innerX = round(centerX + radiusX * cos(angle));
		innerY = round(centerY + radiusY * sin(angle));
		
		if (innerX < clipLine->s.x || innerX >= clipLine->e.x ||
			innerY < clipLine->s.y || innerY >= clipLine->e.y)
			continue;

		/* We cannot paint the same pixel more than once (breaks alpha) */
		if ((lastFillX == innerX ^ lastFillY == innerY) || 
			(firstFillX == innerX && firstFillY == innerY)) 
		{
			lastFillX = -1;
			lastFillY = -1;
			continue; 
		}
		
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

sjme_errorCode sjme_attrOptimize sjme_scritchpen_corePrim_fillArc(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint startAngle,
	sjme_attrInValue sjme_jint arcAngle)
{
	sjme_errorCode error;
	sjme_jint steps, innerX, innerY, filledZ, i, j, allocSize, zh, zl;
	sjme_jint zhMask;
	sjme_jboolean hasAlpha, filledVoid;
	sjme_jboolean* filledPixels;
	sjme_scritchui_line* clipLine;
	sjme_scritchui_pencilDrawPixelFunc drawPixel;
#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	float centerX, centerY, radiusX, radiusY, startAngleRad, endAngleRad;
	float maxRad, angle;
#endif

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Java's coordinate system has positive angles moving counter-clockwise */
	arcAngle = -arcAngle;
	startAngle = -startAngle;

	/* How are pixels to be drawn? */
	hasAlpha = g->hasAlpha;
	drawPixel = g->prim.drawPixel;

	/* Get clipping information. */
	clipLine = &g->state.clipLine;

#if defined(SJME_CONFIG_HAS_FLOAT_HARD)
	/* Only allocate the alpha buffer if the color isn't opaque. Noticeably */
	/* improves performance for opaque arcs. 8 pixels of information are */
	/* packed in a single boolean/byte, noticeably reducing memory usage. */
	/* Width and height are inclusive, hence the + 1 on each. */
	if (hasAlpha)
	{
		allocSize = (w + 1) * (h + 1);
		filledPixels = sjme_alloca(allocSize);
		if (filledPixels == NULL)
		{
			error = sjme_error_outOfMemory(NULL, allocSize);
			goto fail_any;
		}

		/* Set every value to high to indicate that nothing has ever */
		/* been drawn here. */
		memset(filledPixels, 0xFF, allocSize);

		/* Allow filledPixels high index to be set with values. */
		zhMask = INT32_MAX;
	}

	/* If not drawing with alpha, filledPixels is still valid however it */
	/* is filled with nothing. */
	else
	{
		/* Have the array access still be valid, but go nowhere. */
		filledPixels = &filledVoid;

		/* No pixel is ever considered to have ever been drawn, */
		/* therefor all pixels are valid. */
		filledVoid = 0xFF;

		/* Only the 0th index is valid, thus strip all bits. */
		/* This is also used when masking zl. */
		zhMask = 0;
	}

	/* Calculate arc coordinates. This is effectively similar to */
	/* how sjme_scritchpen_corePrim_drawArc() renders arcs. */
	centerX = x + w / 2.0f;
	centerY = y + h / 2.0f;
	radiusX = w / 2.0f;
	radiusY = h / 2.0f;
	startAngleRad = startAngle * SJME_ANGLE_RAD;
	endAngleRad = ((startAngle + arcAngle) * SJME_ANGLE_RAD) - startAngleRad;

	maxRad = (radiusX > radiusY ? radiusX : radiusY);

	steps = fabs(arcAngle * ((w + h) / 2.0f) / 50.0f);

	/* Draw arcs in steps. */
	error = SJME_ERROR_NONE;
	for (i = 0; i < steps; i++) 
	{
		angle = startAngleRad + (i * endAngleRad / steps);

		for (j = 0; j < maxRad; j++) 
		{
			innerX = round(centerX + radiusX * cos(angle) * (j / maxRad));
			innerY = round(centerY + radiusY * sin(angle) * (j / maxRad));
			
			/* Make sure we're not drawing out of bounds. Or accessing the */
			/* alpha buffer at an invalid position with innerX-x or innerY-y */
			if (innerX < clipLine->s.x || innerX >= clipLine->e.x ||
				innerY < clipLine->s.y || innerY >= clipLine->e.y  ||
				innerX - x < 0 || innerY - y < 0)
				continue;

			/* Calculate filledPixels index. */
			filledZ = ((innerY - y) * w + innerX - x);
			zh = (filledZ >> 3) & zhMask;
			zl = (1 << (7 - filledZ & 7));
			
			/* Only draw if opaque, or if the alpha buffer is not yet filled */
			/* for the current position. */
			if ((filledPixels[zh] & zl) != 0)
				error |= drawPixel(g, innerX, innerY);
			filledPixels[zh] ^= (zl & zhMask);
		}
	}

	if (hasAlpha)
		sjme_alloca_free(filledPixels);

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
	
	/* Success? */
	return error;
	
fail_any:
	if (hasAlpha && filledPixels != NULL &&
		filledPixels != &filledVoid)
		sjme_alloca_free(filledPixels);
	
	return sjme_error_default(error);
#else
	sjme_todo("Fixed Point fillArc Impl?");
	return sjme_error_notImplemented(0);
#endif
}

sjme_errorCode sjme_attrOptimize sjme_scritchpen_corePrim_fillPolygon(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints,
	sjme_attrInValue sjme_jboolean safePoints)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawHorizFunc drawHoriz;
	sjme_jint yMin, yMax, intersectionCount, allocBytes;
	sjme_jint xStart, xEnd, temp, dy, i, j, y, ix;
	sjme_jint* xPoints;
	sjme_jint* yPoints;
	sjme_jint* intersections;
	sjme_scritchui_line* clipLine;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (xOffset < 0 || yOffset < 0 || nPoints < 0 ||
		(xOffset + nPoints) < 0 || (yOffset + nPoints) < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Not drawing a polygon? */
	if (nPoints == 0)
		return SJME_ERROR_NONE;

	/* Allocate intersections. */
	allocBytes = sizeof(sjme_jint) * (nPoints + 1);
	intersections = sjme_alloca(allocBytes);

	/* Failed to allocate? */
	if (intersections == NULL)
	{
		error = sjme_error_outOfMemory(NULL, allocBytes * 3);
		goto fail_alloc;
	}
	
	/* Clear. */
	memset(intersections, 0, allocBytes);

	/* Points are considered to be safe to modify? */
	if (safePoints)
	{
		/* This is technically an unsafe cast. */
		xPoints = (void*)&inXPoints[xOffset];
		yPoints = (void*)&inYPoints[yOffset];
	}

	/* The input points must not be modified. */
	else
	{
		/* Input arrays are correctly bounded, so they can be copied, first */
		/* we need to allocate accordingly. */
		xPoints = sjme_alloca(allocBytes);
		yPoints = sjme_alloca(allocBytes);

		/* If any failed, that is not good. */
		if (xPoints == NULL || yPoints == NULL || intersections == NULL)
		{
			error = sjme_error_outOfMemory(NULL, allocBytes * 3);
			goto fail_alloc;
		}

		/* Clear everything so all space is wiped. */
		memset(xPoints, 0, allocBytes);
		memset(yPoints, 0, allocBytes);

		/* Coordinates can be copied over directly. */
		memmove(&xPoints[0], &inXPoints[xOffset], sizeof(sjme_jint) * nPoints);
		memmove(&yPoints[0], &inYPoints[yOffset], sizeof(sjme_jint) * nPoints);
	}

	/* Start with extremes on both ends. */
	yMax = INT32_MIN;
	yMin = INT32_MAX;

	/* Alpha blending in hardware? */
	if (!g->state.applyAlpha && g->impl->drawHorizSrc != NULL)
		drawHoriz = g->impl->drawHorizSrc;
	else if (g->state.applyAlpha && g->impl->drawHorizSrcOver != NULL &&
		g->state.blending == SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER)
		drawHoriz = g->impl->drawHorizSrcOver;
	else
		drawHoriz = g->prim.drawHoriz;

	/* Get clipping information. */
	clipLine = &g->state.clipLine;
	
	/* Filling polygons is done through the canonical Scan Line fill */
	/* algorithm. It works just like its description: Find the yMax and */
	/* yMin of the polygon, calculate the intersections between each edge, */
	/* sort intersections by increasing X coordinate, then fill from top to */
	/* bottom. */
	for (i = 0; i < nPoints; i++) 
	{
		if (yPoints[i] < yMin) 
			yMin = yPoints[i];
		if (yPoints[i] > yMax) 
			yMax = yPoints[i];
	}

	/* Clip ymin and ymax to the screen area if any vertex is outside */
	if (yMin + g->state.translateReal.y < clipLine->s.y) 
		yMin = clipLine->s.y - g->state.translateReal.y;
	
	if (yMax + g->state.translateReal.y >= clipLine->e.y) 
		yMax = clipLine->e.y - g->state.translateReal.y;

	/* Render polygon by each scanline. */
	error = SJME_ERROR_NONE;
	for (y = yMin; y < yMax; y++)
	{
		intersectionCount = 0;
		for (i = 0; i < nPoints; i++)
		{
			j = (i + 1) % nPoints;
			if ((yPoints[i] <= y && yPoints[j] > y) ||
				(yPoints[j] <= y && yPoints[i] > y))
			{
				dy = yPoints[j] - yPoints[i];
				if (dy != 0)
				{
					ix = xPoints[i] * dy + (y - yPoints[i]) *
						(xPoints[j] - xPoints[i]);
					ix /= dy;
					intersections[intersectionCount++] = ix;
				}
			}
		}

		for (i = 0; i < intersectionCount - 1; i++)
			for (j = 0; j < intersectionCount - 1 - i; j++)
			{
				if (intersections[j] > intersections[j + 1])
				{
					temp = intersections[j];
					intersections[j] = intersections[j + 1];
					intersections[j + 1] = temp;
				}
			}

		for (i = 0; i < intersectionCount; i += 2)
			if (i + 1 < intersectionCount)
			{
				xStart = sjme_max(intersections[i], clipLine->s.x);
				xEnd = sjme_min(intersections[i + 1], clipLine->e.x);

				/* Start > End is an invalid area we can just skip */
				if (xEnd <= xStart)
					continue;

				/* Draw the scan. */
				error |= drawHoriz(g, xStart, y, xEnd - xStart);
			}
	}

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;

	/* Cleanup. */
	sjme_alloca_free(intersections);
	if (!safePoints)
	{
		sjme_alloca_free(xPoints);
		sjme_alloca_free(yPoints);
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
fail_alloc:
	if (intersections != NULL)
		sjme_alloca_free(intersections);
	
	if (!safePoints)
	{
		if (xPoints != NULL)
			sjme_alloca_free(xPoints);
		if (yPoints != NULL)
			sjme_alloca_free(yPoints);
	}
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_attrOptimize sjme_scritchpen_corePrim_fillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	sjme_jint xPoints[3];
	sjme_jint yPoints[3];
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* A triangle is just a polygon with 3 vertices, so just call */
	/* fillPolygon() to draw it like any other in Software mode, for */
	/* consistency. */
	xPoints[0] = x1;
	yPoints[0] = y1;
	xPoints[1] = x2;
	yPoints[1] = y2;
	xPoints[2] = x3;
	yPoints[2] = y3;

	/* For now use the polygon filling algorithm. Note that the normal */
	/* triangle drawing algorithm will be much faster in the future. */
	return g->prim.fillPolygon(g,
		&xPoints[0], 0,
		&yPoints[0], 0, 3,
		SJME_JNI_TRUE);
}

sjme_errorCode sjme_attrOptimize sjme_scritchpen_corePrim_drawRect(
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
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_attrOptimize sjme_scritchpen_corePrim_fillRect(
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
		error |= drawHoriz(g, x, yz, w);
	
	/* Failed? */
	if (sjme_error_is(error))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
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

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Nothing to draw? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);

	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Use primitive arc drawing. */
	if (sjme_error_is(error = g->prim.drawArc(g, x, y, w, h,
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
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints)
{
	sjme_errorCode error;
	sjme_scritchui_pencilDrawLineFunc drawLine;
	sjme_jint i, n, allocBytes;
	sjme_jint* xPoints;
	sjme_jint* yPoints;

	if (g == NULL || inXPoints == NULL || inYPoints == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (xOffset < 0 || yOffset < 0 || nPoints < 0 ||
		(xOffset + nPoints) < 0 || (yOffset + nPoints) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Drawing nothing? */
	if (nPoints == 0)
		return SJME_ERROR_NONE;
	
	/* Input arrays are correctly bounded, so they can be copied, first we */
	/* need to allocate accordingly. */
	allocBytes = sizeof(sjme_jint) * (nPoints + 1);
	xPoints = sjme_alloca(allocBytes);
	yPoints = sjme_alloca(allocBytes);

	/* If any failed, that is not good. */
	if (xPoints == NULL || yPoints == NULL)
	{
		error = sjme_error_outOfMemory(NULL, allocBytes * 3);
		goto fail_alloc;
	}

	/* Clear everything so all space is wiped. */
	memset(xPoints, 0, allocBytes);
	memset(yPoints, 0, allocBytes);

	/* Coordinates can be copied over directly. */
	memmove(&xPoints[0], &inXPoints[xOffset], sizeof(sjme_jint) * nPoints);
	memmove(&yPoints[0], &inYPoints[yOffset], sizeof(sjme_jint) * nPoints);

	/* Translate all points. */
	for (i = 0; i < nPoints; i++)
		sjme_scritchpen_coreUtil_applyTranslate(g,
			&xPoints[i], &yPoints[i]);
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		goto fail_lock;

	/* Primitive line drawing will handle alpha blending. */
	drawLine = g->prim.drawLine;
	
	/* Drawing a polyline means basically drawing the edges (lines) between */
	/* each pair of vertices that compose said polyline. */
	error = SJME_ERROR_NONE;
	for (i = 0; i < nPoints; i++)
		error |= drawLine(g, xPoints[i], yPoints[i],
			xPoints[i + 1], yPoints[i + 1]);

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		goto fail_unlock;

	/* Cleanup. */
	sjme_alloca_free(xPoints);
	sjme_alloca_free(yPoints);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);

fail_unlock:
fail_lock:
fail_alloc:
	if (xPoints != NULL)
		sjme_alloca_free(xPoints);
	if (yPoints != NULL)
		sjme_alloca_free(yPoints);
	
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

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing to draw? */
	if (w < 0 || h < 0)
		return SJME_ERROR_NONE;
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Use primitives otherwise. */
	if (sjme_error_is(error = g->prim.drawRect(g, x, y, w, h)))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
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
	sjme_jint xw, yh, arcWBy2, arcHBy2;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Nothing to draw? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;

	/* Arcs cannot be negative. */
	arcWidth = abs(arcWidth);
	arcHeight = abs(arcHeight);

	/* We'll be doing only even arc widths and heights, otherwise the */
	/* borders will look off due to fractional rounding (java's AWT  */
	/* Graphics do allow for odd width/heights though) */
	if ((arcWidth & 1) != 0)
		arcWidth++;
	if ((arcHeight & 1) != 0)
		arcHeight++;
	
	/* The arcs cannot be larger than the rect's width/height */
	if (arcWidth >= w)
		arcWidth = w - 1;
	if (arcHeight >= h)
		arcHeight = h - 1;

	/* Pre-calculate coordinates. */
	xw = x + w;
	yh = y + h;
	arcWBy2 = (arcWidth / 2);
	arcHBy2 = (arcHeight / 2);
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	error = SJME_ERROR_NONE;
	drawLine = g->prim.drawLine;
	
	/* Draw horizontal spans first, from / up to where the rounding happens. */ 
	/* Top line is drawn first, then the bottom one. */
	error |= drawLine(g, x + arcWBy2 + 1, y,
		xw - arcWBy2 - 2, y);
	error |= drawLine(g, x + arcWBy2 + 1, yh,
		xw - arcWBy2 - 2, yh);
	
	/* Draw vertical spans from / up to where the rounding happens. */ 
	/* Left line is drawn first, then the right one. */
	error |= drawLine(g, x, y + arcHBy2 + 1,
		x, yh - arcHBy2 - 2);
	error |= drawLine(g, xw, y + arcHBy2 + 1,
		xw, yh - arcHBy2 - 2);
	
	/* Then draw the Arcs which are the rect's corners. Order is as follows: */
	/* Top-left corner */
	/* Top-right corner */
	/* Bottom-left corner */
	/* Bottom-right corner */
	drawArc = g->prim.drawArc;
	error |= drawArc(g, x, y, arcWidth, arcHeight,
		90, 90);
	error |= drawArc(g, xw - arcWidth - 1, y, arcWidth,
		arcHeight, 0, 90);
	error |= drawArc(g, x, yh - arcHeight - 1, arcWidth,
		arcHeight, 180, 90);
	error |= drawArc(g, xw - arcWidth - 1, yh - arcHeight - 1,
		arcWidth, arcHeight, 270, 90);

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
		
	/* Transform. */
	if (sjme_error_is(error = g->util->applyTranslate(g, &x1, &y1)))
		return sjme_error_default(error);
	if (sjme_error_is(error = g->util->applyTranslate(g, &x2, &y2)))
		return sjme_error_default(error);
	if (sjme_error_is(error = g->util->applyTranslate(g, &x3, &y3)))
		return sjme_error_default(error);
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Clear error state. */
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

	if (w <= 0 || h <= 0)
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
	sjme_attrInNotNull const sjme_jint* inXPoints,
	sjme_attrInPositive sjme_jint xOffset,
	sjme_attrInNotNull const sjme_jint* inYPoints,
	sjme_attrInPositive sjme_jint yOffset,
	sjme_attrInPositive sjme_jint nPoints)
{
	sjme_errorCode error;
	sjme_jint i, allocBytes;
	sjme_jint* xPoints;
	sjme_jint* yPoints;
	sjme_scritchui_line* clipLine;
	sjme_jboolean needsClipping;

	if (g == NULL || inXPoints == NULL || inYPoints == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (xOffset < 0 || yOffset < 0 || nPoints < 0 ||
		(xOffset + nPoints) < 0 || (yOffset + nPoints) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Drawing nothing? */
	if (nPoints == 0)
		return SJME_ERROR_NONE;
	
	/* Input arrays are correctly bounded, so they can be copied, first we */
	/* need to allocate accordingly. */
	allocBytes = sizeof(sjme_jint) * (nPoints + 1);
	xPoints = sjme_alloca(allocBytes);
	yPoints = sjme_alloca(allocBytes);

	/* If any failed, that is not good. */
	if (xPoints == NULL || yPoints == NULL)
	{
		error = sjme_error_outOfMemory(NULL, allocBytes * 3);
		goto fail_alloc;
	}

	/* Clear everything so all space is wiped. */
	memset(xPoints, 0, allocBytes);
	memset(yPoints, 0, allocBytes);

	/* Coordinates can be copied over directly. */
	memmove(&xPoints[0], &inXPoints[xOffset], sizeof(sjme_jint) * nPoints);
	memmove(&yPoints[0], &inYPoints[yOffset], sizeof(sjme_jint) * nPoints);

	/* Translate all coordinates. */
	for (i = 0; i < nPoints; i++)
		sjme_scritchpen_coreUtil_applyTranslate(g,
			&xPoints[i], &yPoints[i]);
	
	/* Check to see if clipping needs to be performed on the polygon. */
	clipLine = &g->state.clipLine;
	needsClipping = SJME_JNI_FALSE;
	for (i = 0; i < nPoints; i++)
		needsClipping |= (xPoints[i] < clipLine->s.x || 
			yPoints[i] < clipLine->s.y ||
			xPoints[i] > clipLine->e.x || 
			yPoints[i] > clipLine->e.y);
	
	/* Perform Sutherland-Hodgman clipping for any software which decides */
	/* it should draw absurdly large polygons. */
	if (needsClipping)
		if (sjme_error_is(error = sjme_scritchpen_core_clipPolygon(g,
			xPoints, yPoints, nPoints, clipLine)))
			goto fail_clipPolygon;

	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		goto fail_lock;
	
	/* Use primitive draw operation. */
	if (sjme_error_is(error = g->prim.fillPolygon(g,
		xPoints, 0, yPoints, 0, nPoints,
		SJME_JNI_TRUE)))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		goto fail_unlock;
	
	/* Cleanup. */
	sjme_alloca_free(xPoints);
	sjme_alloca_free(yPoints);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Release lock before failing */
	sjme_scritchpen_core_lockRelease(g);

fail_clipPolygon:
fail_unlock:
fail_lock:
fail_alloc:
	if (xPoints != NULL)
		sjme_alloca_free(xPoints);
	if (yPoints != NULL)
		sjme_alloca_free(yPoints);
	
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

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing to draw? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;
	
	/* Transform. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Use primitives otherwise. */
	if (sjme_error_is(error = g->prim.fillRect(g, x, y, w, h)))
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
	sjme_jint xw, yh, arcWBy2, arcHBy2;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Nothing to draw? */
	if (w <= 0 || h <= 0)
		return SJME_ERROR_NONE;
	
	/* Arcs cannot be negative. */
	arcWidth = abs(arcWidth);
	arcHeight = abs(arcHeight);

	/* We'll be doing only even arc widths and heights, otherwise the */
	/* borders will look off due to fractional rounding (java's AWT  */
	/* Graphics do allow for odd width/heights though) */
	if ((arcWidth & 1) != 0)
		arcWidth++;
	if ((arcHeight & 1) != 0)
		arcHeight++;
	
	/* The arcs cannot be larger than the rect's width/height */
	if (arcWidth >= w)
		arcWidth = w - 1;
	if (arcHeight >= h)
		arcHeight = h - 1;

	/* Pre-calculate coordinates. */
	xw = x + w;
	yh = y + h;
	arcWBy2 = (arcWidth / 2);
	arcHBy2 = (arcHeight / 2);
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);

	error = SJME_ERROR_NONE;
	fillRect = g->prim.fillRect;
	
	/* Fill the main rectangle area in the following order: */
	/* Middle part */
	/* Left Side part */
	/* Right Side part */
	error |= fillRect(g, x + arcWBy2 + 1, y,
		w - arcWidth - 2, h);
	error |= fillRect(g, x, y + arcHBy2 + 1, arcWBy2 + 1,
		h - arcHeight - 2);
	error |= fillRect(g, x + (w - arcWBy2) - 1,
		y + arcHBy2 + 1, arcWBy2 + 1, h - arcHeight - 2);
	
	/* Then fill the Arcs which are the rect's corners. Order is as follows: */
	/* Top-left corner */
	/* Top-right corner */
	/* Bottom-left corner */
	/* Bottom-right corner */
	fillArc = g->prim.fillArc;
	error |= fillArc(g, x, y, arcWidth, arcHeight,
		90, 90);
	error |= fillArc(g, xw - arcWidth - 1, y,
		arcWidth, arcHeight, 0, 90);
	error |= fillArc(g, x, yh - arcHeight - 1,
		arcWidth, arcHeight, 180, 90);
	error |= fillArc(g, xw - arcWidth - 1,
		yh - arcHeight - 1, arcWidth,
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
	sjme_jint xPoints[3];
	sjme_jint yPoints[3];
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* A triangle is just a polygon with 3 vertices, so just call */
	/* fillPolygon() to draw it like any other in Software mode, for */
	/* consistency. */
	xPoints[0] = x1;
	yPoints[0] = y1;
	xPoints[1] = x2;
	yPoints[1] = y2;
	xPoints[2] = x3;
	yPoints[2] = y3;

	/* TODO: For now use the polygon filling algorithm. Note that the normal */
	/* TODO: triangle drawing algorithm will be much faster in the future. */
	/* TODO: Note that this should not do the primitive draw directly as */
	/* TODO: that does not handle any kind of translation and/or clipping. */
	return g->apiInThread->fillPolygon(g,
		&xPoints[0], 0,
		&yPoints[0], 0, 3);
}
