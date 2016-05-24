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
import net.multiphasicapps.squirreljme.ui.UIImage;
import net.multiphasicapps.squirreljme.ui.UIMenu;
import net.multiphasicapps.squirreljme.ui.UIMenuItem;

/**
 * This implements a menu item using Swing.
 *
 * @since 2016/05/23
 */
public class SwingMenuItem
	extends SwingBase
	implements PIMenuItem
{
	/** The Swing menu item. */
	protected final JMenuItem menuitem;
	
	/**
	 * Initializes the swing based menu item.
	 *
	 * @param __sm The swing manager.
	 * @param __ref The external reference.
	 * @since 2016/05/23d
	 */
	public SwingMenuItem(SwingManager __sm,
		Reference<? extends UIMenuItem> __ref)
	{
		this(__sm, __ref, new JMenuItem());
	}
	
	/**
	 * Initailizes the swing based menu item with the given Swing object.
	 *
	 * @param __sm The swing manager.
	 * @param __ref The external reference.
	 * @param __jmi The menu object to use.
	 * @throws NullPointerException If {@code __jmi} is null.
	 * @since 2016/05/23
	 */
	protected SwingMenuItem(SwingManager __sm,
		Reference<? extends UIMenuItem> __ref, JMenuItem __jmi)
		throws NullPointerException
	{
		super(__sm, __ref);
		
		// Check
		if (__jmi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.menuitem = __jmi;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public void setIcon(UIImage __icon)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			JMenuItem menuitem = this.menuitem;
			
			// Update the icon
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public void setText(String __text)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			JMenuItem menuitem = this.menuitem;
			
			// Update the text
			menuitem.setText(__text);
			menuitem.invalidate();
		}
	}
	
	/**
	 * Returns the menu item for the menu.
	 *
	 * @return The swing menu item.
	 * @since 2016/05/23
	 */
	final JMenuItem __getJMenuItem()
	{
		return this.menuitem;
	}
}

