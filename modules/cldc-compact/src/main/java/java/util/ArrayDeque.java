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

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
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
@ImplementationNote("In SquirrelJME, this is implemented by having a single" +
	"array managed by pivots for the left and right sides.")
public class ArrayDeque<E>
	extends AbstractCollection<E>
	implements Deque<E>, Cloneable
{
	/** The default capacity. */
	private static final int _DEFAULT_CAPACITY =
		16;

	/** The modification count of this queue. */
	final __ModCounter__ _modCount =
		new __ModCounter__();
	
	/** The deque storage. */
	final __ArrayDequeStore__<E> _store =
		new __ArrayDequeStore__<E>(this._modCount);

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

		// Setup arrays
		this._store.__ensureCapacity(__initialCap,
			false, false);
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
		
		__ArrayDequeStore__<E> store = this._store;
		
		// Setup arrays
		int size = __c.size();
		store.__ensureCapacity(size,
			false, true);
		
		// Add elements
		for (E item : __c)
		{
			if (item == null)
				throw new NullPointerException("NARG");
			
			store.__elementAdd(true, item);
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public boolean add(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		this._store.__elementAdd(true, __v);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public void addFirst(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		this._store.__elementAdd(false, __v);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public void addLast(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");

		this._store.__elementAdd(true, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public void clear()
	{
		this._store.__clear();
	}

	@Override
	public ArrayDeque<E> clone()
	{
		throw Debugging.todo();
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public boolean contains(Object __v)
	{
		// Will never contain null or if it is empty
		if (__v == null || this._store._size <= 0)
			return false;
		
		for (Iterator<E> it = this._store.__iterator(false);
			it.hasNext(); )
		{
			E item = it.next();
			
			if (Objects.equals(item, __v))
				return true;
		}
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public Iterator<E> descendingIterator()
	{
		return this._store.__iterator(true);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E element()
		throws NoSuchElementException
	{
		// Queue is empty
		if (this._store._size == 0)
			throw new NoSuchElementException("NSEE");
		
		return this._store.__elementGet(false, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E getFirst()
		throws NoSuchElementException
	{
		// Queue is empty
		if (this._store._size == 0)
			throw new NoSuchElementException("NSEE");
		
		return this._store.__elementGet(false, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E getLast()
		throws NoSuchElementException
	{
		// Queue is empty
		if (this._store._size == 0)
			throw new NoSuchElementException("NSEE");
		
		return this._store.__elementGet(true, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public Iterator<E> iterator()
	{
		return this._store.__iterator(false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public boolean offer(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");

		this._store.__elementAdd(true, __v);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public boolean offerFirst(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");

		this._store.__elementAdd(false, __v);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public boolean offerLast(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		this._store.__elementAdd(true, __v);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E peek()
	{
		// Queue is empty
		if (this._store._size == 0)
			return null;
		
		return this._store.__elementGet(false, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E peekFirst()
	{
		// Queue is empty
		if (this._store._size == 0)
			return null;
		
		return this._store.__elementGet(false, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E peekLast()
	{
		// Queue is empty
		if (this._store._size == 0)
			return null;
		
		return this._store.__elementGet(true, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E poll()
	{
		// Queue is empty
		if (this._store._size == 0)
			return null;
		
		return this._store.__elementGet(false, true);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E pollFirst()
	{
		// Queue is empty
		if (this._store._size == 0)
			return null;
		
		return this._store.__elementGet(false, true);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E pollLast()
	{
		// Queue is empty
		if (this._store._size == 0)
			return null;
		
		return this._store.__elementGet(true, true);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E pop()
		throws NoSuchElementException
	{
		// Queue is empty
		if (this._store._size == 0)
			throw new NoSuchElementException("NSEE");
		
		return this._store.__elementGet(false, true);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public void push(E __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");

		this._store.__elementAdd(false, __v);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E remove()
		throws NoSuchElementException
	{
		// Queue is empty
		if (this._store._size == 0)
			throw new NoSuchElementException("NSEE");
		
		return this._store.__elementGet(false, true);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E removeFirst()
		throws NoSuchElementException
	{
		// Queue is empty
		if (this._store._size == 0)
			throw new NoSuchElementException("NSEE");
		
		return this._store.__elementGet(false, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public boolean removeFirstOccurrence(Object __v)
	{
		// Will never contain null
		if (__v == null)
			return false;
		
		return this.__removeFirst(
			this._store.__iterator(false), __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E removeLast()
		throws NoSuchElementException
	{
		// Queue is empty
		if (this._store._size == 0)
			throw new NoSuchElementException("NSEE");
		
		return this._store.__elementGet(true, true);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public boolean removeLastOccurrence(Object __v)
	{
		// Will never contain null
		if (__v == null)
			return false;
		
		return this.__removeFirst(
			this._store.__iterator(true), __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public int size()
	{
		// Use the pre-cached size, since we cannot use the pivot positions
		// as it would be confusing for empty dequeue
		return this._store._size;
	}
	
	/**
	 * Removes the first occurrence of an item with an iterator.
	 * 
	 * @param __iterator The iterator.
	 * @param __v The item to remove.
	 * @return If it was found and removed.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/20
	 */
	private boolean __removeFirst(Iterator<E> __iterator, Object __v)
		throws NullPointerException
	{
		if (__iterator == null)
			throw new NullPointerException("NARG");
		
		// Go through every element
		while (__iterator.hasNext())
		{
			E element = __iterator.next();
			
			// Was this found?
			if (Objects.equals(element, __v))
			{
				__iterator.remove();
				return true;
			}
		}
		
		// Was not found
		return false;
	}
}
