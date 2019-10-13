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
import cc.squirreljme.jvm.lib.ClassPath;

/**
 * This manages multiple tasks.
 *
 * @since 2019/06/22
 */
public final class TaskManager
{
	/** The maximum number of permitted tasks. */
	public static final int MAX_TASKS =
		15;
	
	/** The tasks which are available. */
	public final Task[] tasks =
		new Task[MAX_TASKS];
	
	/** The next logical task ID. */
	private volatile int _nextlid =
		1;
	
	/**
	 * Initializes the client task manager.
	 *
	 * @since 2019/06/22
	 */
	public TaskManager()
	{
		// Setup a system task, it has no classpath and is always zero
		this.tasks[0] = new Task(0, 0, new ClassPath());
	}
	
	/**
	 * Creates a new task.
	 *
	 * @param __cp The class path to use.
	 * @param __im Is the initial class a MIDlet?
	 * @param __mcl The main class.
	 * @param __args The arguments to the task.
	 * @param __sp System properties.
	 * @param __main The main thread output.
	 * @return The resulting task.
	 * @throws RuntimeException If the task could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/22
	 */
	public Task newTask(ClassLibrary[] __cp, String __mcl, boolean __im,
		String[] __args, String[] __sp, TaskThread[] __main)
		throws NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		// Tasks that are currently active
		Task[] tasks = this.tasks;
		
		// Find a free task spot
		int pid;
		for (pid = 1; pid < MAX_TASKS; pid++)
			if (tasks[pid] == null)
				break;
		
		// {@squirreljme.error SV01 Task limit reached.}
		if (pid >= MAX_TASKS)
			throw new RuntimeException("SV01");
		
		// Setup and store task now
		Task rv = new Task(pid, this._nextlid++, new ClassPath(__cp));
		tasks[pid] = rv;
		
		// Create main thread to initialize
		TaskThread thread = rv.createThread();
		
		// Set static field pointer of this thread, this is so that static
		// field areas can be executed properly
		thread.setStaticFieldPointer(rv.allocator.getStaticFieldPointer());
		
		// The method and arguments to use for the entry call
		String mname, mtype;
		int[] callargs;
		
		// Loading a MIDlet, so initialize MIDlet class
		if (__im)
		{
			// Load instance of main MIDlet class
			int mainclass = rv.loadClass(__mcl);
			
			// Create instance of the MIDlet class
			int midinstance = rv.newInstance(mainclass);
			
			// Setup call information
			mname = "startApp";
			mtype = "()V";
			callargs = new int[]{midinstance};
		}
		
		// Start from static main() entry point
		else
		{
			// Load arguments for main class into array
			int numargs = (__args == null ? 0 : __args.length);
			int[] mainargs = new int[numargs];
			for (int i = 0; i < numargs; i++)
				mainargs[i] = rv.loadString(__args[i]);
			
			// Load main arguments into string array
			int argsarray = rv.loadObjectArray(
				rv.loadClass("[Ljava/lang/String;"), mainargs);
			
			// Setup call information
			mname = "main";
			mtype = "([Ljava/lang/String;)V";
			callargs = new int[]{argsarray};
		}
		
		// Enter this frame
		thread.enterFrame(__mcl, mname, mtype, callargs);
		
		// Store the thread being executed before we return, all done with it!
		if (__main != null && __main.length > 0)
			__main[0] = thread;
		
		// Return this task
		return rv;
	}
}

