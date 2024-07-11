/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include <string.h>

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"

static sjme_errorCode sjme_scritchui_core_lock(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	sjme_scritchui_pencilLockState* state;
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try locking. */
	if (g->lock != NULL)
	{
		/* Not implemented? */
		if (g->lock->lock == NULL || g->lock->lockRelease == NULL)
			return SJME_ERROR_NOT_IMPLEMENTED;
		
		/* Restore state. */
		state = &g->lockState;
		
		/* Forward if locking is needed. */
		if (sjme_atomic_sjme_jint_getAdd(&state->count, 1) == 0)
			if (sjme_error_is(error = g->lock->lock(g)))
				return sjme_error_default(error);
	}
	
	/* Nothing to do! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_lockRelease(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	sjme_scritchui_pencilLockState* state;
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try locking. */
	if (g->lock != NULL)
	{
		/* Not implemented? */
		if (g->lock->lock == NULL || g->lock->lockRelease == NULL)
			return SJME_ERROR_NOT_IMPLEMENTED;
		
		/* Restore state. */
		state = &g->lockState;
		
		/* Already released? Do nothing... */
		if (sjme_atomic_sjme_jint_get(&state->count) == 0)
			return SJME_ERROR_NONE;
		
		/* Forward if release is needed. */
		if (sjme_atomic_sjme_jint_getAdd(&state->count, -1) == 1)
			if (sjme_error_is(error = g->lock->lockRelease(g)))
				return sjme_error_default(error);
	}
	
	/* Nothing to do! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_corePrim_drawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	sjme_errorCode error;
	sjme_scritchui_rect* rect;
	sjme_jint numBytes, ex;
	void* outRaw;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Off top or bottom? */
	rect = &g->state.clip;
	if (y < 0 || y < rect->y ||
		y >= g->height || y >= rect->y + rect->height)
		return SJME_ERROR_NONE;
	
	/* Normalize horizontal coordinates. */
	ex = x + w;
	
	if (x < 0)
		x = 0;
	if (ex >= g->width)
		ex = g->width;
	if (ex >= rect->x + rect->width)
		ex = rect->x + rect->width;
	
	w = ex - x;
	
	/* Not visible? */
	if (w <= 0 || ex < 0 || x >= g->width || x >= rect->x + rect->width)
		return SJME_ERROR_NONE;
	
	/* Setup raw pixel buffer. */
	numBytes = g->bytesPerPixel * w;
	outRaw = sjme_alloca(numBytes);
	if (outRaw == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	
	/* Map it. */
	memset(outRaw, 0, numBytes);
	if (sjme_error_is(error = g->prim.rawScanFill(g,
		outRaw, 0, numBytes,
		g->state.color.v, w)))
		return sjme_error_default(error);
	
	/* Render line. */
	return g->prim.rawScanPut(g, x, y,
		outRaw, numBytes, w);
}

static sjme_errorCode sjme_scritchui_corePrim_drawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return SJME_ERROR_NONE;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_corePrim_drawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Use horizontal line drawing. */
	return g->prim.drawHoriz(g, x, y, 1);
}
	
static sjme_errorCode sjme_scritchui_corePrim_mapColorFromRGB(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb,
	sjme_attrOutNotNull sjme_scritchui_pencilColor* outColor)
{
	sjme_jint v, aa, rr, gg, bb, ii;
	sjme_jint i, numCol, d, bestCol, bestColScore, thisColScore;
	sjme_jint pargb, prr, pgg, pbb;
	sjme_jint mrr, mgg, mbb;
	const sjme_jint* colors;

	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Set base color properties. */
	aa = (argb >> 24) & 0xFF;
	outColor->a = aa;
	rr = (argb >> 16) & 0xFF;
	outColor->r = rr;
	gg = (argb >> 8) & 0xFF;
	outColor->g = gg;
	bb = (argb) & 0xFF;
	outColor->b = bb;
	
	/* Find closest indexed color. */
	ii = -1;
	numCol = g->palette.numColors;
	if (g->palette.colors != NULL && numCol > 0)
	{
		/* Clear alpha. */
		d = argb & 0xFFFFFF;
		
		/* Determine the most important color channel. */
		mrr = (rr >= gg && rr >= bb ? 1 : 2);
		mgg = (gg >= rr && gg >= bb ? 1 : 2);
		mbb = (bb >= rr && bb >= gg ? 1 : 2);
		
		/* Start with a horrible color score. */
		bestCol = 0;
		bestColScore = 134217728;
		
		/* Find exact color match? */
		colors = g->palette.colors;
		for (i = 0; i < numCol; i++)
		{
			/* Exact match? */
			pargb = colors[i] & 0xFFFFFF;
			if (d == pargb)
			{
				ii = i;
				break;
			}
			
			/* Get original RGB value. */
			prr = (pargb >> 16) & 0xFF;
			pgg = (pargb >> 8) & 0xFF;
			pbb = (pargb) & 0xFF;
			
			/* Calculate this color score, use if it is better. */
			thisColScore = (abs(prr - rr) * mrr) +
				(abs(pgg - gg) * mgg) +
				(abs(pbb - bb) * mbb);
			if (thisColScore < bestColScore)
			{
				bestCol = i;
				bestColScore = thisColScore;
			}
		}
		
		/* If no exact color was found, use the best scoring one. */ 
		if (ii < 0)
			ii = bestCol;
	}
	
	/* Determine raw pixel color. */
	switch (g->pixelFormat)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
			v = argb;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
			v = (bb << 24) | (gg << 16) | (rr << 8) | aa;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
			v = (bb << 24) | (gg << 16) | (rr << 8);
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_XBGR8888:
			v = (bb << 16) | (gg << 8) | (rr);
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
			v = argb & 0x00FFFFFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
			v = (((aa >> 4) & 0xF) << 12) |
				(((rr >> 4) & 0xF) << 8) |
				(((gg >> 4) & 0xF) << 4) |
				((bb >> 4) & 0xF);
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
			v = (((rr >> 3) & 0x1F) << 11) |
				(((gg >> 2) & 0x3F) << 5) |
				((bb >> 3) & 0x1F);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
			v = (((rr >> 3) & 0x1F) << 10) |
				(((gg >> 3) & 0x1F) << 5) |
				((bb >> 3) & 0x1F);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
			v = (((aa >> 7) & 0x1) << 15) |
				(((bb >> 3) & 0x1F) << 10) |
				(((gg >> 3) & 0x1F) << 5) |
				((rr >> 3) & 0x1F);
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			v = ii & 0xFFFF;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
			v = ii & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
			v = ii & 0xF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
			v = ii & 0x3;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
			v = ii & 0x1;
			break;
	}
		
	/* Store raw colors. */
	outColor->i = ii;
	outColor->v = v;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_corePrim_mapColorFromRaw(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint v,
	sjme_attrOutNotNull sjme_scritchui_pencilColor* outColor)
{
	sjme_jint numCol, aa, rr, gg, bb;
	
	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If there is a palette, try using it to get a color. */
	numCol = g->palette.numColors;
	if (g->palette.colors != NULL && numCol > 0)
	{
		if (v > 0 && v < numCol)
			return sjme_scritchui_corePrim_mapColorFromRGB(g,
				g->palette.colors[v], outColor);
		
		/* Invalid color, render to black or close to it. */
		return sjme_scritchui_corePrim_mapColorFromRGB(g,
			0, outColor);
	}
	
	/* Initial map to black. */
	aa = 0;
	rr = 0;
	gg = 0;
	bb = 0;
	
	/* Recover raw pixel color. */
	switch (g->pixelFormat)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
			aa = (v >> 24) & 0xFF;
			rr = (v >> 16) & 0xFF;
			gg = (v >> 8) & 0xFF;
			bb = (v) & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
			aa = (v) & 0xFF;
			rr = (v >> 8) & 0xFF;
			gg = (v >> 16) & 0xFF;
			bb = (v >> 24) & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
			aa = 0xFF;
			rr = (v >> 8) & 0xFF;
			gg = (v >> 16) & 0xFF;
			bb = (v >> 24) & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_XBGR8888:
			aa = 0xFF;
			rr = (v) & 0xFF;
			gg = (v >> 8) & 0xFF;
			bb = (v >> 16) & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
			aa = 0xFF;
			rr = (v >> 16) & 0xFF;
			gg = (v >> 8) & 0xFF;
			bb = (v) & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
			sjme_todo("Impl?");
			v = (((aa >> 4) & 0xF) << 12) |
				(((rr >> 4) & 0xF) << 8) |
				(((gg >> 4) & 0xF) << 4) |
				((bb >> 4) & 0xF);
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
			sjme_todo("Impl?");
			v = (((rr >> 3) & 0x1F) << 11) |
				(((gg >> 2) & 0x3F) << 5) |
				((bb >> 3) & 0x1F);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
			sjme_todo("Impl?");
			v = (((rr >> 3) & 0x1F) << 10) |
				(((gg >> 3) & 0x1F) << 5) |
				((bb >> 3) & 0x1F);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
			sjme_todo("Impl?");
			v = (((aa >> 7) & 0x1) << 15) |
				(((bb >> 3) & 0x1F) << 10) |
				(((gg >> 3) & 0x1F) << 5) |
				((rr >> 3) & 0x1F);
			break;
	}
	
	/* Map back to normalize. */
	return sjme_scritchui_corePrim_mapColorFromRGB(g,
		(aa << 24) | (rr << 16) | (gg << 8) | bb, outColor);
}

