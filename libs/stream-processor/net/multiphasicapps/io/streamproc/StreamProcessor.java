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

import java.io.Closeable;
import java.io.IOException;

/**
 * This is the base class for all stream processor which are used to input
 * and output data.
 *
 * This class is not thread safe.
 *
 * @since 2016/12/20
 */
public abstract class StreamProcessor
	implements Closeable
{
	/**
	 * Returns the number of output bytes which are available for usage.
	 *
	 * @return The number of output bytes available, may be zero.
	 * @since 2017/02/24
	 */
	public final int available()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Attempts to fill the entire destination buffer with bytes which have
	 * been processed by this processor.
	 *
	 * @param __i The stream to read bytes from for output.
	 * @param __b The destination array.
	 * @param __o The offset into the target.
	 * @param __l The maximum number of bytes to process.
	 * @return The number of processed bytes or a negative value if the 
	 * stream has terminated.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IOException On processing errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/24
	 */
	public final int process(StreamInput __i, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Check
		if (__i == null || __b == null)
			throw new NullPointerException("NARG");
		int bl = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bl)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		throw new Error("TODO");
	}
}

