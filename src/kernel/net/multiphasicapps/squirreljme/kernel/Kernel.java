// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import net.multiphasicapps.squirreljme.kernel.display.ConsoleDisplay;
import net.multiphasicapps.squirreljme.kernel.event.EventQueue;
import net.multiphasicapps.squirreljme.kernel.Kernel;

/**
 * This is the base class for the kernel interfaces which are defined by
 * systems to provide anything that the default kernel does not provide
 * when it comes to interfaces. All calls which are done by the user interface
 * and the running programs in the kernel, will call the kernel to perform
 * system calls and such.
 *
 * @since 2016/05/14
 */
public abstract class Kernel
{
	/** Threads currently associated with the kernel. */
	protected final Set<Thread> threads =
		new HashSet<>();
	
	/** The event queue. */
	protected final EventQueue events =
		new EventQueue();
	
	/**
	 * Creates a view of a console window.
	 *
	 * Note that if multi-headed consoles are supported then the interface
	 * may show multiple terminals either in windows, tabs, or some other
	 * interface specific means. If a console does not support multiple heads
	 * then any console being displayed will potentially erase or draw over
	 * a previously drawn console.
	 *
	 * @return A newly created console window or {@code null} if it could not
	 * be created for some reason.
	 * @since 2016/05/14
	 */
	public abstract ConsoleDisplay createConsoleDisplay();
	
	/**
	 * Returns the event queue of the kernel.
	 *
	 * @return The kernel event queue.
	 * @since 2016/05/15
	 */
	public final EventQueue eventQueue()
	{
		return this.events;
	}
	
	/**
	 * Creates a new thread and registers it with the kernel.
	 *
	 * @param __r The code to be ran when the thread is started.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	public final Thread newThread(Runnable __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Setup new thread and start it
		Thread rv = new Thread(__r);
		rv.start();
		
		// Lock
		Set<Thread> thr = threads;
		synchronized (thr)
		{
			thr.add(rv);
		}
		
		// Return the newly created thread
		return rv;
	}
	
	/**
	 * Does not return until all threads are no longer alive.
	 *
	 * @since 2016/05/14
	 */
	public final void untilThreadless()
	{
		// Loop
		Set<Thread> thr = threads;
		for (;;)
		{
			// Lock
			int livecount = 0;
			synchronized (thr)
			{
				// Count and remove threads
				Iterator<Thread> it = thr.iterator();
				try
				{
					// Go through each iteration
					for (;;)
					{
						// Get the next thread
						Thread t = it.next();
						
						// If the thread is alive, count it
						if (t.isAlive())
							livecount++;
						
						// Otherwise remove it
						else
							it.remove();
					}
				}
				
				// End
				catch (NoSuchElementException e)
				{
				}
			}
			
			// Out of threads?
			if (livecount < 0)
				return;
			
			// Rest for a bit since threads usually will not just die.
			try
			{
				Thread.sleep(750L);
			}
			
			// Do nothing
			catch (InterruptedException e)
			{
			}
		}
	}
}

