// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.lang;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Resources within a namespace must be exposed, however every language will
 * have a unique way of referring to resource data. As such, language namespace
 * writers must extend this class which provides standard output for resources
 * in a given language.
 *
 * @since 2016/07/17
 */
public abstract class ResourceOutputStream
	extends OutputStream
{
	/** The stream to write to. */
	protected final PrintStream output;
	
	/**
	 * Initializes the resource output stream.
	 *
	 * @param __ps The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public ResourceOutputStream(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __ps;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the output
		this.output.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public final void flush()
		throws IOException
	{
		// Flush the output
		this.output.flush();
	}
}

