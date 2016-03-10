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
 * This represents a sliding byte window.
 *
 * @since 2016/03/10
 */
public class SlidingByteWindow
{
	/** The standard estimated fragment size. */
	public static final int DEFAULT_FRAGMENT_SIZE =
		512;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The window size. */
	protected final int windowsize;
	
	/** The size of fragments. */
	protected final int fragmentsize;
	
	/** The number of fragments. */
	protected final int numfragments;
	
	/**
	 * This initializes the sliding byte window.
	 *
	 * @param __wsz The size of the sliding window.
	 * @throws IllegalArgumentException If the window size is zero or negative
	 * or is not a power of 2.
	 * @since 2016/03/10
	 */
	public SlidingByteWindow(int __wsz)
		throws IllegalArgumentException
	{
		// Check
		if (__wsz <= 0 || Integer.bitCount(__wsz) != 1)
			throw new IllegalArgumentException();
		
		// Set
		windowsize = __wsz;
		
		// Determine the best fragment size
		fragmentsize = Math.min(windowsize, DEFAULT_FRAGMENT_SIZE);
		numfragments = windowsize / fragmentsize;
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
		throw new Error("TODO");
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
			throw new NullPointerException();
		
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
	 * @throws IllegalArgumentException If the offset or length are negative or
	 * the offset and the length exceeds the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public SlidingByteWindow append(byte __b[], int __o, int __l)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		if (__o < 0 || __l < __o || (__o + __l > __b.length))
			throw new IllegalArgumentException();
		
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
}

