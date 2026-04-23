/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"
#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiExtern.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "lib/scritchui/core/coreRaster.h"

sjme_errorCode sjme_scritchpen_core_drawChar(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint c,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor,
	sjme_attrOutNullable sjme_jint* outCw)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFontWithParam font;
	sjme_jint cw, ch, area, dx, dy, sx, sy, v, scanLen, baseline;
	sjme_jint offX, offY;
	sjme_jubyte* bitmap;
	sjme_scritchui_pencilBitLineFunc bitline;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We need the font for rendering. */
	font = g->state.font;
	if (font.font == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Need character width. */
	cw = 0;
	if (sjme_error_is(error = font.font->apiInThread->pixelCharWidth(
		font.font, &font.params, c, &cw)))
		return sjme_error_default(error);
	
	/* Do not draw space characters. */
	if (c == '\t' || c == ' ')
	{
		/* Do report their length however! */
		if (outCw != NULL)
			*outCw = cw;
		
		return SJME_ERROR_NONE;
	}
	
	/* Translate. */
	if (sjme_error_is(error = g->util->applyTranslate(g, &x, &y)))
		return sjme_error_default(error);
	
	/* Need to lock? */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);
	
	/* Allocation checks for this are after this point. */
	bitmap = NULL;
	
	/* And the pixel height, since this is a bitmap font. */
	ch = 0;
	if (sjme_error_is(error = font.font->apiInThread->metricPixelSize(
		font.font, &font.params, -1, &ch)))
		goto fail_anyInLock;
	
	/** Do not bother drawing nothing. */
	if (cw == 0 || ch == 0)
		return SJME_ERROR_NONE;
	
	/* Need the font baseline. */
	baseline = 0;
	if (sjme_error_is(error = font.font->apiInThread->metricPixelBaseline(
		font.font, &font.params, &baseline)))
		goto fail_anyInLock;
	
	/* Calculate anchor point accordingly. */
	if (anchor != 0)
		if (sjme_error_is(error = sjme_scritchpen_coreUtil_applyAnchor(
			anchor,
			x, y, cw, ch, baseline, &x, &y)))
			goto fail_anyInLock;
		
	/* Determine scanline length for each bitmap row. */
	scanLen = sjme_scritchui_pencilFontScanLen(cw);
	
	/* Allocate bitmap. */
	area = sizeof(*bitmap) * (scanLen * ch);
	bitmap = sjme_alloca(area);
	if (bitmap == NULL)
	{
		error = sjme_error_outOfMemory(NULL, area);
		goto fail_anyInLock;
	}
	
	/* Initialize. */
	memset(bitmap, 0, area);
	
	/* Offsets for proper glyph drawing. */
	offX = 0;
	offY = 0;
	
	/* Get glyph bitmap. */
	if (sjme_error_is(error = font.font->apiInThread->renderBitmap(font.font,
		&font.params, c, bitmap, 0, scanLen,
		ch, &offX, &offY)))
		goto fail_anyInLock;
	
	/* Draw bit-lines for the glyphs. */
	for (sy = 0, dy = y + offY, v = 0; sy < ch; sy++, dy++)
		for (sx = 0, dx = x + offX; sx < scanLen; sx++, dx += 8, v++)
		{
			/* Which bitline to use? */
			bitline = sjme_scritchui_pencilBitLines[bitmap[v]];
			
			/* Render the bitline. */
			if (sjme_error_is(error = bitline(g, dx, dy)))
				goto fail_anyInLock;
		}
		
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		goto fail_unlock;
	
	/* Success! */
	if (outCw != NULL)
		*outCw = cw;

	/* Cleanup. */
	if (bitmap != NULL)
		sjme_alloca_free(bitmap);
		
	return SJME_ERROR_NONE;

