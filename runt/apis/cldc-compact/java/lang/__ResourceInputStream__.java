// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.InputStream;
import cc.squirreljme.runtime.cldc.asm.ResourceAccess;

/**
 * This input stream handles reading of resources.
 *
 * @since 2018/10/07
 */
final class __ResourceInputStream__
	extends InputStream
{
	/** The file descriptor. */
	protected final int fd;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the resource input stream.
	 *
	 * @param __fd The file descriptor.
	 * @since 2018/10/07
	 */
	__ResourceInputStream__(int __fd)
	{
		this.fd = __fd;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	public final int available()
	{
		int rv = ResourceAccess.available(this.fd);
		if (rv < 0)
			return 0;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	public final void close()
	{
		// Prevent double close of resources because the values may be
		// recycled
		if (!this._closed)
		{
			this._closed = true;
			ResourceAccess.close(this.fd);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	public final int read()
	{
		byte[] rv = new byte[1];
		for (;;)
		{
			int code = this.read(rv, 0, 1);
			
			// Try again
			if (code == 0)
				continue;
			
			// EOF
			else if (code < 0)
				return -1;
			
			// Snip bits off
			return (rv[0] & 0xFF);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	public final int read(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return this.read(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	public final int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// If the stream has been closed it is probably not valid so always
		// return EOF
		if (this._closed)
			return -1;
		
		// Read from stream
		int rv = ResourceAccess.read(this.fd, __b, __o, __l);
		
		// If the end of stream was reached, so just close this stream so
		// that resources are free. Since resources always exist it is very
		// possible that they might not be closed because they are seen as
		// a free resource that has no implications if they are never closed.
		if (rv < 0)
		{
			this.close();
			return -1;
		}
		
		// Will be the number of read bytes
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	public final long skip(long __n)
	{
		// Nothing to skip
		if (__n <= 0)
			return 0;
		
		// Counting total
		long total = 0;
		
		// Still bytes to be skipped?
		int fd = this.fd;
		while (__n > 0)
		{
			// This method uses long but resource input stream uses integers
			// for simplicity, so just skip in max value chunks
			int count = (int)Math.min(Integer.MAX_VALUE, __n);
			
			// Skip these bytes
			int skipped = ResourceAccess.skip(fd, count);
			
			// No bytes were skipped, just stop trying to skip then
			if (skipped <= 0)
				break;
			
			// Add to skip count
			total += skipped;
		}
		
		// This might not match the input
		return total;
	}
}

