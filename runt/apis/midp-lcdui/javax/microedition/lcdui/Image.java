// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.gfx.BasicGraphics;
import cc.squirreljme.runtime.lcdui.gfx.IntArrayGraphics;
import cc.squirreljme.runtime.lcdui.gfx.PixelArrayGraphics;
import cc.squirreljme.runtime.lcdui.image.ImageReaderDispatcher;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

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
		throw new todo.TODO();
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
		
		// If no alpha, set upper channel to full opaqueness
		if (!__alpha)
			for (int i = 0, n = __data.length; i < n; i++)
				__data[i] |= 0xFF000000;
	}
	
	public final void getARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
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
	public final Graphics getGraphics()
		throws IllegalStateException
	{
		// {@squirreljme.error EB1q Cannot get mutable graphic operations for
		// an immutable image.}
		if (!isMutable())
			throw new IllegalStateException("EB1q");
		
		// Create
		return new IntArrayGraphics(this._data, this._width, this._height,
			this._alpha, this._width, 0);
	}
	
	/**
	 * Returns the image height.
	 *
	 * @return The height of the image.
	 * @since 2017/02/10
	 */
	public final int getHeight()
	{
		return this._height;
	}
	
	/**
	 * Copies RGB image data from the source image.
	 *
	 * The source image data must be within the bounds of the image.
	 *
	 * All written pixels will have an alpha value regardless if the image has
	 * an alpha channel or not. In the case the image has no alpha channel then
	 * all read pixels will have a value of {@code 0xFF} as their alpha
	 * channel.
	 *
	 * @param __b The destination array.
	 * @param __o The offset into the array.
	 * @param __sl The scanline length of the destination array, this value may
	 * be negative to indicate that pixels are placed in reverse order.
	 * @param __x The source X position.
	 * @param __y The source Y position.
	 * @param __w The width to copy, if this is zero nothing is copied.
	 * @param __h The height to copy, if this is zero nothing is copied.
	 * @throws ArrayIndexOutOfBoundsException If writing to the destination
	 * buffer would result in a write that exceeds the bounds of the array.
	 * @throws IllegalArgumentException If the source X or Y position is
	 * negative; If the source region exceeds the image bounds; If the absolute
	 * value of the scanline length is lower than the width.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/11
	 */
	public final void getRGB(int[] __b, int __o, int __sl, int __x, int __y,
		int __w, int __h)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		// Do nothing
		if (__w <= 0 || __h <= 0)
			return;
		
		// Scalable images must be rasterized
		if (isScalable())
			throw new todo.TODO();
			
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB1r The source coordinates are negative.}
		if (__x < 0 || __y < 0)
			throw new IllegalArgumentException("EB1r");
	
		// {@squirreljme.error EB1s The absolute value of the scanline length
		// exceeds the read width.}
		int absl = Math.abs(__sl);
		if (absl < __w)
			throw new IllegalArgumentException("EB1s");
		
		// {@squirreljme.error EB1t Reading of RGB data would exceed the bounds
		// out the output array.}
		int srcarea = __w * __h;
		int areasl = __sl * __h;
		if (__o < 0 || (__o + areasl) > __b.length || (__o + areasl) < 0)
			throw new ArrayIndexOutOfBoundsException("EB1t");
		
		// {@squirreljme.error EB1u The area to read exceeds the bounds of the
		// image.}
		int ex = __x + __w,
			ey = __y + __h;
		int iw = this._width,
			ih = this._height;
		if (ex > iw || ey > ih)
			throw new IllegalArgumentException("EB1u");
		
		// Read image data
		int[] data = this._data;
		for (int sy = __y, wy = 0; sy < ey; sy++, wy++)
		{
			// Calculate offsets
			int srcoff = (iw * sy) + __x;
			int dstoff = __o + (wy * __sl);
			
			// Copy data
			for (int sx = __x; sx < ex; sx++)
				__b[dstoff++] = data[srcoff++];
		}
	}
	
	public final void getRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the image width.
	 *
	 * @return The width of the image.
	 * @since 2017/02/10
	 */
	public final int getWidth()
	{
		return this._width;
	}
	
	/**
	 * Returns {@code true} if this image has an alpha channel.
	 *
	 * @return {@code true} if this image has an alpha channel.
	 * @since 2017/02/10
	 */
	public final boolean hasAlpha()
	{
		return this._alpha;
	}
	
	/**
	 * Returns {@code true} if this image is animated.
	 *
	 * @return {@code true} if this image is animated.
	 * @since 2017/02/10
	 */
	public final boolean isAnimated()
	{
		return (this instanceof AnimatedImage);
	}
	
	/**
	 * Returns {@code true} if this image is mutable.
	 *
	 * @return {@code true} if this image is mutable.
	 * @since 2017/02/10
	 */
	public final boolean isMutable()
	{
		return this._mutable && !isAnimated() && !isScalable();
	}
	
	/**
	 * Returns {@code true} if this image is scalable.
	 *
	 * @return {@code true} if this image is scalable.
	 * @since 2017/02/10
	 */
	public final boolean isScalable()
	{
		return (this instanceof ScalableImage);
	}
	
	/**
	 * Loads the specified image from the specified byte array.
	 *
	 * @param __b The array to read from.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @return The loaded image.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IllegalArgumentException If the image could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	public static Image createImage(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Could fail
		try
		{
			return createImage(new ByteArrayInputStream(__b, __o, __l));
		}
		
		// {@squirreljme.error EB1v Could not load the image data.}
		catch (IOException e)
		{
			throw new IllegalArgumentException("EB1v", e);
		}
	}
	
	/**
	 * Same as {@code createImage(__w, __h, false, 0x00FFFFFF)}.
	 *
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @return The created image.
	 * @throws IllegalArgumentException If the requested image size has a zero
	 * or negative dimension.
	 * @since 2017/02/11
	 */
	public static Image createImage(int __w, int __h)
		throws IllegalArgumentException
	{
		return createImage(__w, __h, false, 0x00FFFFFF);
	}
	
	/**
	 * Creates a mutable image that may or may not have an alpha channel.
	 *
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @param __alpha Whether it has an alpha channel.
	 * @param __c The initial color to fill.
	 * @return The created image.
	 * @throws IllegalArgumentException If the requested image size has a zero
	 * or negative dimension.
	 * @since 2017/02/11
	 */
	public static Image createImage(int __w, int __h, boolean __alpha, int __c)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB1w Zero or negative image size requested.}
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("EB1w");
		
		// Setup buffer
		int area = __w * __h;
		int[] data = new int[area];
		
		// Fill with color
		for (int i = 0; i < area; i++)
			data[i] = __c;
		
		// Create
		return new Image(data, __w, __h, true, __alpha);
	}
	
	/**
	 * Loads the image from the specified input stream.
	 *
	 * @param __is The stream to read image data from.
	 * @throws IOException If the image could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	public static Image createImage(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Parse
		return ImageReaderDispatcher.parse(new DataInputStream(__is));
	}
	
	/**
	 * This loads the specified resource as an image.
	 *
	 * @param __s The string to load the resource for.
	 * @throws IOException If the resource does not exist or the image data
	 * could not be decoded.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	public static Image createImage(String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Try loading it
		try (InputStream is = ActiveMidlet.get().getClass().
			getResourceAsStream(__s))
		{
			// {@squirreljme.error EB1x The specified resource does not
			// exist. (The resource name)}
			if (is == null)
				throw new IOException(String.format("EB1x %s", __s));
			
			return createImage(is);
		}
	}
	
	public static Image createImage(Image __a)
	{
		throw new todo.TODO();
	}
	
	public static Image createImage(Image __a, int __b, int __c, int __d, int
		__e, int __f)
	{
		throw new todo.TODO();
	}
	
	public static Image createImage(Image __i, int __x, int __y, int __w,
		int __h, int __trans, int __iw, int __ih)
	{
		throw new todo.TODO();
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
		
		// {@squirreljme.error EB1y Invalid image size. (The width;
		// The height)}
		int area = __w * __h;
		if (__w <= 0 || __h <= 0 || area <= 0)
			throw new IllegalArgumentException(String.format("EB1y %d %d",
				__w, __h));
		
		// {@squirreljme.error EB1z The input integer buffer is shorter than
		// the specified area.}
		int rgblen;
		if ((rgblen = __rgb.length) < area)
			throw new IndexOutOfBoundsException("EB1z");
		
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
}

