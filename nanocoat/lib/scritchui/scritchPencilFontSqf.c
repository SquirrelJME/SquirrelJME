/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiPencilFontSqf.h"
#include "sjme/debug.h"

/** Functions for native SQF support. */
static const sjme_scritchui_pencilFontFunctions sjme_scritch_sqfFontFunctions =
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

sjme_errorCode sjme_sqf_newPencilFont(
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont,
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull const sjme_sqf_codepage* inSqfCodepage)
{
	if (outFont == NULL || inPool == NULL || inSqfCodepage == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
