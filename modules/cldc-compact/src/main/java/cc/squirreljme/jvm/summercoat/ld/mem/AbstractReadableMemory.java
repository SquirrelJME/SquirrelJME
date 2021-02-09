// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.mem;

/**
 * This is a class which handles reading of memory by using pure integers
 * instead of forcing all implementations to implement short and byte
 * readers.
 *
 * @since 2019/04/21
 */
public abstract class AbstractReadableMemory
	implements ReadableMemory
{
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memReadBytes(int __addr, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		for (int i = 0; i < __l; i++)
			__b[__o++] = (byte)this.memReadByte(__addr++);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/17
	 */
	@Override
	public MemHandleReference memReadHandle(int __addr)
	{
		return new MemHandleReference(this.memReadInt(__addr));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadInt(int __addr)
	{
		return (this.memReadByte(__addr++) << 24) |
			(this.memReadByte(__addr++) << 16) |
			(this.memReadByte(__addr++) << 8) |
			this.memReadByte(__addr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/17
	 */
	@Override
	public long memReadLong(int __addr)
	{
		return ((long)this.memReadByte(__addr++) << 56) |
			((long)this.memReadByte(__addr++) << 48) |
			((long)this.memReadByte(__addr++) << 40) |
			((long)this.memReadByte(__addr++) << 32) |
			((long)this.memReadByte(__addr++) << 24) |
			(this.memReadByte(__addr++) << 16) |
			(this.memReadByte(__addr++) << 8) |
			this.memReadByte(__addr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadShort(int __addr)
	{
		return (this.memReadByte(__addr++) << 8) |
			this.memReadByte(__addr);
	}
}
