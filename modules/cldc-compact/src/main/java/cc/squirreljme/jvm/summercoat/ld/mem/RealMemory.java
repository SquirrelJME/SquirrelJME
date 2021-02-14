// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.mem;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.summercoat.lle.LLERuntimeShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Access to real memory within the system, this should access the entire
 * address space that is available to the system.
 *
 * @since 2021/02/14
 */
public final class RealMemory
	extends AbstractWritableMemory
{
	/**
	 * Initializes the real memory accessor.
	 * 
	 * @since 2021/02/14
	 */
	public RealMemory()
	{
		// Use the byte order of the system so it matches properly for
		// read/write operations
		super(LLERuntimeShelf.byteOrder());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@SuppressWarnings("MagicNumber")
	@Override
	public int memReadByte(long __addr)
	{
		return Assembly.memReadByte(__addr, 0) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public void memReadBytes(long __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public MemHandleReference memReadHandle(long __addr)
	{
		int rv = Assembly.memReadInt(__addr, 0);
		return (rv == 0 ? null : new MemHandleReference(rv));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public int memReadInt(long __addr)
	{
		return Assembly.memReadInt(__addr, 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public long memReadLong(long __addr)
	{
		return Assembly.memReadLong(__addr, 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@SuppressWarnings("MagicNumber")
	@Override
	public int memReadShort(long __addr)
	{
		return Assembly.memReadShort(__addr, 0) & 0xFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public int memRegionOffset()
	{
		// Always starts at zero
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public long memRegionSize()
	{
		// This can address all of 64-bit memory!
		return Memory.MAX_64BIT;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public void memWriteByte(long __addr, int __v)
	{
		Assembly.memWriteByte(__addr, 0, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public void memWriteBytes(long __addr, byte[] __b, int __o, int __l)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public void memWriteHandle(long __addr, MemHandleReference __v)
	{
		Assembly.memWriteInt(__addr, 0, (__v == null ? 0 : __v.id));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public void memWriteInt(long __addr, int __v)
	{
		Assembly.memWriteInt(__addr, 0, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public void memWriteLong(long __addr, long __v)
	{
		Assembly.memWriteLong(__addr, 0, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public void memWriteShort(long __addr, int __v)
	{
		Assembly.memWriteShort(__addr, 0, __v);
	}
}
