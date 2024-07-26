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

sjme_errorCode sjme_scritchpen_corePrim_mapColorFromRGB(
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
			v = (bb << 24) | (gg << 16) | (rr << 8) | 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_BGR888:
			v = 0xFF000000 | (bb << 16) | (gg << 8) | (rr);
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_RGBX8888:
			v = (rr << 24) | (gg << 16) | (bb << 8) | 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
			v = argb | 0xFF000000;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_BYTE3_RGB888:
			v = (rr << 16) | (gg << 8) | bb;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_BYTE3_BGR888:
			v = (rr) | (gg << 8) | (bb << 16);
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
	outColor->argb = argb;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_corePrim_mapColorFromRaw(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint v,
	sjme_attrOutNotNull sjme_scritchui_pencilColor* outColor)
{
	sjme_jint numCol, aa, rr, gg, bb, argb;
	
	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If there is a palette, try using it to get a color. */
	numCol = g->palette.numColors;
	if (g->palette.colors != NULL && numCol > 0)
	{
		if (v > 0 && v < numCol)
			return sjme_scritchpen_corePrim_mapColorFromRGB(g,
				g->palette.colors[v], outColor);
		
		/* Invalid color, render to black or close to it. */
		return sjme_scritchpen_corePrim_mapColorFromRGB(g,
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
		
		case SJME_GFX_PIXEL_FORMAT_INT_BGR888:
		case SJME_GFX_PIXEL_FORMAT_BYTE3_BGR888:
			aa = 0xFF;
			rr = (v) & 0xFF;
			gg = (v >> 8) & 0xFF;
			bb = (v >> 16) & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_RGB888:
		case SJME_GFX_PIXEL_FORMAT_BYTE3_RGB888:
			aa = 0xFF;
			rr = (v >> 16) & 0xFF;
			gg = (v >> 8) & 0xFF;
			bb = (v) & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_INT_RGBX8888:
			aa = 0xFF;
			rr = (v >> 24) & 0xFF;
			gg = (v >> 16) & 0xFF;
			bb = (v >> 8) & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444:
			aa = ((v >> 12) & 0xF);
			aa |= aa << 4;
			rr = ((v >> 8) & 0xF);
			rr |= rr << 4;
			gg = ((v >> 4) & 0xF);
			gg |= gg << 4;
			bb = ((v) & 0xF);
			bb |= bb << 4;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB565:
			aa = 0xFF;
			rr = ((v >> 11) & 0x1F) << 3;
			gg = ((v >> 4) & 0x3F) << 2;
			bb = ((v) & 0x1F) << 3;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB555:
			aa = 0xFF;
			rr = ((v >> 10) & 0x1F) << 3;
			gg = ((v >> 5) & 0x1F) << 3;
			bb = ((v) & 0x1F) << 3;
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
	argb = (aa << 24) | (rr << 16) | (gg << 8) | bb;
	outColor->argb = argb;
	return sjme_scritchpen_corePrim_mapColorFromRGB(g, argb, outColor);
}

sjme_errorCode sjme_scritchpen_corePrim_mapColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_pencilColor* outColor)
{
	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Otherwise, use our own colormapping code. */
	if (fromRaw)
		return sjme_scritchpen_corePrim_mapColorFromRaw(g,
			inRgbOrRaw, outColor);
	return sjme_scritchpen_corePrim_mapColorFromRGB(g,
		inRgbOrRaw, outColor);
}

sjme_errorCode sjme_scritchpen_coreUtil_applyAnchor(
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

sjme_errorCode sjme_scritchpen_coreUtil_applyRotateScale(
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

sjme_errorCode sjme_scritchpen_coreUtil_applyTranslate(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInOutNotNull sjme_jint* x,
	sjme_attrInOutNotNull sjme_jint* y)
{
	if (g == NULL || x == NULL || y == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Apply translation. */
	(*x) += g->state.translate.x;
	(*y) += g->state.translate.y;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_core_mapColor(
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

sjme_errorCode sjme_scritchpen_core_setAlphaColor(
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
	
	/* Is alpha applicable? */
	/* Note that if we cannot read from the source buffer, we cannot */
	/* apply alpha correctly so we just ignore it. */
	g->state.applyAlpha = (target->a != 0xFF &&
		g->prim.rawScanGet != NULL &&
		g->state.blending != SJME_SCRITCHUI_PENCIL_BLEND_SRC);
	
	/* Forward. */
	if (g->impl->setAlphaColor != NULL)
		return g->impl->setAlphaColor(g, argb);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_core_setBlendingMode(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInRange(0, SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		sjme_scritchui_pencilBlendingMode mode)
{
	sjme_scritchui_pencilColor* color;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (mode < 0 || mode >= SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Source blending cannot be used if there is no alpha channel. */
	if (!g->hasAlpha && mode == SJME_SCRITCHUI_PENCIL_BLEND_SRC)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Set mode. */
	g->state.blending = mode;
	
	/* Set color again, to reset alpha blending state. */
	color = &g->state.color;
	g->api->setAlphaColor(g,
		(color->a << 24) | (color->r << 16) | (color->g << 8) |
		color->b);
	
	/* Forward. */
	if (g->impl->setBlendingMode != NULL)
		return g->impl->setBlendingMode(g, mode);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_core_setClip(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h)
{
	sjme_scritchui_rect* rect;
	sjme_scritchui_line* line;
	sjme_jint ex, ey;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Translate coordinates. */
	sjme_scritchpen_coreUtil_applyTranslate(g, &x, &y);
	
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
	
	/* Copy clip. */
	rect = &g->state.clip;
	rect->s.x = x;
	rect->s.y = y;
	rect->d.width = w;
	rect->d.height = h;
	
	/* Set end coordinates. */
	line = &g->state.clipLine;
	line->s.x = x;
	line->s.y = y;
	line->e.x = ex;
	line->e.y = ey;
	
	/* Forward to native call. */
	if (g->impl->setClip != NULL)
		return g->impl->setClip(g, x, y, w, h);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_core_setDefaultFont(
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

sjme_errorCode sjme_scritchpen_core_setParametersFrom(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil from)
{
	sjme_errorCode error;
	
	if (g == NULL || from == NULL)
		return SJME_ERROR_NONE;
	
	/* Initially successful. */
	error = SJME_ERROR_NONE;
	
	/* Remove any current translation. */
	error |= g->api->translate(g,
		-g->state.translate.x, -g->state.translate.y);
	
	/* Copy all basic parameters. */
	error |= g->api->setAlphaColor(g, from->state.color.argb);
	error |= g->api->setClip(g, from->state.clip.s.x, from->state.clip.s.y,
		from->state.clip.d.width, from->state.clip.d.height);
	error |= g->api->setStrokeStyle(g, from->state.stroke);
	
	/* We can only copy the blending mode if we have alpha support. */
	if (!g->hasAlpha)
		error |= g->api->setBlendingMode(g,
			SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER);
	else
		error |= g->api->setBlendingMode(g, from->state.blending);
	
	/* If the other has no font, then just set the default. */
	if (from->state.font == NULL)
		error |= g->api->setDefaultFont(g);
	else
		error |= g->api->setFont(g, from->state.font);
	
	/* Re-translate to target coordinate system. */
	error |= g->api->translate(g,
		from->state.translate.x, from->state.translate.y);
	
	/* Any resultant error? */
	return error;
}

sjme_errorCode sjme_scritchpen_core_setStrokeStyle(
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

sjme_errorCode sjme_scritchpen_core_translate(
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
