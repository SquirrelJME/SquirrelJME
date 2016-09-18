// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap;

import java.util.ServiceLoader;
import net.multiphasicapps.squirreljme.bootstrap.base.compiler.BootCompiler;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.BootLauncher;

/**
 * This is the main entry class for the bootstrap builder.
 *
 * @since 2016/09/18
 */
public class Main
{
	/**
	 * This is the main entry point for the bootstrap builder.
	 *
	 * @param __args Program arguments.
	 * @since 2016/09/18
	 */
	public static void main(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		throw new Error("TODO");
	}
	
	/**
	 * Main entry point but one where the boot compiler and launcher can be
	 * specified.
	 *
	 * @param __bc The boot compiler.
	 * @param __bl The boot launcher.
	 * @param __args Program arguments.
	 * @since 2016/09/18
	 */
	public static void main(BootCompiler __bc, BootLauncher __bl,
		String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		throw new Error("TODO");
	}
}

