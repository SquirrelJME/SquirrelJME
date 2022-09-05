// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import java.util.Deque;

/**
 * This class contains settings for compilation.
 *
 * @since 2020/11/22
 */
public final class CompileSettings
{
	/** Is this a bootloader? */
	public final boolean isBootLoader;
	
	/** The variant used for compilation. */
	public final String variant;
	
	/**
	 * Initializes the compilation settings.
	 * 
	 * @param __isBootLoader Is this a bootloader?
	 * @param __variant The variant used for compilation.
	 * @since 2020/11/23
	 */
	public CompileSettings(boolean __isBootLoader, String __variant)
	{
		this.isBootLoader = __isBootLoader;
		this.variant = __variant;
	}
	
	/**
	 * Parses compile settings for the compilation step.
	 * 
	 * @param __args The arguments to parse.
	 * @return The resultant settings.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/23
	 */
	public static CompileSettings parse(Deque<String> __args)
		throws NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Possible settings
		boolean isBootLoader = false;
		String variant = null;
		
		// Parse settings
		while (!__args.isEmpty())
		{
			String arg = __args.removeFirst();
			
			// Does this have a value?
			int colonDx = arg.indexOf(':');
			String value;
			if (colonDx < 0)
				value = null;
			else
			{
				value = arg.substring(colonDx + 1);
				arg = arg.substring(0, colonDx);
			}
			
			// Which argument is this?
			switch (arg)
			{
					// Is this a bootloader?
				case "-boot":
					isBootLoader = true;
					break;
					
					// Variant used?
				case "-variant":
					variant = value;
					break;
				
					// {@squirreljme.error AE06 Unknown compilation setting.
					// (The argument)}
				default:
					throw new IllegalArgumentException("AE06 " + arg);
			}
		}
		
		// Initialize final settings
		return new CompileSettings(isBootLoader, variant);
	}
}
