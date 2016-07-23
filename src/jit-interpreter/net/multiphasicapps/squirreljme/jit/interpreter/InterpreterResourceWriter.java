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

import java.io.IOException;
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
		super(__nsw, __rn);
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
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BV08 Write of resource after closed.}
			if (isClosed())
				throw new JITException("BV08");
			
			// Send in the data
			try
			{
				super.output.write(__b, __o, __l);
			}
			
			// {@squirreljme.error BV07 Failed to write the resource to the
			// output.}
			catch (IOException e)
			{
				throw new JITException("BV07", e);
			}
		}
	}
}

