// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.IOException;
import java.io.InputStream;

/**
 * This is used to read directly from the block accessor.
 *
 * @since 2017/01/03
 */
class __BlockAccessorRegionInputStream__
	extends InputStream
{
	/** The accessor to read from. */
	protected final BlockAccessor accessor;
	
	/** The next position to read from. */
	private volatile long _next;
	
	/** The current number of bytes remaining. */
	private volatile long _rest;
	
	/**
	 * Initializes the block region input.
	 *
	 * @param __ba The block accessor to read data from.
	 * @param __start The start address of the read.
	 * @param __len The number of bytes to read.
	 * @throws IllegalArgumentException If the start and/or length are
	 * negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/03
	 */
	__BlockAccessorRegionInputStream__(BlockAccessor __ba, long __start,
		long __len)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__ba == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error BF0r The start position and length cannot be
		negative.} */
		if (__start < 0 || __len < 0)
			throw new IllegalArgumentException("BF0r");
		
		// Set
		this.accessor = __ba;
		this._next = __start;
		this._rest = __len;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/03
	 */
	@Override
	public int available()
		throws IOException
	{
		// Never exceed 2GiB
		return (int)Math.min(Math.max(0, this._rest), Integer.MAX_VALUE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/03
	 */
	@Override
	public int read()
		throws IOException
	{
		// Forward to multi-byte version
		byte[] b = new byte[1];
		for (;;)
		{
			int rv = this.read(b, 0, 1);
			
			// EOF?
			if (rv < 0)
				return -1;
			
			// Return value otherwise
			return b[0] & 0xFF;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/03
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// EOF?
		long rest = this._rest;
		if (rest <= 0)
			return -1;
		
		// Determine number of bytes to read
		long next = this._next;
		int desired = (int)Math.min(__l, rest);
		
		// Read in the data
		int actual = this.accessor.read(next, __b, __o, desired);
		
		// Set next position
		this._next = next + actual;
		this._rest = rest - actual;
		
		// Return the actual read count
		return actual;
	}
}

