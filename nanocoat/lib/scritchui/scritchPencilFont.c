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

/** Functions for basic font support. */
static const sjme_scritchui_pencilFontFunctions sjme_scritch_fontFunctions =
{
	.equals = NULL,
	.metricCharDirection = NULL,
	.metricCharValid = NULL,
	.metricFontFace = NULL,
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

sjme_errorCode sjme_scritchui_newPencilFontStatic(
	sjme_scritchui_pencilFont inOutFont)
{
	if (inOutFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Set base fields. */
	inOutFont->api = &sjme_scritch_fontFunctions;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
