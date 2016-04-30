// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.bits;

/**
 * This is a bit compactor which is given bits, when there are enough bits to
 * make a byte then a callback is called which is given the read byte value.
 *
 * @since 2016/03/10
 */
public class BitCompactor
{
	/** Lock. */
	protected final Object lock =
		new Object();	
	
	/** The callback for when a bit is ready. */
	protected final Callback callback;
	
	/** The queue bit. */
	private volatile byte _queue;
	
	/** The current bit to write. */
	private volatile int _at;
	
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
	 * @return {@code this}.
	 * @since 2016/03/10
	 */
	public BitCompactor add(boolean __b)
	{
		// Lock
		synchronized (lock)
		{
			// Get current position
			int now = _at;
			
			// Set it in the queue
			byte val = _queue;
			if (__b)
				val |= (byte)(1 << now);
			
			// Increase it
			now++;
			
			// Pushing new byte?
			if (now == 8)
			{
				// Clear and wait for next
				_queue = 0;
				_at = 0;
				
				// Send it to the callback
				callback.ready(val);
			}
			
			// Rest at the next bit
			else
			{
				_queue = val;
				_at = now;
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * Adds multiple bits to be compacted.
	 *
	 * @param __b The bits to add.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/10
	 */
	public BitCompactor add(boolean... __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// Go through the array
			int n = __b.length;
			for (int i = 0; i < n; i++)
				add(__b[i]);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Adds a group of bits, the bits are added from the least significant
	 * bit first.
	 *
	 * @param __val The value to add.
	 * @param __mask The mask for valid bits, only the least significant bits
	 * should be set in sequence.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If any bit is set that is not within
	 * the mask, or the mask has a zero gap in it.
	 * @since 2016/03/10
	 */
	public BitCompactor add(int __val, int __mask)
		throws IllegalArgumentException
	{
		return add(__val, __mask, false);
	}
	
	/**
	 * Adds a group of bits.
	 *
	 * @param __val The value to add.
	 * @param __mask The mask for valid bits, only the least significant bits
	 * should be set in sequence.
	 * @param __msb If {@code true} then bits are added from the most
	 * significant value first.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If any bit is set that is not within
	 * the mask, or the mask has a zero gap in it.
	 * @since 2016/03/10
	 */
	public BitCompactor add(int __val, int __mask, boolean __msb)
		throws IllegalArgumentException
	{
		// Number of bits in the mask
		int ibm = Integer.bitCount(__mask);
		
		// Check to make sure the input is valid
		if ((__val & (~__mask)) != 0)
			throw new IllegalArgumentException(String.format("XI01 %x %x",
				__val, __mask));
		if (ibm != (32 - Integer.numberOfLeadingZeros(__mask)) ||
			(__mask & 1) == 0)
			throw new IllegalArgumentException(String.format("XI02 %x %x",
				__val, __mask));
		
		// Lock
		synchronized (lock)
		{
			// Read input bits
			int an = (__msb ? -1 : 1);
			for (int at = (__msb ? ibm - 1 : 0); at >= 0 && at < ibm; at += an)
				add(0 != (__val & (1 << at)));
		}
		
		return this;
	}
	
	/**
	 * Returns the next bit to write.
	 *
	 * @return The next bit to be written.
	 * @since 2016/03/12
	 */
	public int nextBit()
	{
		// Lock
		synchronized (lock)
		{
			return _at;
		}
	}
	
	/**
	 * If there is at least 1 bit written of a byte (and not zero), then the
	 * remaining byte will be filled with zeros then passed to the output.
	 *
	 * @return {@code this}.
	 * @since 2016/03/12
	 */
	public BitCompactor zeroRemainder()
	{
		// Lock
		synchronized (lock)
		{
			// If at the start, do not bother
			if (_at == 0)
				return this;
			
			// Otherwise add until zero
			while (_at != 0)
				add(false);
		}
		
		// Self
		return this;
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

