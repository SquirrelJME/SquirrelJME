// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.opt.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.gfx.ExtraGraphics;
import cc.squirreljme.runtime.nttdocomo.ui.BGColor;
import cc.squirreljme.runtime.nttdocomo.ui.LockFlush;
import com.nttdocomo.ui.Graphics;
import com.nttdocomo.ui.Image;
import com.nttdocomo.ui.UIException;

@Api
public abstract class Graphics2
	extends Graphics
{
	/** Represents the normal coordinate mode. */
	@Api
	public static final int CM_NORMAL = 0;

	/** Represents the zoomed coordinate mode. */
	@Api
	public static final int CM_ZOOM = 256;

	/** Represents the REPLACE pixel blending mode (default). */
	@Api
	public static final int OP_REPL = 0;

	/** Represents the ADD pixel blending mode. */
	@Api
	public static final int OP_ADD = 1;

	/** Represents the SUB pixel blending mode. */
	@Api
	public static final int OP_SUB = 2;

	/** The base graphics to forward to. */
	private final javax.microedition.lcdui.Graphics _graphics;

	/**
	 * Wraps the given graphics object.
	 *
	 * @param __g The graphics to wrap.
	 * @param __bgColor The background color for
	 * {@link #clearRect(int, int, int, int)}.
	 * @param __flush Optional flush callback to be executed when this
	 * occurs.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/01
	 */
	protected Graphics2(javax.microedition.lcdui.Graphics __g,
		BGColor __bgColor, LockFlush __flush)
		throws NullPointerException
	{
		super(__g, __bgColor, __flush);
		this._graphics = __g;
	}

	/**
	 * {@inheritDoc}
	 * @deprecated Since DoJa-3.5 in favor of
	 * {@link Graphics#drawImageMap(com.nttdocomo.ui.ImageMap, int, int)}.
	 * @since 2026/10/01
	 */
	@Api
	@ApiDefinedDeprecated
	public void drawImageMap(com.nttdocomo.ui.ImageMap __map, int __x, int __y)
	{
		super.drawImageMap(__map, __x, __y);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated Since DoJa-3.0 in favor of
	 * {@link Graphics#drawScaledImage(com.nttdocomo.ui.Image, int, int,
	 * int, int, int, int, int, int)}.
	 * @since 2026/10/01
	 */
	@Api
	@ApiDefinedDeprecated
	public void drawScaledImage(Image __i, int __dx, int __dy,
		int __dw, int __dh, int __sx, int __sy, int __sw, int __sh)
		throws IllegalArgumentException, UIException, NullPointerException
	{
		super.drawScaledImage(__i, __dx, __dy, __dw, __dh,__sx, __sy,
			__sw, __sh);
	}

	/**
	 * Draws the specified image index from an animated GIF containing multiple
	 * images.
	 *
	 * @param __image The animated GIF containing image data.
	 * @param __k The image index to draw.
	 * @param __x The x coordinate of the drawing operation.
	 * @param __y The y coordinate of the drawing operation.
	 * @throws NullPointerException If {@code __image} is null.
	 * @throws IllegalArgumentException If {@code __k} is either negative or an
	 * invalid index on the animated GIF.
	 * @throws UIException If the GIF image argument has already been unused or
	 * disposed of.
	 * @since 2026/10/01
	 */
	@Api
	public void drawNthImage(com.nttdocomo.ui.MediaImage __image, int __k,
		int __x, int __y)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__image == null)
			throw new IllegalArgumentException("NARG");

		/* {@squirreljme.error AH2a Invalid image index.} */
		if (__k < 0)
			throw new IllegalArgumentException("AH2a");

		throw Debugging.todo();
	}

	/**
	 * {@inheritDoc}
	 * @deprecated Since DoJa-3.5 in favor of
	 * {@link Graphics#drawSpriteSet(com.nttdocomo.ui.SpriteSet)}.
	 * @since 2026/10/01
	 */
	@Api
	@ApiDefinedDeprecated
	public void drawSpriteSet(com.nttdocomo.ui.SpriteSet __sprites)
		throws NullPointerException, UIException
	{
		super.drawSpriteSet(__sprites);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated Since DoJa-3.5 in favor of
	 * {@link Graphics#drawSpriteSet(com.nttdocomo.ui.SpriteSet, int, int)}.
	 * @since 2026/10/01
	 */
	@Api
	@ApiDefinedDeprecated
	public void drawSpriteSet(com.nttdocomo.ui.SpriteSet __sprites,
		int __offset, int __count)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		super.drawSpriteSet(__sprites, __offset, __count);
	}

	/**
	 * Draws the specified number with the requested amount of digits,
	 * justified to the right.
	 *
	 * For fixed-width fonts, this is equivalent to calling
	 * {@link Graphics#drawString(String, int, int)}
	 * after extracting digit characters from:
	 * "(an infinite blank string) + (a string representation of value)"
	 * from the right side.
	 *
	 * Proportional fonts result in device-dependent behavior, although an area
	 * large enough to display the value with the given number of digits must
	 * be allocated either way.
	 *
	 * @param __x The baseline x coordinate to draw the number.
	 * @param __y The baseline y coordinate to draw the number.
	 * @param __value The number to draw.
	 * @param __digit The amount of digits to draw.
	 * @throws IllegalArgumentException If the {@code __digit} argument is zero
	 * or less.
	 * @since 2026/10/01
	 */
	@Api
	public void drawNumber(int __x, int __y, int __value, int __digit)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error AH2d Digit must be greater than 0.} */
		if (__digit <= 0)
			throw new IllegalArgumentException("AH2d");

		throw Debugging.todo();
	}

	/**
	 * Retrieves pixels from a specified rectangle of the screen as an
	 * {@link Image}. Clipping is not considered for this operation, although
	 * the source's drawable area bounds still apply. This means that if
	 * the specified rectangle area extends outside of the display's width
	 * or height, the returned image will have its width and height limited
	 * to the display's size. The returned image is to be considered an
	 * off-screen image, just as if it was created by
	 * {@link Image#createImage(int, int)}.
	 *
	 * @param __x The X coordinate of the rectangle's top-left corner.
	 * @param __y The Y coordinate of the rectangle's top-left corner.
	 * @param __width The width of the rectangle.
	 * @param __height The height of the rectangle.
	 * @throws IllegalArgumentException If width or height are zero or less.
	 * @since 2026/10/01
	 */
	@Api
	public Image getImage(int __x, int __y, int __width, int __height)
	{
		if (__x < 0)
			__x = 0;
		if (__y < 0)
			__y = 0;

		ExtraGraphics g = this.__extra();

		if (__x + __width > g.surfaceWidth())
			__width = g.surfaceWidth() - __x;

		if (__y + __height > g.surfaceHeight())
			__height = g.surfaceHeight() - __y;

		/* {@squirreljme.error AH2e Width and height must be greater
			than 0.} */
		if (__width <= 0 || __height <= 0)
			throw new IllegalArgumentException("AH2e");

		return Image.createImage(__width, __height,
			super.getRGBPixels(__x, __y, __width, __height, null, 0), 0);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated Since DoJa-3.0 in favor of
	 * {@link Graphics#getPixel(int, int)}.
	 * @since 2026/10/01
	 */
	@ApiDefinedDeprecated
	@Api
	public int getPixel(int __x, int __y)
		throws NullPointerException, UIException
	{
		return super.getPixel(__x, __y);
	}

	/**
	 * Returns the screen refresh rate of the device in microseconds. If the
	 * screen has a refresh rate of 60hz, that means this function will return
	 * {@code 1000000/60 = 16667}.
	 *
	 * @return The screen refresh rate in microseconds.
	 * @since 2026/10/01
	 */
	@Api
	public int getSyncUnlockInterval()
	{
		// TODO: Return the device's screen refresh rate in us?
		throw Debugging.todo();
	}

	/**
	 * Sets the coordinate mode to be used for drawing.
	 * {@link Graphics2#CM_NORMAL} uses the normal coordinate system while
	 * {@link Graphics2#CM_ZOOM} has the x and y arguments of draw methods be
	 * added to the origin set by {@link Graphics#setOrigin(int, int)}, with
	 * resulting values being divided by 256 in order to determine the final
	 * drawing coordinate.
	 *
	 * @param __mode The coordinate mode to use.
	 * @throws IllegalArgumentException If {@code __mode} is not a valid
	 * coordinate mode.
	 * @since 2026/10/01
	 */
	@Api
	public void setCoordinateMode(int __mode)
	{
		/* {@squirreljme.error AH2f Invalid coordinate mode.} */
		if (__mode != Graphics2.CM_NORMAL && __mode != Graphics2.CM_ZOOM)
			throw new IllegalArgumentException("AH2f");

		throw Debugging.todo();
	}

	/**
	 * {@inheritDoc}
	 * @deprecated Since DoJa-3.0 in favor of
	 * {@link Graphics#setFlipMode(int)}.
	 * @since 2026/10/01
	 */
	@Deprecated
	@Api
	public void setFlipMode(int __mode)
		throws IllegalArgumentException
	{
		super.setFlipMode(__mode);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated Since DoJa-3.0 in favor of
	 * {@link Graphics#setPixel(int, int)}.
	 * @since 2026/10/01
	 */
	@Deprecated
	@Api
	public void setPixel(int __x, int __y)
		throws NullPointerException, UIException
	{
		super.setPixel(__x, __y);
	}

	/**
	 * {@inheritDoc}
	 * @deprecated Since DoJa-3.0 in favor of
	 * {@link Graphics#setPixel(int, int, int)}.
	 * @since 2026/10/01
	 */
	@Deprecated
	@Api
	public void setPixel(int __x, int __y, int __color)
		throws NullPointerException, UIException
	{
		super.setPixel(__x, __y, __color);
	}

	/**
	 * Sets the pixel blending mode to use in subsequent drawing operations.
	 *
	 * If {@code __operator} equals {@link Graphics2#OP_REPL}, the drawing
	 * operations will use the standard alpha blending formula of
	 * {@code Dst = Src * srcRatio}.
	 *
	 * If the operator is {@link Graphics2#OP_SUB}, the drawing formula becomes
	 * {@code Dst = Dst * dstRatio - Src * srcRatio}.
	 *
	 * If the operator is {@link Graphics2#OP_ADD}, the drawing formula will
	 * then be {@code Dst = Dst * dstRatio + Src * srcRatio}.
	 *
	 * @param __operator The rendering operator to use.
	 * @param __srcRatio The degree to which the source will affect the
	 * rendering operation, from 0 to 255.
	 * @param __dstRatio The degree to which the destination will affect the
	 * rendering operation, from 0 to 255.
	 * @throws IllegalArgumentException If {@code __operator},
	 * {@code __srcRatio} or {@code __dstRatio} are not valid.
	 * @since 2026/10/01
	 */
	@Api
	public void setRenderMode(int __operator, int __srcRatio, int __dstRatio)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error AH2j Invalid renderMode operator.} */
		if (__operator < Graphics2.OP_REPL || __operator > Graphics2.OP_SUB)
			throw new IllegalArgumentException("AH2j");

		/* {@squirreljme.error AH2j Invalid renderMode source ratio.} */
		if (__srcRatio < 0 || __srcRatio > 255)
			throw new IllegalArgumentException("AH2k");

		/* {@squirreljme.error AH2l Invalid renderMode destination ratio.} */
		if (__dstRatio < 0 || __dstRatio > 255)
			throw new IllegalArgumentException("AH2l");

		ExtraGraphics g = this.__extra();

		throw Debugging.todo("Graphics2 Blend Modes");
	}

	/**
	 * Unlocks the graphics buffer (just like calling for
	 * {@code Graphics.unlock(true)}), but synchronized with the device's
	 * vertical sync, making it useful to remove tearing and flickering.
	 *
	 * The {@code __interval} argument indicates how many v-syncs must happen
	 * before updating the display. For an interval of 2 on a screen that
	 * refreshes 60 times per second, this results in 30 updates per second,
	 * an interval of 3 means 20 updates per second, and so on.
	 *
	 * This method does nothing and returns 0 if it is called while no buffer
	 * lock (by means of {@link Graphics#lock()}) is currently in place due to
	 * a lock count of 0, or the target image has been disposed of.
	 *
	 * Note that this call IS blocking, meaning that rendering and input
	 * events will not execute while this method waits for the specified
	 * amount of v-syncs.
	 *
	 * @param __interval The amount of v-syncs to wait for before unlocking
	 * the buffer and unblocking execution.
	 * @return How many v-syncs were actually waited for. Usually, the same as
	 * the {@code __interval} value.
	 * @throws IllegalArgumentException If {@code __interval} is 0 or less;
	 * or if {@code __interval} exceeds the maximum value supported by the
	 * device.
	 * @since 2026/10/01
	 */
	@Api
	public int syncUnlock(int __interval)
	{
		if(__interval <= 0)
			throw new IllegalArgumentException("INVL");

		throw Debugging.todo();
	}

	/**
	 * Returns an intermediate color between two specified colors and a ratio
	 * used to indicate each one's contribution to the result.
	 *
	 * The calculation done for the intermediate color is as follows:
	 * {@code ( (255 - __ratio) * __color1 + __ratio * __color2 ) / 255}.
	 * Meaning that if the ratio is 0, the resulting color is the same as
	 * {@code __color1}, whereas a ratio of 255 means that the result is the
	 * same as {@code __color2}.
	 *
	 * How this conversion is actually made is device specific, color space
	 * conversions may be applied.
	 *
	 * @param __color1 The first color to calculate the intermediate from.
	 * @param __color2 The second color to calculate the intermediate from.
	 * @param __ratio The ratio between __color1 and __color2.
	 * @throws IllegalArgumentException If any of the arguments are invalid.
	 * @since 2026/10/01
	 */
	@Api
	public static int getIntermediateColor(int __color1, int __color2,
		int __ratio)
	{
		/* {@squirreljme.error AH2i Invalid intermediate color ratio.} */
		if (__ratio < 0 || __ratio > 255)
			throw new IllegalArgumentException("AH2i");

		if (((__color1 & 0xFF000000) != 0) ||
			((__color2 & 0xFF000000) != 0))
			throw new IllegalArgumentException("INVL");

		int red1 = (__color1 >> 16) & 0xFF;
		int green1 = (__color1 >> 8) & 0xFF;
		int blue1 = __color1 & 0xFF;

		int red2 = (__color2 >> 16) & 0xFF;
		int green2 = (__color2 >> 8) & 0xFF;
		int blue2 = __color2 & 0xFF;

		int red = (((255 - __ratio) * red1 + __ratio * red2) / 255);
		int green = (((255 - __ratio) * green1 + __ratio * green2) / 255);
		int blue = (((255 - __ratio) * blue1 + __ratio * blue2) / 255);

		return (0xFF << 24) | (red << 16) | (green << 8) | blue;
	}

	/**
	 * Returns the graphics instance as an {@link ExtraGraphics}.
	 *
	 * @return The {@link ExtraGraphics}.
	 * @throws UIException If this is not an {@link ExtraGraphics}.
	 * @since 2026/10/01
	 */
	ExtraGraphics __extra()
		throws UIException
	{
		/* {@squirreljme.error AH91 Graphics is not capable of extra
		functions.} */
		javax.microedition.lcdui.Graphics g = this._graphics;
		if (!(g instanceof ExtraGraphics))
			throw new UIException(UIException.ILLEGAL_STATE,
				"AH91");

		// Cast
		return (ExtraGraphics)g;
	}
}
