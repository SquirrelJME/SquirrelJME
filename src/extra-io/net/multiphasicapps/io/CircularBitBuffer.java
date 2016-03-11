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
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The backing circular byte buffer. */
	protected final CircularByteBuffer backing;
	
	/**
	 * Initializes a circular bit buffer.
	 *
	 * @since 2016/03/11
	 */
	public CircularBitBuffer()
	{
		this(new CircularByteBuffer());
	}
	
	/**
	 * Initializes a circular bit buffer which uses the given circular byte
	 * buffer as a backing storage area for complete bytes.
	 *
	 * @param __w The circular byte buffer to use as storage.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public CircularBitBuffer(CircularByteBuffer __w)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException();
		
		// Set
		backing = __w;
	}
	
	/**
	 * Returns the backing circular byte buffer.
	 *
	 * @return The circular byte buffer which backs this.
	 * @since 2016/03/11
	 */
	public final CircularByteBuffer backing()
	{
		return backing;
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
		throw new Error("TODO");
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
		throw new Error("TODO");
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
}

