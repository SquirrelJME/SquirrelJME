// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.config.ConfigRomKey;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.jvm.boot.lib.BootRom;
import cc.squirreljme.jvm.boot.task.TaskCreateResult;
import cc.squirreljme.jvm.boot.task.TaskManager;
import cc.squirreljme.jvm.boot.task.TaskSysCallHandler;
import cc.squirreljme.jvm.boot.task.ThreadManager;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is the bootstrap entry point for the supervisor.
 *
 * @since 2019/05/25
 */
public final class Bootstrap
{
	/**
	 * Not used.
	 *
	 * @since 2019/05/25
	 */
	private Bootstrap()
	{
	}
	
	/**
	 * Entry point for the bootstrap.
	 *
	 * @param __rambase The base RAM address.
	 * @param __ramsize The size of RAM.
	 * @param __rombase Base address of the ROM (for offset calculation).
	 * @param __romsize The size of ROM.
	 * @param __confbase The configuration memory base.
	 * @param __confsize The configuration memory size.
	 * @since 2019/05/25
	 */
	@SuppressWarnings("unused")
	static final void __start(int __rambase, int __ramsize,
		int __rombase, int __romsize, int __confbase, int __confsize)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
		/*
		// Initialize the RAM links to setup dirty bits and initialize the
		// last block of memory with anything that remains. This makes it so
		// the RAM is actually useable.
		Allocator.__initRamLinks(__rambase, __ramsize);
		
		// Could crash!
		try
		{
			// Initialize kernel thread, since there has to be a thread
			// reference for classes to work!
			ThreadManager thm = Globals.getThreadManager();
			Assembly.specialSetThreadRegister(Assembly.objectToPointerWide(
				thm.BOOT_THREAD));
			
			// Initialize config reader
			ConfigReader config = new ConfigReader(__confbase);
			
			// Basic SquirrelJME Banner
			todo.DEBUG.note("SquirrelJME Run-Time 0.3.0");
			todo.DEBUG.note("VM: %s %s",
				config.loadString(ConfigRomKey.JAVA_VM_NAME),
				config.loadString(ConfigRomKey.JAVA_VM_VERSION));
			todo.DEBUG.note("(C) %s",
				config.loadString(ConfigRomKey.JAVA_VM_VENDOR));
			todo.DEBUG.note("RAM/ROM (bytes): %d/%d", __ramsize, __romsize);
			
			// Spacer
			todo.DEBUG.note("");
			
			// Load system call handler
			TaskSysCallHandler.initTaskHandler(config);
			
			// Get the task manager
			TaskManager ctm = Globals.getTaskManager();
			
			// Start the initial task
			todo.DEBUG.note("Creating initial task...");
			TaskCreateResult boot = ctm.newTask(
				BootRom.initialClasspath(__rombase, config),
				BootRom.initialMain(__rombase, config),
				BootRom.initialIsMidlet(__rombase, config),
				config.loadStrings(ConfigRomKey.MAIN_ARGUMENTS),
				config.loadKeyValueMap(ConfigRomKey.DEFINE_PROPERTY));
			todo.DEBUG.note("Okay.");
			
			// Set the kernel as booted okay!
			todo.DEBUG.note("Appears things are going well?");
			Assembly.sysCall(SystemCallIndex.SUPERVISOR_BOOT_OKAY);
			
			// Enter the main task now, since we can do that!
			todo.DEBUG.note("Entering main method!");
			boot.thread.execute(boot.mainclass, boot.mainmethodname,
				boot.mainmethodtype, boot.callargs);
			
			// Finished? Just terminate then
			todo.DEBUG.note("Main program finished execution, terminating!");
			Assembly.sysCallP(SystemCallIndex.EXIT, 0);
		}
		
		// It crashes
		catch (Throwable t)
		{
			// Print the trace
			try
			{
				// Print stack trace for this class
				t.printStackTrace();
			}
			
			// Double fault?!?!?! SOMETHING IS VERY WRONG!
			catch (Throwable u)
			{
				// Ignore
			}
			
			// Try to exit the VM
			Assembly.sysCallP(SystemCallIndex.EXIT, 1);
			
			// If that did not work, just break and return!
			Assembly.breakpoint();
			return;
		}
		
		 */
	}
}

