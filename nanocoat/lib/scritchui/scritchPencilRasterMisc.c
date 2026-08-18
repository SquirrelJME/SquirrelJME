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

sjme_errorCode sjme_scritchpen_corePrim_mapColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_color* outColor)
{
	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Otherwise, use our own color mapping code. */
	if (fromRaw)
		return sjme_scritchpen_corePrim_mapColorFromRaw(g,
			inRgbOrRaw, outColor);
	return sjme_scritchpen_corePrim_mapColorFromRGB(g,
		inRgbOrRaw, outColor);
}
	
sjme_errorCode sjme_scritchpen_corePrim_mapColorPfToRgb(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInValue sjme_jint v,
	sjme_attrOutNotNull sjme_scritchui_color* outColor)
{
	sjme_jint numCol, aa, rr, gg, bb, argb;
	sjme_jboolean isIndexed;
	
	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If there is a palette, try using it to get a color. */
	/* Note this requires a palette from the graphics context to be valid. */
	numCol = g->palette.numColors;
	isIndexed = sjme_scritchpen_isIndexed(pf);
	if (isIndexed || (g->palette.colors != NULL && numCol > 0))
	{
		if (g->palette.colors != NULL && v >= 0 && v < numCol)
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
	switch (pf)
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
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB444:
			aa = 0xFF;
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
			aa = ((v >> 15) & 0x01) * 0xFF;
			bb = (v >> 10) & 0x1F; 
			bb = (bb << 3) | (bb >> 2);
			gg = (v >> 5) & 0x1F; 
			gg = (gg << 3) | (gg >> 2);
			rr = v & 0x1F;
			rr = (rr << 3) | (rr >> 2);
			break;

		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB1555:
			aa = ((v >> 15) & 0x01) * 0xFF;
			rr = (v >> 10) & 0x1F; 
			rr = (rr << 3) | (rr >> 2);
			gg = (v >> 5) & 0x1F; 
			gg = (gg << 3) | (gg >> 2);
			bb = v & 0x1F;
			bb = (bb << 3) | (bb >> 2);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_RGB332:
			aa = 0xFF;
			rr = (v >> 5) & 0x7;
			rr = (rr << 5) | (rr << 2) | (rr >> 2);
			gg = (v >> 2) & 0x7;
			gg = (gg << 5) | (gg << 2) | (gg >> 2);
			bb = (v) & 0x3;
			bb = (bb << 6) | (bb << 4) | (bb << 2) | bb;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_A8:
			aa = v;
			rr = 0;
			gg = 0;
			bb = 0;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_R8:
			aa = 0xFF;
			rr = v;
			gg = 0;
			bb = 0;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_G8:
			aa = 0xFF;
			rr = 0;
			gg = v;
			bb = 0;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_B8:
			aa = 0xFF;
			rr = 0;
			gg = 0;
			bb = v;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536A:
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256A:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4A:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2A:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1A:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1_VERTICAL:
			return sjme_error_fatal(SJME_ERROR_SHOULD_NOT_HAPPEN);
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_A4:
		case SJME_GFX_PIXEL_FORMAT_PACKED_A2:
		case SJME_GFX_PIXEL_FORMAT_PACKED_A1:
		default:
			return sjme_error_notImplemented(0);
	}
	
	/* Map back to normalize. */
	argb = (aa << 24) | (rr << 16) | (gg << 8) | bb;
	outColor->argb = argb;
	return sjme_scritchpen_corePrim_mapColorFromRGB(g, argb, outColor);
}

sjme_errorCode sjme_scritchpen_corePrim_mapColorFromRGB(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb,
	sjme_attrOutNotNull sjme_scritchui_color* outColor)
{
	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_scritchpen_corePrim_mapColorRgbToPf(g, g->pixelFormat,
		argb, outColor);
}

