// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is an input stream where a specific number of bytes are read, if it
 * is too short then failure occurs.
 *
 * If this stream is closed, then the underlying stream is consumed to the
 * limit.
 *
 * @since 2016/03/20
 */
class __ChunkedInputStream__
	extends InputStream
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Source data. */
	protected final InputStream in;
	
	/** Data read limit. */
	protected final long limit;
	
	/** Current read count. */
	private volatile long _count;
	
	__ChunkedInputStream__(InputStream __w, long __lim)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// Set
		in = __w;
		limit = __lim;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public void close()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// Reach the limit
			for (;;)
			{
				// Get count
				long cc = _count;
				
				// At the limit? then stop
				if (cc >= limit)
					break;
				
				// Otherwise skip a byte, fail on EOF
				if (in.read() < 0)
					throw new IOException("IN1e");
				
				// Set next position
				_count = cc + 1;
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public int read()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// Current position
			long cur = _count;
			
			// At the end?
			if (cur >= limit)
				return -1;
			
			// Otherwise read value
			int rv = in.read();
			
			// Bad EOF?
			if (rv < 0)
				throw new IOException("IN1e");
			
			// Increase index
			_count = cur + 1;
			
			// Return it
			return rv;
		}
	}
}

