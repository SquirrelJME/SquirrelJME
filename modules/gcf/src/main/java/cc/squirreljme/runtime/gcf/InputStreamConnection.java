// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.InputConnection;

/**
 * A connection which simply wraps an {@link InputStream}.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public final class InputStreamConnection
	implements InputConnection
{
	/** The stream to source from. */
	@SquirrelJMEVendorApi
	protected final InputStream in;
	
	/**
	 * Initializes the input stream connection.
	 *
	 * @param __in The input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	public InputStreamConnection(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Make sure marking is always supported
		if (__in.markSupported())
			this.in = __in;
		else
			this.in = new MarkableInputStream(__in);
		
		// Determine the number of available bytes for marking
		int avail;
		try
		{
			avail = __in.available();
		}
		catch (IOException __e)
		{
			avail = 0;
		}
		
		// Pre-mark the start of the stream
		this.in.mark(Math.max(avail, 1024));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void close()
		throws IOException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public DataInputStream openDataInputStream()
		throws IOException
	{
		return new DataInputStream(this.openInputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		synchronized (this)
		{
			// Always reset the mark
			InputStream in = this.in;
			in.reset();
			
			// Use the stream
			return in;
		}
	}
}
