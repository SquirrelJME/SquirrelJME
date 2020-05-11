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
	 * This is executed if the execution engine is too new and would not be
	 * capable of running this ROM.
	 *
	 * @since 2020/05/10
	 */
	@SuppressWarnings("FeatureEnvy")
	private static void __advancedSquirrelJME()
	{
		// Get standard error since we cannot use other classes here
		int stdErr = Assembly.sysCallPV(SystemCallIndex.PD_OF_STDERR);
		
		// Need to be able to print without allocating any memory...
		// "This execution engine requires a newer ROM file to execute it..."
		SystemBoot.__lowPrint(stdErr, 'T', 'h', 'i', 's');
		SystemBoot.__lowPrint(stdErr, ' ', 'e', 'x', 'e');
		SystemBoot.__lowPrint(stdErr, 'c', 'u', 't', 'i');
		SystemBoot.__lowPrint(stdErr, 'o', 'n', ' ', 'e');
		SystemBoot.__lowPrint(stdErr, 'n', 'g', 'i', 'n');
		SystemBoot.__lowPrint(stdErr, 'e', ' ', 'r', 'e');
		SystemBoot.__lowPrint(stdErr, 'q', 'u', 'i', 'r');
		SystemBoot.__lowPrint(stdErr, 'e', 's', ' ', 'a');
		SystemBoot.__lowPrint(stdErr, ' ', 'n', 'e', 'w');
		SystemBoot.__lowPrint(stdErr, 'e', 'r', ' ', 'R');
		SystemBoot.__lowPrint(stdErr, 'O', 'M', ' ', 'f');
		SystemBoot.__lowPrint(stdErr, 'i', 'l', 'e', ' ');
		SystemBoot.__lowPrint(stdErr, 't', 'o', ' ', 'e');
		SystemBoot.__lowPrint(stdErr, 'x', 'e', 'c', 'u');
		SystemBoot.__lowPrint(stdErr, 't', 'e', ' ', 'i');
		SystemBoot.__lowPrint(stdErr, 't', '.', '.', '.');
		
		// End line sequence, just assume Windows endings here
		SystemBoot.__lowPrint(stdErr, '\r', '\n',
			'\r', '\n');
		
		// Try to stop the VM somehow
		Assembly.breakpoint();
		Assembly.sysCallPV(SystemCallIndex.EXIT, -14);
	}
	
	/**
	 * This is used when the version of SquirrelJME is too ancient.
	 *
	 * This method assumes that no memory system has been initialized.
	 *
	 * @since 2020/05/10
	 */
	@SuppressWarnings("FeatureEnvy")
	private static void __ancientSquirrelJME()
	{
		// Get standard error since we cannot use other classes here
		int stdErr = Assembly.sysCallPV(SystemCallIndex.PD_OF_STDERR);
		
		// This may seem complicated to print, however when this code runs
		// the allocator cannot actually initialize because the given addresses
		// are not valid...
		// "This version of SquirrelJME requires a newer execution engine..."
		SystemBoot.__lowPrint(stdErr, 'T', 'h', 'i', 's');
		SystemBoot.__lowPrint(stdErr, ' ', 'v', 'e', 'r');
		SystemBoot.__lowPrint(stdErr, 's', 'i', 'o', 'n');
		SystemBoot.__lowPrint(stdErr, ' ', 'o', 'f', ' ');
		SystemBoot.__lowPrint(stdErr, 'S', 'q', 'u', 'i');
		SystemBoot.__lowPrint(stdErr, 'r', 'r', 'e', 'l');
		SystemBoot.__lowPrint(stdErr, 'J', 'M', 'E', ' ');
		SystemBoot.__lowPrint(stdErr, 'r', 'e', 'q', 'u');
		SystemBoot.__lowPrint(stdErr, 'i', 'r', 'e', 's');
		SystemBoot.__lowPrint(stdErr, ' ', 'a', ' ', 'n');
		SystemBoot.__lowPrint(stdErr, 'e', 'w', 'e', 'r');
		SystemBoot.__lowPrint(stdErr, ' ', 'e', 'x', 'e');
		SystemBoot.__lowPrint(stdErr, 'c', 'u', 't', 'i');
		SystemBoot.__lowPrint(stdErr, 'o', 'n', ' ', 'e');
		SystemBoot.__lowPrint(stdErr, 'n', 'g', 'i', 'n');
		SystemBoot.__lowPrint(stdErr, 'e', '.', '.', '.');
		
		// End line sequence, just assume Windows endings here
		SystemBoot.__lowPrint(stdErr, '\r', '\n',
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
	@SuppressWarnings({"FeatureEnvy", "unused"})
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
	 * Prints a bunch of characters to the output.
	 *
	 * @param __pd The pipe to write to.
	 * @param __a Character A.
	 * @param __b Character B.
	 * @param __c Character C.
	 * @param __d Character D.
	 * @since 2020/05/10
	 */
	@SuppressWarnings("FeatureEnvy")
	private static void __lowPrint(int __pd, char __a, char __b, char __c,
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
		// Detect if this is a SquirrelJME that is too old and will not be
		// capable of running this ROM at all.
		if (Assembly.sysCallPV(SystemCallIndex.API_LEVEL) <
			Constants.API_LEVEL_2020_05_10)
		{
			SystemBoot.__ancientSquirrelJME();
			return;
		}
		
		// Additionally check if the execution engine is too new and it will
		// not be compatible with this ROM
		if (Assembly.sysCallPV(SystemCallIndex.CHECK_EXEC_COMPATIBILITY,
			Constants.API_LEVEL_CURRENT) == 0)
		{
			SystemBoot.__advancedSquirrelJME();
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
