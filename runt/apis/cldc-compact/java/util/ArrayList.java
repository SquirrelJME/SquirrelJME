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
		// {@squirreljme.error ZZ0w Attempt to initialize array list with
		// a negative capacity. (The capicity)}
		if (__cap < 0)
			throw new IllegalArgumentException(
				String.format("ZZ0w %d", __cap));
		
		this._elements = (E[])new Object[__cap];
	}
	
	public ArrayList(Collection<? extends E> __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean add(E __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public void add(int __a, E __b)
	{
		throw new todo.TODO();
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
	
	@Override
	public void clear()
	{
		throw new todo.TODO();
	}
	
	@Override
	public Object clone()
	{
		throw new todo.TODO();
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
	
	@Override
	public E get(int __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public int indexOf(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean isEmpty()
	{
		throw new todo.TODO();
	}
	
	@Override
	public Iterator<E> iterator()
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
	
	@Override
	public E remove(int __a)
	{
		throw new todo.TODO();
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
	
	@Override
	public Object[] toArray()
	{
		throw new todo.TODO();
	}
	
	@Override
	public <T> T[] toArray(T[] __a)
	{
		throw new todo.TODO();
	}
	
	public void trimToSize()
	{
		throw new todo.TODO();
	}
}

