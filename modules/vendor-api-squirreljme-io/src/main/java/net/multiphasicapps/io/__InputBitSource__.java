// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Bit source for huffman reads.
 *
 * @since 2017/02/25
 */
final class __InputBitSource__
	implements MultiBitSource
{
	/** The stream to read bits from. */
	private final InputStream _in;
	
	/**
	 * The miniature read window, it stores a 32-bit value and is given
	 * input bytes to read along with being used as output. This is an int
	 * because it is faster to work with integer values rather than bytes.
	 * It also means that it is much simpler to work with.
	 */
	private int _miniwindow;
	
	/** Represents the number of bits in the mini window. */
	int _minisize;
	
	/** The read-in buffer which is used to bulk read input bytes. */
	private final byte[] _readin = new byte[4];
	
	/** The number of compressed bytes. */
	volatile long _compressedsize;
	
	/**
	 * Initializes the bit source.
	 *
	 * @param __in The stream to read bits from.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/03
	 */
	__InputBitSource__(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this._in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2017/02/25
	 */
	@Override
	public boolean nextBit()
		throws IOException
	{
		return 0 != this.readBits(1, true);
	}
	
	/**
	 * Reads bits from the input stream.
	 *
	 * @param __n The number of bits to read.
	 * @param __msb If {@code true} the most significant bits are first.
	 * @return The read data.
	 * @throws IOException On read errors.
	 * @since 2017/02/25
	 */
	public int readBits(int __n, boolean __msb)
		throws IOException
	{
		// Nothing to read
		if (__n == 0)
			return 0;
		
		// Get the mini window information
		int miniwindow = this._miniwindow, minisize = this._minisize;
		
		// Not enough bits to read the value
		while (minisize < __n)
		{
			// The number of bytes to be read
			int bc = (__n - minisize) / 8;
			if (bc == 0)
				bc = 1;
			
			// Read input bytes
			byte[] readin = this._readin;
			int rc = this._in.read(readin, 0, bc);
			
			/* {@squirreljme.error BD1g Reached EOF while reading bytes to
			decompress. (Bits in the queue; Requested number of bits)} */
			if (rc < 0)
				throw new IOException(
					String.format("BD1g %d %d", minisize, __n));
			
			// Shift in the read bytes to the higher positions
			for (int i = 0; i < rc; i++)
			{
				miniwindow |= ((readin[i] & 0xFF) << minisize);
				minisize += 8;
			}
			
			// Count the number of compressed bytes
			this._compressedsize += rc;
		}
		
		// Mask in the value, which is always at the lower bits
		int rv = miniwindow & ((1 << __n) - 1);
		
		// Shift down the mini window for the next read
		// Make sure the shift down is unsigned so that zeroes are in the
		// higher bits for the filling OR operation.
		miniwindow >>>= __n;
		minisize -= __n;
		
		// Store for next run
		this._miniwindow = miniwindow;
		this._minisize = minisize;
		
		// Want MSB to be first, need to swap all the bits so the lowest ones
		// are at the highest positions
		// Luckily such a method already exists and it could potentially be
		// inlined by the JVM or converted to native code if such an
		// instruction exists.
		if (__msb)
			return Integer.reverse(rv) >>> (32 - __n);
		
		// Return read result
		return rv;
	}
}
