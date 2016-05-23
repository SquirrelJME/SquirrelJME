// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

/**
 * This represents a menu item inside of a menu, it has a label and an assigned
 * label.
 *
 * @since 2016/05/23
 */
public final class UIMenuItem
	extends UIElement
{
	/** The menu which contains this item. */
	private volatile UIMenu _parent;
	
	/**
	 * Initializes the menu item.
	 *
	 * @param __dm The display manager owning this.
	 * @since 2016/05/23
	 */
	UIMenuItem(UIDisplayManager __dm)
	{
		super(__dm);
	}
}

