// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.nttdocomo.ui.BGColor;

/**
 * Represents a raster image.
 *
 * @see javax.microedition.lcdui.Image
 * @since 2021/11/30
 */
@Api
@SuppressWarnings("AbstractClassWithOnlyOneDirectInheritor")
public abstract class Image
{
	/** The actually contained image. */
	final javax.microedition.lcdui.Image _midpImage;

	/** The background color of the image. */
	final BGColor _bgColor;

	@Api
	protected Image()
	{
		// Not accessible
		this._midpImage = null;
		this._bgColor = null;
	}

	/**
	 * Initializes the image which uses the given image as the source.
	 *
	 * @param __midpImage The image to wrap.
	 * @param __bgColor The background color to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	Image(javax.microedition.lcdui.Image __midpImage, BGColor __bgColor)
		throws NullPointerException
	{
		if (__midpImage == null)
			throw new NullPointerException("NARG");

		this._midpImage = __midpImage;
		this._bgColor = __bgColor;
	}

	@Api
	public abstract void dispose();

	@Api
	public void getAlpha()
		throws UIException
	{
		throw Debugging.todo();
	}

	/**
	 * Returns the {@link Graphics} which is used to draw into the given image.
	 * This only works for mutable images created by either
	 * {@link #createImage(int, int)} or
	 * {@link #createImage(int, int, int[], int)}.
	 *
	 * @return The graphics for drawing onto this image.
	 * @throws UnsupportedOperationException If the image is not mutable.
	 * @throws UIException If the image has already been disposed of with
	 * the error {@link UIException#ILLEGAL_STATE}.
	 * @since 2024/01/06
	 */
	@Api
	public Graphics getGraphics()
		throws UnsupportedOperationException, UIException
	{
		// If there is no base image, we cannot do anything
		javax.microedition.lcdui.Image midpImage = this._midpImage;
		if (midpImage == null)
			throw new UnsupportedOperationException();

		// Try to wrap the graphics
		try
		{
			return new __Graphics2__(midpImage.getGraphics(), this._bgColor,
				null);
		}

		// MIDP gives IllegalStateException instead...
		catch (IllegalStateException __e)
		{
			/* {@squirreljme.error AH11 Image is not mutable.} */
			throw new UnsupportedOperationException("AH11", __e);
		}
	}

	/**
	 * Returns the image height.
	 *
	 * @return The image height.
	 * @throws UIException If the image has been disposed, this will
	 * be {@link UIException#ILLEGAL_STATE}.
	 * @since 2024/06/24
	 */
	@Api
	public int getHeight()
		throws UIException
	{
		javax.microedition.lcdui.Image midpImage = this._midpImage;
		if (midpImage == null)
			throw new UIException(UIException.ILLEGAL_STATE);

		return midpImage.getHeight();
	}

	@Api
	public int getTransparentColor()
		throws UIException
	{
		throw Debugging.todo();
	}

	/**
	 * Returns the image width.
	 *
	 * @return The image width.
	 * @throws UIException If the image has been disposed, this will
	 * be {@link UIException#ILLEGAL_STATE}.
	 * @since 2024/06/24
	 */
	@Api
	public int getWidth()
		throws UIException
	{
		javax.microedition.lcdui.Image midpImage = this._midpImage;
		if (midpImage == null)
			throw new UIException(UIException.ILLEGAL_STATE);

		return midpImage.getWidth();
	}

	@Api
	public void setAlpha(int __alpha)
		throws IllegalArgumentException, UIException
	{
		/* {@squirreljme.error AH2b Invalid alpha value.} */
		if (__alpha < 0 || __alpha > 255)
			throw new IllegalArgumentException("AH2b");

		throw Debugging.todo();
	}

	@Api
	public void setTransparentColor(int __color)
		throws UIException
	{
		throw Debugging.todo();
	}

	@Api
	public void setTransparentEnabled(boolean __enable)
		throws UIException
	{
		throw Debugging.todo();
	}

	/**
	 * Creates a new blank image where the initial color of the image is set
	 * to the background color of the implementation. To draw onto the image,
	 * the {@link #getGraphics()} must be used.
	 *
	 * @param __w The image width.
	 * @param __h The image height.
	 * @return The resultant image.
	 * @throws IllegalArgumentException If the width and/or height are zero
	 * or negative.
	 * @since 2024/01/06
	 */
	@Api
	public static Image createImage(int __w, int __h)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error AH10 Zero or negative image size.} */
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("AH10");

		// Initialize base source MIDP image
		javax.microedition.lcdui.Image midpImage =
			javax.microedition.lcdui.Image.createImage(__w, __h);

		// Fill with background color
		javax.microedition.lcdui.Graphics midpGfx = midpImage.getGraphics();
		int bgColor = Display.__midpDisplay().getColor(
			javax.microedition.lcdui.Display.COLOR_BACKGROUND);
		midpGfx.setColor(bgColor);
		midpGfx.fillRect(0, 0, __w, __h);

		// Setup resultant image
		return new __MutableImage__(midpImage, new BGColor(bgColor));
	}

	/**
	 * Creates a new mutable RGB image from the specified RGB data array.
	 *
	 * This is the same as creating an image and immediately calling
	 * {@link Graphics#setRGBPixels(int, int, int, int, int[], int)} with the
	 * given data.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __data The array containing RGB pixel data.
	 * @param __off The offset from which to start reading RGB pixels from the
	 * {@code __data} array.
	 * @return The resulting mutable RGB image.
	 * @throws ArrayIndexOutOfBoundsException If {@code __off} is negative; or
	 * the result of {@code (__off + __w * __h)} is larger than the length of
	 * the {@code __data} array.
	 * @throws IllegalArgumentException If {@code __w} or {@code __h} are less
	 * than, or equal to, zero.
	 * @throws NullPointerException If {@code __data} is null.
	 * @since 2026/10/01
	 */
	@Api
	public static Image createImage(int __w, int __h, int[] __data, int __off)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");

		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("NEGV");

		int len = __w * __h;
		if (__off < 0 || (__off + len) > __data.length || (__off + len) < 0)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		Image img = Image.createImage(__w, __h);

		img.getGraphics().setRGBPixels(0, 0, __w, __h, __data, __off);

		return img;
	}
}
