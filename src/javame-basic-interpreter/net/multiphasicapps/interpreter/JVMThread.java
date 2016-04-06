// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

/**
 * This represents a thread within the interpreter.
 *
 * @since 2016/03/01
 */
public class JVMThread
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The engine which owns this thread. */
	protected final JVMEngine engine;
	
	/** Has this thread started? */
	private volatile boolean _started;
	
	/**
	 * Initializes the thread.
	 *
	 * @param __owner The owning thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	JVMThread(JVMEngine __owner)
		throws NullPointerException
	{
		// Check
		if (__owner == null)
			throw new NullPointerException();
		
		// Set
		engine = __owner;
	}
	
	/**
	 * Runs the current thread.
	 *
	 * @return {@code this}.
	 * @throws JVMIllegalThreadStateException If this thread has already
	 * started.
	 * @since 2016/03/01
	 */
	public JVMThread start()
		throws JVMIllegalThreadStateException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error IN01 Thread has already been started.
			// (The current thread)}
			if (_started)
				throw new JVMIllegalThreadStateException(String.format(
					"IN01 %s", this));
			_started = true;
			
			throw new Error("TODO");
		}
	}
}

