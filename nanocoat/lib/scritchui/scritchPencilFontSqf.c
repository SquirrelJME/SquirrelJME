/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/scritchuiExtern.h"
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiPencilFontSqf.h"
#include "lib/scritchui/scritchuiStatePencil.h"
#include "sjme/debug.h"

static sjme_errorCode sjme_scritchui_sqfLocate(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull const sjme_scritchui_sqf** outCodepage,
	sjme_attrOutNotNull sjme_jint* outIndex)
{
	const sjme_scritchui_sqfCodepage* sqf;
	sjme_jint pageId, at, n;
	
	if (inFont == NULL || outCodepage == NULL || outIndex == 0)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inCodepoint < 0)
		return SJME_ERROR_INVALID_CODEPOINT;
	
	/* Recover SQF. */
	sqf = inFont->handle;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Determine the page id of the codepoint. */
	pageId = inCodepoint & (~0xFF);
	
	/* Go through each codepage to find it. */
	for (at = 0, n = sqf->numCodepages; at < n; at++)
		if (pageId == sqf->codepages[at]->codepointStart)
		{
			*outCodepage = sqf->codepages[at];
			*outIndex = inCodepoint & 0xFF;
			return SJME_ERROR_NONE;
		}
	
	/* Not found. */
	*outCodepage = NULL;
	*outIndex = -1;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricCharValid(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_jboolean* outValid)
{
	sjme_errorCode error;
	const sjme_scritchui_sqfCodepage* sqf;
	const sjme_scritchui_sqf* codepage;
	sjme_jint index;
	
	if (inFont == NULL || outValid == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover SQF. */
	sqf = inFont->handle;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Try locating it. */
	codepage = NULL;
	index = -1;
	if (sjme_error_is(error = sjme_scritchui_sqfLocate(inFont,
		inCodepoint, &codepage, &index)))
		return sjme_error_default(error);
	
	/* Not valid if missing. */
	if (codepage == NULL || index < 0)
	{
		*outValid = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}
	
	/* Need to check the character's validity flag. */
	/* Currently consider RaFoCES compressed glyphs as not valid. */
	if ((codepage->charFlags[index] & SJME_SCRITCHUI_SQF_FLAG_VALID) != 0 &&
		(codepage->charFlags[index] & SJME_SCRITCHUI_SQF_FLAG_RAFOCES) == 0)
		*outValid = SJME_JNI_TRUE;
	else
		*outValid = SJME_JNI_FALSE;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelAscent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outAscent)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outAscent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->handle;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Give the value. */
	*outAscent = sqf->codepages[0]->ascent;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelBaseline(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_jint* outBaseline)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outBaseline == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->handle;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Give the value. */
	/* MIDP says this is the max ascent of the font, since there is only */
	/* a single ascent, it becomes this. */
	*outBaseline = sqf->codepages[0]->ascent;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelDescent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outDescent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->handle;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Give the value. */
	*outDescent = sqf->codepages[0]->descent;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelLeading(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outLeading == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->handle;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Give the value. */
	*outLeading = sqf->codepages[0]->bbx;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelSize(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInNegativeOnePositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (inCodepoint < -1)
		return SJME_ERROR_INVALID_ARGUMENT;
		
	/* Recover SQF. */
	sqf = inFont->handle;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Give the size. */
	*outSize = sqf->codepages[0]->pixelHeight;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelCharWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNullable const sjme_scritchui_pencilFontParam* inParams,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth)
{
	sjme_errorCode error;
	const sjme_scritchui_sqf* codepage;
	sjme_jint index;
	
	if (inFont == NULL || outWidth == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try locating it. */
	codepage = NULL;
	index = -1;
	if (sjme_error_is(error = sjme_scritchui_sqfLocate(inFont,
		inCodepoint, &codepage, &index)))
		return sjme_error_default(error);
	
	/* Consider it zero width if it is missing. */
	if (codepage == NULL || index < 0)
	{
		*outWidth = SJME_JNI_FALSE;
		return SJME_ERROR_NONE;
	}
	
	/* Get width from codepage information. */
	*outWidth = ((sjme_jint)codepage->charWidths[index]) & 0xFF;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfRenderBitmap(
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
	const sjme_scritchui_sqf* codepage;
	const sjme_jubyte* src;
	const sjme_jubyte* sp;
	sjme_jubyte* dp;
	sjme_jint index, scanLen, bh, sx, sy, dx, dy;
	
	if (inFont == NULL || buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Try locating it. */
	codepage = NULL;
	index = -1;
	if (sjme_error_is(error = sjme_scritchui_sqfLocate(inFont,
		inCodepoint, &codepage, &index)))
		return sjme_error_default(error);
	
	/* Do nothing if missing. */
	if (codepage == NULL || index < 0)
		return SJME_ERROR_NONE;
	
	/* Get source bitmap data. */
	scanLen = ((sjme_jint)codepage->charBmpScan[index]) & 0xFF;
	bh = codepage->pixelHeight;
	src = (const sjme_jubyte*)&codepage->charBmp[
		codepage->charBmpOffset[index]];
	
	/* Copy over. */
	for (sy = 0, dy = 0; sy < bh && dy < bufHeight; sy++, dy++)
	{
		/* Determine source and destination pointers. */
		dp = &buf[bufOff + (dy * bufScanLen)];
		sp = &src[sy * scanLen];
		
		/* Copy. */
		for (sx = 0, dx = 0; sx < scanLen && dx < bufScanLen; sx++, dx++)
			(*(dp++)) = (*(sp++));
	}
	
	/* These are needed for proper rendering. */
	if (outOffX != NULL)
		*outOffX = codepage->charXOffset[index];
	if (outOffY != NULL)
		*outOffY = codepage->charYOffset[index];
	
	/* Success! */
	return SJME_ERROR_NONE;
}

/** Functions for native SQF support. */
static const sjme_scritchui_pencilFontImplFunctions
	sjme_scritchui_sqfFunctions =
{
	sjme_sm(.driverName, "sqf"),
	sjme_sm(.equals, NULL),
	sjme_sm(.metricCharValid, sjme_scritchui_sqfMetricCharValid),
	sjme_sm(.metricPixelAscent, sjme_scritchui_sqfMetricPixelAscent),
	sjme_sm(.metricPixelBaseline, sjme_scritchui_sqfMetricPixelBaseline),
	sjme_sm(.metricPixelDescent, sjme_scritchui_sqfMetricPixelDescent),
	sjme_sm(.metricPixelLeading, sjme_scritchui_sqfMetricPixelLeading),
	sjme_sm(.metricPixelSize, sjme_scritchui_sqfMetricPixelSize),
	sjme_sm(.pixelCharWidth, sjme_scritchui_sqfMetricPixelCharWidth),
	sjme_sm(.renderBitmap, sjme_scritchui_sqfRenderBitmap),
};

sjme_errorCode sjme_scritchui_core_fontBuiltin(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont)
{
	sjme_scritchui_pencilFont only;
	sjme_errorCode error;
	
	if (inState == NULL || outFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Does the font need initialization? */
	only = inState->font.builtinFont;
	if (only == NULL)
	{
		/* Initialize static SQF. */
		if (sjme_error_is(error = inState->intern->objectNew(inState,
			SJME_SUI_CAST_COMMON_P(&only),
			sizeof(*only), SJME_SCRITCHUI_TYPE_FONT,
			sjme_scritchui_newPencilFontSqfStatic,
			(sjme_pointer)&sqf_font_sanserif_12)))
			return sjme_error_default(error);

		/* Valid now, so cache. */
		inState->font.builtinFont = sjme_weakUpR(sjme_scritchui_pencilFont,
			only);
	}
	
	/* Success, or already cached! */
	*outFont = only;
	return SJME_ERROR_NONE;

fail_commonInit:
fail_init:
	/* Cleanup. */
	if (only != NULL)
		sjme_alloc_free(only);
			
	/* Fail. */
	return sjme_error_default(error);
}

sjme_errorCode sjme_scritchui_newPencilFontSqfStatic(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiCommon inOutFontR,
	sjme_attrInNullable sjme_pointerR(const sjme_scritchui_sqfCodepage*)
		inSqfCodepageR)
{
	sjme_errorCode error;
	const sjme_scritchui_sqf* firstPage;
	sjme_scritchui_pencilFont inOutFont;
	const sjme_scritchui_sqfCodepage* inSqfCodepage;

	inOutFont = (sjme_scritchui_pencilFont)inOutFontR;
	inSqfCodepage = inSqfCodepageR;
	if (inOutFont == NULL || inSqfCodepage == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* No codepages? */
	if (inSqfCodepage->numCodepages <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover the first codepage. */
	firstPage = inSqfCodepage->codepages[0];
	if (firstPage == NULL)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Initialize font. */
	inOutFont->impl = &sjme_scritchui_sqfFunctions;
	inOutFont->handle = (sjme_pointer)inSqfCodepage;
	
	/* SQFs are always lowest level fonts with regards to depth. */
	inOutFont->priority = 0;
	
	/* Fill in ID details. */
	strncpy(inOutFont->id.name, firstPage->name, SJME_MAX_FONT_NAME - 1);
	if (firstPage->family == SJME_SCRITCHUI_SQF_FAMILY_MONOSPACE)
		inOutFont->id.face = SJME_SCRITCHUI_PENCIL_FONT_FACE_MONOSPACE;
	else if (firstPage->family == SJME_SCRITCHUI_SQF_FAMILY_SERIF)
		inOutFont->id.face = SJME_SCRITCHUI_PENCIL_FONT_FACE_SERIF;
	else
		inOutFont->id.face = SJME_SCRITCHUI_PENCIL_FONT_FACE_NORMAL;
	inOutFont->id.param.style = SJME_SCRITCHUI_PENCIL_FONT_STYLE_PLAIN;
	inOutFont->id.param.pixelSize = firstPage->pixelHeight;
	
	/* Perform default initialization. */
	if (sjme_error_is(error = sjme_scritchui_newPencilFontStatic(
		inState, inOutFont)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
