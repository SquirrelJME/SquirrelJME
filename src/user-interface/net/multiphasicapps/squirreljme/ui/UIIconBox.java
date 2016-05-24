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
 * This is an icon box which is similar to a list except that all items are
 * mostly icons and are associated with text. This is used for example to
 * graphically select a program or otherwise for launching.
 *
 * This internally may use a list to display elements if the platform does
 * not implement an icon box.
 *
 * @since 2016/05/24
 */
public class UIIconBox
	extends UIComponent
{
	/**
	 * Initializes the icon box.
	 *
	 * @param __dm The owning display manager.
	 * @since 2016/05/24
	 */
	public UIIconBox(UIManager __dm)
	{
		super(__dm);
	}
}

