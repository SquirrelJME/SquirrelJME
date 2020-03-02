// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.fbui;

import javax.microedition.lcdui.List;

/**
 * This is the drawing of a list.
 *
 * @since 2020/01/18
 */
public class DrawingList
	extends UIDrawerState
{
	/** The list to draw. */
	protected final List list;
	
	/**
	 * Initializes the drawer.
	 *
	 * @param __l The list to draw.
	 * @since 2020/01/18
	 */
	public DrawingList(List __l)
	{
		super(__l);
		
		this.list = __l;
	}
}

