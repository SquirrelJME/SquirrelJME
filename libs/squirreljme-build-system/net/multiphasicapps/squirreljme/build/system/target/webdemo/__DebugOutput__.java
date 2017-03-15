// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system.target.webdemo;

import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is used to debug the output.
 *
 * @since 2017/03/15
 */
class __DebugOutput__
	extends OutputStream
	implements Flushable
{
	/** Where to write bytes to. */
	protected final OutputStream output;
	
	/**
	 * Initializes the wrapper.
	 *
	 * @param __os Where bytes are written to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/15
	 */
	__DebugOutput__(OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");	
		
		// Set
		this.output = __os;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/15
	 */
	@Override
	public void close()
		throws IOException
	{
		this.output.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/15
	 */
	@Override
	public void flush()
		throws IOException
	{
		this.output.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/15
	 */
	@Override
	public void write(int __c)
		throws IOException
	{
		this.output.write(__c);
		System.err.write(__c);
		System.err.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/15
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		this.output.write(__b, __o, __l);
		System.err.write(__b, __o, __l);
		System.err.flush();
	}
}

