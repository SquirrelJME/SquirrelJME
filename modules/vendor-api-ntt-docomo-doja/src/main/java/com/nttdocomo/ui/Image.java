// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

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
	final __BGColor__ _bgColor;
	
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
	Image(javax.microedition.lcdui.Image __midpImage, __BGColor__ __bgColor)
		throws NullPointerException
	{
		if (__midpImage == null)
			throw new NullPointerException("NARG");
		
		this._midpImage = __midpImage;
		this._bgColor = __bgColor;
	}
	
	@Api
	public abstract void dispose();
	
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
			return new Graphics(midpImage.getGraphics(), this._bgColor);
		}
		
		// MIDP gives IllegalStateException instead...
		catch (IllegalStateException __e)
		{
			/* {@squirreljme.error AH11 Image is not mutable.} */
			throw new UnsupportedOperationException("AH11", __e);
		}
	}
	
	@Api
	public int getHeight()
		throws UIException
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getTransparentColor()
		throws UIException
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getWidth()
		throws UIException
	{
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
		return new __MutableImage__(midpImage, new __BGColor__(bgColor));
	}
	
	@Api
	public static Image createImage(int __w, int __h, int[] __data, int __off)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		throw Debugging.todo();
	}
}
