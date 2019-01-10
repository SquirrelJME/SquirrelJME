// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMSuiteManager;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This represents a task which is running within the virtual machine.
 *
 * @since 2019/01/01
 */
public final class RunningTask
{
	/** The status of this task. */
	protected final TaskStatus status;
	
	/** The next thread ID. */
	private volatile int _nextthreadid;
	
	/**
	 * Initializes the running task.
	 *
	 * @param __st The task status.
	 * @param __cl The class loader.
	 * @param __sprops System properties.
	 * @param __p Profiler information.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/05
	 */
	public RunningTask(TaskStatus __st)
		throws NullPointerException
	{
		if (__st == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.status = __st;
	}
	
	/**
	 * Creates a new thread and returns it, the thread will be blank and will
	 * have no state attached to it. It is not started unless it is explictely
	 * started.
	 *
	 * @return The created thread.
	 * @since 2019/01/10
	 */
	public final RunningThread createThread()
	{
		// Lock on self since we need to increment our next ID and add the
		// actual thread
		synchronized (this)
		{
			// Use the next thread ID, whatever that is
			int usetid = this._nextthreadid++;
			
			// Setup new thread using our status since the state information
			// is there instead of in this task instance
			TaskStatus status = this.status;
			RunningThread rv = new RunningThread(usetid, status);
			
			// Register thread with our status
			status.__registerThread(rv);
			
			// The thread being returned will be blank
			return rv;
		}
	}
}

