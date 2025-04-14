// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	/** Is this a boot loader? */
	public final boolean isBootLoader;
	
	/**
	 * Initializes the compilation settings.
	 * 
	 * @param __isBootLoader Is this a boot loader?
	 * @since 2020/11/23
	 */
	public CompileSettings(boolean __isBootLoader)
	{
		this.isBootLoader = __isBootLoader;
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
		
		// Parse settings
		while (!__args.isEmpty())
		{
			String arg = __args.removeFirst();
			
			switch (arg)
			{
					// Is this a bootloader?
				case "-boot":
					isBootLoader = true;
					break;
				
					/* {@squirreljme.error AE06 Unknown compilation setting.
					(The argument)} */
				default:
					throw new IllegalArgumentException("AE06 " + arg);
			}
		}
		
		// Initialize final settings
		return new CompileSettings(isBootLoader);
	}
}
