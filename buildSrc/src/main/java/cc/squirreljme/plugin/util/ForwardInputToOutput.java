// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Forwards an {@link InputStream} to an {@link OutputStream}.
 *
 * @since 2024/02/04
 */
public class ForwardInputToOutput
	extends ForwardStream
{
	/** The stream to read from. */
	protected final InputStream in;
	
	/** The stream to write to. */
	protected final OutputStream out;
	
	/**
	 * Initializes the forwarder.
	 *
	 * @param __from Where to read from.
	 * @param __to Where to forward to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/04
	 */
	public ForwardInputToOutput(InputStream __from, OutputStream __to)
		throws NullPointerException
	{
		super(__from, __to);
		
		this.in = __from;
		this.out = __to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public void close()
		throws IOException
	{
		super.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public void run()
	{
		InputStream in = this.in;
		OutputStream out = this.out;
		
		// Run communication in a loop
		try
		{
			byte[] buf = new byte[512 * 1024];
			for (;;)
			{
				// Read in data
				int rc = in.read(buf, 0, buf.length);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Write to the output and flush the target
				out.write(buf, 0, rc);
				out.flush();
			}
		}
		
		// Failed?
		catch (IOException __e)
		{
			__e.printStackTrace();
		}
		
		// Make sure everything is cleaned up
		finally
		{
			try
			{
				this.close();
			}
			catch (IOException __e)
			{
				__e.printStackTrace();
			}
		}
	}
}
