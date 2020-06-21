// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Iterator for {@link ArrayDeque<E>}.
 *
 * @param <E> The type used.
 * @since 2020/06/20
 */
class __ArrayDequeIterator__<E>
	implements Iterator<E>
{
	/** The queue to check against. */
	private final ArrayDeque<E> _deque;
	
	/** Are we descending. */
	private final boolean _descending;
	
	/** The elements to iterate. */
	private final E[] _elements;
	
	/** The current modification count. */
	private final __ModCounter__ _dequeModCount;
	
	/** The current valid modification count. */
	private int _validModCount;
	
	/** Which element are we currently at? */
	private int _at;
	
	/** How many elements do we have left? */
	private int _elementsLeft;
	
	/** The last processed index, for removing. */
	private int _lastAt;
	
	/**
	 * Initializes the iterator;
	 * 
	 * @param __elements The elements in the queue.
	 * @param __size The size of the deque.
	 * @param __start The starting point.
	 * @param __end The ending point.
	 * @param __modCount The modification counter.
	 * @param __deque The deque to check.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/20
	 */
	public __ArrayDequeIterator__(E[] __elements, int __size, int __start,
		int __end, __ModCounter__ __modCount, ArrayDeque<E> __deque)
		throws NullPointerException
	{
		if (__elements == null || __modCount == null || __deque == null)
			throw new NullPointerException("NARG");
		
		// Are we ascending or descending?
		boolean descending = (__start > __end);
		this._descending = descending;
		this._at = (descending ? __end : __start);
		
		this._elements = __elements;
		this._elementsLeft = __size;
		this._deque = __deque;
		
		// Setup valid counter
		this._dequeModCount = __modCount;
		this._validModCount = __modCount.modCount;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public boolean hasNext()
	{
		return this._elementsLeft > 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E next()
		throws NoSuchElementException
	{
		// Ran out of elements?
		int elementsLeft = this._elementsLeft;
		if (elementsLeft <= 0)
			throw new NoSuchElementException("NSEE");
		
		// Check if the deque changed
		this.__checkModification();
		
		// Next the next element
		int at = this._at;
		E rv = this._elements[at];
		
		// Store last index, is used for deletion
		this._lastAt = at;
		
		// Set new position
		this._at = (this._descending ? at - 1 : at + 1);
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public void remove()
		throws IllegalStateException 
	{
		// We must have previously got an element we want to delete
		int lastAt = this._lastAt;
		if (lastAt < 0)
			throw new IllegalStateException("ITNR");
		
		// Check if the deque changed
		this.__checkModification();
		
		if (true)
			throw Debugging.todo();
		
		// Removing is no longer valid
		this._lastAt = -1;
	}
	
	/**
	 * Checks the modification count.
	 * 
	 * @throws ConcurrentModificationException If the count was modified.
	 * @since 2020/06/20
	 */
	private void __checkModification()
		throws ConcurrentModificationException
	{
		// Check for invalid modification
		if (this._validModCount != this._dequeModCount.modCount)
			throw new ConcurrentModificationException("CCME");
	}
}
