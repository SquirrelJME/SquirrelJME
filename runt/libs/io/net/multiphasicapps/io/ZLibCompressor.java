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
	
	/** The deflater used. */
	private final DeflaterOutputStream _dos;
	
	/** Adler checksum of uncompressed stream. */
	private final Adler32Calculator _adler =
		new Adler32Calculator();
	
	/** Has this been closed? */
	private boolean _closed;
	
	/** Initialized with Zlib header? */
	private boolean _init;
	
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
		this._dos = new DeflaterOutputStream(__os);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final long compressedBytes()
	{
		return this._dos.compressedBytes() +
			(this._init ? 2 + (this._closed ? 4 : 0) : 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final void close()
		throws IOException
	{
		// Close only once
		if (!this._closed)
		{
			this._closed = true;
			
			// Flush and close the other compression stream
			DeflaterOutputStream dos = this._dos;
			dos.flush();
			dos.close();
			
			// Write the checksum
			OutputStream out = this.out;
			int checksum = this._adler.checksum();
			out.write(checksum >> 24);
			out.write(checksum >> 16);
			out.write(checksum >> 8);
			out.write(checksum);
			
			// Flush the output
			out.flush();
		}
		
		// Forward close to output
		this.out.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final void flush()
		throws IOException
	{
		// Flush to the output
		this.out.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public final long uncompressedBytes()
	{
		return this._dos.uncompressedBytes();
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
		
		OutputStream out = this.out;
		
		// If the stream has not been initialized, we need to set a flag
		// and such
		if (!this._init)
		{
			this._init = true;
			
			// DEFLATE compression with no flags
			out.write(0x78);
			out.write(1);
		}
		
		// Write to data
		this._dos.write(__b, __o, __l);
		
		// Checksum that
		this._adler.offer(__b, __o, __l);
	}
}

