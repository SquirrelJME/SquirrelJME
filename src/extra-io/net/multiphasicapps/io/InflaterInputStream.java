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
import java.nio.ByteBuffer;

/**
 * This input stream reads deflated input (using the deflate algorithm) and
 * decompresses it to provide the original data.
 *
 * @since 2016/03/09
 */
public class InflaterInputStream
	extends InputStream
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The wrapped bit stream. */
	protected final BitInputStream in;
	
	/** Pending bytes. */
	private volatile ByteBuffer _pending;
	
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
			System.err.printf("Finish: %s%n", in.read());
			System.err.printf("Type  : %s%n", in.readBits(2));
			
			
			throw new Error("TODO");
		}
	}
}

