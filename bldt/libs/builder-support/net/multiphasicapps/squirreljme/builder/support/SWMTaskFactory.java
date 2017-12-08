// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.Task;
import javax.microedition.swm.TaskManager;

/**
 * This is a factory which is used to handle tasks via the SWM sub-system.
 *
 * @since 2017/12/07
 */
public class SWMTaskFactory
	implements Runnable
{
	/** The command to execute. */
	protected final String command;
	
	/** The manager for tasks, which is required. */
	protected final TaskManager manager;
	
	/** Arguments to the task command. */
	private final String[] _args;
	
	/**
	 * Initializes the SWM task factory.
	 *
	 * @param __args The argument to the factory.
	 * @since 2017/12/07
	 */
	public SWMTaskFactory(String... __args)
	{
		// Copy arguments for processing
		Deque<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.addLast(a);
		
		// Obtain the manager because it is possible that there are
		// no permissions to do so
		this.manager = ManagerFactory.getTaskManager();
		
		// {@squirreljme.error AU0q Expected command for task operation.}
		String command = args.pollFirst();
		if (command == null)
			throw new IllegalArgumentException("AU0q");
		this.command = command;
		
		// Use remaining arguments as input
		this._args = args.<String>toArray(new String[args.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public void run()
	{
		// Load arguments into a queue
		Deque<String> args =
			new ArrayDeque<>(Arrays.<String>asList(this._args));
		
		// Depends on the command
		String command = this.command;
		switch (command)
		{
				// List tasks
			case "ls":
			case "list":
				listTasks(System.out);
				break;
				
				// Start a system task
			case "system-start":
				{
					// {@squirreljme.error AU0s Expected name of class to
					// start as a system task.}
					String name = args.removeFirst();
					if (name == null)
						throw new IllegalArgumentException("AU0s");
					
					systemStart(name);
				}
				break;
				
				// {@squirreljme.error AU0r The specified task command is not
				// valid. Valid commands are:
				// ls, list, system-start
				// .(The command)}
			default:
				throw new IllegalArgumentException(String.format("AU0r %s",
					command));
		}
	}
	
	/**
	 * Lists the tasks and prints them to the specified stream.
	 *
	 * @param __ps The stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/07
	 */
	public void listTasks(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Go through all tasks
		for (Task t : this.manager.getTaskList(true))
		{
			__ps.printf("Task: %s%n", t.getName());
			
			__ps.printf("\tis system? %s%n", t.isSystemTask());
			__ps.printf("\tpriority : %s%n", t.getPriority());
			__ps.printf("\tstatus   : %s%n", t.getStatus());
			__ps.printf("\tused heap: %d bytes%n", t.getHeapUse());
		}
	}
	
	/**
	 * Starts the specified class as a system task, this blocks until the
	 * task terminates.
	 *
	 * @param __n The task to start under the system.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	public Task systemStart(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		Task task = this.manager.startTask(Suite.SYSTEM_SUITE, __n);
		
		throw new todo.TODO();
	}
}

