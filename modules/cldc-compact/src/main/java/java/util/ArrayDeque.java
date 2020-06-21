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

	/** How much more space to add at a time. */
	private static final int _CAPACITY_JUMP =
		4;

	/** The modification count of this queue. */
	final __ModCounter__ _modCount =
		new __ModCounter__();

	/** Array elements. */
	private E[] _elements;

	/** The number of elements in the queue. */
	private int _size;

	/** The left side of the deque, where the first item is (always valid). */
	private int _leftAt;

	/** The right side of the deque, where the last item is (always valid). */
	private int _rightAt;

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
		this.__ensureCapacity(__initialCap,
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
		
		// Use fixed size to match the collection
		int size = __c.size();
		this._size = size;
		
		// Store the collection directly into the array
		E[] elements = ArrayDeque.<E>__newArray(size);
		int i = 0;
		for (E e : __c)
			elements[i++] = e;
		this._elements = elements;
		
		// Consumes the entire space, note that the right side is directly
		// indexed
		this._leftAt = 0;
		this._rightAt = size - 1;
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
		// If there were elements in here, then the iterators will not be
		// valid
		int oldSize = this._size;
		if (oldSize > 0)
			this._modCount.modCount++;
		
		// Completely destroy the contents of this deque
		this._elements = null;
		this._size = 0;
		this._leftAt = 0;
		this._rightAt = 0;
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
		if (__v == null || this._size <= 0)
			return false;
		
		// Scan for the element
		E[] elements = this._elements;
		for (int at = this._leftAt, endAt = this._rightAt; at <= endAt; at++)
			if (Objects.equals(elements[at], __v))
				return true;
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @since 2020/06/20
	 */
	@Override
	public Iterator<E> descendingIterator()
	{
		return new __ArrayDequeIterator__<E>(this._elements, this._size,
			this._rightAt, this._leftAt, this._modCount, this);
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		return new __ArrayDequeIterator__<E>(this._elements, this._size,
			this._leftAt, this._rightAt, this._modCount, this);
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		if (this._size == 0)
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
		
		return this.__removeFirst(this.iterator(), __v);
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
		if (this._size == 0)
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
		
		return this.__removeFirst(this.descendingIterator(), __v);
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
		return this._size;
	}
	
	/**
	 * Adds a value to the queue on a given side.
	 * 
	 * @param __rightSide Add to the right side? 
	 * @param __value The value to add.
	 * @since 2020/06/20
	 */
	private void __elementAdd(boolean __rightSide, E __value)
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	/**
	 * Ensures that the given number of elements can be stored, the bumping
	 * may be used to determine where the extra space should be placed and is
	 * used as a suggestion if used.
	 * 
	 * On initial allocation, unless bumping is used, the pivot will be
	 * directly in the middle of the array.
	 * 
	 * Note that if bumping is used and there is free space on one side of
	 * the dequeue, then elements may be shifted over. 
	 *
	 * @param __cap The capacity to ensure.
	 * @param __bumpLeft Bump capacity on the left side.
	 * @param __bumpRight Bump capacity on the right side.
	 * @throws IllegalArgumentException If both {@code __bumpLeft} and
	 * {@code __bumpRight} are {@code true}.
	 * @since 2020/06/20
	 */
	private void __ensureCapacity(int __cap, boolean __bumpLeft,
		boolean __bumpRight)
	{
		// {@squirreljme.error ZZ3x Cannot bump both the left and the right
		// side.}
		if (__bumpLeft && __bumpRight)
			throw new IllegalArgumentException("ZZ3x");
		
		// Used to detect if iterators are at new positions
		int origLeftAt = this._leftAt;
		int origRightAt = this._rightAt;
		int newLeftAt;
		int newRightAt;
		
		// In the event there are element changes
		E[] oldElements = this._elements;
		E[] newElements;
		
		// If there is no initialized array, this will be our first allocation
		// so allocate this many elements
		if (oldElements == null)
		{
			newElements = ArrayDeque.<E>__newArray(__cap);
			
			// Have all the empty space on the left side
			if (__bumpLeft)
				newLeftAt = newRightAt = __cap - 1;
			
			// Have all the empty space on the right side
			else if (__bumpRight)
				newLeftAt = newRightAt = 0;
			
			// Place the pivots directly in the center, since we do not know
			// how this will be used
			else
			{
				newLeftAt = __cap >> 1;
				newRightAt = newLeftAt;
			}
		}
		
		// Otherwise, we will have to move elements around and such
		else
		{
			// If the capacity is under the size, this will be used as a hint
			// to determine if we can shift all the elements over for this
			// deque
			boolean underCurrentCap = (__cap <= this._size);
			
			throw Debugging.todo();
		}
		
		// If our elements changed position, then our iterators will not be
		// at the correct location
		if (origLeftAt != newLeftAt || origRightAt != newRightAt)
			this._modCount.modCount++;
		
		// Store changes
		this._leftAt = newLeftAt;
		this._rightAt = newRightAt;
		this._elements = newElements;
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

	/**
	 * Allocates a new array.
	 *
	 * @param <E> The element to use.
	 * @param __len The length of the array.
	 * @return The newly allocated array.
	 * @since 2020/06/20
	 */
	@SuppressWarnings("unchecked")
	private static <E> E[] __newArray(int __len)
	{
		return (E[])new Object[__len];
	}
}
