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
	 * Returns the number of bytes available in the queue.
	 *
	 * @return The available bytes.
	 * @since 2016/03/11
	 */
	public int available()
	{
		// Lock
		synchronized (lock)
		{
			// If no buffer, will always be empty
			byte[] buf = _buffer;
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
	 * Returns {@code true} if bytes are available.
	 *
	 * @return {@code true} if bytes are available.
	 * @since 2016/03/11
	 */
	public boolean hasAvailable()
	{
		return available() > 0;
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
		synchronized (lock)
		{
			throw new Error("TODO");
		}
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
	public CircularByteBuffer offerFirst(byte... __b)
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
	public CircularByteBuffer offerFirst(byte[] __b, int __o, int __l)
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
		synchronized (lock)
		{
			throw new Error("TODO");
		}
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
	public CircularByteBuffer offerLast(byte... __b)
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
	public CircularByteBuffer offerLast(byte[] __b, int __o, int __l)
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
	 * Reads and removes the first available byte, if one is not available
	 * then an exception is thrown.
	 *
	 * @return The next value.
	 * @throws NoSuchElementException If no values are available.
	 * @since 2016/03/11
	 */
	public byte removeFirst()
		throws NoSuchElementException
	{
		synchronized (lock)
		{
			// If not buffer, then there is nothing to remove
			byte[] buf = _buffer;
			if (buf == null)
				throw new NoSuchElementException();
			
			// Get head and tail position
			int head = _head;
			int tail = _tail;
			
			// If the head is at the tail, cannot get
			if (head == tail)
				throw new NoSuchElementException();
			
			// Get value here
			byte rv = buf[head];
			
			// Clear it to invalidate it (just in case)
			buf[head] = 0;
			
			// Increment head position
			_head = (head + 1) & (buf.length - 1);
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Reads and removes the first available bytes and places them within the
	 * given array.
	 *
	 * @param __b The array to write byte values into.
	 * @return The number of bytes which were removed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeFirst(byte[] __b)
		throws NullPointerException
	{
		return removeFirst(__b, 0, __b.length);
	}
	
	/**
	 * Reads and removes multiple bytes at the start of the queue up to the
	 * length and places them into the given array.
	 *
	 * @param __b The array to write byte values into.
	 * @param __o The offset into the array to start writing at.
	 * @param __l The maximum number of bytes to remove.
	 * @return The number of removed bytes.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeFirst(byte[] __b, int __o, int __l)
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
			
			// Remove bytes
			for (int i = 0; i < __l; i++)
				try
				{
					__b[__o + i] = removeFirst();
					rc++;
				}
				
				// Return the number of read bytes
				catch (NoSuchElementException nsee)
				{
					return rc;
				}
			
			// Return the read count
			return rc;	
		}
	}
	
	/**
	 * Reads and removes the last available byte, if one is not available
	 * then an exception is thrown.
	 *
	 * @return The next value.
	 * @throws NoSuchElementException If no values are available.
	 * @since 2016/03/11
	 */
	public byte removeLast()
		throws NoSuchElementException
	{
		synchronized (lock)
		{
			// If not buffer, then there is nothing to remove
			byte[] buf = _buffer;
			if (buf == null)
				throw new NoSuchElementException();
			
			// Get head and tail position
			int head = _head;
			int tail = _tail;
			
			// If the tail is at the head, cannot get
			if (head == tail)
				throw new NoSuchElementException();
			
			// Get value here
			int from = tail - 1;
			byte rv = buf[from];
			
			// Clear it to invalidate it (just in case)
			buf[from] = 0;
			
			// Decrement tail position
			_tail = (from) & (buf.length - 1);
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Reads and removes the last available bytes and places them within the
	 * given array.
	 *
	 * @param __b The array to write byte values into.
	 * @return The number of bytes which were removed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeLast(byte[] __b)
		throws NullPointerException
	{
		return removeLast(__b, 0, __b.length);
	}
	
	/**
	 * Reads and removes multiple bytes at the end of the queue up to the
	 * length and places them into the given array.
	 *
	 * @param __b The array to write byte values into.
	 * @param __o The offset into the array to start writing at.
	 * @param __l The maximum number of bytes to remove.
	 * @return The number of removed bytes.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeLast(byte[] __b, int __o, int __l)
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
			
			// Remove bytes
			for (int i = __l - 1; i >= 0; i--)
				try
				{
					__b[__o + i] = removeLast();
					rc++;
				}
				
				// Return the number of read bytes
				catch (NoSuchElementException nsee)
				{
					return rc;
				}
			
			// Return the read count
			return rc;	
		}
	}
}

