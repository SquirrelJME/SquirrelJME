// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Factory for creating controllers.
 *
 * @since 2021/03/08
 */
public final class JDWPHostFactory
	implements Closeable
{
	/** The input communication stream. */
	protected final InputStream in;
	
	/** The output communication stream. */
	protected final OutputStream out;
	
	/** The port the debugger is on. */
	protected final int port;
	
	/**
	 * Initializes the factory for the given stream connections.
	 *
	 * @param __in The input stream to read from.
	 * @param __out The output stream to write to.
	 * @param __port The port the debugger is on.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public JDWPHostFactory(InputStream __in, OutputStream __out, int __port)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.in = new DataInputStream(__in);
		this.out = new DataOutputStream(__out);
		this.port = __port;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/08
	 */
	@Override
	public void close()
		throws IOException
	{
		IOException fail = null;
		
		// Close the input
		try
		{
			this.in.close();
		}
		catch (IOException e)
		{
			fail = e;
		}
		
		// And the output
		try
		{
			this.out.close();
		}
		catch (IOException e)
		{
			if (fail == null)
				fail = e;
			else
				fail.addSuppressed(e);
		}
		
		if (fail != null)
			throw fail;
	}
	
	/**
	 * Returns the input stream.
	 *
	 * @return The input stream.
	 * @since 2024/01/30
	 */
	public InputStream in()
	{
		return this.in;
	}
	
	/**
	 * Opens the controller.
	 * 
	 * @param __bind The binding to use.
	 * @return The controller used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public final JDWPHostController open(JDWPHostBinding __bind)
		throws NullPointerException
	{
		if (__bind == null)
			throw new NullPointerException("NARG");
		
		// Return a new controller for processing events
		return new JDWPHostController(__bind, this.in, this.out);
	}
	
	/**
	 * Returns the output stream.
	 *
	 * @return The output stream.
	 * @since 2024/01/30
	 */
	public OutputStream out()
	{
		return this.out;
	}
}
