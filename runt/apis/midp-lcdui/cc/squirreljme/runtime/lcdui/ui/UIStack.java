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
	
	/** The X position from the parent. */
	public int xoffset;
	
	/** The Y position from the parent. */
	public int yoffset;
	
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
	}
}

