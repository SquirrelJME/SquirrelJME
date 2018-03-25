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
 * 32-bit ARGB 8888 Graphics.
 *
 * @since 2018/03/25
 */
public final class IntegerARGB8888ArrayGraphics
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
	public IntegerARGB8888ArrayGraphics(int[] __buf,
		int __w, int __h, int __p, int __o)
		throws IllegalArgumentException, NullPointerException
	{
		super(__w, __h, __p, __o, __buf.length, 1, true,
			0b11111111_00000000__00000000_00000000,
			0b00000000_11111111__00000000_00000000,
			0b00000000_00000000__11111111_00000000,
			0b00000000_00000000__00000000_11111111);
		
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		this.buffer = __buf;
	}
}

