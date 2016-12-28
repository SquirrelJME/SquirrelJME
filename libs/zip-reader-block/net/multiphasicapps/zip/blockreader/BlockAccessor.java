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

import java.io.Closeable;
import java.io.IOException;

/**
 * This interface is used to access the data in the ZIP file.
 *
 * @since 2016/12/27
 */
public interface BlockAccessor
	extends Closeable
{
	/**
	 * Reads data from the block and writes to the destination array.
	 *
	 * @param __addr The address to start reading from.
	 * @param __b The destination array to write values to.
	 * @param __o The offset into the array.
	 * @param __l The maximum number of bytes to read.
	 * @return The number of bytes read or a negative value if the address
	 * exceeds the bounds of the block.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceeds the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	public abstract int read(long __addr, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException;
	
	/**
	 * Returns the number of bytes which are available for reading.
	 *
	 * @return The number of bytes in the block.
	 * @throws IOException If it could not be determined.
	 * @since 2016/12/27
	 */
	public abstract long size()
		throws IOException;
}

