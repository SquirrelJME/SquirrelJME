/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "lib/scritchui/scritchuiExtern.h"
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

sjme_errorCode sjme_scritchui_core_fontBuiltin(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_pencilFont* outFont)
{
	sjme_scritchui_pencilFont only;
	sjme_errorCode error;
	
	if (inState == NULL || outFont == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Does the font need initialization? */
	only = NULL;
	if (inState->builtinFont == NULL)
	{
		if (sjme_error_is(error = sjme_alloc(inState->pool,
			sizeof(*only), &only)))
			return sjme_error_default(error);
		
		/* Initialize font. */
		if (sjme_error_is(error = sjme_scritchui_newPencilFontSqfStatic(
			only,
			&sqf_font_sanserif_12)))
		{
			/* Cleanup. */
			sjme_alloc_free(only);
		
			return sjme_error_default(error);
		}
		
		/* Valid now, so cache. */
		inState->builtinFont = only;
	}
	
	/* Success, or already cached! */
	*outFont = inState->builtinFont;
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
	init.impl = &sjme_scritch_sqfFontFunctions;
	init.context = (sjme_pointer)inSqfCodepage;
	
	if (sjme_error_is(error = sjme_scritchui_newPencilFontStatic(
		&init)))
		return sjme_error_default(error);
	
	/* Success! */
	memmove(inOutFont, &init, sizeof(init));
	return SJME_ERROR_NONE;
}
