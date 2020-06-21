// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Storage for {@link ArrayDeque}.
 *
 * @param <E> The element to store.
 * @since 2020/06/21
 */
final class __ArrayDequeStore__<E>
{
	/** The modification counter. */
	final __ModCounter__ _modCount;

	/** Array elements. */
	E[] _elements;

	/** The number of elements in the queue. */
	int _size;

	/** The left side of the deque, where the first item is (always valid). */
	int _leftAt;

	/** The right side of the deque, where the last item is (always valid). */
	int _rightAt;
	
	/**
	 * Initializes the deque storage.
	 * 
	 * @param __modCount The modification count.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/21
	 */
	__ArrayDequeStore__(__ModCounter__ __modCount)
		throws NullPointerException
	{
		if (__modCount == null)
			throw new NullPointerException("NARG");
		
		this._modCount = __modCount;
	}
	
	/**
	 * Clears the deque.
	 * 
	 * @since 2020/06/21
	 */
	void __clear()
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
	
	/**
	 * Adds a value to the queue on a given side.
	 * 
	 * @param __rightSide Add to the right side? 
	 * @param __value The value to add.
	 * @throws NullPointerException On a null value.
	 * @since 2020/06/20
	 */
	void __elementAdd(boolean __rightSide, E __value)
		throws NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		// Make sure that there is room for this element
		int oldSize = this._size;
		int newSize = oldSize + 1;
		this.__ensureCapacity(newSize, !__rightSide, __rightSide);
		
		// Add to the right side
		E[] elements = this._elements;
		if (__rightSide)
		{
			int rightAt = this._rightAt;
			
			elements[++rightAt] = __value;
			
			this._rightAt = rightAt;
		}
		
		// Add to the left side
		else
		{
			int leftAt = this._leftAt;
			
			elements[--leftAt] = __value;
			
			this._leftAt = leftAt;
		}
		
		// Update internal variables
		this._size = newSize;
		this._modCount.modCount++;
	}
	
	/**
	 * Removes an element from the queue.
	 * 
	 * @param __rightSide Remove from the right side?
	 * @param __delete Is the element to be deleted?
	 * @return The element to remove.
	 * @since 2020/06/20
	 */
	E __elementGet(boolean __rightSide, boolean __delete)
	{
		// {@squirreljme.error ZZ37 Get of element from an empty deque?}
		int oldSize = this._size;
		if (oldSize == 0)
			throw new IllegalStateException("ZZ37");
		
		// Get/remove from right side
		E rv;
		E[] elements = this._elements;
		if (__rightSide)
		{
			int rightAt = this._rightAt;
			
			rv = elements[rightAt];
			
			if (__delete)
			{
				elements[rightAt--] = null;
				this._rightAt = rightAt;
			}
		}
		
		// Get/remove from left side
		else
		{
			int leftAt = this._leftAt;
			
			rv = elements[leftAt];
			
			if (__delete)
			{
				elements[leftAt++] = null;
				this._leftAt = leftAt;
			}
		}
		
		// Internal state only changes when deletion occurs
		if (__delete)
		{
			this._size = oldSize - 1;
			this._modCount.modCount++;
		}
		
		return rv;
	}
	
	/**
	 * Removes the element at the specified index.
	 * 
	 * @param __index The index to remove.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @since 2020/06/21
	 */
	void __elementRemoveAt(int __index)
		throws IndexOutOfBoundsException
	{
		int oldSize = this._size;
		if (__index < 0 || __index >= oldSize)
			throw new IndexOutOfBoundsException("IOOE");
		
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
	@SuppressWarnings("UnnecessaryLocalVariable")
	void __ensureCapacity(int __cap, boolean __bumpLeft,
		boolean __bumpRight)
	{
		// {@squirreljme.error ZZ3x Cannot bump both the left and the right
		// side.}
		if (__bumpLeft && __bumpRight)
			throw new IllegalArgumentException("ZZ3x");
		
		// Are we bumping at all?
		boolean isBumping = __bumpLeft || __bumpRight;
		
		// Used to detect if iterators are at new positions
		int oldLeftAt = this._leftAt;
		int oldRightAt = this._rightAt;
		int newLeftAt;
		int newRightAt;
		
		// In the event there are element changes
		E[] oldElements = this._elements;
		E[] newElements;
		
		// The current capacity of the array
		int currentCapacity = (oldElements == null ? 0 : oldElements.length);
		
		// The room that is available on the left and right
		int roomLeft = oldLeftAt;
		int roomRight = (currentCapacity - 1) - oldRightAt;
		
		// If there is no initialized array, this will be our first allocation
		// so allocate this many elements
		if (oldElements == null)
		{
			newElements = __ArrayDequeStore__.<E>__newArray(__cap);
			
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
			if (underCurrentCap)
			{
				// If there is room on the left and right side, we do not have
				// to actually do anything
				if (roomLeft > 0 && roomRight > 0)
					return;
				
				throw Debugging.todo();
			}
			
			// Bumping to the right and there is room to the left?
			else if (__bumpRight && roomRight <= 0 && roomLeft > 0)
				throw Debugging.todo();
			
			// Bumping to the left and there is room to the right?
			else if (__bumpLeft && roomLeft <= 0 && roomRight > 0)
				throw Debugging.todo();
			
			// Attempting to add element to the right side
			else if (__bumpRight)
				throw Debugging.todo();
			
			// Any other condition, we need to do lots of moving around
			else
				throw Debugging.todo();
		}
		
		// If our elements changed position, then our iterators will not be
		// at the correct location
		if (oldLeftAt != newLeftAt || oldRightAt != newRightAt)
			this._modCount.modCount++;
		
		// Store changes
		this._leftAt = newLeftAt;
		this._rightAt = newRightAt;
		this._elements = newElements;
	}
	
	/**
	 * Returns the element iterator.
	 * 
	 * @param __descending Is this descending?
	 * @return The iterator.
	 * @since 2020/06/21
	 */
	Iterator<E> __iterator(boolean __descending)
	{
		throw Debugging.todo();
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
