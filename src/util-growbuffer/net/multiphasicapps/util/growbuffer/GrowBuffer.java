// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.growbuffer;

/**
 * This class provides a buffer which is capable of being grown or shrunk on
 * a single end.
 *
 * @since 2016/09/30
 */
public class GrowBuffer
{
	/**
	 * Returns the byte at the specified index.
	 *
	 * @param __i The index to get.
	 * @return The byte at this index.
	 * @throws ArrayIndexOutOfBoundsException If the index is not within the
	 * buffer bounds.
	 * @since 2016/09/30
	 */
	public byte get(int __i)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Places a byte at the specified index and returns the old byte.
	 *
	 * @param __i The index to place the byte at.
	 * @param __v The value to set.
	 * @return The value that was previously set at this position.
	 * @throws ArrayIndexOutOfBoundsException If the index is not within the
	 * buffer bounds.
	 * @since 2016/09/30
	 */
	public byte put(int __i, byte __v)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the current size of the buffer.
	 *
	 * @return The buffer size.
	 * @since 2016/09/30
	 */
	public int size()
	{
		throw new Error("TODO");
	}
}

