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
 * 8-bit RGB Graphics.
 *
 * @since 2018/03/25
 */
public final class ByteRGB332ArrayGraphics
	extends AbstractRGBArrayGraphics
{
	/** The array buffer. */
	protected final byte[] buffer;
	
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
	public ByteRGB332ArrayGraphics(byte[] __buf,
		int __w, int __h, int __p, int __o, int __atx, int __aty)
		throws IllegalArgumentException, NullPointerException
	{
		super(__w, __h, __p, __o, __buf.length, 1, false, __atx, __aty,
			0b0000_0000,
			0b1110_0000,
			0b0001_1100,
			0b0000_0011);
		
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/25
	 */
	@Override
	protected final void internalFillRectSolid(int __x, int __y, int __ex,
		int __ey, int __w, int __h)
	{
		throw new todo.TODO();
	}
}

