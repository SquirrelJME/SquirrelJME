// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import org.gradle.internal.os.OperatingSystem;

/**
 * General path utilities.
 *
 * @since 2023/06/04
 */
public class PathUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2023/06/04
	 */
	private PathUtils()
	{
	}
	
	/**
	 * Finds a file that is in {@code $PATH}.
	 * 
	 * @param __name The name of the file
	 * @return The path to the file of the given name or {@code null}
	 * if not found.
	 * @throws NullPointerException On arguments.
	 * @since 2023/06/04
	 */
	public static Path findPath(String __name)
	{
		// Path should exist, but it might not
		String pathEnv = System.getenv("PATH");
		if (pathEnv == null)
			return null;
		
		// Search each path piece for the given executable
		for (String pathSegment : pathEnv.split(
			Pattern.quote(System.getProperty("path.separator"))))
		{
			Path fullPath = Paths.get(pathSegment).resolve(__name);
			
			// If we find it, cache it
			if (Files.isRegularFile(fullPath) && Files.isExecutable(fullPath))
				return fullPath;
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Finds an executable that is in {@code $PATH}.
	 * 
	 * @param __name The name of the executable
	 * @return The path to the executable of the given name or {@code null}
	 * if not found.
	 * @throws NullPointerException On arguments.
	 * @since 2023/06/04
	 */
	public static Path findPathExecutable(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// If we are on Windows there are multiple potential path executables
		// for actual executables
		if (OperatingSystem.current() == OperatingSystem.WINDOWS)
		{
			// Get path extensions that exist
			String pathExts = System.getenv("PathExt");
			if (pathExts == null)
				pathExts = ".EXE";
			
			// Go through each extension and try to find it
			for (String pathExt : pathExts.split(Pattern.quote(";")))
			{
				Path found = PathUtils.findPath(__name + pathExt);
				if (found != null)
					return found;
			}
		}
		
		// Otherwise just the exact name
		return PathUtils.findPath(__name);
	}
}
