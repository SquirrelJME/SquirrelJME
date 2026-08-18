// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * This interface provides direct framebuffer pixel access.
 *
 * @since 2019/10/07
 */
@Api
public interface DirectGraphics
{
	/** Constant for flipping an image horizontally */
	@Api
	int FLIP_HORIZONTAL = 8192;

	/** Constant for flipping an image vertically */
	@Api
	int FLIP_VERTICAL = 16384;

	/** Constant for using a 180 degree counter-clockwise rotation */
	@Api
	int ROTATE_180 = 180;

	/** Constant for using a 270 degree counter-clockwise rotation */
	@Api
	int ROTATE_270 = 270;

	/** Constant for using a 90 degree counter-clockwise rotation */
	@Api
	int ROTATE_90 = 90;

	/** Pixel format containing 8 horizontal pixels per byte, 2 gray shades */
	@Api
	int TYPE_BYTE_1_GRAY = 1;

	/** Pixel format containing 8 vertical pixels per byte, 2 gray shades */
	@Api
	int TYPE_BYTE_1_GRAY_VERTICAL = -1;

	/** Pixel format containing 4 horizontal pixels per byte, 4 gray shades */
	@Api
	int TYPE_BYTE_2_GRAY = 2;

	/** 
	 * Pixel format containing 3 bits for red color, 3 bits for green color,
	 * and 2 bits for blue color.
	 */
	@Api
	int TYPE_BYTE_332_RGB = 332;

	/** Pixel format containing 2 horizontal pixels per byte, 16 gray shades */
	@Api
	int TYPE_BYTE_4_GRAY = 4;

	/** Pixel format containing 256 grey shades, one pixel per byte */
	@Api
	int TYPE_BYTE_8_GRAY = 8;

	/** Standard 8-bit-per-channel RGB format */
	@Api
	int TYPE_INT_888_RGB = 888;

	/** Standard 8-bit-per-channel ARGB format */
	@Api
	int TYPE_INT_8888_ARGB = 8888;

	/** 16-bit pixel format containing 5 bits for each color and binary alpha */
	@Api
	int TYPE_USHORT_1555_ARGB = 1555;

	/** 16-bit pixel format containing 4 bits for each color and no alpha */
	@Api
	int TYPE_USHORT_444_RGB = 444;

	/** 16-bit pixel format containing 4 bits for each color and alpha */
	@Api
	int TYPE_USHORT_4444_ARGB = 4444;

	/** 16-bit pixel format containing 5 bits for each color and no alpha */
	@Api
	int TYPE_USHORT_555_RGB = 555;

	/** 
	 * 16-bit pixel format containing 5 bits for red color, 6 bits for green
	 * color, and 5 bits for blue color.
	 */
	@Api
	int TYPE_USHORT_565_RGB = 565;

	/***
	 * Draws an image into the specified position on the target {@link Canvas}. 
	 * 
	 * Note that some Nokia VMs have an undisclosed behavior where they
	 * translate the graphics object back to (0,0) and restore it afterwards,
	 * something that a select few games like Fantasy Zone (176x208 version)
	 * make use of. This has to be fixed through a user-space compatibility
	 * flag, as that behavior is in no way the default, which works very
	 * closely to MIDP 2. In fact, this method has been deprecated since
	 * Nokia UI API 1.1 in favor of MIDP 2.0's {@link Graphics#drawRegion(
	 * Image, int, int, int, int, int, int, int, int)}
	 *
	 * @param __img The image data to be drawn
	 * @param __x X position of the graphics object (adds to translation)
	 * @param __y Y position of the graphics object (adds to translation)
	 * @param __anchor The anchoring to be used in rendering (same as MIDP)
	 * @param __manipulation The transformation to be done on the image, can be
	 * any of the supported DirectGraphics manipulations
	 * @since 2025/11/25
	 */
	@ApiDefinedDeprecated
	@Api
	void drawImage(@NotNull Image __img, int __x, int __y,
		@MagicConstant(flagsFromClass = Graphics.class) int __anchor,
		@MagicConstant(flagsFromClass = DirectGraphics.class)
		int __manipulation);

