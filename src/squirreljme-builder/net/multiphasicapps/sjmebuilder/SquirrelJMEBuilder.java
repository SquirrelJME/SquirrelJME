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

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class performs the actual building of SquirrelJME depending on the
 * supported targets and such.
 *
 * @since 2016/06/15
 */
public class SquirrelJMEBuilder
{
	/** The directory where JAR files are placed. */
	protected final Path jardir;
	
	/** The directory which contains source code. */
	protected final Path srcdir;
	
	/**
	 * Initializes the builder.
	 *
	 * @param __args The arguments to use for the build.
	 * @throws IllegalArgumentException If the input arguments are not valid.
	 * @since 2016/06/15
	 */
	public SquirrelJMEBuilder(String... __args)
	{
		// Must exist
		if (__args == null)
			__args = new String[0];
		
		// Extract options and their values
		Map<String, String> options = new LinkedHashMap<>();
		int n = __args.length;
		for (int i = 0; i < n; i++)
		{
			// Get
			String sw = __args[i];
			
			// Find equal sign
			int eq = sw.indexOf('=');
			
			// No sign?
			if (eq < 0)
				options.put(sw, "");
			
			// There is one
			else
				options.put(sw.substring(0, eq), sw.substring(eq + 1));
		}
		
		// {@squirreljme.cmdline jar.path=(path) The path which contains all
		// of the prebuilt JAR files.}
		this.jardir = Paths.get(Objects.toString(options.get("jar.path"),
			System.getProperty("user.dir")));
		
		// {@squirreljme.cmdline source.path=(path) The path where SquirrelJME
		// projects are placed for optional building.}
		this.srcdir = Paths.get(Objects.toString(options.get("source.path"),
			System.getProperty("user.dir")));
	}
	
	/**
	 * Builds SquirrelJME.
	 *
	 * @since 2016/06/15
	 */
	public void build()
	{
		throw new Error("TODO");
	}
}

