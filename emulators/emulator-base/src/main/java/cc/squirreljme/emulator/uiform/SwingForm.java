// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Represents a single Swing form, which is built-on top of a panel that
 * is shown in a window.
 *
 * @since 2020/07/01
 */
public final class SwingForm
	implements UIFormBracket
{
	/** The panel which makes up the form. */
	protected final JPanel formPanel;
	
	/** Specially placed items. */
	private final SwingItem[] _specialItems =
		new SwingItem[(-UIItemPosition.MIN_VALUE) + 1];
	
	/** Items on this form. */
	private final List<SwingItem> _items =
		new ArrayList<>();
	
	/** The command bar. */
	protected final JPanel commandBar =
		new JPanel();
	
	/** Left command. */
	protected final JPanel leftCommand =
		new JPanel();
	
	/** Right command. */
	protected final JPanel rightCommand =
		new JPanel();
	
	/** Title. */
	protected final JPanel title =
		new JPanel();
	
	/** Standard body. */
	protected final JPanel body =
		new JPanel();
	
	/** The ticker. */
	protected final JPanel ticker =
		new JPanel();
	
	/** The top panel (title and ticker). */
	protected final JPanel topBar =
		new JPanel();
	
	/** The primary form list contents. */
	protected final JPanel adjacent =
		new JPanel();
	
	/**
	 * Initializes the form.
	 * 
	 * @since 2020/07/01
	 */
	public SwingForm()
	{
		JPanel panel = new JPanel();
		
		// Make sure the panel is not so tiny
		Dimension minDim = new Dimension(SwingHardMetrics.DISPLAY_MIN_WIDTH,
			SwingHardMetrics.DISPLAY_MIN_HEIGHT);
		panel.setMinimumSize(minDim);
		panel.setPreferredSize(minDim);
		panel.setSize(minDim);
		
		// Use a border layout because we can set tops and bottoms
		// accordingly
		panel.setLayout(new BorderLayout());
		
		this.formPanel = panel;
	}
	
	/**
	 * Deletes the form.
	 * 
	 * @since 2020/07/01
	 */
	public void delete()
	{
		Debugging.todoNote("Form deletion?");
	}
	
	/**
	 * Returns the item at the position.
	 * 
	 * @param __pos The position.
	 * @return The item at the position or {@code null} if there is nothing.
	 * @throws MLECallError If the position is not valid.
	 * @since 2020/07/18
	 */
	public final SwingItem itemAtPosition(int __pos)
		throws MLECallError
	{
		if (__pos < UIItemPosition.MIN_VALUE)
			throw new MLECallError("Bad position: " + __pos);
		
		synchronized (this)
		{
			// Is this in a special index
			if (__pos < 0)
				return this._specialItems[-__pos];
			
			// Get based on the index
			List<SwingItem> items = this._items;
			if (__pos >= items.size())
				throw new MLECallError("Invalid index: " + __pos);
			
			return items.get(__pos);
		}
	}
	
	/**
	 * Returns the number of normal items on this form.
	 * 
	 * @return The number of normal items.
	 * @since 2020/07/18
	 */
	public final int itemCount()
	{
		synchronized (this)
		{
			return this._items.size();
		}
	}
	
	/**
	 * Returns the position of the given item.
	 * 
	 * @param __item The item to query.
	 * @return The position of the item.
	 * @throws MLECallError On null arguments.
	 * @since 2020/07/18
	 */
	public final int itemPosition(SwingItem __item)
		throws MLECallError
	{
		if (__item == null)
			throw new MLECallError("NARG");
		
		synchronized (this)
		{
			int n = this.itemCount();
			for (int i = UIItemPosition.MIN_VALUE; i < n; i++)
				if (__item == this.itemAtPosition(i))
					return i;
			
			return UIItemPosition.NOT_ON_FORM;
		}
	}
	
	/**
	 * Sets the position of the given item.
	 * 
	 * @param __item The item.
	 * @param __pos The position.
	 * @throws MLECallError If the position is not valid, or on null arguments.
	 * @since 2020/07/18
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public final void itemPosition(SwingItem __item, int __pos)
		throws MLECallError
	{
		if (__item == null)
			throw new MLECallError("Null arguments");
		
		if (__pos < UIItemPosition.MIN_VALUE)
			throw new MLECallError("Invalid position: " + __pos);
		
		synchronized (this)
		{
			// Special index?
			if (__pos < 0)
			{
				SwingItem[] specialItems = this._specialItems;
				
				// Remove old item, if there is one
				SwingItem old = specialItems[-__pos];
				if (old != null)
					old.removeFromForm();
				
				// Store new item
				synchronized (__item)
				{
					// Remove the given item from the form it is in
					__item.removeFromForm();
					
					// Bind new assignment
					specialItems[-__pos] = __item;
					__item._form = this;
				}
			}
			
			// Either setting the final item or replacing one
			else
			{
				List<SwingItem> items = this._items;
				int n = items.size();
				if (__pos > n)
					throw new MLECallError("Out of range: " + n);
				
				// Remove old item, if there is one
				if (__pos != n)
				{
					SwingItem old = items.get(__pos);
					if (old != null)
						old.removeFromForm();
				}
				
				// Store new item
				synchronized (__item)
				{
					// Remove the given item from the form it is in
					__item.removeFromForm();
					
					// Add the item here, because it would have been removed
					items.add(__pos, __item);
					__item._form = this;
				}
			}
			
			// Refresh the form
			this.refresh();
		}
	}
	
	/**
	 * Removes the item at the given index.
	 * 
	 * @param __pos The position to remove from.
	 * @return The item that was here, this may be {@code null}.
	 * @throws MLECallError If the position is not valid.
	 * @since 2020/07/18
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public final SwingItem itemRemove(int __pos)
		throws MLECallError
	{
		if (__pos < UIItemPosition.MIN_VALUE)
			throw new MLECallError("Invalid position: " + __pos);
		
		synchronized (this)
		{
			SwingItem old;
			
			// Special position
			if (__pos < 0)
			{
				SwingItem[] specialItems = this._specialItems;
				
				old = specialItems[-__pos];
				if (old != null)
					synchronized (old)
					{
						specialItems[-__pos] = null;
						old._form = null;
					}
			}
			
			// Remove from the item list
			else
			{
				List<SwingItem> items = this._items;
				
				// Check that a bad item is not being removed
				int n = items.size();
				if (__pos >= n)
					throw new MLECallError("Invalid position: " + __pos);
				
				// Remove from the list
				old = items.remove(__pos);
				if (old != null)
					synchronized (old)
					{
						old._form = null;
					}
			}
			
			// Refresh the form
			this.refresh();
			
			return old;
		}
	}
	
	/**
	 * Removes the given item from this form.
	 * 
	 * @param __item The item to remove.
	 * @throws MLECallError On null arguments.
	 * @since 2020/07/18
	 */
	public final void itemRemove(SwingItem __item)
		throws MLECallError
	{
		if (__item == null)
			throw new MLECallError("NARG");
		
		synchronized (this)
		{
			int foundPos = this.itemPosition(__item);
			if (foundPos == UIItemPosition.NOT_ON_FORM)
				throw new MLECallError("Item not on form.");
			
			this.itemRemove(foundPos);
			
			// Refresh the form
			this.refresh();
		}
	}
	
	/**
	 * Refreshes this form.
	 * 
	 * @since 2020/07/18
	 */
	public final void refresh()
	{
		synchronized (this)
		{
			JPanel formPanel = this.formPanel;
			
			// The border layout is the simplest for this and makes sense
			formPanel.removeAll();
			formPanel.setLayout(new BorderLayout());
			
			// If a body item is set, only use this one and care about nothing
			// else (is the full-screen desired item)
			SwingItem bodyItem = this.itemAtPosition(UIItemPosition.BODY);
			if (bodyItem != null)
			{
				formPanel.add(bodyItem.component(), BorderLayout.CENTER);
				return;
			}
			
			// Adding a title and a ticker to the form?
			SwingItem titleItem = this.itemAtPosition(UIItemPosition.TITLE);
			SwingItem tickerItem = this.itemAtPosition(UIItemPosition.TICKER);
			if (titleItem != null || tickerItem != null)
			{
				JPanel topBar = this.topBar;
				
				// If we are using the grid layout, we have to add one by one
				// so just remove everything to refresh it
				topBar.removeAll();
				topBar.setLayout(new GridLayout(0, 1));
				
				// Add the title component
				if (titleItem != null)
					topBar.add(titleItem.component());
				
				// Then the ticker component (if any)
				if (tickerItem != null)
					topBar.add(tickerItem.component());
				
				// Now use the top bar on the form
				formPanel.add(topBar, BorderLayout.PAGE_START);
			}
			
			// Adding commands to the frame?
			SwingItem leftItem = this.itemAtPosition(
				UIItemPosition.LEFT_COMMAND);
			SwingItem rightItem = this.itemAtPosition(
				UIItemPosition.RIGHT_COMMAND);
			if (leftItem != null || rightItem != null)
			{
				JPanel commandBar = this.commandBar;
				
				// Setup command bar for two items
				commandBar.removeAll();
				commandBar.setLayout(new GridLayout(1, 2));
				
				if (leftItem != null)
					commandBar.add(leftItem.component());
				if (rightItem != null)
					commandBar.add(rightItem.component());
				
				// Use as the bottom of the form
				formPanel.add(commandBar, BorderLayout.PAGE_END);
			}
			
			// Setup normal adjacent items
			JPanel adjacent = this.adjacent;
			int n = this.itemCount();
			
			// Use a growing layout but one that is even
			adjacent.removeAll();
			adjacent.setLayout(new GridBagLayout());
			
			// Setup constraints for all the items
			GridBagConstraints cons = new GridBagConstraints();
			cons.gridwidth = 1;
			cons.gridheight = n;
			cons.fill = GridBagConstraints.HORIZONTAL;
			cons.weightx = 0.0;
			cons.weighty = 1.0;
			cons.anchor = GridBagConstraints.PAGE_START;
			
			// Now add all the various form bits
			for (int i = 0; i < n; i++)
			{
				cons.gridx = 0;
				cons.gridy = i;
				
				adjacent.add(this.itemAtPosition(i).component(), cons);
			}
			
			// Add the final form
			formPanel.add(adjacent, BorderLayout.CENTER);
			
			// Request everything be redrawn
			formPanel.validate();
			formPanel.repaint();
		}
	}
}
