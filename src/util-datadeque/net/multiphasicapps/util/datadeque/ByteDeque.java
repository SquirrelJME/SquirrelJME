// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.datadeque;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This is a byte buffer which provides bytes for input and output as a
 * double ended queue.
 *
 * If the queue reaches full capacity then it is increased in size.
 *
 * @since 2016/03/11
 */
public class ByteDeque
{
	/**
	 * {@squirreljme.property net.multiphasicapps.util.datadeque.blocksize=n
	 * The block size of individual arrays that make up the {@link ByteDeque}
	 * class. The value must be a power of two.}
	 */
	private static final int _BLOCK_SIZE =
		Math.max(8, Integer.getInteger(
			"net.multiphasicapps.util.datadeque.blocksize", 64));
	
	/** The block size mask. */
	private static final int _BLOCK_MASK =
		_BLOCK_SIZE - 1;
	
	/** The lock to use. */
	protected final Object lock;
	
	/** The maximum permitted capacity. */
	protected final int capacity;
	
	/** Blocks which make up the queue. */
	private final LinkedList<byte[]> _blocks =
		new LinkedList<>();
	
	/** Single byte (since it is synchronized). */
	private final byte[] _solo =
		new byte[1];
	
	/** The number of bytes in the queue. */
	private volatile int _total;
	
	/** The relative position of the head in relation to the first block. */
	private volatile int _head;
	
	/** The relative position of the tail in relation to the last block. */
	private volatile int _tail;
	
