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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This contains the thread manager for the interpreter engine..
 *
 * @since 2016/04/06
 */
public final class JVMThreads
{
	/** The owning engine. */
	protected final JVMEngine engine;
	
	/** The threads the interpreter owns (lock on this). */
	protected final Set<JVMThread> threads =
		new LinkedHashSet<>();
	
	/** The default thread. */
	protected final JVMThread defaultthread;
	
	/** The next thread ID. */
	private volatile long _nextid =
		0L;
	
	/**
	 * Initializes the thread manager.
	 *
	 * @param __e The owning engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/06
	 */
	JVMThreads(JVMEngine __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Set
		engine = __e;
		
		// Setup default thread
		defaultthread = new JVMThread(this, _nextid++);
	}
	
	/**
	 * Creates a new a thread.
	 *
	 * @param __meth The method to start execution at.
	 * @param __args Thread arguments, these take either boxed types or
	 * {@code JVMObject}, all other classes are illegal.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public JVMThread createThread(JVMMethod __meth, Object... __args)
		throws NullPointerException
	{
		// Check
		if (__meth == null)
			throw new NullPointerException("NARG");
		
		// Lock on threads
		Set<JVMThread> ths = threads;
		synchronized (ths)
		{
			// Create
			JVMThread jt = new JVMThread(this, _nextid++, __meth, __args);
			
			// Add it to the thread list
			ths.add(jt);
			
			// Return it
			return jt;
		}
	}
	
	/**
	 * The default thread.
	 *
	 * @return The default thread.
	 * @since 2016/04/07
	 */
	public JVMThread defaultThread()
	{
		return defaultthread;
	}
	
	/**
	 * Returns {@code true} if the intepreter has no threads remaining which
	 * are alive and executing (they have all exited).
	 *
	 * @return {@code true} if no living threads remain, otherwise
	 * {@code false}.
	 * @since 2016/03/01
	 */
	public final boolean isTerminated()
	{
		// Lock on threads
		Set<JVMThread> ths = threads;
		synchronized (ths)
		{
			// Go through all threads
			Iterator<JVMThread> it = ths.iterator();
			while (it.hasNext())
			{
				// Next thread to check
				JVMThread nx = it.next();
				
				// Has ended?
				if (nx.isTerminated())
					it.remove();
			}
			
			// Only if no threads remain
			return threads.isEmpty();
		}
	}
}

