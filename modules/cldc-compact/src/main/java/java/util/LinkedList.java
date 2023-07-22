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
 * A linked list is a list of items which are held together using chains. Each
 * value for an element is placed within a chain link which is then held to
 * other links in the chain. This is a doubly linked list.
 *
 * This class has efficient insertion and removal via the iterator interfaces.
 *
 * Random access is not efficient and the further away the element is from the
 * initial sequence the more elements will need to be skipped to access the
 * data.
 *
 * This class is not thread safe.
 *
 * @param <E> The type of element to store.
 * @since 2016/09/05
 */
@ImplementationNote("For simplicity this uses base anchor nodes for the " +
	"head and tail of the list.")
@Api
public class LinkedList<E>
	extends AbstractSequentialList<E>
	implements List<E>, Deque<E>, Cloneable
{
	/** The list head. */
	final __Link__<E> _head =
		new __Link__<E>(null, null, null);
	
	/** The list tail, this gets linked into the head. */
	final __Link__<E> _tail =
		new __Link__<E>(this._head, null, null);
		
	/** The number of entries in the list. */
	int _size;
	
	/**
	 * Initializes a linked list with no entries.
	 *
	 * @since 2016/09/05
	 */
	@Api
	public LinkedList()
	{
	}
	
	/**
	 * Initializes a linked list which contains a copy of all of the elements
	 * of the other collection in its iterator order.
	 *
	 * @param __a The collection to copy elements from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/05
	 */
	@Api
	public LinkedList(Collection<? extends E> __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Just call addAll
		this.addAll(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public void addFirst(E __v)
	{
		this.add(0, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public void addLast(E __v)
	{
		this.add(this._size, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/05
	 */
	@Override
	public void clear()
	{
		// Since there are anchor entries used for the linked list we can just
		// set the anchor points to each other
		__Link__<E> head = this._head,
			tail = this._tail;
		head._next = tail;
		tail._prev = head;
		
		// Clear size
		this._size = 0;
		
		// List was modified
		LinkedList.this.modCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public Object clone()
	{
		return new LinkedList<>(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public Iterator<E> descendingIterator()
	{
		return new __DescendingIteratorViaListIterator__<E>(
			new __LinkedListListIterator__<E>(this, this._size));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E element()
		throws NoSuchElementException
	{
		return this.getFirst();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E getFirst()
		throws NoSuchElementException
	{
		/* {@squirreljme.error ZZ2s Cannot get the first element because the
		linked list is empty.} */
		if (this._size == 0)
			throw new NoSuchElementException("ZZ2s");
		
		// One forward from the head
		return this._head._next._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E getLast()
	{
		/* {@squirreljme.error ZZ2t Cannot get the last element because the
		linked list is empty.} */
		int sz;
		if ((sz = this._size) == 0)
			throw new NoSuchElementException("ZZ2t");
		
		// Just one back from the tail
		return this._tail._prev._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public ListIterator<E> listIterator(int __i)
	{
		return new __LinkedListListIterator__<E>(this, __i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean offer(E __v)
	{
		return this.offerLast(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean offerFirst(E __v)
	{
		// There are no capacity restrictions
		this.add(0, __v);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public boolean offerLast(E __v)
	{
		// There are no capacity restrictions
		this.add(this._size, __v);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E peek()
	{
		return this.peekFirst();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E peekFirst()
	{
		// Return null on empty list
		if (this._size == 0)
			return null;
		
		// One forward from the head
		return this._head._next._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E peekLast()
	{
		// Return null if the list is empty
		int sz;
		if ((sz = this._size) == 0)
			return null;
		
		// Just one back from the tail
		return this._tail._prev._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E poll()
	{
		return this.pollFirst();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E pollFirst()
	{
		if (this._size == 0)
			return null;
		
		ListIterator<E> it = this.listIterator(0);
		
		// Remove the first element
		E rv = it.next();
		it.remove();
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E pollLast()
	{
		if (this._size == 0)
			return null;
		
		ListIterator<E> it = this.listIterator(this._size);
		
		// Remove the last element
		E rv = it.previous();
		it.remove();
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E pop()
	{
		return this.removeFirst();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public void push(E __v)
	{
		this.addFirst(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E remove()
	{
		return this.removeFirst();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E removeFirst()
		throws NoSuchElementException
	{
		if (this._size == 0)
			throw new NoSuchElementException("NSEE");
		
		ListIterator<E> it = this.listIterator(0);
		
		// Remove the first element
		E rv = it.next();
		it.remove();
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean removeFirstOccurrence(Object __a)
	{
		// Start at the start of the list and remove the first match
		Iterator<E> rover = this.iterator();
		while (rover.hasNext())
		{
			E v = rover.next();
			
			// If this is the same object then remove it
			if (Objects.equals(v, __a))
			{
				rover.remove();
				return true;
			}
		}
		
		// Nothing was removed
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public E removeLast()
	{
		if (this._size == 0)
			throw new NoSuchElementException("NSEE");
		
		ListIterator<E> it = this.listIterator(this._size);
		
		// Remove the last element
		E rv = it.previous();
		it.remove();
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean removeLastOccurrence(Object __a)
	{
		// Start at the end of the list and remove the first match
		ListIterator<E> rover = this.listIterator(this._size);
		while (rover.hasPrevious())
		{
			E v = rover.previous();
			
			// If this is the same object then remove it
			if (Objects.equals(v, __a))
			{
				rover.remove();
				return true;
			}
		}
		
		// Nothing was removed
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public int size()
	{
		return this._size;
	}
}

