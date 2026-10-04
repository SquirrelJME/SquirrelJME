// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.Closeable;
import java.io.EOFException;
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
	 * Reads a single byte.
	 *
	 * @param __addr The address to read from.
	 * @return The read byte.
	 * @throws EOFException If the read is past the end of file.
	 * @throws IOException On read/write errors.
	 * @since 2016/12/29
	 */
	byte read(long __addr)
		throws EOFException, IOException;
	
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
	int read(long __addr, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException;
	
	/**
	 * Returns the number of bytes which are available for reading.
	 *
	 * @return The number of bytes in the block.
	 * @throws IOException If it could not be determined.
	 * @since 2016/12/27
	 */
	long size()
		throws IOException;
}

