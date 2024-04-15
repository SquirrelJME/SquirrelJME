// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.IntegerArrayList;
import com.nttdocomo.ui.Palette;

/**
 * This contains the storage for an 8-bit image.
 *
 * @since 2024/01/14
 */
public final class EightBitImageStore
{
	/** The image width. */
	protected final int width;
	
	/** The image height. */
	protected final int height;
	
	/** Is there an alpha channel? */
	protected final boolean hasAlpha;
	
	/** Internal Image palette. */
	protected final Palette palette;
	
	/** The transparent index, if any. */
	protected final int transparentIndex;
	
	/** Image pixel data. */
	private final byte[] _pixels;
	
	/**
	 * Initializes the image store.
	 *
	 * @param __pixels The pixels to use, this is used directly.
	 * @param __width The image width.
	 * @param __height The image height.
	 * @param __palette The image palette.
	 * @param __hasAlpha Does this have an alpha channel?
	 * @param __transIndex The transparent color index.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/14
	 */
	EightBitImageStore(byte[] __pixels, int __width, int __height,
		int[] __palette, boolean __hasAlpha, int __transIndex)
		throws NullPointerException
	{
		if (__pixels == null || __palette == null)
			throw new NullPointerException("NARG");
		
		this._pixels = __pixels;
		this.palette = new Palette(__palette);
		this.width = __width;
		this.height = __height;
		this.hasAlpha = __hasAlpha;
		this.transparentIndex = (__transIndex >= __palette.length ? -1 :
			__transIndex);
	}
	
	/**
	 * Returns the height of the image.
	 *
	 * @return The image height.
	 * @since 2024/01/14
	 */
	public int getHeight()
	{
		return this.height;
	}
	
	/**
	 * Returns the image store's palette.
	 *
	 * @return The palette of the image store.
	 * @since 2024/01/14
	 */
	public Palette getPalette()
	{
		return this.palette;
	}
	
	/**
	 * Loads all the palette based image data into the given RGB buffer for
	 * drawing.
	 *
	 * @param __b The destination array.
	 * @param __o The offset into the array.
	 * @param __sl The scanline length of the destination array, this value may
	 * be negative to indicate that pixels are placed in reverse order.
	 * @param __palette The palette used on this image.
	 * @param __x The source X position.
	 * @param __y The source Y position.
	 * @param __w The width to copy, if this is zero nothing is copied.
	 * @param __h The height to copy, if this is zero nothing is copied.
	 * @param __transparentIndex The transparent color index, if any.
	 * @throws ArrayIndexOutOfBoundsException If writing to the destination
	 * buffer would result in a write that exceeds the bounds of the array.
	 * @throws IllegalArgumentException If the source X or Y position is
	 * negative; If the source region exceeds the image bounds; If the absolute
	 * value of the scanline length is lower than the width.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/14
	 */
	@Api
	public void getRGB(int[] __b, int __o, int __sl, Palette __palette,
		int __x, int __y, int __w, int __h, int __transparentIndex)
		throws IllegalArgumentException, NullPointerException,
			IndexOutOfBoundsException
	{
		if (__b == null || __palette == null)
			throw new NullPointerException("NARG");
		
		// How big of an area is being read?
		int area = __w * __h;
		
		// Size of the current store
		int imgW = this.width;
		int imgH = this.height;
		
		// Check bounds
		int[] buf = __b;
		int bufLen = buf.length;
		if (__o < 0 || __sl < 0 || __x < 0 || __y < 0 ||
			__w <= 0 || __h <= 0 || __x + __w > imgW || __y + __h > imgH)
			throw new IllegalArgumentException("IOOB");
		
		// Precache palette
		int[] palCache = new int[256];
		for (int i = 0, n = __palette.getEntryCount(); i < n; i++)
			palCache[i] = __palette.getEntry(i);
		
		// Set transparent color
		if (__transparentIndex < 0)
			__transparentIndex = this.transparentIndex;
		if (__transparentIndex >= 0 && __transparentIndex < 256)
			palCache[__transparentIndex] = 0x00_000000;
		
		// Translate pixels with palette colors
		byte[] pix = this._pixels;
		for (int outY = 0, outBS = __o, inBS = imgW * __y; 
			outY < __h; outY++, outBS += __sl, inBS += __w)
			for (int outX = 0, outS = outBS, inS = inBS;
				outX < __w; outX++, outS++, inS++)
			{
				__b[outS] = palCache[pix[inS] & 0xFF];
			}
	}
	
	/**
	 * Returns the transparent color index.
	 *
	 * @return The transparent color image or {@code -1} if not valid.
	 * @since 2024/01/15
	 */
	public int getTransparentIndex()
	{
		return this.transparentIndex;
	}
	
	/**
	 * Returns the width of the image.
	 *
	 * @return The image width.
	 * @since 2024/01/14
	 */
	public int getWidth()
	{
		return this.width;
	}
}
