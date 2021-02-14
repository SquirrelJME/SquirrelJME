// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.mem;

import cc.squirreljme.jvm.mle.constants.ByteOrderType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a class which handles reading of memory by using pure integers
 * instead of forcing all implementations to implement short and byte
 * readers.
 *
 * @since 2019/04/21
 */
@SuppressWarnings("MagicNumber")
public abstract class AbstractReadableMemory
	implements HasByteOrder, ReadableMemory
{
	/** The byte order of this memory. */
	protected final int byteOrder;
	
	/**
	 * Uses big endian for the memory.
	 * 
	 * @since 2021/02/14
	 */
	public AbstractReadableMemory()
	{
		this(ByteOrderType.BIG_ENDIAN);
	}
	
	/**
	 * Uses the specific byte order for the memory.
	 * 
	 * @param __byteOrder The {@link ByteOrderType} used.
	 * @throws IllegalArgumentException If the byte order is not valid.
	 * @since 2021/02/14
	 */
	public AbstractReadableMemory(int __byteOrder)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ3u Invalid byte order.}
		if (__byteOrder != ByteOrderType.BIG_ENDIAN &&
			__byteOrder != ByteOrderType.LITTLE_ENDIAN)
			throw new IllegalArgumentException("ZZ3u " + __byteOrder);
		
		this.byteOrder = __byteOrder;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public final int byteOrder()
	{
		return this.byteOrder;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memReadBytes(long __addr, byte[] __b, int __o, int __l)
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
	public MemHandleReference memReadHandle(long __addr)
	{
		return new MemHandleReference(this.memReadInt(__addr));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadInt(long __addr)
	{
		switch (this.byteOrder)
		{
			case ByteOrderType.BIG_ENDIAN:
				return (this.memReadByte(__addr++) << 24) |
					(this.memReadByte(__addr++) << 16) |
					(this.memReadByte(__addr++) << 8) |
					this.memReadByte(__addr);
					
			case ByteOrderType.LITTLE_ENDIAN:
				return (this.memReadByte(__addr++)) |
					(this.memReadByte(__addr++) << 8) |
					(this.memReadByte(__addr++) << 16) |
					this.memReadByte(__addr) << 24;
			
			default:
				throw Debugging.oops();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/17
	 */
	@Override
	public long memReadLong(long __addr)
	{
		switch (this.byteOrder)
		{
			case ByteOrderType.BIG_ENDIAN:
				return ((long)this.memReadByte(__addr++) << 56) |
					((long)this.memReadByte(__addr++) << 48) |
					((long)this.memReadByte(__addr++) << 40) |
					((long)this.memReadByte(__addr++) << 32) |
					((long)this.memReadByte(__addr++) << 24) |
					(this.memReadByte(__addr++) << 16) |
					(this.memReadByte(__addr++) << 8) |
					this.memReadByte(__addr);
			
			case ByteOrderType.LITTLE_ENDIAN:
				return (this.memReadByte(__addr++)) |
					(this.memReadByte(__addr++) << 8) |
					(this.memReadByte(__addr++) << 16) |
					(this.memReadByte(__addr++) << 24) |
					((long)this.memReadByte(__addr++) << 32) |
					((long)this.memReadByte(__addr++) << 40) |
					((long)this.memReadByte(__addr++) << 48) |
					(long)this.memReadByte(__addr) << 56;
			
			default:
				throw Debugging.oops();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public int memReadShort(long __addr)
	{
		switch (this.byteOrder)
		{
			case ByteOrderType.BIG_ENDIAN:
				return (this.memReadByte(__addr++) << 8) |
					this.memReadByte(__addr);
					
			case ByteOrderType.LITTLE_ENDIAN:
				return (this.memReadByte(__addr++)) |
					this.memReadByte(__addr) << 8;
			
			default:
				throw Debugging.oops();
		}
	}
}
