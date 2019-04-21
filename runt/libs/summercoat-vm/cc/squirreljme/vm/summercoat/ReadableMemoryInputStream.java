// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import java.io.InputStream;

/**
 * This is an input stream which can read from readable memory.
 *
 * @since 2019/04/21
 */
public final class ReadableMemoryInputStream
	extends InputStream
{
	/** The input memory. */
	protected final ReadableMemory memory;
	
	/** The base read address. */
	protected final int address;
	
	/** The number of bytes that can be read. */
	protected final int length;
	
	/** The current read offset. */
	private int _at;
	
	/**
	 * Initializes the stream.
	 *
	 * @param __mem The memory.
	 * @param __ad The start address.
	 * @param __ln The length.
	 * @throws NullPointerException On null arguments.
	 */
	public ReadableMemoryInputStream(ReadableMemory __mem, int __ad, int __ln)
		throws NullPointerException
	{
		if (__mem == null)
			throw new NullPointerException("NARG");
		
		this.memory = __mem;
		this.address = __ad;
		this.length = __ln;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int available()
	{
		return this.length - this._at;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final void close()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int read()
	{
		// EOF?
		int at = this._at;
		if (at >= this.length)
			return -1;
		
		// Read
		this._at = at + 1;
		return this.memory.memReadByte(this.address + at) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int read(byte[] __b)
		throws NullPointerException
	{
		return this.read(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Used to check bounds
		int at = this._at,
			length = this.length,
			address = this.address;
		
		// Read in all bytes
		ReadableMemory memory = this.memory;
		for (int i = 0; i < __l; i++)
		{
			// EOF?
			if (at >= length)
			{
				this._at = at + i;
				return (i == 0 ? -1 : i);
			}
			
			// Read in
			__b[__o++] = (byte)memory.memReadByte(address + at + i);
		}
		
		// Count
		this._at = at + __l;
		return __l;
	}
}

