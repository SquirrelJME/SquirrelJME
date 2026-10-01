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
	sjme_jint steps, innerX, innerY, lastFillX, lastFillY, i;
	sjme_jint centerX, centerY, radiusX, radiusY;
	sjme_jboolean dot, dotFlip;
	sjme_scritchui_line* clipLine;
	sjme_scritchui_pencilDrawPixelFunc drawPixel;
	sjme_fixed startAngleRad, endAngleRad;
	sjme_fixed angleStep, fillCos, fillSin, nextCos, nextSin, stepCos, stepSin;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	error = SJME_ERROR_NONE;

	dot = SJME_JNI_TRUE;
	dotFlip = (g->state.stroke == SJME_SCRITCHUI_PENCIL_STROKE_DOTTED);

	drawPixel = g->prim.drawPixel;

	/* Get clipping information. */
	clipLine = &g->state.clipLine;

	/* Java's coordinate system has positive angles move counter-clockwise */
	arcAngle = -arcAngle;
	startAngle = -startAngle;

	/* Cap these to 360 degrees, otherwise we'll overdraw semi-transparent */
	/* arcs. It also shouldn't matter whether the angle is positive or */
	/* negative, it just gets clamped to a full circle anyway. */
	if (arcAngle > 360 || arcAngle < -360)
		arcAngle = 360;

	/* DrawArc draws an arc of [w+1,h+1] size*/
	w += 1;
	h += 1;

	/* This works similarly to Bresenham's midpoint circle algorithm. */
	/* The number of steps must account for the maximum dimension and arc */
	/* span, otherwise we get gaps and risk overdraws on oblique ovals. */
	/* The magic "45" here is just the ideal step density divider of pi/4. */
	/* Any lower and it causes overdraw, any bigger and gaps show up. */
	steps = sjme_max(sjme_abs(arcAngle), (sjme_max(w, h) *
		sjme_abs(arcAngle)) / 45);

	/* If we don't have at least one step to be drawn, return outright. */
	if (steps <= 0)
		return SJME_ERROR_NONE;

	centerX = (x << 1) + w;
	centerY = (y << 1) + h;
	radiusX = w;
	radiusY = h;
	startAngleRad = sjme_fixed_degToRad(sjme_fixed_hi(startAngle));
	endAngleRad = sjme_fixed_degToRad(sjme_fixed_hi(startAngle + arcAngle)) -
		startAngleRad;
	angleStep = endAngleRad / steps;

	/* DDA, because cos/sin in the inner loop is expensive. Get only the */
	/* increments for each step as well as starting values, and do simple */
	/* operations inside the loop. */
	stepCos = sjme_fixed_cos(angleStep);
	stepSin = sjme_fixed_sin(angleStep);
	fillCos = sjme_fixed_cos(startAngleRad);
	fillSin = sjme_fixed_sin(startAngleRad);

	/* To prevent overdraw here, all we need to do is track the last pixel. */
	lastFillX = -1;
	lastFillY = -1;

	for (i = 0; i < steps; i++)
	{
		innerX = (centerX + (sjme_fixed_int(sjme_fixed_mul(
			sjme_fixed_hi(radiusX), fillCos)))) >> 1;
		innerY = (centerY + (sjme_fixed_int(sjme_fixed_mul(
			sjme_fixed_hi(radiusY), fillSin)))) >> 1;

		/* We cannot paint the same pixel more than once (breaks alpha) */
		if (innerX != lastFillX || innerY != lastFillY)
		{
			lastFillX = innerX;
			lastFillY = innerY;

			if (innerX < clipLine->s.x || innerX >= clipLine->e.x ||
				innerY < clipLine->s.y || innerY >= clipLine->e.y)
				continue;

			if (dot)
				error |= drawPixel(g, innerX, innerY);

			dot ^= dotFlip;
		}

		/* As the cos/sin step increments were calculated out of the loop, */
		/* all we need to do hare are simple multiply-adds on each step. */
		nextCos = (sjme_fixed_mul(fillCos, stepCos) -
			sjme_fixed_mul(fillSin, stepSin));
		nextSin = (sjme_fixed_mul(fillSin, stepCos) +
			sjme_fixed_mul(fillCos, stepSin));

		fillCos = nextCos;
		fillSin = nextSin;
	}

	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;

	/* Success? */
	return error;

