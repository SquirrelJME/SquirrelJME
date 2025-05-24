// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * Various utilities.
 *
 * @since 2025/05/23
 */
public class Utils
{
	/**
	 * Returns the base name of the file.
	 *
	 * @param __name The input name.
	 * @return The base name of the file.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/23
	 */
	public static String baseName(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		int ls = __name.lastIndexOf('/');
		if (ls < 0)
			return __name;
		return __name.substring(ls + 1);
	}
	
	/**
	 * Finds the executable path
	 *
	 * @param __exeName The executable base name.
	 * @return The resultant path or {@code null} if there is none.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/23
	 */
	public static Path findExecutable(String __exeName)
		throws NullPointerException
	{
		if (__exeName == null)
			throw new NullPointerException("NARG");
		
		// Windows or not?
		String exeName;
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			exeName = __exeName + ".exe";
		else
			exeName = __exeName;
		
		// Use system PATH
		String paths = System.getenv("PATH");
		if (paths != null)
			for (String path : paths.split(Pattern.quote(File.pathSeparator)))
			{
				Path maybe = Paths.get(path, exeName);
				if (Files.exists(maybe) && Files.isExecutable(maybe))
					return maybe.toAbsolutePath();
			}
		
		// Not found
		return null;
	}
}
