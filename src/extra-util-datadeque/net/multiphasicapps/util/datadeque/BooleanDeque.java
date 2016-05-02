// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.datadeque;

import java.util.Arrays;
import java.util.NoSuchElementException;
import net.multiphasicapps.util.dynbuffer.DynamicByteBuffer;

/**
 * This is a circular buffer which provides bits for input and output as a kind
 * of queue.
 *
 * If the queue reaches full capacity then it is increased in size.
 *
 * @since 2016/03/11
 */
public class BooleanDeque
{
	/** The lock to use. */
	protected final Object lock;
	
	/** The buffer to base off. */
	protected final DynamicByteBuffer base =
		new DynamicByteBuffer();
	
	/** The maximum permitted capacity. */
	protected final int capacity;
	
	/** The head set. */
	private final boolean _head[] =
		new boolean[8];
	
	/** The tail set. */
	private final boolean _tail[] =
		new boolean[8];
	
	/** The head offset. */
	private volatile int _headp;
	
	/** The tail offset. */
	private volatile int _tailp;
	
	/** The number of bits in the deque. */
	private volatile int _count;
	
	/**
	 * Initializes a boolean deque.
	 *
	 * @since 2016/03/11
	 */
	public BooleanDeque()
	{
		this(new Object(), Integer.MAX_VALUE);
	}
	
	/**
	 * Initializes a boolean deque with the given capacity.
	 *
	 * @param __cap The maximum deque capacity.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2016/05/01
	 */
	public BooleanDeque(int __cap)
		throws IllegalArgumentException
	{
		this(new Object(), __cap);
	}
	
