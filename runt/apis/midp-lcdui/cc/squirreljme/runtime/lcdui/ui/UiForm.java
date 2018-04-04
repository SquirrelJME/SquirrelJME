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
 * This represents a form which will consist of various input items as they
 * are used and needed.
 *
 * @since 2018/04/04
 */
public interface UiForm
	extends UiDisplayable, UiTabbedDisplayable
{
	/**
	 * Adds the specified item to the form.
	 *
	 * @param __i The item to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public abstract void addItem(UiFormItem __i)
		throws NullPointerException;
	
	/**
	 * Returns all of the items which are added to this form.
	 *
	 * @return All of the items that were added to this form.
	 * @since 2018/04/04
	 */
	public abstract UiFormItem[] getItems();
	
	/**
	 * Removes the specified item from the form.
	 *
	 * @param __i The item to remove.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/04
	 */
	public abstract void removeItem(UiFormItem __i)
		throws NullPointerException;
}

