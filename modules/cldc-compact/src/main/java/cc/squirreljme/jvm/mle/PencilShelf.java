// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.constants.PencilCapabilities;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * This shelf is responsible for accelerated graphics drawing.
 *
 * @see PencilBracket
 * @since 2020/09/25
 */
@SuppressWarnings("UnstableApiUsage")
@SquirrelJMEVendorApi
public final class PencilShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/09/25
	 */
	@SquirrelJMEVendorApi
	private PencilShelf()
	{
	}
	
	/**
	 * Returns the capabilities of the native possibly hardware accelerated
	 * pencil graphics drawing for the given pixel format.
	 * 
	 * @param __pf The {@link UIPixelFormat} being used for drawing.
	 * @throws MLECallError If the pixel format is not valid.
	 * @return The capabilities, will be the bit-field of
	 * {@link PencilCapabilities}. If there is not capability for this format
	 * then {@code 0} will be returned.
	 * @since 2020/09/25
	 */
	@SquirrelJMEVendorApi
	@CheckReturnValue
	@MagicConstant(flagsFromClass = PencilCapabilities.class)
	public static native int capabilities(
		@MagicConstant(valuesFromClass = UIPixelFormat.class) int __pf)
		throws MLECallError;
	
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
	 * @throws MLECallError If the call is not valid or the native graphics
	 * does not support this operation.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareCopyArea(@NotNull PencilBracket __g,
		int __sx, int __sy,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		int __dx, int __dy, int __anchor)
		throws MLECallError;
	
	/**
	 * This draws the outer edge of the ellipse from the given angles using
	 * the color, alpha, and stroke style.
	 *
	 * The coordinates are treated as if they were in a rectangular region. As
	 * such the center of the ellipse to draw the outline of is in the center
	 * of the specified rectangle.
	 *
	 * Note that no lines are drawn to the center point, so the shape does not
	 * result in a pie slice.
	 *
	 * The angles are in degrees and visually the angles match those of the
	 * unit circle correctly transformed to the output surface. As such, zero
	 * degrees has the point of {@code (__w, __h / 2)}, that is it points to
	 * the right. An angle at 45 degrees will always point to the top right
	 * corner.
	 *
	 * If the width or height are zero, then nothing is drawn. The arc will
	 * cover an area of {@code __w + 1} and {@code __h + 1}.
	 *
	 * @param __g The hardware graphics to draw with.
	 * @param __x The X position of the upper left corner, will be translated.
	 * @param __y The Y position of the upper left corner, will be translated.
	 * @param __w The width of the arc.
	 * @param __h The height of the arc.
	 * @param __startAngle The starting angle in degrees, 
	 * @param __arcAngle The offset from the starting angle, negative values
	 * indicate clockwise direction while positive values are counterclockwise.
	 * @throws MLECallError If the pencil is not valid; or the arc requested
	 * to draw is not valid.
	 * @since 2024/07/14
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawArc(@NotNull PencilBracket __g,
		int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		int __startAngle,
		int __arcAngle)
		throws MLECallError;
	
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
	 * @throws MLECallError If the graphics is not valid, does not support
	 * the given operation, if the anchor point is not valid, or if the
	 * offset and/or length are out of bounds.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawChars(@NotNull PencilBracket __g,
		@NotNull char[] __s,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l,
		int __x, int __y, int __anchor)
		throws MLECallError;
	
	/**
	 * Draws a horizontal line in hardware.
	 *
	 * @param __g The graphics to draw with.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width of the line.
	 * @throws MLECallError On null arguments; if the pencil is not valid; or
	 * the width is negative.
	 * @since 2024/05/17
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawHoriz(@NotNull PencilBracket __g,
		int __x, int __y, @Range(from = 1, to = Integer.MAX_VALUE) int __w)
		throws MLECallError;
	
	/**
	 * Draws a line in hardware.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __x1 The starting X coordinate.
	 * @param __y1 The starting Y coordinate.
	 * @param __x2 The ending X coordinate.
	 * @param __y2 The ending Y coordinate.
	 * @throws MLECallError On null arguments.
	 * @since 2021/12/05
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawLine(@NotNull PencilBracket __g,
		int __x1, int __y1, int __x2, int __y2)
		throws MLECallError;
	
	/**
	 * Draws a single pixel.
	 *
	 * @param __g The graphics to draw on.
	 * @param __x The X coordinate.
	 * @param __y The y coordinate.
	 * @throws MLECallError On null arguments or if the pencil is not valid.
	 * @since 2024/05/17
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawPixel(@NotNull PencilBracket __g,
		int __x, int __y)
		throws MLECallError;
	
	/**
	 * Draws the outline of a polygon. 
	 *
	 * @param __g The graphics to draw with.
	 * @param __x The X coordinate.
	 * @param __xOff The offset into the X array.
	 * @param __y The Y coordinate.
	 * @param __yOff The offset into the Y array.
	 * @param __n The number of sides to draw.
	 * @throws MLECallError If the graphics is not valid; or if the sides
	 * are not valid.
	 * @since 2024/07/14
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawPolyline(@NotNull PencilBracket __g,
		@NotNull int[] __x,
		@Range(from = 0, to = Integer.MAX_VALUE) int __xOff,
		@NotNull int[] __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __yOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __n)
		throws MLECallError;
	
	/**
	 * Draws the outline of the given rectangle using the current color and
	 * stroke style. The rectangle will cover an area that is
	 * {@code [width + 1, height + 1]}.
	 *
	 * Nothing is drawn if the width and/or height are zero.
	 *
	 * @param __g The hardware graphics to draw with.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError If the graphics is not valid or does not support
	 * the given operation.
	 * @since 2023/02/16
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawRect(@NotNull PencilBracket __g,
		int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h)
		throws MLECallError;
	
	/**
	 * Draws a round rectangle.
	 * 
	 * If the width and/or height are zero or negative, nothing is drawn.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width of the rectangle.
	 * @param __h The height of the rectangle.
	 * @param __arcWidth The horizontal diameter of the arc on the corners.
	 * @param __arcHeight The vertical diameter of the arc on the corners.
	 * @throws MLECallError If the pencil is invalid; or if the requested
	 * round rectangle is not valid.
	 * @since 2024/07/14
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawRoundRect(@NotNull PencilBracket __g,
		int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		@Range(from = 0, to = Integer.MAX_VALUE) int __arcWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __arcHeight)
		throws MLECallError;
	
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
	 * @throws MLECallError If the graphics is not valid, this operation is
	 * not supported, or on null arguments, or if the offset and/or length are
	 * negative or exceed the string bounds.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawSubstring(@NotNull PencilBracket __g,
		@NotNull String __s,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o, 
		@Range(from = 0, to = Integer.MAX_VALUE) int __l,
		int __x, int __y, int __anchor)
		throws MLECallError;
	
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
	 * {@code javax.microedition.lcdui.game.Sprite}.
	 * @param __xDest The destination X position, is translated.
	 * @param __yDest The destination Y position, is translated.
	 * @param __anchor The anchor point.
	 * @param __wDest The destination width.
	 * @param __hDest The destination height.
	 * @param __origImgWidth Original image width.
	 * @param __origImgHeight Original image height.
	 * @throws MLECallError On null arguments.
	 * @since 2022/01/26
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareDrawXRGB32Region(
		@NotNull PencilBracket __hardware, @NotNull int[] __data,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __scanLen,
		boolean __alpha, int __xSrc, int __ySrc,
		@Range(from = 0, to = Integer.MAX_VALUE) int __wSrc,
		@Range(from = 0, to = Integer.MAX_VALUE) int __hSrc,
		int __trans, int __xDest, int __yDest, int __anchor,
		@Range(from = 0, to = Integer.MAX_VALUE) int __wDest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __hDest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __origImgWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __origImgHeight)
		throws MLECallError;
	
	/**
	 * This draws the filled slice of an ellipse (like a pie slice) from the
	 * given angles using the color, alpha, and stroke style.
	 *
	 * Unlike {@link #hardwareDrawArc(PencilBracket, int, int, int, int, int,
	 * int)}, the width and height are not increased by a single pixel.
	 *
	 * Otherwise, this follows the same set of rules as
	 * {@link #hardwareDrawArc(PencilBracket, int, int, int, int, int, int)}.
	 *
	 * @param __g The hardware graphics to draw with.
	 * @param __x The X position of the upper left corner, will be translated.
	 * @param __y The Y position of the upper left corner, will be translated.
	 * @param __w The width of the arc.
	 * @param __h The height of the arc.
	 * @param __startAngle The starting angle in degrees, 
	 * @param __arcAngle The offset from the starting angle, negative values
	 * indicate clockwise direction while positive values are counterclockwise.
	 * @see #hardwareDrawArc(PencilBracket, int, int, int, int, int, int)
	 * @throws MLECallError If the pencil is not valid; or if the requested
	 * arc is not valid.
	 * @since 2024/07/14
	 */
	@Api
	public static native void hardwareFillArc(@NotNull PencilBracket __g,
		int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		int __startAngle, int __arcAngle)
		throws MLECallError;
	
	/**
	 * Draws a filled polygon in the same manner
	 * as {@link #hardwareDrawPolyline(PencilBracket, int[], int, int[],
	 * int, int)}.
	 *
	 * @param __g The graphics to draw with.
	 * @param __x The X coordinate.
	 * @param __xOff The offset into the X array.
	 * @param __y The Y coordinate.
	 * @param __yOff The offset into the Y array.
	 * @param __n The number of sides to draw.
	 * @throws MLECallError If the graphics is not valid; if the sides
	 * are not valid; or if the values are out of bounds of the array.
	 * @since 2024/07/14
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareFillPolygon(@NotNull PencilBracket __g,
		@NotNull int[] __x,
		@Range(from = 0, to = Integer.MAX_VALUE) int __xOff,
		@NotNull int[] __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __yOff,
		@Range(from = 0, to = Integer.MAX_VALUE) int __n)
		throws MLECallError;
	
	/**
	 * Performs rectangular fill in hardware.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareFillRect(@NotNull PencilBracket __g,
		int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h)
		throws MLECallError;
	
	/**
	 * Draws a filled round rectangle in the same manner
	 * as {@link #hardwareDrawRoundRect(PencilBracket, int, int, int, int,
	 * int, int)} 
	 *
	 * @param __g The graphics to use.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @param __arcWidth The width of the arc at each corner.
	 * @param __arcHeight The height of the arc at each corner.
	 * @throws MLECallError If the graphics is not valid; or the requested
	 * round rectangle is not valid.
	 * @since 2024/07/14
	 */
	@Api
	public static native void hardwareFillRoundRect(@NotNull PencilBracket __g,
		int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		int __arcWidth,
		int __arcHeight)
		throws MLECallError;
	
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
	 * @throws MLECallError If no graphics were specified or the graphics does
	 * not actually support the given operation.
	 * @since 2023/02/16
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareFillTriangle(@NotNull PencilBracket __g,
		int __x1, int __y1, int __x2, int __y2, int __x3, int __y3)
		throws MLECallError;
	
	/**
	 * Is there an alpha channel for this pencil?
	 *
	 * @param __g The graphics to check.
	 * @return If there is an alpha channel or not.
	 * @throws MLECallError On null arguments or if the pencil is not valid.
	 * @since 2024/05/12
	 */
	@SquirrelJMEVendorApi
	public static native boolean hardwareHasAlpha(@NotNull PencilBracket __g)
		throws MLECallError;
	
	/**
	 * Sets the alpha color for graphics.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __argb The color to set.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareSetAlphaColor(@NotNull PencilBracket __g,
		int __argb)
		throws MLECallError;
	
	/**
	 * Sets the blending mode to use.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __mode The blending mode to use.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareSetBlendingMode(
		@NotNull PencilBracket __g, int __mode)
		throws MLECallError;
	
	/**
	 * Sets the clipping rectangle position.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareSetClip(@NotNull PencilBracket __g,
		int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h)
		throws MLECallError;
	
	/**
	 * Sets that the graphics should now use the default font.
	 * 
	 * @param __g The graphics used.
	 * @throws MLECallError If the graphics is not valid or does not support
	 * this operation.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareSetDefaultFont(
		@NotNull PencilBracket __g)
		throws MLECallError;
	
	/**
	 * Sets to use the specified font.
	 *
	 * @param __g The graphics used.
	 * @param __font The font to set.
	 * @throws MLECallError If the graphics is not valid or does not support
	 * this operation.
	 * @since 2023/02/19
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareSetFont(@NotNull PencilBracket __g,
		@NotNull PencilFontBracket __font)
		throws MLECallError;
	
	/**
	 * Sets the stroke style for the hardware graphics.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __style The stroke type to set.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareSetStrokeStyle(
		@NotNull PencilBracket __g,
		int __style)
		throws MLECallError;
	
	/**
	 * Translates drawing operations.
	 * 
	 * @param __g The hardware graphics to draw with.
	 * @param __x The X translation.
	 * @param __y The Y translation.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2021/12/05
	 */
	@SquirrelJMEVendorApi
	public static native void hardwareTranslate(@NotNull PencilBracket __g,
		int __x, int __y)
		throws MLECallError;
	
	/**
	 * Performs native image loading
	 * 
	 * @param __type The {@link NativeImageLoadType} to load.
	 * @param __b The buffer.
	 * @param __o The offset.
	 * @param __l The length.
	 * @param __callback The callback that performs the image loading.
	 * @return The object returned will be passed through the callback from
	 * the native callback, should return {@code null} if the load has been
	 * cancelled.
	 * @throws MLECallError If the image could not be loaded.
	 * @see NativeImageLoadCallback
	 * @since 2021/12/05
	 */
	@SquirrelJMEVendorApi
	@Nullable
	public static native Object nativeImageLoadRGBA(
		@MagicConstant(valuesFromClass = NativeImageLoadType.class) int __type,
		@NotNull byte[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l,
		@NotNull NativeImageLoadCallback __callback)
		throws MLECallError;
	
	/**
	 * Returns a bit field of {@link NativeImageLoadType} which indicates which
	 * types of images are capable of being natively loaded on a platform
	 * which requiring byte code to be executed for it.
	 * 
	 * @return The bit field of {@link NativeImageLoadType} that can be
	 * natively loaded.
	 * @since 2021/12/05
	 */
	@SquirrelJMEVendorApi
	@CheckReturnValue
	@MagicConstant(valuesFromClass = NativeImageLoadType.class)
	public static native int nativeImageLoadTypes();
}
