// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import org.gradle.internal.os.OperatingSystem;

/**
 * This class provides support for the Fossil executable, to do some tasks
 * and otherwise from within Gradle.
 *
 * @since 2020/06/24
 */
public final class FossilExe
{
	/** Cached executable. */
	@SuppressWarnings({"StaticVariableMayNotBeInitialized", "unused"})
	private static FossilExe _cached;
	
	/** The executable path. */
	private final Path exe;
	
	/**
	 * Initializes the executable reference with the given path.
	 * 
	 * @param __exe The executable path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/24
	 */
	public FossilExe(Path __exe)
		throws NullPointerException
	{
		if (__exe == null)
			throw new NullPointerException("NARG");
		
		this.exe = __exe;
	}
	
	/**
	 * Returns the executable path.
	 * 
	 * @return The executable path.
	 * @since 2020/06/25
	 */
	public final Path exePath()
	{
		return this.exe;
	}
	
	/**
	 * Attempts to locate the fossil executable.
	 * 
	 * @return The executable instance.
	 * @throws IllegalArgumentException If an executable could not be found.
	 * @since 2020/06/24
	 */
	@SuppressWarnings({"CallToSystemGetenv", 
	"StaticVariableUsedBeforeInitialization"})
	public static FossilExe instance()
		throws IllegalArgumentException
	{
		// Pre-cached already?
		FossilExe rv = FossilExe._cached;
		if (rv != null)
			return rv;
		
		// Path should exist, but it might not
		String pathEnv = System.getenv("PATH");
		if (pathEnv == null)
			throw new IllegalArgumentException("No PATH variable is set.");
		
		// The executable we are looking for
		Path exeName = Paths.get(
			(OperatingSystem.current() == OperatingSystem.WINDOWS ?
			"fossil.exe" : "fossil"));
		
		// Search each path piece for the given executable
		for (String pathSegment : pathEnv.split(
			Pattern.quote(System.getProperty("path.separator"))))
		{
			Path fullPath = Paths.get(pathSegment).resolve(exeName);
			
			// If we find it, cache it
			if (Files.isRegularFile(fullPath) && Files.isExecutable(fullPath))
			{
				rv = new FossilExe(fullPath);
				
				FossilExe._cached = rv;
				
				return rv;
			}
		}
		
		// Not found
		throw new IllegalArgumentException(
			"Could not find Fossil executable.");
	}
}
