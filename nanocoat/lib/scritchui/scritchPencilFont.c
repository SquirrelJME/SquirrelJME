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
#include "lib/scritchui/scritchuiStatePencil.h"
#include "sjme/debug.h"
#include "sjme/fixed.h"
#include "sjme/util.h"

#include <lib/scritchui/core/coreSerial.h>

/** Use a better barcode-like scaling algorithm I thought of. */
#define SJME_CONFIG_SCRITCHUI_FONT_BARCODE

/** The size to grow the font list by. */
#define SJME_FONT_LIST_GROW 16

/** Horizontal scaler for font values. */
#define sjme_scritchui_fontScaleH(inFont, inParams, value) \
	(value)

/** Vertical scaler for font values. */
#define sjme_scritchui_fontScaleV(inFont, inParams, value) \
	(inParams == NULL ? (value) : \
	sjme_fixed_intClip(1, \
		sjme_fixed_mul(sjme_fixed_hi((value)), \
		sjme_fixed_fraction((inParams)->pixelSize, \
		(inFont)->id.param.pixelSize)), \
	INT32_MAX))

typedef struct sjme_scritchui_fontByFaceData
{
	/** The font face. */
	sjme_scritchui_pencilFontFace inFace;

	/** Font parameters. */
	sjme_scritchui_pencilFontParam inParams;

	/** The currently selected font. */
	sjme_scritchui_pencilFontCompare derive;
} sjme_scritchui_fontByFaceData;

typedef struct sjme_scritchui_fontDeriveData
{
	/** Score of the best font. */
	sjme_jint resultScore;

	/** The currently selected font. */
	sjme_scritchui_pencilFont result;

	/** The font parameters to derive to. */
	sjme_scritchui_pencilFontParam deriveParams;
} sjme_scritchui_fontDeriveData;

