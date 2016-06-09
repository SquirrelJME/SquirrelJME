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
 * This is used by anything which provides read access to memory.
 *
 * @since 2016/06/05
 */
@Deprecated
public interface ReadableMemory
{
	/**
	 * Reads a single byte value.
	 *
	 * @param __addr The address to read to.
	 * @param __at If {@code true} then the read is atomic.
	 * @return The read value.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @since 2016/06/05
	 */
	public abstract byte readByte(long __addr, boolean __at)
		throws MemoryIOException;
	
	/**
	 * Reads multiple byte values.
	 * 
	 * @param __addr The address to read to.
	 * @param __v The destination value array.
	 * @param __o The starting offset to read values into.
	 * @param __l The number of values to read.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the destination array length.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @throws NullPointerException If no destination array was specified.
	 * @since 2016/06/05
	 */
	public abstract void readBytes(long __addr, byte[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Reads a single char value.
	 *
	 * @param __addr The address to read to.
	 * @param __at If {@code true} then the read is atomic.
	 * @return The read value.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @since 2016/06/05
	 */
	public abstract char readChar(long __addr, boolean __at)
		throws MemoryIOException;
	
	/**
	 * Reads multiple char values.
	 *
	 * @param __addr The address to read to.
	 * @param __v The destination value array.
	 * @param __o The starting offset to read values into.
	 * @param __l The number of values to read.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the destination array length.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @throws NullPointerException If no destination array was specified.
	 * @since 2016/06/05
	 */
	public abstract void readChars(long __addr, char[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Reads a single double value.
	 *
	 * @param __addr The address to read to.
	 * @param __at If {@code true} then the read is atomic.
	 * @return The read value.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @since 2016/06/05
	 */
	public abstract double readDouble(long __addr, boolean __at)
		throws MemoryIOException;
	
	/**
	 * Reads multiple double values.
	 *
	 * @param __addr The address to read to.
	 * @param __v The destination value array.
	 * @param __o The starting offset to read values into.
	 * @param __l The number of values to read.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the destination array length.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @throws NullPointerException If no destination array was specified.
	 * @since 2016/06/05
	 */
	public abstract void readDoubles(long __addr, double[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Reads a single float value.
	 *
	 * @param __addr The address to read to.
	 * @param __at If {@code true} then the read is atomic.
	 * @return The read value.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @since 2016/06/05
	 */
	public abstract float readFloat(long __addr, boolean __at)
		throws MemoryIOException;
	
	/**
	 * Reads multiple float values.
	 *
	 * @param __addr The address to read to.
	 * @param __v The destination value array.
	 * @param __o The starting offset to read values into.
	 * @param __l The number of values to read.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the destination array length.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @throws NullPointerException If no destination array was specified.
	 * @since 2016/06/05
	 */
	public abstract void readFloats(long __addr, float[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Reads a single int value.
	 *
	 * @param __addr The address to read to.
	 * @param __at If {@code true} then the read is atomic.
	 * @return The read value.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @since 2016/06/05
	 */
	public abstract int readInt(long __addr, boolean __at)
		throws MemoryIOException;
	
	/**
	 * Reads multiple int values.
	 *
	 * @param __addr The address to read to.
	 * @param __v The destination value array.
	 * @param __o The starting offset to read values into.
	 * @param __l The number of values to read.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the destination array length.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @throws NullPointerException If no destination array was specified.
	 * @since 2016/06/05
	 */
	public abstract void readInts(long __addr, int[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Reads a single long value.
	 *
	 * @param __addr The address to read to.
	 * @param __at If {@code true} then the read is atomic.
	 * @return The read value.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @since 2016/06/05
	 */
	public abstract long readLong(long __addr, boolean __at)
		throws MemoryIOException;
	
	/**
	 * Reads multiple long values.
	 *
	 * @param __addr The address to read to.
	 * @param __v The destination value array.
	 * @param __o The starting offset to read values into.
	 * @param __l The number of values to read.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the destination array length.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @throws NullPointerException If no destination array was specified.
	 * @since 2016/06/05
	 */
	public abstract void readLongs(long __addr, long[] __v,
		int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
	
	/**
	 * Reads a single short value.
	 *
	 * @param __addr The address to read to.
	 * @param __at If {@code true} then the read is atomic.
	 * @return The read value.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @since 2016/06/05
	 */
	public abstract short readShort(long __addr, boolean __at)
		throws MemoryIOException;
	
	/**
	 * Reads multiple short values.
	 *
	 * @param __addr The address to read to.
	 * @param __v The destination value array.
	 * @param __o The starting offset to read values into.
	 * @param __l The number of values to read.
	 * @throws IndexOutOfBoundsException If the offset and/or the length are
	 * negative or exceed the destination array length.
	 * @throws MemoryIOException If the memory could not be read from.
	 * @throws NullPointerException If no destination array was specified.
	 * @since 2016/06/05
	 */
	public abstract void readShorts(long __addr, short[] __v, int __o, int __l)
		throws IndexOutOfBoundsException, MemoryIOException,
			NullPointerException;
}

