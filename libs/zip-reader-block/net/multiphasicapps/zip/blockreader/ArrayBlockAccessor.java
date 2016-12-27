// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

/**
 * This wraps an array and provides it to the block accessor interface so that
 * ZIP files may be read from arrays.
 *
 * @since 2016/12/27
 */
public class ArrayBlockAccessor
	implements BlockAccessor
{
	/**
	 * Initializes the block accessor which uses the entire array.
	 *
	 * @param __b The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	public ArrayBlockAccessor(byte[] __b)
		throws NullPointerException
	{
		this(__b, 0, __b.length);
	}
	
	/**
	 * Initializes the block accessor which uses the entire array.
	 *
	 * @param __b The array to wrap.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to make available.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	public ArrayBlockAccessor(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		throw new Error("TODO");
	}
}

