// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.util;

/**
 * A glyph bitmap which is mutable.
 *
 * @since 2024/06/08
 */
public final class MutableGlyphBitmap
{
	/** The bitmap data. */
	public final byte[] bitmap;
	
	/** The bytes per row. */
	public final int bytesPerRow;
	
	/** The bitmap width. */
	public final int width;
	
	/** The bitmap height. */
	public final int height;
	
	/**
	 * Initializes the mutable bitmap.
	 *
	 * @param __w The bitmap width.
	 * @param __h The bitmap height.
	 * @since 2024/06/08
	 */
	public MutableGlyphBitmap(int __w, int __h)
	{
		// Calculate scanline length of bitmap
		int bytesPerRow = (__w / 8) + ((__w % 8) != 0 ? 1 : 0); 
		this.bytesPerRow = bytesPerRow;
		this.width = __w;
		this.height = __h;
		
		// Allocate target bitmap
		this.bitmap = new byte[bytesPerRow * __h];
	}
	
	/**
	 * Draws into the bitmap.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @since 2024/06/08
	 */
	public void draw(int __x, int __y)
	{
		int w = this.width;
		int h = this.height;
		
		// Set bit
		this.bitmap[GlyphBitmap.calcIndex(__x, __y, w, h)] |=
			(byte)(1 << GlyphBitmap.calcShift(__x, __y, w, h));
	}
}
