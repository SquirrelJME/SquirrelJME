// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a resizeable list which is backed by an array.
 *
 * This class is not thread safe, for a thread safe version see {@link Vector}.
 *
 * If a modification is detected in the iterator then
 * {@link ConcurrentModificationException} is thrown as soon as possible.
 *
 * @param <E> The element type.
 * @see Vector
 * @since 2018/09/15
 */
@Api
public class ArrayList<E>
	extends AbstractList<E>
	implements List<E>, RandomAccess, Cloneable
{
	/** Capacity growth size. */
	private static final int _GROWTH =
		8;
	
	/** Elements in the list. */
	private E[] _elements;
	
	/** The number of elements in the list. */
	private int _size;
	
	/**
	 * Initializes a list which has an initial capacity of 10.
	 *
	 * @since 2018/09/15
	 */
	@Api
	public ArrayList()
	{
		this(10);
	}
	
	/**
	 * Initializes a list which has the specified capacity.
	 *
	 * @param __cap The initial capacity.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2018/09/15
	 */
	@SuppressWarnings({"unchecked"})
	public ArrayList(int __cap)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error ZZ2e Attempt to initialize array list with
		a negative capacity. (The capacity)} */
		if (__cap < 0)
			throw new IllegalArgumentException(
				String.format("ZZ2e %d", __cap));
		
		this._elements = (E[])new Object[__cap];
	}
	
	/**
	 * Initializes a list which has all the elements of the given collection
	 * in its iteration order.
	 *
	 * @param __o The other collection.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/10
	 */
	@SuppressWarnings({"unchecked"})
	public ArrayList(Collection<? extends E> __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Initial capacity is the size of the other collection
		this._elements = (E[])new Object[__o.size()];
		
		// And then add everything
		this.addAll(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public void add(int __i, E __v)
		throws IndexOutOfBoundsException
	{
		int size = this._size;
		if (__i < 0 || __i > size)
			throw new IndexOutOfBoundsException("IOOB");
		
		E[] elements = this._elements;
		int cap = elements.length,
			nextsize = size + 1;
		
		// Cannot fit in this array
		E[] source = elements;
		boolean resize = (nextsize > cap);
		if (resize)
		{
			// Grow the list by a bit
			int newcap = nextsize + ArrayList._GROWTH;
			elements = (E[])new Object[newcap];
			
			// Copy old stuff over, but only up to the index as needed
			System.arraycopy(source, 0, elements, 0, __i);
		}
		
		// Move down to fit
		for (int i = size - 1, o = size; o > __i; i--, o--)
			elements[o] = source[i];
		
		// Store data here
		elements[__i] = __v;
		
		// Did the array change?
		this._size = nextsize;
		if (elements != source)
			this._elements = elements;
			
		// If we resized the array, clear everything from the old array
		// This is so everything gets garbage collected
		if (nextsize > cap)
			for (int i = 0, n = source.length; i < n; i++)
				source[i] = null;
		
		// Structurally modified
		this.modCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public void clear()
	{
		// Clear the entire array so things get collected
		E[] elements = this._elements;
		int size = this._size;
		for (int i = 0; i < size; i++)
			elements[i] = null;
		
		// Keep the backing array, just clear the size
		this._size = 0;
		
		// Structurally modified
		this.modCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public Object clone()
	{
		return new ArrayList<>(this);
	}
	
	/**
	 * Ensures that the given number of elements can fit in the list.
	 *
	 * @param __n The number of elements.
	 * @since 2019/05/14
	 */
	@Api
	@SuppressWarnings({"unchecked"})
	public void ensureCapacity(int __n)
	{
		// Pointless
		if (__n <= 0)
			return;
		
		// Meets or exceeds the desired capacity?
		E[] elements = this._elements;
		int nowl = elements.length;
		if (__n <= nowl)
			return;
		
		// Copy values over
		E[] extra = (E[])new Object[__n];
		System.arraycopy(elements, 0, extra, 0, nowl);
		
		// Set
		this._elements = extra;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public E get(int __i)
		throws IndexOutOfBoundsException
	{
		if (__i < 0 || __i >= this._size)
			throw new IndexOutOfBoundsException("IOOB");
		
		return this._elements[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/12
	 */
	@Override
	public E remove(int __i)
		throws IndexOutOfBoundsException
	{
		// Out of bounds?
		int size = this._size;
		if (__i < 0 || __i >= size)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Get the old element first
		E[] elements = this._elements;
		E rv = elements[__i];
		
		// Copy all of the elements down
		for (int o = __i, i = __i + 1; i < size; o++, i++)
			elements[o] = elements[i];
		elements[size - 1] = null;
		
		// Set new size
		this._size = size - 1;
		
		// Structurally modified
		this.modCount++;
		
		// And the old element
		return rv;
	}
	
	@Override
	protected void removeRange(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/10
	 */
	@Override
	public E set(int __i, E __v)
	{
		// Out of bounds?
		int size = this._size;
		if (__i < 0 || __i >= size)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Read old value
		E[] elements = this._elements;
		E rv = elements[__i];
		
		// Set new value
		elements[__i] = __v;
		
		// Return old
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public int size()
	{
		return this._size;
	}
	
	@Api
	public void trimToSize()
	{
		throw Debugging.todo();
	}
}

