/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"

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

static sjme_errorCode sjme_scritchui_corePrim_lineViaPixel(
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

static sjme_errorCode sjme_scritchui_corePrim_horizViaLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward. */
	return g->impl->drawLine(g, x, y, x + w, y);
}

static sjme_errorCode sjme_scritchui_corePrim_pixelViaLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Forward. */
	return g->impl->drawLine(g, x, y, x + 1, y);
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
	sjme_attrInValue sjme_jint anchor,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
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

static sjme_errorCode sjme_scritchui_core_pencilSetAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb)
{
	sjme_scritchui_pencilColor* target;
	sjme_jint v, aa, rr, gg, bb, ii;
	sjme_jint i, numCol, d, bestCol, bestColScore, thisColScore;
	sjme_jint pargb, prr, pgg, pbb;
	sjme_jint mrr, mgg, mbb;
	const sjme_jint* colors;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Implementation must have. */
	if (g->impl->setAlphaColor == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Set base color properties. */
	target = &g->state.color;
	aa = (argb >> 24) & 0xFF;
	target->a = aa;
	rr = (argb >> 16) & 0xFF;
	target->r = rr;
	gg = (argb >> 8) & 0xFF;
	target->g = gg;
	bb = (argb) & 0xFF;
	target->b = bb;
	
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
	target->i = ii;
	target->v = v;
	
	/* Forward. */
	return g->impl->setAlphaColor(g, argb);
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
	
	if (g->impl->setBlendingMode == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Source blending cannot be used if there is no alpha channel. */
	if (!g->hasAlpha && mode == SJME_SCRITCHUI_PENCIL_BLEND_SRC)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Set mode. */
	g->state.blending = mode;
	
	/* Forward. */
	return g->impl->setBlendingMode(g, mode);
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
	
	if (g->impl->setClip == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
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
	return g->impl->setClip(g, x, y, w, h);
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
	
	if (g->impl->setStrokeStyle == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Set stroke mode. */
	g->state.stroke = style;
	
	/* Forward to native. */
	return g->impl->setStrokeStyle(g, style);
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
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd)
{
	sjme_scritchui_pencilBase result;
	
	if (inPencil == NULL || inFunctions == NULL || defaultFont == NULL ||
		(inLockFrontEndCopy != NULL && inLockFuncs == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (sw <= 0 || sh <= 0 || bw <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (pf < 0 || pf >= SJME_NUM_GFX_PIXEL_FORMATS)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* These are required at the minimum. */
	if (inFunctions->drawLine == NULL && inFunctions->drawPixel == NULL)
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
	
	/* Copy lock front end source? */
	if (inLockFuncs != NULL && inLockFrontEndCopy != NULL)
		memmove(&result.lockState.source, inLockFrontEndCopy,
			sizeof(result.lockState.source));
	
	/* Is there an alpha channel? */
	result.hasAlpha = (pf == SJME_GFX_PIXEL_FORMAT_INT_ARGB8888 ||
		pf == SJME_GFX_PIXEL_FORMAT_SHORT_ARGB4444 ||
		pf == SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555 ? SJME_JNI_TRUE :
		SJME_JNI_FALSE);
	
	/* Copy in front end? */
	if (copyFrontEnd != NULL)
		memmove(&result.frontEnd, copyFrontEnd,
			sizeof(*copyFrontEnd));
	
	/* Determine core pixel primitive. */
	if (result.impl->drawPixel != NULL)
	{
		result.prim.drawPixel = result.impl->drawPixel;
		
		/* Use efficient line drawing? */
		if (result.impl->drawLine != NULL)
			result.prim.drawLine = result.impl->drawLine;
		else
			result.prim.drawLine = sjme_scritchui_corePrim_lineViaPixel;
	}
	
	/* Implement pixel via line operation. */
	else if (result.impl->drawLine != NULL)
	{
		result.prim.drawLine = result.impl->drawLine;
		result.prim.drawPixel = sjme_scritchui_corePrim_pixelViaLine;
	}
	
	/* Horizontal line via the line function. */
	if (result.impl->drawHoriz != NULL)
		result.prim.drawHoriz = result.impl->drawHoriz;
	else
		result.prim.drawHoriz = sjme_scritchui_corePrim_horizViaLine;
	
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
