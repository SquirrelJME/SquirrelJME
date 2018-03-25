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
	
	/** The alpha shift. */
	protected final int alphashift;
	
	/** The red mask. */
	protected final int redmask;
	
	/** The red shift. */
	protected final int redshift;
	
	/** The green mask. */
	protected final int greenmask;
	
	/** The green shift. */
	protected final int greenshift;
	
	/** The blue mask. */
	protected final int bluemask;
	
	/** The blue shift. */
	protected final int blueshift;
	
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
		int __l, int __ppe, boolean __alpha, int __amask, int __rmask,
		int __gmask, int __bmask)
		throws IllegalArgumentException, NullPointerException
	{
		super(__w, __h, __p, __o, __l, __ppe, __alpha);
		
		// Set shifts and masks
		this.alphamask = __amask;
		this.alphashift = Integer.numberOfTrailingZeros(__amask);
		this.redmask = __rmask;
		this.redshift = Integer.numberOfTrailingZeros(__rmask);
		this.greenmask = __gmask;
		this.greenshift = Integer.numberOfTrailingZeros(__gmask);
		this.bluemask = __bmask;
		this.blueshift = Integer.numberOfTrailingZeros(__bmask);
	}
}

