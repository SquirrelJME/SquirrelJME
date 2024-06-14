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

static sjme_errorCode sjme_scritchui_sqfFontMetricFontFace(
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

static sjme_errorCode sjme_scritchui_sqfFontMetricFontName(
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

static sjme_errorCode sjme_scritchui_sqfFontMetricPixelSize(
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

/** Functions for native SQF support. */
static const sjme_scritchui_pencilFontFunctions
	sjme_scritchui_sqfFontFunctions =
{
	.equals = NULL,
	.metricCharDirection = NULL,
	.metricCharValid = NULL,
	.metricFontFace = sjme_scritchui_sqfFontMetricFontFace,
	.metricFontName = sjme_scritchui_sqfFontMetricFontName,
	.metricFontStyle = NULL,
	.metricPixelAscent = NULL,
	.metricPixelBaseline = NULL,
	.metricPixelDescent = NULL,
	.metricPixelLeading = NULL,
	.metricPixelSize = sjme_scritchui_sqfFontMetricPixelSize,
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
	init.impl = &sjme_scritchui_sqfFontFunctions;
	init.context = (sjme_pointer)inSqfCodepage;
	
	/* Perform default initialization. */
	if (sjme_error_is(error = sjme_scritchui_newPencilFontInit(
		&init)))
		return sjme_error_default(error);
	
	/* Success! */
	memmove(inOutFont, &init, sizeof(init));
	return SJME_ERROR_NONE;
}
