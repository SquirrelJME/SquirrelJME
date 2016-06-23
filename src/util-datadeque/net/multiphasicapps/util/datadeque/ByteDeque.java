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

import java.util.NoSuchElementException;
import net.multiphasicapps.util.dynbuffer.DynamicByteBuffer;

/**
 * This is a byte buffer which provides bytes for input and output as a
 * double ended queue.
 *
 * If the queue reaches full capacity then it is increased in size.
 *
 * {@squirreljme.error AE02 No bytes available.}
 * {@squirreljme.error AE03 Capacity reached.}
 *
 * @since 2016/03/11
 */
public class ByteDeque
{
	/** The lock to use. */
	protected final Object lock;
	
	/** The buffer to base off. */
	protected final DynamicByteBuffer base =
		new DynamicByteBuffer();
	
	/** The maximum permitted capacity. */
	protected final int capacity;
	
	/**
	 * Initializes a byte deque.
	 *
	 * @since 2016/03/11
	 */
	public ByteDeque()
	{
		this(new Object(), Integer.MAX_VALUE);
	}
	
	/**
	 * Initializes a byte deque with the given capacity.
	 *
	 * @param __cap The maximum deque capacity.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2016/05/01
	 */
	public ByteDeque(int __cap)
		throws IllegalArgumentException
	{
		this(new Object(), __cap);
	}
	
	/**
	 * Initializes a byte deque with the given lock object.
	 *
	 * @param __lock The lock to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public ByteDeque(Object __lock)
		throws NullPointerException
	{
		this(__lock, Integer.MAX_VALUE);
	}
	
	/**
	 * Initializes a byte deque with the given capacity and lock object.
	 *
	 * @param __lock The lock object to use.
	 * @param __cap The maximum deque capacity.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2016/05/01
	 */
	public ByteDeque(Object __lock, int __cap)
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
	 * Attempts to add a single byte to the start of the queue, if the capacity
	 * would be violated then an exception is thrown.
	 *
	 * @param __b The byte to add.
	 * @throws IllegalStateException If the capacity is violated.
	 * @since 2016/05/01
	 */
	public void addFirst(byte __b)
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Exceeds capacity?
			int n = base.size();
			if (n < 0 || n >= capacity)
				throw new IllegalStateException("AE03");
			
			// Add to the start
			base.add(0, __b);
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
	public void addFirst(byte[] __b)
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
	public void addFirst(byte[] __b, int __o, int __l)
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
			int n = base.size();
			int w = n + __l;
			if (w < 0 || w > capacity)
				throw new IllegalStateException("AE03");
			
