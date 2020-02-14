// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/**
 * This is a list which contains no elements.
 *
 * @since 2016/04/10
 */
public final class EmptyList
	extends AbstractList
	implements RandomAccess
{
	/** The empty list. */
	private static Reference<List> _EMPTY_LIST;
	
	/**
	 * Initializes the empty list.
	 *
	 * @since 2016/04/10
	 */
	private EmptyList()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean contains(Object __o)
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean containsAll(Collection __c)
	{
		return __c.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public Object get(int __i)
	{
		// {@squirreljme.error AC04 The empty list contains no elements.}
		throw new IndexOutOfBoundsException("AC04");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public int indexOf(Object __o)
	{
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean isEmpty()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/01
	 */
	@Override
	public Iterator iterator()
	{
		return EmptyIterator.empty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public int lastIndexOf(Object __o)
	{
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public int size()
	{
		return 0;
	}
	
	/**
	 * This returns a list which contains nothing.
	 *
	 * @param <V> The type of values the list contains.
	 * @return The empty list.
	 * @since 2016/04/10
	 */
	@SuppressWarnings({"unchecked"})
	public static <V> List<V> empty()
	{
		// Get reference
		Reference<List> ref = _EMPTY_LIST;
		List rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
			_EMPTY_LIST = new WeakReference<>(
				(rv = new EmptyList()));
		
		// Return it
		return (List<V>)rv;
	}
}

