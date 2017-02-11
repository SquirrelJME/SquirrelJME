// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.squirreljme.lcdui.BasicGraphics;

public class Image
{
	/** The RGB image data. */
	private final int[] _data;
	
	/** Image width. */
	private final int _width;
	
	/** Image height. */
	private final int _height;
	
	/** Is this image mutable? */
	private final boolean _mutable;
	
	/** Does this have an alpha channel? */
	private final boolean _alpha;
	
	Image()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Initializes the image with the given settings.
	 *
	 * @parma __data The image data, this is used directly.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __mut If this image is mutable
	 * @param __alpha If this image has an alpha channel.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	Image(int[] __data, int __w, int __h, boolean __mut, boolean __alpha)
	{
		// Check
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._data = __data;
		this._width = __w;
		this._height = __h;
		this._mutable = __mut && !isAnimated() && !isScalable();
		this._alpha = __alpha;
	}
	
	public void getARGB16(short[] __data, int __off, int __scanlen, int __x,
		int __y, int __w, int __h)
	{
		throw new Error("TODO");
	}
	
	/**
	 * This obtains the graphics interface which is used to draw on top of
	 * a mutable image.
	 *
	 * It defaults to:
	 * The clipping region covers the entire image.
	 * The color is a fully opaque black.
	 * The blending mode is {@link Graphics#SRC_OVER}.
	 * The stroke is {@link Graphics#SOLID}.
	 * The font is the default font.
	 * The coordinate origin is the top-left corner.
	 *
	 * The blending mode may only be changed to {@link Graphics#SRC} if the
	 * image has an alpha channel.
	 *
	 * @return A new graphics drawer.
	 * @throws IllegalStateException If the image is not mutable.
	 * @since 2017/02/10
	 */
	public Graphics getGraphics()
		throws IllegalStateException
	{
		// {@squirreljme.error EB0f Cannot get mutable graphic operations for
		// an immutable image.}
		if (!isMutable())
			throw new IllegalStateException("EB0f");
		
		// Create
		return new __Graphics__();
	}
	
	/**
	 * Returns the image height.
	 *
	 * @return The height of the image.
	 * @since 2017/02/10
	 */
	public int getHeight()
	{
		return this._height;
	}
	
	public void getRGB(int[] __a, int __b, int __c, int __d, int __e, int __f,
		int __g)
	{
		throw new Error("TODO");
	}
	
	public void getRGB16(short[] __data, int __off, int __scanlen, int __x,
		int __y, int __w, int __h)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the image width.
	 *
	 * @return The width of the image.
	 * @since 2017/02/10
	 */
	public int getWidth()
	{
		return this._width;
	}
	
	/**
	 * Returns {@code true} if this image has an alpha channel.
	 *
	 * @return {@code true} if this image has an alpha channel.
	 * @since 2017/02/10
	 */
	public boolean hasAlpha()
	{
		return this._alpha;
	}
	
	/**
	 * Returns {@code true} if this image is animated.
	 *
	 * @return {@code true} if this image is animated.
	 * @since 2017/02/10
	 */
	public boolean isAnimated()
	{
		return (this instanceof AnimatedImage);
	}
	
	/**
	 * Returns {@code true} if this image is mutable.
	 *
	 * @return {@code true} if this image is mutable.
	 * @since 2017/02/10
	 */
	public boolean isMutable()
	{
		return this._mutable && !isAnimated() && !isScalable();
	}
	
	/**
	 * Returns {@code true} if this image is scalable.
	 *
	 * @return {@code true} if this image is scalable.
	 * @since 2017/02/10
	 */
	public boolean isScalable()
	{
		return (this instanceof ScalableImage);
	}
	
	public static Image createImage(byte[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static Image createImage(int __a, int __b)
	{
		throw new Error("TODO");
	}
	
	public static Image createImage(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public static Image createImage(String __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public static Image createImage(Image __a)
	{
		throw new Error("TODO");
	}
	
	public static Image createImage(Image __a, int __b, int __c, int __d, int
		__e, int __f)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Creates an image from the specified ARGB pixel array.
	 *
	 * @param __rgb The ARGB or RGB image data to use as the image data.
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @param __alpha If {@code true} then the alpha is processed, otherwise
	 * all pixels are treated as fully opaque.
	 * @since 2017/02/10
	 */
	public static Image createRGBImage(int[] __rgb, int __w, int __h, boolean 
		__alpha)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__rgb == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB0d Invalid image size. (The width;
		// The height)}
		int area = __w * __h;
		if (__w <= 0 || __h <= 0 || area <= 0)
			throw new IllegalArgumentException(String.format("EB0d %d %d",
				__w, __h));
		
		// {@squirreljme.error EB0e The input integer buffer is shorter than
		// the specified area.}
		int rgblen;
		if ((rgblen = __rgb.length) < area)
			throw new IndexOutOfBoundsException("EB0e");
		
		// Use a cloned copy of the pixel data?
		if (rgblen == area)
			__rgb = __rgb.clone();
		
		// Otherwise initialize a new one
		else
		{
			int[] copy = new int[area];
			for (int i = 0; i < area; i++)
				copy[i] = __rgb[i];
			__rgb = copy;
		}
		
		// If there is no alpha channel, force all of it opaque
		if (!__alpha)
			for (int i = 0; i < area; i++)
				__rgb[i] |= 0xFF000000;
		
		// Setup image
		return new Image(__rgb, __w, __h, false, __alpha);
	}
	
	public static Image createImage(int __w, int __h, boolean __alpha,
		int __fill)
	{
		throw new Error("TODO");
	}
	
	public static Image createImage(Image __i, int __x, int __y, int __w,
		int __h, int __trans, int __iw, int __ih)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Implements the primitive operations needed to draw onto images.
	 *
	 * @since 2017/02/10
	 */
	private final class __Graphics__
		extends BasicGraphics
	{
		/**
		 * {@inheritDoc}
		 * @since 2017/02/10
		 */
		@Override
		protected boolean primitiveHasAlphaChannel()
		{
			return Image.this._alpha;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/10
		 */
		@Override
		protected void primitiveLine(int __x1, int __y1, int __x2,
			int __y2, int __color, boolean __dotted, boolean __blend)
		{
			throw new Error("TODO");
		}
	}
}


