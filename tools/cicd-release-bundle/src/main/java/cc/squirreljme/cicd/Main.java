// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Main entry point.
 *
 * @since 2024/10/04
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On read/write errors.
	 * @since 2024/10/04
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Needs to have something
		if (__args == null || __args.length < 1)
			throw new IllegalArgumentException(
				"Usage: [version] [task=output...]");
		
		// Get the SquirrelJME version
		String version = __args[0];
		
		// Upload files into the un-versioned space
		// romNanoCoatRelease=/home/.../squirreljme.jar
		FossilCommand fossil = FossilCommand.instance();
		if (fossil != null)
			for (String arg : Arrays.asList(__args).subList(1, __args.length))
			{
				int eq = arg.indexOf('=');
				if (eq < 0)
					continue;
				
				// Split task name and the target file
				String name = arg.substring(0, eq);
				Path path = Paths.get(arg.substring(eq + 1));
				
				// Determine target name
				String target = Main.uvTarget(version, name);
				
				// Store into un-versioned space
				fossil.add(path, target);
			}
	}
	
	/**
	 * Determines the un-versioned space target.
	 *
	 * @param __version The version of SquirrelJME.
	 * @param __name The task name.
	 * @return The resultant target.
	 * @throws IllegalArgumentException If the target is unknown.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/05
	 */
	public static String uvTarget(String __version, String __name)
		throws IllegalArgumentException, NullPointerException
	{
		if (__version == null || __name == null)
			throw new NullPointerException("NARG");
		
		switch (__name)
		{
			case "romNanoCoatRelease":
				return String.format("unstable/squirreljme-%s-fast.jar",
					__version);
				
			case "romNanoCoatDebug":
				return String.format("unstable/squirreljme-%s-slow.jar",
					__version);
				
			case "romTestNanoCoatDebug":
				return String.format("unstable/squirreljme-%s-slow-test.jar",
					__version);
			
			default:
				throw new IllegalArgumentException(
					"Unknown target: " + __name);
		}
	}
}
