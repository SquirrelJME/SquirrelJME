/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdio.h>

#include "lib/scritchui/scritchuiExtern.h"
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiPencilFontSqf.h"
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
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Recover SQF. */
	sqf = inFont->context;
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
		return SJME_ERROR_NONE;
		
	/* Recover SQF. */
	sqf = inFont->context;
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
	if ((codepage->charFlags[index] & SJME_SCRITCHUI_SQF_FLAG_VALID) != 0)
		*outValid = SJME_JNI_TRUE;
	else
		*outValid = SJME_JNI_FALSE;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricFontFace(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontFace* outFace)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outFace == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->context;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Give the face based on the family. */
	sjme_scritchui_pencilFontFace face;
	switch (sqf->codepages[0]->family)
	{
			/* Monospace font. */
		case SJME_SCRITCHUI_SQF_FAMILY_MONOSPACE:
			face = SJME_SCRITCHUI_PENCIL_FONT_FACE_MONOSPACE;
			break;
			
			/* Serif font. */
		case SJME_SCRITCHUI_SQF_FAMILY_SERIF:
			face = SJME_SCRITCHUI_PENCIL_FONT_FACE_SERIF;
			break;
		
			/* Regular otherwise. */
		case SJME_SCRITCHUI_SQF_FAMILY_REGULAR:
		case SJME_SCRITCHUI_SQF_FAMILY_SANS_SERIF:
		default:
			face = SJME_SCRITCHUI_PENCIL_FONT_FACE_NORMAL;
			break;
	}
	
	/* Success! */
	*outFace = face;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricFontName(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInOutNotNull sjme_lpcstr* outName)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->context;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Give the name of the SQF. */
	*outName = sqf->name;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelAscent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outAscent)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outAscent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->context;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Give the value. */
	*outAscent = sqf->codepages[0]->ascent;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelBaseline(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_jint* outBaseline)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outBaseline == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->context;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Give the value. */
	*outBaseline = sqf->codepages[0]->bby;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelDescent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outDescent == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->context;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Give the value. */
	*outDescent = sqf->codepages[0]->descent;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelLeading(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outLeading == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover SQF. */
	sqf = inFont->context;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
		
	/* Give the value. */
	*outLeading = sqf->codepages[0]->bbx;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelSize(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize)
{
	const sjme_scritchui_sqfCodepage* sqf;
	
	if (inFont == NULL || outSize == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Recover SQF. */
	sqf = inFont->context;
	if (sqf == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Give the size. */
	*outSize = sqf->codepages[0]->pixelHeight;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_scritchui_sqfMetricPixelCharWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth)
{
	sjme_errorCode error;
	const sjme_scritchui_sqfCodepage* sqf;
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

/** Functions for native SQF support. */
static const sjme_scritchui_pencilFontImplFunctions
	sjme_scritchui_sqfFunctions =
{
	.equals = NULL,
	.metricCharValid = sjme_scritchui_sqfMetricCharValid,
	.metricFontFace = sjme_scritchui_sqfMetricFontFace,
	.metricFontName = sjme_scritchui_sqfMetricFontName,
	.metricFontStyle = NULL,
	.metricPixelAscent = sjme_scritchui_sqfMetricPixelAscent,
	.metricPixelBaseline = sjme_scritchui_sqfMetricPixelBaseline,
	.metricPixelDescent = sjme_scritchui_sqfMetricPixelDescent,
	.metricPixelLeading = sjme_scritchui_sqfMetricPixelLeading,
	.metricPixelSize = sjme_scritchui_sqfMetricPixelSize,
	.pixelCharWidth = sjme_scritchui_sqfMetricPixelCharWidth,
	.renderBitmap = NULL,
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
	only = inState->builtinFont;
	if (only == NULL)
	{
		if (sjme_error_is(error = sjme_alloc(inState->pool,
			sizeof(*only), &only)) || only == NULL)
			return sjme_error_default(error);
		
		/* Initialize font. */
		if (sjme_error_is(error = sjme_scritchui_newPencilFontSqfStatic(
			only, &sqf_font_sanserif_12)))
		{
			/* Cleanup. */
			sjme_alloc_free(only);
			
			/* Fail. */
			return sjme_error_default(error);
		}
		
		/* Valid now, so cache. */
		inState->builtinFont = only;
	}
	
	/* Success, or already cached! */
	*outFont = only;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_scritchui_newPencilFontSqfStatic(
	sjme_scritchui_pencilFont inOutFont,
	const sjme_scritchui_sqfCodepage* inSqfCodepage)
{
	struct sjme_scritchui_pencilFontBase init;
	sjme_errorCode error;

	if (inOutFont == NULL || inSqfCodepage == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Initialize font. */
	memset(&init, 0, sizeof(init));
	init.impl = &sjme_scritchui_sqfFunctions;
	init.context = (sjme_pointer)inSqfCodepage;
	
	/* Perform default initialization. */
	if (sjme_error_is(error = sjme_scritchui_newPencilFontStatic(
		&init)))
		return sjme_error_default(error);
	
	/* Success! */
	memmove(inOutFont, &init, sizeof(init));
	return SJME_ERROR_NONE;
}
