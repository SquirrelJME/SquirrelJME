// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

/**
 * This represents a thread within the interpreter.
 *
 * @since 2016/03/01
 */
public class JVMThread
	implements Runnable
{
	/** The real thread of the interpreter. */
	protected final Thread realthread;	
	
	/** The engine which owns this thread. */
	protected final JVMEngine engine;
	
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
		
		// Setup real thread
		realthread = new Thread(this);
	}
	
	/**
	 * Runs the current thread.
	 *
	 * @since 2016/03/01
	 */
	public void run()
	{
		throw new Error("TODO");
	}
}

