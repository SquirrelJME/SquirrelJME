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
 * This provide a base circular buffer class which is extended by subclasses
 * which handle the given types as required.
 *
 * @param <T> The type of buffer to use.
 * @param <E> The erased type.
 * @since 2016/03/11
 */
public abstract class CircularGenericBuffer<T, E>
{
	/** Initial buffer size (power of two). */
	protected static final int INITIAL_SIZE =
		8;	
	
	/** Lock. */
	protected final Object lock;
	
	/** The internal buffer. */
	private volatile T _buffer;
	
	/** Head of the buffer. */
	private volatile int _head =
		-1;
	
	/** Tail of the buffer. */
	private volatile int _tail =
		-1;
	
	/**
	 * Sanity check.
	 *
	 * @since 2016/03/12
	 */
	static
	{
		// Check power of two
		if (Integer.bitCount(INITIAL_SIZE) != 1)
			throw new RuntimeException();
	}
	
	/**
	 * Initializes the base generic buffer.
	 *
	 * @since 2016/03/11
	 */
	public CircularGenericBuffer()
	{
		this(null);
	}
	
	/**
	 * Initializes the base generic buffer with the given lock.
	 *
	 * @param __lock The locking object to use, if {@code null} then one is
	 * created.
	 * @since 2016/03/11
	 */
	public CircularGenericBuffer(Object __lock)
	{
		lock = (__lock != null ? __lock : new Object());
	}
	
	/**
	 * Returns the length of the given array.
	 *
	 * @param __arr The array to get the length of.
	 * @return The array length.
	 * @since 2016/03/11
	 */
	protected abstract int arrayLength(T __arr);
	
	/**
	 * Allocates a new array of the given length.
	 *
	 * @param __len The length of the new array.
	 * @return The new array.
	 * @since 2016/03/11
	 */
	protected abstract T arrayNew(int __len);
	
	/**
	 * Read the array at the given position.
	 *
	 * @param __arr The array to read from.
	 * @param __dx The index to read.
	 * @return The value at this position.
	 * @since 2016/03/11
	 */
	protected abstract E arrayRead(T __arr, int __dx);
	
	/**
	 * Writes the array at the given position.
	 *
	 * @param __arr The array to write to.
	 * @param __dx The index to write at.
	 * @param __v The value to write here
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	protected abstract CircularGenericBuffer arrayWrite(T __arr, int __dx,
		E __v);
	
	/**
	 * Returns the number of values available in the queue.
	 *
	 * @return The available values.
	 * @since 2016/03/11
	 */
	public int available()
	{
		// Lock
		synchronized (lock)
		{
			// If no buffer, will always be empty
			T buf = _buffer;
			if (_buffer == null)
				return 0;
			
			// Get head and tail positions
			long head = _head;
			long tail = _tail;
			
			// If the tail is less than the head, add buffer size
			if (tail < head)
				tail += arrayLength(buf);
			
			// Return the value location difference
			return (int)(tail - head);
		}
	}
	
	/**
	 * Returns {@code true} if values are available.
	 *
	 * @return {@code true} if values are available.
	 * @since 2016/03/11
	 */
	public boolean hasAvailable()
	{
		return available() > 0;
	}
	
	/**
	 * Returns the head position.
	 *
	 * @return The head position.
	 * @since 2016/03/12
	 */
	public int headPosition()
	{
		// Lock
		synchronized (lock)
		{
			return _head;
		}
	}
	
