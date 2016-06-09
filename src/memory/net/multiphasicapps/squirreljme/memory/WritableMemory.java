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
 * This interface is used by anything which provides write access to memory.
 *
 * @since 2016/06/05
 */
@Deprecated
public interface WritableMemory
{
	/**
	 * Writes a single byte value.
	 *
	 * @param __addr The address to write to.
	 * @param __at If {@code true} then the write is atomic.
	 * @param __v The value to write.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @since 2016/06/05
	 */
	public abstract void writeByte(long __addr, boolean __at, byte __v)
		throws MemoryIOException;
	
	/**
	 * Writes multiple byte values.
	 * 
	 * @param __addr The address to write to.
	 * @param __v The values to write.
	 * @param __o The starting offset to read values from.
	 * @param __l The number of values to write.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the source array length.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @throws NullPointerException If no source array was specified.
	 * @since 2016/06/05
	 */
	public abstract void writeBytes(long __addr, byte[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Writes a single char value.
	 *
	 * @param __addr The address to write to.
	 * @param __at If {@code true} then the write is atomic.
	 * @param __v The value to write.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @since 2016/06/05
	 */
	public abstract void writeChar(long __addr, boolean __at, char __v)
		throws MemoryIOException;
	
	/**
	 * Writes multiple char values.
	 *
	 * @param __addr The address to write to.
	 * @param __v The values to write.
	 * @param __o The starting offset to read values from.
	 * @param __l The number of values to write.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the source array length.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @throws NullPointerException If no source array was specified.
	 * @since 2016/06/05
	 */
	public abstract void writeChars(long __addr, char[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Writes a single double value.
	 *
	 * @param __addr The address to write to.
	 * @param __at If {@code true} then the write is atomic.
	 * @param __v The value to write.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @since 2016/06/05
	 */
	public abstract void writeDouble(long __addr, boolean __at, double __v)
		throws MemoryIOException;
	
	/**
	 * Writes multiple double values.
	 *
	 * @param __addr The address to write to.
	 * @param __v The values to write.
	 * @param __o The starting offset to read values from.
	 * @param __l The number of values to write.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the source array length.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @throws NullPointerException If no source array was specified.
	 * @since 2016/06/05
	 */
	public abstract void writeDoubles(long __addr, double[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Writes a single float value.
	 *
	 * @param __addr The address to write to.
	 * @param __at If {@code true} then the write is atomic.
	 * @param __v The value to write.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @since 2016/06/05
	 */
	public abstract void writeFloat(long __addr, boolean __at, float __v)
		throws MemoryIOException;
	
	/**
	 * Writes multiple float values.
	 *
	 * @param __addr The address to write to.
	 * @param __v The values to write.
	 * @param __o The starting offset to read values from.
	 * @param __l The number of values to write.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the source array length.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @throws NullPointerException If no source array was specified.
	 * @since 2016/06/05
	 */
	public abstract void writeFloats(long __addr, float[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Writes a single int value.
	 *
	 * @param __addr The address to write to.
	 * @param __at If {@code true} then the write is atomic.
	 * @param __v The value to write.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @since 2016/06/05
	 */
	public abstract void writeInt(long __addr, boolean __at, int __v)
		throws MemoryIOException;
	
	/**
	 * Writes multiple int values.
	 *
	 * @param __addr The address to write to.
	 * @param __v The values to write.
	 * @param __o The starting offset to read values from.
	 * @param __l The number of values to write.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the source array length.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @throws NullPointerException If no source array was specified.
	 * @since 2016/06/05
	 */
	public abstract void writeInts(long __addr, int[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Writes a single long value.
	 *
	 * @param __addr The address to write to.
	 * @param __at If {@code true} then the write is atomic.
	 * @param __v The value to write.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @since 2016/06/05
	 */
	public abstract void writeLong(long __addr, boolean __at, long __v)
		throws MemoryIOException;
	
	/**
	 * Writes multiple long values.
	 *
	 * @param __addr The address to write to.
	 * @param __v The values to write.
	 * @param __o The starting offset to read values from.
	 * @param __l The number of values to write.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the source array length.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @throws NullPointerException If no source array was specified.
	 * @since 2016/06/05
	 */
	public abstract void writeLongs(long __addr, long[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Writes a single short value.
	 *
	 * @param __addr The address to write to.
	 * @param __at If {@code true} then the write is atomic.
	 * @param __v The value to write.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @since 2016/06/05
	 */
	public abstract void writeShort(long __addr, boolean __at, short __v)
		throws MemoryIOException;
	
	/**
	 * Writes multiple short values.
	 *
	 * @param __addr The address to write to.
	 * @param __v The values to write.
	 * @param __o The starting offset to read values from.
	 * @param __l The number of values to write.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the source array length.
	 * @throws MemoryIOException If the memory could not be written to.
	 * @throws NullPointerException If no source array was specified.
	 * @since 2016/06/05
	 */
	public abstract void writeShorts(long __addr, short[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
}

