// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.zlibdecompression;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.inflate.InflaterInputStream;

/**
 * This class supports decompressing ZLib streams.
 *
 * Associated standards:
 * {@link https://www.ietf.org/rfc/rfc1950.txt}.
 *
 * This class is not thread safe.
 *
 * @since 2017/03/04
 */
public class ZLibDecompressor
	extends InputStream
{
	/** The source stream. */
	protected final InputStream in;
	
	/** Single byte array to be shared for single reads. */
	private final byte[] _solo =
		new byte[1];
	
	/** Current stream to read data from, will change for blocks. */
	private volatile InputStream _current;
	
	/** Has EOF been read? */
	private volatile boolean _eof;
	
	/**
	 * Initializes the ZLib decompressor.
	 *
	 * @param __in The stream to read data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/04
	 */
	public ZLibDecompressor(InputStream __in)
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
	 * @since 2017/03/04
	 */
	@Override
	public int available()
		throws IOException
	{
		// If the current stream is known, it is possible that the number
		// of available bytes will be known from it
		InputStream current = this._current;
		if (current != null)
			return current.available();
		
		// Otherwise no amount is known
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/04
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
		
		// Close the current stream also
		InputStream current = this._current;
		if (current != null)
			current.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/04
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
	 * @since 2017/03/04
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
		
		// Read EOF?
		if (this._eof)
			return -1;
		
		// Try to fill the buffer up as much as possible
		int rv = 0;
		boolean eof = false;
		InputStream current = this._current;
		while (rv < __l)
		{
			throw new todo.TODO();
		}
		
		// Return EOF or the read count
		return (eof && rv == 0 ? -1 : rv);
	}
}

