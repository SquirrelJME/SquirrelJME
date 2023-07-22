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
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

/**
 * This is a double-ended queue which is backed by an array, this grows
 * accordingly as elements are added and otherwise. This collection does
 * not allow {@code null} elements.
 *
 * @param <E> The type of element in the queue.
 * @since 2020/06/19
 */
@SuppressWarnings("UseOfClone")
@ImplementationNote("In SquirrelJME, the current implementation is naive " +
	"in that it uses an ArrayList, a more optimal solution should be added.")
@Api
public class ArrayDeque<E>
	extends AbstractCollection<E>
	implements Deque<E>, Cloneable
{
	/** The default capacity. */
	private static final int _DEFAULT_CAPACITY =
		16;
	
	/** The backing contents. */
	private final ArrayList<E> _elements;
	
	/**
	 * Initializes an empty queue with a default capacity of 16.
	 *
	 * @since 2020/06/19
	 */
	@Api
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
	@Api
	public ArrayDeque(int __initialCap)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error ZZxx Cannot have an initial capacity that is
		negative. (The initial capacity)} */
		if (__initialCap < 0)
			throw new IllegalArgumentException("ZZxx " + __initialCap);

		// Setup storage
		this._elements = new ArrayList<>(__initialCap);
	}

	/**
	 * Initializes the queue with the given collection, the first iterated
	 * element is at the front of the queue.
	 *
	 * @param __c The initial collection to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/19
	 */
	@Api
	public ArrayDeque(Collection<? extends E> __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Initialize empty list with the correct capacity
		int size = __c.size();
		ArrayList<E> elements = new ArrayList<>(size);
		
		// Check for null and add
		for (E element : __c)
		{
			if (element == null)
				throw new NullPointerException("NARG");
			
			elements.add(element);
		}
		
		this._elements = elements;
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
		
		this.__elementAdd(true, __v);
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
		
		this.__elementAdd(false, __v);
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

		this.__elementAdd(true, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public void clear()
	{
		this._elements.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/21
	 */
	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public ArrayDeque<E> clone()
	{
		// Just use copy constructor here, for simplicity
		return new ArrayDeque<>(this);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public boolean contains(Object __v)
	{
		return this._elements.contains(__v);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public Iterator<E> descendingIterator()
	{
		return this.__iterator(true);
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
		if (this._elements.isEmpty())
			throw new NoSuchElementException("NSEE");
		
		return this.__elementGet(false, false);
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
		if (this._elements.isEmpty())
			throw new NoSuchElementException("NSEE");
		
		return this.__elementGet(false, false);
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
		if (this._elements.isEmpty())
			throw new NoSuchElementException("NSEE");
		
		return this.__elementGet(true, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public Iterator<E> iterator()
	{
		return this.__iterator(false);
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

		this.__elementAdd(true, __v);
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

		this.__elementAdd(false, __v);
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
		
		this.__elementAdd(true, __v);
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
		if (this._elements.isEmpty())
			return null;
		
		return this.__elementGet(false, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E peekFirst()
	{
		// Queue is empty
		if (this._elements.isEmpty())
			return null;
		
		return this.__elementGet(false, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E peekLast()
	{
		// Queue is empty
		if (this._elements.isEmpty())
			return null;
		
		return this.__elementGet(true, false);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E poll()
	{
		// Queue is empty
		if (this._elements.isEmpty())
			return null;
		
		return this.__elementGet(false, true);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E pollFirst()
	{
		// Queue is empty
		if (this._elements.isEmpty())
			return null;
		
		return this.__elementGet(false, true);
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public E pollLast()
	{
		// Queue is empty
		if (this._elements.isEmpty())
			return null;
		
		return this.__elementGet(true, true);
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
		if (this._elements.isEmpty())
			throw new NoSuchElementException("NSEE");
		
		return this.__elementGet(false, true);
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

		this.__elementAdd(false, __v);
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
		if (this._elements.isEmpty())
			throw new NoSuchElementException("NSEE");
		
		return this.__elementGet(false, true);
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
		if (this._elements.isEmpty())
			throw new NoSuchElementException("NSEE");
		
		return this.__elementGet(false, true);
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
		
		return this.__removeFirst(this.__iterator(false), __v);
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
		if (this._elements.isEmpty())
			throw new NoSuchElementException("NSEE");
		
		return this.__elementGet(true, true);
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
		
		return this.__removeFirst(this.__iterator(true), __v);
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
		return this._elements.size();
	}
	
	/**
	 * Adds a value to the queue on a given side.
	 * 
	 * @param __rightSide Add to the right side? 
	 * @param __value The value to add.
	 * @throws NullPointerException On a null value.
	 * @since 2020/06/20
	 */
	private void __elementAdd(boolean __rightSide, E __value)
		throws NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		ArrayList<E> elements = this._elements;
		
		// Add to end
		if (__rightSide)
			elements.add(__value);
		
		// Add to start
		else
			elements.add(0, __value);
	}
	
	/**
	 * Removes an element from the queue.
	 * 
	 * @param __rightSide Remove from the right side?
	 * @param __delete Is the element to be deleted?
	 * @return The element to remove.
	 * @since 2020/06/20
	 */
	private E __elementGet(boolean __rightSide, boolean __delete)
	{
		ArrayList<E> elements = this._elements;
		
		/* {@squirreljme.error ZZ37 Get of element from an empty deque?} */
		int size = elements.size();
		if (size <= 0)
			throw new IllegalStateException("ZZ37");
		
		// From right?
		if (__rightSide)
		{
			if (__delete)
				return elements.remove(size - 1);
			else
				return elements.get(size - 1);
		}
		
		// From left?
		else
		{
			if (__delete)
				return elements.remove(0);
			else
				return elements.get(0);
		}
	}
	
	/**
	 * Returns the element iterator.
	 * 
	 * @param __descending Is this descending?
	 * @return The iterator.
	 * @since 2020/06/21
	 */
	private Iterator<E> __iterator(boolean __descending)
	{
		// If descending, use the list iterator but start from the end
		ArrayList<E> elements = this._elements;
		if (__descending)
			return new __DescendingIteratorViaListIterator__<E>(
				elements.listIterator(elements.size()));
		
		// Use normal iterator, do not expose the ListIterator!
		else
			return new __HideIterator__<E>(elements.iterator());
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
