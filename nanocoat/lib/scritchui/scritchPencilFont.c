/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiPencilFontPseudo.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"

static sjme_jboolean sjme_scritchui_fontEquals(
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

static sjme_errorCode sjme_scritchui_fontMetricCharDirection(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrInRange(-1, 1) sjme_jint* outDirection)
{
	if (inFont == NULL || outDirection == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontMetricCharValid(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_jboolean* outValid)
{
	if (inFont == NULL || outValid == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontMetricFontFace(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontFace* outFace)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFontFace result;
	
	if (inFont == NULL || outFace == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.face != 0)
	{
		*outFace = inFont->cache.face;
		return SJME_ERROR_NONE;
	}
	
	/* Not implemented? */
	if (inFont->impl->metricFontFace == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Load into cache. */
	result = 0;
	if (sjme_error_is(error = inFont->impl->metricFontFace(inFont,
		&result)) || result == 0)
		return sjme_error_default(error);
	
	/* Cache and use it. */
	inFont->cache.face = result;
	*outFace = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricFontName(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInOutNotNull sjme_lpcstr* outName)
{
	sjme_errorCode error;
	
	if (inFont == NULL || outName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need to load cache? */
	if (inFont->cache.name == NULL)
	{
		/* Not implemented? */
		if (inFont->impl->metricFontName == NULL)
			return SJME_ERROR_NOT_IMPLEMENTED;
		
		/* Use internal lookup. */
		if (sjme_error_is(error = inFont->impl->metricFontName(
			inFont, &inFont->cache.name)))
			return sjme_error_default(error);
	}
	
	/* Return the name. */
	*outName = inFont->cache.name;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricFontStyle(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontStyle* outStyle)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFontStyle result;
	
	if (inFont == NULL || outStyle == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.style != 0)
	{
		*outStyle = inFont->cache.style;
		return SJME_ERROR_NONE;
	}
	
	/* Not implemented? */
	if (inFont->impl->metricFontStyle != NULL)
	{
		/* Load into cache. */
		result = -1;
		if (sjme_error_is(error = inFont->impl->metricFontStyle(inFont,
			&result)) || result == -1)
			return sjme_error_default(error);
	}
	
	/* Font has no style otherwise. */
	else
		result = 0;
	
	/* Cache and use it. */
	inFont->cache.style = result;
	*outStyle = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelAscent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outAscent)
{
	if (inFont == NULL || outAscent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelBaseline(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_jint* outBaseline)
{
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelDescent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent)
{
	if (inFont == NULL || outDescent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelHeight(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_jint* outHeight)
{
	sjme_errorCode error;
	sjme_jint leading, ascent, descent;
	
	if (inFont == NULL || outHeight == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.height != 0)
	{
		*outHeight = inFont->cache.height;
		return SJME_ERROR_NONE;
	}
	
	/* Get all of these parameters. */
	leading = -1;
	ascent = -1;
	descent = -1;
	if (sjme_error_is(error = inFont->api->metricPixelLeading(inFont,
			&leading)) ||
		sjme_error_is(error = inFont->api->metricPixelAscent(inFont,
			SJME_JNI_FALSE, &ascent)) ||
		sjme_error_is(error = inFont->api->metricPixelDescent(inFont,
			SJME_JNI_FALSE, &descent)))
		return sjme_error_default(error);
	
	/* Calculate. */
	inFont->cache.height = leading + ascent + descent; 
	
	/* Success! */
	*outHeight = inFont->cache.height;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelLeading(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading)
{
	if (inFont == NULL || outLeading == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelSize(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.pixelSize != 0)
	{
		*outSize = inFont->cache.pixelSize;
		return SJME_ERROR_NONE;
	}
	
	/* Not implemented? */
	if (inFont->impl->metricPixelSize == NULL)
		return SJME_ERROR_NOT_IMPLEMENTED;
	
	/* Load into cache. */
	result = 0;
	if (sjme_error_is(error = inFont->impl->metricPixelSize(inFont,
		&result)) || result == 0)
		return sjme_error_default(error);
	
	/* Cache and use it. */
	inFont->cache.pixelSize = result;
	*outSize = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontPixelCharHeight(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outHeight)
{
	if (inFont == NULL || outHeight == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontPixelCharWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth)
{
	if (inFont == NULL || outWidth == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontRenderBitmap(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_jbyte* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufScanLen,
	sjme_attrInPositive sjme_jint surfaceX,
	sjme_attrInPositive sjme_jint surfaceY,
	sjme_attrInPositive sjme_jint surfaceW,
	sjme_attrInPositive sjme_jint surfaceH)
{
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontRenderChar(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInValue sjme_jint xPos,
	sjme_attrInNotNull sjme_jint yPos,
	sjme_attrOutNullable sjme_jint* nextXPos,
	sjme_attrOutNullable sjme_jint* nextYPos)
{
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontStringWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNotNull const sjme_charSeq* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrOutNotNull sjme_jint* outWidth)
{
	if (inFont == NULL || s == NULL || outWidth == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	if (o < 0 || l < 0 || (o + l) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

/** Functions for basic font support. */
static const sjme_scritchui_pencilFontFunctions sjme_scritchui_fontFunctions =
{
	.equals = sjme_scritchui_fontEquals,
	.metricCharDirection = sjme_scritchui_fontMetricCharDirection,
	.metricCharValid = sjme_scritchui_fontMetricCharValid,
	.metricFontFace = sjme_scritchui_fontMetricFontFace,
	.metricFontName = sjme_scritchui_fontMetricFontName,
	.metricFontStyle = sjme_scritchui_fontMetricFontStyle,
	.metricPixelAscent = sjme_scritchui_fontMetricPixelAscent,
	.metricPixelBaseline = sjme_scritchui_fontMetricPixelBaseline,
	.metricPixelDescent = sjme_scritchui_fontMetricPixelDescent,
	.metricPixelHeight = sjme_scritchui_fontMetricPixelHeight,
	.metricPixelLeading = sjme_scritchui_fontMetricPixelLeading,
	.metricPixelSize = sjme_scritchui_fontMetricPixelSize,
	.pixelCharHeight = sjme_scritchui_fontPixelCharHeight,
	.pixelCharWidth = sjme_scritchui_fontPixelCharWidth,
	.renderBitmap = sjme_scritchui_fontRenderBitmap,
	.renderChar = sjme_scritchui_fontRenderChar,
	.stringWidth = sjme_scritchui_fontStringWidth,
};

sjme_errorCode sjme_scritchui_core_fontDerive(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_scritchui_pencilFontStyle inStyle,
	sjme_attrInPositiveNonZero sjme_jint inPixelSize,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outDerived)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFontStyle wasStyle;
	sjme_jint wasPixelSize;
	
	if (inState == NULL || inFont == NULL || outDerived == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inPixelSize <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Limit. */
	inStyle &= SJME_SCRITCHUI_PENCIL_FONT_STYLE_ALL;
	
	/* Get old font properties. */
	wasStyle = 0;
	wasPixelSize = 0;
	if (sjme_error_is(error = inFont->api->metricFontStyle(inFont,
			&wasStyle)) ||
		sjme_error_is(error = inFont->api->metricPixelSize(inFont,
			&wasPixelSize)))
		return sjme_error_default(error);
	
	/* If the font is the same, do nothing. */
	if (wasStyle == inStyle && wasPixelSize == inPixelSize)
	{
		*outDerived = inFont;
		return SJME_ERROR_NONE;
	}
	
	/* Create pseudo font. */
	return sjme_scritchui_core_fontPseudo(inState, inFont, inStyle,
		inPixelSize, outDerived);
}

sjme_errorCode sjme_scritchui_newPencilFontStatic(
	sjme_scritchui_pencilFont inOutFont)
{
	if (inOutFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inOutFont->impl == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Set base fields. */
	inOutFont->api = &sjme_scritchui_fontFunctions;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
