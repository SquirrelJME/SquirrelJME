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
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"

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
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward. */
	return wrapped->api->metricPixelAscent(wrapped, isMax, outAscent);
}

static sjme_errorCode sjme_scritchui_pseudoMetricPixelBaseline(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_jint* outBaseline)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward. */
	return wrapped->api->metricPixelBaseline(wrapped, outBaseline);
}

static sjme_errorCode sjme_scritchui_pseudoMetricPixelDescent(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward. */
	return wrapped->api->metricPixelDescent(wrapped, isMax, outDescent);
}

static sjme_errorCode sjme_scritchui_pseudoMetricPixelLeading(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward. */
	return wrapped->api->metricPixelLeading(wrapped, outLeading);
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

static sjme_errorCode sjme_scritchui_pseudoPixelCharHeight(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outHeight)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* Forward. */
	return wrapped->api->pixelCharHeight(wrapped, inCodepoint, outHeight);
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
	sjme_attrInNotNull sjme_jbyte* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufScanLen,
	sjme_attrInPositive sjme_jint surfaceX,
	sjme_attrInPositive sjme_jint surfaceY,
	sjme_attrInPositive sjme_jint surfaceW,
	sjme_attrInPositive sjme_jint surfaceH)
{
	sjme_scritchui_pencilFont wrapped;
	
	if (inFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover wrapper. */
	wrapped = inFont->context;
	if (wrapped == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

/** Functions for basic font support. */
static const sjme_scritchui_pencilFontImplFunctions
	sjme_scritchui_pseudoFontFunctions =
{
	.equals = sjme_scritchui_pseudoEquals,
	.metricCharValid = sjme_scritchui_pseudoMetricCharValid,
	.metricFontFace = sjme_scritchui_pseudoMetricFontFace,
	.metricFontName = sjme_scritchui_pseudoMetricFontName,
	.metricFontStyle = sjme_scritchui_pseudoMetricFontStyle,
	.metricPixelAscent = sjme_scritchui_pseudoMetricPixelAscent,
	.metricPixelBaseline = sjme_scritchui_pseudoMetricPixelBaseline,
	.metricPixelDescent = sjme_scritchui_pseudoMetricPixelDescent,
	.metricPixelLeading = sjme_scritchui_pseudoMetricPixelLeading,
	.metricPixelSize = sjme_scritchui_pseudoMetricPixelSize,
	.pixelCharHeight = sjme_scritchui_pseudoPixelCharHeight,
	.pixelCharWidth = sjme_scritchui_pseudoPixelCharWidth,
	.renderBitmap = sjme_scritchui_pseudoRenderBitmap,
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
	
	/* Allocate. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc(inState->pool,
		sizeof(*result), &result)))
		goto fail_alloc;
	
	/* Setup new font. */
	result->impl = &sjme_scritchui_pseudoFontFunctions;
	result->context = inFont;
	result->cache.style = inStyle;
	result->cache.pixelSize = inPixelSize;
	
	/* Initialize base font. */
	if (sjme_error_is(error = sjme_scritchui_newPencilFontStatic(
		result)))
		goto fail_initBase;
	
	/* Give the font. */
	*outDerived = result;
	return SJME_ERROR_NONE;
	
fail_initBase:
fail_alloc:
	if (result != NULL)
		sjme_alloc_free(result);
	
	return sjme_error_default(error);
}
