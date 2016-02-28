// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.hairball;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Main entry point for hairball.
 *
 * @since 2016/02/28
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/02/28
	 */
	public static void main(String... __args)
	{
		// Need two arguments, JAR output directory and the source root
		if (__args == null || __args.length < 2)
			throw new IllegalArgumentException("Usage: (Output) (Projects)");
		
		// Get locations
		Path outdir = Paths.get(Objects.<String>requireNonNull(__args[0],
			"No output directory specified."));
		Path srcdir = Paths.get(Objects.<String>requireNonNull(__args[1],
			"No source directory specified."));
		
		// Could fail
		try
		{
			// Build package list
			PackageList pl = new PackageList(outdir, srcdir);
			System.out.println(pl);
			
			throw new Error("TODO");
		}
		
		// Could not read or compile package
		catch (IOException ioe)
		{
			throw new RuntimeException(ioe);
		}
	}
}