	/**
	 * Draws the pixel data from the specified array onto the specified region
	 * of the target Canvas. Pixels are expected to be in one of the available
	 * BYTE types in {@link DirectGraphics}.
	 *
	 * @param __pixels The data array containing byte data to be drawn
	 * @param __alphaMask Alpha mask (if not null) to apply to
	 * the data in __pixels.
	 * @param __offset The point where array reads (for both __pixels and
	 * __alphaMask) will begin.
	 * @param __scanlength The offset between different rows of data (treat it
	 * as the "width" of the image in the array).
	 * @param __x X position of the graphics object (adds to translation)
	 * @param __y Y position of the graphics object (adds to translation)
	 * @param __w Width of the area to be drawn.
	 * @param __h Height of the area to be drawn.
	 * @param __manipulation The transformation to be done on the image
	 * @param __format The TYPE_BYTE_* format used in __pixels' data
	 * @throws IllegalArgumentException If width or height are less than zero,
	 * or the pixel format is unsupported.
	 * @throws NullPointerException if the the pixels array is null
	 * @throws ArrayIndexOutOfBoundsException If the offset is out of bounds
	 * @since 2025/11/25
	 */
	@Api
	void drawPixels(@NotNull byte[] __pixels, @Nullable byte[] __alphaMask,
		int __offset, int __scanlength, int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		@MagicConstant(flagsFromClass = DirectGraphics.class) 
		int __manipulation,
		@MagicConstant(flagsFromClass = DirectGraphics.class) int __format);

	/**
	 * Draws the pixel data from the specified array onto the specified region
	 * of the target Canvas. Pixels are expected to be in one of the available
	 * USHORT types in {@link DirectGraphics}.
	 *
	 * @param __pixels The data array containing short data to be drawn
	 * @param __transparency Indicates whether transparency has to be processed
	 * @param __offset The point where array reads will begin.
	 * @param __scanlength The offset between different rows of data (treat it
	 * as the "width" of the image in the array).
	 * @param __x X position of the graphics object (adds to translation)
	 * @param __y Y position of the graphics object (adds to translation)
	 * @param __w Width of the area to be drawn.
	 * @param __h Height of the area to be drawn.
	 * @param __manipulation The transformation to be done on the image
	 * @param __format The TYPE_USHORT_* format used in __pixels' data
	 * @throws IllegalArgumentException If width or height are less than zero,
	 * or the pixel format is unsupported.
	 * @throws NullPointerException if the the pixels array is null
	 * @throws ArrayIndexOutOfBoundsException If the offset is out of bounds
	 * @since 2025/11/25
	 */
	@Api
	void drawPixels(@NotNull short[] __pixels, boolean __transparency,
		int __offset, int __scanlength, int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		@MagicConstant(flagsFromClass = DirectGraphics.class) 
		int __manipulation,
		@MagicConstant(flagsFromClass = DirectGraphics.class) int __format);

