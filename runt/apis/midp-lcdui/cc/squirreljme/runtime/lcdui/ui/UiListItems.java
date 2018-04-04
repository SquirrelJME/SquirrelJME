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

/**
 * This manages a list of items that can be displayed within a list or a
 * choice group.
 *
 * @since 2018/04/04
 */
public final class UiListItems
{
	/**
	 * Adds the specified list listener which is used as a callback when the
	 * list has changed state.
	 *
	 * @param __l The listener to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public final void addListListener(UiListListener __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Removes the specified list listener so that it no longer is called
	 * when the list has been updated.
	 *
	 * @param __l The listener to remove.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public final void removeListListener(UiListListener __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