sjme_errorCode sjme_scritchpen_corePrim_mapColorFromRaw(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint v,
	sjme_attrOutNotNull sjme_scritchui_color* outColor)
{
	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return sjme_scritchpen_corePrim_mapColorPfToRgb(
		g, g->pixelFormat, v, outColor);
}
	
sjme_errorCode sjme_scritchpen_corePrim_mapColorRgbToPf(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInValue sjme_jint argb,
	sjme_attrOutNotNull sjme_scritchui_color* outColor)
{
	sjme_jint v, aa, rr, gg, bb, ii;
	sjme_jint i, numCol, d, bestCol, bestColScore, thisColScore;
	sjme_jint thisAlphaScore, bestAlphaScore;
	sjme_jint pargb, paa, prr, pgg, pbb;
	sjme_jint mrr, mgg, mbb;
	const sjme_jint* colors;
	sjme_jboolean alphaIndexed, isIndexed;

	if (g == NULL || outColor == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Set base color properties. */
	aa = (argb >> 24) & 0xFF;
	outColor->a = (sjme_jubyte)aa;
	rr = (argb >> 16) & 0xFF;
	outColor->r = (sjme_jubyte)rr;
	gg = (argb >> 8) & 0xFF;
	outColor->g = (sjme_jubyte)gg;
	bb = (argb) & 0xFF;
	outColor->b = (sjme_jubyte)bb;
	
	/* Find closest indexed color. */
	ii = -1;
	numCol = g->palette.numColors;
	isIndexed = sjme_scritchpen_isIndexed(pf);
	if (isIndexed && (g->palette.colors == NULL || numCol <= 0))
		ii = 0;
	else if (isIndexed || (g->palette.colors != NULL && numCol > 0))
	{
		/* Does this set of indexed colors consider alpha? */
		alphaIndexed =
			(g->pixelFormat >= SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536A &&
			g->pixelFormat <= SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1A);
		if (alphaIndexed)
			d = argb;
		else
			d = argb & 0xFFFFFF;
		
		/* Determine the most important color channel. */
		mrr = (rr >= gg && rr >= bb ? 1 : 2);
		mgg = (gg >= rr && gg >= bb ? 1 : 2);
		mbb = (bb >= rr && bb >= gg ? 1 : 2);
		
		/* Start with a horrible color score. */
		bestCol = 0;
		bestColScore = 134217728;
		bestAlphaScore = 256;
		
		/* Find exact color match? */
		colors = g->palette.colors;
		for (i = 0; i < numCol && g->palette.colors; i++)
		{
			/* Exact match? */
			pargb = colors[i];
			if (d == pargb)
			{
				ii = i;
				break;
			}
			
			/* Get original ARGB value. */
			prr = (pargb >> 16) & 0xFF;
			pgg = (pargb >> 8) & 0xFF;
			pbb = (pargb) & 0xFF;
			
			/* Calculate this color score, use if it is better. */
			thisColScore = (abs(prr - rr) * mrr) +
				(abs(pgg - gg) * mgg) +
				(abs(pbb - bb) * mbb);
			if (thisColScore < bestColScore)
			{
				/* If alpha is being used, try to find the color with the */
				/* closest alpha match. */
				if (alphaIndexed)
				{
					/* Calculate the score of this alpha color. */
					paa = (pargb >> 24) & 0xFF;
					thisAlphaScore = abs(paa - aa);

					/* If the alpha score is worse, even though the color is */
					/* better, this might end up being a color that does */
					/* not match the desired alpha value. */
					if (thisAlphaScore > bestAlphaScore)
						continue;

					/* Consider the new alpha score. */
					bestAlphaScore = thisAlphaScore;
				}

				/* Use this color (and best alpha, if selected). */
				bestCol = i;
				bestColScore = thisColScore;
			}
		}
		
		/* If no exact color was found, use the best scoring one. */ 
		if (ii < 0)
			ii = bestCol;
	}
	
	/* Determine raw pixel color. */
	switch (pf)
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
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_RGB444:
			v = (((rr >> 4) & 0xF) << 8) |
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

		case SJME_GFX_PIXEL_FORMAT_SHORT_ARGB1555:
			v = (((aa >> 7) & 0x1) << 15) |
				(((rr >> 3) & 0x1F) << 10) |
				(((gg >> 3) & 0x1F) << 5) |
				((bb >> 3) & 0x1F);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_RGB332:
			v = (((rr >> 5) & 0x7) << 5) |
				(((gg >> 5) & 0x7) << 2) |
				(bb & 0x3);
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_A8:
			v = aa;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_R8:
			v = rr;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_G8:
			v = gg;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_B8:
			v = bb;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536:
		case SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536A:
			v = ii & 0xFFFF;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256:
		case SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256A:
			v = ii & 0xFF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4A:
			v = ii & 0xF;
			break;
		
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2A:
			v = ii & 0x3;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1A:
		case SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1_VERTICAL:
			v = ii & 0x1;
			break;
			
		case SJME_GFX_PIXEL_FORMAT_PACKED_A4:
		case SJME_GFX_PIXEL_FORMAT_PACKED_A2:
		case SJME_GFX_PIXEL_FORMAT_PACKED_A1:
		default:
			return sjme_error_notImplemented(0);
	}
		
	/* Store raw colors. */
	outColor->i = (sjme_jubyte)ii;
	outColor->v = v;
	outColor->argb = argb;
	
	/* Success! */
	return SJME_ERROR_NONE;
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

sjme_errorCode sjme_scritchpen_coreUtil_applyCoordinateAdj(
	sjme_attrInValue sjme_scritchui_pencilTranslate inTrans,
	sjme_attrInOutNotNull sjme_jint* x,
	sjme_attrInOutNotNull sjme_jint* y,
	sjme_attrInOutNotNull sjme_jint* w,
	sjme_attrInOutNotNull sjme_jint* h,
	sjme_attrInPositive sjme_jint dataWidth,
	sjme_attrInPositive sjme_jint dataHeight)
{
	sjme_jint temp, xform;
	
	if (x == NULL || y == NULL || w == NULL || h == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (dataWidth < 0 || dataHeight < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Determine the transformation function to use. */
	switch (inTrans)
	{
		/* These bits represent the stuff to do! == 0b9VH; */
		case SJME_SCRITCHUI_TRANS_NONE:				xform = 0; break;
		case SJME_SCRITCHUI_TRANS_MIRROR:			xform = 1; break;
		case SJME_SCRITCHUI_TRANS_MIRROR_ROT180:	xform = 2; break;
		/* TRANS_ROT180 is basically a mix of the two above */
		case SJME_SCRITCHUI_TRANS_ROT180:			xform = 3; break;
		case SJME_SCRITCHUI_TRANS_ROT90:			xform = 4; break;
		case SJME_SCRITCHUI_TRANS_MIRROR_ROT90:		xform = 8; break;
		case SJME_SCRITCHUI_TRANS_MIRROR_ROT270:	xform = 16; break;
		case SJME_SCRITCHUI_TRANS_ROT270:			xform = 32; break;
		/* These bits represent the stuff to do! == 0b9VH; */

		default:
			return sjme_error_notImplemented(0);
	}
	
	/* Whenever we receive a transformation that alters the width and height */
	/* the image, the first adjustment we have to do is update the source */
	/* and destination width/height accordingly (makes source/dest width and */
	/* height usage more consistent on further adjustments and clipping). */
	if ((xform & 4) || (xform & 8) || (xform & 16) || (xform & 32))
	{
		temp = *h;
		*h = *w;
		*w = temp;
	}

	/* Mirrored horizontally */
	if (xform & 1)
		*x = dataWidth - *x - *w + 1;

	/* Mirrored vertically */
	if (xform & 2)
		*y = dataHeight - *y - *h + 1;

	/* Was rotated 90 degrees clockwise. */
	if (xform & 4)
	{
		temp = *x;
		*x = dataHeight - *y - *w + 1;
		*y = temp;
	}

	/* Was mirrored horizontally and rotated 90 degrees clockwise.*/
	if (xform & 8)
	{
		temp = *y;
		*y = dataWidth - *x - *h + 1;
		*x = dataHeight - temp - *w + 1;
	}

	/* Was mirrored horizontally and rotated 270 degrees clockwise */
	if (xform & 16)
	{
		temp = *x;
		*x = *y;
		*y = temp;
	}

	/* Was rotated 270 degrees clockwise */
	if (xform & 32)
	{
		temp = *y;
		*y = dataWidth - *x - *h + 1;
		*x = temp;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_coreUtil_applyRotateScale(
	sjme_attrInOutNotNull sjme_scritchui_matrix* adjMatrix,
	sjme_attrInValue sjme_scritchui_pencilTranslate inTrans,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest)
{
	sjme_jint temp, xform;
	
	if (adjMatrix == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Determine the transformation function to use. */
	switch (inTrans)
	{
		/* These bits represent the stuff to do! == 0bLR9VH; */
		case SJME_SCRITCHUI_TRANS_NONE:				xform = 0; break;
		case SJME_SCRITCHUI_TRANS_MIRROR:			xform = 1; break;
		case SJME_SCRITCHUI_TRANS_MIRROR_ROT180:	xform = 2; break;
		/* TRANS_ROT180 is basically a mix of the two above */
		case SJME_SCRITCHUI_TRANS_ROT180:			xform = 3; break;
		case SJME_SCRITCHUI_TRANS_ROT90:			xform = 4; break;
		case SJME_SCRITCHUI_TRANS_MIRROR_ROT90:		xform = 8; break;
		case SJME_SCRITCHUI_TRANS_MIRROR_ROT270:	xform = 16; break;
		case SJME_SCRITCHUI_TRANS_ROT270:			xform = 32; break;
		/* These bits represent the stuff to do! == 0bLR9VH; */

		default:
			return sjme_error_notImplemented(0);
	}

	/**
	 * This is simple enough to calculate, it's just the destination but with
	 * its width and height swapped if we're handling any 90 or 270 transform
	 * variation.
	 */
	if (xform & 4 || xform & 8 || xform & 16 || xform & 32)
	{
		adjMatrix->tw = hDest;
		adjMatrix->th = wDest;
	}
	else
	{
		adjMatrix->tw = wDest;
		adjMatrix->th = hDest;
	}
	
	/* Base the matrix x, y step calculations from the scaling values. */
	adjMatrix->x.wx = sjme_fixed_fraction(wSrc, wDest);
	adjMatrix->y.zy = sjme_fixed_fraction(hSrc, hDest);
	
	/* Mirror horizontally? */
	if (xform & 1)
		adjMatrix->x.wx = -adjMatrix->x.wx;

	/* Mirror vertically? */
	if (xform & 2)
		adjMatrix->y.zy = -adjMatrix->y.zy;

	/* Rotate 90 degrees clockwise */
	/* Thanks to jercos for helping out with the matrix math! */
	/* The math here has been simplified to remove constants and otherwise. */
	if (xform & 4)
	{
		temp = adjMatrix->x.wx;
		adjMatrix->x.wx = adjMatrix->x.zy;
		adjMatrix->x.zy = -temp;

		temp = adjMatrix->y.wx;
		adjMatrix->y.wx = adjMatrix->y.zy;
		adjMatrix->y.zy = -temp;
	}

	/* Mirror horizontally and rotate 90 degrees clockwise */
	if (xform & 8)
	{
		temp = adjMatrix->x.wx;
		adjMatrix->x.wx = -adjMatrix->x.zy;
		adjMatrix->x.zy = -temp;

		temp = adjMatrix->y.wx;
		adjMatrix->y.wx = -adjMatrix->y.zy;
		adjMatrix->y.zy = -temp;
	}

	/* Mirror horizontally and rotate 270 degrees clockwise */
	if (xform & 16)
	{
		temp = adjMatrix->x.wx;
		adjMatrix->x.wx = adjMatrix->x.zy;
		adjMatrix->x.zy = temp;

		temp = adjMatrix->y.wx;
		adjMatrix->y.wx = adjMatrix->y.zy;
		adjMatrix->y.zy = temp;
	}

	/* Rotate 270 degrees clockwise (A.K.A. 90 degrees counter-clockwise). */
	if (xform & 32)
	{
		temp = adjMatrix->x.wx;
		adjMatrix->x.wx = -adjMatrix->x.zy;
		adjMatrix->x.zy = temp;

		temp = adjMatrix->y.wx;
		adjMatrix->y.wx = -adjMatrix->y.zy;
		adjMatrix->y.zy = temp;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_coreUtil_applyTranslate(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInOutNotNull sjme_jint* x,
	sjme_attrInOutNotNull sjme_jint* y)
{
	if (g == NULL || x == NULL || y == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Apply translation, use the real one. */
	(*x) += g->state.translateReal.x;
	(*y) += g->state.translateReal.y;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchpen_core_mapColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jboolean fromRaw,
	sjme_attrInValue sjme_jint inRgbOrRaw,
	sjme_attrOutNotNull sjme_scritchui_color* outColor)
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
	sjme_scritchui_color* target;
	
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
	sjme_scritchui_color* color;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (mode < 0 || mode >= SJME_NUM_SCRITCHUI_PENCIL_BLENDS)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Source blending cannot be used if there is no alpha channel, */
	/* or anything else that is not SRC_OVER. */
	if (!g->hasAlpha && mode != SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Set mode. */
	g->state.blending = mode;
	
	/* Set color again, to reset alpha blending state. */
	color = &g->state.color;
	g->apiInThread->setAlphaColor(g,
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
	sjme_errorCode error;
	sjme_scritchui_rect* rect;
	sjme_scritchui_line* line;
	sjme_jint ex, ey;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Translate coordinates. */
	if (sjme_error_is(error = g->util->applyTranslate(g, &x, &y)))
		return sjme_error_default(error);
	
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
	if (ex >= g->width)
		ex = g->width;
	if (ey >= g->height)
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
	return g->apiInThread->setFont(g, g->defaultFont, NULL);
}

sjme_errorCode sjme_scritchpen_core_setParametersFrom(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencil from)
{
	sjme_errorCode error;
	
	if (g == NULL || from == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initially successful. */
	error = SJME_ERROR_NONE;
	
	/* Remove any current translation. */
	error |= g->apiInThread->translate(g,
		-g->state.translate.x, -g->state.translate.y);
	
	/* Copy all basic parameters. */
	error |= g->apiInThread->setAlphaColor(g, from->state.color.argb);
	error |= g->apiInThread->setClip(g,
		from->state.clip.s.x, from->state.clip.s.y,
		from->state.clip.d.width, from->state.clip.d.height);
	error |= g->apiInThread->setStrokeStyle(g, from->state.stroke);
	
	/* We can only copy the blending mode if we have alpha support. */
	if (!g->hasAlpha)
		error |= g->apiInThread->setBlendingMode(g,
			SJME_SCRITCHUI_PENCIL_BLEND_SRC_OVER);
	else
		error |= g->apiInThread->setBlendingMode(g, from->state.blending);
	
	/* If the other has no font, then just set the default. */
	if (from->state.font.font == NULL)
		error |= g->apiInThread->setDefaultFont(g);
	else
		error |= g->apiInThread->setFont(g, from->state.font.font,
			&from->state.font.params);
	
	/* Re-translate to target coordinate system. */
	error |= g->apiInThread->translate(g,
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
	
	/* Determine the actual real translation. */
	g->state.translateReal.x = g->state.translate.x + g->forceTranslate.x;
	g->state.translateReal.y = g->state.translate.y + g->forceTranslate.y;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
