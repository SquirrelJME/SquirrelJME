/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"
#include "sjme/util.h"

/** Use a better barcode-like scaling algorithm I thought of. */
#define SJME_CONFIG_SCRITCHUI_FONT_BARCODE

static sjme_jboolean sjme_scritchui_pseudoEquals(
	sjme_attrInNullable sjme_scritchui_pencilFont a,
	sjme_attrInNullable sjme_scritchui_pencilFont b)
{
	if (a == NULL)
		return b == NULL;
	else if (b == NULL)
		return SJME_JNI_FALSE;
	
	sjme_todo("Impl?");
	return SJME_JNI_FALSE;
}

static sjme_errorCode sjme_scritchui_pseudoMetricCharValid(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_jboolean* outValid)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward. */
	return wrapped->api->metricCharValid(wrapped, inCodepoint,
		outValid);
}

static sjme_errorCode sjme_scritchui_pseudoMetricFontFace(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontFace* outFace)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL || outFace == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward. */
	return wrapped->api->metricFontFace(wrapped, outFace);
}

static sjme_errorCode sjme_scritchui_pseudoMetricFontName(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInOutNotNull sjme_lpcstr* outName)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL || outName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward. */
	return wrapped->api->metricFontName(wrapped, outName);
}

static sjme_errorCode sjme_scritchui_pseudoMetricFontStyle(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontStyle* outStyle)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Always use the cached base. */
	*outStyle = inFont->cache.style;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_pseudoMetricPixelAscent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outAscent)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont wrapped;
	sjme_jint orig;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Obtain original value. */
	orig = 0;
	if (sjme_error_is(error = wrapped->api->metricPixelAscent(
		wrapped, isMax, &orig)))
		return sjme_error_default(error);
	
	/* Perform fraction math on it, to normalize. */
	*outAscent = sjme_fixed_int(sjme_fixed_mul(sjme_fixed_hi(orig),
		inFont->cache.fraction));
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_pseudoMetricPixelBaseline(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_jint* outBaseline)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont wrapped;
	sjme_jint orig;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Obtain original value. */
	orig = 0;
	if (sjme_error_is(error = wrapped->api->metricPixelBaseline(
		wrapped, &orig)))
		return sjme_error_default(error);
	
	/* Perform fraction math on it, to normalize. */
	*outBaseline = sjme_fixed_int(sjme_fixed_mul(
		sjme_fixed_hi(orig), inFont->cache.fraction));
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_pseudoMetricPixelDescent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont wrapped;
	sjme_jint orig;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Obtain original value. */
	orig = 0;
	if (sjme_error_is(error = wrapped->api->metricPixelDescent(
		wrapped, isMax, &orig)))
		return sjme_error_default(error);
	
	/* Perform fraction math on it, to normalize. */
	*outDescent = sjme_fixed_int(sjme_fixed_mul(
		sjme_fixed_hi(orig), inFont->cache.fraction));
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_pseudoMetricPixelLeading(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont wrapped;
	sjme_jint orig;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Obtain original value. */
	orig = 0;
	if (sjme_error_is(error = wrapped->api->metricPixelLeading(
		wrapped, &orig)))
		return sjme_error_default(error);
	
	/* Perform fraction math on it, to normalize. */
	*outLeading = sjme_fixed_int(sjme_fixed_mul(
		sjme_fixed_hi(orig), inFont->cache.fraction));
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_pseudoMetricPixelSize(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Always use the cached base. */
	*outSize = inFont->cache.pixelSize;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_pseudoPixelCharWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward. */
	return wrapped->api->pixelCharWidth(wrapped, inCodepoint, outWidth);
}

static sjme_errorCode sjme_scritchui_pseudoRenderBitmap(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_jubyte* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufScanLen,
	sjme_attrInPositive sjme_jint bufHeight,
	sjme_attrOutNullable sjme_jint* outOffX,
	sjme_attrOutNullable sjme_jint* outOffY)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFont wrapped;
	sjme_jint origOffX, origOffY, scanLen, area, cw, ch;
	sjme_jubyte* src;
	sjme_jubyte* sp;
	sjme_jubyte* dp;
	sjme_jint dy, th, minScanLen, syInt;
	sjme_fixed sy, ifrac;
#if defined(SJME_CONFIG_SCRITCHUI_FONT_BARCODE)
	sjme_jint dx, syIntLast;
	sjme_jubyte* bar;
	sjme_jubyte* sup;
	sjme_jubyte orig, diff, bif, lim;
#endif
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Need character width. */
	cw = 0;
	if (sjme_error_is(error = wrapped->api->pixelCharWidth(
		wrapped, inCodepoint, &cw)))
		return sjme_error_default(error);
		
	/* And the pixel height, since this is a bitmap font. */
	ch = 0;
	if (sjme_error_is(error = wrapped->api->metricPixelSize(
		wrapped, &ch)))
		return sjme_error_default(error);
	
	/* Determine scanline length for each bitmap row. */
	scanLen = sjme_scritchui_pencilFontScanLen(cw);
	
	/* We do not want to write over other rows. */
	if (bufScanLen < scanLen)
		minScanLen = bufScanLen;
	else
		minScanLen = scanLen;
	
	/* Allocate source bitmap. */
	area = sizeof(*src) * (scanLen * ch);
	src = sjme_alloca(area);
	
#if defined(SJME_CONFIG_SCRITCHUI_FONT_BARCODE)
	/* Current barcode bits and suppressor. */
	bar = sjme_alloca(minScanLen);
	sup = sjme_alloca(minScanLen);
#endif
	
	/* Did any allocations fail? */
	if (
#if defined(SJME_CONFIG_SCRITCHUI_FONT_BARCODE)
		bar == NULL || sup == NULL ||
#endif
		src == NULL)
		return sjme_error_outOfMemory(NULL, area);
	
	/* Initialize. */
	memset(src, 0, area);
#if defined(SJME_CONFIG_SCRITCHUI_FONT_BARCODE)
	memset(bar, 0, minScanLen);
	memset(sup, 0, minScanLen);
	syIntLast = -1;
#endif
	
	/* Get original glyph bitmap. */
	origOffX = 0;
	origOffY = 0;
	if (sjme_error_is(error = wrapped->api->renderBitmap(wrapped,
		inCodepoint,
		src, 0, scanLen,
		ch, &origOffX, &origOffY)))
		goto fail_renderBitmap;
	
	/* Target desired pixel size. */
	th = inFont->cache.pixelSize;
	
	/* Copy rows, for every change in dy we grab from the source. */
	ifrac = inFont->cache.ifraction;
	for (dy = 0, sy = 0; dy < th && dy < bufHeight; dy++, sy += ifrac)
	{
		/* Normalize sy. */
		syInt = sjme_fixed_int(sjme_fixed_floor(sy));
		
		/* Determine where to move and copy from. */
		dp = &buf[bufOff + (dy * bufScanLen)];
		sp = &src[syInt * scanLen];
		
#if defined(SJME_CONFIG_SCRITCHUI_FONT_BARCODE)
		/* Use a new set of suppressor bits? */
		if (syIntLast != syInt)
		{
			memmove(sup, bar, minScanLen);
			syIntLast = syInt;
		}
		
		/* Run through each bit and draw the last barcode state. */
		for (dx = 0; dx < minScanLen; dx++)
		{
			/* Determine the new bit state, with any bits will change. */
			orig = bar[dx];
			diff = (orig ^ sp[dx]);
			bar[dx] ^= diff;
			
			/* Directly copy over, with suppressors in place. The */
			/* suppressors really only have an effect at very high scales */
			/* changes as they reduce some edge distortion. */
#if defined(SJME_I_REALLY_LIKE_THIS_BUT_THIS_IS_MISSING_INNER_LINES)
			/* The inner lines are missing for this, but this produces a */
			/* very clean thin line set otherwise. */
			dp[dx] = orig & (orig ^ (orig ^ sup[dx]));
#else
			/* There are very ugly looking thick horizontal bars due, these */
			/* can be removed with suppression however they end up leaving */
			/* gaps. Thus, we need to bifuricate bits to remove anything. */
			/* This essentially removes subsequent zeroes. */
			bif = 0;
#if 0
			bif = lim = sup[dx];
			bif = (bif << 1) & bif;
#endif
			
			/* The inner lines are normally missing for this, but this */
			/* produces a very clean thin line set otherwise. To get the */
			/* inner lines, the bifurication value is used. */
			dp[dx] = (orig & (orig ^ (orig ^ sup[dx]))) | bif;
#endif
		}
#else
		/* Copy entire scanline over (nearest scaling). */
		memmove(dp, sp, minScanLen);
#endif
	}
	
	/* X-axis is unchanged. */
	if (outOffX)
		*outOffX = origOffX;
	
	/* Translate height, so it actually offsets correctly! */
	if (outOffY)
		*outOffY = sjme_fixed_int(sjme_fixed_mul(
			sjme_fixed_hi(origOffY), inFont->cache.fraction));
	
	/* Cleanup. */
	sjme_alloca_free(src);
#if defined(SJME_CONFIG_SCRITCHUI_FONT_BARCODE)
	sjme_alloca_free(bar);
	sjme_alloca_free(sup);
#endif
	
	/* Success! */
	return SJME_ERROR_NONE;
	
fail_renderBitmap:
	if (src != NULL)
		sjme_alloca_free(src);
	
#if defined(SJME_CONFIG_SCRITCHUI_FONT_BARCODE)
	if (bar != NULL)
		sjme_alloca_free(bar);
	if (sup != NULL)
		sjme_alloca_free(sup);
#endif

	return sjme_error_default(error);
}

