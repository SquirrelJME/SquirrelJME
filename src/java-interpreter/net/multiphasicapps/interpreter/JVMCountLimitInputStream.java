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
public class JVMCountLimitInputStream
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
	
	/**
	 * Initializes the counter and limiter of an input stream which is used to
	 * splice a portion of a stream.
	 *
	 * @param __w The base stream to wrap.
	 * @param __lim The number of bytes to limit the stream to.
	 * @throws IllegalArgumentException If the limit is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/20
	 */
	public JVMCountLimitInputStream(InputStream __w, long __lim)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException("NARG");
		if (__lim < 0L)
			throw new IllegalArgumentException(String.format("IN1i %d",
				__lim));
		
		// Set
		in = __w;
		limit = __lim;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/23
	 */
	@Override
	public int available()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			return (int)Math.min((long)Integer.MAX_VALUE, Math.max(0L,
				Math.min(remaining(), in.available())));
		}
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
	 * Returns the number of bytes which were read by this stream.
	 *
	 * @return The number of read bytes.
	 * @since 2016/03/23
	 */
	public long count()
	{
		// Lock
		synchronized (lock)
		{
			return _count;
		}
	}
	
	/**
	 * Returns {@code true} if the limit has not been reached yet.
	 *
	 * @return {@code true} if the limit was not reached.
	 * @since 2016/03/23
	 */
	public boolean hasRemaining()
	{
		// Lock
		synchronized (lock)
		{
			return remaining() > 0L;
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
	
	/**
	 * Returns the number of bytes remaining in the stream.
	 *
	 * @return The remaining byte count.
	 * @since 2016/03/23
	 */
	public long remaining()
	{
		// Lock
		synchronized (lock)
		{
			return limit - _count;
		}
	}
}

