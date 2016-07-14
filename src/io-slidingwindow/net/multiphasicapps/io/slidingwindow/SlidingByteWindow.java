// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.slidingwindow;

import net.multiphasicapps.util.dynbuffer.DynamicByteBuffer;

/**
 * This represents a sliding byte window.
 *
 * It is allocated in chunks of a given fragment size so that the entire window
 * is not allocated in its entirety until it is actually used.
 *
 * In the future this class may support a kind of compression so that older
 * bytes in the sliding window may be compacted when they are not used.
 *
 * @since 2016/03/10
 */
public class SlidingByteWindow
{
	/** The default fragment size. */
	public static final int DEFAULT_FRAGMENT_SIZE =
		4;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The backing byte buffer. */
	protected final DynamicByteBuffer backingbuffer;
	
	/** The window size. */
	protected final int windowsize;
	
	/** The total number of written bytes. */
	private volatile int _total;
	
	/**
	 * Sanity check.
	 *
	 * @since 2016/03/12
	 */
	static
	{
		// {@squirreljme.error AI01 The default fragment size is not a power of
		// two. (The default fragment size)}
		if (Integer.bitCount(DEFAULT_FRAGMENT_SIZE) != 1)
			throw new RuntimeException(String.format("AI01 %d",
				DEFAULT_FRAGMENT_SIZE));
	}
	
	/**
	 * This initializes the sliding byte window.
	 *
	 * @param __wsz The size of the sliding window.
	 * @since 2016/03/10
	 */
	public SlidingByteWindow(int __wsz)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AI02 Zero or negative window size specified.
		// (The window size)}
		if (__wsz <= 0)
			throw new IllegalArgumentException(String.format("AI02 %d",
				__wsz));
		
		// Set
		windowsize = __wsz;
		
		// Setup backing store
		backingbuffer = new DynamicByteBuffer(Math.min(4, Math.max(1, Math.min(
			Integer.highestOneBit(windowsize), DEFAULT_FRAGMENT_SIZE))));
	}
	
	/**
	 * Appends a single byte to the sliding window.
	 *
	 * @param __b The byte to add to the window.
	 * @return {@code this}.
	 * @since 2016/03/10
	 */
	public SlidingByteWindow append(byte __b)
	{
		// Lock
		synchronized (lock)
		{
			// Add to the sliding window
			DynamicByteBuffer back = backingbuffer;
			back.add(__b);
			
			// Increases by one byte
			int vt = _total + 1;
			
			// Exceeds the window size? Remove the first byte
			int max = windowsize;
			if (vt > max)
				back.remove(0);
			
			// Set total count
			else
				_total = vt;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Appends multiple bytes to the sliding window.
	 *
	 * @param __b Bytes to add to the sliding window.
	 * @return {@code this}
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public SlidingByteWindow append(byte... __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Lock to make semi-atomic
		synchronized (lock)
		{
			// Append all bytes
			int n = __b.length;
			for (int i = 0; i < n; i++)
				append(__b[i]);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Appends the bytes from the input array in the given range to the sliding
	 * window.
	 *
	 * @param __b Bytes to add to the sliding window.
	 * @param __o Offset into the byte array.
	 * @param __l The number of bytes to add.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or the offset and the length exceeds the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public SlidingByteWindow append(byte __b[], int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < __o || (__o + __l > __b.length))
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (lock)
		{
			// Append
			int n = __o + __l;
			for (int i = __o; i < n; i++)
				append(__b[i]);
		}
		
		// Self
		return this;
	}
	
	/**
	 * This reads bytes from the sliding window at a given historical index
	 * and writes them to the input array.
	 *
	 * @param __ago How many bytes in the past to read.
	 * @param __b The output array of bytes to write.
	 * @param __o The offset into the array where to start writing bytes to.
	 * @param __l The number of bytes to read from history to write into the
	 * array.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or the offset and the length exceeds the array bounds; {@code __ago}
	 * is zero or negative; the distance back and the length exceeds the
	 * amount of available history; the distance back exceeds the maximum
	 * window size; or the distance back exceeds the total amount of history.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/13
	 */
	public SlidingByteWindow get(int __ago, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < __o || (__o + __l > __b.length))
			throw new IndexOutOfBoundsException(String.format("BAOB %d %d %d",
				__b.length, __o, __l));
		
		// Lock
		synchronized (lock)
		{
			// Total buffer size
			int max = _total;
			
			// {@squirreljme.error AI03 Bulk read of window bytes would exceed
			// the bounds of the window. (The bytes in the past to start the
			// copy from; The number of bytes to read; The total number of
			// bytes in the window)}
			if (__ago <= 0 || (__ago - __l) > max)
				throw new IndexOutOfBoundsException(String.format(
					"AI03 %d %d %d", __ago, __l, max));
			
			// Get backing buffer
			DynamicByteBuffer back = backingbuffer;
			
			// Read from the buffer at a given position
			back.get(max - __ago, __b, __o, __l);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the total number of bytes in the sliding window.
	 *
	 * @return The total number of bytes in the window.
	 * @since 2016/03/28
	 */
	public int size()
	{
		// lock
		synchronized (lock)
		{
			return _total;
		}
	}
}

