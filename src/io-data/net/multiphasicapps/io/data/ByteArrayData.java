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
}

