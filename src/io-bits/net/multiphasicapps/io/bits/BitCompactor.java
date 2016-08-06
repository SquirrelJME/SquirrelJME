// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.bits;

/**
 * This is a bit compactor which is given bits, when there are enough bits to
 * make a byte then a callback is called which is given the read byte value.
 *
 * This class is not thread safe.
 *
 * @since 2016/03/10
 */
public class BitCompactor
{
	/** The callback for when a bit is ready. */
	protected final Callback callback;
	
	/** The queue bit. */
	private volatile byte _queue;
	
	/** The current mask to use. */
	private volatile byte _mask =
		1;
	
	/**
	 * Initializes the bit compactor with the given callback.
	 *
	 * @param __cb Callback which is called when a byte is ready.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public BitCompactor(Callback __cb)
		throws NullPointerException
	{
		// Check
		if (__cb == null)
			throw new NullPointerException("NARG");
		
		// Set
		callback = __cb;
	}
	
	/**
	 * Adds a single bit.
	 *
	 * @param __b The value of the bit to add.
	 * @since 2016/03/10
	 */
	public void add(boolean __b)
	{
		// Get the current mask
		byte mask = this._mask;
		
		// OR in the mask, if the bit is set.
		byte val = this._queue;
		if (__b)
			val |= mask;
		
		// Increase mask
		mask <<= 1;
		
		// Push new value?
		if (mask == 0)
		{
			// Reset mask to base value and clear the value
			this._mask = 1;
			this._queue = 0;
			
			// Send it to the callback
			callback.ready(val);
		}
		
		// Not pushing
		else
		{
			// Set otherwise
			this._mask = mask;
			this._queue = val;
		}
	}
	
	/**
	 * Adds multiple bits to be compacted.
	 *
	 * @param __a The first boolean to add.
	 * @param __b The bits to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public void add(boolean __a, boolean... __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Add the first
		add(__a);
		
		// Go through the array
		int n = __b.length;
		for (int i = 0; i < n; i++)
			add(__b[i]);
	}
	
	/**
	 * Adds a group of bits, the bits are added from the least significant
	 * bit first.
	 *
	 * @param __val The value to add.
	 * @param __mask The mask for valid bits, only the least significant bits
	 * should be set in sequence.
	 * @throws IllegalArgumentException If any bit is set that is not within
	 * the mask, or the mask has a zero gap in it.
	 * @since 2016/03/10
	 */
	public void add(int __val, int __mask)
		throws IllegalArgumentException
	{
		add(__val, __mask, false);
	}
	
	/**
	 * Adds a group of bits.
	 *
	 * @param __val The value to add.
	 * @param __mask The mask for valid bits, only the least significant bits
	 * should be set in sequence.
	 * @param __msb If {@code true} then bits are added from the most
	 * significant value first.
	 * @throws IllegalArgumentException If any bit is set that is not within
	 * the mask, or the mask has a zero gap in it.
	 * @since 2016/03/10
	 */
	public void add(int __val, int __mask, boolean __msb)
		throws IllegalArgumentException
	{
		// Number of bits in the mask
		int ibm = Integer.bitCount(__mask);
		
		// {@squirreljme.error AH01 The value is outside the range of the mask.
		// (The value; The mask)}
		if ((__val & (~__mask)) != 0)
			throw new IllegalArgumentException(String.format("AH01 %x %x",
				__val, __mask));
		
		// {@squirreljme.error AH02 The mask has a zero gap between bits or
		// in the least significant bits. (The value; The mask)}
		if (ibm != (32 - Integer.numberOfLeadingZeros(__mask)) ||
			(__mask & 1) == 0)
			throw new IllegalArgumentException(String.format("AH02 %x %x",
				__val, __mask));
		
		// Higher bits first
		if (__msb)
		{
			for (int sh = Integer.highestOneBit(__mask); sh != 0; sh >>>= 1)
				add(0 != (__val & sh));
		}
		
		// Lower bits first
		else
		{
			int stop = Integer.highestOneBit(__mask) << 1;
			for (int sh = 1; sh != stop; sh <<= 1)
				add(0 != (__val & sh));
		}
	}
	
	/**
	 * Returns the next bit to write.
	 *
	 * @return The next bit to be written.
	 * @since 2016/03/12
	 */
	public int nextBit()
	{
		return Integer.numberOfTrailingZeros(this._mask);
	}
	
	/**
	 * If there is at least 1 bit written of a byte (and not zero), then the
	 * remaining byte will be filled with zeros then passed to the output.
	 *
	 * @since 2016/03/12
	 */
	public void zeroRemainder()
	{
		// If writing the first value, do nothing
		byte mask = this._mask;
		if (mask == 1)
			return;
		
		// Just send what is in the queue and reset the values
		callback.ready(this._queue);
		this._mask = 1;
		this._queue = 0;
	}
	
	/**
	 * This interface is used in the compactor as the callback for when a byte
	 * is ready for output.
	 *
	 * @since 2016/03/10
	 */
	public static interface Callback
	{
		/**
		 * This method is called when a byte is ready.
		 *
		 * @param __v The byte which is ready.
		 * @since 2016/03/10
		 */
		public abstract void ready(byte __v);
	}
}

