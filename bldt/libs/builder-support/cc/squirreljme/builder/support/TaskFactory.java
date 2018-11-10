// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.SuiteManager;
import javax.microedition.swm.SuiteType;
import javax.microedition.swm.Task;
import javax.microedition.swm.TaskManager;
import javax.microedition.swm.TaskStatus;

/**
 * This is a factory which is used to handle tasks via the SWM sub-system.
 *
 * @since 2017/12/07
 */
public class TaskFactory
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
	public TaskFactory(String... __args)
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
		
		// {@squirreljme.error AU0w Expected command for task operation.}
		String command = args.pollFirst();
		if (command == null)
			throw new IllegalArgumentException("AU0w");
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
				// Launch a task
			case "launch":
				{
					// Parse arguments
					String[] parse;
					while (null != (parse = BuilderFactory.
						__getopts(":?", args)))
						switch (parse[0])
						{
								// {@squirreljme.error AU0x Unknown argument.
								// Usage: launch [suite] (class name).
								// }
							default:
								throw new IllegalArgumentException("AU0x");
						}
					
					// {@squirreljme.error AU0y Expected name of class to
					// start as a system task.}
					String arga = args.pollFirst();
					if (arga == null)
						throw new IllegalArgumentException("AU0y");
					
					// Secondary argument is optional
					String argb = args.pollFirst();
					
					// Launch task
					Task task;
					if (argb == null)
						task = launch(null, arga);
					else
						task = launch(arga, argb);
					
					// Wait for the task to terminate
					// There is no non-blocking mechanism which waits until
					// the task terminates, so just keep quering the state
					// until it is not running
					TaskStatus status;
					for (;;)
					{
						status = task.getStatus();
						if (status != TaskStatus.STARTING &&
							status != TaskStatus.RUNNING)
							break;
						
						// Sleep so the CPU is not burnt out
						try
						{
							Thread.sleep(500);
						}
						catch (InterruptedException e)
						{
						}
					}
					
					// {@squirreljme.error AU0z The task exited with the
					// given status. (The status)}
					if (status != TaskStatus.EXITED_REGULAR)
						throw new RuntimeException(String.format(
							"AU0z %s%n", status));
				}
				break;
				
				// List tasks
			case "ls":
			case "list":
				listTasks(System.out);
				break;
				
				// {@squirreljme.error AU10 The specified task command is not
				// valid. Valid commands are:
				// ls, list, launch
				// .(The command)}
			case "help":
			default:
				throw new IllegalArgumentException(String.format("AU10 %s",
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
			
			// Print suite information
			Suite s = t.getSuite();
			if (s != null)
				SuiteFactory.printSuite(__ps, s);
		}
	}
	
	/**
	 * Launches the optional suite and via the given class.
	 *
	 * @param __su The suite to launch, if {@code null} then the system suite
	 * is used.
	 * @param __cl The class to use as the entry point.
	 * @throws NullPointerException If no class was specified.
	 * @since 2017/12/08
	 */
	public Task launch(String __su, String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Locate the suite to use
		Suite suite = null;
		if (__su != null)
		{
			// For simplicity have an index lookup
			int wantdx = -1;
			try
			{
				wantdx = Integer.parseInt(__su);
			}
			catch (NumberFormatException e)
			{
			}
			
			// Go through suites and try to find a match
			SuiteManager sm = ManagerFactory.getSuiteManager();
			int atdx = 0;
			for (Suite s : sm.getSuites(SuiteType.APPLICATION))
				if (wantdx == (atdx++) ||
					__su.equals(s.getName()) ||
					__su.equals(s.getVendor()))
				{
					suite = s;
					break;
				}
			
			// {@squirreljme.error AU11 Could not locate a suite with the
			// given name. (The input name)}
			if (suite == null)
				throw new IllegalArgumentException(
					String.format("AU11 %s", __su));
		}
		
		// Use system suite
		else
			suite = Suite.SYSTEM_SUITE;
		
		// Show suite to launch
		SuiteFactory.printSuite(System.out, suite);
		
		// Start the task
		return this.manager.startTask(suite, __cl);
	}
}

