// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.multiphasicapps.sjmepackages.PackageList;

/**
 * This is the main entry point for the builder.
 *
 * @since 2016/06/24
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @since 2016/06/24
	 */
	public static void main(String... __args)
	{
		// Load the package list
		PackageList plist;
		try
		{
			plist = new PackageList(Paths.get(System.getProperty("user.dir")),
				null);
		}
		
		// {@squirreljme.error DW01 Failed to load the package list.}
		catch (IOException e)
		{
			throw new RuntimeException("DW01", e);
		}
		
		throw new Error("TODO");
	}
}

