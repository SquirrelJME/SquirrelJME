// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import org.jetbrains.annotations.Async;

@Api
public class Form
	extends Screen
{
	/** Items on the form. */
	final __VolatileList__<Item> _items =
		new __VolatileList__<>();
		
	/** The layout policy for this form. */
	volatile FormLayoutPolicy _layout =
		new __DefaultFormLayoutPolicy__(this);
	
	/** Is the layout considered stale? */
	volatile boolean _staleLayout;
	
	/**
	 * Initializes an empty form with an optionally specified title.
	 *
	 * @param __t The title of the form, may be {@code null}.
	 * @since 2017/08/19
	 */
	@Api
	public Form(String __t)
	{
		this(__t, null);
	}
	
	/**
	 * Initializes a form with the given items and an optionally specified
	 * title.
	 *
	 * @param __t The title of the form, may be {@code null}.
	 * @param __i The items to add to the form.
	 * @throws IllegalStateException If an item in the form is already owned
	 * by another container.
	 * @throws NullPointerException If any element in {@code __i} is
	 * {@code null}.
	 * @since 2017/08/19
	 */
	@Api
	public Form(String __t, Item[] __i)
		throws IllegalStateException, NullPointerException
	{
		// Forms just use the titles the same as Displayables
		try
		{
			this.setTitle(__t);
		}
		
		// Ignore this if it occurs so that constructing the form does not
		// end in failure
		catch (DisplayCapabilityException e)
		{
		}
		
		// Append items in order
		if (__i != null)
			for (Item i : __i)
			{
				// Check
				if (i == null)
					throw new NullPointerException("NARG");
				
				this.append(i);
			}
	}
	
	/**
	 * Appends the given string.
	 *
	 * @param __s The string.
	 * @return The index of the item.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/17
	 */
	@Api
	public int append(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		return this.append(new StringItem(null, __s));
	}
	
	/**
	 * Appends the given image.
	 *
	 * @param __i The image.
	 * @return The index of the item.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/17
	 */
	@Api
	public int append(Image __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		return this.append(new ImageItem(null, __i,
			ImageItem.LAYOUT_DEFAULT, null));
	}
	
	/**
	 * Appends the given item to the form.
	 *
	 * @param __i The item to append.
	 * @return The index of the item.
	 * @throws IllegalStateException If the item is already associated with
	 * a form.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/17
	 */
	@Api
	public int append(Item __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB23 Cannot append an item which has already
		been associated with a form.} */
		if (__i._displayable != null)
			throw new IllegalStateException("EB23");
		__i._displayable = this;
		
		// Append item
		__VolatileList__<Item> items = this._items;
		int rv = items.append(__i);
		
		// Update display
		this.__update();
		
		return rv;
	}
	
	/**
	 * Deletes the specified index in the form.
	 * 
	 * @param __i The index to delete.
	 * @throws IndexOutOfBoundsException If the item is not valid.
	 * @since 2022/07/07
	 */
	@Api
	public void delete(int __i)
		throws IndexOutOfBoundsException
	{
		// Delete item
		this._items.remove(__i);
		
		// Update display
		this.__update();
	}
	
	@Api
	public void deleteAll()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the given form item.
	 *
	 * @param __i The index to get.
	 * @return The item.
	 * @throws IndexOutOfBoundsException If the index is not within range.
	 * @since 2019/05/19
	 */
	@Api
	public Item get(int __i)
		throws IndexOutOfBoundsException
	{
		return this._items.get(__i);
	}
	
	/**
	 * Returns the currently focused item in the form, or {@code null} if there
	 * is not item being focused.
	 *
	 * @return The current focus item.
	 * @since 2019/12/09
	 */
	@Api
	public Item getCurrent()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getHeight()
	{
		return Displayable.__getHeight(this, null);
	}
	
	/**
	 * Returns the current layout policy for the form.
	 * 
	 * @return The form layout policy currently in effect, if there is none
	 * this will be {@code null}.
	 * @since 2021/11/26
	 */
	@Api
	public FormLayoutPolicy getLayoutPolicy()
	{
		FormLayoutPolicy layout = this._layout;
		if (layout.getClass() == __DefaultFormLayoutPolicy__.class)
			return null;
		
		return layout;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getWidth()
	{
		return Displayable.__getWidth(this, null);
	}
	
	@Api
	public void insert(int __a, Item __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void set(int __a, Item __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setItemStateListener(ItemStateListener __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setItemTraversalListener(ItemTraversalListener __itl)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the layout policy for the current form.
	 * 
	 * @param __layout The layout to set to, may be {@code null} to reset the
	 * policy to the default.
	 * @throws IllegalArgumentException If {@link FormLayoutPolicy#getForm()}
	 * does not match this form.
	 * @since 2021/11/26
	 */
	@Api
	public void setLayoutPolicy(FormLayoutPolicy __layout)
		throws IllegalArgumentException
	{
		this.__setLayoutPolicy(__layout);
	}
	
	/**
	 * Returns the number of items in the form.
	 *
	 * @return The number of form items.
	 * @since 2109/05/19
	 */
	@Api
	public int size()
	{
		return this._items.size();
	}
	
	/**
	 * This is called so that the form layout is actually done.
	 * 
	 * @since 2022/07/20
	 */
	@SerializedEvent
	@Async.Execute
	void __performLayout()
	{
		// If this form is not on a display, do not calculate the layout as
		// we might still be loading everything in
		Display display = this._display;
		if (display == null)
			return;
		
		// Only perform this if the current layout is stale and must be
		// updated
		if (!this._staleLayout)
			return;
		this._staleLayout = false;
		
		UIBackend backend = this.__backend();
		UIFormBracket uiForm = this.__state(
			__DisplayableState__.class)._uiForm;
		FormLayoutPolicy layout = this._layout;
		
		// Indicate we are in an update
		synchronized (layout)
		{
			// Do not double update
			if (layout._inUpdate)
				return;
			
			layout._inUpdate = true;
		}
		
		// Perform the update, revert to the default if there is an error
		// with the layout policy
		__LayoutLock__ lock = layout._lock;
		try
		{
			// Set the lock
			lock.lock();
			
			// Initialize the layout for these items
			Item[] items = this._items.toArray(new Item[0]);
			layout.__init(items);
			
			// Perform update traversal
			/*layout.doLayout(
			doLayout(int __viewportX, int __viewportY,
				int __viewportW, int __viewportH, int[] __totalSize)*/
			
			if (true)
				throw Debugging.todo();
		}
		catch (RuntimeException e)
		{
			// If this is the default, just throw the exception since we
			// cannot do anything otherwise regarding this... would otherwise
			// cause an infinite loop here and just not work at all.
			if (layout.getClass() == __DefaultFormLayoutPolicy__.class)
				throw e;
			
			// Return the policy to the default
			this.__setLayoutPolicy(null);
			
			// Try laying out again
			this.__update();
		}
		finally
		{
			// We are no longer in an update
			synchronized (layout)
			{
				layout._inUpdate = false;
			}
			
			// Clear the lock
			lock.unlock();
		}
	}
	
	/**
	 * Sets the layout policy for the current form, this is the actual
	 * implementation due to non-{@code final}.
	 * 
	 * @param __layout The layout to set to, may be {@code null} to reset the
	 * policy to the default.
	 * @throws IllegalArgumentException If {@link FormLayoutPolicy#getForm()}
	 * does not match this form.
	 * @since 2021/11/26
	 */
	private void __setLayoutPolicy(FormLayoutPolicy __layout)
		throws IllegalArgumentException
	{
		// Initialize back to the default?
		if (__layout == null)
		{
			if (this.getLayoutPolicy() != null)
			{
				this._layout = new __DefaultFormLayoutPolicy__(this);
				this._staleLayout = true;
			}
			
			return;
		}
		
		/* {@squirreljme.error EB3p The layout belong to a different form.} */
		if (__layout.getForm() != this)
			throw new IllegalArgumentException("EB3p");
		
		// Set and make stale
		this._layout = __layout;
		this._staleLayout = true;
	}
	
	/**
	 * Signals that the form should be refreshed and update all of its
	 * stored items accordingly.
	 * 
	 * @since 2021/11/26
	 */
	@SquirrelJMEVendorApi
	void __update()
	{
		// Indicate that the layout is now stale and must be updated
		this._staleLayout = true;
		
		// Queue this up for later, so it is forced to be redrawn
		Display display = this._display;
		if (display != null)
			this.__backend()
				.formRefresh(this.__state(__DisplayableState__.class)._uiForm);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	__CommonState__ __stateInit(UIBackend __backend)
		throws NullPointerException
	{
		return new __FormState__(__backend, this);
	}
	
	/**
	 * File selector state.
	 * 
	 * @since 2023/01/14
	 */
	static class __FormState__
		extends Screen.__ScreenState__
	{
		/**
		 * Initializes the backend state.
		 *
		 * @param __backend The backend used.
		 * @param __self Self widget.
		 * @since 2023/01/14
		 */
		__FormState__(UIBackend __backend, DisplayWidget __self)
		{
			super(__backend, __self);
		}
	}
}


