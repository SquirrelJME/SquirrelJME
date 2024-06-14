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
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontMetricCharValid(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_jboolean* outValid)
{
	if (inFont == NULL)
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
	
	if (inFont == NULL)
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
	if (inFont == NULL)
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
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontMetricPixelLeading(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading)
{
	if (inFont == NULL)
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
	
	if (inFont == NULL)
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
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

static sjme_errorCode sjme_scritchui_fontPixelCharWidth(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth)
{
	if (inFont == NULL)
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
	.metricPixelLeading = sjme_scritchui_fontMetricPixelLeading,
	.metricPixelSize = sjme_scritchui_fontMetricPixelSize,
	.pixelCharHeight = sjme_scritchui_fontPixelCharHeight,
	.pixelCharWidth = sjme_scritchui_fontPixelCharWidth,
	.renderBitmap = sjme_scritchui_fontRenderBitmap,
	.renderChar = sjme_scritchui_fontRenderChar,
};

sjme_errorCode sjme_scritchui_newPencilFontInit(
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
