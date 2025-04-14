// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * List iterator which can go forwards and backwards through this abstract
 * list. Indexed elements are used here, not sequential lists.
 *
 * @param <E> The element type.
 * @since 2018/10/28
 */
final class __AbstractListListIterator__<E>
	implements ListIterator<E>
{
	/** The owning list. */
	protected final AbstractList<E> owner;
	
	/** The next element to be returned. */
	private int _next;
	
	/** The current modification count, to detect modifications. */
	private int _atmod;
	
	/** The index of the last next/previous, allows for removal. */
	private int _lastNextPrevIndex =
		-1;
	
	/**
	 * Initializes the list iterator.
	 *
	 * @param __owner The owner of this list.
	 * @param __i The index to use.
	 * @throws IndexOutOfBoundsException If the index is outside the list
	 * bounds.
	 * @since 2018/10/28
	 */
	__AbstractListListIterator__(AbstractList<E> __owner, int __i)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__owner == null)
			throw new NullPointerException("NARG");
		
		if (__i < 0 || __i > __owner.size())
			throw new IndexOutOfBoundsException("IOOB");
		
		this.owner = __owner;
		this._next = __i;
		this._atmod = __owner.modCount;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final void add(E __a)
	{
		// Check modification
		this.__checkConcurrent();
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final boolean hasNext()
	{
		// Check modification
		this.__checkConcurrent();
		
		// There are elements as long as the next one is below the size
		return this._next < this.owner.size();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final boolean hasPrevious()
	{
		// Check modification
		this.__checkConcurrent();
		
		// As long as this is not the first element there will be
		// previous ones
		return this._next > 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final E next()
		throws NoSuchElementException
	{
		// Check modification
		this.__checkConcurrent();
		
		// End of list?
		int next = this._next;
		if (this._next >= this.owner.size())
			throw new NoSuchElementException("NSEE");
		
		// Get this element
		E rv = this.owner.get(next);
		
		// Next one is after this, also the element to be removed is set
		// by this method
		this._lastNextPrevIndex = next;
		this._next = next + 1;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final int nextIndex()
	{
		// Check modification
		this.__checkConcurrent();
		
		return this._next;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final E previous()
		throws NoSuchElementException
	{
		// Check modification
		this.__checkConcurrent();
		
		// End of list?
		int next = this._next;
		if (this._next <= 0)
			throw new NoSuchElementException("NSEE");
		
		// Get this element
		int eldx = next - 1;
		E rv = this.owner.get(eldx);
		
		// The element to remove is the one we just got and the next one
		// is one down the list
		this._lastNextPrevIndex = eldx;
		this._next = eldx;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final int previousIndex()
	{
		// Check modification
		this.__checkConcurrent();
		
		// If next is zero then this would be -1
		return this._next - 1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final void remove()
	{
		// Check modification
		this.__checkConcurrent();
		
		/* {@squirreljme.error ZZ2c No previously returned element was
		iterated, it was already removed, or an element was added.} */
		int rmdx = this._lastNextPrevIndex;
		if (rmdx < 0)
			throw new IllegalStateException("ZZ2c");
		
		// Remove this index
		this._lastNextPrevIndex = -1;
		this.owner.remove(rmdx);
		
		// Next element would be moved down
		int next = this._next;
		if (next > rmdx)
			this._next = next - 1;

		// Set new modification count
		this._atmod = this.owner.modCount;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final void set(E __v)
	{
		// Check modification
		this.__checkConcurrent();
		
		/* {@squirreljme.error ZZ3z No previous call to next or previous was
		performed.} */
		int index = this._lastNextPrevIndex;
		if (index < 0)
			throw new IllegalStateException("ZZ2c");
		
		// Set the item
		this.owner.set(index, __v);
		
		// Set new modification count because the list has been changed
		this._atmod = this.owner.modCount;
	}
	
	/**
	 * Checks if the list was concurrently modified.
	 *
	 * @throws ConcurrentModificationException If it was modified.
	 * @since 2018/10/29
	 */
	private void __checkConcurrent()
		throws ConcurrentModificationException
	{
		/* {@squirreljme.error ZZ2d List has been concurrently modified.} */
		if (this._atmod != this.owner.modCount)
			throw new ConcurrentModificationException("ZZ2d");
	}
}

