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
 * This represents a component which is the base for all widgets that are
 * used inside of a display.
 *
 * @since 2016/05/22
 */
public class UIComponent
	extends UIBase
{
	/**
	 * Initializes the base component that is placed within a display.
	 *
	 * @param __dm The owning display manager.
	 * @since 2016/05/22
	 */
	public UIComponent(UIManager __dm)
	{
		super(__dm);
	}
}

