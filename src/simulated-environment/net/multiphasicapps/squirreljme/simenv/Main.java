// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.simenv;

import java.nio.file.Path;
import java.nio.file.Paths;
import net.multiphasicapps.sjmepackages.PackageList;

/**
 * This is the main entry point for the simulated environment.
 *
 * @since 2016/08/13
 */
public class Main
{
	/**
	 * This is the entry point for the simulated environment
	 *
	 * @param __args Program arguments.
	 * @since 2016/08/13
	 */
	public static void main(String... __args)
	{
		// Must exist
		if (__args == null)
			__args = new String[0];
		
		// Could fail
		try
		{
			// Setup the package list
			PackageList plist = new PackageList(Paths.get("."));
		
			throw new Error("TODO");
		}
		
		// {@squirrejme.error AY01 Read/write error setting up the simulated
		// environment.}
		catch (IOException e)
		{
			throw new RuntimeException("AY01", e);
		}
	}
}

