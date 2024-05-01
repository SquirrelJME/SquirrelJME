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
#include "sjme/error.h"

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
typedef sjme_errorCode (*hardwareCopyArea)(@NotNull PencilBracket __g,
	int __sx, int __sy,
	@Range(from = 0, to = Integer.MAX_VALUE) int __w,
	@Range(from = 0, to = Integer.MAX_VALUE) int __h,
	int __dx, int __dy, int __anchor);

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
typedef sjme_errorCode (*hardwareDrawChars)(@NotNull PencilBracket __g,
	@NotNull char[] __s,
	@Range(from = 0, to = Integer.MAX_VALUE) int __o,
	@Range(from = 0, to = Integer.MAX_VALUE) int __l,
	int __x, int __y, int __anchor);

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
typedef sjme_errorCode (*hardwareDrawLine)(@NotNull PencilBracket __g,
	int __x1, int __y1, int __x2, int __y2);

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
typedef sjme_errorCode (*hardwareDrawRect)(@NotNull PencilBracket __g,
	int __x, int __y,
	@Range(from = 0, to = Integer.MAX_VALUE) int __w,
	@Range(from = 0, to = Integer.MAX_VALUE) int __h);

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
typedef sjme_errorCode (*hardwareDrawSubstring)(@NotNull PencilBracket __g,
	@NotNull String __s,
	@Range(from = 0, to = Integer.MAX_VALUE) int __o, 
	@Range(from = 0, to = Integer.MAX_VALUE) int __l,
	int __x, int __y, int __anchor);

/**
 * Draws a region of 32-bit RGB data into the target.
 *
 * @param __hardware The hardware graphics to draw with.
 * @param __data The source buffer.
 * @param __off The offset into the buffer.
 * @param __scanLen The scanline length.
 * @param __alpha Drawing with the alpha channel?
 * @param __xSrc The source X position.
 * @param __ySrc The source Y position.
 * @param __wSrc The width of the source region.
 * @param __hSrc The height of the source region.
 * @param __trans Sprite translation and/or rotation, see
 * @c javax.microedition.lcdui.game.Sprite.
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
typedef sjme_errorCode (*hardwareDrawXRGB)32Region(
	@NotNull PencilBracket __hardware, @NotNull int[] __data,
	@Range(from = 0, to = Integer.MAX_VALUE) int __off,
	@Range(from = 0, to = Integer.MAX_VALUE) int __scanLen,
	boolean __alpha, int __xSrc, int __ySrc,
	@Range(from = 0, to = Integer.MAX_VALUE) int __wSrc,
	@Range(from = 0, to = Integer.MAX_VALUE) int __hSrc,
	int __trans, int __xDest, int __yDest, int __anch,
	@Range(from = 0, to = Integer.MAX_VALUE) int __wDest,
	@Range(from = 0, to = Integer.MAX_VALUE) int __hDest,
	@Range(from = 0, to = Integer.MAX_VALUE) int __origImgWidth,
	@Range(from = 0, to = Integer.MAX_VALUE) int __origImgHeight);

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
typedef sjme_errorCode (*hardwareFillRect)(@NotNull PencilBracket __g,
	int __x, int __y,
	@Range(from = 0, to = Integer.MAX_VALUE) int __w,
	@Range(from = 0, to = Integer.MAX_VALUE) int __h);

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
typedef sjme_errorCode (*hardwareFillTriangle)(@NotNull PencilBracket __g,
	int __x1, int __y1, int __x2, int __y2, int __x3, int __y3);

/**
 * Creates a hardware reference bracket to the native hardware graphics.
 * 
 * @param __pf The {@link UIPixelFormat} used for the draw.
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
public static native PencilBracket hardwareGraphics(
	@MagicConstant(valuesFromClass = UIPixelFormat.class) int __pf,
	@Range(from = 0, to = Integer.MAX_VALUE) int __bw,
	@Range(from = 0, to = Integer.MAX_VALUE) int __bh,
	@NotNull Object __buf,
	@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
	@Nullable int[] __pal,
	int __sx, int __sy,
	@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
	@Range(from = 0, to = Integer.MAX_VALUE) int __sh);

/**
 * Sets the alpha color for graphics.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __argb The color to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*hardwareSetAlphaColor)(@NotNull PencilBracket __g,
	int __argb);

/**
 * Sets the blending mode to use.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __mode The blending mode to use.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*hardwareSetBlendingMode)(
	@NotNull PencilBracket __g, int __mode);

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
typedef sjme_errorCode (*hardwareSetClip)(@NotNull PencilBracket __g,
	int __x, int __y,
	@Range(from = 0, to = Integer.MAX_VALUE) int __w,
	@Range(from = 0, to = Integer.MAX_VALUE) int __h);

/**
 * Sets that the graphics should now use the default font.
 * 
 * @param __g The graphics used.
 * @return An error if the graphics is not valid or does not support
 * this operation.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*hardwareSetDefaultFont)(
	@NotNull PencilBracket __g);

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
typedef sjme_errorCode (*hardwareSetFont)(@NotNull PencilBracket __g,
	@NotNull String __name, int __style,
	@Range(from = 1, to = Integer.MAX_VALUE) int __pixelSize);

/**
 * Sets the stroke style for the hardware graphics.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __style The stroke type to set.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*hardwareSetStrokeStyle)(
	@NotNull PencilBracket __g,
	int __style);

/**
 * Translates drawing operations.
 * 
 * @param __g The hardware graphics to draw with.
 * @param __x The X translation.
 * @param __y The Y translation.
 * @return An error on @c NULL arguments.
 * @since 2024/05/01
 */
typedef sjme_errorCode (*hardwareTranslate)(@NotNull PencilBracket __g,
	int __x, int __y);

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
