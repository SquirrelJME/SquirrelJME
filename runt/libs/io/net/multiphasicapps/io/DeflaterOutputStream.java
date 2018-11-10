// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is used to compress to standard deflate streams.
 *
 * Associated standards:
 * {@link https://www.ietf.org/rfc/rfc1951.txt}.
 *
 * This class is not thread safe.
 *
 * @since 2018/11/10
 */
public class DeflaterOutputStream
	extends OutputStream
	implements CompressionStream
{
	/** Stream to write compressed data to. */
	protected final OutputStream out;
	
	/** Has this been closed? */
	private boolean _closed;
	
	/**
	 * Initializes the deflation stream.
	 *
	 * @param __os The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public DeflaterOutputStream(OutputStream __os)
		throws NullPointerException
	{
		this(__os, CompressionLevel.DEFAULT);
	}
	
	/**
	 * Initializes the deflation stream.
	 *
	 * @param __os The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public DeflaterOutputStream(OutputStream __os, CompressionLevel __cl)
		throws NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		this.out = __os;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final void close()
		throws IOException
	{
		// Only close once!
		if (!this._closed)
		{
			// Is closed
			this._closed = true;
			
			// Make sure the output is flushed first
			this.flush();
			
			// Close it out
			throw new todo.TODO();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final long compressedBytes()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final long uncompressedBytes()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		// Just forward write call since it is easier
		this.write(new byte[]{(byte)__b}, 0, 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		throw new todo.TODO();
	}
}

