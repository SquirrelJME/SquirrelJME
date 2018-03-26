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
	 * @throws IllegalArgumentException If the input parameters are not
	 * correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/25
	 */
	public IntegerRGB888ArrayGraphics(int[] __buf,
		int __w, int __h, int __p, int __o)
		throws IllegalArgumentException, NullPointerException
	{
		super(__w, __h, __p, __o, __buf.length, 1, false,
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
}

