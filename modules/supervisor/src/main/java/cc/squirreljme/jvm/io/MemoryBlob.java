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
	 * Returns the base address of this blob.
	 *
	 * @return The base address of this blob.
	 * @since 2019/12/01
	 */
	public final int baseAddress()
	{
		return this.base;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/01
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof MemoryBlob))
			return false;
		
		MemoryBlob o = (MemoryBlob)__o;
		return this.base == o.base &&
			this.size == o.size;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/01
	 */
	@Override
	public final int hashCode()
	{
		return this.base ^ (-this.size);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/22
	 */
	@Override
	public byte readByte(int __o)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error SV09 Out of range region read.}
		if (__o < 0 || __o >= this.size)
			throw new IndexOutOfBoundsException("SV09");
		
		return (byte)Assembly.memReadByte(this.base, __o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/24
	 */
	@Override
	public void readBytes(int __i, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__i < 0 || __o < 0 || (__o + __l) > __b.length)
			throw new NullPointerException("IOOB");
			
		// {@squirreljme.error SV0u Out of range region read.}
		if (__i < 0 || (__i + __l) > this.size)
			throw new IndexOutOfBoundsException("SV0u");
		
		for (int i = 0, bp = this.base + __i; i < __l; i++)
			__b[__o + i] = (byte)Assembly.memReadByte(bp, i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/17
	 */
	@Override
	public int readJavaInt(int __o)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error SV09 Out of range region read.}
		if (__o < 0 || (__o + 3) >= this.size)
			throw new IndexOutOfBoundsException("SV09");
		
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
		// {@squirreljme.error SV09 Out of range region read.}
		if (__o < 0 || (__o + 1) >= this.size)
			throw new IndexOutOfBoundsException("SV09");
		
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
		// {@squirreljme.error SV09 Out of range region read.}
		if (__o < 0 || (__o + 1) >= this.size)
			throw new IndexOutOfBoundsException("SV09");
		
		return Assembly.memReadJavaShort(this.base, __o) & 0xFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/17
	 */
	@Override
	public int size()
	{
		return this.size;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/11/17
	 */
	@Override
	public BinaryBlob subSection(int __base, int __len)
		throws IndexOutOfBoundsException
	{
		int size = this.size;
		if (__base < 0 || __len < 0 || (__base + __len) > size)
			throw new IndexOutOfBoundsException("IOOB " + __base + " "
				+ __len);
		
		return new MemoryBlob(this.base + __base, __len);
	}
}