static sjme_errorCode sjme_scritchui_core_fontByFaceIterator(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_fontIterateStep* inOutStep)
{
	sjme_scritchui_fontByFaceData* data;

	if (inState == NULL || inOutStep == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover data. */
	data = (sjme_scritchui_fontByFaceData*)inOutStep->data;
	if (data == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* If the face matches, return the found font. */
	if (inOutStep->current->id.face == data->inFace)
	{
		memset(&data->derive, 0, sizeof(data->derive));
		data->derive.font = inOutStep->current;
		memmove(&data->derive.id, &inOutStep->current->id,
			sizeof(data->derive.id));

		return SJME_ERROR_STOP;
	}

	/* Continue. */
	return SJME_ERROR_CONTINUE;
}

static sjme_jint sjme_scritchui_core_fontScore(
	sjme_attrInNotNull sjme_scritchui_pencilFont current,
	sjme_attrInNotNull sjme_scritchui_pencilFont against,
	sjme_attrInNotNull const sjme_scritchui_pencilFontParam* desireParam)
{
	sjme_jint penalty;

	if (current == NULL || against == NULL || desireParam == NULL)
		return INT32_MAX;

	/* Symbol fonts are never comparable. */
	if ((current->id.face & SJME_SCRITCHUI_PENCIL_FONT_FACE_SYMBOL) != 0 ||
		(against->id.face & SJME_SCRITCHUI_PENCIL_FONT_FACE_SYMBOL) != 0)
		return INT32_MAX;

	/* Stylistic fonts are never comparable. */
	if ((current->id.face & SJME_SCRITCHUI_PENCIL_FONT_FACE_STYLISTIC) != 0 ||
		(against->id.face & SJME_SCRITCHUI_PENCIL_FONT_FACE_STYLISTIC) != 0)
		return INT32_MAX;

	/* Fonts with a different name, never compare. */
	if (!strncmp(current->id.name, against->id.name,
		SJME_MAX_FONT_NAME))
		return INT32_MAX;

	/* If the face is different, maybe same family but different stroke? */
	if (current->id.face != against->id.face)
		return INT32_MAX;

	/* Start with no penalty, as this is reasonably the same font. */
	penalty = 0;

	/* If the style is different increase the penalty but not as much */
	/* for every bit that is different. */
	if (desireParam->style != against->id.param.style)
		penalty += 64 * sjme_util_intBitCountU(
			desireParam->style ^ against->id.param.style);

	/* Penalize based on the size. */
	penalty += abs(desireParam->pixelSize - against->id.param.pixelSize);

	/* Return the final penalty. */
	return penalty;
}

static sjme_errorCode sjme_scritchui_core_fontDeriveIterator(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_fontIterateStep* inOutStep)
{
	sjme_errorCode error;
	sjme_scritchui_fontDeriveData* data;
	sjme_jint thisScore;

	if (inState == NULL || inOutStep == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover data. */
	data = (sjme_scritchui_fontDeriveData*)inOutStep->data;
	if (data == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Compare the best font against the current step font. */
	thisScore = sjme_scritchui_core_fontScore(data->result,
		inOutStep->current, &data->deriveParams);

	/* Is this a better font? */
	if (thisScore < data->resultScore)
	{
		/* Use this better font. */
		data->result = inOutStep->current;

		/* Are we at the best score possible? */
		data->resultScore = thisScore;
		if (thisScore == 0)
			return SJME_ERROR_STOP;
	}

	/* Keep going. */
	return SJME_ERROR_CONTINUE;
}

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

static sjme_errorCode sjme_scritchui_validateChar(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInOutNotNull sjme_jint* inOutCodepoint)
{
	sjme_errorCode error;
	sjme_jboolean isValid;
	sjme_jint tryCodepoint;
	
	if (inFont == NULL || inOutCodepoint == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Should be implemented. */
	if (inFont->apiInThread == NULL ||
		inFont->apiInThread->metricCharValid == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
	
	/* The codepoint to try. */
	tryCodepoint = *inOutCodepoint;
	
	/* Determine if this character is even valid. */
	isValid = SJME_JNI_FALSE;
	if (sjme_error_is(error = inFont->apiInThread->metricCharValid(inFont,
		tryCodepoint, &isValid)))
		return sjme_error_default(error);

#if defined(SJME_DISABLED_OUTSIDE_FONT_BRANCH)
	/* If not valid, look at lower priority fonts preferably with the same */
	/* pixel size for a contained glyph that we can for rendering or for */
	/* grabbing glyph details. */
	if (!isValid)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
#endif
	
	/* If it is still not valid, then replace with the invalid character. */
	if (!isValid)
	{
		/* Try zero first. */
		tryCodepoint = 0;
		
		/* Check to see if zero is valid. */
		isValid = SJME_JNI_FALSE;
		if (sjme_error_is(error = inFont->apiInThread->metricCharValid(inFont,
			tryCodepoint, &isValid)))
			return sjme_error_default(error);
		
		/* If it is not, then likely the Unicode replacement character is */
		/* used instead. */
		if (!isValid)
			tryCodepoint = 0xFFFD;
	}

#if defined(SJME_DISABLED_OUTSIDE_FONT_BRANCH)
	/* Give the actual font and glyph information that was discovered, since */
	/* this could be in a completely different font. */
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#endif
	
	/* Success! */
	*inOutCodepoint = tryCodepoint;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontEquals(
	sjme_attrInNullable sjme_scritchui_pencilFont a,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* aParams,
	sjme_attrInNullable sjme_scritchui_pencilFont b,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* bParams)
{
	if (a == NULL)
		return b == NULL;
	else if (b == NULL)
		return SJME_ERROR_NOT_MATCHED;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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

	/* Invalid state? */
	if (inFont->impl == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
		
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
	*outStyle = inFont->id.param.style;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelAscent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outAscent)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outAscent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.ascent[!!isMax] != 0)
		result = inFont->cache.ascent[!!isMax];
	
	/* Load from the cache? */
	else
	{
		/* Invalid state? */
		if (inFont->impl == NULL)
			return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);

		/* Not implemented? */
		if (inFont->impl->metricPixelAscent == NULL)
			return sjme_error_notImplemented(0);
		
		/* Load into cache. */
		result = 0;
		if (sjme_error_is(error = inFont->impl->metricPixelAscent(inFont,
			NULL, isMax, &result)))
			return sjme_error_default(error);
		
		/* Cache and use it. */
		inFont->cache.ascent[!!isMax] = result;
	}
	
	/* Perform scaling, if required. */
	*outAscent = sjme_scritchui_fontScaleV(inFont, inParams, result);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelBaseline(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_jint* outBaseline)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.baseline != 0)
		result = inFont->cache.baseline;
	else
	{
		/* Invalid state? */
		if (inFont->impl == NULL)
			return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);

		/* Not implemented? */
		if (inFont->impl->metricPixelBaseline == NULL)
			return sjme_error_notImplemented(0);
	
		/* Load into cache. */
		result = 0;
		if (sjme_error_is(error = inFont->impl->metricPixelBaseline(inFont,
			NULL, &result)))
			return sjme_error_default(error);
		
		/* Cache and use it. */
		inFont->cache.baseline = result;
	}
	
	/* Cache and use it. */
	*outBaseline = sjme_scritchui_fontScaleV(inFont, inParams, result);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelDescent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outDescent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Cached? */
	if (inFont->cache.descent[!!isMax] != 0)
		result = inFont->cache.descent[!!isMax];
	else
	{
		/* Invalid state? */
		if (inFont->impl == NULL)
			return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);

		/* Not implemented? */
		if (inFont->impl->metricPixelDescent == NULL)
			return sjme_error_notImplemented(0);
	
		/* Load into cache. */
		result = 0;
		if (sjme_error_is(error = inFont->impl->metricPixelDescent(inFont,
			NULL, isMax, &result)))
			return sjme_error_default(error);
	
		/* Cache and use it. */
		inFont->cache.descent[!!isMax] = result;
	}
	
	*outDescent = sjme_scritchui_fontScaleV(inFont, inParams, result);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelHeight(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_jint* outHeight)
{
	sjme_errorCode error;
	sjme_jint leading, ascent, descent;
	
	if (inFont == NULL || outHeight == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* These should be implemented. */
	if (inFont->apiInThread == NULL ||
		inFont->apiInThread->metricPixelLeading == NULL ||
		inFont->apiInThread->metricPixelAscent == NULL ||
		inFont->apiInThread->metricPixelDescent == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
	
	/* Get all of these parameters, as caching individual heights can lead */
	/* to multiplicative errors. */
	leading = 0;
	if (sjme_error_is(error = inFont->apiInThread->metricPixelLeading(inFont,
		inParams, &leading)))
		return sjme_error_default(error);
		
	ascent = 0;
	if (sjme_error_is(error = inFont->apiInThread->metricPixelAscent(inFont,
		inParams, SJME_JNI_FALSE, &ascent)))
		return sjme_error_default(error);
		
	descent = 0;
	if (sjme_error_is(error = inFont->apiInThread->metricPixelDescent(inFont,
		inParams, SJME_JNI_FALSE, &descent)))
		return sjme_error_default(error);
	
	/* Do not scale, use a consistent value here */
	*outHeight = leading + ascent + descent;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelLeading(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outLeading == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cached? */
	if (inFont->cache.leading != 0)
		result = inFont->cache.leading;
	else
	{
		/* Invalid state? */
		if (inFont->impl == NULL)
			return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);

		/* Not implemented? */
		if (inFont->impl->metricPixelLeading == NULL)
			return sjme_error_notImplemented(0);

		/* Load into cache. */
		result = 0;
		if (sjme_error_is(error = inFont->impl->metricPixelLeading(inFont,
			NULL, &result)))
			return sjme_error_default(error);

		/* Cache and use it. */
		inFont->cache.leading = result;
	}

	/* Scale result. */
	*outLeading = sjme_scritchui_fontScaleV(inFont, inParams, result);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelSize(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInNegativeOnePositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize)
{
	if (inFont == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inCodepoint < -1)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* If input parameters are passed, this is the size specified there. */
	if (inParams != NULL)
		*outSize = inParams->pixelSize;
	
	/* Otherwise the size is derived from the ID. */
	else
		*outSize = inFont->id.param.pixelSize;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_fontPixelCharWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth)
{
	sjme_errorCode error;
	sjme_jint result;
	
	if (inFont == NULL || outWidth == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Invalid state? */
	if (inFont->impl == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
	
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
		NULL, inCodepoint, &result)) || result < 0)
		return sjme_error_default(error);
	
	/* Success! */
	*outWidth = sjme_scritchui_fontScaleH(inFont, inParams, result);
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_renderBitmapScaled(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNotNull const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_jubyte* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufScanLen,
	sjme_attrInPositive sjme_jint bufHeight,
	sjme_attrOutNullable sjme_jint* outOffX,
	sjme_attrOutNullable sjme_jint* outOffY)
{
	sjme_errorCode error;
	sjme_jint origOffX, origOffY, scanLen, area, cw, ch;
	sjme_jubyte* src;
	sjme_jubyte* sp;
	sjme_jubyte* dp;
	sjme_jint dy, th, minScanLen, syInt;
	sjme_fixed sy, fraction, ifraction;
#if defined(SJME_CONFIG_SCRITCHUI_FONT_BARCODE)
	sjme_jint dx, syIntLast;
	sjme_jubyte* bar;
	sjme_jubyte* sup;
	sjme_jubyte orig, diff, cmp, lim;
#endif
	
	if (inFont == NULL || inParams == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inCodepoint < -1)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Invalid state? Note that impl renderBitmap is valid here! */
	if (inFont->impl == NULL || inFont->apiInThread == NULL ||
		inFont->apiInThread->pixelCharWidth == NULL ||
		inFont->apiInThread->metricPixelSize == NULL ||
		inFont->impl->renderBitmap == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
	
	/* Need the original character width. */
	cw = 0;
	if (sjme_error_is(error = inFont->apiInThread->pixelCharWidth(
		inFont, NULL, inCodepoint, &cw)))
		return sjme_error_default(error);
		
	/* And the original pixel height, since this is a bitmap font. */
	ch = 0;
	if (sjme_error_is(error = inFont->apiInThread->metricPixelSize(
		inFont, NULL, inCodepoint, &ch)))
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
	
	/* Get original glyph bitmap, perform unscaled drawing of it. */
	origOffX = 0;
	origOffY = 0;
	if (sjme_error_is(error = inFont->impl->renderBitmap(inFont, NULL,
		inCodepoint, src, 0, scanLen,
		ch, &origOffX, &origOffY)))
		goto fail_renderBitmap;
	
	/* Target desired pixel size. */
	th = inParams->pixelSize;
	
	/* Calculate the font fraction. */
	fraction = sjme_fixed_fraction(th, ch);
	ifraction = sjme_fixed_fraction(ch, th);
	
	/* Copy rows, for every change in dy we grab from the source. */
	for (dy = 0, sy = 0; dy < th && dy < bufHeight; dy++, sy += ifraction)
	{
		/* Normalize sy. */
		syInt = sjme_fixed_int(sjme_fixed_floor(sy));
		
		/* Determine where to move and copy from. */
		dp = &buf[bufOff + (dy * bufScanLen)];
		sp = &src[syInt * scanLen];
		
#if defined(SJME_CONFIG_SCRITCHUI_FONT_BARCODE)
		/* Use a new set of suppressor bits? */
		/* The suppressor bits are used so that only when the barcode bits */
		/* change, they are actually drawn. Suppressed bits get drawn in */
		/* at a later step. */
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
			/* There are very ugly looking thick horizontal bars due, these */
			/* can be removed with suppression however they end up leaving */
			/* gaps. Thus, compact the bits so they just become lines. */
			cmp = (sjme_jubyte)(sjme_util_intCompactRight(sup[dx],
					UINT32_MAX) |
				sjme_util_intCompactLeft(sup[dx], UINT32_MAX));
			
			/* Compacting from both sides leaves nubs on the ends of glyphs */
			/* however, which looks bad, however the nubs are always within */
			/* a single bit from the current row. So create a wiggle of the */
			/* current row to use as the mask. */
			lim = sp[dx];
			lim |= (lim << 1) | (lim >> 1);
			
			/* Apply the wiggle mask. */
			cmp &= lim;
			
			/* The inner lines are normally missing for this, but this */
			/* produces a very clean thin line set otherwise. To get the */
			/* inner lines, the compacted value is used. */
			dp[dx] = (orig & sup[dx]) | cmp;
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
			sjme_fixed_hi(origOffY), fraction));
	
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

static sjme_errorCode sjme_scritchui_fontRenderBitmap(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_jubyte* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufScanLen,
	sjme_attrInPositive sjme_jint bufHeight,
	sjme_attrOutNullable sjme_jint* outOffX,
	sjme_attrOutNullable sjme_jint* outOffY)
{
	sjme_errorCode error;
	sjme_jint ch;
	
	if (inFont == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inCodepoint < 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (bufOff < 0 || bufScanLen <= 0 || bufHeight <= 0 ||
		(bufHeight * bufScanLen) < 0 ||
		(bufOff + (bufHeight * bufScanLen)) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Invalid state? */
	if (inFont->impl == NULL || inFont->apiInThread == NULL ||
		inFont->apiInThread->metricPixelSize == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
	
	/* Not implemented? */
	if (inFont->impl->renderBitmap == NULL)
		return sjme_error_notImplemented(0);
	
	/* Validate glyph. */
	if (sjme_error_is(error = sjme_scritchui_validateChar(inFont,
		&inCodepoint)))
		return sjme_error_default(error);
	
	/* Get the height of this specific codepoint. */
	ch = 0;
	if (sjme_error_is(error = inFont->apiInThread->metricPixelSize(
		inFont, inParams, inCodepoint, &ch)))
		return sjme_error_default(error);
	
	/* If scaling is needed, use the barcode font scaling algorithm I wrote. */
	if (ch != inFont->id.param.pixelSize)
		return sjme_scritchui_renderBitmapScaled(inFont, inParams,
			inCodepoint, buf, bufOff, bufScanLen, bufHeight, outOffX, outOffY);
	
	/* Otherwise just use the underlying font rendering system, which may */
	/* apply its own scaling or not. */
	return inFont->impl->renderBitmap(inFont, inParams,
		inCodepoint, buf, bufOff, bufScanLen, bufHeight, outOffX, outOffY);
}

static sjme_errorCode sjme_scritchui_fontRenderChar(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
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
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
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

	/* These should always be valid. */
	if (inFont->apiInThread == NULL ||
		inFont->apiInThread->pixelCharWidth == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
		
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
		if (sjme_error_is(error = inFont->apiInThread->pixelCharWidth(inFont,
			inParams, c, &cw)))
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

/** In-thread serialized wrappers for font. */
static const sjme_scritchui_pencilFontFunctions
	sjme_scritchui_coreSerial_fontFunctions =
{
	sjme_sm(.equals, sjme_scritchui_coreSerial_fontEquals),
	sjme_sm(.metricCharDirection,
		sjme_scritchui_coreSerial_fontMetricCharDirection),
	sjme_sm(.metricCharValid, sjme_scritchui_coreSerial_fontMetricCharValid),
	sjme_sm(.metricFontFace, sjme_scritchui_coreSerial_fontMetricFontFace),
	sjme_sm(.metricFontName, sjme_scritchui_coreSerial_fontMetricFontName),
	sjme_sm(.metricFontStyle, sjme_scritchui_coreSerial_fontMetricFontStyle),
	sjme_sm(.metricPixelAscent,
		sjme_scritchui_coreSerial_fontMetricPixelAscent),
	sjme_sm(.metricPixelBaseline,
		sjme_scritchui_coreSerial_fontMetricPixelBaseline),
	sjme_sm(.metricPixelDescent,
		sjme_scritchui_coreSerial_fontMetricPixelDescent),
	sjme_sm(.metricPixelHeight,
		sjme_scritchui_coreSerial_fontMetricPixelHeight),
	sjme_sm(.metricPixelLeading,
		sjme_scritchui_coreSerial_fontMetricPixelLeading),
	sjme_sm(.metricPixelSize, sjme_scritchui_coreSerial_fontMetricPixelSize),
	sjme_sm(.pixelCharWidth, sjme_scritchui_coreSerial_fontPixelCharWidth),
	sjme_sm(.renderBitmap, sjme_scritchui_coreSerial_fontRenderBitmap),
	sjme_sm(.renderChar, sjme_scritchui_coreSerial_fontRenderChar),
	sjme_sm(.stringWidth, sjme_scritchui_coreSerial_fontStringWidth),
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

sjme_errorCode sjme_scritchui_core_intern_fontIterate(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_fontIterateStep* inOutStep)
{
	sjme_errorCode error;
	sjme_jint ignored, step, i, n;
	sjme_scritchui wrappedState;
	sjme_scritchui_pencilFont current;
	sjme_list_sjme_scritchui_pencilFont** whichRegister;
	sjme_scritchui_fontState* fontState;

	if (inState == NULL || inOutStep == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inOutStep->limitDepth < 0 || inOutStep->registerMask == 0 ||
		inOutStep->iterator == NULL)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* If wrapped, always use the underlying layer fonts. */
	wrappedState = inState->wrappedState;
	if (wrappedState != NULL)
		return wrappedState->intern->fontIterate(wrappedState, inOutStep);

	/* If no fonts have been scanned, scan every one. */
	ignored = 0;
	if (inState->font.fontRegister == NULL || inState->font.scanTotal <= 0)
		if (sjme_error_is(error = inState->intern->fontScanAll(inState,
			&ignored)))
			return sjme_error_default(error);

	/* Always clear current. */
	inOutStep->current = NULL;

	/* Check already registered real and pseudo fonts. */
	fontState = &inState->font;
	for (step = 0; step < 2; step++)
	{
		/* Is this mask being ignored? */
		if ((inOutStep->registerMask & (1 << step)) == 0)
			continue;

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
			current = (*whichRegister)->elements[i];
			if (current == NULL)
				continue;

			/* Font is too deep? */
			if (current->priority > inOutStep->limitDepth)
				continue;

			/* Iterate. */
			inOutStep->current = current;
			if (sjme_error_is(error = inOutStep->iterator(inState, inOutStep)))
			{
				/* Stop iteration without any errors? */
				if (error == SJME_ERROR_STOP)
					return SJME_ERROR_NONE;
				else if (error == SJME_ERROR_CONTINUE)
					continue;
				return sjme_error_default(error);
			}
		}
	}

	/* Iteration complete. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_intern_fontParamFromFlat(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFontParam* outParams,
	sjme_attrInNotNullBuf(inFlatLen) const sjme_jint* inFlat,
	sjme_attrInPositive sjme_jint inFlatOff,
	sjme_attrInPositiveNonZero sjme_jint inFlatLen)
{
#define sjme_unflat(structish, enumish) \
	if (SJME_SCRITCHUI_PENCIL_FONT_PARAM_##enumish < inFlatLen) \
		outParams->structish = inFlat[inFlatOff + \
			SJME_SCRITCHUI_PENCIL_FONT_PARAM_##enumish]

	if (inState == NULL || outParams == NULL || inFlat == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (inFlatOff < 0 || inFlatLen < 0 || (inFlatOff + inFlatLen) < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Map any valid entries. */
	sjme_unflat(pixelSize, PIXEL_SIZE);
	sjme_unflat(style, STYLE);

	/* Success! */
	return SJME_ERROR_NONE;
#undef sjme_unflat
}

sjme_errorCode sjme_scritchui_core_intern_fontParamToFlat(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNullBuf(outFlatLen) sjme_jint* outFlat,
	sjme_attrInPositive sjme_jint outFlatOff,
	sjme_attrInPositiveNonZero sjme_jint outFlatLen)
{
#define sjme_flat(structish, enumish) \
	if (SJME_SCRITCHUI_PENCIL_FONT_PARAM_##enumish < outFlatLen) \
		outFlat[outFlatOff + SJME_SCRITCHUI_PENCIL_FONT_PARAM_##enumish] = \
			(sjme_jint)inParams->structish;

	if (inState == NULL || inParams == NULL || outFlat == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outFlatOff < 0 || outFlatLen < 0 || (outFlatOff + outFlatLen) < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Map any valid entries. */
	sjme_flat(pixelSize, PIXEL_SIZE);
	sjme_flat(style, STYLE);

	/* Success! */
	return SJME_ERROR_NONE;
#undef sjme_flat
}

sjme_errorCode sjme_scritchui_core_intern_fontRegister(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont)
{
	sjme_errorCode error;
	sjme_scritchui_fontState* fontState;
	sjme_list_sjme_scritchui_pencilFont** whichRegister;
	sjme_scritchui_pencilFont* freeSlot;
	sjme_scritchui_pencilFont check;
	sjme_jint i, n;
	sjme_jboolean isPseudo;
	
	if (inState == NULL || inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Would this be considered a pseudo font? */
	/* A pseudo font is one that has a priority that is higher than zero, */
	/* which basically means it is used as a source for fallback glyphs. */
	isPseudo = (inFont->priority > 0);
	
	/* We operate with the font state */
	fontState = &inState->font;
	
	/* Check the font register if this font already exists. */
	n = 0;
	freeSlot = NULL;
	whichRegister = (isPseudo ? &fontState->pseudoRegister :
		&fontState->fontRegister);
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

	/* Pseudo fonts need to be sorted by priority then size, so that when a */
	/* fallback glyph needs to be selected one is accordingly chosen when */
	/* it comes time to chose one. Size is important to sort by, due to the */
	/* barcode scaling algorithm that is in place. */
	if (isPseudo)
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}

	/* Success! */
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

	/* Invalid state? */
	if (inState->impl == NULL || inState->intern == NULL ||
		inState->intern->fontScanResource == NULL ||
		inState->intern->fontBuiltin == NULL ||
		inState->intern->fontRegister == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
	
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
#if defined(SJME_DISABLED_OUTSIDE_FONT_BRANCH)
	errorResource = inState->intern->fontScanResource(inState, &current);
	
	/* Add to the total. */
	if (!sjme_error_is(errorResource))
		total += sjme_max(0, current);
#endif
	
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
		errorFallback = inState->intern->fontRegister(inState, builtin);
		
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
#if defined(SJME_DISABLED_OUTSIDE_FONT_BRANCH)
	if (sjme_error_is(errorResource))
		return sjme_error_default(errorResource);
#endif
	if (sjme_error_is(errorFallback))
		return sjme_error_default(errorFallback);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_core_fontByFace(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont,
	sjme_attrOutNullable sjme_scritchui_pencilFontParam* outParams,
	sjme_attrInValue sjme_scritchui_pencilFontFace inFace,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams)
{
	sjme_errorCode error;
	sjme_scritchui_fontIterateStep step;
	sjme_scritchui_fontByFaceData data;

	if (inState == NULL || outFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* There may exactly only be one bit set. */
	if (sjme_util_intBitCountU(inFace) != 1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Setup data. */
	memset(&data, 0, sizeof(data));
	data.inFace = inFace;
	if (inParams != NULL)
		memmove(&data.inParams, inParams, sizeof(data.inParams));

	/* Use font size specified by the system. */
	if (data.inParams.pixelSize <= 0)
	{
		if (sjme_error_is(error = inState->apiInThread->lafMetric(inState,
			NULL, &data.inParams.pixelSize,
			SJME_SCRITCHUI_LAF_METRIC_FONT_SIZE_DEFAULT)))
			return sjme_error_default(error);
	}

	/* Setup iterator. */
	memset(&step, 0, sizeof(step));
	step.registerMask = ~0;
	step.limitDepth = INT32_MAX;
	step.iterator = sjme_scritchui_core_fontByFaceIterator;
	step.data = &data;

	/* Iterate. */
	if (sjme_error_is(error = inState->intern->fontIterate(inState,
		&step)))
		return sjme_error_default(error);

	/* If we found no candidate fonts, then fail. */
	if (data.derive.font == NULL)
		return SJME_ERROR_INVALID_FONT;

	/* Use this font, take from the input parameters rather than from */
	/* the font itself. */
	*outFont = data.derive.font;
	if (outParams != NULL)
		memmove(outParams, &data.inParams, sizeof(data.inParams));
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
	sjme_attrOutNotNull sjme_scritchui_pencilFont* newFont,
	sjme_attrOutNullable sjme_scritchui_pencilFontParam* newParams,
	sjme_attrInNotNull sjme_scritchui_pencilFont oldFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* deriveParams)
{
	sjme_errorCode error;
	sjme_scritchui_fontIterateStep step;
	sjme_scritchui_fontDeriveData data;

	if (inState == NULL || newFont == NULL || oldFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Pointless operation? Just return the base font. */
	if (deriveParams == NULL)
	{
		/* Copy base. */
		*newFont = oldFont;

		/* Use base font parameters. */
		if (newParams != NULL)
			memmove(newParams, &oldFont->id.param,
				sizeof(oldFont->id.param));

		/* Success! */
		return SJME_ERROR_NONE;
	}

	/* Setup data. */
	/* Always start from this font, with the worst possible score. */
	memset(&data, 0, sizeof(data));
	data.resultScore = INT32_MAX;
	data.result = oldFont;

	/* Are there target parameters to derive? */
	if (deriveParams != NULL)
		memmove(&data.deriveParams, deriveParams,
			sizeof(data.deriveParams));

	/* Use font size specified by the system. */
	if (data.deriveParams.pixelSize == 0)
	{
		if (sjme_error_is(error = inState->apiInThread->lafMetric(inState,
			NULL, &data.deriveParams.pixelSize,
			SJME_SCRITCHUI_LAF_METRIC_FONT_SIZE_DEFAULT)))
			return sjme_error_default(error);
	}

	/* Setup iterator. */
	memset(&step, 0, sizeof(step));
	step.registerMask = ~0;
	step.limitDepth = INT32_MAX;
	step.iterator = sjme_scritchui_core_fontDeriveIterator;
	step.data = &data;

	/* Iterate. */
	if (sjme_error_is(error = inState->intern->fontIterate(inState,
		&step)))
		return sjme_error_default(error);

	/* If we found no candidate fonts, fail. */
	if (data.result == NULL)
		return SJME_ERROR_INVALID_FONT;

	/* Use this font, always use the searched derived parameters. */
	*newFont = data.result;
	if (newParams != NULL)
		memmove(newParams, &data.deriveParams, sizeof(data.deriveParams));
	return SJME_ERROR_NONE;
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
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_scritchui_pencilFont inOutFont)
{
	if (inState == NULL || inOutFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inOutFont->impl == NULL)
		return sjme_error_fatal(SJME_ERROR_ILLEGAL_STATE);
		
	/* Set base fields. */
	inOutFont->api = &sjme_scritchui_coreSerial_fontFunctions;
	inOutFont->apiInThread = &sjme_scritchui_fontFunctions;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
