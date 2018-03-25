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

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This class acts as the base for graphics drawers which are based on palettes
 * rather than a RGB color model.
 *
 * @since 2018/03/25
 */
public abstract class AbstractIndexedArrayGraphics
	extends AbstractArrayGraphics
{
	/** The palette data. */
	protected final int[] palette;
	
	/** The number of colors in the palette. */
	protected final int numcolors;
	
	/**
	 * Initializes the base graphics.
	 *
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __p The image pitch.
	 * @param __o The buffer offset.
	 * @param __l The length of the buffer.
	 * @param __pal The palette.
	 * @param __ppe Pixels per element.
	 * @param __numc The number of colors in the palette.
	 * @throws IllegalArgumentException If the palette is too small.
	 * @throws NullPointerException If no palette was specified.
	 * @since 2018/03/25
	 */
	public AbstractIndexedArrayGraphics(int __w, int __h, int __p, int __o,
		int __l, int __ppe, int[] __pal, int __numc)
		throws IllegalArgumentException, NullPointerException
	{
		super(__w, __h, __p, __o, __l, __ppe, false);
		
		if (__pal == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB2f The input palette does not have enough
		// entries to store color information.}
		if (__pal.length < __numc)
			throw new IllegalArgumentException("EB2f");
		
		throw new todo.TODO();
	}
}

