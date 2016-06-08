// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.memory;

/**
 * This is a memory pool which implements some methods which may be overridden
 * if there is a more efficient means of writing the data.
 *
 * @since 2016/06/05
 */
public abstract class AbstractMemoryPool
	implements MemoryPool
{
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void readBytes(long __addr, byte[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public char readChar(long __addr, boolean __at)
		throws MemoryIOException
	{
		return (char)readShort(__addr, __at);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void readChars(long __addr, char[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public double readDouble(long __addr, boolean __at)
		throws MemoryIOException
	{
		return Double.longBitsToDouble(readLong(__addr, __at));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void readDoubles(long __addr, double[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public float readFloat(long __addr, boolean __at)
		throws MemoryIOException
	{
		return Float.intBitsToFloat(readInt(__addr, __at));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void readFloats(long __addr, float[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void readInts(long __addr, int[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void readLongs(long __addr, long[] __v, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void readShorts(long __addr, short[] __v, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeBytes(long __addr, byte[] __v, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeChar(long __addr, boolean __at, char __v)
		throws MemoryIOException
	{
		writeShort(__addr, __at, (short)__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeChars(long __addr, char[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeDouble(long __addr, boolean __at, double __v)
		throws MemoryIOException
	{
		writeLong(__addr, __at, Double.doubleToLongBits(__v));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeDoubles(long __addr, double[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeFloat(long __addr, boolean __at, float __v)
		throws MemoryIOException
	{
		writeInt(__addr, __at, Float.floatToIntBits(__v));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeFloats(long __addr, float[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeInts(long __addr, int[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeLongs(long __addr, long[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/05
	 */
	@Override
	public void writeShorts(long __addr, short[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
}

