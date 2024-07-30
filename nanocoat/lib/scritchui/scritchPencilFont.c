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

static sjme_errorCode sjme_scritchui_validateChar(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInOutNotNull sjme_jint* inOutCodepoint)
{
	sjme_errorCode error;
	sjme_jboolean isValid;
	
	if (inFont == NULL || inOutCodepoint == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Determine if this character is even valid. */
	isValid = SJME_JNI_FALSE;
	if (sjme_error_is(error = inFont->api->metricCharValid(inFont,
		*inOutCodepoint, &isValid)))
		return sjme_error_default(error);
	
	/* If it is not valid, then replace with the invalid character. */
	if (!isValid)
	{
		/* Try zero first. */
		*inOutCodepoint = 0;
		
		/* Check to see if zero is valid. */
		isValid = SJME_JNI_FALSE;
		if (sjme_error_is(error = inFont->api->metricCharValid(inFont,
			*inOutCodepoint, &isValid)))
			return sjme_error_default(error);
		
		/* If it is not, then likely the Unicode bad character is used. */
		if (!isValid)
			*inOutCodepoint = 0xFFFD;
	}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

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
	return sjme_error_notImplemented();
}

static sjme_errorCode sjme_scritchui_fontMetricCharValid(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_jboolean* outValid)
{
	if (inFont == NULL || outValid == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Not implemented? */
	if (inFont->impl->metricCharValid == NULL)
		return sjme_error_notImplemented();
	
	/* Negative codepoints are always invalid. */
	if (inCodepoint < 0)
	{
		*outValid = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}
	
	/* Forward. */
	return inFont->impl->metricCharValid(inFont, inCodepoint, outValid);
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
		return sjme_error_notImplemented();
	
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
			return sjme_error_notImplemented();
		
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
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outAscent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.ascent[!!isMax] != 0)
	{
		*outAscent = inFont->cache.ascent[!!isMax];
		return SJME_ERROR_NONE;
	}
	
	/* Not implemented? */
	if (inFont->impl->metricPixelAscent == NULL)
		return sjme_error_notImplemented();
	
	/* Load into cache. */
	result = 0;
	if (sjme_error_is(error = inFont->impl->metricPixelAscent(inFont,
		isMax, &result)))
		return sjme_error_default(error);
	
	/* Cache and use it. */
	inFont->cache.ascent[!!isMax] = result;
	*outAscent = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelBaseline(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_jint* outBaseline)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.baseline != 0)
	{
		*outBaseline = inFont->cache.baseline;
		return SJME_ERROR_NONE;
	}
	
	/* Not implemented? */
	if (inFont->impl->metricPixelBaseline == NULL)
		return sjme_error_notImplemented();
	
	/* Load into cache. */
	result = 0;
	if (sjme_error_is(error = inFont->impl->metricPixelBaseline(inFont,
		&result)))
		return sjme_error_default(error);
	
	/* Cache and use it. */
	inFont->cache.baseline = result;
	*outBaseline = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelDescent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outDescent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.descent[!!isMax] != 0)
	{
		*outDescent = inFont->cache.descent[!!isMax];
		return SJME_ERROR_NONE;
	}
	
	/* Not implemented? */
	if (inFont->impl->metricPixelDescent == NULL)
		return sjme_error_notImplemented();
	
	/* Load into cache. */
	result = 0;
	if (sjme_error_is(error = inFont->impl->metricPixelDescent(inFont,
		isMax, &result)))
		return sjme_error_default(error);
	
	/* Cache and use it. */
	inFont->cache.descent[!!isMax] = result;
	*outDescent = result;
	return SJME_ERROR_NONE;
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
	leading = 0;
	if (sjme_error_is(error = inFont->api->metricPixelLeading(inFont,
		&leading)))
		return sjme_error_default(error);
		
	ascent = 0;
	if (sjme_error_is(error = inFont->api->metricPixelAscent(inFont,
		SJME_JNI_FALSE, &ascent)))
		return sjme_error_default(error);
		
	descent = 0;
	if (sjme_error_is(error = inFont->api->metricPixelDescent(inFont,
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
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outLeading == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.leading != 0)
	{
		*outLeading = inFont->cache.leading;
		return SJME_ERROR_NONE;
	}
	
	/* Not implemented? */
	if (inFont->impl->metricPixelLeading == NULL)
		return sjme_error_notImplemented();
	
	/* Load into cache. */
	result = 0;
	if (sjme_error_is(error = inFont->impl->metricPixelLeading(inFont,
		&result)))
		return sjme_error_default(error);
	
	/* Cache and use it. */
	inFont->cache.leading = result;
	*outLeading = result;
	return SJME_ERROR_NONE;
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
		return sjme_error_notImplemented();
	
	/* Load into cache. */
	result = -1;
	if (sjme_error_is(error = inFont->impl->metricPixelSize(inFont,
		&result)) || result <= 0)
		return sjme_error_default(error);
	
	/* Cache and use it. */
	inFont->cache.pixelSize = result;
	*outSize = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontPixelCharWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outWidth == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not implemented? */
	if (inFont->impl->pixelCharWidth == NULL)
		return sjme_error_notImplemented();
	
	/* Codepoints with no actual length. */
	if (inCodepoint == '\r' || inCodepoint == '\n' ||
		inCodepoint == '\v' || inCodepoint == '\f' ||
		inCodepoint == 0x2060 || inCodepoint == 0xFEFF ||
		inCodepoint == 0x200D || inCodepoint == 0x200C)
	{
		*outWidth = 0;
		return SJME_ERROR_NONE;
	}
	
	/* Treat tabs as spaces. */
	if (inCodepoint == '\t')
		inCodepoint = ' ';
	
	/* Validate character to use. */
	if (sjme_error_is(error = sjme_scritchui_validateChar(inFont,
		&inCodepoint)))
		return sjme_error_default(error);
	
	/* Forward. */
	result = -1;
	if (sjme_error_is(error = inFont->impl->pixelCharWidth(inFont,
		inCodepoint, &result)) || result < 0)
		return sjme_error_default(error);
	
	/* Success! */
	*outWidth = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontRenderBitmap(
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
	
	if (inFont == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inCodepoint < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (bufOff < 0 || bufScanLen <= 0 || bufHeight <= 0 ||
		(bufHeight * bufScanLen) < 0 ||
		(bufOff + (bufHeight * bufScanLen)) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	/* Not implemented? */
	if (inFont->impl->renderBitmap == NULL)
		return sjme_error_notImplemented();
	
	/* Validate glyph. */
	if (sjme_error_is(error = sjme_scritchui_validateChar(inFont,
		&inCodepoint)))
		return sjme_error_default(error);
	
	/* Render resultant bitmap. */
	if (sjme_error_is(error = inFont->impl->renderBitmap(inFont,
		inCodepoint, buf, bufOff, bufScanLen, bufHeight, outOffX, outOffY)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
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
	return sjme_error_notImplemented();
}

static sjme_errorCode sjme_scritchui_fontStringWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNotNull const sjme_charSeq* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrOutNotNull sjme_jint* outWidth)
{
	sjme_errorCode error;
	sjme_jint seqLen, at, cw, result, maxResult;
	sjme_jchar c;
	
	if (inFont == NULL || s == NULL || outWidth == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	if (o < 0 || l < 0 || (o + l) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Get sequence length for further checking. */
	seqLen = -1;
	if (sjme_error_is(error = sjme_charSeq_length(s,
		&seqLen)) || seqLen < 0)
		return sjme_error_default(error);
	
	/* Out of bounds? */
	if ((o + l) > seqLen)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
		
	/* Defaults to zero length. */
	result = 0;
	maxResult = 0;
	
	/* Process each character within the sequence. */
	for (at = 0; at < l; at++)
	{
		/* Get the next character to check. */
		c = 0;
		if (sjme_error_is(error = sjme_charSeq_charAt(
			s, o + at, &c)))
			return sjme_error_default(error);
		
		/* Reset width? */
		if (c == '\r' || c == '\n')
		{
			result = 0;
			continue;
		}
		
		/* Determine character width. */
		cw = 0;
		if (sjme_error_is(error = inFont->api->pixelCharWidth(inFont,
			c, &cw)))
			return sjme_error_default(error);
		
		/* Add onto. */
		result += cw;
		
		/* New max? */
		if (result > maxResult)
			maxResult = result;
	}
	
	/* Success! */
	*outWidth = maxResult;
	return SJME_ERROR_NONE;
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
	.pixelCharWidth = sjme_scritchui_fontPixelCharWidth,
	.renderBitmap = sjme_scritchui_fontRenderBitmap,
	.renderChar = sjme_scritchui_fontRenderChar,
	.stringWidth = sjme_scritchui_fontStringWidth,
};

sjme_errorCode sjme_scritchui_core_intern_fontBuiltin(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont)
{
	sjme_scritchui topState;
	
	if (inState == NULL || outFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If we are using a top level state, grab the font from there so */
	/* that we have a consistent default to use. */
	topState = sjme_atomic_sjme_pointer_get(&inState->topState);
	if (topState != NULL)
		return topState->api->fontBuiltin(topState, outFont);
	
	return inState->api->fontBuiltin(inState, outFont);
}

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

sjme_jint sjme_scritchui_pencilFontScanLen(
	sjme_attrInPositive sjme_jint w)
{
	return (w >> 3) + ((w & 7) != 0 ? 1 : 0);
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
