// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.archive;

import java.io.Closeable;
import java.io.IOException;

/**
 * Represents a single entry within an archive.
 *
 * @since 2022/08/20
 */
public interface ArchiveStreamEntry
	extends Closeable
{
	/**
	 * Returns the name of the entry.
	 *
	 * @return The entry name.
	 * @since 2022/08/20
	 */
	String name();
		
	/**
	 * Reads a single byte from the input stream.
	 *
	 * @return The read unsigned byte value ({@code 0-255}) or {@code -1} if
	 * the end of stream has been reached.
	 * @throws IOException On read errors.
	 * @since 2022/08/20
	 */
	int read()
		throws IOException;
		
	/**
	 * Reads multiple bytes and stores them into the array.
	 *
	 * @param __b The destination array.
	 * @return The number of bytes read or {@code -1} if the end of stream has
	 * been reached.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/20
	 */
	int read(byte[] __b)
		throws IOException, NullPointerException;
	
	/**
	 * Reads multiple bytes and stores them into the array.
	 *
	 * @param __b The destination array.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to read.
	 * @return The number of bytes read or {@code -1} if the end of stream has
	 * been reached.
	 * @throws IndexOutOfBoundsException If the offset and/or length 
	 * negative or exceed the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/20
	 */
	int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException;
}
