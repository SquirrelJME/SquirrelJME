// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.io;

import cc.squirreljme.jvm.Assembly;

/**
 * This is a blob of data which is sourced from memory.
 *
 * @since 2019/09/22
 */
public final class MemoryBlob
	extends BinaryBlob
{
	/** The base address. */
	protected final int base;
	
	/** The size. */
	protected final int size;
	
	/**
	 * Initializes the memory blob.
	 *
	 * @param __base The base address of the blob.
	 * @param __size The size of the blob.
	 * @throws IllegalArgumentException If the size is negative.
	 * @since 2019/09/22
	 */
	public MemoryBlob(int __base, int __size)
		throws IllegalArgumentException
	{
		// {@squirreljme.error SV06 Negative memory blob size.}
		if (__size < 0)
			throw new IllegalArgumentException("SV06");
		
		this.base = __base;
		this.size = __size;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/22
	 */
	@Override
	public int readByte(int __o)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error SV09 Out of range region read.}
		if (__o < 0 || __o >= this.size)
			throw new IndexOutOfBoundsException("SV09");
		
		return (byte)Assembly.memReadByte(this.base, __o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/17
	 */
	@Override
	public int readJavaInt(int __o)
		throws IndexOutOfBoundsException
	{
		return Assembly.memReadJavaInt(this.base, __o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/17
	 */
	@Override
	public short readJavaShort(int __o)
		throws IndexOutOfBoundsException
	{
		return (short)Assembly.memReadJavaShort(this.base, __o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/17
	 */
	@Override
	public int readJavaUnsignedShort(int __o)
		throws IndexOutOfBoundsException
	{
		return Assembly.memReadJavaShort(this.base, __o) & 0xFFFF;
	}
}

