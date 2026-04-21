// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.constants.PencilBlendingMode;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This class describes extended rendering methods that do not exist within
 * the standard {@link Graphics} set, but are available on some vendor
 * implementations.
 *
 * @since 2025/12/20
 */
@SquirrelJMEVendorApi
public interface ExtraGraphics
{
	/**
	 * Draws a region of data in a given pixel format into the target.
	 *
	 * @param __pf The image format that the data is in.
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
	 * @throws NullPointerException If {@code __data} is null.
	 * @throws IllegalArgumentException If the region is not valid, or the
	 * pixel format is not valid.
	 * @since 2025/12/07
	 */
	@SquirrelJMEVendorApi
	void drawPfRegion(
		@MagicConstant(valuesFromClass = UIPixelFormat.class) int __pf,
		@NotNull Object __data,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __scanLen,
		boolean __alpha, int __xSrc, int __ySrc,
		@Range(from = 0, to = Integer.MAX_VALUE) int __wSrc,
		@Range(from = 0, to = Integer.MAX_VALUE) int __hSrc, int __trans,
		int __xDest, int __yDest, int __anchor,
		@Range(from = 0, to = Integer.MAX_VALUE) int __wDest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __hDest,
		@Range(from = 0, to = Integer.MAX_VALUE) int __origImgWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __origImgHeight);
	
