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
 * @since 2016/03/11
 */
public class CircularBooleanBuffer
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
	public CircularBooleanBuffer()
	{
		super();
	}
	
	/**
	 * Initializes a circular bit buffer with the given lock.
	 *
	 * @param __lock The lock to use.
	 * @since 2016/03/11
	 */
	public CircularBooleanBuffer(Object __lock)
	{
		super(__lock);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	protected int arrayLength(boolean[] __arr)
	{
		return __arr.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	protected boolean[] arrayNew(int __len)
	{
		return new boolean[__len];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	protected Boolean arrayRead(boolean[] __arr, int __dx)
	{
		return __arr[__dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	protected CircularGenericBuffer arrayWrite(boolean[] __arr, int __dx,
		Boolean __v)
	{
		__arr[__dx] = __v.booleanValue();
		return this;
	}
	
	/**
	 * Offers a primitive value to the start of the queue.
	 *
	 * @param __b Value to add.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularBooleanBuffer offerFirst(boolean __b)
	{
		return (CircularBooleanBuffer)super.offerFirst(Boolean.valueOf(__b));
	}
	
	/**
	 * Offers a primitive value to the end of the queue.
	 *
	 * @param __b Value to add.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularBooleanBuffer offerLast(boolean __b)
	{
		return (CircularBooleanBuffer)super.offerLast(Boolean.valueOf(__b));
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
	public final CircularBooleanBuffer offerLastInt(int __val, int __mask)
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
	public final CircularBooleanBuffer offerLastInt(int __val, int __mask,
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
	 * Removes the first integer value with the given number of bits.
	 *
	 * @param __bits The number of bits to read.
	 * @return The read integer value with the given bit count.
	 * @throws IllegalArgumentException If bits is zero or higher than 32.
	 * @throws NoSuchElementException If not enough bits are available.
	 * @since 2016/03/11
	 */
	public int removeFirstInt(int __bits)
		throws IllegalArgumentException, NoSuchElementException
	{
		return removeFirstInt(__bits, false);
	}
	
	/**
	 * Removes the first integer value with the given number of bits.
	 *
	 * @param __bits The number of bits to read.
	 * @param __msb If {@code true} then the returned value is written to
	 * with higher shifts first.
	 * @return The read integer value with the given bit count.
	 * @throws IllegalArgumentException If bits is zero, negative, or higher
	 * than 32.
	 * @throws NoSuchElementException If not enough bits are available.
	 * @since 2016/03/11
	 */
	public int removeFirstInt(int __c, boolean __msb)
		throws IllegalArgumentException, NoSuchElementException
	{
		// Check
		if (__c <= 0 || __c > 32)
			throw new IllegalArgumentException();
		
		// Lock
		synchronized (lock)
		{
			// Not enough bits
			if (available() < __c)
				throw new NoSuchElementException();
			
			// Return value
			int rv = 0;
			
			// Read input bits
			int an = (__msb ? -1 : 1);
			for (int i = 0, at = (__msb ? __c - 1 : 0); i >= 0 && i < __c;
				i++, at += an)
				if (removeFirst())
					rv |= (1L << at);
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Removes the first element, but returns a primitive type.
	 *
	 * @return The next primitive value.
	 * @throws NoSuchElementException If no elements remain.
	 * @since 2016/03/11
	 */
	public boolean removeFirstPrimitive()
		throws NoSuchElementException
	{
		return super.removeFirst().booleanValue();
	}
	
	/**
	 * Removes the first element, but returns a primitive type.
	 *
	 * @return The next primitive value.
	 * @throws NoSuchElementException If no elements remain.
	 * @since 2016/03/11
	 */
	public boolean removeLastPrimitive()
		throws NoSuchElementException
	{
		return super.removeLast().booleanValue();
	}
}

