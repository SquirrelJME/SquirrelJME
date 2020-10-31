// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * List.
 *
 * @since 2020/07/18
 */
public class SwingItemList
	extends SwingItem
{
	/** The model for the list. */
	private final DefaultListModel<ListEntry> model;
	
	/** The list used. */
	private final JList<ListEntry> list;
	
	/**
	 * Initializes the item.
	 * 
	 * @since 2020/07/18
	 */
	public SwingItemList()
	{
		DefaultListModel<ListEntry> model = new DefaultListModel<>();
		this.model = model;
		this.list = new JList<ListEntry>(model);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public JList<?> component()
	{
		return this.list;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void deletePost()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void property(int __id, int __sub, int __newValue)
		throws MLECallError
	{
		DefaultListModel<ListEntry> model = this.model;
		JList<ListEntry> list = this.list;
		int n = model.getSize();
		
		try
		{
			switch (__id)
			{
				case UIWidgetProperty.INT_NUM_ELEMENTS:
					if (__newValue < 0)
						throw new MLECallError("Negative list size.");
					
					while (__newValue < n)
						model.removeElementAt(--n);
					while (__newValue > n)
						model.add(n++, new ListEntry());
					break;
				
				case UIWidgetProperty.INT_LIST_ITEM_ID_CODE:
					model.get(__sub)._idCode = __newValue;
					break;
				
				default:
					throw new MLECallError("" + __id);
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new MLECallError("Invalid index: " + __sub, e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void property(int __id, int __sub, String __newValue)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/31
	 */
	@Override
	public int propertyInt(int __intProp, int __sub)
		throws MLECallError
	{
		DefaultListModel<ListEntry> model = this.model;
		JList<ListEntry> list = this.list;
		
		try
		{
			switch (__intProp)
			{
				case UIWidgetProperty.INT_LIST_ITEM_ID_CODE:
					return model.get(__sub)._idCode;
				
				default:
					return super.propertyInt(__intProp, __sub);
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new MLECallError("Invalid index: " + __sub, e);
		}
	}
}
