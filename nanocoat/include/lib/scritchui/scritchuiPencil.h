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
 * ScritchUI Pencil state.
 * 
 * @since 2024/05/01
 */
typedef struct sjme_scritchui_pencilBase* sjme_scritchui_pencil;

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
 * @param __g The hardware graphics to draw with.
 * @param __sx The source X position, will be translated.
 * @param __sy The source Y position, will be translated.
 * @param __w The width to copy.
 * @param __h The height to copy.
 * @param __dx The destination X position, will be translated.
 * @param __dy The destination Y position, will be translated.
 * @param __anchor The anchor point of the destination.
 * @return An error if the call is not valid or the native graphics
 * does not support this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilCopyAreaFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __sx,
	sjme_attrInValue sjme_jint __sy,
	sjme_attrInPositive sjme_jint __w,
	sjme_attrInPositive sjme_jint __h,
	sjme_attrInValue sjme_jint __dx,
	sjme_attrInValue sjme_jint __dy,
	sjme_attrInValue sjme_jint __anchor);

/**
 * Draws the given characters.
 *
 * @param __g The hardware graphics to draw with.
 * @param __s The characters to draw.
 * @param __o The offset into the buffer.
 * @param __l The number of characters to draw.
 * @param __x The X position.
 * @param __y The Y position.
 * @param __anchor The anchor point.
 * @return An error if the graphics is not valid, does not support
 * the given operation, if the anchor point is not valid, or if the
 * offset and/or length are out of bounds.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawCharsFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInNotNull sjme_jchar* __s,
	sjme_attrInPositive sjme_jint __o,
	sjme_attrInPositive sjme_jint __l,
	sjme_attrInValue sjme_jint __x,
	sjme_attrInValue sjme_jint __y,
	sjme_attrInValue sjme_jint __anchor);

/**
 * Draws a line in hardware.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __x1 The starting X coordinate.
 * @param __y1 The starting Y coordinate.
 * @param __x2 The ending X coordinate.
 * @param __y2 The ending Y coordinate.
 * @return An error on null arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawLineFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __x1,
	sjme_attrInValue sjme_jint __y1,
	sjme_attrInValue sjme_jint __x2,
	sjme_attrInValue sjme_jint __y2);

/**
 * Draws the outline of the given rectangle using the current color and
 * stroke style. The rectangle will cover an area that is
 * @c [width + 1, height + 1].
 *
 * Nothing is drawn if the width and/or height are zero.
 *
 * @param __g The hardware graphics to draw with.
 * @param __x The X coordinate.
 * @param __y The Y coordinate.
 * @param __w The width.
 * @param __h The height.
 * @return An error if the graphics is not valid or does not support
 * the given operation.
 * @since 22024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __x,
	sjme_attrInValue sjme_jint __y,
	sjme_attrInPositive sjme_jint __w,
	sjme_attrInPositive sjme_jint __h);

/**
 * Draws the given substring.
 *
 * @param __g The hardware graphics to draw with.
 * @param __s The string to draw.
 * @param __o The offset into the string.
 * @param __l The offset into the length.
 * @param __x The X coordinate.
 * @param __y The Y coordinate.
 * @param __anchor The anchor point.
 * @return An error if the graphics is not valid, this operation is
 * not supported, or on null arguments, or if the offset and/or length are
 * negative or exceed the string bounds.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawSubstringFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInNotNull sjme_lpcstr __s,
	sjme_attrInPositive sjme_jint __o, 
	sjme_attrInPositive sjme_jint __l,
	sjme_attrInValue sjme_jint __x,
	sjme_attrInValue sjme_jint __y,
	sjme_attrInValue sjme_jint __anchor);

/**
 * Draws a region of 32-bit RGB data into the target.
 *
 * @param __g The hardware graphics to draw with.
 * @param __data The source buffer.
 * @param __off The offset into the buffer.
 * @param __scanLen The scanline length.
 * @param __alpha Drawing with the alpha channel?
 * @param __xSrc The source X position.
 * @param __ySrc The source Y position.
 * @param __wSrc The width of the source region.
 * @param __hSrc The height of the source region.
 * @param __trans Sprite translation and/or rotation,
 * see @c javax.microedition.lcdui.game.Sprite.
 * @param __xDest The destination X position, is translated.
 * @param __yDest The destination Y position, is translated.
 * @param __anch The anchor point.
 * @param __wDest The destination width.
 * @param __hDest The destination height.
 * @param __origImgWidth Original image width.
 * @param __origImgHeight Original image height.
 * @return An error on null arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilDrawXRGB32RegionFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInNotNull int* __data,
	sjme_attrInPositive sjme_jint __off,
	sjme_attrInPositive sjme_jint __scanLen,
	sjme_attrInValue sjme_jboolean __alpha,
	sjme_attrInValue sjme_jint __xSrc,
	sjme_attrInValue sjme_jint __ySrc,
	sjme_attrInPositive sjme_jint __wSrc,
	sjme_attrInPositive sjme_jint __hSrc,
	sjme_attrInValue sjme_jint __trans,
	sjme_attrInValue sjme_jint __xDest,
	sjme_attrInValue sjme_jint __yDest,
	sjme_attrInValue sjme_jint __anch,
	sjme_attrInPositive sjme_jint __wDest,
	sjme_attrInPositive sjme_jint __hDest,
	sjme_attrInPositive sjme_jint __origImgWidth,
	sjme_attrInPositive sjme_jint __origImgHeight);

/**
 * Performs rectangular fill in hardware.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __x The X coordinate.
 * @param __y The Y coordinate.
 * @param __w The width.
 * @param __h The height.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillRectFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __x,
	sjme_attrInValue sjme_jint __y,
	sjme_attrInPositive sjme_jint __w,
	sjme_attrInPositive sjme_jint __h);

/**
 * Draws a filled triangle using the current color, the lines which make
 * up the triangle are included in the filled area.
 *
 * @param __g The graphics to use for drawing.
 * @param __x1 First X coordinate.
 * @param __y1 First Y coordinate.
 * @param __x2 Second X coordinate.
 * @param __y2 Second Y coordinate.
 * @param __x3 Third X coordinate.
 * @param __y3 Third Y coordinate.
 * @return An error if no graphics were specified or the graphics does
 * not actually support the given operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilFillTriangleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __x1,
	sjme_attrInValue sjme_jint __y1,
	sjme_attrInValue sjme_jint __x2,
	sjme_attrInValue sjme_jint __y2,
	sjme_attrInValue sjme_jint __x3,
	sjme_attrInValue sjme_jint __y3);

/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param __pf The @c sjme_gfx_pixelFormat used for the draw.
 * @param __bw The buffer width, this is the scanline width of the buffer.
 * @param __bh The buffer height.
 * @param __buf The target buffer to draw to, this is cast to the correct
 * buffer format.
 * @param __offset The offset to the start of the buffer.
 * @param __pal The color palette, may be @c NULL. 
 * @param __sx Starting surface X coordinate.
 * @param __sy Starting surface Y coordinate.
 * @param __sw Surface width.
 * @param __sh Surface height.
 * @return The bracket capable of drawing hardware accelerated graphics.
 * @return An error if the requested graphics are not valid.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilGraphicsFunc)(
	sjme_attrInOutNotNull sjme_scritchui_pencil* outPencil,
	sjme_attrInValue sjme_gfx_pixelFormat __pf,
	sjme_attrInPositive sjme_jint __bw,
	sjme_attrInPositive sjme_jint __bh,
	sjme_attrInNotNull void* __buf,
	sjme_attrInPositive sjme_jint __offset,
	sjme_attrInNullable sjme_jint __pal,
	sjme_attrInValue sjme_jint __sx,
	sjme_attrInValue sjme_jint __sy,
	sjme_attrInPositive sjme_jint __sw,
	sjme_attrInPositive sjme_jint __sh);

/**
 * Sets the alpha color for graphics.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __argb The color to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetAlphaColorFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __argb);

/**
 * Sets the blending mode to use.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __mode The blending mode to use.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetBlendingModeFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __mode);

/**
 * Sets the clipping rectangle position.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __x The X coordinate.
 * @param __y The Y coordinate.
 * @param __w The width.
 * @param __h The height.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetClipFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __x,
	sjme_attrInValue sjme_jint __y,
	sjme_attrInPositive sjme_jint __w,
	sjme_attrInPositive sjme_jint __h);

/**
 * Sets that the graphics should now use the default font.
 * 
 * @param __g The graphics used.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetDefaultFontFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g);

/**
 * Sets to use the specified font.
 *
 * @param __g The graphics used.
 * @param __name The font name.
 * @param __style The style of the font.
 * @param __pixelSize The pixel size of the font.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetFontFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInNotNull sjme_lpcstr __name,
	sjme_attrInValue sjme_jint __style,
	sjme_attrInPositiveNonZero sjme_jint __pixelSize);

/**
 * Sets the stroke style for the hardware graphics.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __style The stroke type to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilSetStrokeStyleFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __style);

/**
 * Translates drawing operations.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __x The X translation.
 * @param __y The Y translation.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*sjme_scritchui_pencilTranslateFunc)(
	sjme_attrInNotNull sjme_scritchui_pencil __g,
	sjme_attrInValue sjme_jint __x,
	sjme_attrInValue sjme_jint __y);

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
	
	/** @c Graphics . */
	SJME_SCRITCHUI_QUICK_PENCIL(Graphics);
	
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
