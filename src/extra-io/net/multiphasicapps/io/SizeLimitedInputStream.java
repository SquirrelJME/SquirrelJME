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
 * This is an input stream in which the size of the input stream is maximally
 * bound to a given size or fixed to a specific size.
 *
 * @since 2016/03/09
 */
public class SizeLimitedInputStream
	extends InputStream
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The wrapped stream. */
	protected final InputStream wrapped;
	
	/** Exact size? */
	protected final boolean exact;
	
	/** The read limit. */
	protected final long limit;
	
	/** The current read size. */
	private volatile long _current;	
	
	/**
	 * Initializes the size limited input stream with a maximal bound.
	 *
	 * @param __is Input stream to wrap.
	 * @param __li The length of data to limit to.
	 * @throws IllegalArgumentException If the length is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/09
	 */
	public SizeLimitedInputStream(InputStream __is, long __li)
		throws IllegalArgumentException, NullPointerException
	{
		this(__is, __li, false);
	}
	
	/**
	 * Initializes the size limited input stream.
	 *
	 * @param __is Input stream to wrap.
	 * @param __li The length of data to limit to.
	 * @param __ex If {@code true} then the stream must end at least at the
	 * limit and not before it, otherwise if {@code false} then it is only
	 * limited to either the limit or the number of bytes in the stream.
	 * @throws IllegalArgumentException If the length is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/09
	 */
	public SizeLimitedInputStream(InputStream __is, long __li, boolean __ex)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException();
		if (__li < 0)
			throw new IllegalArgumentException();
		
		// Set
		wrapped = __is;
		limit = __li;
		exact = __ex;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public int available()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// Get the count for the wrapped stream
			long wav = wrapped.available();
		
			// Either limited to the max integer size, the number of available
			// bytes, or the remaining stream count.
			return (int)Math.min(Integer.MAX_VALUE,
				Math.min(wav, (limit - _current)));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the underlying stream
		wrapped.close();
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
			// Current position
			long cur = _current;
			
			// Reached the limit?
			if (cur >= limit)
				return -1;
			
			// Read next byte
			int next = wrapped.read();
			
			// EOF?
			if (next < 0)
			{
				// No short read?
				if (exact && cur != limit)
					throw new IOException("Expected " + limit + " bytes, " +
						"however only " + cur + " were read.");
				
				// Return original negative
				return next;
			}
			
			// Increase current location
			_current = cur + 1L;
			
			// Return it
			return next;
		}
	}
}

