// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase.swing;

import java.lang.ref.Reference;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.multiphasicapps.squirreljme.ui.PIMenu;
import net.multiphasicapps.squirreljme.ui.PIMenuItem;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIMenu;
import net.multiphasicapps.squirreljme.ui.UIMenuItem;
/**
 * This represents the internal representation of a menu as it is used in
 * Swing.
 *
 * @since 2016/05/23
 */
public class SwingMenu
	extends SwingMenuItem
	implements PIMenu
{
	/** The current menu representation. */
	protected final JMenu menu;
	
	/**
	 * Initializes the swing based menu.
	 *
	 * @param __sm The swing manager.
	 * @param __ref The external reference.
	 * @since 2016/05/23
	 */
	public SwingMenu(SwingManager __sm, Reference<? extends UIMenu> __ref)
	{
		super(__sm, __ref, new JMenu());
		
		// Set menu
		this.menu = (JMenu)this.menuitem;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public void updatedItems()
		throws UIException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Creates a JMenu for this given menu.
	 *
	 * @return The created menu.
	 * @throws UIException If the menu could not be created.
	 * @since 2016/05/23
	 */
	final JMenu __createJMenu()
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			// The current menu
			JMenu menu = this.menu;
			
			// Obtain the external menu first
			UIMenu external = super.<UIMenu>external(UIMenu.class);
			
			// Is this a sub-menu?
			UIMenu parent = external.getParent();
			if (parent != null)
				throw new Error("TODO");
			
			// This is a top-level menu
			else
			{
				if (true)
					throw new Error("TODO");
			}
			
			// Return the menu
			return menu;
		}
	}
}

