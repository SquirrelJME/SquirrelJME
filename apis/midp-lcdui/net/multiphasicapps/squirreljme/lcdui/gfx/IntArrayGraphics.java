// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.gfx;

/**
 * This is used to draw graphics to surfaces which are backed by an integer
 * array.
 *
 * @since 2017/10/26
 */
public class IntArrayGraphics
	extends PixelArrayGraphics
{
	/** The RGB image data. */
	private final int[] _data;
	
	/**
	 * Initializes the graphics drawer which draw into the given integer
	 * array.
	 *
	 * @param __data The buffer to draw into.
	 * @param __width The width of the image.
	 * @param __height The height of the image.
	 * @param __alpha If {@code true} then an alpha channel is used.
	 * @param __pitch The image pitch.
	 * @param __offset The data buffer offset.
	 * @throws ArrayIndexOutOfBoundsException If the image dimensions exceeds
	 * the array bounds.
	 * @throws IllegalArgumentException If the width or height is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/26
	 */
	public IntArrayGraphics(int[] __data, int __width, int __height,
		boolean __alpha, int __pitch, int __offset)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		super(__data, __width, __height, __alpha, __pitch, __offset);
		
		// This just needs to be set.
		this._data = __data;
	}
}

