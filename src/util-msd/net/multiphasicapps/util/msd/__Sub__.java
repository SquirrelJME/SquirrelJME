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
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/**
 * This is a sub-deque which is given access to the {@link MultiSetDeque}.
 *
 * @since 2016/09/03
 */
final class __Sub__<V>
	implements Deque<V>
{
	/** The base queue. */
	final Deque<V> _deque;
	
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
		
		// Initialize the queue
		if (__l == Integer.MAX_VALUE)
			this._deque = new ArrayDeque<>();
		else
			this._deque = new ArrayDeque<>(__l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean add(V __a)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean addAll(Collection<? extends V> __a)
	{
		throw new Error("TODO");
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
		throw new Error("TODO");
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
}

