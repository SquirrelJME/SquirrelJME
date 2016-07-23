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

import java.io.DataOutputStream;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class acts as the base for interpreter outputs to either classes or
 * resources.
 *
 * @since 2016/07/22
 */
public abstract class InterpreterBaseOutput
	implements AutoCloseable
{
	/** Lock. */
	protected final Object lock;
	
	/** The output. */
	protected final DataOutputStream output;
	
	/** The position where the data starts. */
	protected final int datastart;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the base output.
	 *
	 * @param __nsw The owning namespace writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public InterpreterBaseOutput(InterpreterNamespaceWriter __nsw)
		throws NullPointerException
	{
		// Check
		if (__nsw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.lock = __nsw.interpreterLock();
		DataOutputStream dos = __nsw.interpreterOutput();
		this.output = dos;
		
		// Get starting point
		synchronized (this.lock)
		{
			this.datastart = dos.size();
		}
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
				// Set closed
				this._closed = true;
				
				throw new Error("TODO");
			}
		}
	}
}

