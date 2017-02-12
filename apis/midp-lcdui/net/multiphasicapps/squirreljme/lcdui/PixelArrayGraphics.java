// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

/**
 * This is a graphics handler which.
 *
 * @since 2017/02/12
 */
public class PixelArrayGraphics
	extends BasicGraphics
{
	/** The RGB image data. */
	private final int[] _data;
	
	/** Image width. */
	private final int _width;
	
	/** Image height. */
	private final int _height;
	
	/** Does this have an alpha channel? */
	private final boolean _alpha;
	
	/**
	 * Initializes the graphics drawer which draw into the given integer
	 * array.
	 *
	 * @param __data The buffer to draw into.
	 * @param __width The width of the image.
	 * @param __height The height of the image.
	 * @param __alpha If {@code true} then an alpha channel is used.
	 * @throws ArrayIndexOutOfBoundsException If 
	 * @throws IllegalArgumentException If the width or height is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/12
	 */
	public PixelArrayGraphics(int[] __data, int __width, int __height,
		boolean __alpha)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB0p Invalid width and/or height specified.}
		if (__width <= 0 || __height <= 0)
			throw new IllegalArgumentException("EB0p");
		
		// Set
		this._data = __data;
		this._width = __width;
		this._height = __height;
		this._alpha = __alpha;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	protected boolean primitiveHasAlphaChannel()
	{
		return this._alpha;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/11
	 */
	@Override
	protected int primitiveImageHeight()
	{
		return this._height;
	}

	/**
	 * {@inheritDoc}
	 * @since 2017/02/11
	 */
	@Override
	protected int primitiveImageWidth()
	{
		return this._width;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/11
	 */
	@Override
	protected void primitiveRGBSlice(int[] __b, int __o, int __l,
		int __x, int __y, boolean __blend)
	{
		int[] data = this._data;
		int iw = this._width;
		
		// Calculate destination
		int dest = (__y * iw) + __x;
		int end = __o + __l;
		
		// Blending?
		if (__blend)
			for (int src = __o; src < end; src++)
			{
				// Get both src and dest colors
				int sc = __b[src],
					dc = data[dest];
				
				// Split into RGB
				int sa = (sc >> 24) & 0xFF,
					sr = (sc >> 16) & 0xFF,
					sg = (sc >> 8) & 0xFF,
					sb = (sc) & 0xFF,
					da = (dc >> 24) & 0xFF,
					dr = (dc >> 16) & 0xFF,
					dg = (dc >> 8) & 0xFF,
					db = (dc) & 0xFF;
				
				// Perform blending
				int qq = 255 - sa;
				int xa = sa + da - ((sa * da) / 255),
					xr = ((sr * sa) / 255) + ((dr * qq) / 255),
					xg = ((sg * sa) / 255) + ((dg * qq) / 255),
					xb = ((sb * sa) / 255) + ((db * qq) / 255);
				
				// Recompile
				int xc = (xa << 24) | (xr << 16) | (xg << 8) | xb;
				data[dest++] = xc;
			}
		
		// Not blending
		else
			for (int src = __o; src < end; src++)
				data[dest++] = __b[src] | 0xFF000000;
	}
}