	/**
	 * Draws the pixel data from the specified array onto the specified region
	 * of the target Canvas. Pixels are expected to be in one of the available
	 * INT formats in {@link DirectGraphics}.
	 *
	 * @param __pixels The data array containing integer data to be drawn
	 * @param __transparency Indicates whether transparency has to be processed
	 * @param __offset The point where array reads will begin.
	 * @param __scanlength The offset between different rows of data (treat it
	 * as the "width" of the image in the array).
	 * @param __x X position of the graphics object (adds to translation)
	 * @param __y Y position of the graphics object (adds to translation)
	 * @param __w Width of the area to be drawn.
	 * @param __h Height of the area to be drawn.
	 * @param __manipulation The transformation to be done on the image
	 * @param __format The TYPE_INT_* format used in __pixels' data
	 * @throws IllegalArgumentException If width or height are less than zero
	 * or the pixel format is invalid.
	 * @throws NullPointerException if the the pixels array is null
	 * @throws ArrayIndexOutOfBoundsException If the offset is out of bounds
	 * @since 2025/11/25
	 */
	@Api
	void drawPixels(@NotNull int[] __pixels, boolean __transparency,
		int __offset, int __scanlength, int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE)int __w,
		@Range(from = 0, to = Integer.MAX_VALUE)int __h,
		@MagicConstant(flagsFromClass = DirectGraphics.class) 
		int __manipulation,
		@MagicConstant(valuesFromClass = DirectGraphics.class) int __format);

	/**
	 * Draws a polygon using the color passed to {@code __argbColor}. The color
	 * set by {@code __argbColor} will be used *ONLY* for the render operation,
	 * with the previously set color being restored afterwards.
	 *
	 * @param __xPoints An array of x coordinates for the vertices
	 * @param __xOffset The offset from which X positions should be read
	 * @param __yPoints An array of x coordinates for the vertices
	 * @param __yOffset The offset from which Y positions should be read
	 * @param __nPoints How many points/vertices should be drawn
	 * @param __argbColor The color to be used for rendering the triangle.
	 * @throws NullPointerException If {@code __xPoints} or {@code __yPoints}
	 * are null.
	 * @throws IllegalArgumentException If any of {@code __xOffset, __yOffset,
	 * __nPoints} are less than zero, or the sum of either
	 * {@code __xOffset, __yOffset} with {@code __nPoints} go out of bounds
	 * for {@code __xPoints} and {@code __yPoints} respectively.
	 * @since 2025/11/25
	 */
	@Api
	void drawPolygon(@NotNull int[] __xPoints,
		@Range(from = 0, to = Integer.MAX_VALUE) int __xOffset,
		@NotNull int[] __yPoints,
		@Range(from = 0, to = Integer.MAX_VALUE) int __yOffset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __nPoints,
		int __argbColor);

	/**
	 * Draws a triangle using the color passed to {@code __argbColor}. The
	 * color set by {@code __argbColor} will be used *ONLY* for the render
	 * operation, with the previously set color being restored afterwards.
	 *
	 * @param __x1 X coordinate of the first vertex.
	 * @param __y1 Y coordinate of the first vertex.
	 * @param __x2 X coordinate of the second vertex.
	 * @param __y2 Y coordinate of the second vertex.
	 * @param __x3 X coordinate of the third vertex.
	 * @param __y3 Y coordinate of the third vertex.
	 * @param __argbColor The color to be used for rendering the triangle.
	 * @since 2025/11/25
	 */
	@Api
	void drawTriangle(int __x1, int __y1, int __x2, int __y2, int __x3,
		int __y3, int __argbColor);

	/**
	 * Draws a filled Polygon using the color passed to {@code __argbColor},
	 * the lines which make up the polygon are included in the filled area.
	 * The color set by {@code __argbColor} will be used *ONLY* for the render
	 * operation, with the previously set color being restored afterwards.
	 *
	 * @param __xPoints An array of x coordinates for the vertices
	 * @param __xOffset The offset from which X positions should be read
	 * @param __yPoints An array of x coordinates for the vertices
	 * @param __yOffset The offset from which Y positions should be read
	 * @param __nPoints How many points/vertices should be drawn
	 * @param __argbColor The color to be used for rendering the polygon.
	 * @throws NullPointerException If {@code __xPoints} or {@code __yPoints}
	 * are null.
	 * @throws IllegalArgumentException If any of {@code __xOffset, __yOffset,
	 * __nPoints} are less than zero, or the sum of either
	 * {@code __xOffset, __yOffset} with {@code __nPoints} go out of bounds
	 * for {@code __xPoints} and {@code __yPoints} respectively.
	 * @since 2025/11/25
	 */
	@Api
	void fillPolygon(@NotNull int[] __xPoints,
		@Range(from = 0, to = Integer.MAX_VALUE) int __xOffset,
		@NotNull int[] __yPoints,
		@Range(from = 0, to = Integer.MAX_VALUE) int __yOffset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __nPoints,
		int __argbColor);

	/**
	 * Draws a filled triangle using the color passed to __argbColor. Functions
	 * almost like the method of same name in MIDP 2.0's Graphics class,
	 * aside from the {@code __argbColor} argument to use a specific color
	 * for the triangle alone.
	 *
	 * @param __x1 X coordinate of the first vertex.
	 * @param __y1 Y coordinate of the first vertex.
	 * @param __x2 X coordinate of the second vertex.
	 * @param __y2 Y coordinate of the second vertex.
	 * @param __x3 X coordinate of the third vertex.
	 * @param __y3 Y coordinate of the third vertex.
	 * @param __argbColor The color to be used for rendering the triangle.
	 * @since 2025/11/25
	 */
	@Api
	void fillTriangle(int __x1, int __y1, int __x2, int __y2, int __x3,
		int __y3, int __argbColor);

	/**
	 * Returns the alpha component of the currently set color
	 *
	 * @return The integer returned contains ONLY the alpha component of the
	 * current rendering color.
	 * @since 2025/11/25
	 */
	@Api
	int getAlphaComponent();

	/**
	 * Returns the platform's native pixel format
	 *
	 * @return The integer returned contains the native pixel format used by
	 * the platform, which can be one of the type constants defined in
	 * {@link DirectGraphics}, like {@link DirectGraphics#TYPE_INT_8888_ARGB}.
	 * @see DirectGraphics
	 * @since 2025/12/07
	 */
	@Api
	@MagicConstant(valuesFromClass = DirectGraphics.class) 
	int getNativePixelFormat();

	/**
	 * Gets pixels from the platform's Graphics context and saves into a byte
	 * buffer ({@code __pixels}).
	 * 
	 * If {@code __alphaMask} is not null, the read pixel's alpha value is
	 * saved into it, converted to a valid range depending on the specified
	 * {@code __format} argument.
	 *
	 * @param __pixels The byte buffer to save the context's data into
	 * @param __alphaMask The optional alpha mask array to write pixels' alpha
	 * data into.
	 * @param __offset The starting offset from which to place data into the
	 * received byte buffer.
	 * @param __scanlength The scanlength (each row's width) of the received
	 * byte buffer.
	 * @param __x The starting X position of the context region to be read.
	 * @param __y The starting Y position of the context region to be read.
	 * @param __w The width of the context region to be read.
	 * @param __h The height of the context region to be read.
	 * @param __format The pixel format that the context data must be converted
	 * to before being placed into the byte buffer.
	 * @since 2025/12/07
	 */
	@Api
	void getPixels(@NotNull byte[] __pixels, @Nullable byte[] __alphaMask,
		int __offset, int __scanlength,
		@Range(from = 0, to = Integer.MAX_VALUE) int __x,
		@Range(from = 0, to = Integer.MAX_VALUE) int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		@MagicConstant(valuesFromClass = DirectGraphics.class) int __format);

	/**
	 * Gets pixels from the platform's Graphics context and saves into an int
	 * buffer ({@code __pixels}).
	 * 
	 * Pixels are blended into the {@code __pixels} buffer when formats that
	 * support alpha channel are requested, as applications may use it as a
	 * back buffer.
	 *
	 * @param __pixels The int buffer to save the context's data into
	 * @param __alphaMask The optional alpha mask array to write pixels' alpha
	 * data into.
	 * @param __offset The starting offset from which to place data into the
	 * received int buffer.
	 * @param __scanlength The scanlength (each row's width) of the received
	 * int buffer.
	 * @param __x The starting X position of the context region to be read.
	 * @param __y The starting Y position of the context region to be read.
	 * @param __w The width of the context region to be read.
	 * @param __h The height of the context region to be read.
	 * @param __format The pixel format that the context data must be converted
	 * to before being placed into the int buffer.
	 * @since 2025/12/07
	 */
	@Api
	void getPixels(@NotNull int[] __pixels,
		int __offset, int __scanlength,
		@Range(from = 0, to = Integer.MAX_VALUE) int __x,
		@Range(from = 0, to = Integer.MAX_VALUE) int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		@MagicConstant(valuesFromClass = DirectGraphics.class) int __format);

	/**
	 * Gets pixels from the platform's Graphics context and saves into a short
	 * buffer ({@code __pixels}).
	 *
	 * Pixels are blended into the {@code __pixels} buffer when formats that
	 * support alpha channel are requested, as applications may use it as a
	 * back buffer.
	 * 
	 * @param __pixels The short buffer to save the context's data into
	 * @param __offset The starting offset from which to place data into the
	 * received short buffer.
	 * @param __scanlength The scanlength (each row's width) of the received
	 * short buffer.
	 * @param __x The starting X position of the context region to be read.
	 * @param __y The starting Y position of the context region to be read.
	 * @param __w The width of the context region to be read.
	 * @param __h The height of the context region to be read.
	 * @param __format The pixel format that the context data must be converted
	 * to before being placed into the short buffer.
	 * @since 2025/12/07
	 */
	@Api
	void getPixels(@NotNull short[] __pixels,
		int __offset, int __scanlength,
		@Range(from = 0, to = Integer.MAX_VALUE) int __x,
		@Range(from = 0, to = Integer.MAX_VALUE) int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h,
		@MagicConstant(valuesFromClass = DirectGraphics.class) int __format);

	/**
	 * Sets a new ARGB color on the target DirectGraphics object.
	 *
	 * @param __argbColor The color to be used for any subsequent rendering.
	 * @since 2025/11/25
	 */
	@Api
	void setARGBColor(int __argbColor);
}

