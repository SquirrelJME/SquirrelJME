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
#include "lib/scritchui/scritchuiPencilFontPseudo.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/debug.h"
#include "sjme/util.h"

/** The size to grow the font list by. */
#define SJME_FONT_LIST_GROW 16

static sjme_errorCode sjme_scritchui_fromCache(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_list(sjme_scritchui_pencilFont)* outFonts,
	sjme_attrOutNotNull sjme_jint* outValid,
	sjme_attrOutNullable sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_jint limit, i, n;
	sjme_list(sjme_scritchui_pencilFont)* fontCache;
	
	if (inState == NULL || outFonts == NULL || outValid == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Font list already cached? */
	fontCache = inState->font.fontRegister;
	if (fontCache == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Determine how many fonts can actually be stored. */
	n = outFonts->length;
	limit = (n < fontCache->length ? n : fontCache->length);

	/* Copy over from the cache. */
	for (i = 0; i < limit; i++)
		outFonts->elements[i] = fontCache->elements[i];
	for (; i < n; i++)
		outFonts->elements[i] = NULL;

	/* Set resultant count. */
	*outValid = limit;
	
	/* Report the max number of fonts, if requested. */
	if (outCount != NULL)
		*outCount = fontCache->length;

	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_jint sjme_scritchui_core_fontScore(
	sjme_attrInNotNull const sjme_scritchui_pencilFontId* desireId,
	sjme_attrInNotNull const sjme_scritchui_pencilFontCompare* against)
{
	sjme_jint penalty;
	
	if (desireId == NULL || against == NULL || against->font == NULL)
		return INT32_MAX;
	
	/* Symbol fonts are never comparable. */
	if ((desireId->face & SJME_SCRITCHUI_PENCIL_FONT_FACE_SYMBOL) != 0 ||
		(against->id.face & SJME_SCRITCHUI_PENCIL_FONT_FACE_SYMBOL) != 0)
		return INT32_MAX;
	
	/* Start with no penalty. */
	penalty = 0;
	
	/* If the face is different, increase the penalty greatly */
	if (desireId->face != against->id.face)
		penalty += 1024;
	
	/* Otherwise, only increase the penalty slightly if the name differs. */
	else if (!strncmp(desireId->name, against->id.name,
		SJME_MAX_FONT_NAME))
		penalty += 32;
	
	/* If the style is different increase the penalty but not as much */
	/* for every bit that is different. */
	if (desireId->style != against->id.style)
		penalty += 64 * sjme_util_intBitCountU(
			desireId->style ^ against->id.style);
	
	/* Penalize based on the size. */
	penalty += abs(desireId->pixelSize - against->id.pixelSize);
	
	/* Return the final penalty. */
	return penalty;
}

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
	return sjme_error_notImplemented(0);
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
		return sjme_error_notImplemented(0);
	
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
	
	/* Use from the ID. */
	*outFace = inFont->id.face;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricFontName(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInOutNotNull sjme_lpcstr* outName)
{
	sjme_errorCode error;
	
	if (inFont == NULL || outName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Use from the ID. */
	*outName = inFont->id.name;
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
	
	/* Use from the ID. */
	*outStyle = inFont->id.style;
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
		return sjme_error_notImplemented(0);
	
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
		return sjme_error_notImplemented(0);
	
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
		return sjme_error_notImplemented(0);
	
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
		return sjme_error_notImplemented(0);
	
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
	sjme_attrInNegativeOnePositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inCodepoint < -1)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Codepoint specified and the implementation has pixel size */
	/* implementation? */
	if (inCodepoint >= 0 && inFont->impl->metricPixelSize != NULL)
		if (sjme_error_is(error = inFont->impl->metricPixelSize(inFont,
			inCodepoint, &result)) || result <= 0)
			return sjme_error_default(error);
	
	/* Otherwise, use the pixel size from the ID. */
	*outSize = inFont->id.pixelSize;
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
		return sjme_error_notImplemented(0);
	
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
		return sjme_error_notImplemented(0);
	
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
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_scritchui_fontStringWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNotNull const sjme_charSeq s,
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
	sjme_sm(.equals, sjme_scritchui_fontEquals),
	sjme_sm(.metricCharDirection, sjme_scritchui_fontMetricCharDirection),
	sjme_sm(.metricCharValid, sjme_scritchui_fontMetricCharValid),
	sjme_sm(.metricFontFace, sjme_scritchui_fontMetricFontFace),
	sjme_sm(.metricFontName, sjme_scritchui_fontMetricFontName),
	sjme_sm(.metricFontStyle, sjme_scritchui_fontMetricFontStyle),
	sjme_sm(.metricPixelAscent, sjme_scritchui_fontMetricPixelAscent),
	sjme_sm(.metricPixelBaseline, sjme_scritchui_fontMetricPixelBaseline),
	sjme_sm(.metricPixelDescent, sjme_scritchui_fontMetricPixelDescent),
	sjme_sm(.metricPixelHeight, sjme_scritchui_fontMetricPixelHeight),
	sjme_sm(.metricPixelLeading, sjme_scritchui_fontMetricPixelLeading),
	sjme_sm(.metricPixelSize, sjme_scritchui_fontMetricPixelSize),
	sjme_sm(.pixelCharWidth, sjme_scritchui_fontPixelCharWidth),
	sjme_sm(.renderBitmap, sjme_scritchui_fontRenderBitmap),
	sjme_sm(.renderChar, sjme_scritchui_fontRenderChar),
	sjme_sm(.stringWidth, sjme_scritchui_fontStringWidth),
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
	topState = sjme_atomic_g(sjme_pointer, &inState->topState);
	if (topState != NULL)
		return topState->api->fontBuiltin(topState, outFont);
	
	return inState->api->fontBuiltin(inState, outFont);
}

sjme_errorCode sjme_scritchui_core_intern_fontRegister(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isPseudo)
{
	sjme_errorCode error;
	sjme_scritchui_fontState* fontState;
	sjme_list_sjme_scritchui_pencilFont** whichRegister;
	sjme_scritchui_pencilFont* freeSlot;
	sjme_scritchui_pencilFont check;
	sjme_jint i, n;
	
	if (inState == NULL || inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We operate with the font state */
	fontState = &inState->font;
	
	/* Check the font register if this font already exists. */
	freeSlot = NULL;
	whichRegister = (isPseudo ? &fontState->pseudoRegister :
		&fontState->fontRegister);
	n = 0;
	if ((*whichRegister) != NULL)
		for (n = (*whichRegister)->length, i = 0; i < n; i++)
		{
			/* Ignore blank slots. */
			check = (*whichRegister)->elements[0];
			if (check == NULL)
			{
				if (freeSlot == NULL)
					freeSlot = &(*whichRegister)->elements[0];
				continue;
			}
			
			/* If the ID is a perfect match, skip. */
			if (0 == memcmp(&check->id, &inFont->id,
				sizeof(sjme_scritchui_pencilFontId)))
				return SJME_ERROR_FONT_ALREADY_REGISTERED;
		}
	
	/* Truly need to grow the list? */
	if (freeSlot == NULL)
	{
		/* Resize the list. */
		if (sjme_error_is(error = sjme_list_replace(inState->pool,
			n + SJME_FONT_LIST_GROW, whichRegister,
			sjme_scritchui_pencilFont, 0)))
			return sjme_error_default(error);
		
		/* Free slot at the start of the resized list. */
		freeSlot = &(*whichRegister)->elements[n];
	}
	
	/* Place in the free slot. */
	*freeSlot = sjme_weakUpR(sjme_scritchui_pencilFont, inFont);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_intern_fontScanAll(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_jint* outCount)
{
	sjme_errorCode error, errorSystem, errorResource, errorFallback;
	sjme_jint total;
	sjme_jint current;
	sjme_scritchui_fontState* fontState;
	sjme_scritchui_pencilFont builtin;
	sjme_scritchui wrappedState;
	
	if (inState == NULL || outCount == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If wrapped, always use the underlying layer fonts. */
	wrappedState = inState->wrappedState;
	if (wrappedState != NULL)
		return wrappedState->intern->fontScanAll(wrappedState,
			outCount);
	
	/* Recover and check the state. */
	fontState = &inState->font;
	if (fontState->fontRegister != NULL && fontState->scanTotal > 0)
	{
		*outCount = fontState->scanTotal;
		return SJME_ERROR_NONE;
	}
	
	/* Start at nothing. */
	total = 0;
	
	/* Scan resource fonts. */
	current = 0;
	errorResource = inState->intern->fontScanResource(inState, &current);
	
	/* Add to the total. */
	if (!sjme_error_is(errorResource))
		total += sjme_max(0, current);
	
	/* There might not be a system font implementation. */
	errorSystem = SJME_ERROR_NONE;
	if (inState->impl->fontScanSystem != NULL)
	{
		/* Perform the scan. */
		current = 0;
		errorSystem = inState->impl->fontScanSystem(inState, &current);
		
		/* Add to the total. */
		if (!sjme_error_is(errorSystem))
			total += sjme_max(0, current);
	}
	
	/* Load the builtin font. */
	builtin = NULL;
	errorFallback = inState->intern->fontBuiltin(inState, &builtin);
	
	/* Register the fallback font. */
	if (!sjme_error_is(errorFallback))
	{
		/* Attempt registration. */
		errorFallback = inState->intern->fontRegister(inState, builtin,
			SJME_JNI_FALSE);
		
		/* Since the builtin was registered, count it up. */
		if (!sjme_error_is(errorFallback))
			total += 1;
	}
	
	/* Since all fonts were scanned, set as such. */
	fontState->scanTotal = total;
	*outCount = total;
	
	/* Did any fail? */
	if (sjme_error_is(errorSystem))
		return sjme_error_default(errorSystem);
	if (sjme_error_is(errorResource))
		return sjme_error_default(errorResource);
	if (sjme_error_is(errorFallback))
		return sjme_error_default(errorFallback);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_fontCount(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_jint* outCount)
{
	sjme_scritchui wrappedState;
	
	if (inState == NULL || outCount == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If wrapped, always use the underlying layer fonts. */
	wrappedState = inState->wrappedState;
	if (wrappedState != NULL)
		return wrappedState->apiInThread->fontCount(wrappedState,
			outCount);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_core_fontDerive(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNullable sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable sjme_lpcstr inName,
	sjme_attrInValue sjme_scritchui_pencilFontFace inFace,
	sjme_attrInValue sjme_scritchui_pencilFontStyle inStyle,
	sjme_attrInPositiveNonZero sjme_jint inPixelSize,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outDerived)
{
	sjme_errorCode error;
	sjme_scritchui_pencilFontStyle wasStyle;
	sjme_jint wasPixelSize, ignored, step, i, n, j;
	sjme_jint scoreChosen, scoreDerive;
	sjme_scritchui wrappedState;
	sjme_list_sjme_scritchui_pencilFont** whichRegister;
	sjme_scritchui_fontState* fontState;
	sjme_scritchui_pencilFontId desireId;
	sjme_scritchui_pencilFontCompare chosen;
	sjme_scritchui_pencilFontCompare derive;
	
	if (inState == NULL || outDerived == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inPixelSize <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* If wrapped, always use the underlying layer fonts. */
	wrappedState = inState->wrappedState;
	if (wrappedState != NULL)
		return wrappedState->apiInThread->fontDerive(wrappedState,
			inFont, inName, inFace, inStyle, inPixelSize, outDerived);
	
	/* If no fonts have been scanned, scan every one. */
	ignored = 0;
	if (inState->font.fontRegister == NULL || inState->font.scanTotal <= 0)
		if (sjme_error_is(error = inState->intern->fontScanAll(inState,
			&ignored)))
			return sjme_error_default(error);
	
	/* Debug. */
	sjme_messageB("deriveFont(%p %s %d %d %d)",
		inFont, inName, inFace, inStyle, inPixelSize);
	
	/* There is no best font, yet. */
	memset(&derive, 0, sizeof(derive));
	
	/* Build the ID of the font we desire. */
	memset(&desireId, 0, sizeof(desireId));
	desireId.pixelSize = inPixelSize;
	
	/* Name. */
	if (inName != NULL)
		strncpy(desireId.name, inName, SJME_MAX_FONT_NAME - 1);
	else if (inFont != NULL)
		strncpy(desireId.name, inFont->id.name, SJME_MAX_FONT_NAME - 1);
	
	/* Face. */
	if (inFace != SJME_SCRITCHUI_PENCIL_FONT_FACE_AUTOMATIC)
		desireId.face = inFace;
	else if (inFont != NULL)
		desireId.face = inFont->id.face;
	else
		desireId.face = SJME_SCRITCHUI_PENCIL_FONT_FACE_NORMAL;
	
	/* Style. */
	if (inStyle != SJME_SCRITCHUI_PENCIL_FONT_STYLE_AUTOMATIC)
		desireId.style = inStyle;
	else if (inFont != NULL)
		desireId.style = inFont->id.style;
	else
		desireId.style = SJME_SCRITCHUI_PENCIL_FONT_STYLE_PLAIN;
	
	/* Check already registered real and pseudo fonts. */
	fontState = &inState->font;
	for (step = 0; step < 2; step++)
	{
		/* Which font are we looking for? */
		whichRegister = (step == 1 ? &fontState->pseudoRegister :
			&fontState->fontRegister);
		
		/* No fonts in this group? */
		if ((*whichRegister) == NULL)
			continue;
		
		/* Scan the set of fonts. */
		for (n = (*whichRegister)->length, i = 0; i < n; i++)
		{
			/* Ignore blank slots. */
			memset(&chosen, 0, sizeof(chosen));
			chosen.font = (*whichRegister)->elements[i];
			if (chosen.font == NULL)
				continue;
			
			/* Normalize chosen's ID, remove automatics. */
			memmove(&chosen.id, &chosen.font->id, sizeof(chosen.id));
			if (0 != (chosen.id.style &
				SJME_SCRITCHUI_PENCIL_FONT_STYLE_AUTOMATIC))
				chosen.id.style = desireId.style;
			if (0 != (chosen.id.face &
				SJME_SCRITCHUI_PENCIL_FONT_FACE_AUTOMATIC))
				chosen.id.face = desireId.face;
			
			/* Exact match? */
			if (0 == memcmp(&desireId, &chosen.id, sizeof(chosen.id)))
			{
				*outDerived = chosen.font;
				return SJME_ERROR_NONE;
			}
			
			/* Otherwise score both. */
			scoreChosen = sjme_scritchui_core_fontScore(&desireId, &chosen);
			scoreDerive = sjme_scritchui_core_fontScore(&desireId, &derive);
			
			/* Debug. */
			sjme_message("deriveFont(): %s %d ?= %s %d",
				chosen.id.name, scoreChosen,
				derive.id.name, scoreDerive);
			
			/* The lower the penalty the better. */
			if (scoreChosen < scoreDerive && scoreChosen != INT32_MAX &&
				scoreChosen != INT32_MIN)
				memmove(&derive, &chosen, sizeof(chosen));
		}
	}
	
	/* If we found no candidate fonts, then we need to fallback. */
	if (derive.font == NULL)
	{
		/* Do not infinite loop trying to find the fallback font. */
		if (desireId.name[0] != '\0' && 0 == strcmp("fallback", desireId.name))
			return SJME_ERROR_INVALID_FONT;
		
		/* Run this again, with the fallback font specified. */
		return sjme_scritchui_core_fontDerive(inState, NULL, "fallback",
			inFace, inStyle, inPixelSize, outDerived);
	}
	
	/* If this is a size and style match, use this font. */
	if (desireId.pixelSize == derive.id.pixelSize &&
		desireId.style == derive.id.style)
	{
		*outDerived = derive.font;
		return SJME_ERROR_NONE;
	}
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#if 0
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
#endif
}

sjme_errorCode sjme_scritchui_core_fontList(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_list(sjme_scritchui_pencilFont)* outFonts,
	sjme_attrOutNotNull sjme_jint* outValid,
	sjme_attrOutNullable sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_jint limit, i, n;
	sjme_list(sjme_scritchui_pencilFont)* fontCache;
	sjme_scritchui wrappedState;
	
	if (inState == NULL || outFonts == NULL || outValid == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* If wrapped, always use the underlying layer fonts. */
	wrappedState = inState->wrappedState;
	if (wrappedState != NULL)
		return wrappedState->apiInThread->fontList(wrappedState,
			outFonts, outValid, outCount);

	/* Font list already cached? */
	fontCache = inState->font.fontRegister;
	if (fontCache != NULL)
		return sjme_scritchui_fromCache(inState, outFonts, outValid,
			outCount);
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);

	/* Font cache was set, so load that in. */
	return sjme_scritchui_fromCache(inState, outFonts, outValid,
		outCount);
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
