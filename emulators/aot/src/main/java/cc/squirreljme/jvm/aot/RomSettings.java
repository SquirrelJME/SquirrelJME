// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

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
		String clutterLevel = null;
		
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
			
			/* {@squirreljme.error AE07 Unknown argument. (The argument)} */
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
	 * Example: {@code -Xsquirrels:0:cute}.
	 * 
	 * @param __out The output list.
	 * @param __arg The input argument.
	 * @return A new list or {@code __out}.
	 * @throws NullPointerException If {@code __arg} is {@code null}.
	 * @since 2021/08/28
	 */
	private static List<String> __parseArgs(List<String> __out, String __arg)
		throws NullPointerException
	{
		if (__arg == null)
			throw new NullPointerException("NARG");
			
		/* {@squirreljme.error AE0c Missing colon in argument. (The argument)} */
		int col = __arg.indexOf(':');
		if (col < 0)
			throw new IllegalArgumentException("AE0c " + __arg);
			
			
		/* {@squirreljme.error AE0d Missing second colon in argument.
		(The argument)} */
		int sec = __arg.indexOf(':', col + 1);
		if (sec < 0)
			throw new IllegalArgumentException("AE0d " + __arg);
		
		// Parse the index value
		int index;
		try
		{
			/* {@squirreljme.error AE0g Index refers to a negative position.
			(The argument)} */
			index = Integer.parseInt(__arg.substring(col + 1, sec), 10);
			if (index < 0)
				throw new IllegalArgumentException("AE0g " + __arg);
		}
		catch (NumberFormatException e)
		{
			/* {@squirreljme.error AE0e Could not parse number index.
			(The argument)} */
			throw new IllegalArgumentException("AE0e " + __arg);
		}
		
		// Get the string to be stored
		String value = __arg.substring(sec + 1);
		
		// If missing, make sure it gets created
		if (__out == null)
			__out = new ArrayList<>();
		
		// Fill with empty arguments until it is hit
		while (__out.size() <= index)
			__out.add("");
		
		// Set the value, since we could be replacing it!
		__out.set(index, value);
		
		return __out;
	}
	
	/**
	 * Parses class path indexes.
	 * 
	 * Example: {@code -Xsquirrels:1,2,3,4}.
	 * 
	 * @param __arg The input argument.
	 * @return The integer index list.
	 * @throws NullPointerException If {@code __arg} is {@code null}.
	 * @since 2021/08/28
	 */
	private static List<Integer> __parseClassPath(String __arg)
		throws NullPointerException
	{
		if (__arg == null)
			throw new NullPointerException("NARG");
			
		/* {@squirreljme.error AE0a Missing colon in argument. (The argument)} */
		int col = __arg.indexOf(':');
		if (col < 0)
			throw new IllegalArgumentException("AE0a " + __arg);
		
		// Decode the integer list
		List<Integer> result = new ArrayList<>();
		for (int i = col + 1, n = __arg.length(); i < n;)
		{
			int seq = __arg.indexOf(',', i);
			if (seq < 0)
				seq = n;
			
			// Parse it and add
			try
			{
				/* {@squirreljme.error AE0f Index refers to a negative
				position. (The argument)} */
				int index = Integer.parseInt(__arg.substring(i, seq), 10);
				if (index < 0)
					throw new IllegalArgumentException("AE0f " + __arg);
				
				result.add(index);
			}
			catch (NumberFormatException e)
			{
				/* {@squirreljme.error AE0b Could not parse integer value
				as it was not valid. (The argument)} */
				throw new IllegalArgumentException("AE0b " + __arg);
			}
			
			// Skip to the next sequence
			i = seq + 1;
		}
		
		return UnmodifiableList.of(result);
	}
	
	/**
	 * Parses a single string.
	 * 
	 * Example: {@code -Xsquirrels:cute}.
	 * 
	 * @param __arg The input argument.
	 * @return The single string.
	 * @throws NullPointerException If {@code __arg} is {@code null}.
	 * @since 2021/08/28
	 */
	private static String __parseString(String __arg)
		throws NullPointerException
	{
		if (__arg == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error AE09 Missing colon in argument. (The argument)} */
		int col = __arg.indexOf(':');
		if (col < 0)
			throw new IllegalArgumentException("AE09 " + __arg);
		
		return __arg.substring(col + 1);
	}
	
	/**
	 * Protects the given list.
	 * 
	 * @param <T> The type of list to protect.
	 * @param __list The list to protect.
	 * @return The protected list.
	 * @since 2021/08/28
	 */
	private static <T> List<T> __protect(List<T> __list)
	{
		if (__list == null)
			return null;
		
		return UnmodifiableList.of(__list);
	}
}
