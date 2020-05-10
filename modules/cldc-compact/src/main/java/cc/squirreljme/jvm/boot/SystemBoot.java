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
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.SystemCall;
import cc.squirreljme.jvm.SystemCallIndex;
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
	 * Prints a bunch of characters to the output.
	 *
	 * @param __pd The pipe to write to.
	 * @param __a Character A.
	 * @param __b Character B.
	 * @param __c Character C.
	 * @param __d Character D.
	 * @since 2020/05/10
	 */
	private static void __ancientPrint(int __pd, char __a, char __b, char __c,
		char __d)
	{
		Assembly.sysCallP(SystemCallIndex.PD_WRITE_BYTE, __pd,
			(__a >= 0x7F ? '?' : __a));
		Assembly.sysCallP(SystemCallIndex.PD_WRITE_BYTE, __pd,
			(__b >= 0x7F ? '?' : __b));
		Assembly.sysCallP(SystemCallIndex.PD_WRITE_BYTE, __pd,
			(__c >= 0x7F ? '?' : __c));
		Assembly.sysCallP(SystemCallIndex.PD_WRITE_BYTE, __pd,
			(__d >= 0x7F ? '?' : __d));
	}
	
	/**
	 * This is used when the version of SquirrelJME is too ancient.
	 *
	 * This method assumes that no memory system has been initialized.
	 *
	 * @since 2020/05/10
	 */
	private static void __ancientSquirrelJME()
	{
		// We need to get stderr's output so we can print a message saying
		// that this version is too old and is broken
		int stdErr = Assembly.sysCallPV(SystemCallIndex.PD_OF_STDERR);
		
		// This may seem complicated to print, however when this code runs
		// the allocator cannot actually initialize because the given addresses
		// are not valid...
		// "This version of SquirrelJME requires a newer execution engine..."
		SystemBoot.__ancientPrint(stdErr, 'T', 'h', 'i', 's');
		SystemBoot.__ancientPrint(stdErr, ' ', 'v', 'e', 'r');
		SystemBoot.__ancientPrint(stdErr, 's', 'i', 'o', 'n');
		SystemBoot.__ancientPrint(stdErr, ' ', 'o', 'f', ' ');
		SystemBoot.__ancientPrint(stdErr, 'S', 'q', 'u', 'i');
		SystemBoot.__ancientPrint(stdErr, 'r', 'r', 'e', 'l');
		SystemBoot.__ancientPrint(stdErr, 'J', 'M', 'E', ' ');
		SystemBoot.__ancientPrint(stdErr, 'r', 'e', 'q', 'u');
		SystemBoot.__ancientPrint(stdErr, 'i', 'r', 'e', 's');
		SystemBoot.__ancientPrint(stdErr, ' ', 'a', ' ', 'n');
		SystemBoot.__ancientPrint(stdErr, 'e', 'w', 'e', 'r');
		SystemBoot.__ancientPrint(stdErr, ' ', 'e', 'x', 'e');
		SystemBoot.__ancientPrint(stdErr, 'c', 'u', 't', 'i');
		SystemBoot.__ancientPrint(stdErr, 'o', 'n', ' ', 'e');
		SystemBoot.__ancientPrint(stdErr, 'n', 'g', 'i', 'n');
		SystemBoot.__ancientPrint(stdErr, 'e', '.', '.', '.');
		
		// End line sequence, just assume Windows endings here
		SystemBoot.__ancientPrint(stdErr, '\r', '\n',
			'\r', '\n');
		
		// Try to stop the VM somehow
		Assembly.breakpoint();
		Assembly.sysCallPV(SystemCallIndex.EXIT, -13);
	}
	
	/**
	 * Performs the actual boot process.
	 *
	 * @param __ramAddr The RAM Address.
	 * @param __ramLen The length of RAM.
	 * @param __config The configuration data.
	 * @since 2020/05/10
	 */
	static void __boot(long __ramAddr, int __ramLen, ConfigReader __config)
	{
		// Store configuration for later
		SystemBoot._config = __config;
		
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
		if (Assembly.sysCallPV(SystemCallIndex.API_LEVEL) <
			Constants.API_LEVEL_2020_05_10)
		{
			SystemBoot.__ancientSquirrelJME();
			return;
		}
		
		// Initialize the links in RAM
		Allocator.__initRamLinks(__ramAddr, __ramLen);
		
		// Setup the configuration reader to obtain our entry point info
		ConfigReader config = new ConfigReader(
			new ReadableAssemblyMemory(__configAddr, __configLen));
		
		// Forward to standard boot process
		SystemBoot.__boot(__ramAddr, __ramLen, config);
	}
}
