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

/**
 * This class contains settings for ROM building.
 *
 * @since 2020/11/27
 */
public final class RomSettings
{
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
				throw Debugging.todo();
			
			// Bootloader class path
			// -XbootLoaderClassPath:0
			else if (arg.startsWith("-XbootLoaderClassPath:"))
				throw Debugging.todo();
			
			// Launcher main class
			// -XlauncherMainClass:javax.microedition.midlet.__MainHandler__
			else if (arg.startsWith("-XlauncherMainClass:"))
				throw Debugging.todo();
			
			// Launcher arguments
			// -XlauncherArgs:0:cc.squirreljme.runtime.launcher.ui.MidletMain
			else if (arg.startsWith("-XlauncherArgs:"))
				throw Debugging.todo();
				
			// -XlauncherClassPath:0,6,38,24,25,44,43,27
			else if (arg.startsWith("-XlauncherClassPath:"))
				throw Debugging.todo();
			
			// {@squirreljme.error AE07 Unknown argument. (The argument)}
			else
				throw new IllegalArgumentException("AE07 " + arg);
		}
		
		return new RomSettings();
	}
}
