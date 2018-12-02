// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * This class represents the drawing chain that must be done to draw every
 * single widget and such.
 *
 * @since 2018/11/18
 */
final class __DrawChain__
{
	/** Sub-links to get their chain drawn. */
	public final __VolatileList__<__Drawable__> links =
		new __VolatileList__<>();
	
	/** The X position. */
	public int x;
	
	/** The Y position. */
	public int y;
	
	/** The width. */
	public int w;
	
	/** The height. */
	public int h;
	
	/**
	 * Adds a link to the draw chain.
	 *
	 * @param __w The widget to draw.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/18
	 */
	public void addLink(__Drawable__ __w)
		throws NullPointerException
	{
		if (__w == null)
			throw new NullPointerException("NARG");
		
		this.links.append(__w);
	}
	
	/**
	 * Draws the children of this chain.
	 *
	 * @param __g The graphics to draw into.
	 * @since 2018/11/18
	 */
	public void drawChildren(Graphics __g)
	{
		for (__Drawable__ w : this.links)
			w.__drawChainWrapped(__g);
	}
	
	/**
	 * Resets the draw chain.
	 *
	 * @since 2018/11/18
	 */
	public void reset()
	{
		// Clear links
		this.links.clear();
		
		// Reset positions
		this.x = 0;
		this.y = 0;
		this.w = 0;
		this.h = 0;
	}
	
	/**
	 * Sets the chain to the given parameters.
	 *
	 * @param __x The X position.
	 * @param __y The Y postiion.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/11/18
	 */
	public final void set(int __x, int __y, int __w, int __h)
	{
		this.x = __x;
		this.y = __y;
		this.w = __w;
		this.h = __h;
	}
	
	/**
	 * Sets the chain to the given slice.
	 *
	 * @param __sl The slice to use.
	 * @since 2018/11/18
	 */
	public final void set(__DrawSlice__ __sl)
	{
		this.x = __sl.x;
		this.y = __sl.y;
		this.w = __sl.w;
		this.h = __sl.h;
	}
}

