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

import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This wraps a stream processor so that any data written to the output stream
 * is ran through the processor and sent to the output stream.
 *
 * @since 2016/12/20
 */
public final class StreamProcessorOutputStream
	extends OutputStream
	implements Flushable
{
	/** Output stream to write to. */
	protected final OutputStream out;
	
	/** The stream processor used. */
	protected final StreamProcessor processor;
	
	/**
	 * Initializes the output stream processor.
	 *
	 * @param __os The output stream to write data to.
	 * @param __sp The stream processor used to process the data.
	 * @since 2016/12/20
	 */
	public StreamProcessorOutputStream(OutputStream __os, StreamProcessor __sp)
		throws NullPointerException
	{
		// Check
		if (__os == null || __sp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.out = __os;
		this.processor = __sp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public void flush()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int bl = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bl)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		throw new Error("TODO");
	}
}

