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
	 * @throws NullPointerException
	 * @since 2024/01/14
	 */
	EightBitImageStore(byte[] __pixels, int __width, int __height,
		int[] __palette, boolean __hasAlpha)
		throws NullPointerException
	{
		if (__pixels == null || __palette == null)
			throw new NullPointerException("NARG");
		
		this._pixels = __pixels;
		this.palette = new Palette(__palette);
		this.width = __width;
		this.height = __height;
		this.hasAlpha = __hasAlpha;
	}
	
	/**
	 * Returns the height of the image.
	 *
	 * @return The image height.
	 * @since 2024/01/14
	 */
	public int getHeight()
	{
		throw Debugging.todo();
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
		int __x, int __y, int __w, int __h)
		throws IllegalArgumentException, NullPointerException,
			IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the width of the image.
	 *
	 * @return The image width.
	 * @since 2024/01/14
	 */
	public int getWidth()
	{
		throw Debugging.todo();
	}
}
