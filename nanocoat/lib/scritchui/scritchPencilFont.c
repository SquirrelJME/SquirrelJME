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

/** Functions for basic font support. */
static const sjme_scritchui_pencilFontFunctions sjme_scritchui_fontFunctions =
{
	.equals = NULL,
	.metricCharDirection = NULL,
	.metricCharValid = NULL,
	.metricFontFace = NULL,
	.metricFontName = sjme_scritchui_fontMetricFontName,
	.metricFontStyle = NULL,
	.metricPixelAscent = NULL,
	.metricPixelBaseline = NULL,
	.metricPixelDescent = NULL,
	.metricPixelLeading = NULL,
	.pixelCharHeight = NULL,
	.pixelCharWidth = NULL,
	.renderBitmap = NULL,
	.renderChar = NULL,
};

sjme_errorCode sjme_scritchui_newPencilFontInit(
	sjme_scritchui_pencilFont inOutFont)
{
	if (inOutFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Set base fields. */
	inOutFont->api = &sjme_scritchui_fontFunctions;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
