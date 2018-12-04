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
 * 32-bit RGB 888 Graphics.
 *
 * @since 2018/03/25
 */
public final class IntegerRGB888ArrayGraphics
	extends AbstractRGBArrayGraphics
{
	/** The array buffer. */
	protected final int[] buffer;
	
	/**
	 * Initializes the graphics.
	 *
	 * @param __buf The buffer.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __p The image pitch.
	 * @param __o The buffer offset.
	 * @param __atx Absolute X translation.
	 * @param __aty Absolute Y translation.
	 * @throws IllegalArgumentException If the input parameters are not
	 * correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/25
	 */
	public IntegerRGB888ArrayGraphics(int[] __buf,
		int __w, int __h, int __p, int __o, int __atx, int __aty)
		throws IllegalArgumentException, NullPointerException
	{
		super(__w, __h, __p, __o, __buf.length, 1, false, __atx, __aty,
			0b00000000_00000000__00000000_00000000,
			0b00000000_11111111__00000000_00000000,
			0b00000000_00000000__11111111_00000000,
			0b00000000_00000000__00000000_11111111);
		
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		this.buffer = __buf;
		
		// Use default settings
		this.resetParameters(true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01 
	 */
	@Override
	protected final void internalDrawCharBitmap(boolean __blend, int __color,
		int __dsx, int __dsy, byte[] __bmp, int __bytesperscan,
		int __scanoff, int __scanlen, int __lineoff, int __linelen)
	{
		int[] data = this.buffer;
		int offset = this.offset,
			pitch = this.pitch;
		
		// Treat lens as end indexes
		int origscanlen = __scanlen;
		__scanlen += __scanoff;
		__linelen += __lineoff;
		
		// Remember the old scan offset, because we reset
		int resetscanoff = __scanoff;
		
		// Determine the draw pointer for this line
		int basep = offset + (__dsy * pitch) + __dsx;
		
		// Base source offset line according to the line offset
		int bi = __lineoff * __bytesperscan;
		
		// Blending?
		if (__blend)
		{
			throw new todo.TODO();
		}
		
		// Not blending
		else
		{
			// Drew each line
			for (; __lineoff < __linelen; __lineoff++, __dsy++)
			{
				// Reset parameters for this line
				int p = basep;
				__scanoff = resetscanoff;
				
				// Draw each scan from the bitmap
				for (; __scanoff < __scanlen; __scanoff++, p++)
				{
					// Get the byte that represents the scan here
					byte b = __bmp[bi + (__scanoff >>> 3)];	
					
					// If there is a pixel here, draw it
					if ((b & (1 << (__scanoff & 0x7))) != 0)
						data[p] = __color;
				}
				
				// Move the source and dest pointers to the next line
				basep += pitch;
				bi += __bytesperscan;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	protected final void internalDrawLine(int __x1, int __y1,
		int __x2, int __y2)
	{
		int[] data = this.buffer;
		int iw = this.pitch,
			dx = __x2 - __x1;
		
		int dy = __y2 - __y1;
		boolean neg;
		if ((neg = dy < 0))
			dy = -dy;	
		
		int sy = (__y1 < __y2 ? 1 : -1),
			ssy = iw * sy,
			err = (dx > dy ? dx : -dy) >> 2,
			color = this.paintcolor,
			dest = this.offset + (iw * __y1) + __x1;
		
		for (;;)
		{
			// End, it ends when the X coordinate exceeds the end because there
			// is nothing to draw again
			// Y ends after it reaches the bound
			//if (((__x1 ^ __x2) | (__y1 ^ __y2)) == 0)
			if (__x1 >= __x2 &&
				((neg && __y1 <= __y2) || (!neg && __y1 >= __y2)))
				break;
			
			data[dest] = color;
			
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	protected final void internalDrawLineBlended(int __x, int __y,
		int __ex, int __ey)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	protected final void internalDrawLineDotted(int __x, int __y,
		int __ex, int __ey)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	protected final void internalDrawLineBlendedDotted(int __x, int __y,
		int __ex, int __ey)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/25
	 */
	@Override
	protected final void internalFillRectBlend(int __x, int __y, int __ex,
		int __ey, int __w, int __h)
	{
		int[] buffer = this.buffer;
		
		int pac = this.paintcolor,
			sa = this.paintalpha,
			na = (sa ^ 0xFF),
			srb = ((pac & 0xFF00FF) * sa),
			sgg = (((pac >>> 8) & 0xFF) * sa);
		
		// Blend each color
		int mod = 0;
		boolean did = false;
		for (int y = __y; y < __ey; y++)
			for (int dest = offset + (y * pitch) + __x, pex = dest + __w;
				dest < pex; dest++)
			{
				int dcc = buffer[dest],
					xrb = (srb + ((dcc & 0xFF00FF) * na)) >>> 8,
					xgg = (((sgg + (((dcc >>> 8) & 0xFF) * na)) + 1) * 257)
						>>> 16;
				
				buffer[dest] = ((xrb & 0xFF00FF) | ((xgg & 0xFF) << 8));
			}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/25
	 */
	@Override
	protected final void internalFillRectSolid(int __x, int __y, int __ex,
		int __ey, int __w, int __h)
	{
		int b = this.paintcolor;
		int[] buffer = this.buffer;
		int pitch = this.pitch,
			offset = this.offset;
		
		// Just a simple color fill
		for (int y = __y; y < __ey; y++)
			for (int dest = offset + (y * pitch) + __x, pex = dest + __w;
				dest < pex; dest++)
				buffer[dest] = b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	protected final void internalRGBTile(int[] __b, int __o, int __l,
		int __x, int __y, int __w, int __h)
	{
		int[] data = this.buffer;
		int iw = this.pitch;
		
		// The distance from the end of a row to the scanline, this way the
		// source variable does not need an extra copy
		int eosa = __l - __w;
		
		// Draw tile data
		int dest = this.offset + (__y * iw) + __x, src = 0, ey = __y + __h;
		for (; __y < ey; __y++, dest += iw, src += eosa)
			for (int spend = src + __w, dp = dest; src < spend;
				dp++, src++)
				data[dp] = __b[src];
	}
}

