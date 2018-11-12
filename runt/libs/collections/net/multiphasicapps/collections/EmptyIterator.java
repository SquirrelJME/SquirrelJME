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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is an iterator which is empty.
 *
 * @since 2016/04/10
 */
public final class EmptyIterator
	implements Iterator
{
	/** The empty iterator. */
	private static Reference<Iterator> _EMPTY_ITERATOR;
	
	/**
	 * Initializes the empty iterator.
	 *
	 * @since 2016/04/10
	 */
	private EmptyIterator()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public boolean hasNext()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public Object next()
	{
		// {@squirreljme.error AC02 The empty iterator contains no elements.}
		throw new NoSuchElementException("AC02");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public void remove()
	{
		// {@squirreljme.error AC03 Cannot remove elements from the empty
		// iterator.}
		throw new UnsupportedOperationException("AC03");
	}
	
	/**
	 * This returns an iterator which contains nothing.
	 *
	 * @param <V> The type of values to iterate over.
	 * @return The empty iterator.
	 * @since 2016/04/10
	 */
	@SuppressWarnings({"unchecked"})
	public static <V> Iterator<V> empty()
	{
		// Get reference
		Reference<Iterator> ref = _EMPTY_ITERATOR;
		Iterator rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
			_EMPTY_ITERATOR = new WeakReference<>(
				(rv = new EmptyIterator()));
		
		// Return it
		return (Iterator<V>)rv;
	}
}

