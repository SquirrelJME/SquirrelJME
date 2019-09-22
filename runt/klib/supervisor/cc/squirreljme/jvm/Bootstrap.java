// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.jvm.lib.BootRom;
import cc.squirreljme.jvm.lib.BootRomLibrary;

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
	static final void __start(int __rambase, int __ramsize,
		int __rombase, int __romsize, int __confbase, int __confsize)
	{
		// Initialize the RAM links to setup dirty bits and initialize the
		// last block of memory with anything that remains. This makes it so
		// the RAM is actually useable.
		Allocator.__initRamLinks(__rambase, __ramsize);
		
		// Could crash!
		try
		{
			// Initialize config reader
			ConfigReader config = new ConfigReader(__confbase);
			
			// Basic SquirrelJME Banner
			todo.DEBUG.note("SquirrelJME Run-Time 0.3.0");
			todo.DEBUG.note("VM: %s %s",
				config.loadString(ConfigRomType.JAVA_VM_NAME),
				config.loadString(ConfigRomType.JAVA_VM_VERSION));
			todo.DEBUG.note("(C) %s",
				config.loadString(ConfigRomType.JAVA_VM_VENDOR));
			todo.DEBUG.note("RAM/ROM (bytes): %d/%d", __ramsize, __romsize);
			
			// Spacer
			todo.DEBUG.note("");
			
			// Initialize the client task manager
			todo.DEBUG.note("Initializing task manager...");
			ClientTaskManager ctm = new ClientTaskManager();
			todo.DEBUG.note("Okay.");
			
			// Spacer
			todo.DEBUG.note("");
			
			// Start the initial task
			todo.DEBUG.note("Creating initial task...");
			ClientTask boot = ctm.newTask(
				BootRom.initialClasspath(__rombase, config),
				BootRom.initialMain(__rombase, config),
				config.loadStrings(ConfigRomType.MAIN_ARGUMENTS),
				config.loadKeyValueMap(ConfigRomType.DEFINE_PROPERTY));
			todo.DEBUG.note("Okay.");
			
			// Something later on
			Assembly.breakpoint();
			
			// Set the kernel as booted okay!
			Assembly.sysCall(SystemCallIndex.SUPERVISOR_BOOT_OKAY);
			
			Assembly.breakpoint();
			throw new todo.TODO();
		}
		
		// It crashes
		catch (Throwable t)
		{
			// Print stack trace for this class
			t.printStackTrace();
			
			// Try to exit the VM
			Assembly.sysCallP(SystemCallIndex.EXIT, 1);
			
			// If that did not work, just break and return!
			Assembly.breakpoint();
			return;
		}
	}
}

