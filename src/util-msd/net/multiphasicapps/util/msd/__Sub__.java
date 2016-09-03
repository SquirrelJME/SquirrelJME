// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.msd;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This is a sub-deque which is given access to the {@link MultiSetDeque}.
 *
 * @since 2016/09/03
 */
final class __Sub__<V>
	implements Deque<V>
{
	/** The list which acts as a queue. */
	final List<V> _deque;
	
	/** The items which are in this queue. */
	final Set<V> _set =
		new HashSet<>();
	
	/** The capacity limit. */
	final int _limit;
	
	/**
	 * Initializes the sub-queue.
	 *
	 * @param __msd The owning queue.
	 * @param __l The size limit of this queue.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	__Sub__(MultiSetDeque<V> __msd, int __l)
		throws NullPointerException
	{
		// Check
		if (__msd == null)
			throw new NullPointerException("NARG");
		
		// Set limit
		this._limit = __l;
		
		// Base on a list
		this._deque = new ArrayList<>();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean add(V __a)
		throws NullPointerException
	{
		// Remember old size
		List<V> deque = this._deque;
		int was = deque.size();
		
		// Add it
		addLast(__a);
		
		// If the size changed then it was added
		return deque.size() != was;
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean addAll(Collection<? extends V> __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Add everything
		boolean rv = false;
		for (V v : __a)
			rv |= add(v);
		
		// Has this changed?
		return rv;
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public void addFirst(V __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public void addLast(V __a)
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BZ02 Deque capacity would be exceeded.}
		List<V> deque = this._deque;
		if (deque.size() + 1 >= this._limit)
			throw new IllegalStateException("BZ02");
		
		// Do not add the element if it is already in this queue.
		Set<V> set = this._set;
		if (set.contains(__a))
			return;
		
		// Otherwise add it
		deque.add(__a);
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public void clear()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean contains(Object __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean containsAll(Collection<?> __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public Iterator<V> descendingIterator()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V element()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V getFirst()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V getLast()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean isEmpty()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public Iterator<V> iterator()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean offer(V __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean offerFirst(V __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean offerLast(V __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V peek()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V peekFirst()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V peekLast()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V poll()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V pollFirst()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V pollLast()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V pop()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public void push(V __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean remove(Object __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V remove()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean removeAll(Collection<?> __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V removeFirst()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean removeFirstOccurrence(Object __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V removeLast()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean removeLastOccurrence(Object __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean retainAll(Collection<?> __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public int size()
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public <T> T[] toArray(T[] __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public Object[] toArray()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Removes the item if it is in this deque.
	 *
	 * @param __v The item to remove.
	 * @return {@code true} if it was removed.
	 * @since 2016/09/03
	 */
	final boolean __remove(V __v)
	{
		// Not in this deque
		Set<V> set = this._set;
		if (!set.contains(__v))
			return false;
		
		// Otherwise remove it
		if (!this._deque.remove(__v))
			throw new RuntimeException("OOPS");
		
		// Remove from set
		set.remove(__v);
		
		// Did remove
		return true;
	}
}

