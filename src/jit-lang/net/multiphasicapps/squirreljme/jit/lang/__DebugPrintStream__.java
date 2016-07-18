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
 * This is a stream which writes any byte it recieves to standard error for
 * debugging purposes.
 *
 * @since 2016/07/17
 */
class __DebugPrintStream__
	extends OutputStream
{
	/** Output. */
	protected final OutputStream output;
	
	/**
	 * Initializes the debug stream
	 *
	 * @param __os The stream to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	__DebugPrintStream__(OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __os;
		
		// Say it was opened
		System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public void close()
		throws IOException
	{
		// Say it was closed
		System.err.println("-----------------------------------------------");
		
		// Close
		this.output.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public void flush()
		throws IOException
	{
		this.output.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		// If a good ASCII character, print it
		PrintStream err = System.err;
		if (__b == '\t' || __b == '\r' || __b == '\n' ||
			(__b >= 0x20 && __b <= 0x7E))
			err.print((char)__b);
		
		// Otherwise use a replacement
		else
			err.print('?');
		
		// Normal write
		this.output.write(__b);
	}
}

