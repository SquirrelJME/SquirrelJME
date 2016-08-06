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

import java.io.Closeable;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;

/**
 * This is an input stream which wraps a byte based input and provides a bit
 * based view of it.
 *
 * @since 2016/03/09
 */
public class BitInputStream
	implements Closeable
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Wrapped stream. */
	protected final InputStream wrapped;
	
	/** Least significant bit first? */
	protected final boolean lsb;
	
	/** The current inner bit mask. */
	private volatile int _mask =
		0;
	
	/** The current read byte. */
	private volatile byte _byte;
	
	/**
	 * Initializes the bit view of the given input stream.
	 *
	 * @param __w The input stream to provide a byte interface over.
	 * @param __msb Most significant bit first? If {@code true} then the bit
	 * that is at the highest shift of a byte will appear first, otherwise if
	 * {@code false} then the lowest shift will appear first.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/09
	 */
	public BitInputStream(InputStream __w, boolean __msb)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// Set
		wrapped = __w;
		lsb = !__msb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the wrapped stream
		wrapped.close();
	}
	
	/**
	 * Reads the next bit.
	 *
	 * @return The read bit value.
	 * @throws EOFException At the end of the file.
	 * @throws IOException On read errors.
	 * @since 2016/03/09
	 */
	public boolean read()
		throws EOFException, IOException
	{
		// Lock
		synchronized (lock)
		{
			// Current bit to get
			int mask = this._mask;
			boolean lsb = this.lsb;
			byte value;
			
			// Read in the next byte?
			if (mask == 0)
			{
				// Read next byte
				int val = wrapped.read();
				
				// {@squirreljme.error AH03 End of file reached.}
				if (val < 0)
					throw new EOFException("AH03");
				
				// Set active byte
				value = (byte)val;
				this._byte = value;
				
				// Use lowest or highest
				mask = (lsb ? 0x01 : 0x80);
			}
			
			// Otherwise use the pre-existing value
			else
				value = this._byte;
			
			// Shift the mask up or down?
			if (lsb)
				this._mask = (mask << 1);
			else
				this._mask = (mask >>> 1);
			
			// Return the value dependent on the mask
			return (0 != (value & mask));
		}
	}
	
	/**
	 * Reads the specified number of bits and returns the value. The read bits
	 * are not reversed by default.
	 *
	 * @param __c The number of bits to read.
	 * @return The read bit value.
	 * @throws EOFException If no more bits are left.
	 * @throws IllegalArgumentException If the bit count is not within the
	 * range of a long or is zero.
	 * @throws IOException On read errors.
	 * @since 2016/03/09
	 */
	public long readBits(int __c)
		throws EOFException, IllegalArgumentException, IOException
	{
		return readBits(__c, false);
	}
	
	/**
	 * Invokes {@code readBitsInt(__c, false)}.
	 *
	 * @param __c As forwarded.
	 * @return The integer value of the read bits.
	 * @throws IllegalArgumentException If the number of bits is not between
	 * 1 and 32.
	 * @throws IOException On read errors.
	 * @since 2016/03/11
	 */
	public int readBitsInt(int __c)
		throws IllegalArgumentException, IOException
	{
		return readBitsInt(__c, false);
	}
	
	/**
	 * Invokes {@code (int)readBits(__c, __msb)}.
	 *
	 * @param __c The number of bits to read.
	 * @param __msb If {@code true} then bits which are read first are placed
	 * at higher addresses, otherwise they are placed at lower addresses.
	 * @return The integer value of the read bits.
	 * @throws IllegalArgumentException If the number of bits is not between
	 * 1 and 32.
	 * @throws IOException On read errors.
	 * @since 2016/03/11
	 */
	public int readBitsInt(int __c, boolean __msb)
		throws IllegalArgumentException, IOException
	{
		// {@squirreljme.error AH04 The number of bits to read is not valid
		// for the integer type. (The number of bits to read)}
		if (__c <= 0 || __c > 32)
			throw new IllegalArgumentException(String.format("AH04 %d", __c));
		
		// Lock
		synchronized (this.lock)
		{
			// Output value
			int rv = 0;
			
			// Most significant bits first
			if (__msb)
			{
				// Read input bits
				for (int i = 0, sh = (1 << (__c - 1)); i < __c; i++, sh >>>= 1)
					if (read())
						rv |= sh;
			}
			
			// Least significant bits first
			else
			{
				// Read input bits
				for (int i = 0, sh = 1; i < __c; i++, sh <<= 1)
					if (read())
						rv |= sh;
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Reads the specified number of bits and returns the value.
	 *
	 * Given an example byte value as done by {@link #read()}: {@code 0 1}.
	 * If {@code __msb} is {@code false} then the read value is {@code 0b10}.
	 * If {@code __msb} is {@code true} then the read value is {@code 0b01}.
	 *
	 * @param __c The number of bits to read.
	 * @param __msb Reverse all read bits, if {@code true} then the higher bits
	 * are written to first otherwise lower bits are written to first.
	 * @return The read bit value.
	 * @throws EOFException If no more bits are left.
	 * @throws IllegalArgumentException If the bit count is not within the
	 * range of a long or is zero.
	 * @throws IOException On read errors.
	 * @since 2016/03/09
	 */
	public long readBits(int __c, boolean __msb)
		throws EOFException, IllegalArgumentException, IOException
	{
		// {@squirreljme.error AH05 The number of bits to read is not valid
		// for the long type. (The number of bits to read)}
		if (__c <= 0 || __c > 64)
			throw new IllegalArgumentException(String.format("AH05 %d", __c));
		
		// Fit as an integer, use that
		if (__c <= 32)
			return ((long)readBitsInt(__c, __msb)) & 0xFFFFFFFFL;
		
		// Lock
		synchronized (this.lock)
		{
			// Read both sets of bits
			int rest = __c - 32;
			long fir = (((long)readBitsInt(32, __msb)) & 0xFFFFFFFFL);
			long sec = (((long)readBitsInt(rest, __msb)) & 0xFFFFFFFFL);
			
			// High bits first
			if (__msb)
				return (fir << rest) | sec;
			
			// Lower bits first
			else
				return (sec << 32) | fir;
		}
	}
	
	/**
	 * Reads a single byte.
	 *
	 * @return The read byte value.
	 * @throws EOFException If no more bits are left.
	 * @throws IOException On read errors.
	 * @since 2016/03/09
	 */
	public byte readByte()
		throws EOFException, IOException
	{
		return (byte)readBitsInt(8);
	}
}

