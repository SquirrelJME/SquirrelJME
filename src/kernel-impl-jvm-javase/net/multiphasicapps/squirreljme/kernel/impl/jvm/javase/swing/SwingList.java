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
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import net.multiphasicapps.imagereader.ImageType;
import net.multiphasicapps.squirreljme.ui.PIList;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIImage;
import net.multiphasicapps.squirreljme.ui.UIList;
import net.multiphasicapps.squirreljme.ui.UIListData;

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
	protected final JList<Object> list;
	
	/** The list data. */
	protected final UIListData<Object> data;
	
	/**
	 * Initializes the swing list.
	 *
	 * @param __sm The swing manager.
	 * @param __ref The external reference.
	 * @since 2016/05/24
	 */
	public SwingList(SwingManager __sm, Reference<UIList<Object>> __ref,
		UIListData<Object> __ld)
	{
		super(__sm, __ref, new JScrollPane(
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		// Set
		this.data = __ld;
		JList<Object> list;
		this.list = (list = new JList<>());
		JScrollPane pane = (JScrollPane)this.component;
		pane.getViewport().setView(list);
		
		// Vertical scroll only
		list.setLayoutOrientation(JList.VERTICAL);
		
		// Force a minimum size
		list.setMinimumSize(new Dimension(100, 100));
		
		// Set model to use
		__Model__ m = new __Model__();
		list.setModel(m);
		__ld.addDataChangeListener(m);
		
		// Only allow selecting single items
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Use custom renderer which uses labels instead
		list.setCellRenderer(new __Renderer__());
	}
	
	/**
	 * This implements the list model for viewing over the internal list data.
	 *
	 * @since 2016/05/26
	 */
	private final class __Model__
		extends AbstractListModel<Object>
		implements UIListData.DataChangeListener
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/05/26
		 */
		@Override
		public Object getElementAt(int __i)
		{
			return SwingList.this.data.get(__i);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/26
		 */
		@Override
		public int getSize()
		{
			return SwingList.this.data.size();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/26
		 */
		@Override
		public void listDataChanged(int __from, int __to)
		{
			fireContentsChanged(this, __from, __to);
		}
	}
	
	/**
	 * This is the renderer for list items.
	 *
	 * @since 2016/05/26
	 */
	private final class __Renderer__
		implements ListCellRenderer<Object>
	{
		/** The label used for drawing. */
		protected final JLabel label =
			new JLabel();
		
		/**
		 * Basic initialization.
		 *
		 * @since 2016/05/26
		 */
		{
			// Force the background to appear
			label.setOpaque(true);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/25
		 */
		@Override
		public Component getListCellRendererComponent(JList __jl,
			Object __v, int __dx, boolean __is, boolean __fo)
		{
			// Get label
			JLabel label = this.label;
			UIListData<Object> data = SwingList.this.data;
			
			// Set the text of the label to the generated text
			label.setText(data.generateText(__dx, __v));
			
			// Possibly get the icon to use
			UIImage ico = data.generateIcon(__dx, __v);
			if (ico == null)
				label.setIcon(null);
			else
				label.setIcon(SwingList.this.manager.imageDataToImageIcon(
					ico.getImage(16, 16, ImageType.INT_ARGB, true)));
			
			// Make it so the label is colorized if it is selected or
			// not
			label.setForeground((__is ? __jl.getSelectionForeground() :
				__jl.getForeground()));
			label.setBackground((__is ? __jl.getSelectionBackground() :
				__jl.getBackground()));
			
			// Use the label
			return label;
		}
	}
}

