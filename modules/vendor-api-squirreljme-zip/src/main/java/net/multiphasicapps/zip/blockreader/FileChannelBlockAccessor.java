// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * This wraps a file channel and provides block level access to it.
 *
 * @since 2016/12/27
 */
@SuppressWarnings("DuplicateThrows")
public class FileChannelBlockAccessor
	implements BlockAccessor
{
	/** The file channel to wrap. */
	protected final FileChannel channel;
	
	/**
	 * Initializes the block accessor for the given path.
	 *
	 * @param __p The path to open.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/29
	 */
	public FileChannelBlockAccessor(Path __p)
		throws IOException, NullPointerException
	{
		this(FileChannel.open(__p, StandardOpenOption.READ));
	}
	
	/**
	 * Initializes the block accessor for the file channel.
	 *
	 * @param __fc The channel to access data from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	public FileChannelBlockAccessor(FileChannel __fc)
		throws IOException, NullPointerException
	{
		// Check
		if (__fc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.channel = __fc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public void close()
		throws IOException
	{
		this.channel.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/29
	 */
	@Override
	public byte read(long __addr)
		throws EOFException, IOException
	{
		/* {@squirreljme.error BF07 Cannot read from a negative offset.} */
		if (__addr < 0)
			throw new IOException("BF07");
		
		// Just forward to the array variant
		byte[] val = new byte[1];
		int rv = this.read(__addr, val, 0, 1);
		
		/* {@squirreljme.error BF08 Read past end of file.} */
		if (rv < 0)
			throw new EOFException("BF08");
		
		return val[0];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public int read(long __addr, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		/* {@squirreljme.error BF09 Cannot read from a negative offset.} */
		if (__addr < 0)
			throw new IOException("BF09");
		
		// Read until every byte has been read so that partial reads are not
		// returned
		ByteBuffer buf = ByteBuffer.wrap(__b, __o, __l);
		FileChannel channel = this.channel;
		int n;
		while (buf.hasRemaining())
			if (channel.read(buf, __addr + buf.position()) < 0)
				if ((n = buf.position()) <= 0)
					return -1;
				else
					return n;
		
		// Use the read position
		return buf.position();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public long size()
		throws IOException
	{
		return this.channel.size();
	}
}