fail_any:
	return sjme_error_default(error);
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
	sjme_jint py, px;
	sjme_jboolean isFullCircle, isConcave, inSector;
	sjme_scritchui_line* clipLine;
	sjme_scritchui_pencilDrawPixelFunc drawPixel;
	sjme_jint centerX, centerY, radiusX, radiusY;
	sjme_jint dx, dy, lineStartX, lineEndX, margin, nStart, nEnd;
	sjme_jint startX, startY, endX, endY;
	sjme_fixed maxDx, normY, normY2, normDx, cosE, cosS, sinE, sinS;
	sjme_fixed crossStart, crossEnd, crossYStart, crossYEnd;

	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initialize for warnings/optimization. */
	cosS = SJME_FIXED_ONE;
	sinS = SJME_FIXED_ONE;
	cosE = SJME_FIXED_ONE;
	sinE = SJME_FIXED_ONE;
	crossYStart = 0;
	crossYEnd = 0;

	/* Get clipping information. */
	clipLine = &g->state.clipLine;

	/* Figure out the start and end points of the bounding box right away, */
	/* as we can use the clip rectangle as the direct limits for those. */
	startX = sjme_max(x, clipLine->s.x);
	startY = sjme_max(y, clipLine->s.y);
	endX = sjme_min(x + w, clipLine->e.x);
	endY = sjme_min(y + h, clipLine->e.y);

	/* If the resulting angle span doesn't paint any pixels, we can skip */
	/* this entirely. */
	if (startX >= endX || startY >= endY)
		return SJME_ERROR_NONE;

	/* Arcs are filled by calculating the arc's bounding box, and painting */
	/* with a standard scanline raster algorithm. We normalize the */
	/* coordinates (normDx, normDy) so that ovals match Java SE's behavior */
	/* of angles scaling with the bounding box for any shape. */
	radiusX = w >> 1;
	radiusY = h >> 1;
	centerX = x + radiusX;
	centerY = y + radiusY;

	/* Angle vectors setup. These are what we use to actually check if a */
	/* pixel is within the angle boundaries for drawing. A full circle */
	/* actually gives us a fast path where we don't even need to check */
	/* cross products on each scanline. */
	/* Java's coordinate system has positive angles move counter-clockwise */
	/* so we also negate the start and arc angles inline when normalizing. */
	isFullCircle = (sjme_abs(arcAngle) >= 360);
	nStart = (-startAngle) % 360;
	nEnd = (-startAngle - arcAngle) % 360;
	if (nStart < 0)
		nStart += 360;
	if (nEnd < 0) 
		nEnd += 360;

	/* We only need to calculate the sine/cosine of the starting and */
	/* end angles, as we use vector cross-products to check whether the */
	/* pixel we're going to paint is inside the arc or not (much better */
	/* performance than doing these per angle step, and also removes the */
	/* need for atan2() entirely!), while also being easier to read. */
	if (!isFullCircle)
	{
		cosS = sjme_fixed_cos(sjme_fixed_degToRad(sjme_fixed_hi(nStart)));
		sinS = sjme_fixed_sin(sjme_fixed_degToRad(sjme_fixed_hi(nStart)));
		cosE = sjme_fixed_cos(sjme_fixed_degToRad(sjme_fixed_hi(nEnd)));
		sinE = sjme_fixed_sin(sjme_fixed_degToRad(sjme_fixed_hi(nEnd)));
	}

	/* Arcs may be either convex or concave, thus we need to adapt */
	/* drawing accordingly. Convex needs pixels to be after the start AND */
	/* before the end cross-products, while concave has pixel sweeps going */
	/* past the arc boundaries, thus the pixels must be after the start OR */
	/* before the end cross-products. */
	isConcave = (sjme_abs(arcAngle) > 180);

	/* Minor error margin for vector cross-product checks, can't be too */
	/* strict here or we get arcs that are slightly misaligned because */
	/* the product causes pixels to evaluate as out of bounds. This one */
	/* results in 128 in Q16.16 and seems to be the best on a wide range */
	/* of angles. */
	margin = SJME_FIXED_FRAC_1_512;

	/* Draw the arc. */
	error = SJME_ERROR_NONE;
	drawPixel = g->prim.drawPixel;
	for (py = startY; py < endY; py++)
	{
		dy = centerY - py;
		normY = sjme_fixed_div(dy, radiusY > 0 ? radiusY : 1);
		normY2 = sjme_fixed_mul(normY, normY);

		if (normY2 >= SJME_FIXED_ONE)
			continue;

		/* Find out the arc's boundaries for the current scanline. */
		maxDx = sjme_fixed_int(radiusX * sjme_fixed_sqrt(SJME_FIXED_ONE -
			normY2));
		lineStartX = sjme_max(startX, centerX - maxDx);
		lineEndX = sjme_min(endX, centerX + maxDx + 1);

		/* Pre-scale normDy for 2D cross-product scanline checks in X loop, */
		/* otherwise we'd waste cycles doing this per-pixel. */
		if (!isFullCircle)
		{
			crossYStart = sjme_fixed_mul(normY, cosS);
			crossYEnd = sjme_fixed_mul(normY, cosE);
		}

		for (px = lineStartX; px < lineEndX; px++)
		{
			/* Not a full circle? Then we need to verify if this pixel */
			/* is within the arc's boundaries for this scanline. */
			if (!isFullCircle)
			{
				dx = px - centerX;
				normDx = sjme_fixed_div(dx, radiusX > 0 ? 
					radiusX : 1);

				crossStart = sjme_fixed_mul(normDx, sinS) + crossYStart;
				crossEnd = sjme_fixed_mul(normDx, sinE) + crossYEnd;

				if (!isConcave)
				{
					/* For arcs <= 180 degrees, the pixel must be on the */
					/* correct side of both boundary rays, otherwise we'll */
					/* have the whole quadrant drawn at the opposite end. */
					if (arcAngle > 0)
						inSector = ((crossStart >= -margin) &&
							(crossEnd <= margin));
					else
						inSector = ((crossStart <= margin) &&
							(crossEnd >= -margin));
				}
				else
				{
					/* For arcs > 180 degrees (concave), similar idea as */
					/* above when <= 180 degrees. */
					if (arcAngle > 0)
						inSector = ((crossStart >= -margin) ||
							(crossEnd <= margin));
					else
						inSector = ((crossStart <= margin) ||
							(crossEnd >= -margin));
				}

				if (!inSector) 
					continue;
			}

			error |= drawPixel(g, px, py);
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
