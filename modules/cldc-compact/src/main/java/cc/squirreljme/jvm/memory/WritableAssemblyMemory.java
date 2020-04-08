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
 * Provides memory writing through standard memory access.
 *
 * @since 2020/04/02
 */
public class WritableAssemblyMemory
	extends ReadableAssemblyMemory
	implements WritableBasicMemory
{
	/**
	 * Initializes the memory information.
	 *
	 * @param __base The address.
	 * @param __size The memory size.
	 * @throws IllegalArgumentException If the size is zero or negative.
	 * @since 2020/04/02
	 */
	public WritableAssemblyMemory(long __base, int __size)
		throws IllegalArgumentException
	{
		super(__base, __size);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public void write(long __addr, byte __v)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr >= this.size)
			throw new MemoryAccessException(__addr, "MOOB");
		
		Assembly.memWriteByte(this.base, (int)__addr, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public void write(long __addr, byte[] __b, int __o, int __l)
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
	public void writeInt(long __addr, int __v)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr + 4 > this.size)
			throw new MemoryAccessException(__addr, "MOOB");
		
		Assembly.memWriteInt(this.base, (int)__addr, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public void writeLong(long __addr, long __v)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr + 8 > this.size)
			throw new MemoryAccessException(__addr, "MOOB");
			
		Assembly.memWriteLong(this.base, (int)__addr, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/02
	 */
	@Override
	public void writeShort(long __addr, short __v)
		throws MemoryAccessException
	{
		if (__addr < 0 || __addr + 2 > this.size)
			throw new MemoryAccessException(__addr, "MOOB");
			
		Assembly.memWriteShort(this.base, (int)__addr, __v);
	}
}
