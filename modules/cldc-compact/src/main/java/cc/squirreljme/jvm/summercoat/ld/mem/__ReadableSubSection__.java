// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.mem;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Readable sub-section of memory.
 *
 * @since 2021/04/03
 */
class __ReadableSubSection__
	implements ReadableMemory
{
	/** Readable memory. */
	protected final ReadableMemory readable;
	
	/** The base memory address. */
	protected final long base;
	
	/** The length of memory to access. */
	protected final long length;
	
	/**
	 * Initializes the sub-section.
	 * 
	 * @param __basis The memory to access.
	 * @param __base The base address.
	 * @param __len The length of the region to access.
	 * @throws IndexOutOfBoundsException If the base and/or length are
	 * negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/03
	 */
	__ReadableSubSection__(ReadableMemory __basis, long __base,
		long __len)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__basis == null)
			throw new NullPointerException("NARG");
		if (__base < 0 || __len < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		this.readable = __basis;
		this.base = __base;
		this.length = __len;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/03
	 */
	@Override
	public final long absoluteAddress(long __addr)
		throws MemoryAccessException, NotRealMemoryException
	{
		// {@squirreljme.error ZZ4s Address is out of range of the section.}
		if (__addr < 0 || __addr >= this.length)
			throw new MemoryAccessException(__addr, "ZZ4s");
		
		return this.readable.absoluteAddress(this.base + __addr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/03
	 */
	@Override
	public int memReadByte(long __addr)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/03
	 */
	@Override
	public void memReadBytes(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/03
	 */
	@Override
	public MemHandleReference memReadHandle(long __addr)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/03
	 */
	@Override
	public int memReadInt(long __addr)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/03
	 */
	@Override
	public long memReadLong(long __addr)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/03
	 */
	@Override
	public int memReadShort(long __addr)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/03
	 */
	@Override
	public final long memRegionSize()
	{
		return this.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/03
	 */
	@Override
	public ReadableMemory subSection(long __base, long __len)
		throws MemoryAccessException
	{
		// {@squirreljme.error ZZ4q Sub-section would be out of range of
		// this memory region. (The base address; The length)}
		if (__base < 0 || __len < 0 || (__base + __len) > this.memRegionSize())
			throw new MemoryAccessException(__base,
				"ZZ4q " + __base + " " + __len);
		
		return this.readable.subSection(this.base + __base, __len);
	}
}
