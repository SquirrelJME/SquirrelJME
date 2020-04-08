// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.memory;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Provides memory reading through standard memory access.
 *
 * @since 2020/04/02
 */
public class ReadableAssemblyMemory
	implements ReadableBasicMemory
{
	/** The base address. */
	protected final long base;
	
	/** The size of this block. */
	protected final int size;
	
	/**
	 * Initializes the memory information.
	 *
	 * @param __base The address.
	 * @param __size The memory size.
	 * @throws IllegalArgumentException If the size is zero or negative.
	 * @since 2020/04/02
	 */
	public ReadableAssemblyMemory(long __base, int __size)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ3X Negative memory size requested.}
		if (__size <= 0)
			throw new IllegalArgumentException("ZZ3X");
		
		this.base = __base;
		this.size = __size;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public byte read(long __addr)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr >= this.size)
			throw new MemoryAccessException(__addr, "MOOB");
		
		return Assembly.memReadByte(this.base, (int)__addr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public void read(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryAccessException,
		NullPointerException
	{
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		if (__addr < 0 || (__addr + __l) > this.size)
			throw new MemoryAccessException(__addr, "MOOB");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public int readInt(long __addr)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr + 4 > this.size)
			throw new MemoryAccessException(__addr, "MOOB");
		
		return Assembly.memReadInt(this.base, (int)__addr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public long readLong(long __addr)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr + 8 > this.size)
			throw new MemoryAccessException(__addr, "MOOB");
			
		return Assembly.memReadLong(this.base, (int)__addr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public short readShort(long __addr)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr + 2 > this.size)
			throw new MemoryAccessException(__addr, "MOOB");
			
		return Assembly.memReadShort(this.base, (int)__addr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public int size()
	{
		return this.size;
	}
}
