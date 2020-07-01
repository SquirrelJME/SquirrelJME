// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.fbui;

import javax.microedition.lcdui.Displayable;

/**
 * This contains the state of a drawer.
 *
 * @since 2020/01/15
 */
@Deprecated
public abstract class UIDrawerState
{
	/** The displayable to be drawn/interacted with. */
	@Deprecated
	protected final Displayable displayable;
	
	/**
	 * Initializes the drawer state.
	 *
	 * @param __d The displayable to draw.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/01/18
	 */
	@Deprecated
	public UIDrawerState(Displayable __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		this.displayable = __d;
	}
	
	/**
	 * Returns the content height of this state.
	 *
	 * @return The content height.
	 * @since 2020/01/15
	 */
	@Deprecated
	public final int contentHeight()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the content width of this state.
	 *
	 * @return The content width.
	 * @since 2020/01/15
	 */
	@Deprecated
	public final int contentWidth()
	{
		throw new todo.TODO();
	}
}

