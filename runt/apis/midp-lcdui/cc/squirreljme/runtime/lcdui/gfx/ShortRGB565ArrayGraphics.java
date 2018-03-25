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
 * 16-bit RGB 565 Graphics.
 *
 * @since 2018/03/25
 */
public final class ShortRGB565ArrayGraphics
	extends AbstractRGBArrayGraphics
{
	/** The array buffer. */
	protected final short[] buffer;
	
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
	public ShortRGB565ArrayGraphics(short[] __buf,
		int __w, int __h, int __p, int __o)
		throws IllegalArgumentException, NullPointerException
	{
		super(__w, __h, __p, __o, __buf.length, 1, false,
			0b0000_0000__0000_0000,
			0b1111_1000__0000_0000,
			0b0000_0111__1110_0000,
			0b0000_0000__0001_1111);
		
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		this.buffer = __buf;
	}
}

