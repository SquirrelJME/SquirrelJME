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
	public CircularBitBuffer(Object __lock)
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
		__arr[__dx] = __v;
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
}

