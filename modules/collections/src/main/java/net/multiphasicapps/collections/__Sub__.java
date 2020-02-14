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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This is a sub-deque which is given access to the {@link MultiSetDeque}.
 *
 * @since 2016/09/03
 */
final class __Sub__<V>
	implements Deque<V>
{
	/** The owning multi-set. */
	final MultiSetDeque<V> _msd;
	
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
		
		// Set
		this._msd = __msd;
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
		throw new todo.TODO();
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
		
		// {@squirreljme.error AC09 Deque capacity would be exceeded.}
		List<V> deque = this._deque;
		if (deque.size() + 1 >= this._limit)
			throw new IllegalStateException("AC09");
		
		// Do not add the element if it is already in this queue.
		Set<V> set = this._set;
		if (set.contains(__a))
			return;
		
		// Otherwise add it
		deque.add(__a);
		set.add(__a);
		this._msd._master.add(__a);
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public void clear()
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean contains(Object __a)
	{
		return this._deque.contains(__a);
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean containsAll(Collection<?> __a)
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public Iterator<V> descendingIterator()
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V element()
	{
		return getFirst();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean equals(Object __a)
	{
		
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V getFirst()
	{
		// {@squirreljme.error AC0a The deque is empty.}
		List<V> deque = this._deque;
		if (deque.size() <= 0)
			throw new NoSuchElementException("AC0a");
		
		// Just get it
		return deque.get(0);
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V getLast()
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public int hashCode()
	{
		return this._deque.hashCode();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean isEmpty()
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public Iterator<V> iterator()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean offer(V __a)
	{
		return offerLast(__a);
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean offerFirst(V __a)
	{
		try
		{
			addFirst(__a);
			return true;
		}
		
		// Exceeds capacity
		catch (IllegalStateException e)
		{
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean offerLast(V __a)
	{
		try
		{
			addLast(__a);
			return true;
		}
		
		// Exceeds capacity
		catch (IllegalStateException e)
		{
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V peek()
	{
		return peekFirst();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V peekFirst()
	{
		try
		{
			return getFirst();
		}
		
		// Empty
		catch (NoSuchElementException e)
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V peekLast()
	{
		try
		{
			return getLast();
		}
		
		// Empty
		catch (NoSuchElementException e)
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V poll()
	{
		return pollFirst();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V pollFirst()
	{
		try
		{
			return removeFirst();
		}
		
		// Empty
		catch (NoSuchElementException e)
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V pollLast()
	{
		try
		{
			return removeLast();
		}
		
		// Empty
		catch (NoSuchElementException e)
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V pop()
	{
		return removeFirst();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public void push(V __a)
	{
		addFirst(__a);
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean remove(Object __a)
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V remove()
	{
		return removeFirst();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean removeAll(Collection<?> __a)
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V removeFirst()
		throws NoSuchElementException
	{
		// {@squirreljme.error AC0b The deque is empty.}
		List<V> deque = this._deque;
		if (deque.size() <= 0)
			throw new NoSuchElementException("AC0b");
		
		// Remove it
		V rv = deque.remove(0);
		
		// The value is gone from this collection now so remove it
		Set<V> set = this._set;
		set.remove(rv);
		
		// Remove from all over queues
		this._msd.remove(rv);
		
		// Return
		return rv;
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean removeFirstOccurrence(Object __a)
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public V removeLast()
		throws NoSuchElementException
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean removeLastOccurrence(Object __a)
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public boolean retainAll(Collection<?> __a)
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public int size()
	{
		return this._deque.size();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public <T> T[] toArray(T[] __a)
	{
		return this._deque.<T>toArray(__a);
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public Object[] toArray()
	{
		return this._deque.toArray();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/25
	 */
	@Override
	public String toString()
	{
		return this._deque.toString();
	}
	
	/**
	 * Quick clear of the queue.
	 *
	 * @since 2017/03/25
	 */
	final void __clear()
	{
		this._deque.clear();
		this._set.clear();
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
			throw new todo.OOPS();
		
		// Remove from set
		set.remove(__v);
		
		// Did remove
		return true;
	}
}

