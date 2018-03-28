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
 * This class acts as the base for RGB based graphics.
 *
 * @since 2018/03/25
 */
public abstract class AbstractRGBArrayGraphics
	extends AbstractArrayGraphics
{
	/** The alpha mask. */
	protected final int alphamask;
	
	/** The alpha value mask. */
	protected final int alphavalmask;
	
	/** The alpha shift. */
	protected final int alphashift;
	
	/** The red mask. */
	protected final int redmask;
	
	/** The red shift. */
	protected final int redshift;
	
	/** The red value mask. */
	protected final int redvalmask;
	
	/** Downshift for red. */
	protected final int reddownshift;
	
	/** The green mask. */
	protected final int greenmask;
	
	/** The green shift. */
	protected final int greenshift;
	
	/** The green value mask. */
	protected final int greenvalmask;
	
	/** Downshift for green. */
	protected final int greendownshift;
	
	/** The blue mask. */
	protected final int bluemask;
	
	/** The blue shift. */
	protected final int blueshift;
	
	/** The blue value mask. */
	protected final int bluevalmask;
	
	/** Downshift for blue. */
	protected final int bluedownshift;
	
	/** The painting alpha color. */
	protected int paintalpha;
	
	/** The color to use for painting. */
	protected int paintcolor;
	
	/** Paint color with the alpha channel set to the max. */
	protected int paintcolorhigh;
	
	/** The alpha color and normal color for painting. */
	protected int paintalphacolor;
	
	/**
	 * Initializes the base graphics.
	 *
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __p The image pitch.
	 * @param __o The buffer offset.
	 * @param __l The length of the buffer.
	 * @param __ppe The number of pixels per element.
	 * @param __alpha Is there an alpha specified?
	 * @param __atx Absolute X translation.
	 * @param __aty Absolute Y translation.
	 * @param __amask Alpha mask.
	 * @param __rmask Red mask.
	 * @param __gmask Green mask.
	 * @param __bmask Blue mask.
	 * @throws IllegalArgumentException If the input parameters are not
	 * correct.
	 * @throws NullPointerException If no palette was specified.
	 * @since 2018/03/25
	 */
	public AbstractRGBArrayGraphics(int __w, int __h, int __p, int __o,
		int __l, int __ppe, boolean __alpha, int __amask, int __atx, int __aty,
		int __rmask, int __gmask, int __bmask)
		throws IllegalArgumentException, NullPointerException
	{
		super(__w, __h, __p, __o, __l, __ppe, __alpha, __atx, __aty);
		
		// The alpha mask is only valid if there is an alpha channel
		if (__alpha)
		{
			int alphashift = Integer.numberOfTrailingZeros(__amask);
			
			this.alphamask = __amask;
			this.alphashift = alphashift;
			this.alphavalmask = __amask >>> alphashift;
		}
		else
		{
			this.alphamask = 0;
			this.alphashift = 0;
			this.alphavalmask = 0;
		}
		
		int redshift = Integer.numberOfTrailingZeros(__rmask),
			greenshift = Integer.numberOfTrailingZeros(__gmask),
			blueshift = Integer.numberOfTrailingZeros(__bmask);
		
		// Red
		this.redmask = __rmask;
		this.redshift = redshift;
		this.redvalmask = __rmask >>> redshift;
		this.reddownshift = 16 - redshift;
		
		// Green
		this.greenmask = __gmask;
		this.greenshift = greenshift;
		this.greenvalmask = __gmask >>> greenshift;
		this.greendownshift = 8 - greenshift;
		
		// Blue
		this.bluemask = __bmask;
		this.blueshift = blueshift;
		this.bluevalmask = __bmask >>> blueshift;
		this.bluedownshift = 0 - blueshift;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/25
	 */
	@Override
	protected final void internalSetColor(int __a, int __rgb, boolean __blend)
	{
		// Set plain alpha color
		this.paintalpha = __a;
		
		// Shift the component arround correctly
		int paintcolor = ((__rgb >>> this.reddownshift) & this.redmask) |
				((__rgb >>> this.greendownshift) & this.greenmask) |
				((__rgb >>> this.bluedownshift) & this.bluemask),
			paintalphacolor = paintcolor |
				((__a & this.alphavalmask) << this.alphashift);
		this.paintcolor = paintcolor;
		this.paintcolorhigh = paintcolor | this.alphamask;
		this.paintalphacolor = paintalphacolor;
	}
}