fail_anyInLock:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
fail_unlock:
	if (bitmap != NULL)
		sjme_alloca_free(bitmap);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_drawChars(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_errorCode error;
	sjme_charSeqStatic seq;
	
	if (g == NULL || s == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (o < 0 || l < 0 || (o + l) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* This is just setting up a static sequence then forwarding it. */
	memset(&seq, 0, sizeof(seq));
	if (sjme_error_is(error = sjme_charSeq_newWideStatic(&seq,
		s, o, l)))
		return sjme_error_default(error);
	
	/* Forward, we do not need to worry about cleanup as everything is */
	/* on the stack. Note offset becomes zero as it is properly offset */
	/* in the static initialization. */
	return g->apiInThread->drawSubstring(g, &seq, 0, l,
		x, y, anchor);
}

sjme_errorCode sjme_scritchpen_core_drawSubstring(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull const sjme_charSeq s,
	sjme_attrInPositive sjme_jint o, 
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	sjme_errorCode error;
	sjme_jint seqLen, at, dx, dy, bx, lineHeight, tw, cw, baseline;
	sjme_jchar c;
	sjme_scritchui_pencilFontWithParam* font;
	
	if (g == NULL || s == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (o < 0 || l < 0 || (o + l) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lock(g)))
		return sjme_error_default(error);

	/* We need the font for rendering. */
	font = &g->state.font;
	if (font->font == NULL || font->font->apiInThread == NULL ||
		font->font->apiInThread->metricPixelHeight == NULL ||
		font->font->apiInThread->metricPixelBaseline == NULL ||
		font->font->apiInThread->stringWidth == NULL ||
		g->apiInThread == NULL ||
		g->apiInThread->drawChar == NULL)
	{
		error = sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
		goto fail_getFont;
	}
	
	/* Need to get the height of a line. */
	lineHeight = -1;
	if (sjme_error_is(error = font->font->apiInThread->metricPixelHeight(
		font->font, &font->params, &lineHeight)) || lineHeight < 0)
	{
		error = sjme_error_defaultOr(error,
			SJME_ERROR_FONT_NEGATIVE_HEIGHT);
		goto fail_fontHeight;
	}
	
	/* Need the font baseline. */
	baseline = 0;
	if (sjme_error_is(error = font->font->apiInThread->metricPixelBaseline(
		font->font, &font->params, &baseline)))
		goto fail_fontBaseline;
	
	/* Get sequence length for further checking. */
	seqLen = -1;
	if (sjme_error_is(error = sjme_charSeq_length(s,
		&seqLen)) || seqLen < 0)
		goto fail_seqLen;
	
	/* Out of bounds? */
	if ((o + l) > seqLen)
	{
		error = SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		goto fail_seqBounds;
	}
	
	/* Determine visual size of this block of text. */
	tw = -1;
	if (sjme_error_is(error = font->font->apiInThread->stringWidth(font->font,
		&font->params, s, o, l, &tw)) || tw < 0)
		goto fail_blockDim;
	
	/* Determine anchor point of this block of text. */
	dx = x;
	dy = y;
	if (anchor != 0 && sjme_error_is(error =
		sjme_scritchpen_coreUtil_applyAnchor(
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
		if (sjme_error_is(error = g->apiInThread->drawChar(g, c, dx, dy,
			0, &cw)))
			goto fail_drawChar;
		
		/* Move over. */
		dx += cw;
	}
	
	/* Release lock. */
	if (sjme_error_is(error = sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_drawChar:
fail_charAt:
fail_anchor:
fail_blockDim:
fail_seqBounds:
fail_seqLen:
fail_fontBaseline:
fail_fontHeight:
fail_getFont:
	/* Need to release the lock? */
	if (sjme_error_is(sjme_scritchpen_core_lockRelease(g)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchpen_core_setFont(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_scritchui_pencilFont font,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* params)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont oldFont;
	sjme_scritchui_pencilFontWithParam* target;
	
	if (g == NULL || font == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Only adjust the base font if it is changing. */
	target = &g->state.font;
	if (font != target->font)
	{
		/* Clear old font, if one is used. */
		oldFont = target->font;
		target->font = NULL;

		/* Set font used. */
		target->font = sjme_weakUpR(sjme_scritchui_pencilFont, font);
	
		/* Count down. */
		if (oldFont != NULL)
			if (sjme_error_is(error = sjme_alloc_weakUnRef(oldFont)))
				return sjme_error_default(error);
	}
	
	/* Use parameters. */
	if (params != NULL)
	{
		/* Copy over. */
		memmove(&target->params, params, sizeof(target->params));
		
		/* Make sure the values are valid. */
		target->params.style &= SJME_SCRITCHUI_PENCIL_FONT_STYLE_ALL;
		target->params.pixelSize = sjme_max(1,
			target->params.pixelSize);
	}
	
	/* Derive from the base font otherwise. */
	else
	{
		target->params.style = font->id.param.style;
		target->params.pixelSize = font->id.param.pixelSize;
	}

	/* Resultant font not valid? */
	if (target->font == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
