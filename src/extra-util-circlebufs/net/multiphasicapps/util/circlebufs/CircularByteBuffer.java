// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.circlebufs;

import java.util.NoSuchElementException;

/**
 * This is a circular buffer which provides bytes for input and output as a
 * queue.
 *
 * If the queue reaches full capacity then it is increased in size.
 *
 * @since 2016/03/11
 */
public class CircularByteBuffer
	extends CircularGenericBuffer<byte[], Byte>
{
	/**
	 * Initializes a circular byte buffer.
	 *
	 * @since 2016/03/11
	 */
	public CircularByteBuffer()
	{
		super();
	}
	
	/**
	 * Initializes a circular byte buffer with the given lock object.
	 *
	 * @param __lock The lock to use, if {@code null} then one is initialized.
	 * @since 2016/03/11
	 */
	public CircularByteBuffer(Object __lock)
	{
		super(__lock);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	@Deprecated
	protected int arrayLength(byte[] __arr)
	{
		return __arr.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	@Deprecated
	protected byte[] arrayNew(int __len)
	{
		return new byte[__len];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	@Deprecated
	protected Byte arrayRead(byte[] __arr, int __dx)
	{
		return __arr[__dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	@Deprecated
	protected CircularGenericBuffer arrayWrite(byte[] __arr, int __dx,
		Byte __v)
	{
		__arr[__dx] = __v.byteValue();
		return this;
	}
	
	/**
	 * Offers a primitive value to the start of the queue.
	 *
	 * @param __b Value to add.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularByteBuffer offerFirst(byte __b)
	{
		return (CircularByteBuffer)super.offerFirst(Byte.valueOf(__b));
	}
	
	/**
	 * Offers a primitive value to the end of the queue.
	 *
	 * @param __b Value to add.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularByteBuffer offerLast(byte __b)
	{
		return (CircularByteBuffer)super.offerLast(Byte.valueOf(__b));
	}
	
	/**
	 * Removes the first element, but returns a primitive type.
	 *
	 * @return The next primitive value.
	 * @throws NoSuchElementException If no elements remain.
	 * @since 2016/03/11
	 */
	public byte removeFirstPrimitive()
		throws NoSuchElementException
	{
		return super.removeFirst().byteValue();
	}
	
	/**
	 * Removes the first element, but returns a primitive type.
	 *
	 * @return The next primitive value.
	 * @throws NoSuchElementException If no elements remain.
	 * @since 2016/03/11
	 */
	public byte removeLastPrimitive()
		throws NoSuchElementException
	{
		return super.removeLast().byteValue();
	}
}

