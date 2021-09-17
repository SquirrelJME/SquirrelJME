// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class Form
	extends Screen
{
	/** Items on the form. */
	final __VolatileList__<Item> _items =
		new __VolatileList__<>();
	
	/**
	 * Initializes an empty form with an optionally specified title.
	 *
	 * @param __t The title of the form, may be {@code null}.
	 * @since 2017/08/19
	 */
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
	public int append(Image __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		return this.append(new ImageItem(null, __i, ImageItem.LAYOUT_DEFAULT,
			null));
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
	public int append(Item __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB23 Cannot append an item which has already
		// be associated with a form.}
		if (__i._displayable != null)
			throw new IllegalStateException("EB23");
		__i._displayable = this;
		
		// Append item
		__VolatileList__<Item> items = this._items;
		int rv = items.append(__i);
		
		// Update display
		throw Debugging.todo();
		/*Display d = this._display;
		if (d != null)
			UIState.getInstance().repaint();
		
		return rv;*/
	}
	
	public void delete(int __a)
	{
		throw new todo.TODO();
	}
	
	public void deleteAll()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the given form item.
	 *
	 * @param __i The index to get.
	 * @return The item.
	 * @throws IndexOutOfBoundsException If the index is not within range.
	 * @since 2019/05/19
	 */
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
	public Item getCurrent()
	{
		throw new todo.TODO();
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
	
	public FormLayoutPolicy getLayoutPolicy()
	{
		throw new todo.TODO();
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
	
	public void insert(int __a, Item __b)
	{
		throw new todo.TODO();
	}
	
	public void set(int __a, Item __b)
	{
		throw new todo.TODO();
	}
	
	public void setItemStateListener(ItemStateListener __a)
	{
		throw new todo.TODO();
	}
	
	public void setItemTraversalListener(ItemTraversalListener __itl)
	{
		throw new todo.TODO();
	}
	
	public void setLayoutPolicy(FormLayoutPolicy __p)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the number of items in the form.
	 *
	 * @return The number of form items.
	 * @since 2109/05/19
	 */
	public int size()
	{
		return this._items.size();
	}
}


