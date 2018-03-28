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
	
	/** The scores for quick color lookup. */
	protected final int[] scores;
	
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
	 * @param __atx Absolute X translation.
	 * @param __aty Absolute Y translation.
	 * @param __numc The number of colors in the palette.
	 * @throws IllegalArgumentException If the palette is too small.
	 * @throws NullPointerException If no palette was specified.
	 * @since 2018/03/25
	 */
	public AbstractIndexedArrayGraphics(int __w, int __h, int __p, int __o,
		int __l, int __ppe, int __atx, int __aty, int[] __pal, int __numc)
		throws IllegalArgumentException, NullPointerException
	{
		super(__w, __h, __p, __o, __l, __ppe, false, __atx, __aty);
		
		if (__pal == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB2f The input palette does not have enough
		// entries to store color information.}
		if (__pal.length < __numc)
			throw new IllegalArgumentException("EB2f");
		
		// Score all colors in the palette for faster lookup potentially
		int[] scores = new int[__numc];
		for (int i = 0; i < __numc; i++)
			scores[i] = scoreColor(__pal[i]);
		
		this.palette = __pal;
		this.scores = scores;
		this.numcolors = __numc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/25
	 */
	@Override
	protected final void internalSetColor(int __a, int __rgb, boolean __blend)
	{
		throw new todo.TODO();
	}
	
	/**
	 * This creates a score for the given color which is used to approximate
	 * and quickly for finding a color in a palette.
	 *
	 * @param __rgb The input RGB color.
	 * @since 2018/03/25
	 */
	public static final int scoreColor(int __rgb)
	{
		// Mask out alpha always
		__rgb &= 0x00FFFFFF;
		
		// Average the three color channels and set them as the high priority
		// bits so that the average of the color is chosen first
		// Then mix in an average of the red/green and green/blue components
		// The lowest 8-bits is set to red so that similar red/green matches
		// better
		int red = (__rgb >>> 16);
		return (((((red + ((__rgb >>> 16) & 0xFF) + (__rgb & 0xFF))
			* 341) >> 10) << 24) |
			((((__rgb >>> 8) + (__rgb & 0xFFFF))) >>> 1) << 8) |
			red;
	}
}

