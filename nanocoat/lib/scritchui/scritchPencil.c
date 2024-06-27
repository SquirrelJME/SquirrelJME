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
 * @param anchor 
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
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_core_pencilDrawChar(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInPositive sjme_jint c,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
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
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_core_pencilDrawHoriz(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint w)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x, &y);
	
	/* Use primitive. */
	return g->prim.drawHoriz(g, x, y, w);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawLine(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x1, &y1);
	sjme_scritchui_core_transform(g, &x2, &y2);
	
	/* Use primitive. */
	return g->prim.drawLine(g, x1, y1, x2, y2);
}

static sjme_errorCode sjme_scritchui_core_pencilDrawPixel(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x, &y);
	
	/* Use primitive. */
	return g->prim.drawPixel(g, x, y);
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
	
	/* Success? */
	return error;
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
	
	/* Need to get the height of a line. */
	lineHeight = -1;
	if (sjme_error_is(error = font->api->metricPixelHeight(font,
		&lineHeight)) || lineHeight < 0)
		goto fail_fontHeight;
	
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
		if (sjme_error_is(error = g->api->drawChar(g, c, dx, dy,
			0)))
			goto fail_drawChar;
		
		/* Move right. */
		cw = -1;
		if (sjme_error_is(error = font->api->pixelCharWidth(font,
			c, &cw)) || cw < 0)
			goto fail_charWidth;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_charWidth:
fail_drawChar:
fail_charAt:
fail_anchor:
fail_blockDim:
fail_seqLen:
fail_fontBaseline:
fail_fontHeight:
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
		
	/* Transform. */
	sjme_scritchui_core_transform(g, &x1, &y1);
	sjme_scritchui_core_transform(g, &x2, &y2);
	sjme_scritchui_core_transform(g, &x3, &y3);
	
	/* Clear error state. */
	error = SJME_ERROR_NONE;
	
	/* Draw lines via primitives. */
	g->prim.drawLine(g, x1, y1, x2, y2);
	g->prim.drawLine(g, x2, y2, x3, y3);
	g->prim.drawLine(g, x3, y3, x1, y1);
	
	/* Success? */
	return error;
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
	sjme_attrInValue sjme_jint anch,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight)
{
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
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
	
	/* Transform. */
	sjme_scritchui_core_transform(g, &x, &y);
	
	/* Cap width and height to 1 always. */
	if (w <= 0)
		w = 1;
	if (h <= 0)
		h = 1;
	
	/* Natively supported? */
	if (g->impl->fillRect != NULL)
		return g->impl->fillRect(g, x, y, w, h);
	
	/* Use primitives otherwise. */
	error = SJME_ERROR_NONE;
	drawHoriz = g->prim.drawHoriz;
	for (yz = y, yze = y + h; yz < yze; yz++)
		error |= drawHoriz(g, x, yz, w);
	
	/* Success? */
	return error;
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
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_core_pencilSetAlphaColor(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb)
{
	sjme_scritchui_pencilColor* target;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Implementation must have. */
	if (g->impl->setAlphaColor == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Set base color properties. */
	target = &g->state.color;
	target->argb = argb;
	target->a = (argb >> 24) & 0xFF;
	target->r = (argb >> 16) & 0xFF;
	target->g = (argb >> 8) & 0xFF;
	target->b = (argb) & 0xFF;
	
	/* Find closest indexed color. */
	if (g->palette.colors != NULL && g->palette.numColors > 0)
		sjme_todo("Color search?");
	
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
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_core_pencilSetDefaultFont(
	sjme_attrInNotNull sjme_scritchui_pencil g)
{
	sjme_errorCode error;
	
	if (g == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Clear font now. */
	g->state.font = NULL;
	
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
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositiveNonZero sjme_jint sw,
	sjme_attrInPositiveNonZero sjme_jint sh,
	sjme_attrInNullable sjme_frontEnd* copyFrontEnd)
{
	sjme_scritchui_pencilBase result;
	
	if (inPencil == NULL || inFunctions == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (sw <= 0 || sh <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (pf < 0 || pf >= SJME_NUM_GFX_PIXEL_FORMATS)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* These are required at the minimum. */
	if (inFunctions->drawLine == NULL && inFunctions->drawPixel == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
		
	/* Setup base result. */
	memset(&result, 0, sizeof(result));
	result.api = &sjme_scritchui_core_pencil;
	result.impl = inFunctions;
	result.pixelFormat = pf;
	
	/* Is there an alpha channel? */
	result.hasAlpha = (pf == SJME_GFX_PIXEL_FORMAT_INT_RGBA8888 ||
		pf == SJME_GFX_PIXEL_FORMAT_SHORT_RGBA4444 ||
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
