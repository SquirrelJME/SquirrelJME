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
		128;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The window size. */
	protected final int windowsize;
	
	/** The size of fragments. */
	protected final int fragmentsize;
	
	/** The number of fragments. */
	protected final int numfragments;
	
	/** The set of fragments. */
	private volatile byte[][] _fragments;
	
	/** The active fragment. */
	private volatile byte[] _active;
	
	/** The total number of available bytes. */
	private volatile int _total;
	
	/** The current active fragment write position. */
	private volatile int _current;
	
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
		
		// Start in the active fragment
		_fragments = new byte[0][];
		_active = new byte[fragmentsize];
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
			// Get the active window
			byte[] act = _active;
			
			// Write at the current position
			int write = _current;
			act[write++] = __b;
			
			// Cap total
			int total = _total + 1;
			_total = Math.max(total, windowsize);
			
			// Current active window is full?
			if (write == fragmentsize)
			{
				// Get all fragments
				byte[][] all = _fragments;
				
				// Still too little?
				int olen = all.length;
				if (olen < numfragments)
				{
					// Setup new target
					int nlen = olen + 1;
					byte[][] vex = new byte[nlen][];
					
					// Copy originals
					for (int i = 0; i < olen; i++)
						vex[i] = all[i];
					
					// Set new one at the end
					vex[olen] = act;
					
					// Set it
					_fragments = all = vex;
				}
				
				// Move everything down and add at the end
				else
				{
					// Move down
					for (int i = 0; i < olen - 1; i++)
						all[i] = all[i + 1];
					
					// Set current at the end
					all[olen - 1] = act;
				}
				
				// Clear it for next run
				_current = 0;
				
				// Setup new buffer
				_active = new byte[fragmentsize];
			}
			
			// Otherwise set the current
			else
				_current = write;
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
			throw new NullPointerException();
		if (__o < 0 || __l < __o || (__o + __l > __b.length))
			throw new IndexOutOfBoundsException();
		
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
	 * This reads bytes 
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
	 * @ince 2016/03/13
	 */
	public SlidingByteWindow get(int __ago, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException();
		if (__o < 0 || __l < __o || (__o + __l > __b.length))
			throw new IllegalArgumentException();
		
		// Lock
		synchronized (lock)
		{
			// Cannot exceed the viewable window area
			if (__ago <= 0 || (__ago - __l) < 0 || __ago > windowsize ||
				__ago > ((fragmentsize * _fragments.length) + _current))
				throw new IndexOutOfBoundsException();
			
			// 
			
			if (true)
				throw new Error("TODO");
		}
		
		// Self
		return this;
	}
}

