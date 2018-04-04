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
 * This interface represents any UI element that has list items.
 *
 * @since 2018/04/04
 */
public interface UiHasListItems
	extends UiInterface
{
	/**
	 * Returns the associated list items.
	 *
	 * @return The list items.
	 * @since 2018/04/04
	 */
	public abstract UiListItems getListItems();
}

