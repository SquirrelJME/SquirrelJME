// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This class is used to provide simple writing for types other than integers
 * into memory.
 *
 * @since 2019/04/21
 */
public abstract class AbstractWritableMemory
	extends AbstractReadableMemory
	implements WritableMemory
{
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memWriteBytes(int __a, byte[] __b, int __o, int __l)
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		for (int i = 0; i < __l; i++)
			this.memWriteByte(__a++, __b[__o++] & 0xFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memWriteInt(int __addr, int __v)
	{
		this.memWriteByte(__addr++, __v >>> 24);
		this.memWriteByte(__addr++, __v >>> 16);
		this.memWriteByte(__addr++, __v >>> 8);
		this.memWriteByte(__addr++, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public void memWriteShort(int __addr, int __v)
	{
		this.memWriteByte(__addr++, __v >>> 8);
		this.memWriteByte(__addr++, __v);
	}
}

