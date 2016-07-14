// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

/**
 * This is a data buffer which provides raw read access to the specified byte
 * array.
 *
 * @since 2016/04/27
 */
public class CIByteBuffer
{
	/** The source data array. */
	private final byte[] _data;
	
	/** The base array offset. */
	private final int _offset;
	
	/** The area to read from. */
	private final int _length;
	
	/**
	 * Initializes the data buffer.
	 *
	 * @param __d The data buffer.
	 * @param __o The offset in the array to read from.
	 * @param __l The length of the data segment.
	 * @throws IndexOutOfBoundsException If the offset or length are negative
	 * or when combined they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public CIByteBuffer(byte[] __d, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __d.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Set
		_data = __d;
		_offset = __o;
		_length = __l;
	}
	
	/**
	 * Returns the length of the buffer.
	 *
	 * @return The buffer length.
	 * @since 2016/04/29
	 */
	public int length()
	{
		return _length;
	}
	
	/**
	 * Reads a byte at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/04/27
	 */
	public byte readByte(int __p)
		throws IndexOutOfBoundsException
	{
		int o = __bounds(__p, 1);
		return _data[o];
	}
	
	/**
	 * Reads a byte at the given position and offset.
	 *
	 * @param __p The position to read from.
	 * @param __o The relative offset.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds
	 * or the offset is negative.
	 * @since 2016/05/08
	 */
	public byte readByte(int __p, int __o)
		throws IndexOutOfBoundsException
	{
		return readByte(__rel(__p, __o));
	}
	
	/**
	 * Reads an int at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/04/27
	 */
	public int readInt(int __p)
		throws IndexOutOfBoundsException
	{
		int o = __bounds(__p, 4);
		return ((((int)_data[o]) & 0xFF) << 24) |
			((((int)_data[o + 1]) & 0xFF) << 16) |
			((((int)_data[o + 2]) & 0xFF) << 8) |
			((((int)_data[o + 3]) & 0xFF));
	}
	
	/**
	 * Reads an int at the given position and offset.
	 *
	 * @param __p The position to read from.
	 * @param __o The relative offset.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds
	 * or the offset is negative.
	 * @since 2016/05/08
	 */
	public int readInt(int __p, int __o)
		throws IndexOutOfBoundsException
	{
		return readInt(__rel(__p, __o));
	}
	
	/**
	 * Reads a short at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/04/27
	 */
	public short readShort(int __p)
		throws IndexOutOfBoundsException
	{
		int o = __bounds(__p, 2);
		return (short)(((((int)_data[o]) & 0xFF) << 8) |
			((((int)_data[o + 1]) & 0xFF)));
	}
	
	/**
	 * Reads a short at the given position and offset.
	 *
	 * @param __p The position to read from.
	 * @param __o The relative offset.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds
	 * or the offset is negative.
	 * @since 2016/05/08
	 */
	public short readShort(int __p, int __o)
		throws IndexOutOfBoundsException
	{
		return readShort(__rel(__p, __o));
	}
	
	/**
	 * Reads an unsigned byte at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/04/27
	 */
	public int readUnsignedByte(int __p)
		throws IndexOutOfBoundsException
	{
		return ((int)readByte(__p)) & 0xFF;
	}
	
	/**
	 * Reads an unsigned byte at the given position and offset.
	 *
	 * @param __p The position to read from.
	 * @param __o The relative offset.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds
	 * or the offset is negative.
	 * @since 2016/05/08
	 */
	public int readUnsignedByte(int __p, int __o)
		throws IndexOutOfBoundsException
	{
		return readUnsignedByte(__rel(__p, __o));
	}
	
	/**
	 * Reads an unsigned int at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/04/27
	 */
	public long readUnsignedInt(int __p)
		throws IndexOutOfBoundsException
	{
		return ((long)readInt(__p)) & 0xFFFF_FFFF;
	}
	
	/**
	 * Reads an unsigned int at the given position and offset.
	 *
	 * @param __p The position to read from.
	 * @param __o The relative offset.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds
	 * or the offset is negative.
	 * @since 2016/05/08
	 */
	public long readUnsignedInt(int __p, int __o)
		throws IndexOutOfBoundsException
	{
		return readUnsignedInt(__rel(__p, __o));
	}
	
	/**
	 * Reads an unsigned short at the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/04/27
	 */
	public int readUnsignedShort(int __p)
		throws IndexOutOfBoundsException
	{
		return ((int)readShort(__p)) & 0xFFFF;
	}
	
	/**
	 * Reads an unsigned short at the given position and offset.
	 *
	 * @param __p The position to read from.
	 * @param __o The relative offset.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the position is not within bounds
	 * or the offset is negative.
	 * @since 2016/05/08
	 */
	public int readUnsignedShort(int __p, int __o)
		throws IndexOutOfBoundsException
	{
		return readUnsignedShort(__rel(__p, __o));
	}
	
	/**
	 * Creates a view of the current byte buffer.
	 *
	 * @param __s The relative start of the sub-window.
	 * @param __l The length of the sub-window.
	 * @return The sub-window view.
	 * @throws IndexOutOfBoundsException If the window region is not within
	 * bounds or any value is negative.
	 * @since 2016/04/28
	 */
	public CIByteBuffer window(int __s, int __l)
		throws IndexOutOfBoundsException
	{
		// No actual window
		int of = _offset;
		int ll = _length;
		if (__s == 0 && __l == ll)
			return this;
		
		// Out of bounds?
		int news = of + __s;
		if (__s < 0 || __l < 0 || __l > ll || ((news + __l) > (of + ll)))
			throw new IndexOutOfBoundsException("IOOB");
		
		// Setup window
		return new CIByteBuffer(_data, news, __l);
	}
	
	/**
	 * Checks the bounds of a given read and returns the offset to base reads
	 * from.
	 *
	 * @param __p The relative position from the base.
	 * @param __l The number of bytes intended to be read.
	 * @return The position to start a read from.
	 * @throws IndexOutOfBoundsException If the position is not within bounds.
	 * @since 2016/04/27
	 */
	private int __bounds(int __p, int __l)
		throws IndexOutOfBoundsException
	{
		int bo = _offset;
		int bl = _length;
		
		// Check bounds
		if (__p < 0 || (__p + __l) > bl)
			throw new IndexOutOfBoundsException(String.format("IOOB %d %d %d",
				__p, __l, bl));
		
		// Calculated base
		return __p + bo;
	}
	
	/**
	 * Calculates the relative offset from the base to read from.
	 *
	 * @param __p The base position to read from.
	 * @param __o The relative offset.
	 * @return The calculated position.
	 * @throws IndexOutOfBoundsException If the position or offset are
	 * negative.
	 * @since 2016/05/08
	 */
	private int __rel(int __p, int __o)
		throws IndexOutOfBoundsException
	{
		// Check
		if (__p < 0 || __o < 0)
			throw new IndexOutOfBoundsException(String.format("IOOB %d %d",
				__p, __o));
		
		// Calculate
		return __p + __o;
	}
}

