// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

public class Form
	extends Screen
{
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
			setTitle(__t);
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
				
				append(i);
			}
	}
	
	public int append(String __a)
	{
		throw new todo.TODO();
	}
	
	public int append(Image __a)
	{
		throw new todo.TODO();
	}
	
	public int append(Item __a)
	{
		throw new todo.TODO();
	}
	
	public void delete(int __a)
	{
		throw new todo.TODO();
	}
	
	public void deleteAll()
	{
		throw new todo.TODO();
	}
	
	public Item get(int __a)
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
		return this.__defaultHeight();
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
		return this.__defaultWidth();
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
	
	public int size()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	void __drawChain(Graphics __g)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	void __updateDrawChain(__DrawSlice__ __sl)
	{
		throw new todo.TODO();
	}
}


