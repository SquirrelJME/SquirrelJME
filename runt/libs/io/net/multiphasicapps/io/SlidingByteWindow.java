// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This represents a sliding byte window.
 *
 * It is allocated in chunks of a given fragment size so that the entire window
 * is not allocated in its entirety until it is actually used.
 *
 * In the future this class may support a kind of compression so that older
 * bytes in the sliding window may be compacted when they are not used.
 *
 * This class is not thread safe.
 *
 * @since 2016/03/10
 */
public class SlidingByteWindow
{
	/** The backing byte buffer. */
	protected final ByteDeque deque;
	
	/** The window size. */
	protected final int windowsize;
	
	/** Single byte for forcing bulk operations. */
	private final byte[] _solo =
		new byte[1];
	
	/** The total number of written bytes. */
	private volatile int _total;
	
	/**
	 * This initializes the sliding byte window.
	 *
	 * @param __wsz The size of the sliding window.
	 * @since 2016/03/10
	 */
	public SlidingByteWindow(int __wsz)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BD1s Zero or negative window size specified.
		// (The window size)}
		if (__wsz <= 0)
			throw new IllegalArgumentException(String.format("BD1s %d",
				__wsz));
		
		// Set
		windowsize = __wsz;
		
		// Setup backing store
		deque = new ByteDeque(__wsz);
	}
	
	/**
	 * Appends a single byte to the sliding window.
	 *
	 * @param __b The byte to add to the window.
	 * @since 2016/03/10
	 */
	public void append(byte __b)
	{
		byte[] solo = this._solo;
		solo[0] = __b;
		append(solo, 0, 1);
	}
	
	/**
	 * Appends multiple bytes to the sliding window.
	 *
	 * @param __b Bytes to add to the sliding window.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public void append(byte[] __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Call other
		this.append(__b, 0, __b.length);
	}
	
	/**
	 * Appends the bytes from the input array in the given range to the sliding
	 * window.
	 *
	 * @param __b Bytes to add to the sliding window.
	 * @param __o Offset into the byte array.
	 * @param __l The number of bytes to add.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or the offset and the length exceeds the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public void append(byte __b[], int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l > __b.length))
			throw new IndexOutOfBoundsException("BAOB");
		
		// Get
		ByteDeque deque = this.deque;
		int windowsize = this.windowsize;
		int total = this._total;
		
		// Check if the new amount will overflow
		int newtotal = total + __l;
		int overflow = 0;
		if (newtotal < 0 || newtotal > windowsize)
		{
			overflow = (newtotal - windowsize);
			newtotal = windowsize;
		}
		
		// Will overflow, delete last bytes
		if (overflow > 0)
			deque.deleteFirst(overflow);
		
		// Add data to the end so that the single-byte insertion order
		// would be the same as the multi-byte insertion.
		// And so no shuffling is required
		deque.addLast(__b, __o, __l);
		this._total = newtotal;
	}
	
	/**
	 * Obtains a single byte at the specified position in the past.
	 *
	 * @param __ago The number of positions ago to get the value for.
	 * @return The byte value at the given position.
	 * @throws IndexOutOfBoundsException If the position is not within the
	 * bounds of the sliding window.
	 * @since 2017/03/04
	 */
	public byte get(int __ago)
		throws IndexOutOfBoundsException
	{
		byte[] solo = this._solo;
		get(__ago, solo, 0, 1);
		return solo[0];
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
	public void get(int __ago, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l > __b.length))
			throw new IndexOutOfBoundsException("BAOB");
		
		// {@squirreljme.error BD1t Bulk read of window bytes would exceed
		// the bounds of the window. (The bytes in the past to start the
		// copy from; The number of bytes to read; The total number of
		// bytes in the window)}
		int windowsize = this.windowsize;
		int total = this._total;
		if (__ago <= 0 || ((total - __ago) + __l) > total)
			throw new IndexOutOfBoundsException(String.format(
				"BD1t %d %d %d", __ago, __l, total));
		
		// {@squirreljme.error BD1u Get of a sliding window read did not
		// read the expected number of bytes. (The expected number of bytes
		// to read; The actual number read)}
		int rv;
		if ((rv = this.deque.get(total - __ago, __b, __o,
			__l)) != __l)
			throw new IndexOutOfBoundsException(String.format(
				"BD1u %d %d", __l, rv));
	}
	
	/**
	 * Returns the total number of bytes in the sliding window.
	 *
	 * @return The total number of bytes in the window.
	 * @since 2016/03/28
	 */
	public int size()
	{
		return this._total;
	}
}