/** Functions for basic font support. */
static const sjme_scritchui_pencilFontImplFunctions
	sjme_scritchui_pseudoFontFunctions =
{
	sjme_sm(.driverName, "pseudo"),
	sjme_sm(.equals, sjme_scritchui_pseudoEquals),
	sjme_sm(.metricCharValid, sjme_scritchui_pseudoMetricCharValid),
	sjme_sm(.metricFontFace, sjme_scritchui_pseudoMetricFontFace),
	sjme_sm(.metricFontName, sjme_scritchui_pseudoMetricFontName),
	sjme_sm(.metricFontStyle, sjme_scritchui_pseudoMetricFontStyle),
	sjme_sm(.metricPixelAscent, sjme_scritchui_pseudoMetricPixelAscent),
	sjme_sm(.metricPixelBaseline, sjme_scritchui_pseudoMetricPixelBaseline),
	sjme_sm(.metricPixelDescent, sjme_scritchui_pseudoMetricPixelDescent),
	sjme_sm(.metricPixelLeading, sjme_scritchui_pseudoMetricPixelLeading),
	sjme_sm(.metricPixelSize, sjme_scritchui_pseudoMetricPixelSize),
	sjme_sm(.pixelCharWidth, sjme_scritchui_pseudoPixelCharWidth),
	sjme_sm(.renderBitmap, sjme_scritchui_pseudoRenderBitmap),
};

