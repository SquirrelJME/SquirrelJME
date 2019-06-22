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
	 * Searches the configuration space for the given key and returns it's
	 * value.
	 *
	 * @param __confbase The configuration base.
	 * @param __key The key to search for.
	 * @return The pointer to the configuration value or {@code 0} if it was
	 * not found.
	 * @since 2019/06/19
	 */
	public static final int configSearch(int __confbase, int __key)
	{
		// Seek through items
		for (int seeker = __confbase;;)
		{
			// Read key and size
			int key = Assembly.memReadJavaShort(seeker,
					Constants.CONFIG_KEY_OFFSET),
				len = Assembly.memReadJavaShort(seeker,
					Constants.CONFIG_SIZE_OFFSET) & 0xFFFF;
			
			// Stop?
			if (key == ConfigRomType.END)
				break;
			
			// Found here?
			if (key == __key)
				return seeker + Constants.CONFIG_HEADER_SIZE;
			
			// Skip otherwise
			seeker += Constants.CONFIG_HEADER_SIZE + len;
		}
		
		// Not found
		return 0;
	}
	
	/**
	 * Loads the main arguments which are passed to the program being
	 * bootstrapped.
	 *
	 * @param __confbase The configuration base.
	 * @return The main arguments.
	 * @since 2019/06/22
	 */
	public static final String[] loadMainArgs(int __confbase)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Loads the main class.
	 *
	 * @param __confbase The configuration base.
	 * @return The main class.
	 * @since 2019/06/22
	 */
	public static final String loadMainClass(int __confbase)
	{
		// Load the string pointer
		return JVMFunction.jvmLoadString(Bootstrap.configSearch(__confbase,
			ConfigRomType.MAIN_CLASS));
	}
	
	/**
	 * Loads the passed system properties.
	 *
	 * @param __confbase The configuration base.
	 * @return The system properties in key/value pairs.
	 * @since 2019/06/22
	 */
	public static final String[] loadSystemProperties(int __confbase)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
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
			// Basic SquirrelJME Banner
			todo.DEBUG.note("SquirrelJME Run-Time 0.3.0");
			todo.DEBUG.note("VM: %s %s", JVMFunction.jvmLoadString(
				Bootstrap.configSearch(__confbase,
				ConfigRomType.JAVA_VM_NAME)), JVMFunction.jvmLoadString(
				Bootstrap.configSearch(__confbase,
				ConfigRomType.JAVA_VM_VERSION)));
			todo.DEBUG.note("(C) %s", JVMFunction.jvmLoadString(
				Bootstrap.configSearch(__confbase,
				ConfigRomType.JAVA_VM_VENDOR)));
			todo.DEBUG.note("RAM/ROM (bytes): %d/%d", __ramsize, __romsize);
			
			// Spacer
			todo.DEBUG.note("");
			
			// Load boot libraries that are available
			todo.DEBUG.note("Scanning libraries and loading classpath...");
			BootLibrary[] bootlibs = BootLibrary.initialClasspath(__rombase, __confbase);
			todo.DEBUG.note("Selecting %d libraries!", bootlibs.length);
			
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
				BootLibrary.initialClasspath(__rombase, __confbase),
				Bootstrap.loadMainClass(__confbase),
				Bootstrap.loadMainArgs(__confbase),
				Bootstrap.loadSystemProperties(__confbase));
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

