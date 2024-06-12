/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Pencil fonts.
 * 
 * @since 2024/05/17
 */

#ifndef SQUIRRELJME_SCRITCHUIPENCILFONT_H
#define SQUIRRELJME_SCRITCHUIPENCILFONT_H

#include "lib/scritchui/scritchuiPencil.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

typedef sjme_errorCode (*sjme_scritchui_pencilFontEqualsFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricCharDirectionFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricCharValidFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricFontFaceFunc)();

/**
 * Returns the name of the font.
 * 
 * @return The font name.
 * @since 2024/06/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricFontNameFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInOutNotNull sjme_lpcstr* outName);

typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricFontStyleFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelAscentFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelBaselineFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelDescentFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelLeadingFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontPixelCharHeightFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontPixelCharWidthFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontRenderBitmapFunc)();

typedef sjme_errorCode (*sjme_scritchui_pencilFontRenderCharFunc)();

/** Quick definition for functions. */
#define SJME_SCRITCHUI_QUICK_PENCIL(what, lWhat) \
	SJME_TOKEN_PASTE3(sjme_scritchui_pencilFont, what, Func) lWhat

/**
 * Functions to access pencil fonts.
 * 
 * @since 2024/05/17
 */
typedef struct sjme_scritchui_pencilFontFunctions
{
	/** Checks font equality. */
	SJME_SCRITCHUI_QUICK_PENCIL(Equals, equals);
	
	/** Returns the direction of the character. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricCharDirection, metricCharDirection);
	
	/** Checks the validity of a glyph. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricCharValid, metricCharValid);
	
	/** Returns the face of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricFontFace, metricFontFace);
	
	/** Returns the name of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricFontName, metricFontName);
	
	/** Returns the style of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricFontStyle, metricFontStyle);
	
	/** Returns the ascent of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricPixelAscent, metricPixelAscent);
	
	/** Returns the baseline of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricPixelBaseline, metricPixelBaseline);
	
	/** Returns the descent of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricPixelDescent, metricPixelDescent);
	
	/** Returns the leading of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricPixelLeading, metricPixelLeading);
	
	/** Returns the height of the font character. */
	SJME_SCRITCHUI_QUICK_PENCIL(PixelCharHeight, pixelCharHeight);
	
	/** Returns the width of the font character. */
	SJME_SCRITCHUI_QUICK_PENCIL(PixelCharWidth, pixelCharWidth);
	
	/** Renders the font character to a bitmap. */
	SJME_SCRITCHUI_QUICK_PENCIL(RenderBitmap, renderBitmap);
	
	/** Renders the font character to the given pencil. */
	SJME_SCRITCHUI_QUICK_PENCIL(RenderChar, renderChar);
} sjme_scritchui_pencilFontFunctions;

#undef SJME_SCRITCHUI_QUICK_PENCIL

struct sjme_scritchui_pencilFontLink
{
	/** The loaded font for this link. */
	sjme_scritchui_pencilFont font;
	
	/** The previous link. */
	sjme_scritchui_pencilFontLink* prev;
	
	/** The next link. */
	sjme_scritchui_pencilFontLink* next;
};

/**
 * Initializes a static pencil font.
 * 
 * @param inOutFont The resultant font.
 * @return Any resultant error, if any.
 * @since 2024/06/12
 */
sjme_errorCode sjme_scritchui_newPencilFontInit(
	sjme_scritchui_pencilFont inOutFont);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONT_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCILFONT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUIPENCILFONT_H */
