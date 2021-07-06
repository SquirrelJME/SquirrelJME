// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

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
public final class JDWPFactory
	implements Closeable
{
	/** The input communication stream. */
	protected final InputStream in;
	
	/** The output communication stream. */
	protected final OutputStream out;
	
	/**
	 * Initializes the factory for the given stream connections.
	 * 
	 * @param __in The input stream to read from.
	 * @param __out The output stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public JDWPFactory(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.in = new DataInputStream(__in);
		this.out = new DataOutputStream(__out);
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
	 * Opens the controller.
	 * 
	 * @param __bind The binding to use.
	 * @return The controller used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/08
	 */
	public final JDWPController open(JDWPBinding __bind)
		throws NullPointerException
	{
		if (__bind == null)
			throw new NullPointerException("NARG");
		
		// Return a new controller for processing events
		return new JDWPController(__bind, this.in, this.out);
	}
}
