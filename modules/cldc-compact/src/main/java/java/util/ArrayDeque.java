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

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a double-ended queue which is backed by an array, this grows
 * accordingly as elements are added and otherwise. This collection does
 * not allow {@code null} elements.
 *
 * @param <E> The type of element in the queue.
 * @since 2020/06/19
 */
@SuppressWarnings("UseOfClone")
public class ArrayDeque<E>
	extends AbstractCollection<E>
	implements Deque<E>, Cloneable
{
	/** The default capacity. */
	private static final int _DEFAULT_CAPACITY =
		16;
	
	/** The modification count of this queue. */
	private int _modCount;
	
	/**
	 * Initializes an empty queue with a default capacity of 16.
	 *
	 * @since 2020/06/19
	 */
	public ArrayDeque()
	{
		this(ArrayDeque._DEFAULT_CAPACITY);
	}
	
	/**
	 * Initializes an empty queue with the given initial capacity.
	 *
	 * @param __initialCap The initial capacity.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2020/06/19
	 */
	public ArrayDeque(int __initialCap)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZxx Cannot have an initial capacity that is
		// negative. (The initial capacity)}
		if (__initialCap < 0)
			throw new IllegalArgumentException("ZZxx " + __initialCap);
		
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the queue with the given collection, the first iterated
	 * element is at the front of the queue.
	 *
	 * @param __c The initial collection to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/19
	 */
	public ArrayDeque(Collection<? extends E> __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	@Override
	public boolean add(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	@Override
	public void addFirst(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	@Override
	public void addLast(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	@Override
	public void clear()
	{
		throw Debugging.todo();
	}
	
	@Override
	public ArrayDeque<E> clone()
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean contains(Object __v)
	{
		// Will never contain null
		if (__v == null)
			return false;
		
		throw Debugging.todo();
	}
	
	@Override
	public Iterator<E> descendingIterator()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E element()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E getFirst()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E getLast()
	{
		throw Debugging.todo();
	}
	
	@Override
	public Iterator<E> iterator()
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean offer(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	@Override
	public boolean offerFirst(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	@Override
	public boolean offerLast(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	@Override
	public E peek()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E peekFirst()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E peekLast()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E poll()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E pollFirst()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E pollLast()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E pop()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void push(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	@Override
	public E remove()
	{
		throw Debugging.todo();
	}
	
	@Override
	public E removeFirst()
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean removeFirstOccurrence(Object __v)
	{
		// Will never contain null
		if (__v == null)
			return false;
		
		throw Debugging.todo();
	}
	
	@Override
	public E removeLast()
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean removeLastOccurrence(Object __v)
	{
		// Will never contain null
		if (__v == null)
			return false;
		
		throw Debugging.todo();
	}
	
	@Override
	public int size()
	{
		throw Debugging.todo();
	}
}

