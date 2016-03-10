// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.InputStream;
import java.io.IOException;

/**
 * This input stream reads deflated input (using the deflate algorithm) and
 * decompresses it to provide the original data.
 *
 * This follows RFC 1951.
 *
 * @since 2016/03/09
 */
public class InflaterInputStream
	extends InputStream
{
	/** The size of the sliding window. */
	public static final int SLIDING_WINDOW_SIZE =
		32768;
	
	/** No compression. */
	protected static final int TYPE_NO_COMPRESSION =
		0b00;
	
	/** Fixed huffman table compression. */
	protected static final int TYPE_FIXED_HUFFMAN =
		0b01;
	
	/** Dynamic huffman table compression. */
	protected static final int TYPE_DYNAMIC_HUFFMAN =
		0b10;
	
	/** An error. */
	protected static final int TYPE_ERROR =
		0b11;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The wrapped bit stream. */
	protected final BitInputStream in;
	
	/** The sliding window where historical bytes may be referenced. */
	protected final SlidingByteWindow slidingwindow =
		new SlidingByteWindow(SLIDING_WINDOW_SIZE);
	
	/** Finished reading? */
	private volatile boolean _finished;
	
	/**
	 * This initializes the input stream which is used to inflate deflated
	 * data.
	 *
	 * @param __w The input deflated stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/09
	 */
	public InflaterInputStream(InputStream __w)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException();
		
		// Set
		in = new BitInputStream(__w, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public int read()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// There might not be enough data for an output read to occur.
			for (;;)
			{
				// If finished, then stop
				if (_finished)
					return -1;
			
				// Is this the final block?
				boolean isfinal = in.read();
			
				// Mark finished?
				_finished |= isfinal;
			
				// Read the compression type
				int type = (int)in.readBits(2);
			
				// No compression
				if (type == TYPE_NO_COMPRESSION)
					throw new Error("TODO");
			
				// Huffman compressed
				else if (type == TYPE_FIXED_HUFFMAN ||
					type == TYPE_DYNAMIC_HUFFMAN)
				{
					// Load in dynamic huffman table?
					if (type == TYPE_DYNAMIC_HUFFMAN)
						throw new Error("TODO");
				
					// Use the fixed one
					else
						throw new Error("TODO");
				}
			
				// Unknown or error
				else
					throw new InflaterException.HeaderErrorTypeException();
			}
		}
	}
}

