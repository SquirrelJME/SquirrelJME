// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.SwingUtilities;

/**
 * List.
 *
 * @since 2020/07/18
 */
public class SwingItemList
	extends SwingItem
{
	/** The model for the list. */
	final DefaultListModel<ListEntry> _model;
	
	/** The list used. */
	final JList<ListEntry> _list;
	
	/**
	 * Initializes the item.
	 * 
	 * @since 2020/07/18
	 */
	public SwingItemList()
	{
		super(UIItemType.LIST);
		
		DefaultListModel<ListEntry> model = new DefaultListModel<>();
		this._model = model;
		
		// Use custom renderer for the list
		JList<ListEntry> list = new JList<ListEntry>(model);
		list.setCellRenderer(new ListRenderer());
		
		this._list = list;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/17
	 */
	@Override
	public void addedOnForm(SwingForm __form, int __pos)
	{
		// Request that all icons be refreshed accordingly, this requires
		// that a callback be defined for the item which it should be when it
		// is part of the form
		DefaultListModel<ListEntry> model = this._model;
		for (int i = 0, n = model.size(); i < n; i++)
			this.__requestIconUpdate(i, model.get(i)._iconDimension);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public JList<?> component()
	{
		return this._list;
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
		DefaultListModel<ListEntry> model = this._model;
		JList<ListEntry> list = this._list;
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
				
				case UIWidgetProperty.INT_LIST_ITEM_DISABLED:
					model.get(__sub)._disabled = (__newValue != 0);
					break;
				
				case UIWidgetProperty.INT_LIST_ITEM_SELECTED:
					model.get(__sub)._selected = (__newValue != 0);
					break;
				
				case UIWidgetProperty.INT_LIST_ITEM_ICON_DIMENSION:
					this.__requestIconUpdate(__sub, __newValue);
					break;
				
				case UIWidgetProperty.INT_LIST_ITEM_FONT:
					model.get(__sub)._fontDescription = __newValue;
					break;
				
				default:
					throw new MLECallError("" + __id);
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new MLECallError("Invalid index: " + __sub, e);
		}
		
		// Always make sure list is updated
		finally
		{
			list.repaint();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void property(int __id, int __sub, String __newValue)
	{
		DefaultListModel<ListEntry> model = this._model;
		JList<ListEntry> list = this._list;
		int n = model.getSize();
		
		try
		{
			switch (__id)
			{
				case UIWidgetProperty.STRING_LIST_ITEM_LABEL:
					model.get(__sub)._label = __newValue;
					break;
				
				default:
					throw new MLECallError("" + __id);
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new MLECallError("Invalid index: " + __sub, e);
		}
		
		// Always make sure list is updated
		finally
		{
			list.repaint();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/31
	 */
	@Override
	public int propertyInt(int __intProp, int __sub)
		throws MLECallError
	{
		DefaultListModel<ListEntry> model = this._model;
		JList<ListEntry> list = this._list;
		
		try
		{
			switch (__intProp)
			{
				case UIWidgetProperty.INT_NUM_ELEMENTS:
					return model.size();
				
				case UIWidgetProperty.INT_LIST_ITEM_ID_CODE:
					return model.get(__sub)._idCode;
				
				case UIWidgetProperty.INT_LIST_ITEM_DISABLED:
					return (model.get(__sub)._disabled ? 1 : 0);
				
				case UIWidgetProperty.INT_LIST_ITEM_SELECTED:
					return (model.get(__sub)._selected ? 1 : 0);
				
				case UIWidgetProperty.INT_LIST_ITEM_ICON_DIMENSION:
					return model.get(__sub)._iconDimension;
				
				case UIWidgetProperty.INT_LIST_ITEM_FONT:
					return model.get(__sub)._fontDescription;
				
				default:
					return super.propertyInt(__intProp, __sub);
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new MLECallError("Invalid index: " + __sub, e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/17
	 */
	@Override
	public String propertyStr(int __strProp, int __sub)
		throws MLECallError
	{
		DefaultListModel<ListEntry> model = this._model;
		JList<ListEntry> list = this._list;
		
		try
		{
			switch (__strProp)
			{
				case UIWidgetProperty.STRING_LIST_ITEM_LABEL:
					return model.get(__sub)._label;
				
				default:
					return super.propertyStr(__strProp, __sub);
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new MLECallError("Invalid index: " + __sub, e);
		}
	}
	
	/**
	 * Requests that the icon be updated.
	 * 
	 * @param __sub The item.
	 * @param __dim The dimension to use.
	 * @since 2020/11/17
	 */
	private void __requestIconUpdate(int __sub, int __dim)
	{
		ListEntry entry = this._model.get(__sub);
		entry._iconDimension = __dim;
		
		// Make the icon dirty, to make it update
		entry._dirtyIcon = true;
		
		// Clear the icon?
		if (__dim <= 0)
		{
			entry._icon = null;
			return;
		}
		
		// Debug
		Debugging.debugNote("Form+Callback? %d", __dim);
		
		// Can only do something if there is a form and callback
		SwingForm form = this.form();
		UIFormCallback callback = (form != null ? form.callback() : null);
		if (form == null || callback == null)
			return;
		
		// Debug
		Debugging.debugNote("Request icon: %dx%d", __dim, __dim);
		
		// Send in request to update the icon accordingly
		SwingUtilities.invokeLater(new ListIconUpdate(
			callback, form, this, entry, __sub, __dim));
	}
}
