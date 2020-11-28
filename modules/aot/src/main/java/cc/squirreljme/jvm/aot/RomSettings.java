// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

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
			if (!arg.startsWith("-"))
			{
				__args.addFirst(arg);
				break;
			}
			
			// {@squirreljme.error AE07 Unknown argument. (The argument)}
			throw new IllegalArgumentException("AE07 " + arg);
		}
		
		return new RomSettings();
	}
}
