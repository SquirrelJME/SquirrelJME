// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is a resizeable list which is backed by an array.
 *
 * This class is not thread safe, for a thread safe version see {@link Vector}.
 *
 * If a modification is detected in the iterator then
 * {@link #ConcurrentModificationException} is thrown as soon as possible.
 *
 * @param <E> The element type.
 * @see Vector
 * @since 2018/09/15
 */
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
		// {@squirreljme.error ZZ1m Attempt to initialize array list with
		// a negative capacity. (The capicity)}
		if (__cap < 0)
			throw new IllegalArgumentException(
				String.format("ZZ1m %d", __cap));
		
		this._elements = (E[])new Object[__cap];
	}
	
	public ArrayList(Collection<? extends E> __a)
	{
		throw new todo.TODO();
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
		if (nextsize > cap)
		{
			// Grow the list by a bit
			int newcap = nextsize + _GROWTH;
			elements = (E[])new Object[newcap];
			
			// Copy old stuff over, but only up to the index as needed
			for (int i = 0; i < __i; i++)
				elements[i] = source[i];
		}
		
		// Move down to fit
		for (int i = size - 1, o = size; o > __i; i--, o--)
			elements[o] = source[i];
		
		// Store data here
		elements[__i] = __v;
		
		// Store new information
		this._size = nextsize;
		if (elements != source)
			this._elements = elements;
		
		// Structurally modified
		this.modCount++;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean addAll(int __a, Collection<? extends E> __b)
	{
		throw new todo.TODO();
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
	
	@Override
	public boolean contains(Object __a)
	{
		throw new todo.TODO();
	}
	
	public void ensureCapacity(int __a)
	{
		throw new todo.TODO();
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
	
	@Override
	public int indexOf(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public int lastIndexOf(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public ListIterator<E> listIterator(int __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public ListIterator<E> listIterator()
	{
		throw new todo.TODO();
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
		
		// And the old element
		return rv;
	}
	
	@Override
	public boolean remove(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean removeAll(Collection<?> __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	protected void removeRange(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean retainAll(Collection<?> __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public E set(int __a, E __b)
	{
		throw new todo.TODO();
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
	
	@Override
	public List<E> subList(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public void trimToSize()
	{
		throw new todo.TODO();
	}
}

