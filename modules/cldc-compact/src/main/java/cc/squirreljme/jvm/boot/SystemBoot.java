// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot;

import cc.squirreljme.jvm.SystemCall;
import cc.squirreljme.jvm.VirtualProcess;
import cc.squirreljme.jvm.memory.ReadableAssemblyMemory;

/**
 * Boots the SquirrelJME virtual machine system.
 *
 * @since 2020/03/26
 */
public final class SystemBoot
{
	/** The used configuration reader. */
	@SuppressWarnings("StaticVariableMayNotBeInitialized")
	private static ConfigReader _config;
	
	/**
	 * Returns the configuration reader.
	 *
	 * @return The configuration reader.
	 * @since 2020/05/03
	 */
	@SuppressWarnings("StaticVariableUsedBeforeInitialization")
	public static ConfigReader config()
	{
		return SystemBoot._config;
	}
	
	/**
	 * System boot entry point.
	 *
	 * @param __ramAddr The RAM address.
	 * @param __ramLen The size of RAM.
	 * @param __configAddr The configuration address.
	 * @param __configLen The configuration length.
	 * @since 2020/03/26
	 */
	@SuppressWarnings({"unused"})
	static void __sysBoot(long __ramAddr, int __ramLen,
		long __configAddr, int __configLen)
	{
		// Initialize the links in RAM
		Allocator.__initRamLinks(__ramAddr, __ramLen);
		
		// Setup the configuration reader to obtain our entry point info
		ConfigReader config = new ConfigReader(
			new ReadableAssemblyMemory(__configAddr, __configLen));
		SystemBoot._config = config;
		
		// Spawn our primary process and initialize it
		VirtualProcess primary = VirtualProcess.spawn();
		
		// Start the process
		primary.start();
		
		// Wait for the process to finish execution
		int exitCode;
		for (;;)
			try
			{
				exitCode = primary.waitForExit();
				break;
			}
			catch (InterruptedException e)
			{
				// Ignore ...
			}
		
		// Now exit with the code
		SystemCall.exit(exitCode);
	}
}