			// Add to the start
			base.add(0, __b, __o, __l);
		}
	}
	
	/**
	 * Attempts to add a single byte to the end of the queue, if the capacity
	 * would be violated then an exception is thrown.
	 *
	 * @param __b The byte to add.
	 * @throws IllegalStateException If the capacity is violated.
	 * @since 2016/05/01
	 */
	public void addLast(byte __b)
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Exceeds capacity?
			int n = base.size();
			if (n < 0 || n >= capacity)
				throw new IllegalStateException("AE03");
			
			// Add to the end
			base.add(n, __b);
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
	public void addLast(byte[] __b)
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
	public void addLast(byte[] __b, int __o, int __l)
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
			int n = base.size();
			int w = n + __l;
			if (w < 0 || w > capacity)
				throw new IllegalStateException("AE03");
			
			// Add to the end
			base.add(n, __b, __o, __l);
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
			return base.size();
		}
	}
	
	/**
	 * Obtains but does not remove the first byte.
	 *
	 * @return The value of the first byte.
	 * @throws NoSuchElementException If the deque is empty.
	 * @since 2016/05/01
	 */
	public byte getFirst()
		throws NoSuchElementException
	{
		// Lock
		synchronized (lock)
		{
			// Check
			if (base.size() <= 0)
				throw new NoSuchElementException("AE02");
			
			// Get
			return base.get(0);
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
	public int getFirst(byte[] __b)
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
	public int getFirst(byte[] __b, int __o, int __l)
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
			if ((n = base.size()) <= 0)
				return 0;
			
			// Get
			int rv;
			base.get(0, __b, __o, rv = Math.min(n, __l));
			return rv;
		}
	}
	
	/**
	 * Obtains but does not remove the last byte.
	 *
	 * @return The value of the last byte.
	 * @throws NoSuchElementException If the deque is empty.
	 * @since 2016/05/01
	 */
	public byte getLast()
		throws NoSuchElementException
	{
		// Lock
		synchronized (lock)
		{
			// Check
			int n;
			if ((n = base.size()) <= 0)
				throw new NoSuchElementException("AE02");
			
			// Get
			return base.get(n - 1);
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
	public int getLast(byte[] __b)
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
	public int getLast(byte[] __b, int __o, int __l)
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
			if ((n = base.size()) <= 0)
				return 0;
			
			// Get
			int d = n - __l;
			int rv;
			base.get(Math.max(0, d), __b, __o, rv = Math.min(n, __l));
			return rv;
		}
	}
	
	/**
	 * Offers a single byte to the start of the deque and returns {@code true}
	 * if it was added to the deque.
	 *
	 * @param __b The byte to add to the start.
	 * @return {@code true} if the capacity was not violated and the bytes were
	 * added.
	 * @since 2016/05/01
	 */
	public boolean offerFirst(byte __b)
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
	public boolean offerFirst(byte[] __b)
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
	public boolean offerFirst(byte[] __b, int __o, int __l)
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
	 * Offers a single byte to the end of the deque and returns {@code true} if
	 * it was added to the deque.
	 *
	 * @param __b The byte to add to the end.
	 * @return {@code true} if the capacity was not violated and the bytes were
	 * added.
	 * @since 2016/05/01
	 */
	public boolean offerLast(byte __b)
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
	public boolean offerLast(byte[] __b)
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
	public boolean offerLast(byte[] __b, int __o, int __l)
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
	 * Obtains but does not remove the first byte, returning a special value
	 * if the deque is empty.
	 *
	 * @return The value of the first byte or a negative value if the deque is
	 * empty.
	 * @since 2016/05/01
	 */
	public int peekFirst()
		throws NoSuchElementException
	{
		// The deque could be empty
		try
		{
			return ((int)getFirst()) & 0xFF;
		}
		
		// Does not exist.
		catch (NoSuchElementException e)
		{
			return -1;
		}
	}
	
	/**
	 * Obtains but does not remove the last byte, returning a special value
	 * if the deque is empty.
	 *
	 * @return The value of the last byte or a negative value if the deque is
	 * empty.
	 * @since 2016/05/01
	 */
	public int peekLast()
		throws NoSuchElementException
	{
		// The deque could be empty
		try
		{
			return ((int)getLast()) & 0xFF;
		}
		
		// Does not exist.
		catch (NoSuchElementException e)
		{
			return -1;
		}
	}
	
	/**
	 * Removes a single byte from the from of the deque.
	 *
	 * @return The next input byte.
	 * @throws NoSuchElementException If not a single byte is available.
	 * @since 2016/05/01
	 */
	public byte removeFirst()
		throws NoSuchElementException
	{
		// Lock
		synchronized (lock)
		{
			// No data available
			if (base.size() <= 0)
				throw new NoSuchElementException("AE02");
			
			// Remove the first item
			return base.remove(0);
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
	public int removeFirst(byte[] __b)
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
	public int removeFirst(byte[] __b, int __o, int __l)
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
			return base.remove(0, __b, __o, __l);
		}
	}
	
	/**
	 * Removes a single byte from the from of the deque.
	 *
	 * @return The next input byte.
	 * @throws NoSuchElementException If not a single byte is available.
	 * @since 2016/05/01
	 */
	public byte removeLast()
		throws NoSuchElementException
	{
		// Lock
		synchronized (lock)
		{
			// No data available
			int n;
			if ((n = base.size()) <= 0)
				throw new NoSuchElementException("AE02");
			
			// Remove the last item
			return base.remove(n - 1);
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
	public int removeLast(byte[] __b)
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
	public int removeLast(byte[] __b, int __o, int __l)
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
			return base.remove(Math.max(0, base.size() - __l), __b, __o, __l);
		}
	}
}

