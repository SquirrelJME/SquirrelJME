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

import java.awt.Dimension;
import java.lang.ref.Reference;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import net.multiphasicapps.squirreljme.ui.PIList;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIList;

/**
 * This is the swing representation of a list.
 *
 * @since 2016/05/24
 */
public class SwingList
	extends SwingComponent
	implements PIList
{
	/** The list component. */
	protected final JList list;
	
	/**
	 * Initializes the swing list.
	 *
	 * @param __sm The swing manager.
	 * @param __ref The external reference.
	 * @since 2016/05/24
	 */
	public SwingList(SwingManager __sm, Reference<? extends UIList> __ref)
	{
		super(__sm, __ref, new JScrollPane(
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		// Set
		JList list;
		this.list = (list = new JList());
		JScrollPane pane = (JScrollPane)this.component;
		pane.getViewport().setView(list);
		
		// Vertical scroll only
		list.setLayoutOrientation(JList.VERTICAL);
		
		// Force a minimum size
		this.list.setMinimumSize(new Dimension(100, 100));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public void containeesChanged()
	{
		throw new Error("TODO");
	}
}

