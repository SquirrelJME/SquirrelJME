// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.io;

/**
 * This is a binary blob which provides memory and I/O access.
 *
 * Sub-classes only need to implement reading of a single byte, however
 * implementing the other methods will result in faster code.
 *
 * @since 2019/09/22
 */
public abstract class BinaryBlob
{
	/**
	 * Reads a single byte.
	 *
	 * @param __o The offset.
	 * @return The read data.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @since 2019/09/22
	 */
	public abstract byte readByte(int __o);
	
	/**
	 * Returns the size of this blob.
	 *
	 * @return The size of this blob.
	 * @since 2019/11/17
	 */
	public abstract int size();
	
	/**
	 * Reads in multiple bytes.
	 *
	 * @param __i The read index.
	 * @param __b The bytes to read.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the index is out of bounds, or
	 * the offset and/or length are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/24
	 */
	public void readBytes(int __i, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__i < 0 || __o < 0 || (__o + __l) > __b.length)
			throw new NullPointerException("IOOB");
		
		for (int i = 0; i < __l; i++)
			__b[__o + i] = (byte)this.readByte(__i + i);
	}
	
	/**
	 * Reads a Java integer.
	 *
	 * @param __o The offset.
	 * @return The read data.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @since 2019/09/22
	 */
	public int readJavaInt(int __o)
		throws IndexOutOfBoundsException
	{
		return ((this.readByte(__o) & 0xFF) << 24) |
			((this.readByte(__o + 1) & 0xFF) << 16) |
			((this.readByte(__o + 2) & 0xFF) << 8) |
			((this.readByte(__o + 3) & 0xFF));
	}
	
	/**
	 * Reads a Java short.
	 *
	 * @param __o The offset.
	 * @return The read data.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @since 2019/11/17
	 */
	public short readJavaShort(int __o)
		throws IndexOutOfBoundsException
	{
		return (short)(((this.readByte(__o + 1) & 0xFF) << 8) |
			((this.readByte(__o) & 0xFF)));
	}
	
	/**
	 * Reads an unsigned Java short.
	 *
	 * @param __o The offset.
	 * @return The read data.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @since 2019/11/17
	 */
	public int readJavaUnsignedShort(int __o)
		throws IndexOutOfBoundsException
	{
		return this.readJavaShort(__o) & 0xFFFF;
	}
	
	/**
	 * Reads a single unsigned byte.
	 *
	 * @param __o The offset.
	 * @return The read data.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @since 2019/09/22
	 */
	public int readUnsignedByte(int __o)
		throws IndexOutOfBoundsException
	{
		return this.readByte(__o) & 0xFF;
	}
	
	/**
	 * Reads a Modified UTF encoded string.
	 *
	 * @param __o The offset.
	 * @return The read string.
	 * @throws IndexOutOfBoundsException If the string is outside of bounds.
	 * @since 2019/09/24
	 */
	public final String readUTF(int __o)
		throws IndexOutOfBoundsException
	{
		// Read length of data first
		int len = this.readJavaUnsignedShort(__o);
		
		// Read in data to byte array
		byte[] data = new byte[len];
		this.readBytes(__o + 2, data, 0, len);
		
		// Initialize string with it
		return new String(data);
	}
	
	/**
	 * Returns a blob which is a sub-section of the given blob.
	 *
	 * It is recommended to replace this class with a more efficient one if
	 * possible, as otherwise it will use a less-efficient sub-section
	 * implementation for this blob.
	 *
	 * @param __base The base of this blob within this one.
	 * @param __len The length of the section.
	 * @return The sub-section.
	 * @throws IndexOutOfBoundsException If the base or length are negative
	 * or exceed the bounds of this blob.
	 * @since 2019/11/17
	 */
	public BinaryBlob subSection(int __base, int __len)
		throws IndexOutOfBoundsException
	{
		if (__base < 0 || __len < 0 || (__base + __len) > this.size())
			throw new IndexOutOfBoundsException("IOOB");
		
		throw new todo.TODO();
	}
}

