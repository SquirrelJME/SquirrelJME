// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.jvm.summercoat.ld.mem.AbstractWritableMemory;

/**
 * Raw memory access.
 *
 * @since 2019/04/21
 */
public final class RawMemory
	extends AbstractWritableMemory
{
	/** The memory offset. */
	protected final int offset;
	
	/** The memory size. */
	protected final int size;
	
	/** The memory data. */
	protected final byte[] bytes;
	
	/**
	 * Raw memory space.
	 *
	 * @param __off The offset.
	 * @param __sz The size of this region.
	 * @since 2019/04/21
	 */
	public RawMemory(int __off, int __sz)
	{
		this.offset = __off;
		this.size = __sz;
		this.bytes = new byte[__sz];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memReadByte(int __addr)
	{
		// Treat out of region reads as invalid data
		if (__addr < 0 || __addr >= this.size)
			throw new VMMemoryAccessException("Invalid Address: " + __addr);
		
		return (this.bytes[__addr] & 0xFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memReadInt(int __addr)
	{
		// Treat out of region reads as invalid data
		if (__addr < 0 || __addr >= this.size - 3)
			throw new VMMemoryAccessException("Invalid Address: " + __addr);
		
		byte[] bytes = this.bytes;
		return ((bytes[__addr++] & 0xFF) << 24) |
			((bytes[__addr++] & 0xFF) << 16) |
			((bytes[__addr++] & 0xFF) << 8) |
			(bytes[__addr++] & 0xFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memReadShort(int __addr)
	{
		// Treat out of region reads as invalid data
		if (__addr < 0 || __addr >= this.size - 1)
			throw new VMMemoryAccessException("Invalid Address: " + __addr);
		
		byte[] bytes = this.bytes;
		return (((bytes[__addr++] & 0xFF) << 8) |
			(bytes[__addr++] & 0xFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memRegionOffset()
	{
		return this.offset;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final int memRegionSize()
	{
		return this.size;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memWriteByte(int __addr, int __v)
	{
		// {@squirreljme.error AE06 Cannot write to the specified address.
		// (The address to write to)}
		if (__addr < 0 || __addr >= this.size - 1)
			throw new VMRuntimeException(String.format("AE06 %08x",
				this.offset + __addr));
		
		byte[] bytes = this.bytes;
		bytes[__addr++] = (byte)(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final void memWriteInt(int __addr, int __v)
	{
		// {@squirreljme.error AE07 Cannot write to the specified address.
		// (The address to write to)}
		if (__addr < 0 || __addr >= this.size - 3)
			throw new VMRuntimeException(String.format("AE07 %08x",
				this.offset + __addr));
		
		byte[] bytes = this.bytes;
		bytes[__addr++] = (byte)(__v >>> 24);
		bytes[__addr++] = (byte)(__v >>> 16);
		bytes[__addr++] = (byte)(__v >>> 8);
		bytes[__addr++] = (byte)(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final void memWriteShort(int __addr, int __v)
	{
		// {@squirreljme.error AE08 Cannot write to the specified address.
		// (The address to write to)}
		if (__addr < 0 || __addr >= this.size - 2)
			throw new VMRuntimeException(String.format("AE08 %08x",
				this.offset + __addr));
		
		byte[] bytes = this.bytes;
		bytes[__addr++] = (byte)(__v >>> 8);
		bytes[__addr++] = (byte)(__v);
	}
}

