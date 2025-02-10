// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This provides random access to data in the given byte array.
 *
 * @since 2016/08/11
 */
public class ByteArrayData
	implements RandomAccessData
{
	/** The starting offset. */
	protected final int offset;
	
	/** The number of bytes to access. */
	protected final int length;
	
	/** The endianess of the data to read. */
	protected final DataEndianess endianess;
	
	/** The backing array. */
	final byte[] _array;
	
	/**
	 * This wraps the given set of bytes.
	 *
	 * @param __end The endianess of the data.
	 * @param __b The bytes to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/11
	 */
	public ByteArrayData(DataEndianess __end, byte... __b)
		throws NullPointerException
	{
		this(__end, __b, 0, __b.length);
	}	
	
	/**
	 * This wraps the given set of bytes.
	 *
	 * @param __end The endianess of the data.
	 * @param __b The bytes to wrap.
	 * @param __o The starting offset.
	 * @param __l The length of bytes to wrap.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array size.
	 * @throws NullPointerException On null arguments
	 * @since 2016/08/11
	 */
	public ByteArrayData(DataEndianess __end, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__end == null || __b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("AIOB");
		
		// Set
		this.endianess = __end;
		this._array = __b;
		this.offset = __o;
		this.length = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public DataEndianess getEndianess()
	{
		return this.endianess;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public void read(int __p, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public int readByte(int __p)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public double readDouble(int __p)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public double readFloat(int __p)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public int readInt(int __p)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public long readLong(int __p)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public int readShort(int __p)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public int readUnsignedByte(int __p)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/11
	 */
	@Override
	public int readUnsignedShort(int __p)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
}

