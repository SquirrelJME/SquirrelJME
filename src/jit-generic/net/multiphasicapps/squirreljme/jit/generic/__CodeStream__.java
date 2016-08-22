// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This wraps the output stream to verify that it gets closed.
 *
 * @since 2016/08/21
 */
final class __CodeStream__
	extends OutputStream
{
	/** The owner. */
	protected final GenericClassWriter owner;
	
	/** The origin output stream. */
	protected final OutputStream output;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the code stream.
	 *
	 * @param __cw The owning class writer.
	 * @param __os The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	__CodeStream__(GenericClassWriter __cw, OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__cw == null || __os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __cw;
		this.output = __os;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public void close()
		throws IOException
	{
		// Handle closing
		if (!this._closed)
		{
			// Mark
			this._closed = true;	
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public void flush()
		throws IOException
	{
		this.output.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		// {@squirreljme.error BA0c Write of single machine code after stream
		// closed.}
		if (this._closed)
			throw new IOException("BA0c");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		// {@squirreljme.error BA0l Write of multiple machine codes after
		// stream closed.}
		if (this._closed)
			throw new IOException("BA0l");
		
		throw new Error("TODO");
	}
}