	/**
	 * Initializes a boolean deque with the given lock object.
	 *
	 * @param __lock The lock to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public BooleanDeque(Object __lock)
		throws NullPointerException
	{
		this(__lock, Integer.MAX_VALUE);
	}
	
	/**
	 * Initializes a boolean deque with the given capacity and lock object.
	 *
	 * @param __lock The lock object to use.
	 * @param __cap The maximum deque capacity.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2016/05/01
	 */
	public BooleanDeque(Object __lock, int __cap)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__lock == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AE01 Negative deque capacity specified.}
		if (__cap < 0)
			throw new IllegalArgumentException("AE01");
		
		// Set
		lock = __lock;
		capacity = __cap;
	}
	
	/**
	 * Attempts to add a single boolean to the start of the queue, if the
	 * capacity would be violated then an exception is thrown.
	 *
	 * @param __b The boolean to add.
	 * @throws IllegalStateException If the capacity is violated.
	 * @since 2016/05/01
	 */
	public void addFirst(boolean __b)
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Exceeds capacity?
			int n = _count;
			if (n < 0 || n >= capacity)
				throw new IllegalStateException("AE03");
			
			// Get the 
			int hp = _headp;
			
			// Need to push byte to the queue
			boolean[] hx = _head;
			if (hp >= 7)
			{
				// Compact the byte to add
				byte syn = __compact(hx);
				
				// Clear the array
				Arrays.fill(hx, false);
				
				// Add the byte to the base
				base.add(0, syn);
				
				// Back to no room
				_headp = hp = 0;
			}
			
			// Set the given boolean
			hx[hp] = __b;
			
			// Next bit to set
			_headp = hp + 1;
		}
	}
	
	/**
	 * Attempts to add multiple bytes to the start of the queue, if the
	 * capacity would be violated then an exception is thrown.
	 *
	 * @param __b The array to source bytes from.
	 * @throws IllegalStateException If the capacity is violated.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public void addFirst(boolean[] __b)
		throws IllegalStateException, NullPointerException
	{
		addFirst(__b, 0, __b.length);
	}
	
	/**
	 * Attempts to add multiple bytes to the start of the queue, if the
	 * capacity would be violated then an exception is thrown.
	 *
	 * @param __b The array to source bytes from.
	 * @param __o The offset to start reading from.
	 * @param __l The number of bytes to write.
	 * @throws IllegalStateException If the capacity is violated.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public void addFirst(boolean[] __b, int __o, int __l)
		throws IllegalStateException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Exceeds capacity?
			int n = _count;
			int w = n + __l;
			if (w < 0 || w > capacity)
				throw new IllegalStateException("AE03");
			
			// Add to the start
			for (int i = __l - 1; i >= 0; i--)
				addFirst(__b[__o + i]);
		}
	}
	
	/**
	 * Adds an integer to the start of the queue.
	 *
	 * @param __bits The bits to set.
	 * @param __mask The mask for set bits.
	 * @throws IllegalStateException If the capacity would be exceeded.
	 * @since 2016/05/02
	 */
	public void addFirstInt(int __bits, int __mask)
		throws IllegalStateException
	{
		addFirstInt(__bits, __mask, false);
	}
	
	/**
	 * Adds an integer to the start of the queue.
	 *
	 * @param __bits The bits to set.
	 * @param __mask The mask for set bits.
	 * @param __msb If the bits are to be added using the most significant
	 * digits first.
	 * @throws IllegalStateException If the capacity would be exceeded.
	 * @since 2016/05/02
	 */
	public void addFirstInt(int __bits, int __mask, boolean __msb)
		throws IllegalStateException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Attempts to add a single boolean to the end of the queue, if the
	 * capacity would be violated then an exception is thrown.
	 *
	 * @param __b The boolean to add.
	 * @throws IllegalStateException If the capacity is violated.
	 * @since 2016/05/01
	 */
	public void addLast(boolean __b)
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Exceeds capacity?
			int n = _count;
			if (n < 0 || n >= capacity)
				throw new IllegalStateException("AE03");
			
			// Add to the end
			throw new Error("TODO");
		}
	}
	
	/**
	 * Attempts to add multiple bytes to the end of the queue, if the capacity
	 * would be violated then an exception is thrown.
	 *
	 * @param __b The array to source bytes from.
	 * @throws IllegalStateException If the capacity is violated.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public void addLast(boolean[] __b)
		throws IllegalStateException, NullPointerException
	{
		addLast(__b, 0, __b.length);
	}
	
	/**
	 * Attempts to add multiple bytes to the end of the queue, if the capacity
	 * would be violated then an exception is thrown.
	 *
	 * @param __b The array to source bytes from.
	 * @param __o The offset to start reading from.
	 * @param __l The number of bytes to write.
	 * @throws IllegalStateException If the capacity is violated.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public void addLast(boolean[] __b, int __o, int __l)
		throws IllegalStateException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Exceeds capacity?
			int n = _count;
			int w = n + __l;
			if (w < 0 || w > capacity)
				throw new IllegalStateException("AE03");
			
			// Add to the end
			throw new Error("TODO");
		}
	}
	
	/**
	 * Adds an integer to the end of the queue.
	 *
	 * @param __bits The bits to set.
	 * @param __mask The mask for set bits.
	 * @throws IllegalArgumentException If any bit is set that is not within
	 * the mask, or the mask has a zero gap in it.
	 * @throws IllegalStateException If the capacity would be exceeded.
	 * @since 2016/05/02
	 */
	public void addLastInt(int __bits, int __mask)
		throws IllegalArgumentException, IllegalStateException
	{
		addLastInt(__bits, __mask, false);
	}
	
	/**
	 * Adds an integer to the end of the queue.
	 *
	 * @param __bits The bits to set.
	 * @param __mask The mask for set bits.
	 * @param __msb If the bits are to be added using the most significant
	 * digits first.
	 * @throws IllegalArgumentException If any bit is set that is not within
	 * the mask, or the mask has a zero gap in it.
	 * @throws IllegalStateException If the capacity would be exceeded.
	 * @since 2016/05/02
	 */
	public void addLastInt(int __val, int __mask, boolean __msb)
		throws IllegalArgumentException, IllegalStateException
	{
		// Number of bits in the mask
		int ibm = Integer.bitCount(__mask);
		
		// Check to make sure the input is valid
		if ((__val & (~__mask)) != 0)
			throw new IllegalArgumentException(String.format("XI06 %x %x",
				__val, __mask));
		if (ibm != (32 - Integer.numberOfLeadingZeros(__mask)) ||
			(__mask & 1) == 0)
			throw new IllegalArgumentException(String.format("XI07 %x %x",
				__val, __mask));
		
		// Lock
		synchronized (lock)
		{
			// Would exceed capacity?
			int n = _count;
			int w = n + ibm;
			if (w < 0 || w > capacity)
				throw new IllegalStateException("AE03");
			
			// Add bits
			int an = (__msb ? -1 : 1);
			for (int at = (__msb ? ibm - 1 : 0); at >= 0 && at < ibm; at += an)
				addLast(0 != (__val & (1 << at)));
		}
	}
	
	/**
	 * Returns the number of available bytes inside of the queue.
	 *
	 * @return The number of bytes in the queue.
	 * @since 2016/05/01
	 */
	public int available()
	{
		// Lock
		synchronized (lock)
		{
			return _count;
		}
	}
	
	/**
	 * Obtains but does not remove the first boolean.
	 *
	 * @return The value of the first boolean.
	 * @throws NoSuchElementException If the deque is empty.
	 * @since 2016/05/01
	 */
	public boolean getFirst()
		throws NoSuchElementException
	{
		// Lock
		synchronized (lock)
		{
			// Check
			if (_count <= 0)
				throw new NoSuchElementException("AE02");
			
			// Get
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains but does not remove the first set of bytes.
	 *
	 * @param __b The destination array to obtain the first bytes for.
	 * @return The number of read bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public int getFirst(boolean[] __b)
		throws NullPointerException
	{
		return getFirst(__b, 0, __b.length);
	}
	
	/**
	 * Obtains but does not remove the first set of bytes.
	 *
	 * @param __b The destination array to obtain the first bytes for.
	 * @param __o The offset in the destination array to start reading bytes
	 * into.
	 * @param __l The number of bytes to read.
	 * @return The number of read bytes.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the bounds of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public int getFirst(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Pointless if empty
			int n;
			if ((n = _count) <= 0)
				return 0;
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains but does not remove the last boolean.
	 *
	 * @return The value of the last boolean.
	 * @throws NoSuchElementException If the deque is empty.
	 * @since 2016/05/01
	 */
	public boolean getLast()
		throws NoSuchElementException
	{
		// Lock
		synchronized (lock)
		{
			// Check
			int n;
			if ((n = _count) <= 0)
				throw new NoSuchElementException("AE02");
			
			// Get
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains but does not remove the last set of bytes.
	 *
	 * @param __b The destination array to obtain the last bytes for.
	 * @return The number of read bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public int getLast(boolean[] __b)
		throws NullPointerException
	{
		return getLast(__b, 0, __b.length);
	}
	
	/**
	 * Obtains but does not remove the last set of bytes.
	 *
	 * @param __b The destination array to obtain the last bytes for.
	 * @param __o The offset in the destination array to start reading bytes
	 * into.
	 * @param __l The number of bytes to read.
	 * @return The number of read bytes.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the bounds of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public int getLast(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Pointless if empty
			int n;
			if ((n = _count) <= 0)
				return 0;
			
			// Get
			throw new Error("TODO");
		}
	}
	
	/**
	 * Offers a single boolean to the start of the deque and returns
	 * {@code true} if it was added to the deque.
	 *
	 * @param __b The boolean to add to the start.
	 * @return {@code true} if the capacity was not violated and the bytes were
	 * added.
	 * @since 2016/05/01
	 */
	public boolean offerFirst(boolean __b)
	{
		// May violate the capacity
		try
		{
			addFirst(__b);
			return true;
		}
		
		// Violates capacity
		catch (IllegalStateException ise)
		{
			return false;
		}
	}
	
	/**
	 * Offers multiple bytes to the start of the deque and returns {@code true}
	 * if they were added to the deque.
	 *
	 * @param __b The array to source bytes from.
	 * @return {@code true} if the capacity was not violated and the bytes were
	 * added.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public boolean offerFirst(boolean[] __b)
		throws NullPointerException
	{
		return offerFirst(__b, 0, __b.length);
	}
	
	/**
	 * Offers multiple bytes to the start of the deque and returns {@code true}
	 * if they were added to the deque.
	 *
	 * @param __b The array to source bytes from.
	 * @param __o The offset to start reading from.
	 * @param __l The number of bytes to write.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public boolean offerFirst(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException
	{
		// May violate the capacity
		try
		{
			addFirst(__b, __o, __l);
			return true;
		}
		
		// Violates capacity
		catch (IllegalStateException ise)
		{
			return false;
		}
	}
	
	/**
	 * Offers an integer and a mask to start of the queue.
	 *
	 * @param __bits The bits to add.
	 * @param __mask The mask for the bits.
	 * @return {@code true} if the bits were added.
	 * @since 2016/05/02
	 */
	public boolean offerFirstInt(int __bits, int __mask)
	{
		return offerFirstInt(__bits, __mask, false);
	}
	
	/**
	 * Offers an integer and a mask to start of the queue.
	 *
	 * @param __bits The bits to add.
	 * @param __mask The mask for the bits.
	 * @param __msb If {@code true} then the bits are added in most significant
	 * order first.
	 * @return {@code true} if the bits were added.
	 * @since 2016/05/02
	 */
	public boolean offerFirstInt(int __bits, int __mask, boolean __msb)
	{
		// May violate the capacity
		try
		{
			addFirstInt(__bits, __mask, __msb);
			return true;
		}
		
		// Violates capacity
		catch (IllegalStateException ise)
		{
			return false;
		}
	}
	
	/**
	 * Offers a single boolean to the end of the deque and returns {@code true}
	 * if it was added to the deque.
	 *
	 * @param __b The boolean to add to the end.
	 * @return {@code true} if the capacity was not violated and the bytes were
	 * added.
	 * @since 2016/05/01
	 */
	public boolean offerLast(boolean __b)
	{
		// May violate the capacity
		try
		{
			addLast(__b);
			return true;
		}
		
		// Violates capacity
		catch (IllegalStateException ise)
		{
			return false;
		}
	}
	
	/**
	 * Offers multiple bytes to the end of the deque and returns {@code true}
	 * if they were added to the deque.
	 *
	 * @param __b The array to source bytes from.
	 * @return {@code true} if the capacity was not violated and the bytes were
	 * added.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public boolean offerLast(boolean[] __b)
		throws NullPointerException
	{
		return offerLast(__b, 0, __b.length);
	}
	
	/**
	 * Offers multiple bytes to the end of the deque and returns {@code true}
	 * if they were added to the deque.
	 *
	 * @param __b The array to source bytes from.
	 * @param __o The offset to start reading from.
	 * @param __l The number of bytes to write.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public boolean offerLast(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException
	{
		// May violate the capacity
		try
		{
			addLast(__b, __o, __l);
			return true;
		}
		
		// Violates capacity
		catch (IllegalStateException ise)
		{
			return false;
		}
	}
	
	/**
	 * Offers an integer and a mask to end of the queue.
	 *
	 * @param __bits The bits to add.
	 * @param __mask The mask for the bits.
	 * @return {@code true} if the bits were added.
	 * @since 2016/05/02
	 */
	public boolean offerLastInt(int __bits, int __mask)
	{
		return offerLastInt(__bits, __mask, false);
	}
	
	/**
	 * Offers an integer and a mask to end of the queue.
	 *
	 * @param __bits The bits to add.
	 * @param __mask The mask for the bits.
	 * @param __msb If {@code true} then the bits are added in most significant
	 * order first.
	 * @return {@code true} if the bits were added.
	 * @since 2016/05/02
	 */
	public boolean offerLastInt(int __bits, int __mask, boolean __msb)
	{
		// May violate the capacity
		try
		{
			addLastInt(__bits, __mask, __msb);
			return true;
		}
		
		// Violates capacity
		catch (IllegalStateException ise)
		{
			return false;
		}
	}
	
	/**
	 * Obtains but does not remove the first boolean, returning a special value
	 * if the deque is empty.
	 *
	 * @return The value of the first boolean or a negative value if the deque
	 * is empty.
	 * @since 2016/05/01
	 */
	public int peekFirst()
		throws NoSuchElementException
	{
		// The deque could be empty
		try
		{
			throw new Error("TODO");
		}
		
		// Does not exist.
		catch (NoSuchElementException e)
		{
			return -1;
		}
	}
	
	/**
	 * Obtains but does not remove the last boolean, returning a special value
	 * if the deque is empty.
	 *
	 * @return The value of the last boolean or a negative value if the deque
	 * is empty.
	 * @since 2016/05/01
	 */
	public int peekLast()
		throws NoSuchElementException
	{
		// The deque could be empty
		try
		{
			throw new Error("TODO");
		}
		
		// Does not exist.
		catch (NoSuchElementException e)
		{
			return -1;
		}
	}
	
	/**
	 * Removes a single boolean from the from of the deque.
	 *
	 * @return The next input boolean.
	 * @throws NoSuchElementException If not a single boolean is available.
	 * @since 2016/05/01
	 */
	public boolean removeFirst()
		throws NoSuchElementException
	{
		// Lock
		synchronized (lock)
		{
			// No data available
			if (_count <= 0)
				throw new NoSuchElementException("AE02");
			
			// Remove the first item
			throw new Error("TODO");
		}
	}
	
	/**
	 * Removes multiple bytes from the front of the deque.
	 *
	 * @param __b The array to read bytes into.
	 * @return The number of removed bytes, may be {@code 0}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public int removeFirst(boolean[] __b)
		throws NullPointerException
	{
		return removeFirst(__b, 0, __b.length);
	}
	
	/**
	 * Removes multiple bytes from the front of the deque.
	 *
	 * @param __b The array to read bytes into.
	 * @param __o The offset to start writing into.
	 * @param __l The number of bytes to remove.
	 * @return The number of removed bytes, may be {@code 0}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public int removeFirst(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Removes an integer from the start of the queue.
	 *
	 * @param __bits The number of bits to remove.
	 * @return The read value.
	 * @throws NoSuchElementException If there are not enough bits in the queue.
	 * @since 2016/05/02
	 */
	public int removeFirstInt(int __bits)
		throws NoSuchElementException
	{
		return removeFirstInt(__bits, false);
	}
	
	/**
	 * Removes an integer from the start of the queue.
	 *
	 * @param __bits The number of bits to remove.
	 * @param __msb If the bits are to be removed from the most significant
	 * end first.
	 * @return The read value.
	 * @throws NoSuchElementException If there are not enough bits in the queue.
	 * @since 2016/05/02
	 */
	public int removeFirstInt(int __bits, boolean __msb)
		throws NoSuchElementException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Removes a single boolean from the from of the deque.
	 *
	 * @return The next input boolean.
	 * @throws NoSuchElementException If not a single boolean is available.
	 * @since 2016/05/01
	 */
	public boolean removeLast()
		throws NoSuchElementException
	{
		// Lock
		synchronized (lock)
		{
			// No data available
			int n;
			if ((n = _count) <= 0)
				throw new NoSuchElementException("AE02");
			
			// Remove the last item
			throw new Error("TODO");
		}
	}
	
	/**
	 * Removes multiple bytes from the end of the deque.
	 *
	 * @param __b The array to read bytes into.
	 * @return The number of removed bytes, may be {@code 0}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public int removeLast(boolean[] __b)
		throws NullPointerException
	{
		return removeLast(__b, 0, __b.length);
	}
	
	/**
	 * Removes multiple bytes from the end of the deque.
	 *
	 * @param __b The array to read bytes into.
	 * @param __o The offset to start writing into.
	 * @param __l The number of bytes to remove.
	 * @return The number of removed bytes, may be {@code 0}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/01
	 */
	public int removeLast(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Removes an integer from the end of the queue.
	 *
	 * @param __bits The number of bits to remove.
	 * @return The read value.
	 * @throws NoSuchElementException If there are not enough bits in the queue.
	 * @since 2016/05/02
	 */
	public int removeLastInt(int __bits)
		throws NoSuchElementException
	{
		return removeLastInt(__bits, false);
	}
	
	/**
	 * Removes an integer from the end of the queue.
	 *
	 * @param __bits The number of bits to remove.
	 * @param __msb If the bits are to be removed from the most significant
	 * end first.
	 * @return The read value.
	 * @throws NoSuchElementException If there are not enough bits in the queue.
	 * @since 2016/05/02
	 */
	public int removeLastInt(int __bits, boolean __msb)
		throws NoSuchElementException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Compacts the given boolean buffer so it fits into a byte.
	 *
	 * @param __b The array to compact.
	 * @return The compacted byte.
	 * @since 2016/05/02
	 */
	private final byte __compact(boolean[] __b)
	{
		byte rv = 0;
		for (byte i = 0, s = 1; i < 8; i++, s <<= 1)
			if (__b[i])
				rv |= s;
		return rv;
	}
}

