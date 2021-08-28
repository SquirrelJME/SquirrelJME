// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Deque;
import java.util.List;

/**
 * This class contains settings for ROM building.
 *
 * @since 2020/11/27
 */
public final class RomSettings
{
	/** The loader main class. */
	public final String bootLoaderMainClass;
	
	/** The class path indexes for the boot loader. */
	public final List<Integer> bootLoaderClassPath;
	
	/** The main launcher class. */
	public final String launcherMainClass;
	
	/** Arguments to start the launcher. */
	public final List<String> launcherArgs;
	
	/** The class path indexes for the launcher. */
	public final List<Integer> launcherClassPath;
	
	/**
	 * Initializes the ROM settings.
	 * 
	 * @param __bootLoaderMainClass The boot loader main class. 
	 * @param __bootLoaderClassPath The boot loader class path.
	 * @param __launcherMainClass The launcher main class.
	 * @param __launcherArgs The arguments to the launcher.
	 * @param __launcherClassPath The launcher class path.
	 * @since 2021/08/28
	 */
	private RomSettings(String __bootLoaderMainClass,
		List<Integer> __bootLoaderClassPath, String __launcherMainClass,
		List<String> __launcherArgs, List<Integer> __launcherClassPath)
	{
		this.bootLoaderMainClass = __bootLoaderMainClass;
		this.bootLoaderClassPath = __bootLoaderClassPath;
		this.launcherMainClass = __launcherMainClass;
		this.launcherArgs = __launcherArgs;
		this.launcherClassPath = __launcherClassPath;
	}
	
	/**
	 * Parses the ROM settings.
	 * 
	 * @param __args The arguments to parse.
	 * @return The ROM settings.
	 * @throws IllegalArgumentException If the settings are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/27
	 */
	public static RomSettings parse(Deque<String> __args)
		throws IllegalArgumentException, NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Arguments to fill
		String bootLoaderMainClass = null;
		List<Integer> bootLoaderClassPath = null;
		String launcherMainClass = null;
		List<String> launcherArgs = null;
		List<Integer> launcherClassPath = null;
		
		// Handle arguments
		while (!__args.isEmpty())
		{
			String arg = __args.removeFirst();
			
			// End of arguments? put it back and handle later
			if (!arg.startsWith("-") || arg.equals("--"))
			{
				__args.addFirst(arg);
				break;
			}
			
			// Arguments are in the following format:
			
			// Bootloader main class
			// -XbootLoaderMainClass:cc.squirreljme.jvm.summercoat.Bootstrap
			if (arg.startsWith("-XbootLoaderMainClass:"))
				bootLoaderMainClass = RomSettings.__parseString(arg);
			
			// Bootloader class path
			// -XbootLoaderClassPath:0
			else if (arg.startsWith("-XbootLoaderClassPath:"))
				bootLoaderClassPath = RomSettings.__parseClassPath(arg);
			
			// Launcher main class
			// -XlauncherMainClass:javax.microedition.midlet.__MainHandler__
			else if (arg.startsWith("-XlauncherMainClass:"))
				launcherMainClass = RomSettings.__parseString(arg);
			
			// Launcher arguments
			// -XlauncherArgs:0:cc.squirreljme.runtime.launcher.ui.MidletMain
			else if (arg.startsWith("-XlauncherArgs:"))
				launcherArgs = RomSettings.__parseArgs(launcherArgs, arg);
				
			// -XlauncherClassPath:0,6,38,24,25,44,43,27
			else if (arg.startsWith("-XlauncherClassPath:"))
				launcherClassPath = RomSettings.__parseClassPath(arg);
			
			// {@squirreljme.error AE07 Unknown argument. (The argument)}
			else
				throw new IllegalArgumentException("AE07 " + arg);
		}
		
		return new RomSettings(bootLoaderMainClass,
			RomSettings.<Integer>__protect(bootLoaderClassPath),
			launcherMainClass,
			RomSettings.<String>__protect(launcherArgs),
			RomSettings.<Integer>__protect(launcherClassPath));
	}
	
	/**
	 * Parses arguments.
	 * 
	 * @param __out The output list.
	 * @param __arg The input argument.
	 * @return A new list or {@code __out}.
	 * @since 2021/08/28
	 */
	private static List<String> __parseArgs(List<String> __out, String __arg)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Parses class path indexes.
	 * 
	 * @param __arg The input argument.
	 * @return The integer index list.
	 * @since 2021/08/28
	 */
	private static List<Integer> __parseClassPath(String __arg)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Parses a single string.
	 * 
	 * @param __arg The input argument.
	 * @return The single string.
	 * @since 2021/08/28
	 */
	private static String __parseString(String __arg)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Protectes the given list.
	 * 
	 * @param <T> The type of list to protect.
	 * @param __list The list to protect.
	 * @return The protected list.
	 * @since 2021/08/28
	 */
	private static <T> List<T> __protect(List<T> __list)
	{
		throw Debugging.todo();
	}
}
