// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.compressionstream;

import java.io.IOException;

/**
 * This interface is used to describe a compression stream which is used as
 * input.
 *
 * @since 2017/08/22
 */
public interface CompressionInputStream
	extends CompressionStream
{
	/**
	 * Returns the number of bytes which are quickly available for reading.
	 *
	 * @throws IOException If it cannot be determined.
	 * @since 2017/08/22
	 */
	public abstract int available()
		throws IOException;
	
	/**
	 * Reads a single byte from the input.
	 *
	 * @return The read value byte in the range of {@code 0-255} or on EOF
	 * {@code -1} is returned.
	 * @throws IOException On read errors.
	 * @since 2017/08/22
	 */
	public abstract int read()
		throws IOException;
	
	/**
	 * Reads multiple bytes into the given array.
	 *
	 * @param __b The array to read into.
	 * @return The number of read bytes.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On arguments.
	 * @since 2017/08/22
	 */
	public abstract int read(byte[] __b)
		throws IOException, NullPointerException;
	
	/**
	 * Reads multiple bytes into the given array at the given offset.
	 *
	 * @param __b The array to read into.
	 * @param __o The offset into the array.
	 * @param __l The maximum number of bytes to read.
	 * @return The number of read bytes.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On arguments.
	 * @since 2017/08/22
	 */
	public abstract int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException;
}

