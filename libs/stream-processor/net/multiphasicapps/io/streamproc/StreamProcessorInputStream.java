// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.streamproc;

import java.io.InputStream;
import java.io.IOException;

/**
 * This wraps an input stream and for any bytes read from the input it returns
 * as read data.
 *
 * This class is not thread safe.
 *
 * @since 2016/12/20
 */
public class StreamProcessorInputStream
	extends InputStream
{
	/** Stream to read data from. */
	protected final InputStream in;
	
	/** The stream processor used. */
	protected final StreamProcessor processor;
	
	/** Single byte read. */
	private final byte[] _solo =
		new byte[1];
	
	/**
	 * Initializes the input stream processor.
	 *
	 * @param __in Where to read data from.
	 * @param __out The processor used to process the data with.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/20
	 */
	public StreamProcessorInputStream(InputStream __in, StreamProcessor __sp)
		throws NullPointerException
	{
		// Check
		if (__in == null || __sp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
		this.processor = __sp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public int available()
		throws IOException
	{
		return this.processor.available();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the input stream
		IOException fail = null;
		try
		{
			this.in.close();
		}
		
		// {@squirreljme.error CK01 Failed to close the input stream.}
		catch (Throwable e)
		{
			if (fail != null)
				fail = new IOException("CK01");
			fail.addSuppressed(e);
		}
		
		// Close the input stream processor
		try
		{
			this.processor.close();
		}
		
		// {@squirreljme.error CK02 Failed to close the stream processor.}
		catch (Throwable e)
		{
			if (fail != null)
				fail = new IOException("CK02");
			fail.addSuppressed(e);
		}
		
		// Threw an exception?
		if (fail != null)
			throw fail;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
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
	 * @since 2016/12/20
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Just call the processor
		return this.processor.read(__b, __o, __l);
	}
}

