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

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
		Math.min(4, Integer.getInteger(
			"net.multiphasicapps.util.datadeque.blocksize", 64));
	
	/** The block size mask. */
	private static final int _BLOCK_MASK =
		_BLOCK_SIZE - 1;
	
	/** The shift to convert block based values. */
	private static final int _BLOCK_SHIFT =
		Integer.numberOfTrailingZeros(_BLOCK_SIZE);
	
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
			
			// Keep adding in data
			int bs = _BLOCK_SIZE;
			int bm = _BLOCK_MASK;
			int left = __l;
			int at = __o;
			while (left > 0)
			{
				// If the tail is at the start of the block then a new one
				// must be created
				byte[] bl;
				if (tail == 0)
				{
					bl = new byte[bs];
					blocks.addLast(bl);
				}
				
				// Otherwise get the last one
				else
					bl = blocks.getLast();
				
				// Only can fit a single block
				int limit = Math.min(bs - tail, left);
				
				// Write data
				for (int i = 0; i < limit; i++)
					bl[tail++] = __b[at++];
				
				// Masking is only needed after the write
				tail &= bm;
				
				// Consumed bytes
				left -= limit;
			}
			
			// Set new details
			this._total = newtotal;
			this._tail = tail;
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
			// Reset variables
			this._total = 0;
			this._head = 0;
			this._tail = 0;
			
			// Zero out all blocks (for security and better compression)
			LinkedList<byte[]> blocks = this._blocks;
			for (byte[] bl : blocks)
				Arrays.fill(bl, (byte)0);
			blocks.clear();
		}
	}
	
	/**
	 * Deletes the specified number of bytes at the start of the deque.
	 *
	 * @param __l The number of bytes to delete.
	 * @return The number of deleted bytes.
	 * @throws IndexOutOfBoundsException If the number of bytes is negative.
	 * @since 2016/08/04
	 */
	public final int deleteFirst(int __l)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error AE0d Attempt to delete starting from a negative
		// address.}
		if (__l < 0)
			throw new IndexOutOfBoundsException("AE0d");
		
		// Do nothing
		if (__l == 0)
			return 0;
		
		// Lock
		synchronized (this.lock)
		{
			// If the queue is empty do nothing
			int total = this._total;
			if (total == 0)
				return 0;
			
			// Do not remove more bytes than there are available
			int limit = Math.min(__l, total);
			int newtotal = total - limit;
			
			// Get some things
			LinkedList<byte[]> blocks = this._blocks;
			int nb = blocks.size();
			int head = this._head, tail = this._tail;
			
			// Write bytes into the target
			int left = limit;
			int bs = _BLOCK_SIZE;
			int bm = _BLOCK_MASK;
			while (left > 0)
			{
				// Get the first block
				byte[] bl = blocks.getFirst();
				boolean lastbl = (blocks.size() == 1);
				
				// Determine the max number of bytes to delete
				int rc = Math.min((lastbl ? (tail == 0 ? bs : tail) - head :
					bs - head), left);
				
				// Should never occur, because that means the end is lower
				// than the start
				if (rc < 0)
					throw new RuntimeException("OOPS");
				
				// Erase data
				for (int i = 0; i < rc; i++)
					bl[head++] = 0;
				
				// Mask the head to detect overflow
				head &= bm;
				
				// If cycled, remove the first block
				if (head == 0 || (lastbl && head == tail))
					blocks.removeFirst();
				
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
			
			// Return the erase count
			return limit;
		}
	}
	
	/**
	 * Gets a single byte offset from the start of the deque as if it were an
	 * array.
	 *
	 * @param __a The index to get the byte value of.
	 * @return The byte at the given position.
	 * @throws IndexOutOfBoundsException If the address is not within bounds.
	 * @since 2016/08/03
	 */
	public final byte get(int __a)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error AE0a Request get at a negative index.}
		if (__a < 0)
			throw new IndexOutOfBoundsException("AE0a");
		
		// Lock
		synchronized (this.lock)
		{
			byte[] solo = this._solo;
			int rv = get(__a, solo, 0, 1);
			if (rv == 1)
				return solo[0];
			
			// {@squirreljme.error AE09 Could not get the byte at the
			// given position because it exceeds the deque bounds. (The index)}
			throw new IndexOutOfBoundsException(String.format("AE09 %d", __a));
		}
	}
	
	/**
	 * Gets multiple bytes offset from the start of the deque as if it were
	 * and array.
	 *
	 * @param __a The index to start reading values from.
	 * @param __b The destination array for values.
	 * @return The number of bytes read.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * bounds of the deque.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/03
	 */
	public final int get(int __a, byte[] __b)
		throws IndexOutOfBoundsException, NullPointerException
	{
		return this.get(__a, __b, 0, __b.length);
	}
	
	/**
	 * Gets multiple bytes offset from the start of the deque as if it were
	 * and array.
	 *
	 * @param __a The index to start reading values from.
	 * @param __b The destination array for values.
	 * @param __o Where to start writing destination values.
	 * @param __l The number of bytes to read.
	 * @return The number of bytes read.
	 * @throws IndexOutOfBoundsException If the address is not within the
	 * bounds of the deque, the offset and/or length are negative, or the
	 * offset and length exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/03
	 */
	public final int get(int __a, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// {@squirreljme.error AE0b Request get at a negative index.}
		if (__a < 0)
			throw new IndexOutOfBoundsException("AE0b");
		
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error AE0c The requested address is outside of
			// the bounds of the queue. (The requested address; The number of
			// bytes in the queue)}
			int total = this._total;
			if (__a < 0 || __a >= total)
				throw new IndexOutOfBoundsException(String.format("AE0c %d %d",
					__a, total));
			
			// If there are no bytes, all reads do nothing
			if (total <= 0)
				return 0;
			
			// If the address is within the starting half then seek from the
			// start, otherwise start from the trailing end
			/*if (__a < (total >> 1))
				return __getViaFirst(__a, __b, __o, __l);*/
			return __getViaLast(__a, __b, __o, __l);
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
			byte[] solo = this._solo;
			int rv = getFirst(solo, 0, 1);
			if (rv == 1)
				return solo[0];
			
			// {@squirreljme.error AE07 Could not get the first byte
			// because the deque is empty.}
			throw new NoSuchElementException("AE07");
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
		// Lock
		synchronized (this.lock)
		{
			// This is the same of an any position get at the start
			return this.get(0, __b, __o, __l);
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
			byte[] solo = this._solo;
			int rv = getLast(solo, 0, 1);
			if (rv == 0)
				return solo[0];
			
			// {@squirreljme.error AE06 Could not remove the last byte because
			// the deque is empty.}
			throw new NoSuchElementException("AE06");
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
		// Check, the length is used so make sure it is positive
		if (__l < 0)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (this.lock)
		{
			// This is the same of an any position get from the end
			int total = this._total;
			return this.get(Math.max(0, total - __l), __b, __o, __l);
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
			byte[] solo = this._solo;
			int rv = removeFirst(solo, 0, 1);
			if (rv == 1)
				return solo[0];
			
			// {@squirreljme.error AE03 Could not remove the first byte
			// because the deque is empty.}
			throw new NoSuchElementException("AE03");
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
			
			// A remove is a get followed by a delete
			int rva = get(0, __b, __o, __l);
			int rvb = deleteFirst(__l);
			
			// If this occurs then the number of bytes deleted was not the
			// same as the number of bytes which were read.
			if (rva != rvb)
				throw new RuntimeException("OOPS");
			
			// Return the read count
			return rva;
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
			
			// {@squirreljme.error AE08 Could not remove the last byte because
			// the deque is empty.}
			throw new NoSuchElementException("AE08");
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
	 * Obtains bytes starting from the head side.
	 *
	 * @param __a The address to read.
	 * @param __b The destination array.
	 * @param __o The output offset into the array.
	 * @param __l The number of bytes to read.
	 * @return The number of bytes read.
	 * @since 2016/08/04
	 */
	private final int __getViaFirst(int __a, byte[] __b, int __o, int __l)
	{
		// Get some things
		int total = this._total;
		LinkedList<byte[]> blocks = this._blocks;
		int nb = blocks.size();
		int head = this._head, tail = this._tail;
		
		// Need to seek, then read the data
		Iterator<byte[]> it = blocks.iterator();
		int at = __o;
		int left = __l;
		int bls = _BLOCK_SIZE;
		int rel = 0;
		for (boolean firstbl = true; left > 0; firstbl = false)
		{
			// No more blocks?
			if (!it.hasNext())
				break;
			
			// Get block here
			byte[] bl = it.next();
			
			// Last block?
			boolean lastbl = !it.hasNext();
			
			// The first and the only block start from the head
			int bs, be;
			if (firstbl)
				bs = head;
			else
				bs = 0;
			
			// The last and the only block ends at the tail
			if (lastbl)
				be = (tail == 0 ? bls : tail);
			else
				be = bls;
			
			// Bytes in the block
			int bn = be - bs;
			
			// Determine the end of this block
			int nextrel = rel + bn;
			
			// Reading data
			if (nextrel > __a)
			{
				// Read offset from the block
				int baseread;
				if (__a >= rel && __a < nextrel)
					baseread = bs + (__a - rel);
			
				// Data always starts at the block start
				else
					baseread = bs;
				
				// Bytes to read
				int limit = Math.min(left, be - baseread);
				
				// Copy them
				for (int i = 0, s = baseread; i < limit; i++)
					__b[at++] = bl[s++];
				
				// Read these bytes
				left -= limit;
			}
			
			// Where the next block starts
			rel = nextrel;
		}
		
		// Return the read count
		return (__l - left);
	}
	
	/**
	 * Obtains bytes starting from the tail side.
	 *
	 * @param __a The address to read.
	 * @param __b The destination array.
	 * @param __o The output offset into the array.
	 * @param __l The number of bytes to read.
	 * @return The number of bytes read.
	 * @since 2016/08/04
	 */
	private final int __getViaLast(int __a, byte[] __b, int __o, int __l)
	{
		// Get some things
		int total = this._total;
		LinkedList<byte[]> blocks = this._blocks;
		int nb = blocks.size();
		int head = this._head, tail = this._tail;
		
		// The number of bytes to actually read
		int limit = Math.min(__l, total - __a);
		
		// Every byte in the array is offset by the head (so if the head is 2
		// then byte 42 is at position 44).
		int trueaddr = __a + head;
		
		// Determine the number of blocks to initially skip before this one
		// is reached.
		int bs = _BLOCK_SIZE;
		int bm = _BLOCK_MASK;
		int blskip = (total - trueaddr) >> _BLOCK_SHIFT;
		int truehead = (trueaddr & bm);
		
		// Skip blocks to reach where the address starts off at, list iterate
		// on the last block so `next` returns that block. If skipping at least
		// one block then the block where data is read is .previous().next()
		ListIterator<byte[]> it = blocks.listIterator(nb - 1);
		for (int i = 0; i < blskip; i++)
			it.previous();
		
		// Read in until no bytes remain
		int left = limit;
		int at = __o;
		for (boolean firstread = true; left > 0; firstread = false)
		{
			// Get the block data here
			byte[] bl = it.next();
			
			// Is this the first/last block in the chain?
			boolean lastbl = !it.hasNext();
			boolean firstbl = !it.hasPrevious();
			boolean onlyblock = (lastbl | firstbl);
			
			// If this is the only block then the tail is significant as it
			// indicates the end.
			// The same goes for the last block in the chain
			int rc;
			int vtail = (tail == 0 ? bs : tail);
			if (onlyblock || lastbl)
				rc = vtail - truehead;
			
			// If this is the first block then read from the end to the true
			// head position (which may be zero).
			// Also if this is the first read then truehead will be important.
			else
				rc = bs - truehead;
			
			// Limit to the left over bytes
			rc = Math.min(left, rc);
			
			// {@squirreljme.error AE0e A condition which should not occur
			// has occurred. (The read count; The number of bytes left to
			// read; Is this the fist block?; Is this the last block?; Is
			// this the only block?; Is this the first block to read?;
			// The true head position)}
			if (rc <= 0)
				throw new RuntimeException(String.format("AE0e %d %d %s %s " +
					"%s %s %d",
					rc, left, firstbl, lastbl, onlyblock, firstread,
					truehead));
			
			// Read in data
			for (int i = 0; i < rc; i++)
				__b[at++] = bl[truehead++];
			
			// Clear true head for the next read, it initially gets the value
			// but after the first read block it has no meaning.
			truehead = 0;
			
			// Read these bytes, can ignore them
			left -= rc;
		}
		
		// Return the read byte count
		return limit;
	}
}

