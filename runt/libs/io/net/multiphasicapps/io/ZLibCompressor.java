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
 * This class supports compressing data to ZLib streams.
 *
 * Associated standards:
 * {@link https://www.ietf.org/rfc/rfc1950.txt}.
 *
 * This class is not thread safe.
 *
 * @since 2018/11/11
 */
public final class ZLibCompressor
	extends OutputStream
	implements CompressionStream
{
	/** The stream to forward to. */
	protected final OutputStream out;
	
	/**
	 * Initializes the compressor.
	 *
	 * @param __os The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public ZLibCompressor(OutputStream __os)
		throws NullPointerException
	{
		this(__os, CompressionLevel.DEFAULT);
	}
	
	/**
	 * Initializes the compressor with the given compression level.
	 *
	 * @param __os The stream to write to.
	 * @param __cl The compression level to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public ZLibCompressor(OutputStream __os, CompressionLevel __cl)
		throws NullPointerException
	{
		if (__os == null || __cl == null)
			throw new NullPointerException("NARG");
		
		this.out = __os;
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final long compressedBytes()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final void close()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final void flush()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final long uncompressedBytes()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
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
	 * @since 2018/11/11
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

