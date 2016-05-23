// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase;

import java.lang.ref.Reference;
import javax.swing.JMenu;
import net.multiphasicapps.squirreljme.ui.InternalMenu;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIMenu;

/**
 * This represents the internal representation of a menu as it is used in
 * Swing.
 *
 * @since 2016/05/23
 */
public class SwingMenu
	extends InternalMenu
{
	/**
	 * Initializes the swing based menu.
	 *
	 * @param __ref The external reference.
	 * @since 2016/05/23
	 */
	public SwingMenu(Reference<UIMenu> __ref)
	{
		super(__ref);
	}
	
	/**
	 * Creates a JMenu for this given menu.
	 *
	 * @return The created menu.
	 * @since 2016/05/23
	 */
	final JMenu __createJMenu()
	{
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}