	static
	{
		// {@squirreljme.error AE02 The block size of the data deque is not
		// a power of two. (The specified block size)}
		if (Integer.bitCount(_BLOCK_SIZE) != 1)
			throw new RuntimeException(String.format("AE02 %d", _BLOCK_SIZE));
	}
	
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
	public final void addFirst(byte __b)
		throws IllegalStateException
	{
		// Lock
		synchronized (this.lock)
		{
			byte[] solo = _solo;
			solo[0] = __b;
			addFirst(solo, 0, 1);
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
	public final void addFirst(byte[] __b)
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
	public final void addFirst(byte[] __b, int __o, int __l)
		throws IllegalStateException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// No bytes to add, do nothing
		if (__l == 0)
			return;
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error AE05 Adding bytes to the start would exceed
			// the capacity of the queue.}
			int total = this._total;
			int newtotal = total + __l;
			if (newtotal < 0 || newtotal > this.capacity)
				throw new IllegalStateException("AE05");
			
			// Get some things
			LinkedList<byte[]> blocks = this._blocks;
			int nb = blocks.size();
			int head = this._head, tail = this._tail;
			
			throw new Error("TODO");
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
	public final void addLast(byte __b)
		throws IllegalStateException
	{
		// Lock
		synchronized (this.lock)
		{
			byte[] solo = _solo;
			solo[0] = __b;
			addLast(solo, 0, 1);
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
	public final void addLast(byte[] __b)
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
	public final void addLast(byte[] __b, int __o, int __l)
		throws IllegalStateException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// No bytes to add, do nothing
		if (__l == 0)
			return;
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error AE04 Adding bytes to the end would exceed
			// the capacity of the queue.}
			int total = this._total;
			int newtotal = total + __l;
			if (newtotal < 0 || newtotal > this.capacity)
				throw new IllegalStateException("AE04");
			
			// Get some things
			LinkedList<byte[]> blocks = this._blocks;
			int nb = blocks.size();
			int head = this._head, tail = this._tail;
			
			// No data? Try to fill the first block as much as possible
			int bs = _BLOCK_SIZE;
			int left = __l;
			int at = __o;
			if (total == 0)
			{
				// Only can fit a single block
				int limit = Math.min(bs, __l);
				
				byte[] bl = new byte[bs];
				for (int i = 0; i < limit; i++)
					bl[i] = __b[at++];
				
				// Add block
				blocks.addLast(bl);
				tail = limit;
				
				// Consume those bytes
				left -= limit;
			}
			
			// Keep adding in data
			while (left > 0)
			{
				throw new Error("TODO");
			}
			
			// Set new details
			this._total = newtotal;
			this._tail = tail;
			
			// Debug
			__DEBUG();
		}
	}
	
	/**
	 * Returns the number of available bytes inside of the queue.
	 *
	 * @return The number of bytes in the queue.
	 * @since 2016/05/01
	 */
	public final int available()
	{
		// Lock
		synchronized (this.lock)
		{
			return this._total;
		}
	}
	
	/**
	 * Clears the queue and every associated byte.
	 *
	 * @since 2016/08/02
	 */
	public final void clear()
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains but does not remove the first byte.
	 *
	 * @return The value of the first byte.
	 * @throws NoSuchElementException If the deque is empty.
	 * @since 2016/05/01
	 */
	public final byte getFirst()
		throws NoSuchElementException
	{
		// Lock
		synchronized (this.lock)
		{
			byte[] solo = _solo;
			getFirst(solo, 0, 1);
			return solo[0];
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
	public final int getFirst(byte[] __b)
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
	public final int getFirst(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Obtains but does not remove the last byte.
	 *
	 * @return The value of the last byte.
	 * @throws NoSuchElementException If the deque is empty.
	 * @since 2016/05/01
	 */
	public final byte getLast()
		throws NoSuchElementException
	{
		// Lock
		synchronized (this.lock)
		{
			byte[] solo = _solo;
			getLast(solo, 0, 1);
			return solo[0];
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
	public final int getLast(byte[] __b)
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
	public final int getLast(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
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
	public final boolean offerFirst(byte __b)
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
	public final boolean offerFirst(byte[] __b)
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
	public final boolean offerFirst(byte[] __b, int __o, int __l)
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
	public final boolean offerLast(byte __b)
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
	public final boolean offerLast(byte[] __b)
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
	public final boolean offerLast(byte[] __b, int __o, int __l)
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
	public final int peekFirst()
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
	public final int peekLast()
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
	public final byte removeFirst()
		throws NoSuchElementException
	{
		// Lock
		synchronized (this.lock)
		{
			byte[] solo = _solo;
			int rv = removeFirst(solo, 0, 1);
			if (rv == 1)
				return solo[0];
			
			// {@squirreljme.error AE03 Did not read a single byte. (The
			// read count)}
			throw new NoSuchElementException(String.format("AE03 %d", rv));
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
	public final int removeFirst(byte[] __b)
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
	public final int removeFirst(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// If nothing to remove, do nothing
		if (__l == 0)
			return 0;
		
		// Lock
		synchronized (this.lock)
		{
			// If the queue is empty do nothing
			int total = this._total;
			if (total == 0)
				return 0;
			
			// Limit the number of bytes to read to the total
			int limit = Math.min(__l, total);
			int newtotal = total - limit;
			
			// Get some things
			LinkedList<byte[]> blocks = this._blocks;
			int nb = blocks.size();
			int head = this._head, tail = this._tail;
			
			// Write bytes into the target
			int at = __o;
			int left = limit;
			int bs = _BLOCK_SIZE;
			int bm = _BLOCK_MASK;
			while (left > 0)
			{
				// Get the first block
				byte[] bl = blocks.getFirst();
				boolean lastbl = (blocks.size() == 1);
				
				// Determine the max number of bytes to read
				int rc = Math.min((lastbl ? bs - tail : bs), limit);
				
				// Read bytes into the target
				for (int i = 0; i < rc; i++)
				{
					__b[at++] = bl[(head = ((head + 1) & bm))];
					
					// If cycled, remove the first block
					if (head == 0 || (lastbl && head == tail))
						blocks.removeFirst();
				}
				
				// Bytes were removed
				left -= rc;
			}
			
			// Emptied? Clear head/tail pointers
			if (newtotal == 0)
				head = tail = 0;
			
			// Set details
			this._total = newtotal;
			this._head = head;
			this._tail = tail;
			
			// Debug
			__DEBUG();
			
			// Return the read count
			return limit;
		}
	}
	
	/**
	 * Removes a single byte from the from of the deque.
	 *
	 * @return The next input byte.
	 * @throws NoSuchElementException If not a single byte is available.
	 * @since 2016/05/01
	 */
	public final byte removeLast()
		throws NoSuchElementException
	{
		// Lock
		synchronized (this.lock)
		{
			byte[] solo = _solo;
			int rv = removeLast(solo, 0, 1);
			if (rv == 1)
				return solo[0];
			throw new NoSuchElementException("NSEE");
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
	public final int removeLast(byte[] __b)
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
	public final int removeLast(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * DEBUG ONLY.
	 *
	 * @since 2016/08/03
	 */
	@Deprecated
	private final void __DEBUG()
	{
		System.err.printf("DEBUG -- T=%d h=%d t=%d%n", this._total,
			this._head, this._tail);
		StringBuilder sb = new StringBuilder();
		for (byte[] bl : this._blocks)
		{
			sb.append("[");
			for (int i = 0; i < bl.length; i++)
			{
				if (i > 0)
					sb.append(' ');
				
				if (i == this._tail && i == this._head)
					sb.append('X');
				else if (i == this._tail)
					sb.append('T');
				else if (i == this._head)
					sb.append('H');
				else
					sb.append(' ');
				sb.append(String.format("%02x", bl[i]));
			}
			sb.append("]");
		}
		System.err.printf("DEBUG -- %s%n", sb);
	}
}

