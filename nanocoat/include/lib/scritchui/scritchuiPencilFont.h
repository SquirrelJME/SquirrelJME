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

/**
 * Font face for pencil fonts.
 * 
 * @since 2024/06/13
 */
typedef enum sjme_scritchui_pencilFontFace
{
	/** Monospaced. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_MONOSPACE = 1,
	
	/** Serifs. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_SERIF = 2,
	
	/** Symbol. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_SYMBOL = 4,
	
	/** Normal, nothing different from anything. */
	SJME_SCRITCHUI_PENCIL_FONT_FACE_NORMAL = 8,
} sjme_scritchui_pencilFontFace;

/**
 * Checks if two brackets refer to the same font.
 *
 * @param a The first font.
 * @param b The second font.
 * @return If the two fonts are the same.
 * @since 2024/05/17
 */
typedef sjme_jboolean (*sjme_scritchui_pencilFontEqualsFunc)(
	sjme_attrInNullable sjme_scritchui_pencilFont a,
	sjme_attrInNullable sjme_scritchui_pencilFont b);

/**
 * Returns the direction of the given character in the font.
 *
 * @param inFont The font to check.
 * @param inCodepoint The character.
 * @param outDirection The direction of the character, will be @c -1  or @c 1 .
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricCharDirectionFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrInRange(-1, 1) sjme_jint* outDirection);

/**
 * Checks whether the character in the given font is valid, as in it has
 * a render-able glyph.
 *
 * @param inFont The font to check within.
 * @param inCodepoint The character to check.
 * @param outValid If the character in the font has a glyph and is valid.
 * @return Any resultant error, if any.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricCharValidFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_jboolean* outValid);

/**
 * Returns the @c sjme_scritchui_pencilFontFace of a font. 
 *
 * @param inFont The font to request from.
 * @param outFace The font face, any flag
 * from @c sjme_scritchui_pencilFontFace .
 * @return Any resultant error, if any.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricFontFaceFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontFace* outFace);

/**
 * Returns the name of the font.
 * 
 * @param inFont The font to get the name of.
 * @param outName The font name.
 * @return Any resultant error, if any.
 * @since 2024/06/12
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricFontNameFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_lpcstr* outName);

/**
 * Returns the style of the font.
 *
 * @param inFont The style of the font to request.
 * @param outStyle The font style, will be flags
 * from @c sjme_scritchui_pencilFontStyle .
 * @return Any resultant error, if any.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricFontStyleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_scritchui_pencilFontStyle* outStyle);

/**
 * Returns the ascent of the font.
 *
 * @param inFont The font to check.
 * @param isMax Should the max be obtained.
 * @param outAscent The ascent of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelAscentFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outAscent);

/**
 * Returns the baseline of the font.
 *
 * @param inFont The font to check.
 * @param outBaseline The baseline of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelBaselineFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_jint* outBaseline);

/**
 * Returns the height of the font.
 *
 * @param inFont The font to check.
 * @param outHeight The height of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelHeightFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_jint* outHeight);

/**
 * Returns the descent of the font.
 *
 * @param inFont The font to check.
 * @param isMax Should the max be obtained.
 * @param outDescent The descent of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelDescentFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInValue sjme_jboolean isMax,
	sjme_attrOutNotNull sjme_jint* outDescent);
	
/**
 * Returns the leading of the font.
 *
 * @param inFont The font to obtain from.
 * @param outLeading The leading amount in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelLeadingFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outLeading);
	
/**
 * Returns the pixel size of the font.
 *
 * @param inFont The font to get the size of.
 * @param outSize The pixel size of the font.
 * @return Any resultant error, if any.
 * @since 2024/05/17
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontMetricPixelSizeFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outSize);

/**
 * Returns the width of the given character.
 *
 * @param inFont The font to obtain from.
 * @param inCodepoint The character.
 * @param outWidth The width of the font in pixels.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontPixelCharWidthFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrOutNotNull sjme_attrOutPositiveNonZero sjme_jint* outWidth);

/**
 * Renders the font glyph to a bitmap represented in a byte array. Each
 * byte within the array represents 8 pixels.
 *
 * @param inFont The font to render to the bitmap.
 * @param inCodepoint The character to render.
 * @param buf The resultant buffer.
 * @param bufOff The offset into the buffer.
 * @param bufScanLen The scanline length of the buffer.
 * @param surfaceX The surface X.
 * @param surfaceY The surface Y.
 * @param surfaceW The surface width.
 * @param surfaceH The surface height.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontRenderBitmapFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_jbyte* buf,
	sjme_attrInPositive sjme_jint bufOff,
	sjme_attrInPositive sjme_jint bufScanLen,
	sjme_attrInPositive sjme_jint surfaceX,
	sjme_attrInPositive sjme_jint surfaceY,
	sjme_attrInPositive sjme_jint surfaceW,
	sjme_attrInPositive sjme_jint surfaceH);

/**
 * Renders the given character to the resultant pencil.
 *
 * @param inFont The font to render from.
 * @param inCodepoint The character to render.
 * @param inPencil The pencil to draw into.
 * @param xPos The target X position.
 * @param yPos The target Y position.
 * @param nextXPos Optional output which contains the next X
 * coordinate accordingly for continual drawing.
 * @param nextYPos Optional output which contains the next Y
 * coordinate accordingly for continual drawing.
 * @return Any resultant error, if any.
 * @since 2024/05/14
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontRenderCharFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInPositive sjme_jint inCodepoint,
	sjme_attrInNotNull sjme_scritchui_pencil inPencil,
	sjme_attrInValue sjme_jint xPos,
	sjme_attrInNotNull sjme_jint yPos,
	sjme_attrOutNullable sjme_jint* nextXPos,
	sjme_attrOutNullable sjme_jint* nextYPos);

/**
 * Calculates the width of the given string.
 * 
 * @param inFont The font to calculate for.
 * @param s The input character sequence.
 * @param o The offset into the sequence.
 * @param l The length of the sequence.
 * @param outWidth The resultant pixel width.
 * @return Any resultant error, if any.
 * @since 2024/06/27
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFontStringWidthFunc)(
	sjme_attrInNotNull sjme_scritchui_pencilFont inFont,
	sjme_attrInNotNull const sjme_charSeq* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrOutNotNull sjme_jint* outWidth);

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
	
	/** Returns the height of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricPixelHeight, metricPixelHeight);
	
	/** Returns the leading of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricPixelLeading, metricPixelLeading);
	
	/** Returns the pixel size of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricPixelSize, metricPixelSize);
	
	/** Returns the width of the font character. */
	SJME_SCRITCHUI_QUICK_PENCIL(PixelCharWidth, pixelCharWidth);
	
	/** Renders the font character to a bitmap. */
	SJME_SCRITCHUI_QUICK_PENCIL(RenderBitmap, renderBitmap);
	
	/** Renders the font character to the given pencil. */
	SJME_SCRITCHUI_QUICK_PENCIL(RenderChar, renderChar);
	
	/** Calculates the length of the given string. */
	SJME_SCRITCHUI_QUICK_PENCIL(StringWidth, stringWidth);
} sjme_scritchui_pencilFontFunctions;

/**
 * Functions to native implementation access pencil fonts.
 * 
 * @since 2024/06/27
 */
typedef struct sjme_scritchui_pencilFontImplFunctions
{
	/** Checks font equality. */
	SJME_SCRITCHUI_QUICK_PENCIL(Equals, equals);
	
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
	
	/** Returns the pixel size of the font. */
	SJME_SCRITCHUI_QUICK_PENCIL(MetricPixelSize, metricPixelSize);
	
	/** Returns the width of the font character. */
	SJME_SCRITCHUI_QUICK_PENCIL(PixelCharWidth, pixelCharWidth);
	
	/** Renders the font character to a bitmap. */
	SJME_SCRITCHUI_QUICK_PENCIL(RenderBitmap, renderBitmap);
} sjme_scritchui_pencilFontImplFunctions;

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
sjme_errorCode sjme_scritchui_newPencilFontStatic(
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
