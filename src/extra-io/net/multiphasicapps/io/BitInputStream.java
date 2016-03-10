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
	
	/** The current inner bit position. */
	private volatile int _inner =
		8;
	
	/** The current read byte. */
	private volatile byte _byte;
	
	/**
	 * Initializes the bit view of the given input stream.
	 *
	 * @param __w The input stream to provide a byte interface over.
	 * @param __lsb Least significant bit first? If {@code true} then the bit
	 * that is at the lowest shift of a byte will appear first, otherwise if
	 * {@code false} then the highest shift will appear first.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/09
	 */
	public BitInputStream(InputStream __w, boolean __lsb)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException();
		
		// Set
		wrapped = __w;
		lsb = __lsb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
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
			int curbit = _inner;
			
			// Read in the next byte?
			if (curbit == 8)
			{
				// Read next byte
				int val = wrapped.read();
				
				// EOF?
				if (val < 0)
					throw new EOFException();
				
				// Set active byte
				_byte = (byte)val;
				
				// Reset current bit
				_inner = curbit = 0;
			}
			
			// Get currently active byte
			byte act = _byte;
			
			// Least significant bit first
			boolean rv;
			if (lsb)
				rv = (0 != (act & (1 << curbit)));
			
			// Most first
			else
				rv = (0 != (act & (1 << (7 - curbit))));
			
			// Set next desired bit
			_inner = curbit + 1;
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Reads the specified number of bits and returns the value.
	 *
	 * @param __bc The number of bits to read.
	 * @return The read bit value.
	 * @throws EOFException If no more bits are left.
	 * @throws IllegalArgumentException If the bit count is not within the
	 * range of a long or is zero.
	 * @throws IOException On read errors.
	 * @since 2016/03/09
	 */
	public long readBits(int __bc)
		throws EOFException, IllegalArgumentException, IOException
	{
		// Check
		if (__bc <= 0 || __bc > 64)
			throw new IllegalArgumentException();
		
		// Lock
		synchronized (lock)
		{
			// Output value
			long rv = 0L;
			
			// Read input bits
			int hi = __bc - 1;
			for (int i = 0; i < __bc; i++)
				if (read())
					rv |= (1L << (long)(hi - i));
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Reads a byte.
	 *
	 * @return The read byte value.
	 * @throws EOFException If no more bits are left.
	 * @throws IOException On read errors.
	 * @since 2016/03/09
	 */
	public byte readByte()
		throws EOFException, IOException
	{
		return (byte)readBits(8);
	}
}

