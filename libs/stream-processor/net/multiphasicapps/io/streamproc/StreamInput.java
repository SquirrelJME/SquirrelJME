// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.streamproc;

import java.io.IOException;

/**
 * This is used as a stream input for when bytes need to be read.
 *
 * @since 2017/02/24
 */
public interface StreamInput
{
	/**
	 * Reads the specified number of bytes from the source of the stream into
	 * the specified array.
	 *
	 * @param __b The destination array.
	 * @param __o The offset into the target.
	 * @param __l The maximum number of bytes to read.
	 * @return The number of read bytes or a negative value if the 
	 * stream has terminated.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/24
	 */
	public abstract int read(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException;
}

