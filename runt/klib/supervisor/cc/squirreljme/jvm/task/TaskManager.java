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
	public static final byte MAX_TASKS =
		15;
	
	/** The shift for tasks. */
	private static final byte _TASK_SHIFT =
		4;
	
	/** The low mask for tasks. */
	private static final byte _TASK_MASK =
		0xF;
	
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
	 * Returns the task by the given logical ID.
	 *
	 * @param __lid The logical ID of the task.
	 * @return The given task.
	 * @throws NoSuchTaskException If the given task does not exist.
	 * @since 2019/12/14
	 */
	public final Task getTask(int __lid)
		throws NoSuchTaskException
	{
		synchronized (this)
		{
			// The lower bits are used to quickly obtain the PID slot
			Task rv = this.tasks[__lid & _TASK_MASK];
			if (rv != null && rv.lid == __lid)
				return rv;
		}
		
		// {@squirreljme.error SV10 No such task exists. (The task)}
		throw new NoSuchTaskException("SV10 " + __lid);
	}
	
	/**
	 * Creates a new task.
	 *
	 * @param __cp The class path to use.
	 * @param __im Is the initial class a MIDlet?
	 * @param __mcl The main class.
	 * @param __args The arguments to the task.
	 * @param __sp System properties.
	 * @return The resulting task.
	 * @throws NullPointerException On null arguments.
	 * @throws TooManyTasksException If the task could not be created.
	 * @since 2019/06/22
	 */
	public TaskCreateResult newTask(ClassLibrary[] __cp, String __mcl,
		boolean __im, String[] __args, String[] __sp)
		throws NullPointerException, TooManyTasksException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		// Tasks that are currently active
		Task[] tasks = this.tasks;
		
		// The resulting new task and its PID
		Task rv;
		int pid;
		
		// One a single process may make tasks at a time, so we need to lock
		// here to prevent the entire system from collapsing
		synchronized (this)
		{
			// Find a free task spot
			for (pid = 1; pid < MAX_TASKS; pid++)
				if (tasks[pid] == null)
					break;
			
			// {@squirreljme.error SV01 Task limit reached.}
			if (pid >= MAX_TASKS)
				throw new TooManyTasksException("SV01");
			
			// Setup and store task now
			rv = new Task(pid, ((this._nextlid++) << _TASK_SHIFT) | pid,
				new ClassPath(__cp));
			tasks[pid] = rv;
		}
		
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
			TaskClass mainclass = rv.loadClass(__mcl);
			
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
		
		// Create result
		return new TaskCreateResult(rv, thread, __mcl, mname, mtype, callargs);
	}
}

