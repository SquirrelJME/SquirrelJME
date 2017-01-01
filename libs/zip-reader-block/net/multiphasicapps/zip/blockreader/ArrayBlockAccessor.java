// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.EOFException;
import java.io.IOException;

/**
 * This wraps an array and provides it to the block accessor interface so that
 * ZIP files may be read from arrays.
 *
 * @since 2016/12/27
 */
public class ArrayBlockAccessor
	implements BlockAccessor
{
	/** The internal buffer. */
	protected final byte[] buffer;
	
	/** The offset into the buffer. */
	protected final int offset;
	
	/** The number of bytes available. */
	protected final int length;
	
	/**
	 * Initializes the block accessor which uses the entire array.
	 *
	 * @param __b The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	public ArrayBlockAccessor(byte[] __b)
		throws NullPointerException
	{
		this(__b, 0, __b.length);
	}
	
	/**
	 * Initializes the block accessor which uses the entire array.
	 *
	 * @param __b The array to wrap.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to make available.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	public ArrayBlockAccessor(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// Set
		this.buffer = __b;
		this.offset = __o;
		this.length = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public void close()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/29
	 */
	@Override
	public byte read(long __addr)
		throws EOFException, IOException
	{
		// {@squirreljme.error CJ03 Cannot read from a negative offset.}
		if (__addr < 0)
			throw new IOException("CJ03");
		
		// {@squirreljme.error CJ04 Read past end of the block.}
		if (__addr > this.length)
			throw new EOFException("CJ04");
		
		// Get
		return this.buffer[this.offset + (int)__addr];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public int read(long __addr, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// {@squirreljme.error CJ01 Cannot read from a negative offset.}
		if (__addr < 0)
			throw new IOException("CJ01");
		
		// After the end?
		int length = this.length;
		if (__addr >= length)
			return -1;
		
		// Number of bytes to actually read
		int addr = (int)__addr;
		int actual = Math.min(__l, length - addr);
		
		// Read bytes
		byte[] buffer = this.buffer;
		int offset = this.offset;
		for (int i = 0, s = offset + addr, d = __o; i < actual; i++, s++, d++)
			__b[d] = buffer[s];
		
		// Return the actual number of bytes read
		return actual;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public long size()
	{
		return this.length;
	}
}

