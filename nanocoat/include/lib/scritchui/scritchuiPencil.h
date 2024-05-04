/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ScritchUI Pencil Drawing.
 * 
 * @since 2024/05/01
 */

#ifndef SQUIRRELJME_SCRITCHUIPENCIL_H
#define SQUIRRELJME_SCRITCHUIPENCIL_H

#include "lib/scritchui/scritchui.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This copies one region of the image to another region.
 *
 * Copying to a display device is not permitted because it may impact how
 * double buffering is implemented, as such it is not supported.
 *
 * Pixels are copied directly and no alpha compositing is performed.
 *
 * If the source and destination overlap then it must be as if they did not
 * overlap at all, this means that the destination will be an exact copy of
 * the source.
 *
 * @param g The hardware graphics to draw with.
 * @param sx The source X position, will be translated.
 * @param sy The source Y position, will be translated.
 * @param w The width to copy.
 * @param h The height to copy.
 * @param dx The destination X position, will be translated.
 * @param dy The destination Y position, will be translated.
 * @param anchor The anchor point of the destination.
 * @return An error if the call is not valid or the native graphics
 * does not support this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilCopyAreaFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h,
	sjme_attrInValue sjme_jint dx,
	sjme_attrInValue sjme_jint dy,
	sjme_attrInValue sjme_jint anchor);

/**
 * Draws the given characters.
 *
 * @param g The hardware graphics to draw with.
 * @param s The characters to draw.
 * @param o The offset into the buffer.
 * @param l The number of characters to draw.
 * @param x The X position.
 * @param y The Y position.
 * @param anchor The anchor point.
 * @return An error if the graphics is not valid, does not support
 * the given operation, if the anchor point is not valid, or if the
 * offset and/or length are out of bounds.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawCharsFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_jchar* s,
	sjme_attrInPositive sjme_jint o,
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

/**
 * Draws a line in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x1 The starting X coordinate.
 * @param y1 The starting Y coordinate.
 * @param x2 The ending X coordinate.
 * @param y2 The ending Y coordinate.
 * @return An error on null arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawLineFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2);

/**
 * Draws the outline of the given rectangle using the current color and
 * stroke style. The rectangle will cover an area that
 * is @code \[width + 1, height + 1\] @endcode .
 *
 * Nothing is drawn if the width and/or height are zero.
 *
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @return An error if the graphics is not valid or does not support
 * the given operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

/**
 * Draws the given substring.
 *
 * @param g The hardware graphics to draw with.
 * @param s The string to draw.
 * @param o The offset into the string.
 * @param l The offset into the length.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param anchor The anchor point.
 * @return An error if the graphics is not valid, this operation is
 * not supported, or on null arguments, or if the offset and/or length are
 * negative or exceed the string bounds.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawSubstringFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_lpcstr s,
	sjme_attrInPositive sjme_jint o, 
	sjme_attrInPositive sjme_jint l,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInValue sjme_jint anchor);

/**
 * Draws a region of 32-bit RGB data into the target.
 *
 * @param g The hardware graphics to draw with.
 * @param data The source buffer.
 * @param off The offset into the buffer.
 * @param scanLen The scanline length.
 * @param alpha Drawing with the alpha channel?
 * @param xSrc The source X position.
 * @param ySrc The source Y position.
 * @param wSrc The width of the source region.
 * @param hSrc The height of the source region.
 * @param trans Sprite translation and/or rotation,
 * see @c javax.microedition.lcdui.game.Sprite.
 * @param xDest The destination X position, is translated.
 * @param yDest The destination Y position, is translated.
 * @param anch The anchor point.
 * @param wDest The destination width.
 * @param hDest The destination height.
 * @param origImgWidth Original image width.
 * @param origImgHeight Original image height.
 * @return An error on null arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawXRGB32RegionFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull int* data,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositive sjme_jint scanLen,
	sjme_attrInValue sjme_jboolean alpha,
	sjme_attrInValue sjme_jint xSrc,
	sjme_attrInValue sjme_jint ySrc,
	sjme_attrInPositive sjme_jint wSrc,
	sjme_attrInPositive sjme_jint hSrc,
	sjme_attrInValue sjme_jint trans,
	sjme_attrInValue sjme_jint xDest,
	sjme_attrInValue sjme_jint yDest,
	sjme_attrInValue sjme_jint anch,
	sjme_attrInPositive sjme_jint wDest,
	sjme_attrInPositive sjme_jint hDest,
	sjme_attrInPositive sjme_jint origImgWidth,
	sjme_attrInPositive sjme_jint origImgHeight);

/**
 * Performs rectangular fill in hardware.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

/**
 * Draws a filled triangle using the current color, the lines which make
 * up the triangle are included in the filled area.
 *
 * @param g The graphics to use for drawing.
 * @param x1 First X coordinate.
 * @param y1 First Y coordinate.
 * @param x2 Second X coordinate.
 * @param y2 Second Y coordinate.
 * @param x3 Third X coordinate.
 * @param y3 Third Y coordinate.
 * @return An error if no graphics were specified or the graphics does
 * not actually support the given operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillTriangleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x1,
	sjme_attrInValue sjme_jint y1,
	sjme_attrInValue sjme_jint x2,
	sjme_attrInValue sjme_jint y2,
	sjme_attrInValue sjme_jint x3,
	sjme_attrInValue sjme_jint y3);

/**
 * Sets the alpha color for graphics.
 * 
 * @param g The hardware graphics to draw with.
 * @param argb The color to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetAlphaColorFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint argb);

/**
 * Sets the blending mode to use.
 * 
 * @param g The hardware graphics to draw with.
 * @param mode The blending mode to use.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetBlendingModeFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint mode);

/**
 * Sets the clipping rectangle position.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X coordinate.
 * @param y The Y coordinate.
 * @param w The width.
 * @param h The height.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetClipFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y,
	sjme_attrInPositive sjme_jint w,
	sjme_attrInPositive sjme_jint h);

/**
 * Sets that the graphics should now use the default font.
 * 
 * @param g The graphics used.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetDefaultFontFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g);

/**
 * Sets to use the specified font.
 *
 * @param g The graphics used.
 * @param name The font name.
 * @param style The style of the font.
 * @param pixelSize The pixel size of the font.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetFontFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInNotNull sjme_lpcstr name,
	sjme_attrInValue sjme_jint style,
	sjme_attrInPositiveNonZero sjme_jint pixelSize);

/**
 * Sets the stroke style for the hardware graphics.
 * 
 * @param g The hardware graphics to draw with.
 * @param style The stroke type to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetStrokeStyleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint style);

/**
 * Translates drawing operations.
 * 
 * @param g The hardware graphics to draw with.
 * @param x The X translation.
 * @param y The Y translation.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilTranslateFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil g,
	sjme_attrInValue sjme_jint x,
	sjme_attrInValue sjme_jint y);

/** Quick definition for functions. */
#define SJME_SCRITCHUI_QUICK_PENCIL(what) \
	SJME_TOKEN_PASTE3(sjme_scritchui_pencil, what, Func) what

