// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is an input stream in which the size of the input stream is maximally
 * bound to a given size or fixed to a specific size.
 *
 * This class is not thread safe.
 *
 * @since 2016/03/09
 */
public class SizeLimitedInputStream
	extends InputStream
{
	/** The wrapped stream. */
	protected final InputStream wrapped;
	
	/** Exact size? */
	protected final boolean exact;
	
	/** The read limit. */
	protected final long limit;
	
	/** If {@code true} then close propogates to the wrapped stream. */
	protected final boolean propogate;
	
	/** The current read size. */
	private volatile long _current;
	
	/** Was this closed? */	
	private volatile boolean _closed;
	
	/**
	 * Initializes the size limited input stream. The close operation in this
	 * stream propogates to the wrapped stream.
	 *
	 * @param __is Input stream to wrap.
	 * @param __li The length of data to limit to.
	 * @param __ex If {@code true} then the stream must end at least at the
	 * limit and not before it, otherwise if {@code false} then it is only
	 * limited to either the limit or the number of bytes in the stream. If
	 * {@code true} then closing the stream will read the remaining number of
	 * bytes.
	 * @throws IllegalArgumentException If the length is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/09
	 */
	public SizeLimitedInputStream(InputStream __is, long __li, boolean __ex)
		throws IllegalArgumentException, NullPointerException
	{
		this(__is, __li, __ex, true);
	}
	
	/**
	 * Initializes the size limited input stream.
	 *
	 * @param __is Input stream to wrap.
	 * @param __li The length of data to limit to.
	 * @param __ex If {@code true} then the stream must end at least at the
	 * limit and not before it, otherwise if {@code false} then it is only
	 * limited to either the limit or the number of bytes in the stream. If
	 * {@code true} then closing the stream will read the remaining number of
	 * bytes.
	 * @param __prop If {@code true} then when this stream is closed, it will
	 * forward the close to the wrapped stream. Otherwise if {@code false}.
	 * @throws IllegalArgumentException If the length is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/28
	 */
	public SizeLimitedInputStream(InputStream __is, long __li, boolean __ex,
		boolean __prop)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BD1n The limit is negative. (The negative
		// limit)}
		if (__li < 0)
			throw new IllegalArgumentException(String.format("BD1n %d", __li));
		
		// Set
		this.wrapped = __is;
		this.limit = __li;
		this.exact = __ex;
		this.propogate = __prop;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public int available()
		throws IOException
	{
		// Get the count for the wrapped stream
		long wav = wrapped.available();
	
		// Either limited to the max integer size, the number of available
		// bytes, or the remaining stream count.
		return (int)Math.min(Integer.MAX_VALUE,
			Math.min(wav, (limit - _current)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public void close()
		throws IOException
	{
		// Read in?
		if (!this._closed)
		{
			// Set
			this._closed = true;
			
			// Read in remaining bytes if doing so
			if (this.exact)
			{
				// {@squirreljme.error BD1o Reached EOF on wrapped stream when
				// requesting an exact number of bytes. (The current read
				// count; The read limit)}
				long limit = this.limit;
				long at;
				while ((at = this._current) < limit)
					if (read() < 0)
						throw new IOException(String.format("BD1o %d %d",
							at, limit));
			}
		}
		
		// Close the underlying stream, but only if propogating
		if (this.propogate)
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
		// Current position
		long limit = this.limit;
		long cur = this._current;
		
		// Reached the limit?
		if (cur >= limit)
			return -1;
		
		// Read next byte
		int next = wrapped.read();
		
		// EOF?
		if (next < 0)
		{
			// {@squirreljme.error BD1p Required an exact number of bytes
			// however the limit was not yet reached, the input stream is too
			// short. (The limit; The current position)}
			if (exact && cur != limit)
				throw new IOException(String.format("BD1p %d %d",
					limit, cur));
			
			// Return original negative
			return next;
		}
		
		// Increase current location
		this._current = cur + 1L;
		
		// Return it
		return next;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/05
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// If the limit was reached, stop
		long current = this._current;
		long limit = this.limit;
		if (current >= limit)
		{
			// {@squirreljme.error BD1q Required an exact number of bytes
			// however the limit was not yet reached, the file is too short.
			// (The limit; The current position)}
			if (exact && current != limit)
				throw new IOException(String.format("BD1q %d %d",
					limit, current));
			
			return -1;
		}
		
		// Do not read more bytes after the limit
		int cc = (int)Math.min(limit - current, __l);
		
		// Read the next few bytes
		InputStream wrapped = this.wrapped;
		int rc = wrapped.read(__b, __o, cc);
		
		// EOF?
		if (rc < 0)
		{
			// {@squirreljme.error BD1r Required an exact number of bytes
			// however the limit was not yet reached. (The limit; The
			// current position)}
			if (exact && current != limit)
				throw new IOException(String.format("BD1r %d %d",
					limit, current));
			
			// Just EOF
			return -1;
		}
		
		// Set the new current
		this._current = current + rc;
		
		// Return the read count
		return rc;
	}
}

