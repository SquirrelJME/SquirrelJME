// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

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
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	protected void primitiveHorizontalLine(int __x, int __y,
		int __w, int __color, boolean __dotted, boolean __blend, int __bor)
	{
		int[] data = this._data;
		int iw = this.width,
			dest = this.offset + (__y * iw) + __x,
			ex = dest + __w;
		
		// Blended
		if (__blend)
		{
			int alpha = (__color >> 24) & 0xFF;
			
			// Dotted
			if (__dotted)
				for (; dest < ex; dest += 2)
					data[dest] = __blend(__color, data[dest], __bor, alpha);
			
			// Not dotted
			else
				for (; dest < ex; dest++)
					data[dest] = __blend(__color, data[dest], __bor, alpha);
		}
		
		// Not blended
		else
		{
			// Dotted
			if (__dotted)
				for (; dest < ex; dest += 2)
					data[dest] = __color;
			
			// Not dotted
			else
				for (; dest < ex; dest++)
					data[dest] = __color;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	protected void primitiveLine(int __x1, int __y1, int __x2,
		int __y2, int __color, boolean __dotted, boolean __blend, int __bor)
	{
		int[] data = this._data;
		int iw = this.width,
			dx = __x2 - __x1,
			dy = Math.abs(__y2 - __y1),
			sy = (__y1 < __y2 ? 1 : -1),
			ssy = iw * sy,
			err = (dx > dy ? dx : -dy) / 2,
			alpha = (__color >> 24) & 0xFF,
			dest = this.offset + (iw * __y1) + __x1;
		
		// Blended?
		if (__blend)
		{
			// Dotted
			if (__dotted)
				for (boolean dot = true;; dot = !dot)
				{
					// Draw the dot?
					if (dot)
						data[dest] = __blend(__color, data[dest], __bor,
							alpha);
					
					// End
					if (__x1 == __x2 && __y1 == __y2)
						break;
					
					// Increase X
					int brr = err;
					if (brr > -dx)
					{
						err -= dy;
						__x1++;
						dest++;
					}
			
					// Increase Y
					if (brr < dy)
					{
						err += dx;
						__y1 += sy;
						dest += ssy;
					}
				}
			
			// Not dotted
			else
				for (;;)
				{
					data[dest] = __blend(__color, data[dest], __bor, alpha);
					
					// End
					if (__x1 == __x2 && __y1 == __y2)
						break;
					
					// Increase X
					int brr = err;
					if (brr > -dx)
					{
						err -= dy;
						__x1++;
						dest++;
					}
			
					// Increase Y
					if (brr < dy)
					{
						err += dx;
						__y1 += sy;
						dest += ssy;
					}
				}
		}
		
		// Not blended
		else
		{
			// Dotted
			if (__dotted)
				for (boolean dot = true;; dot = !dot)
				{
					// Draw the dot?
					if (dot)
						data[dest] = __color;
					
					// End
					if (__x1 == __x2 && __y1 == __y2)
						break;
					
					// Increase X
					int brr = err;
					if (brr > -dx)
					{
						err -= dy;
						__x1++;
						dest++;
					}
			
					// Increase Y
					if (brr < dy)
					{
						err += dx;
						__y1 += sy;
						dest += ssy;
					}
				}
			
			// Not dotted
			else
				for (;;)
				{
					data[dest] = __color;
					
					// End
					if (__x1 == __x2 && __y1 == __y2)
						break;
					
					// Increase X
					int brr = err;
					if (brr > -dx)
					{
						err -= dy;
						__x1++;
						dest++;
					}
			
					// Increase Y
					if (brr < dy)
					{
						err += dx;
						__y1 += sy;
						dest += ssy;
					}
				}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/11
	 */
	@Override
	protected void primitiveRGBTile(int[] __b, int __o, int __sl,
		int __x, int __y, int __w, int __h, boolean __blend, int __bor,
		int __alpha)
	{
		int[] data = this._data;
		int iw = this.width;
		
		// The distance from the end of a row to the scanline, this way the
		// source variable does not need an extra copy
		int eosa = __sl - __w;
		
		// Blending?
		int dest = this.offset + (__y * iw) + __x, src = 0, ey = __y + __h;
		if (__blend)
			for (; __y < ey; __y++, dest += iw, src += eosa)
				for (int spend = src + __w, dp = dest; src < spend;
					dp++, src++)
					data[dp] = __blend(__b[src], data[dp], __bor, __alpha);
		
		// Not blending
		else
			for (; __y < ey; __y++, dest += iw, src += eosa)
				for (int spend = src + __w, dp = dest; src < spend;
					dp++, src++)
					data[dp] = __b[src];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	protected void primitiveVerticalLine(int __x, int __y,
		int __h, int __color, boolean __dotted, boolean __blend, int __bor)
	{
		int[] data = this._data;
		int iw = this.width,
			dest = this.offset + (__y * iw) + __x,
			ey = dest + (iw * __h);
		
		// Draw line
		int skip = (__dotted ? iw << 1 : iw);
		int alpha = (__color >> 24) & 0xFF;
		
		// Blended?
		if (__blend)
			for (; dest < ey; dest += skip)
				data[dest] = __blend(__color, data[dest], __bor, alpha);
		
		// Not blended
		else
			for (; dest < ey; dest += skip)
				data[dest] = __color;
	}
	
	/**
	 * Blends two colors.
	 *
	 * @param __src The source color, this includes the alpha channel so if a
	 * fully opaque color which should then become transparent is drawn this
	 * should be set to {@code 255}.
	 * @param __dest The destination color.
	 * @param __bor The blended OR value on the destination.
	 * @param __alpha Alpha value which modifies the source.
	 * @return The resulting blended color.
	 * @since 2017/02/12
	 */
	private static final int __blend(int __src, int __dest, int __bor,
		int __alpha)
	{
		// Make sure the source alpha value gets multiplied
		// However if the source is invisible then return the destination
		int sa = (((__src >> 24) & 0xFF) * __alpha) / 255;
		if (sa == 0)
			return __dest;
		
		// If the source is fully opaque then return the source
		else if (sa == 0xFF)
			return __src;
		
		// Split into RGB
		int sr = (__src >> 16) & 0xFF,
			sg = (__src >> 8) & 0xFF,
			sb = (__src) & 0xFF,
			da = ((__dest >> 24) & 0xFF) | __bor,
			dr = (__dest >> 16) & 0xFF,
			dg = (__dest >> 8) & 0xFF,
			db = (__dest) & 0xFF;
	
		// Difference of alpha values
		int qq = 255 - sa;
		
		// Perform blending
		int xa = (sa + da - ((sa * da) / 255)) | __bor,
			xr = ((sr * sa) + (dr * qq)) / 255,
			xg = ((sg * sa) + (dg * qq)) / 255,
			xb = ((sb * sa) + (db * qq)) / 255;
	
		// Recompile
		return (xa << 24) | (xr << 16) | (xg << 8) | xb;
	}
}

