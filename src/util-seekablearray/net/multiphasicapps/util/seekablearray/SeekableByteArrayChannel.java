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
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public SeekableByteChannel position(long __p)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public int read(ByteBuffer __dest)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/14
	 */
	@Override
	public long size()
	{
		throw new Error("TODO");
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
	{
		throw new Error("TODO");
	}
}

