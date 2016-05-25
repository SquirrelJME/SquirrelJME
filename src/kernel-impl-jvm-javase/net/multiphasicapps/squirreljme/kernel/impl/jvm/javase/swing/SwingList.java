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

import java.awt.Component;
import java.awt.Dimension;
import java.lang.ref.Reference;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
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
	protected final JList<JLabel> list;
	
	/** The list model. */
	protected final DefaultListModel<JLabel> model =
		new DefaultListModel<>();
	
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
		JList<JLabel> list;
		this.list = (list = new JList<>());
		JScrollPane pane = (JScrollPane)this.component;
		pane.getViewport().setView(list);
		
		// Vertical scroll only
		list.setLayoutOrientation(JList.VERTICAL);
		
		// Force a minimum size
		list.setMinimumSize(new Dimension(100, 100));
		
		// Set model to use
		list.setModel(this.model);
		
		// Use custom renderer which uses labels instead
		list.setCellRenderer(new ListCellRenderer<JLabel>()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/05/25
				 */
				@Override
				public Component getListCellRendererComponent(JList __jl,
					JLabel __v, int __dx, boolean __is, boolean __fo)
				{
					return __v;
				}
			});
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public void containeesChanged()
	{
		// Lock
		synchronized (this.lock)
		{
			// Get
			JList<JLabel> list = this.list;
			DefaultListModel<JLabel> model = this.model;
			
			// Clear list
			model.clear();
			
			// Add all
			SwingManager sm = platformManager();
			UIList xlist = this.<UIList>external(UIList.class);
			int n = xlist.size();
			for (int i = 0; i < n; i++)
			{
				model.add(i, (JLabel)sm.<SwingLabel>internal(SwingLabel.class,
					xlist.get(i)).getComponent());
				System.err.printf("DEBUG -- %s%n", model.get(i));
			}
			
			// revalidate
			list.revalidate();
		}
	}
}