	/**
	 * Offers a single value and adds it to the start of the queue.
	 *
	 * @param __b The value to offer.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularGenericBuffer offerFirst(E __b)
	{
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Offers multiple values within the given offset and length and adds them
	 * to the start of the queue.
	 *
	 * @param __b The array to source values from.
	 * @param __o The offset to within the buffer.
	 * @param __l The number of values to offer.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public CircularGenericBuffer offerFirst(T __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		int len = arrayLength(__b);
		if (__o < 0 || __l < 0 || (__o + __l) > len)
			throw new IndexOutOfBoundsException();
		
		// Lock
		synchronized (lock)
		{
			for (int i = __l - 1; i >= 0; i--)
				offerFirst(arrayRead(__b, __o + i));
		}
		
		// Self
		return this;
	}
	
	/**
	 * Offers a single value and adds it to the end of the queue.
	 *
	 * @param __b The value to offer.
	 * @return {@code this}.
	 * @since 2016/03/11
	 */
	public CircularGenericBuffer offerLast(E __b)
	{
		synchronized (lock)
		{
			// Get target buffer
			T buf = __grow(0, 1);
			
			// End position
			int end = _tail;
			
			// Write at the end
			arrayWrite(buf, end, __b);
			
			// Increment
			_tail = (end + 1) & (arrayLength(buf) - 1);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Offers multiple values within the given offset and length and adds them
	 * to the end of the queue.
	 *
	 * @param __b The array to source values from.
	 * @param __o The offset to within the buffer.
	 * @param __l The number of values to offer.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public CircularGenericBuffer offerLast(T __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		int len = arrayLength(__b);
		if (__o < 0 || __l < 0 || (__o + __l) > len)
			throw new IndexOutOfBoundsException();
		
		// Lock
		synchronized (lock)
		{
			for (int i = 0; i < __l; i++)
				offerLast(arrayRead(__b, __o + i));
		}
		
		// Self
		return this;
	}
	
	/**
	 * Reads and removes the first available value, if one is not available
	 * then an exception is thrown.
	 *
	 * @return The next value.
	 * @throws NoSuchElementException If no values are available.
	 * @since 2016/03/11
	 */
	public E removeFirst()
		throws NoSuchElementException
	{
		synchronized (lock)
		{
			// If not buffer, then there is nothing to remove
			T buf = _buffer;
			if (buf == null)
				throw new NoSuchElementException();
			
			// Get head and tail position
			int len = arrayLength(buf);
			int head = _head;
			int tail = _tail;
			
			// If the head is at the tail, cannot get
			if (head == tail)
				throw new NoSuchElementException();
			
			// Get value here
			E rv = arrayRead(buf, head);
			
			// Increment head position
			_head = (head + 1) & (len - 1);
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Reads and removes multiple values at the start of the queue up to the
	 * length and places them into the given array.
	 *
	 * @param __b The array to write value values into.
	 * @param __o The offset into the array to start writing at.
	 * @param __l The maximum number of values to remove.
	 * @return The number of removed values.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeFirst(T __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		int len = arrayLength(__b);
		if (__o < 0 || __l < 0 || (__o + __l) > len)
			throw new IndexOutOfBoundsException();
		
		// Lock
		synchronized (lock)
		{
			// Total
			int rc = 0;
			
			// Remove values
			for (int i = 0; i < __l; i++)
				try
				{
					arrayWrite(__b, __o + i, removeFirst());
					rc++;
				}
				
				// Return the number of read values
				catch (NoSuchElementException nsee)
				{
					return rc;
				}
			
			// Return the read count
			return rc;	
		}
	}
	
	/**
	 * Reads and removes the last available value, if one is not available
	 * then an exception is thrown.
	 *
	 * @return The next value.
	 * @throws NoSuchElementException If no values are available.
	 * @since 2016/03/11
	 */
	public E removeLast()
		throws NoSuchElementException
	{
		synchronized (lock)
		{
			// If not buffer, then there is nothing to remove
			T buf = _buffer;
			if (buf == null)
				throw new NoSuchElementException();
			
			// Get head and tail position
			int len = arrayLength(buf);
			int head = _head;
			int tail = _tail;
			
			// If the tail is at the head, cannot get
			if (head == tail)
				throw new NoSuchElementException();
			
			// Get value here
			int from = (tail - 1) & (len - 1);
			E rv = arrayRead(buf, from);
			
			// Decrement tail position
			_tail = from;
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Reads and removes multiple values at the end of the queue up to the
	 * length and places them into the given array.
	 *
	 * @param __b The array to write value values into.
	 * @param __o The offset into the array to start writing at.
	 * @param __l The maximum number of values to remove.
	 * @return The number of removed values.
	 * @throws IndexOutOfBoundsException If the offset or length are negative,
	 * or the offset and the length exceeds the array size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/11
	 */
	public int removeLast(T __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		int len = arrayLength(__b);
		if (__o < 0 || __l < 0 || (__o + __l) > len)
			throw new IndexOutOfBoundsException();
		
		// Lock
		synchronized (lock)
		{
			// Total
			int rc = 0;
			
			// Remove values
			for (int i = __l - 1; i >= 0; i--)
				try
				{
					arrayWrite(__b, __o + i, removeLast());
					rc++;
				}
				
				// Return the number of read values
				catch (NoSuchElementException nsee)
				{
					return rc;
				}
			
			// Return the read count
			return rc;	
		}
	}
	
	/**
	 * Returns the tail position.
	 *
	 * @return The tail position.
	 * @since 2016/03/12
	 */
	public int tailPosition()
	{
		// Lock
		synchronized (lock)
		{
			return _tail;
		}
	}
	
	/**
	 * Potentially grows the buffer.
	 *
	 * @param __ha Head addition.
	 * @param __ta tail addition.
	 * @return The new buffer.
	 * @since 2016/03/11
	 */
	private final T __grow(int __ha, int __ta)
	{
		// Lock
		synchronized (lock)
		{
			// Get old buffer
			T obuf = _buffer;
			
			// No array? Then allocate 
			if (obuf == null)
			{
				_head = 0;
				_tail = 0;
				return (_buffer = arrayNew(INITIAL_SIZE));
			}
			
			// Get the old start and end positions
			int olen = arrayLength(obuf);
			int omask = olen - 1;
			int ooh = _head;
			int oot = _tail;
			
			// If there is no chance of collision, then do not grow it
			int pph = (ooh + __ha) & omask;
			int ppt = (oot + __ta) & omask;
			if (pph != ppt)
				return obuf;
			
			// The queue has just collected a large number
			// of bytes which were never collected.
			if (olen >= 0x4000_0000)
				throw new IllegalStateException();
			
			// Calculate the new length
			int nlen = olen << 1;
			int nmask = nlen - 1;
			
			// Setup new buffer
			T nbuf = arrayNew(nlen);
			
			// Start at zero positions
			int nnh = 0;
			int nnt = 0;
			
			// Copy from the old buffer to the new one
			while (ooh != oot)
			{
				// Copy data
				E v = arrayRead(obuf, ooh);
				arrayWrite(nbuf, nnt, v);
				
				// Increase the old read head
				ooh = (ooh + 1) & omask;
				
				// Increase the new write tail
				nnt = (nnt + 1) & nmask;
			}
			
			// Use new details instead
			_buffer = nbuf;
			_head = nnh;
			_tail = nnt;
			
			// Return the new buffer
			return nbuf;
		}
	}
}

