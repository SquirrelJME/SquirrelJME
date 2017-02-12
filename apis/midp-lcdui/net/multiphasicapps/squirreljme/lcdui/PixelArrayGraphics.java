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
	 * @since 2017/02/12
	 */
	@Override
	protected void primitiveHorizontalLine(int __x, int __y,
		int __w, int __color, boolean __dotted, boolean __blend)
	{
		int[] data = this._data;
		int iw = this._width;
		
		// Draw line
		int skip = (__dotted ? 2 : 1);
		for (int dest = (__y * iw) + __x, ex = dest + __w; dest < ex;
			dest += skip)
			if (__blend)
				data[dest] = __blend(__color, data[dest]);
			else
				data[dest] = __color;
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
	protected void primitiveRGBTile(int[] __b, int __o, int __sl,
		int __x, int __y, int __w, int __h, boolean __blend)
	{
		int[] data = this._data;
		int iw = this._width;
		
		// Draw loop
		for (int dest = (__y * iw) + __x, src = 0, ey = __y + __h;
			__y < ey; __y++, dest += iw, src += __sl)
		{
			// Calculate the end of the scanlines
			int sp = src,
				spend = src + __w,
				dp = dest;
			
			// Blending pixels
			if (__blend)
				for (; sp < spend; sp++, dp++)
				{
					// Quickly check to see if no math needs to be done
					int sc = __b[sp];
					int sa = (sc >> 24) & 0xFF;
					if (sa == 0x00)
						continue;
				
					// If the source is fully opaque it will always replace the
					// destination no matter what
					else if (sa == 0xFF)
						data[dp] = sc;
				
					// Otherwise blend
					else
						data[dp] = __blend(sc, data[dp]);
				}
			
			// Not blending
			else
				for (; sp < spend; sp++, dp++)
					data[dp] = __b[sp];
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	protected void primitiveVerticalLine(int __x, int __y,
		int __h, int __color, boolean __dotted, boolean __blend)
	{
		int[] data = this._data;
		int iw = this._width;
		
		// Draw line
		int skip = (__dotted ? iw << 1 : iw);
		for (int dest = (__y * iw) + __x, ey = dest + (iw * __h); dest < ey;
			dest += skip)
			if (__blend)
				data[dest] = __blend(__color, data[dest]);
			else
				data[dest] = __color;
	}
	
	/**
	 * Blends two colors.
	 *
	 * @param __src The source color.
	 * @param __dest The destination color.
	 * @return The resulting blended color.
	 * @since 2017/02/12
	 */
	private static final int __blend(int __src, int __dest)
	{
		// Split into RGB
		int sa = (__src >> 24) & 0xFF,
			sr = (__src >> 16) & 0xFF,
			sg = (__src >> 8) & 0xFF,
			sb = (__src) & 0xFF,
			da = (__dest >> 24) & 0xFF,
			dr = (__dest >> 16) & 0xFF,
			dg = (__dest >> 8) & 0xFF,
			db = (__dest) & 0xFF;
	
		// Difference of alpha values
		// This value was 255, however since shift right by 8 is 256, this
		// may result in a slow and gradual loss of color
		int qq = 256 - sa;
		
		// Perform blending
		// The right shifts by 8 used to be divides by 255, however right
		// shifting 7 times is faster than dividing 7 times
		int xa = sa + da - ((sa * da) >>> 8),
			xr = ((sr * sa) >>> 8) + ((dr * qq) >>> 8),
			xg = ((sg * sa) >>> 8) + ((dg * qq) >>> 8),
			xb = ((sb * sa) >>> 8) + ((db * qq) >>> 8);
	
		// Recompile
		return (xa << 24) | (xr << 16) | (xg << 8) | xb;
	}
}

