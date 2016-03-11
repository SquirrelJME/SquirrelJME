// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.util.NoSuchElementException;

/**
 * This is a circular buffer which provides bits for input and output as a kind
 * of queue.
 *
 * If the queue reaches full capacity then it is increased in size.
 *
 * Note that this buffer is backed by a {@link CircularByteBuffer}, however
 * any currently active bits for writing are not inside of the backed circular
 * byte buffer.
 *
 * @since 2016/03/11
 */
public class CircularBitBuffer
	extends CircularGenericBuffer<boolean[], Boolean>
{
	/** The internal buffer. */
	private volatile boolean[] _buffer;
	
	/** Head of the buffer. */
	private volatile int _head =
		-1;
	
	/** Tail of the buffer. */
	private volatile int _tail =
		-1;
	
	/**
	 * Initializes a circular bit buffer.
	 *
	 * @since 2016/03/11
	 */
	public CircularBitBuffer()
	{
		super();
	}
	
	/**
	 * Initializes a circular bit buffer with the given lock.
	 *
	 * @param __lock The lock to use.
	 * @since 2016/03/11
	 */
	public CircularBitBuffer()
	{
		super(__lock);
	}
	
	/**
	 * Returns the number of available bits in the buffer.
	 *
	 * @return The bit availability count.
	 * @since 2016/03/11
	 */
	public int available()
	{
		// Lock
		synchronized (lock)
		{
			// If no buffer, will always be empty
			boolean[] buf = _buffer;
			if (_buffer == null)
				return 0;
			
			// Get head and tail positions
			long head = _head;
			long tail = _tail;
			
			// If the tail is less than the head, add buffer size
			if (tail < head)
				tail += buf.length;
			
			// Return the byte location difference
			return (int)(tail - head);
		}
	}
	
	/**
	 * Returns {@code true} if bits are available.
	 *
	 * @return {@code true} if bits are available.
	 * @since 2016/03/11
	 */
	public boolean hasAvailable()
	{
		return available() > 0;
	}
	
	/**
	 * Offers a single bit and adds it to the start of the queue.
	 *
	 * @param __b The bit to offer.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularBitBuffer offerFirst(boolean __b)
	{
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Offers the given bit array and adds the bits to the start of the
	 * queue.
	 *
	 * @param __b The buffer to add to the queue.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final CircularBitBuffer offerFirst(boolean... __b)
		throws NullPointerException
	{
		return offerFirst(__b, 0, __b.length);
	}
	
	/**
	 * Offers multiple bits within the given offset and length and adds them
	 * to the start of the queue.
	 *
	 * @param __b The array to source bits from.
	 * @param __o The offset to within the buffer.
	 * @param __l The number of bits to offer.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final CircularBitBuffer offerFirst(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException();
		
		// Lock
		synchronized (lock)
		{
			for (int i = __l - 1; i >= 0; i--)
				offerFirst(__b[__o + i]);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Offers a single bit and adds it to the end of the queue.
	 *
	 * @param __b The bit to offer.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularBitBuffer offerLast(boolean __b)
	{
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Offers the given bit array and adds the bits to the end of the
	 * queue.
	 *
	 * @param __b The buffer to add to the queue.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final CircularBitBuffer offerLast(boolean... __b)
		throws NullPointerException
	{
		return offerLast(__b, 0, __b.length);
	}
	
	/**
	 * Offers multiple bits within the given offset and length and adds them
	 * to the end of the queue.
	 *
	 * @param __b The array to source bits from.
	 * @param __o The offset to within the buffer.
	 * @param __l The number of bits to offer.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final CircularBitBuffer offerLast(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException();
		
		// Lock
		synchronized (lock)
		{
			for (int i = 0; i < __l; i++)
				offerLast(__b[__o + i]);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Adds an integer value to the end of the buffer.
	 *
	 * @param __val Value to add.
	 * @param __mask The mask of the value.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If any bit is set that is not within
	 * the mask, or the mask has a zero gap in it.
	 * @since 2016/03/11
	 */
	public final CircularBitBuffer offerLastInt(int __val, int __mask)
		throws IllegalArgumentException
	{
		return offerLastInt(__val, __mask, false);
	}
	
	/**
	 * Adds an integer value to the end of the buffer.
	 *
	 * @param __val Value to add.
	 * @param __mask The mask of the value.
	 * @param __msb If {@code true} then the bits are added from the higher
	 * shift then to the lower shift.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If any bit is set that is not within
	 * the mask, or the mask has a zero gap in it.
	 * @since 2016/03/11
	 */
	public final CircularBitBuffer offerLastInt(int __val, int __mask,
		boolean __msb)
		throws IllegalArgumentException
	{
		// Number of bits in the mask
		int ibm = Integer.bitCount(__mask);
		
		// Check to make sure the input is valid
		if ((__val & (~__mask)) != 0)
			throw new IllegalArgumentException();
		if (ibm != (32 - Integer.numberOfLeadingZeros(__mask)) ||
			(__mask & 1) == 0)
			throw new IllegalArgumentException();
		
		// Lock
		synchronized (lock)
		{
			int an = (__msb ? -1 : 1);
			for (int at = (__msb ? ibm - 1 : 0); at >= 0 && at < ibm; at += an)
				offerLast(0 != (__val & (1 << at)));
		}
		
		// Self
		return this;
	}
	
	/**
	 * Reads and removes the first available bit, if one is not available
	 * then an exception is thrown.
	 *
	 * @return The next value.
	 * @throws NoSuchElementException If no values are available.
	 * @since 2016/03/11
	 */
	public boolean removeFirst()
		throws NoSuchElementException
	{
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Reads and removes the first available bits and places them within the
	 * given array.
	 *
	 * @param __b The array to write bit values into.
	 * @return The number of bits which were removed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeFirst(boolean[] __b)
		throws NullPointerException
	{
		return removeFirst(__b, 0, __b.length);
	}
	
	/**
	 * Reads and removes multiple bits at the start of the queue up to the
	 * length and places them into the given array.
	 *
	 * @param __b The array to write bit values into.
	 * @param __o The offset into the array to start writing at.
	 * @param __l The maximum number of bits to remove.
	 * @return The number of removed bits.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeFirst(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException();
		
		// Lock
		synchronized (lock)
		{
			// Total
			int rc = 0;
			
			// Remove bits
			for (int i = 0; i < __l; i++)
				try
				{
					__b[__o + i] = removeFirst();
					rc++;
				}
				
				// Return the number of read bits
				catch (NoSuchElementException nsee)
				{
					return rc;
				}
			
			// Return the read count
			return rc;	
		}
	}
	
	/**
	 * Reads and removes the last available bit, if one is not available
	 * then an exception is thrown.
	 *
	 * @return The next value.
	 * @throws NoSuchElementException If no values are available.
	 * @since 2016/03/11
	 */
	public boolean removeLast()
		throws NoSuchElementException
	{
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Reads and removes the last available bits and places them within the
	 * given array.
	 *
	 * @param __b The array to write bit values into.
	 * @return The number of bits which were removed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeLast(boolean[] __b)
		throws NullPointerException
	{
		return removeLast(__b, 0, __b.length);
	}
	
	/**
	 * Reads and removes multiple bits at the end of the queue up to the
	 * length and places them into the given array.
	 *
	 * @param __b The array to write bit values into.
	 * @param __o The offset into the array to start writing at.
	 * @param __l The maximum number of bits to remove.
	 * @return The number of removed bits.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeLast(boolean[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException();
		
		// Lock
		synchronized (lock)
		{
			// Total
			int rc = 0;
			
			// Remove bits
			for (int i = __l - 1; i >= 0; i--)
				try
				{
					__b[__o + i] = removeLast();
					rc++;
				}
				
				// Return the number of read bits
				catch (NoSuchElementException nsee)
				{
					return rc;
				}
			
			// Return the read count
			return rc;	
		}
	}
}

