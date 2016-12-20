// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.data;

/**
 * This interface is used to describe classes which allow access to data via
 * a specific position.
 *
 * @since 2016/08/11
 */
public interface RandomAccessData
	extends GettableEndianess
{
	/**
	 * Reads multiple bytes.
	 *
	 * @param __p The position to read from.
	 * @param __b The byte array to place values at.
	 * @param __o The offset to read from.
	 * @param __l The number of bytes to read.
	 * @throws IndexOutOfBoundsException If the position is not within bounds;
	 * the offset and/or length are negative; or the offset and length exceed
	 * the array bounds.
	 * @since 2016/08/11
	 */
	public abstract void read(int __p, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException;
	
	/**
	 * Reads a byte at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/08/11
	 */
	public abstract int readByte(int __p)
		throws IndexOutOfBoundsException;
	
	/**
	 * Reads a double at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/08/11
	 */
	public abstract double readDouble(int __p)
		throws IndexOutOfBoundsException;
	
	/**
	 * Reads a float at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/08/11
	 */
	public abstract double readFloat(int __p)
		throws IndexOutOfBoundsException;
	
	/**
	 * Reads an integer at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/08/11
	 */
	public abstract int readInt(int __p)
		throws IndexOutOfBoundsException;
	
	/**
	 * Reads a long at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/08/11
	 */
	public abstract long readLong(int __p)
		throws IndexOutOfBoundsException;
	
	/**
	 * Reads a short at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/08/11
	 */
	public abstract int readShort(int __p)
		throws IndexOutOfBoundsException;
	
	/**
	 * Reads an unsigned byte at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/08/11
	 */
	public abstract int readUnsignedByte(int __p)
		throws IndexOutOfBoundsException;
	
	/**
	 * Reads an unsigned short at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/08/11
	 */
	public abstract int readUnsignedShort(int __p)
		throws IndexOutOfBoundsException;
}

