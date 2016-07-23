// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.interpreter;

import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;

/**
 * This write resources to the output.
 *
 * @since 2016/07/22
 */
public class InterpreterResourceWriter
	extends InterpreterBaseOutput
	implements JITResourceWriter
{
	/** The resource being written. */
	protected final String resourcename;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the resource writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __rn The resource being written.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public InterpreterResourceWriter(InterpreterNamespaceWriter __nsw,
		String __rn)
		throws NullPointerException
	{
		super(__nsw);
		
		// Check
		if (__rn == null)
			throw new NullPointerException("NARG");
		
		// The resource name
		this.resourcename = __rn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public void close()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Handle closing
			if (!this._closed)
			{
				// Mark closed
				this._closed = true;
				
				throw new Error("TODO");
			}
			
			// Super handle
			super.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, JITException, NullPointerException
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO"); 
		}
	}
}

