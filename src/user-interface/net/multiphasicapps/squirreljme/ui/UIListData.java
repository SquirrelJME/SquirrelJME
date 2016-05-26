// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * This class is used to internally provide a representation of data to be
 * stored within a list so that it may be displayed by a {@link UIList}. A
 * class which extends this can also optionally specificy alternative icons
 * and text which may be used when displaying elements.
 *
 * Synchronization locks are held on this class.
 *
 * @param <E> The type of value stored in the list.
 * @since 2016/05/25
 */
public class UIListData<E>
	extends AbstractList<E>
	implements RandomAccess
{
	/** The type of class which items must be. */
	protected final Class<E> type;
	
	/** The internal storage area. */
	private final List<E> _internal =
		new ArrayList<>();
	
	/**
	 * Initializes the base list storage.
	 *
	 * @param __cl The class type which elements must be.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/25
	 */
	public UIListData(Class<E> __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __cl;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final boolean add(E __e)
	{
		// Lock
		synchronized (this)
		{
			// Add
			boolean rv = this._internal.add(this.type.cast(__e));
			
			// Did change?
			if (rv)
				__changed();
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final void add(int __i, E __e)
	{
		// Lock
		synchronized (this)
		{
			// Add
			this._internal.add(__i, this.type.cast(__e));
			
			__changed();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final boolean addAll(Collection<? extends E> __c)
	{
		// Lock
		synchronized (this)
		{
			// Add all
			boolean rv = false;
			for (E e : __c)
				rv |= add(e);
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final void clear()
	{
		// Lock
		synchronized (this)
		{
			this._internal.clear();
			
			__changed();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final boolean contains(Object __o)
	{
		// Lock
		synchronized (this)
		{
			return this._internal.contains(__o);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final boolean containsAll(Collection<?> __c)
	{
		// Lock
		synchronized (this)
		{
			return this._internal.containsAll(__c);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Lock
		synchronized (this)
		{
			return this._internal.equals(__o);
		}
	}
	
	/**
	 * This generates the icon which should be displayed when the list is
	 * drawn.
	 *
	 * @param __dx The index of the element.
	 * @param __v The value of the element.
	 * @return The icon to use for the item or {@code null} if it should have
	 * no icon.
	 * @throws UIException If the icon could not be generated.
	 * @since 2016/05/25
	 */
	public UIImage generateIcon(int __dx, E __v)
		throws UIException
	{
		return null;
	}
	
	/**
	 * This generates the text which should be displayed when the list is
	 * drawn.
	 *
	 * @param __dx The index of the element.
	 * @param __v The value of the element.
	 * @return The text that should be displayed on the label.
	 * @throws UIException If the icon could not be generated.
	 * @since 2016/05/25
	 */
	public String generateText(int __dx, E __v)
		throws UIException
	{
		synchronized (this)
		{
			return String.valueOf(__v);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final E get(int __i)
	{
		// Lock
		synchronized (this)
		{
			return this._internal.get(__i);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final int hashCode()
	{
		// Lock
		synchronized (this)
		{
			return this._internal.hashCode();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final int indexOf(Object __o)
	{
		// Lock
		synchronized (this)
		{
			return this._internal.indexOf(__o);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final boolean isEmpty()
	{
		return size() <= 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final Iterator<E> iterator()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final int lastIndexOf(Object __o)
	{
		// Lock
		synchronized (this)
		{
			return this._internal.lastIndexOf(__o);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final ListIterator<E> listIterator()
	{
		return listIterator(0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final ListIterator<E> listIterator(int __i)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final E remove(int __i)
	{
		// Lock
		synchronized (this)
		{
			E rv = this._internal.remove(__i);
			__changed();
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final boolean remove(Object __o)
	{
		// Lock
		synchronized (this)
		{
			boolean rv = this._internal.remove(__o);
			if (rv)
				__changed();
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final boolean removeAll(Collection<?> __c)
	{
		// Lock
		synchronized (this)
		{
			boolean rv = this._internal.removeAll(__c);
			if (rv)
				__changed();
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final boolean retainAll(Collection<?> __c)
	{
		// Lock
		synchronized (this)
		{
			boolean rv = this._internal.retainAll(__c);
			if (rv)
				__changed();
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final E set(int __i, E __e)
	{
		// Lock
		synchronized (this)
		{
			E rv = this._internal.set(__i, this.type.cast(__e));
			__changed();
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final int size()
	{
		// Lock
		synchronized (this)
		{
			return this._internal.size();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final Object[] toArray()
	{
		// Lock
		synchronized (this)
		{
			return this._internal.toArray();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final <T> T[] toArray(T[] __a)
	{
		// Lock
		synchronized (this)
		{
			return this._internal.toArray(__a);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final String toString()
	{
		// Lock
		synchronized (this)
		{
			return this._internal.toString();
		}
	}
	
	/**
	 * This is called whenever the list has been changed.
	 *
	 * @since 2016/05/25
	 */
	private void __changed()
	{
	}
}