	/**
	 * Draws a polyline using the received coordinates.
	 *
	 * @param __xp An array of x coordinates for the vertices
	 * @param __xo The offset from which X positions should be read
	 * @param __yp An array of x coordinates for the vertices
	 * @param __yo The offset from which Y positions should be read
	 * @param __n How many points/vertices should be drawn
	 * @throws NullPointerException If {@code __xp} or {@code __yp} are null.
	 * @throws IllegalArgumentException If any of {@code __xo, __yo, __n} are
	 * less than zero, or the sum of either {@code __xo, __yo} with {@code
	 * __n}
	 * go out of bounds for {@code __xp} and {@code __yp} respectively.
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	void drawPolyline(@NotNull int[] __xp,
		@Range(from = 0, to = Integer.MAX_VALUE) int __xo,
		@NotNull int[] __yp,
		@Range(from = 0, to = Integer.MAX_VALUE) int __yo,
		@Range(from = 0, to = Integer.MAX_VALUE) int __n);
	
	/**
	 * Draws a triangle using the received coordinates.
	 *
	 * @param __x1 X coordinate of the first vertex.
	 * @param __y1 Y coordinate of the first vertex.
	 * @param __x2 X coordinate of the second vertex.
	 * @param __y2 Y coordinate of the second vertex.
	 * @param __x3 X coordinate of the third vertex.
	 * @param __y3 Y coordinate of the third vertex.
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	void drawTriangle(int __x1, int __y1, int __x2, int __y2, int __x3,
		int __y3);
	
	/**
	 * Draws a filled Polygon, the lines which make up the polygon are 
	 * included
	 * in the filled area.
	 *
	 * @param __xp An array of x coordinates for the vertices
	 * @param __xo The offset from which X positions should be read
	 * @param __yp An array of x coordinates for the vertices
	 * @param __yo The offset from which Y positions should be read
	 * @param __n How many points/vertices should be drawn
	 * @throws NullPointerException If {@code __xp} or {@code __yp}
	 * are null.
	 * @throws IllegalArgumentException If any of {@code __xo, __yo, __n} are
	 * less than zero, or the sum of either {@code __xo, __yo} with {@code
	 * __n}
	 * go out of bounds for {@code __xp} and {@code __yp} respectively.
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	void fillPolygon(@NotNull int[] __xp,
		@Range(from = 0, to = Integer.MAX_VALUE) int __xo,
		@NotNull int[] __yp,
		@Range(from = 0, to = Integer.MAX_VALUE) int __yo,
		@Range(from = 0, to = Integer.MAX_VALUE) int __n);
	
	/**
	 * Draws a filled a triangle using the received coordinates.
	 *
	 * @param __x1 X coordinate of the first vertex.
	 * @param __y1 Y coordinate of the first vertex.
	 * @param __x2 X coordinate of the second vertex.
	 * @param __y2 Y coordinate of the second vertex.
	 * @param __x3 X coordinate of the third vertex.
	 * @param __y3 Y coordinate of the third vertex.
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	void fillTriangle(int __x1, int __y1, int __x2, int __y2, int __x3,
		int __y3);
	
	/**
	 * Returns the color along with the alpha color.
	 *
	 * @return The color in the form of {@code @0xAARRGGBB}.
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	int getAlphaColor();
	
	/**
	 * Returns the platform's native pixel format
	 *
	 * @return The integer returned contains the native pixel format used by
	 * the platform, which can be one of the type constants defined in
	 * {@link UIPixelFormat}, like {@link UIPixelFormat#INT_ARGB8888}.
	 * @since 2025/12/07
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = UIPixelFormat.class)
	int getPixelFormat();
	
	/**
	 * Reads a region of pixel data from a hardware graphics context.
	 *
	 * Note that if the hardware graphics does not support reading of
	 * pixel data then the destination buffer may be left unmodified,
	 * filled with a specific value, or filled with off-screen buffer
	 * pixels that may not reflect what is visible on the screen.
	 *
	 * @param __pf One of {@link UIPixelFormat}, the pixel data placed into
	 * {@code __data} will be in this format.
	 * @param __data The destination buffer.
	 * @param __off The offset into the buffer.
	 * @param __scanLen The scanline length.
	 * @param __alpha If this argument is {@code true}, it means we must blend
	 * the content retrieved from the graphics context with the destination
	 * buffer's as opposed to overwriting its contents entirely.
	 * @param __xSrc The source X position.
	 * @param __ySrc The source Y position.
	 * @param __wSrc The width of the source region.
	 * @param __hSrc The height of the source region.
	 * @param __anchor The anchor point.
	 * @throws NullPointerException If {@code __data} is null.
	 * @throws IllegalArgumentException If the region is not valid, or the
	 * pixel format is not valid.
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	void getPfRegion(
		@MagicConstant(valuesFromClass = UIPixelFormat.class) int __pf,
		@NotNull Object __data,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __scanLen,
		boolean __alpha, int __xSrc, int __ySrc,
		@Range(from = 0, to = Integer.MAX_VALUE) int __wSrc,
		@Range(from = 0, to = Integer.MAX_VALUE) int __hSrc, int __anchor);
	
	/**
	 * Sets the alpha color to draw with along with the color to use.
	 *
	 * @param __argb The color in the form of {@code 0xAARRGGBB}.
	 * @param __bypass If {@code true}, then the alpha channel value is
	 * forced to be set to the input alpha value regardless of any
	 * compatibility checks such as {@code MeepRuntime.versionBefore(3, 0)}.
	 * This is useful for specific vendors such as Nokia that support
	 * transparent colors with vendor-specific packages on platforms which
	 * normally should not support transparent colors on their respective
	 * MIDP version.
	 * @since 2025/12/20
	 */
	@SquirrelJMEVendorApi
	void setAlphaColor(int __argb, boolean __bypass);
	
	/**
	 * Sets an extended blending mode beyond {@code SRC} and {@code SRC_OVER}.
	 *
	 * @param __mode The blending mode to set.
	 * @throws IllegalArgumentException If the blending mode is not valid.
	 * @since 2025/12/22
	 */
	@SquirrelJMEVendorApi
	void setBlendingModeEx(
		@MagicConstant(valuesFromClass = PencilBlendingMode.class) int __mode)
		throws IllegalArgumentException;
	
	/**
	 * Sets the font and the extra font parameters.
	 *
	 * @param __base The base font.
	 * @param __font The font to set.
	 * @param __fontParams The font parameters.
	 * @since 2026/04/21
	 */
	@SquirrelJMEVendorApi
	void setFont(Font __base, PencilFontBracket __font, int[] __fontParams);
	
	/**
	 * Returns the surface width.
	 *
	 * @return The surface width.
	 * @since 2025/12/21
	 */
	@SquirrelJMEVendorApi
	int surfaceWidth();
	
	/**
	 * Returns the surface height.
	 *
	 * @return The surface height.
	 * @since 2025/12/21
	 */
	@SquirrelJMEVendorApi
	int surfaceHeight();
}