/**
 * ScritchUI Pencil implementation functions.
 * 
 * @since 2024/05/01
 */
typedef struct sjme_scritchui_pencilFunctions
{
	/** @c CopyArea . */
	SJME_SCRITCHUI_QUICK_PENCIL(CopyArea);
	
	/** @c DrawChars . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawChars);
	
	/** @c DrawLine . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawLine);
	
	/** @c DrawRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawRect);
	
	/** @c DrawSubstring . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawSubstring);
	
	/** @c DrawXRGB32Region . */
	SJME_SCRITCHUI_QUICK_PENCIL(DrawXRGB32Region);
	
	/** @c FillRect . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillRect);
	
	/** @c FillTriangle . */
	SJME_SCRITCHUI_QUICK_PENCIL(FillTriangle);
	
	/** @c SetAlphaColor . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetAlphaColor);
	
	/** @c SetBlendingMode . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetBlendingMode);
	
	/** @c SetClip . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetClip);
	
	/** @c SetDefaultFont . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetDefaultFont);
	
	/** @c SetFont . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetFont);
	
	/** @c SetStrokeStyle . */
	SJME_SCRITCHUI_QUICK_PENCIL(SetStrokeStyle);
	
	/** @c Translate . */
	SJME_SCRITCHUI_QUICK_PENCIL(Translate);
} sjme_scritchui_pencilFunctions;

#undef SJME_SCRITCHUI_QUICK_PENCIL

/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param pf The @c sjme_gfx_pixelFormat used for the draw.
 * @param bw The buffer width, this is the scanline width of the buffer.
 * @param bh The buffer height.
 * @param buf The target buffer to draw to, this is cast to the correct
 * buffer format.
 * @param offset The offset to the start of the buffer.
 * @param pal The color palette, may be @c NULL. 
 * @param sx Starting surface X coordinate.
 * @param sy Starting surface Y coordinate.
 * @param sw Surface width.
 * @param sh Surface height.
 * @return The bracket capable of drawing hardware accelerated graphics.
 * @return An error if the requested graphics are not valid.
 * @since 2024/05/01
 */
sjme_errorCode sjme_scritchui_pencilGraphicsFunc(
	sjme_attrInOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrInValue sjme_gfx_pixelFormat pf,
	sjme_attrInPositive sjme_jint bw,
	sjme_attrInPositive sjme_jint bh,
	sjme_attrInNotNull void* buf,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNullable sjme_jint pal,
	sjme_attrInValue sjme_jint sx,
	sjme_attrInValue sjme_jint sy,
	sjme_attrInPositive sjme_jint sw,
	sjme_attrInPositive sjme_jint sh);

typedef struct sjme_scritchui_pencilColor
{
	/** The RGBA color. */
	sjme_jint rgba;
	
	/** Red. */
	sjme_jubyte r;
	
	/** Green. */
	sjme_jubyte g;
	
	/** Blue. */
	sjme_jubyte b;
	
	/** Alpha. */
	sjme_jubyte a;
	
	/** Indexed color. */
	sjme_jchar i;
} sjme_scritchui_pencilColor;

/**
 * Pencil drawing state, such as colors or otherwise.
 * 
 * @since 2024/05/04
 */
typedef struct sjme_scritchui_pencilState
{
	/** The current color used. */
	sjme_scritchui_pencilColor color;
} sjme_scritchui_pencilState;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H
}
		#undef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCRITCHUIPENCIL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCRITCHUIPENCIL_H */
