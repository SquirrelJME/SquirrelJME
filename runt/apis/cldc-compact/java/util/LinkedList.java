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
public class LinkedList<E>
	extends AbstractSequentialList<E>
	implements List<E>, Deque<E>, Cloneable
{
	/** The number of entries in the list. */
	private int _size;
	
	/** The list head. */
	private final __Link__<E> _head =
		new __Link__<E>(null, null, null);
	
	/** The list tail, this gets linked into the head. */
	private final __Link__<E> _tail =
		new __Link__<E>(this._head, null, null);
	
	/**
	 * Initializes a linked list with no entries.
	 *
	 * @since 2016/09/05
	 */
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
		throw new todo.TODO();
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
		// {@squirreljme.error ZZ1x Cannot get the first element because the
		// linked list is empty.}
		if (this._size == 0)
			throw new NoSuchElementException("ZZ1x");
		
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
		// {@squirreljme.error ZZ1y Cannot get the last element because the
		// linked list is empty.}
		int sz;
		if ((sz = this._size) == 0)
			throw new NoSuchElementException("ZZ1y");
		
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
		return new __ListIterator__(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean offer(E __v)
	{
		return offerLast(__v);
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
		if ((sz = _size) == 0)
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
		if (this.isEmpty())
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
		try
		{
			return this.removeLast();
		}
		
		// Is empty
		catch (NoSuchElementException e)
		{
			return null;
		}
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
		if (this.isEmpty())
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
		Iterator<E> rover = iterator();
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public boolean removeLastOccurrence(Object __a)
	{
		// Start at the end of the list and remove the first match
		ListIterator<E> rover = listIterator(_size);
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
	
	/**
	 * The list iterator for this linked list.
	 *
	 * @since 2018/10/29
	 */
	private final class __ListIterator__
		implements ListIterator<E>
	{
		/** The virtualized index for the list (estimated). */
		private int _vdx;
		
		/** Last virtualized index. */
		private int _lastvdx;
		
		/** The current link the list is at. */
		private __Link__<E> _next;
		
		/** The last element, for removal or setting */
		private __Link__<E> _last;
		
		/** The current modification count, to detect modifications. */
		private int _atmod =
			LinkedList.this.modCount;
		
		/**
		 * Initializes the iterator starting at the given index.
		 *
		 * @param __i The index to start at.
		 * @throws IndexOutOfBoundsException If the index is outside of the
		 * list bounds.
		 * @since 2018/10/29
		 */
		__ListIterator__(int __i)
			throws IndexOutOfBoundsException
		{
			int size = LinkedList.this._size;
			if (__i < 0 || __i > size)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Closer to the start of the list
			__Link__<E> rover;
			if (__i < (size >> 1))
			{
				rover = LinkedList.this._head;
				
				for (int i = -1; i < __i; i++)
					rover = rover._next;
			}
			
			// Closer to the end of the list
			else
			{
				rover = LinkedList.this._tail;
				
				for (int i = size; i > __i; i--)
					rover = rover._prev;
			}
			
			// Store start information
			this._next = rover;
			this._vdx = __i;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/29
		 */
		@Override
		public final void add(E __v)
		{
			// Check modification
			this.__checkConcurrent();
			
			// These will both be adjusted
			int vdx = this._vdx;
			__Link__<E> next = this._next;
			
			// The documentation specifies that the object is inserted
			// before the implicit cursor, which means the next call to
			// previous will return the new element. Next would just return
			// the same element no matter how many elements are added.
			// So it is inserted between the previous and our current next
			new __Link__<E>(next._prev, __v, next);
			
			// Since we inserted an item, vdx goes up
			this._vdx++;
			
			// Size also goes up
			LinkedList.this._size++;
			
			// Set list as being modified and update our count to match
			this._atmod = ++LinkedList.this.modCount;
			
			// Cannot remove or set
			this._last = null;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/29
		 */
		@Override
		public final boolean hasNext()
		{
			// Check modification
			this.__checkConcurrent();
			
			// There is a next as long as the current element is before the
			// size
			return this._vdx < LinkedList.this._size;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/29
		 */
		@Override
		public final boolean hasPrevious()
		{
			// Check modification
			this.__checkConcurrent();
			
			// There is a previous as long as this is not the first element
			return this._vdx > 0;
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
			
			// We are at the tail node, do nothing
			__Link__<E> next = this._next;
			if (next == LinkedList.this._tail)
				throw new NoSuchElementException("NSEE");
			
			// Removal and set can be done on this
			this._last = next;
			
			// Iterate and record for the next element
			this._lastvdx = this._vdx++;
			this._next = next._next;
			
			// Return the current value
			return next._value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/29
		 */
		@Override
		public final int nextIndex()
		{
			// Check modification
			this.__checkConcurrent();
			
			// Virtual index should match this one
			return this._vdx;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/29
		 */
		@Override
		public final E previous()
		{
			// Check modification
			this.__checkConcurrent();
			
			// We are at the head node, do nothing
			__Link__<E> next = this._next;
			if (next == LinkedList.this._head)
				throw new NoSuchElementException("NSEE");
			
			// Move to previous
			__Link__<E> prev = next._prev;
			
			// Removal is done on this
			this._last = prev;
			
			// Move index back
			this._lastvdx = --this._vdx;
			this._next = prev;
			
			// Use previous value
			return prev._value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/29
		 */
		@Override
		public final int previousIndex()
		{
			// Check modification
			this.__checkConcurrent();
			
			// Should be the previous virtual index
			return this._vdx - 1;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/29
		 */
		@Override
		public final void remove()
			throws IllegalStateException
		{
			// Check modification
			this.__checkConcurrent();
			
			// {@squirreljme.error ZZ1z Cannot remove the element from the
			// linked list because there was no previous call to next or
			// previous, or add was called.}
			__Link__<E> last = this._last;
			if (last == null)
				throw new IllegalStateException("ZZ1z");
			
			// Only removed once
			this._last = null;
			
			// Just link the previous and next entries to the others and
			// drop this link
			last._prev._next = last._next;
			last._next._prev = last._prev;
			
			// If the next entry is after this point then it will needs its
			// index dropped
			int vdx = this._vdx,
				lastvdx = this._lastvdx;
			if (vdx > lastvdx)
				this._vdx = vdx - 1;
			
			// Set list as being modified and update our count to match
			this._atmod = ++LinkedList.this.modCount;
			
			// Size goes down
			LinkedList.this._size--;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/10/29
		 */
		@Override
		public final void set(E __v)
			throws IllegalStateException
		{
			// Check modification
			this.__checkConcurrent();
			
			// {@squirreljme.error ZZ20 Cannot set the element from the
			// linked list because there was no previous call to next or
			// previous, or add was called.}
			__Link__<E> last = this._last;
			if (last == null)
				throw new IllegalStateException("ZZ20");
			
			// Just set it
			last._value = __v;
		}
		
		/**
		 * Checks if the list was concurrently modified.
		 *
		 * @throws ConcurrentModificationException If it was modified.
		 * @since 2018/10/29
		 */
		private final void __checkConcurrent()
			throws ConcurrentModificationException
		{
			// {@squirreljme.error ZZ21 List has been concurrently modified.}
			if (this._atmod != LinkedList.this.modCount)
			{
				// Just empty these out so they are never used again
				this._next = null;
				this._last = null;
				
				// Fail
				throw new ConcurrentModificationException("ZZ21");
			}
		}
	}
	
	/**
	 * Represents a single link in the linked list. This is just a basic
	 * structure like object with public fields for simple access.
	 *
	 * @param <E> The type to store.
	 * @since 2018/10/29
	 */
	static final class __Link__<E>
	{
		/** The previous link. */
		__Link__<E> _prev;
		
		/** The next link. */
		__Link__<E> _next;
		
		/** The value to store. */
		E _value;
		
		/**
		 * Initializes the new link and links into the chain.
		 *
		 * @param __prev The previous link to link in.
		 * @param __v The value to use.
		 * @param __next The next link to link in.
		 * @since 2018/10/29
		 */
		__Link__(__Link__<E> __prev, E __v, __Link__<E> __next)
		{
			// Set value first
			this._value = __v;
			
			// Link into previous chain
			this._prev = __prev;
			if (__prev != null)
				__prev._next = this;
			
			// Link into next chain
			this._next = __next;
			if (__next != null)
				__next._prev = this;
		}
	}
}

