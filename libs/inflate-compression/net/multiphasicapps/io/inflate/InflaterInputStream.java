// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.inflate;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.slidingwindow.SlidingByteWindow;
import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This is used to decompress standard deflate compressed stream.
 *
 * @since 2017/02/24
 */
public class InflaterInputStream
	extends InputStream
{
	/** The size of the sliding window. */
	protected static final int SLIDING_WINDOW_SIZE =
		32768;
	
	/** The deflated compressed stream to be decompressed. */
	protected final InputStream in;
	
	/** Sliding window for accessing old bytes. */
	protected final SlidingByteWindow window =
		new SlidingByteWindow(SLIDING_WINDOW_SIZE);
	
	/** If the output cannot be filled, bytes are written here instead. */
	protected final ByteDeque overflow =
		new ByteDeque();
	
	/** Single byte read. */
	private final byte[] _solo =
		new byte[1];
	
	/** EOF has been reached? */
	private volatile boolean _eof;
	
	/**
	 * Initializes the deflate compression stream inflater.
	 *
	 * @param __in The stream to inflate.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/24
	 */
	public InflaterInputStream(InputStream __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/24
	 */
	@Override
	public int available()
		throws IOException
	{
		// Use the number of bytes that are able to be read quickly without
		// requiring decompression
		return this.overflow.available();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/24
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close input
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/24
	 */
	@Override
	public int read()
		throws IOException
	{
		// Try reading a single byte
		byte[] solo = this._solo;
		for (;;)
		{
			int rv = read(solo, 0, 1);
			
			// EOF?
			if (rv < 0)
				return rv;
			
			// Try again
			else if (rv == 0)
				continue;
			
			// Return that byte
			else
				return (solo[0] & 0xFF);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/24
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int bl = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bl)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// If there are bytes in the overflow buffer, read them first into the
		// output because they are the result of previous decompression.
		ByteDeque overflow = this.overflow;
		int ovn = overflow.available(),
			ovr = (ovn < __l ? ovn : __l);
		int c = overflow.removeFirst(__b, __o, __l);
		
		// End of stream reached
		if (this._eof)
		{
			// Never return EOF if no bytes were read and bytes were available
			// even when EOF has been triggered.
			if (c > 0 || overflow.available() > 0)
				return c;
			
			// Otherwise EOF
			return -1;
		}
		
		// Only read overflow bytes? Do not bother decompressing more data
		// because it will just be added to the queue
		if (c >= __l)
			return c;
		
		// Since overflow bytes were read, adjust the output and length
		// accordingly
		__o += c;
		__l -= c;
		
		if (true)
			throw new Error("TODO");
		
		// Return the read count
		return c;
	}
}

