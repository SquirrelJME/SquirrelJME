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
 * This class manages the items used in the form and keeps track of which
 * items are within the form or not.
 *
 * @since 2018/04/04
 */
public final class UiFormItems
{
	/**
	 * Adds the specified item to the form.
	 *
	 * @param __i The item to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public final void addItem(UiFormItem __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns all of the items which are added to this form.
	 *
	 * @return All of the items that were added to this form.
	 * @since 2018/04/04
	 */
	public final UiFormItem[] getItems()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Removes the specified item from the form.
	 *
	 * @param __i The item to remove.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public final void removeItem(UiFormItem __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

