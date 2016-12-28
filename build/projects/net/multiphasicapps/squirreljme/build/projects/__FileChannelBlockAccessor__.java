// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import net.multiphasicapps.zip.blockreader.BlockAccessor;

/**
 * This wraps a file channel and provides block level access to it.
 *
 * @since 2016/12/27
 */
class __FileChannelBlockAccessor__
	implements BlockAccessor
{
	/** The file channel to wrap. */
	protected final FileChannel channel;
	
	/**
	 * Initializes the block accessor for the file channel.
	 *
	 * @param __fc The channel to access data from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/27
	 */
	__FileChannelBlockAccessor__(FileChannel __fc)
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
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// {@squirreljme.error CJ01 Cannot read from a negative offset.}
		if (__addr < 0)
			throw new IOException("CJ01");
		
		// Read until every byte has been read so that partial reads are not
		// returned
		ByteBuffer buf = ByteBuffer.wrap(__b, __o, __l);
		FileChannel channel = this.channel;
		while (buf.hasRemaining())
		{
			channel.read(buf, __addr + buf.position());
		}
		
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

