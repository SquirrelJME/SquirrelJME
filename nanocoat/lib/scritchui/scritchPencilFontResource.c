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

/**
 * Information on known resource fonts.
 * 
 * @since 2026/01/18
 */
typedef struct sjme_scritchui_resourceFontInfo
{
	/** The base directory name for the font. */
	sjme_lpcstr baseDir;
	
	/** The base name of the font. */
	sjme_lpcstr name;
	
	/** The pixel size of the font. */
	sjme_jint size;
	
	/** The face of the font. */
	sjme_scritchui_pencilFontFace face;
	
	/** The style of the font. */
	sjme_scritchui_pencilFontStyle style;
	
	/**
	 * The priority level of the font, for building pseudo fonts. Any font that
	 * is of the same priority level cannot override a font of the same or
	 * higher priority level. The lower the value, the higher the priority.
	 */
	sjme_jint priority;
} sjme_scritchui_resourceFontInfo;

#define SJME_DECLARE_FONT(inName, inSize, inFace, inStyle, inPriority) \
	{ \
		sjme_sm(.baseDir, \
			SJME_TOKEN_STRING_PP(inName)"-"SJME_TOKEN_STRING_PP(inSize)), \
		sjme_sm(.name, "sjme-rc-"SJME_TOKEN_STRING(inName)), \
		sjme_sm(.size, inSize), \
		sjme_sm(.face, \
			SJME_TOKEN_PASTE(SJME_SCRITCHUI_PENCIL_FONT_FACE_, inFace)), \
		sjme_sm(.style, \
			SJME_TOKEN_PASTE(SJME_SCRITCHUI_PENCIL_FONT_STYLE_, inStyle)), \
		sjme_sm(.priority, inPriority), \
	}

static const sjme_scritchui_resourceFontInfo sjme_scritchui_resourceFonts[] =
{
	/* Core fonts are specifically handcrafted and designed for SquirrelJME */
	/* and may contain any characters as required for the various styles. */
	/* All core font priorities should be zero. */
	
	/* Sans-serif. */
	SJME_DECLARE_FONT(sansserif, 8, NORMAL, PLAIN, 0),
	SJME_DECLARE_FONT(sansserif, 12, NORMAL, PLAIN, 0),
	SJME_DECLARE_FONT(sansserif, 16, NORMAL, PLAIN, 0),
	
	/* Monospace. */
	SJME_DECLARE_FONT(monospace, 8, MONOSPACE, PLAIN, 0),
	SJME_DECLARE_FONT(monospace, 12, MONOSPACE, PLAIN, 0),
	SJME_DECLARE_FONT(monospace, 16, MONOSPACE, PLAIN, 0),
	
	/* Serif. */
	SJME_DECLARE_FONT(serif, 8, SERIF, PLAIN, 0),
	SJME_DECLARE_FONT(serif, 12, SERIF, PLAIN, 0),
	SJME_DECLARE_FONT(serif, 16, SERIF, PLAIN, 0),
	
	/* If a specifically crafted core font does not provide a glyph for a */
	/* language, then the fonts here */
	/* Fonts for the same language should always use the same priority. */
	
	/* Japanese Fonts. */
	SJME_DECLARE_FONT(misaki, 8, AUTOMATIC, PLAIN, 32),
	SJME_DECLARE_FONT(misaki, 12, AUTOMATIC, PLAIN, 32),
	
	/* GNU Unifont, a font which effectively has every single glyph. */
	SJME_DECLARE_FONT(unifont, 16, AUTOMATIC, PLAIN, 2048),
	
	/* End. */
	{
		sjme_sm(.baseDir, NULL),
		sjme_sm(.name, NULL),
		sjme_sm(.size, 0),
		sjme_sm(.face, 0),
		sjme_sm(.style, 0),
		sjme_sm(.priority, INT32_MAX),
	}
};

static sjme_errorCode sjme_scritchui_core_intern_rcForPage(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull const sjme_scritchui_resourceFontInfo* preDef)
{
	if (inState == NULL || preDef == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_scritchui_core_intern_rcScanSingle(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull const sjme_scritchui_resourceFontInfo* preDef)
{
	if (inState == NULL || preDef == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_scritchui_core_intern_fontScanResource(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_jint* outCount)
{
	sjme_errorCode error;
	sjme_scritchui wrappedState;
	const sjme_scritchui_resourceFontInfo* preDef;
	sjme_jint procCount;
	
	if (inState == NULL || outCount == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* If wrapped, always use the underlying layer fonts. */
	wrappedState = inState->wrappedState;
	if (wrappedState != NULL)
		return wrappedState->intern->fontScanResource(wrappedState,
			outCount);
	
	/* Externals are required for this. */
	if (inState->externals == NULL ||
		inState->externals->externalAsset == NULL)
	{
		*outCount = 0;
		return SJME_ERROR_NONE;
	}

	/* Go through all pre-defined fonts. */
	procCount = 0;
	for (preDef = &sjme_scritchui_resourceFonts[0]; preDef->name != NULL;
		preDef++)
	{
		/* Scan single font. */
		if (sjme_error_is(error = sjme_scritchui_core_intern_rcScanSingle(
			inState, preDef)))
		{
			/* If the font does not exist naturally, skip it. */
			if (error == SJME_ERROR_RESOURCE_NOT_FOUND)
				continue;

			return sjme_error_default(error);
		}

		/* Process count up. */
		procCount++;
	}

	/* Success! */
	if (outCount != NULL)
		*outCount = procCount;
	return SJME_ERROR_NONE;
}