static sjme_errorCode sjme_scritchui_corePrim_mapColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_pencilColor* outColor)
{
	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (fromRaw)
		return sjme_scritchui_corePrim_mapColorFromRaw(g,
			inRgbOrRaw, outColor);
	return sjme_scritchui_corePrim_mapColorFromRGB(g,
		inRgbOrRaw, outColor);
}

static sjme_errorCode sjme_scritchui_corePrim_rawScanFillInt(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_jint limit, i;
	sjme_jint* p;
	
	if (g == NULL || outRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outRawOff < 0 || outRawLen < 0 || (outRawOff + outRawLen) < 0 ||
		(inNumPixels < 0))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine number of pixels to actually draw. */
	limit = inNumPixels * g->bytesPerPixel;
	
	if (limit < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (outRawLen < limit)
		limit = outRawLen;
	
	/* Fill in. */
	p = SJME_POINTER_OFFSET(outRaw, outRawOff);
	for (i = 0; i < limit; i += 4)
		*(p++) = rawPixel;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_corePrim_rawScanFillShort(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_jint limit, i;
	sjme_jshort* p;
	
	if (g == NULL || outRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outRawOff < 0 || outRawLen < 0 || (outRawOff + outRawLen) < 0 ||
		(inNumPixels < 0))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine number of pixels to actually draw. */
	limit = inNumPixels * g->bytesPerPixel;
	
	if (limit < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (outRawLen < limit)
		limit = outRawLen;
	
	/* Fill in. */
	p = SJME_POINTER_OFFSET(outRaw, outRawOff);
	for (i = 0; i < limit; i += 2)
		*(p++) = rawPixel;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_corePrim_rawScanFillByte(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInValue sjme_jint rawPixel,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	sjme_jint limit, i;
	sjme_jbyte* p;
	
	if (g == NULL || outRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outRawOff < 0 || outRawLen < 0 || (outRawOff + outRawLen) < 0 ||
		(inNumPixels < 0))
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine number of pixels to actually draw. */
	limit = inNumPixels * g->bytesPerPixel;
	
	if (limit < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (outRawLen < limit)
		limit = outRawLen;
	
	/* Fill in. */
	p = SJME_POINTER_OFFSET(outRaw, outRawOff);
	for (i = 0; i < limit; i += 1)
		*(p++) = rawPixel;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_corePrim_rawScanGet(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint inX,
	sjme_attrInPositive sjme_jint inY,
	sjme_attrOutNotNullBuf(inLen) void* outData,
	sjme_attrInPositiveNonZero sjme_jint inDataLen,
	sjme_attrInPositiveNonZero sjme_jint inNumPixels)
{
	if (g == NULL || outData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Do nothing, there is no reading function. */
	return SJME_ERROR_NONE;
}

/**
 * Transforms coordinates.
 * 
 * @param g The graphics to transform via. 
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @since 2024/05/17
 */
static void sjme_scritchui_core_transform(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInOutNotNull sjme_jint* x,
	sjme_attrInOutNotNull sjme_jint* y)
{
	(*x) += g->state.translate.x;
	(*y) += g->state.translate.y;
}

static sjme_errorCode sjme_scritchui_core_translateRotateScale(
	sjme_attrOutNotNull sjme_scritchui_pencilMatrix* outMatrix,
	sjme_attrInValue sjme_scritchui_pencilTranslate inTrans,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest)
{
	sjme_scritchui_pencilMatrix result;
	sjme_fixed scaleX, scaleY, temp;
	sjme_jint xform;
	
	if (outMatrix == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initialize. */
	memset(&result, 0, sizeof(result));
	
	/* Perform total image scaling first. */
	scaleX = sjme_fixed_fraction(wSrc, wDest);
	scaleY = sjme_fixed_fraction(hSrc, hDest);
	
	/* This is simple enough to calculate, is just the destination. */
	result.tw = wDest;
	result.th = hDest;
	
	/* Determine the transformation functions to use. There are just three */
	/* primitive transformation functions: flip horizontally, then */
	/* flip vertically, then rotate 90 degrees clockwise. This handles */
	/* every transformation which fill every single bit. */
	switch (inTrans)
	{
		/* These bits represent the stuff to do! == 0b9VH; */
		case SJME_SCRITCHUI_TRANS_NONE:				xform = 0b000; break;
		case SJME_SCRITCHUI_TRANS_MIRROR:			xform = 0b001; break;
		case SJME_SCRITCHUI_TRANS_MIRROR_ROT180:	xform = 0b010; break;
		case SJME_SCRITCHUI_TRANS_ROT180:			xform = 0b011; break;
		case SJME_SCRITCHUI_TRANS_ROT90:			xform = 0b100; break;
		case SJME_SCRITCHUI_TRANS_MIRROR_ROT90:		xform = 0b101; break;
		case SJME_SCRITCHUI_TRANS_MIRROR_ROT270:	xform = 0b110; break;
		case SJME_SCRITCHUI_TRANS_ROT270:			xform = 0b111; break;
		/* These bits represent the stuff to do! == 0b9VH; */
	}
	
	/* Start with this. */
	result.x.wx = scaleX;
	result.y.zy = scaleY;
	
	/* Mirror horizontally? */
	if ((xform & 0b001) != 0)
		result.x.wx = -result.x.wx;
		
	/* Mirror vertically? */
	if ((xform & 0b010) != 0)
		result.y.zy = -result.y.zy;
		
	/* Rotate 90 degrees clockwise */
	/* Thanks to jercos for helping out with the matrix math! */
	/* The math here has been simplified to remove constants and otherwise. */
	if ((xform & 0b100) != 0)
	{
		temp = result.x.wx;
		result.x.wx = result.x.zy;
		result.x.zy = -temp;
		
		temp = result.y.wx;
		result.y.wx = result.y.zy;
		result.y.zy = -temp;
	}
	
	/* Success! */
	memmove(outMatrix, &result, sizeof(result));
	return SJME_ERROR_NONE;
}

/**
 * Calculates the anchor position of a box on a point.
 * 
 * @param anchor The anchor point to use.
 * @param x The X coordinate. 
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @param baseline The baseline, if this is a font.
 * @param outX The resultant X coordinate.
 * @param outY The resultant Y coordinate.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
static sjme_errorCode sjme_scritchui_core_anchor(
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint baseline,
	sjme_attrOutNotNull sjme_jint* outX,
	sjme_attrOutNotNull sjme_jint* outY)
{
	if (outX == NULL || outY == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cannot be negative. */
	if (w < 0 || h < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Horizontal anchoring. */
	if ((anchor & SJME_SCRITCHUI_ANCHOR_LEFT) == 0)
	{
		if ((anchor & SJME_SCRITCHUI_ANCHOR_HCENTER) != 0)
			x -= w / 2;
		else if ((anchor & SJME_SCRITCHUI_ANCHOR_RIGHT) != 0)
			x -= w;
	}
	
	/* Vertical anchoring. */
	if ((anchor & SJME_SCRITCHUI_ANCHOR_TOP) == 0)
	{
		if ((anchor & SJME_SCRITCHUI_ANCHOR_VCENTER) != 0)
			y -= h / 2;
		else if ((anchor & SJME_SCRITCHUI_ANCHOR_BOTTOM) != 0)
			y -= h;
		else if ((anchor & SJME_SCRITCHUI_ANCHOR_BASELINE) != 0)
			y -= baseline;
	}
	
	/* Success! */
	*outX = x;
	*outY = y;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_pencilBlendRGBInto(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean srcAlpha,
	sjme_attrInNotNullBuf(numPixels) sjme_jint* dest,
	sjme_attrInNotNullBuf(numPixels) sjme_jint* src,
	sjme_attrInPositive sjme_jint numPixels)
{
	sjme_jint i;
	
	if (g == NULL || dest == NULL || src == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (numPixels < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Blend each pixel individually. */
	/* R(dest) = (R(src) * A(src)) + (R(dest) * (1 - A(src))) */
	/* G(dest) = (G(src) * A(src)) + (G(dest) * (1 - A(src))) */
	/* B(dest) = (B(src) * A(src)) + (B(dest) * (1 - A(src))) */
	/* A(dest) = A(src) + A(dest) - (A(src) * A(dest)) */
	for (i = 0; i < numPixels; i++)
	{
		/* From my existing Java code... */
		/* int pac = __b[src]; */
		/* int sa = pac >>> 24; */
		/* int na = (sa ^ 0xFF); */
		/* int srb = ((pac & 0xFF00FF) * sa); */
		/* int sgg = (((pac >>> 8) & 0xFF) * sa); */
		/* int dcc = data[dp]; */
		/* int xrb = (srb + ((dcc & 0xFF00FF) * na)) >>> 8; */
		/* int xgg = (((sgg + (((dcc >>> 8) & 0xFF) * na)) + 1) *  ... */
		/* 	257) >>> 16; */
		/*  */
		/* data[dp] = ((xrb & 0xFF00FF) | ((xgg & 0xFF) << 8)); */
	
		sjme_todo("Impl?");
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_pencilCopyArea(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	return SJME_ERROR_NOT_IMPLEMENTED;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawChar(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint c,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrOutNullable sjme_jint* outCw)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont font;
	sjme_jint cw, ch, area, dx, dy, sx, sy, v, scanLen;
	sjme_jint offX, offY;
	sjme_jubyte* bitmap;
	sjme_scritchui_pencilBitLineFunc bitline;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We need the font for rendering. */
	font = g->state.font;
	if (font == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Need character width. */
	cw = 0;
	if (sjme_error_is(error = font->api->pixelCharWidth(
		font, c, &cw)))
		return sjme_error_default(error);
	
	/* Do not draw space characters. */
	if (c == '\t' || c == ' ')
	{
		/* Do report their length however! */
		if (outCw != NULL)
			*outCw = cw;
		
		return SJME_ERROR_NONE;
	}
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	/* And the pixel height, since this is a bitmap font. */
	ch = 0;
	if (sjme_error_is(error = font->api->metricPixelSize(
		font, &ch)))
		goto fail_any;
	
	/** Do not bother drawing nothing. */
	if (cw == 0 || ch == 0)
		return SJME_ERROR_NONE;
	
	/* Calculate anchor point accordingly. */
	if (anchor != 0)
		if (sjme_error_is(error = sjme_scritchui_core_anchor(anchor,
			x, y, cw, ch, 0, &x, &y)))
			goto fail_any;
		
	/* Determine scanline length for each bitmap row. */
	scanLen = sjme_scritchui_pencilFontScanLen(cw);
	
	/* Allocate bitmap. */
	area = sizeof(*bitmap) * (scanLen * ch);
	bitmap = sjme_alloca(area);
	if (bitmap == NULL)
	{
		error = SJME_ERROR_OUT_OF_MEMORY;
		goto fail_any;
	}
	
	/* Initialize. */
	memset(bitmap, 0, area);
	
	/* Offsets for proper glyph drawing. */
	offX = 0;
	offY = 0;
	
	/* Get glyph bitmap. */
	if (sjme_error_is(error = font->api->renderBitmap(font,
		c, bitmap, 0, scanLen,
		ch, &offX, &offY)))
		goto fail_any;
	
	/* Draw bit-lines for the glyphs. */
	for (sy = 0, dy = y + offY, v = 0; sy < ch; sy++, dy++)
		for (sx = 0, dx = x + offX; sx < scanLen; sx++, dx += 8, v++)
		{
			/* Which bitline to use? */
			bitline = sjme_scritchui_pencilBitLines[bitmap[v]];
			
			/* Render the bitline. */
			if (sjme_error_is(error = bitline(g, dx, dy)))
				goto fail_any;
		}
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	if (outCw != NULL)
		*outCw = cw;
		
	return SJME_ERROR_NONE;

fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawChars(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return SJME_ERROR_NOT_IMPLEMENTED;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x, &y);
	
	/* Use primitive. */
	if (sjme_error_is(error = g->prim.drawHoriz(g, x, y, w)))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x1, &y1);
	sjme_scritchui_core_transform(g, &x2, &y2);
	
	/* Use primitive. */
	if (sjme_error_is(error = g->prim.drawLine(g, x1, y1, x2, y2)))
		goto fail_any;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x, &y);
	
	/* Use primitive. */
	if (sjme_error_is(error = g->prim.drawPixel(g, x, y)))
		goto fail_any;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawRect(
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
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x, &y);
	
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
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawSubstring(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_charSeq* s,
	sjme_attrInPositive sjme_jint o, 
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_errorCode error;
	sjme_jint seqLen, at, dx, dy, bx, lineHeight, tw, cw, baseline;
	sjme_jchar c;
	sjme_scritchui_pencilFont font;
	
	if (g == NULL || s == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (o < 0 || l < 0 || (o + l) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* We need the font for rendering. */
	font = g->state.font;
	if (font == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	/* Need to get the height of a line. */
	lineHeight = -1;
	if (sjme_error_is(error = font->api->metricPixelHeight(font,
		&lineHeight)) || lineHeight < 0)
	{
		error = sjme_error_defaultOr(error,
			SJME_ERROR_FONT_NEGATIVE_HEIGHT);
		goto fail_fontHeight;
	}
	
	/* Need the font baseline. */
	baseline = 0;
	if (sjme_error_is(error = font->api->metricPixelBaseline(
		font, &baseline)))
		goto fail_fontBaseline;
	
	/* Get sequence length for further checking. */
	seqLen = -1;
	if (sjme_error_is(error = sjme_charSeq_length(s,
		&seqLen)) || seqLen < 0)
		goto fail_seqLen;
	
	/* Out of bounds? */
	if ((o + l) > seqLen)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Determine visual size of this block of text. */
	tw = -1;
	if (sjme_error_is(error = font->api->stringWidth(font,
		s, o, l, &tw)) || tw < 0)
		goto fail_blockDim;
	
	/* Determine anchor point of this block of text. */
	dx = x;
	dy = y;
	if (anchor != 0 && sjme_error_is(error = sjme_scritchui_core_anchor(
		anchor & SJME_SCRITCHUI_ANCHOR_TEXT_MASK, x, y,
		tw, lineHeight, baseline, &dx, &dy)))
		goto fail_anchor;
	
	/* Base X is the initial starting coordinate. */
	bx = dx;

	/* Draw each character in the string. */
	for (at = 0; at < l; at++)
	{
		/* Get the next character to render. */
		c = 0;
		if (sjme_error_is(error = sjme_charSeq_charAt(
			s, o + at, &c)))
			goto fail_charAt;
		
		/* Reset to start of line or next line? */
		if (c == '\r' || c == '\n')
		{
			/* Go to next line? */
			if (c == '\n')
				dy += lineHeight;
			
			/* Reset to line base. */
			dx = bx;
			continue;
		}
		
		/* Render character, note always at top+left anchor. */
		cw = 0;
		if (sjme_error_is(error = g->api->drawChar(g, c, dx, dy,
			0, &cw)))
			goto fail_drawChar;
		
		/* Move over. */
		dx += cw;
	}
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_drawChar:
fail_charAt:
fail_anchor:
fail_blockDim:
fail_seqLen:
fail_fontBaseline:
fail_fontHeight:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x1, &y1);
	sjme_scritchui_core_transform(g, &x2, &y2);
	sjme_scritchui_core_transform(g, &x3, &y3);
	
	/* Clear error state. */
	error = SJME_ERROR_NONE;
	
	/* Draw lines via primitives. */
	error |= g->prim.drawLine(g, x1, y1, x2, y2);
	error |= g->prim.drawLine(g, x2, y2, x3, y3);
	error |= g->prim.drawLine(g, x3, y3, x1, y1);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawXRGB32Region(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jint* data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint dataLen,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight)
{
	sjme_errorCode error;
	sjme_scritchui_pencilMatrix m;
	sjme_fixed wx, zy, wxBase, zyMajor;
	sjme_jint dx, dy, iwx, izy, at, q;
	sjme_jint* flatRgb;
	sjme_jint* srcRgb;
	void* rawScan;
	void* srcRaw;
	sjme_jint rawScanBytes, flatRgbBytes;
	sjme_jboolean srcAlpha;
	
	if (g == NULL || data == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The source rectangle must always be in bounds. */
	if (xSrc < 0 || ySrc < 0 || wSrc <= 0 || hSrc <= 0 ||
		(xSrc + wSrc) > origImgWidth || (ySrc + hSrc) > origImgHeight ||
		(xSrc + wSrc) < 0 || (ySrc + hSrc) < 0 ||
		origImgWidth < 0 || origImgHeight < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (off < 0 || dataLen < 0 || (off + dataLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Drawing nothing? */
	if (wDest <= 0 || hDest <= 0)
		return SJME_ERROR_NONE;
	
	/* Apply alpha? */
	srcAlpha = g->hasAlpha;
	alpha = (alpha && g->impl->rawScanGet != NULL);
	
	/* Transform. */
	sjme_scritchui_core_transform(g, &xDest, &yDest);
	
	/* If there is native drawing, use that as it will likely be faster */
	/* or more efficient for the API. */
	if (g->impl->drawXRGB32Region != NULL)
	{
		/* Anchor to target coordinates. */
		if (sjme_error_is(error = sjme_scritchui_core_anchor(anchor,
			xDest, yDest, wSrc, hSrc, 0,
			&xDest, &yDest)))
			return sjme_error_default(error);
		
		/* Lock. */
		if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
			return sjme_error_default(error);
			
		/* Forward, note that this is pre-anchored. */
		if (sjme_error_is(error = g->impl->drawXRGB32Region(
			g, data, off, dataLen, scanLen, alpha, xSrc, ySrc,
			wSrc, hSrc, trans, xDest, yDest, 0, wDest, hDest,
			origImgWidth, origImgHeight)))
			goto fail_any;
		
		/* Release lock. */
		if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
			return sjme_error_default(error);
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* We are now doing the transforming and drawing ourselves. */
	/* Calculate transformation matrix. */
	memset(&m, 0, sizeof(m));
	if (sjme_error_is(error = sjme_scritchui_core_translateRotateScale(
		&m, trans, wSrc, hSrc, wDest, hDest)))
		return sjme_error_default(error);
	
	/* Anchor to target coordinates after scaling, because we need */
	/* to know what our target scale is. */
	if (sjme_error_is(error = sjme_scritchui_core_anchor(anchor,
		xDest, yDest, m.tw, m.th, 0,
		&xDest, &yDest)))
		return sjme_error_default(error);
	
	/* Determine how large our raw scan buffer actually is. */
	rawScanBytes = -1;
	if (sjme_error_is(error = g->api->mapRawScanBytes(g,
		m.tw, &rawScanBytes)) || rawScanBytes < 0)
		return sjme_error_default(error);
	
	/* RGB buffer is this many bytes. */
	flatRgbBytes = m.tw * sizeof(*flatRgb);
	
	/* Setup input and output RGB buffers. */
	rawScan = sjme_alloca(rawScanBytes);
	flatRgb = sjme_alloca(flatRgbBytes);
	srcRgb = (alpha ? sjme_alloca(flatRgbBytes) : NULL);
	srcRaw = (alpha ? sjme_alloca(rawScanBytes) : NULL);
	if (rawScan == NULL || flatRgb == NULL ||
		(alpha && (srcRgb == NULL || srcRaw == NULL)))
		return sjme_error_defaultOr(error,
			SJME_ERROR_OUT_OF_MEMORY);
	
	/* Clear buffers. */
	memset(rawScan, 0, rawScanBytes);
	memset(flatRgb, 0, flatRgbBytes);
	
	if (alpha)
	{
		memset(srcRaw, 0, rawScanBytes);
		memset(srcRgb, 0, flatRgbBytes);
	}
	
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	/* Figure out the position of our base pointer. */
	/* Matrix multiplication? Squeak? */
	wxBase = sjme_fixed_mul(sjme_fixed_hi(xSrc), m.x.wx) +
		sjme_fixed_mul(sjme_fixed_hi(ySrc), m.x.zy);
	wx = wxBase;
	zyMajor = sjme_fixed_mul(sjme_fixed_hi(xSrc), m.y.wx) +
		sjme_fixed_mul(sjme_fixed_hi(ySrc), m.y.zy);
	zy = zyMajor;
	
	/* Scan copy, rotate, and stretch by destination scans. */
	for (dy = 0; dy < m.th; dy++, wxBase += m.y.wx, zyMajor += m.y.zy)
	{
		/* Reset wx to base for start of scan. */
		wx = wxBase;
		zy = zyMajor;
		
		/* Scan in RGB line. */
		for (dx = 0; dx < m.tw; dx++, wx += m.x.wx, zy += m.x.zy)
		{
			/* Get pixel from source buffer. */
			iwx = sjme_fixed_int(sjme_fixed_round(wx)) % wSrc;
			izy = sjme_fixed_int(sjme_fixed_round(zy)) % hSrc;
			
			/* Keep in bounds. */
			if (iwx < 0)
				iwx += (wSrc - 1);
			if (izy < 0)
				izy += (hSrc - 1);
			
			/* Copy pixel from source? */
			at = off + ((izy * scanLen) + iwx);
			flatRgb[dx] = data[at];
		}
		
		/* Perform alpha blending? */
		if (alpha &&
			g->state.blending == SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER)
		{
			/* Get raw scanline data. */
			if (sjme_error_is(error = g->prim.rawScanGet(g,
				xDest, yDest + dy,
				srcRaw, rawScanBytes, m.tw)))
				goto fail_any;
			
			/* Map from Native to RGB. */
			if (sjme_error_is(error = g->api->mapRGBFromRawScan(
				g, srcRgb, 0, m.tw,
				srcRaw, 0, rawScanBytes)))
				goto fail_any;
			
			/* Blend? */
			if (sjme_error_is(error = g->api->blendRGBInto(
				g, srcAlpha, flatRgb, srcRgb, m.tw)))
				goto fail_any;
		}
		
		/* Map RGB to Native. */
		if (sjme_error_is(error = g->api->mapRawScanFromRGB(
			g, rawScan, 0, rawScanBytes,
			flatRgb, 0, m.tw)))
			goto fail_any;
		
		/* Render RGB line at destination. */
		if (sjme_error_is(error = g->prim.rawScanPut(g,
			xDest, yDest + dy,
			rawScan, rawScanBytes, m.tw)))
			goto fail_any;
	}
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
		
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilFillRect(
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
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	/* Transform. */
	sjme_scritchui_core_transform(g, &x, &y);
	
	/* Cap width and height to 1 always. */
	if (w <= 0)
		w = 1;
	if (h <= 0)
		h = 1;
	
	/* Natively supported? */
	if (g->impl->fillRect != NULL)
		if (sjme_error_is(error = g->impl->fillRect(g, x, y, w, h)))
			goto fail_any;
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	drawHoriz = g->prim.drawHoriz;
	for (yz = y, yze = y + h; yz < yze; yz++)
		error |= drawHoriz(g, x, yz, w);
	
	/* Failed? */
	if (sjme_error_is(error))
		goto fail_any;
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success? */
	return error;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilFillTriangle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lock(g)))
		return sjme_error_default(error);
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
		
	return SJME_ERROR_NOT_IMPLEMENTED;
	
fail_any:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchui_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_scritchui_core_pencilMapColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_pencilColor* outColor)
{
	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Use primitive function. */
	return g->prim.mapColor(g, fromRaw, inRgbOrRaw, outColor);
}

static sjme_errorCode sjme_scritchui_core_pencilMapRawScanBytes(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositiveNonZero sjme_jint inPixels,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outBytes)
{
	sjme_jint result;
	
	if (g == NULL || outBytes == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inPixels < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Depends on the pixel format. */
	result = -1;
	switch (g->pixelFormat)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
		case SJME_GFX_PIXEL_FORMAT_INT_XBGR8888:
			result = inPixels * 4;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			result = inPixels * 2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
			result = inPixels;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
			result = (inPixels >> 1) + (inPixels & 1);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
			result = (inPixels >> 2) + ((inPixels >> 1) & 1);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
			result = (inPixels >> 3) + ((inPixels >> 2) & 1);
			break;
	}
	
	/* Make sure what was calculated did not overflow. */
	if (result < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Success! */
	*outBytes = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_pencilMapRawScanFromRGB(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrOutNotNullBuf(rawLen) void* outRaw,
	sjme_attrInPositive sjme_jint outRawOff,
	sjme_attrInPositive sjme_jint outRawLen,
	sjme_attrInNotNullBuf(rgbLen) sjme_jint* inRgb,
	sjme_attrInPositive sjme_jint inRgbOff,
	sjme_attrInPositive sjme_jint inRgbLen)
{
	sjme_errorCode error;
	sjme_jint byteLimit, inRgbOffRaw, inRgbLenRaw;
	
	if (g == NULL || outRaw == NULL || inRgb == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	inRgbOffRaw = inRgbOff * 4;
	if (outRawOff < 0 || outRawLen < 0 || (outRawOff + outRawLen) < 0 ||
		inRgbOff < 0 || inRgbLen < 0 || (inRgbOff + inRgbLen) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Double check RGB count. */
	inRgbLenRaw = -1;
	if (sjme_error_is(error = g->api->mapRawScanBytes(g,
		inRgbLen, &inRgbLenRaw)) || inRgbLenRaw < 0)
		return sjme_error_default(error);
	
	/* Use the smaller of the two. */
	if (inRgbLenRaw < outRawLen)
		byteLimit = inRgbLenRaw;
	else
		byteLimit = outRawLen;
	
	/* Optimal format for direct copy? */
	if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 ||
		g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_RGB888)
	{
		/* Copy over efficiently. */
		memmove(SJME_POINTER_OFFSET(outRaw, outRawOff),
			SJME_POINTER_OFFSET(inRgb, inRgbOffRaw),
			byteLimit);
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Simple byte swap. */
	else if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_BGRA8888 ||
		g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_BGRX8888)
	{
		return sjme_swap_uint_memmove(
			SJME_POINTER_OFFSET(outRaw, outRawOff),
			SJME_POINTER_OFFSET(inRgb, inRgbOffRaw),
			byteLimit);
	}
	
	/* Shift up by 8. */
	else if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_XBGR8888)
	{
		return sjme_swap_shu8_uint_memmove(
			SJME_POINTER_OFFSET(outRaw, outRawOff),
			SJME_POINTER_OFFSET(inRgb, inRgbOffRaw),
			byteLimit);
	}
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_core_pencilMapRGBFromRawScan(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNullBuf(rgbLen) sjme_jint* outRgb,
	sjme_attrInPositive sjme_jint outRgbOff,
	sjme_attrInPositive sjme_jint outRgbLen,
	sjme_attrOutNotNullBuf(rawLen) void* inRaw,
	sjme_attrInPositive sjme_jint inRawOff,
	sjme_attrInPositive sjme_jint inRawLen)
{
	sjme_errorCode error;
	sjme_jint byteLimit, outRgbOffRaw, outRgbLenRaw;
	
	if (g == NULL || outRgb == NULL || inRaw == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	outRgbOffRaw = outRgbOff * 4;
	if (outRgbOff < 0 || outRgbLen < 0 || (outRgbOff + outRgbLen) < 0 ||
		inRawOff < 0 || inRawLen < 0 || (inRawOff + inRawLen) < 0 ||
		outRgbOffRaw < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Double check RGB count. */
	outRgbLenRaw = -1;
	if (sjme_error_is(error = g->api->mapRawScanBytes(g,
		outRgbLen, &outRgbLenRaw)) || outRgbLenRaw < 0)
		return sjme_error_default(error);
		
	/* Use the smaller of the two. */
	if (outRgbLenRaw < inRawLen)
		byteLimit = outRgbLenRaw;
	else
		byteLimit = inRawLen;
	
	/* Optimal format for direct copy? */
	if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 ||
		g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_RGB888)
	{
		/* Copy over efficiently. */
		memmove(SJME_POINTER_OFFSET(outRgb, outRgbOffRaw),
			SJME_POINTER_OFFSET(inRaw, inRawOff),
			byteLimit);
		
		/* Success! */
		return SJME_ERROR_NONE;
	}
	
	/* Simple byte swap. */
	else if (g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_BGRA8888 ||
		g->pixelFormat == SJME_GFX_PIXEL_FORMAT_INT_BGRX8888)
	{
		return sjme_swap_uint_memmove(
			SJME_POINTER_OFFSET(outRgb, outRgbOffRaw),
			SJME_POINTER_OFFSET(inRaw, inRawOff),
			byteLimit);
	}
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_core_pencilSetAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb)
{
	sjme_errorCode error;
	sjme_scritchui_pencilColor* target;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Map color natively, if possible. */
	target = &g->state.color;
	if (sjme_error_is(error = g->prim.mapColor(g,
		SJME_JNI_FALSE, argb, target)))
		return sjme_error_default(error);
	
	/* Forward. */
	if (g->impl->setAlphaColor != NULL)
		return g->impl->setAlphaColor(g, argb);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_pencilSetBlendingMode(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		sjme_scritchui_pencilBlendingMode mode)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (mode < 0 || mode >= SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Source blending cannot be used if there is no alpha channel. */
	if (!g->hasAlpha && mode == SJME_SCRITCHUI_PENCIL_BLEND_SRC)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Set mode. */
	g->state.blending = mode;
	
	/* Forward. */
	if (g->impl->setBlendingMode != NULL)
		return g->impl->setBlendingMode(g, mode);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_pencilSetClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_scritchui_rect* rect;
	sjme_jint ex, ey;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Translate coordinates. */
	sjme_scritchui_core_transform(g, &x, &y);
	
	/* Minimum bounds. */
	if (w <= 0)
		w = 1;
	if (h <= 0)
		h = 1;
	
	/* Get actual coordinates of clip end. */
	ex = x + w;
	ey = y + h;
	
	/* If the clip is negative, normalize to zero. */
	if (x < 0)
		x = 0;
	if (y < 0)
		y = 0;
	
	/* If the clip exceeds the buffer bounds, clip it. */
	if (ex > g->width)
		ex = g->width;
	if (ey > g->height)
		ey = g->height;
	
	/* Translate back. */
	w = ex - x;
	h = ey - y;
	
	/* Minimum bounds. */
	if (w <= 0)
		w = 1;
	if (h <= 0)
		h = 1;
	
	/* Copy clip. */
	rect = &g->state.clip;
	rect->x = x;
	rect->y = y;
	rect->width = w;
	rect->height = h;
	
	/* Forward to native call. */
	if (g->impl->setClip != NULL)
		return g->impl->setClip(g, x, y, w, h);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_pencilSetDefaultFont(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Reset to use the default font. */
	g->state.font = g->defaultFont;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_pencilSetFont(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencilFont font)
{
	if (g == NULL || font == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Set font used. */
	g->state.font = font;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_pencilSetStrokeStyle(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		sjme_scritchui_pencilStrokeMode style)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (style < 0 || style >= SJME_NUM_SCRITCHUI_PENCIL_STROKES)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Set stroke mode. */
	g->state.stroke = style;
	
	/* Forward to native. */
	if (g->impl->setStrokeStyle != NULL)
		return g->impl->setStrokeStyle(g, style);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_core_pencilTranslate(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Add to the translation. */
	g->state.translate.x += x;
	g->state.translate.y += y;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

/** Core pencil functions. */
static const sjme_scritchui_pencilFunctions sjme_scritchui_core_pencil =
{
	.blendRGBInto = sjme_scritchui_core_pencilBlendRGBInto,
	.copyArea = sjme_scritchui_core_pencilCopyArea,
	.drawChar = sjme_scritchui_core_pencilDrawChar,
	.drawChars = sjme_scritchui_core_pencilDrawChars,
	.drawHoriz = sjme_scritchui_core_pencilDrawHoriz,
	.drawLine = sjme_scritchui_core_pencilDrawLine,
	.drawPixel = sjme_scritchui_core_pencilDrawPixel,
	.drawRect = sjme_scritchui_core_pencilDrawRect,
	.drawSubstring = sjme_scritchui_core_pencilDrawSubstring,
	.drawTriangle = sjme_scritchui_core_pencilDrawTriangle,
	.drawXRGB32Region = sjme_scritchui_core_pencilDrawXRGB32Region,
	.fillRect = sjme_scritchui_core_pencilFillRect,
	.fillTriangle = sjme_scritchui_core_pencilFillTriangle,
	.mapColor = sjme_scritchui_core_pencilMapColor,
	.mapRawScanBytes = sjme_scritchui_core_pencilMapRawScanBytes,
	.mapRawScanFromRGB = sjme_scritchui_core_pencilMapRawScanFromRGB,
	.mapRGBFromRawScan = sjme_scritchui_core_pencilMapRGBFromRawScan,
	.setAlphaColor = sjme_scritchui_core_pencilSetAlphaColor,
	.setBlendingMode = sjme_scritchui_core_pencilSetBlendingMode,
	.setClip = sjme_scritchui_core_pencilSetClip,
	.setDefaultFont = sjme_scritchui_core_pencilSetDefaultFont,
	.setFont = sjme_scritchui_core_pencilSetFont,
	.setStrokeStyle = sjme_scritchui_core_pencilSetStrokeStyle,
	.translate = sjme_scritchui_core_pencilTranslate,
};

sjme_errorCode sjme_scritchui_pencilInitStatic(
	sjme_attrInOutNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInNotNull const sjme_scritchui_pencilImplFunctions* inFunctions,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInNotNull sjme_scritchui_pencilFont defaultFont,
	sjme_attrInNullable const sjme_frontEnd* copyFrontEnd)
{
	sjme_scritchui_pencilBase result;
	
	if (inPencil == NULL || inFunctions == NULL || defaultFont == NULL ||
		(inLockFrontEndCopy != NULL && inLockFuncs == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (sw <= 0 || sh <= 0 || bw <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (pf < 0 || pf >= SJME_NUM_GFX_PIXEL_FORMATS)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Raw scan putting are required at a minimum. */
	if (inFunctions->rawScanPut == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Locking functions which are required. */
	if (inLockFuncs != NULL)
		if (inLockFuncs->lock == NULL || inLockFuncs->lockRelease == NULL)
			return SJME_ERROR_NOT_IMPLEMENTED;
		
	/* Setup base result. */
	memset(&result, 0, sizeof(result));
	result.api = &sjme_scritchui_core_pencil;
	result.impl = inFunctions;
	result.lock = inLockFuncs;
	result.defaultFont = defaultFont;
	result.pixelFormat = pf;
	result.width = sw;
	result.height = sh;
	result.scanLen = bw;
	
	/* Determine bits per pixel. */
	result.bitsPerPixel = -1;
	switch (pf)
	{
		case SJME_GFX_PIXEL_FORMAT_INT_ARGB8888:
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRA8888:
		case SJME_GFX_PIXEL_FORMAT_INT_BGRX8888:
		case SJME_GFX_PIXEL_FORMAT_INT_XBGR8888:
			result.bitsPerPixel = 32;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
			result.bitsPerPixel = 16;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
			result.bitsPerPixel = 8;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
			result.bitsPerPixel = 4;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
			result.bitsPerPixel = 2;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
			result.bitsPerPixel = 1;
			break;
	}
	
	/* Determine raw scan line length. */
	result.scanLenBytes = (result.scanLen * result.bitsPerPixel) / 8;
	
	/* Copy lock front end source? */
	if (inLockFuncs != NULL && inLockFrontEndCopy != NULL)
		memmove(&result.lockState.source, inLockFrontEndCopy,
			sizeof(result.lockState.source));
	
	/* Is there an alpha channel? */
	/* Note that alpha can only be supported if we can read the underlying */
	/* pixel data. */
	result.hasAlpha = (pf == SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 ||
		pf == SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444 ||
		pf == SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555 ||
		pf == SJME_GFX_PIXEL_FORMAT_INT_BGRA8888 ? SJME_JNI_TRUE :
		SJME_JNI_FALSE) && (result.impl->rawScanGet != NULL);
	
	/* Copy in front end? */
	if (copyFrontEnd != NULL)
		memmove(&result.frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	
	/* Raw scan put, must be implemented always. */
	result.prim.rawScanPut = result.impl->rawScanPut;
	
	/* Need a primitive draw horizontal line? */
	if (result.impl->drawHoriz != NULL)
		result.prim.drawHoriz = result.impl->drawHoriz;
	else
		result.prim.drawHoriz = sjme_scritchui_corePrim_drawHoriz;
		
	/* Need a primitive draw line? */
	if (result.impl->drawLine != NULL)
		result.prim.drawLine = result.impl->drawLine;
	else
		result.prim.drawLine = sjme_scritchui_corePrim_drawLine;
	
	/* Need a primitive draw pixel? */
	if (result.impl->drawPixel != NULL)
		result.prim.drawPixel = result.impl->drawPixel;
	else
		result.prim.drawPixel = sjme_scritchui_corePrim_drawPixel;
	
	/* Raw scan get. */
	if (result.impl->rawScanGet != NULL)
		result.prim.rawScanGet = result.impl->rawScanGet;
	else
		result.prim.rawScanGet = sjme_scritchui_corePrim_rawScanGet;
	
	/* Color mapping. */
	if (result.impl->mapColor != NULL)
		result.prim.mapColor = result.impl->mapColor;
	else
		result.prim.mapColor = sjme_scritchui_corePrim_mapColor;
	
	/* Determine bytes per pixel. */
	result.api->mapRawScanBytes(&result, 1,
		&result.bytesPerPixel);
	
	/* Basic filling of raw value. */
	if (result.bytesPerPixel == 4)
		result.prim.rawScanFill = sjme_scritchui_corePrim_rawScanFillInt;
	else if (result.bytesPerPixel == 2)
		result.prim.rawScanFill = sjme_scritchui_corePrim_rawScanFillShort;
	else
		result.prim.rawScanFill = sjme_scritchui_corePrim_rawScanFillByte;
	
	/* Set initial state, ignore any errors. */
	result.api->setAlphaColor(&result, 0xFF000000);
	result.api->setStrokeStyle(&result,
		SJME_SCRITCHUI_PENCIL_STROKE_SOLID);
	result.api->setBlendingMode(&result,
		SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER);
	result.api->setDefaultFont(&result);
	
	/* Success! Copy back. */
	memmove(inPencil, &result, sizeof(result));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_hardwareGraphics(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrOutNullable sjme_alloc_weak* outWeakPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint bw,
	sjme_attrInPositiveNonZero sjme_jint bh,
	sjme_attrInNullable const sjme_scritchui_pencilLockFunctions* inLockFuncs,
	sjme_attrInNullable const sjme_frontEnd* inLockFrontEndCopy,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable const sjme_frontEnd* pencilFrontEndCopy)
{
	sjme_errorCode error;
	sjme_scritchui_pencil result;
	sjme_alloc_weak resultWeak;
	sjme_scritchui_pencilFont defaultFont;
	
	if (inState == NULL || outPencil == NULL ||
		(inLockFrontEndCopy != NULL && inLockFuncs == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bw <= 0 || bh <= 0 || sx < 0 || sy < 0 || sw <= 0 || sh <= 0 ||
		sw > bw || sh > bh || (sx + sw) < 0 || (sx + sw) > bw ||
		(sy + sh) < 0 || (sy + sh) > bh)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* If natively supported, see if it can create a graphics context. */
	if (inState->impl->hardwareGraphics != NULL)
	{
		/* If this does not fail, use native graphics. */
		if (!sjme_error_is(inState->impl->hardwareGraphics(
			inState, outPencil, outWeakPencil, pf, bw, bh,
			inLockFuncs, inLockFrontEndCopy,
			sx, sy, sw, sh, pencilFrontEndCopy)))
			return SJME_ERROR_NONE;
	}
	
	/* At this point locking functions are required. */
	if (inLockFuncs == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Get default font. */
	defaultFont = NULL;
	if (sjme_error_is(error = inState->api->fontBuiltin(inState,
		&defaultFont)) || defaultFont == NULL)
		return sjme_error_default(error);
	
	/* Forward to basic operations. */
	result = NULL;
	resultWeak = NULL;
	if (sjme_error_is(error = sjme_scritchui_pencilInitBuffer(
		inState->pool, &result, &resultWeak,
		pf, bw, bh,
		inLockFuncs, inLockFrontEndCopy, sx, sy, sw, sh,
		defaultFont, pencilFrontEndCopy)) ||
		result == NULL || resultWeak == NULL)
		return sjme_error_default(error);
	
	/* Success! */
	*outPencil = result;
	if (outWeakPencil != NULL)
		*outWeakPencil = resultWeak;
	return SJME_ERROR_NONE;
}
