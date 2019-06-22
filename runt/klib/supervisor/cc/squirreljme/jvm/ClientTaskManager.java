// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This manages multiple tasks.
 *
 * @since 2019/06/22
 */
public final class ClientTaskManager
{
	/** The maximum number of permitted tasks. */
	public static final int MAX_TASKS =
		14;
	
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
		// Setup a system task
		this.tasks[0] = new ClientTask(0, 0);
	}
	
	/**
	 * Creates a new task.
	 *
	 * @param __cp The class path to use.
	 * @param __mcl The main class.
	 * @param __args The arguments to the task.
	 * @param __sp System properties.
	 * @return The resulting task.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/22
	 */
	public ClientTask newTask(BootLibrary[] __cp, String __mcl,
		String[] __args, String[] __sp)
		throws NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

