// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.lib.ClassLibrary;

/**
 * This manages multiple tasks.
 *
 * @since 2019/06/22
 */
public final class ClientTaskManager
{
	/** The maximum number of permitted tasks. */
	public static final int MAX_TASKS =
		15;
	
	/** The tasks which are available. */
	public final ClientTask[] tasks =
		new ClientTask[MAX_TASKS];
	
	/** The next logical task ID. */
	private volatile int _nextlid =
		1;
	
	/**
	 * Initializes the client task manager.
	 *
	 * @since 2019/06/22
	 */
	public ClientTaskManager()
	{
		// Setup a system task, it has no classpath and is always zero
		this.tasks[0] = new ClientTask(0, 0, new ClassLibrary[0]);
	}
	
	/**
	 * Creates a new task.
	 *
	 * @param __cp The class path to use.
	 * @param __mcl The main class.
	 * @param __args The arguments to the task.
	 * @param __sp System properties.
	 * @return The resulting task.
	 * @throws RuntimeException If the task could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/22
	 */
	public ClientTask newTask(ClassLibrary[] __cp, String __mcl,
		String[] __args, String[] __sp)
		throws NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		// Tasks that are currently active
		ClientTask[] tasks = this.tasks;
		
		// Find a free task spot
		int pid;
		for (pid = 1; pid < MAX_TASKS; pid++)
			if (tasks[pid] == null)
				break;
		
		// {@squirreljme.error SV01 Task limit reached.}
		if (pid == MAX_TASKS)
			throw new RuntimeException("SV01");
		
		// Setup and store task now
		ClientTask rv = new ClientTask(pid, this._nextlid++, __cp);
		tasks[pid] = rv;
		
		// Debug
		todo.DEBUG.note("Initializing task %d (%d)...", rv.lid, pid);
		
		// Load the main class
		ClientClassInfo maincl = rv.loadClassInfo(__mcl);
		
		if (true)
		{
			Assembly.breakpoint();
			throw new todo.TODO();
		}
		
		// Done with the task
		return rv;
	}
}