sjme_errorCode sjme_scritchui_core_fontPseudo(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_scritchui_pencilFontStyle inStyle,
	sjme_attrInPositiveNonZero sjme_jint inPixelSize,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outDerived)
{
	sjme_scritchui_pencilFont result;
	sjme_errorCode error;
	sjme_jint origPixelSize;
	sjme_fixed fraction, ifraction;
	
	if (inState == NULL || inFont == NULL || outDerived == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inPixelSize <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Is this already a pseudo font? */
	/* If it is then we do not want to derive from a derivation. */
	if (inFont->impl == &sjme_scritchui_pseudoFontFunctions)
		return sjme_scritchui_core_fontPseudo(inState,
			(sjme_scritchui_pencilFont)inFont->context, inStyle,
			inPixelSize, outDerived);
	
	/* We need the original pixel size to calculate the fraction. */
	origPixelSize = -1;
	if (sjme_error_is(error = inFont->api->metricPixelSize(inFont,
		&origPixelSize)) || origPixelSize < 0)
		return sjme_error_default(error);
	
	/* Calculate the font fraction. */
	fraction = sjme_fixed_fraction(inPixelSize, origPixelSize);
	ifraction = sjme_fixed_fraction(origPixelSize, inPixelSize);
	
	/* Allocate. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inState->pool,
		sizeof(*result), NULL, (void**)&result, NULL)))
		goto fail_alloc;
	
	/* Common initialize. */
	if (sjme_error_is(error = inState->intern->initCommon(inState,
		SJME_SUI_CAST_COMMON(result), SJME_JNI_FALSE,
		SJME_SCRITCHUI_TYPE_ROOT_STATE)))
		goto fail_commonInit;
	
	/* Setup new font. */
	result->impl = &sjme_scritchui_pseudoFontFunctions;
	result->context = inFont;
	result->cache.style = inStyle;
	result->cache.pixelSize = inPixelSize;
	result->cache.fraction = fraction;
	result->cache.ifraction = ifraction;
	
	/* Initialize base font. */
	if (sjme_error_is(error = sjme_scritchui_newPencilFontStatic(
		result)))
		goto fail_initBase;
	
	/* Give the font. */
	*outDerived = result;
	return SJME_ERROR_NONE;
	
fail_initBase:
fail_commonInit:
fail_alloc:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}
