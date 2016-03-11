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
 * This is a data processor which is given input bytes and performs
 * transformation of the input and provides an output.
 *
 * Data processors must be able to handle situations where partial information
 * and state is available, that is if there is not enough input available it
 * can continue when there is input.
 *
 * @since 2016/03/11
 */
public abstract class DataProcessor
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Data used for input to the data processor. */
	protected final CircularByteBuffer input =
		new CircularByteBuffer(lock);
	
	/** Data which has been output by the data processor. */
	protected final CircularByteBuffer output =
		new CircularByteBuffer(lock);
	
	/**
	 * Offers a single byte to the processor input.
	 *
	 * @param __b The byte to offer.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public final DataProcessor offer(byte __b)
	{
		// Lock
		synchronized (lock)
		{
			input.offerLast(__b);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Offers the given byte array to the processor input.
	 *
	 * @param __b The buffer to add to the queue.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public final DataProcessor offer(byte... __b)
		throws NullPointerException
	{
		return offer(__b, 0, __b.length);
	}
	
	/**
	 * Offers bytes within the given range of the array to the processor
	 * input.
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
	public final DataProcessor offer(byte[] __b, int __o, int __l)
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
				offer(__b[__o + i]);
		}
		
		// Self
		return this;
	}
}

