// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.memory.MemoryAccessException;
import cc.squirreljme.jvm.memory.ReadableByteMemory;
import cc.squirreljme.jvm.memory.WritableByteMemory;

/**
 * This is a standard chunk of memory which just has a basic set of bytes
 * associated with it. This memory chunk is backed by an array and as such
 * has a size limitation of 2GiB.
 *
 * @since 2020/03/03
 */
public final class MemoryChunk
	implements ReadableByteMemory, WritableByteMemory
{
	/** The size of the memory chunk. */
	protected final int size;
	
	/** The chunk bytes. */
	protected final byte[] _bytes;
	
	/**
	 * Initializes the memory chunk.
	 *
	 * @param __size The size of the chunk.
	 * @throws IllegalArgumentException If the size is zero or negative.
	 * @since 2020/03/03
	 */
	public MemoryChunk(int __size)
		throws IllegalArgumentException
	{
		if (__size <= 0)
			throw new IllegalArgumentException("Negative or zero chunk size.");
		
		this.size = __size;
		this._bytes = new byte[__size];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/07
	 */
	@Override
	public byte read(long __addr)
		throws MemoryAccessException
	{
		int iaddr = (int)__addr;
		if (__addr < 0 || __addr > Integer.MAX_VALUE || iaddr >= this.size)
			throw new MemoryAccessException(__addr);
		
		return this._bytes[iaddr];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/07
	 */
	@Override
	public void read(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
		NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		int iaddr = (int)__addr;
		if (__addr < 0 || (__addr + __l) > Integer.MAX_VALUE ||
			iaddr > this.size)
			throw new MemoryAccessException(__addr);
		
		// Transfer bytes
		byte[] bytes = this._bytes;
		for (int end = __o + __l; __o < end;)
			__b[__o++] = bytes[iaddr++];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/07
	 */
	@Override
	public void write(long __addr, byte __b)
		throws MemoryAccessException
	{
		int iaddr = (int)__addr;
		if (__addr < 0 || __addr > Integer.MAX_VALUE || iaddr >= this.size)
			throw new MemoryAccessException(__addr);
		
		this._bytes[iaddr] = __b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/07
	 */
	@Override
	public void write(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
		NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		int iaddr = (int)__addr;
		if (__addr < 0 || (__addr + __l) > Integer.MAX_VALUE ||
			iaddr > this.size)
			throw new MemoryAccessException(__addr);
		
		// Transfer bytes
		byte[] bytes = this._bytes;
		for (int end = __o + __l; __o < end;)
			bytes[iaddr++] = __b[__o++];
	}
}
