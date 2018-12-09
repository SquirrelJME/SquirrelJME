// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

import javax.microedition.lcdui.Graphics;
import java.util.LinkedList;
import java.util.List;

/**
 * This represents a stack drawing area which continues downwards as it is
 * needed for the child elements.
 *
 * @since 2018/12/08
 */
public final class UIStack
{
	/** The reserved width. */
	public final int reservedwidth;
	
	/** The reserved height. */
	public final int reservedheight;
	
	/** Children stacks. */
	public final List<UIStack> kids =
		new LinkedList<>();
	
	/** The X position from the parent. */
	public int xoffset;
	
	/** The Y position from the parent. */
	public int yoffset;
	
	/** The total drawing width. */
	public int drawwidth;
	
	/** The total drawing height. */
	public int drawheight;
	
	/**
	 * Initializes the stack with the given view width and height.
	 *
	 * @param __w Reserved width.
	 * @param __h Reserved height.
	 * @since 2018/12/08
	 */
	public UIStack(int __w, int __h)
	{
		this.reservedwidth = __w;
		this.reservedheight = __h;
		
		// Draw width defaults to the reserved width
		this.drawwidth = __w;
	}
	
	/**
	 * Adds the item to the draw stack.
	 *
	 * @param __s The stack to add.
	 * @since 2018/12/08
	 */
	public final void add(UIStack __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Get the drawing height because we will stack our height on top of
		// that!
		int drawheight = this.drawheight,
			subresheight = __s.reservedheight,
			newdrawheight = drawheight + subresheight;
		
		// Set position of the child relative to our own offset, stack
		// drawable going down more
		__s.xoffset = this.xoffset + 0;
		__s.yoffset = this.yoffset + drawheight;
		
		// If the child never set a drawheight, set it for them
		if (__s.drawheight == 0)
			__s.drawheight = subresheight;
		
		// For next run or for drawing
		this.drawheight = newdrawheight;
	}
}

