// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.seekablearray;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * This is a class which is given a byte array and allows data to be read.
 *
 * @since 2016/07/14
 */
public class SeekableByteArrayChannel
	implements SeekableByteChannel
{
	/** The array to wrap. */
	protected final byte[] array;
	
	/** The size of the array. */
	protected final long size;
	
	/** The current position. */
	private volatile long _pos;	
	
	/**
	 * Initializes the seekable byte array channel using the given byte array.
	 *
	 * @param __d The byte array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/14
	 */
	public SeekableByteArrayChannel(byte... __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.array = __d;
		this.size = __d.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public void close()
	{
		// Has no effect
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public boolean isOpen()
	{
		// Always open
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public long position()
	{
		return this._pos;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public SeekableByteChannel position(long __p)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BE02 The position cannot be negative. (The
		// position which was requested to be used)}
		if (__p < 0)
			throw new IllegalArgumentException(String.format("BE02 %d", __p));
		
		// Set and return
		this._pos = __p;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public int read(ByteBuffer __dest)
		throws NullPointerException
	{
		// Check
		if (__dest == null)
			throw new NullPointerException("NARG");
		
		// The current position to read from
		long position = this._pos;
		long size = this.size;
		byte[] array = this.array;
		
		// Read in bytes
		int off = 0;
		boolean eof = false;
		while (__dest.hasRemaining())
		{
			// Calculate actual position
			long act = position + off;
			if (act < 0 || act >= size)
			{
				eof = true;
				break;
			}
			
			// Read in byte
			__dest.put(array[(int)act]);
			off++;
		}
		
		// Set new position
		this._pos = position + off;
		
		// Return number of bytes read or EOF
		return (eof && off == 0 ? -1 : off);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public long size()
	{
		return this.size;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public SeekableByteChannel truncate(long __s)
		throws IOException
	{
		// Ignore same size
		if (__s == size())
			return this;
		
		// {@squirreljme.error BE01 This channel cannot be truncated.}
		throw new IOException("BE01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public int write(ByteBuffer __src)
		throws NullPointerException
	{
		// Check
		if (__src == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

