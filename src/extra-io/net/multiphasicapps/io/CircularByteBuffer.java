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

/**
 * This is a circular buffer which provides bytes for input and output as a
 * queue.
 *
 * If the queue reaches full capacity then it is increased in size.
 *
 * @since 2016/03/11
 */
public class CircularByteBuffer
{
	/** Lock. */
	protected final Object lock;
	
	/** Visible lock to the bit buffer. */
	final Object _lock;
	
	/**
	 * Initializes a circular byte buffer.
	 *
	 * @since 2016/03/11
	 */
	public CircularByteBuffer()
	{
		this(new Object());
	}
	
	/**
	 * Initializes a circular byte buffer with the given lock object.
	 *
	 * @param __lock The lock to use, if {@code null} then one is initialized.
	 * @since 2016/03/11
	 */
	public CircularByteBuffer(Object __lock)
	{
		_lock = lock = (__lock != null ? __lock : new Object());
	}
	
	/**
	 * Offers a single byte and adds it to the start of the queue.
	 *
	 * @param __b The byte to offer.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularByteBuffer offerFirst(byte __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Offers the given byte array and adds the bytes to the start of the
	 * queue.
	 *
	 * @param __b The buffer to add to the queue.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final CircularByteBuffer offerFirst(byte... __b)
		throws NullPointerException
	{
		return offerFirst(__b, 0, __b.length);
	}
	
	/**
	 * Offers multiple bytes within the given offset and length and adds them
	 * to the start of the queue.
	 *
	 * @param __b The array to source bytes from.
	 * @param __o The offset to within the buffer.
	 * @param __l The number of bytes to offer.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final CircularByteBuffer offerFirst(byte[] __b, int __o, int __l)
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
	 * Offers a single byte and adds it to the end of the queue.
	 *
	 * @param __b The byte to offer.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularByteBuffer offerLast(byte __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Offers the given byte array and adds the bytes to the end of the
	 * queue.
	 *
	 * @param __b The buffer to add to the queue.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final CircularByteBuffer offerLast(byte... __b)
		throws NullPointerException
	{
		return offerLast(__b, 0, __b.length);
	}
	
	/**
	 * Offers multiple bytes within the given offset and length and adds them
	 * to the end of the queue.
	 *
	 * @param __b The array to source bytes from.
	 * @param __o The offset to within the buffer.
	 * @param __l The number of bytes to offer.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final CircularByteBuffer offerLast(byte[] __b, int __o, int __l)
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
}

