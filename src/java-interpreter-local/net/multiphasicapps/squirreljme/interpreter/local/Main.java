// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter.local;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Main entry point for the local interpreter.
 *
 * @since 2016/02/29
 */
public class Main
{
	/** The separator for paths. */
	public static final String PATH_SEPARATOR;
	
	/**
	 * Determines some details.
	 *
	 * @since 2016/02/29
	 */
	static
	{
		// Get system property
		String spps = System.getProperty("path.separator");
		
		// If available, use it
		if (spps != null)
			PATH_SEPARATOR = spps;
		
		// Otherwise, assume UNIX
		else
			PATH_SEPARATOR = ":";
		
		// Big problems if it is multiple characters long
		if (PATH_SEPARATOR.length() != 1)
			throw new RuntimeException("Cannot handle a path separator that " +
				"is multiple characters long.");
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/02/29
	 */
	public static void main(String... __args)
	{
		// Force arguments to exist
		if (__args == null)
			__args = new String[0];
		
		// Pipe arguments to be handled
		Deque<String> hargs = new LinkedList<>();
		for (int i = 0; i < __args.length; i++)
			hargs.offerLast(__args[i]);
		
		// Bootstrap class path
		boolean didbcp = false;
		Set<Path> bootclasspath = new LinkedHashSet<>();
		
		// Class path
		boolean didcp = false;
		boolean didjar = false;
		Set<Path> classpath = new LinkedHashSet<>();
		
		// Go through and handle arguments
__outer_loop:
		while (!hargs.isEmpty())
		{
			// Get current
			String cur = hargs.peekFirst();
			
			// Depends on what it is
			switch (cur)
			{
					// Class Path
				case "-cp":
				case "-classpath":
					// Only once
					if (didcp || didjar)
						throw new IllegalArgumentException("The switch " +
							"-cp or -jar has already been specified.");
					
					// Eat
					hargs.pollFirst();
					
					// Get next
					String cparg = hargs.pollFirst();
					if (cparg == null)
						throw new IllegalArgumentException("");
					
					// Split and get paths from them
					StringTokenizer st = new StringTokenizer(cparg,
						PATH_SEPARATOR);
					while (st.hasMoreTokens())
						classpath.add(Paths.get(st.nextToken()));
					
					// Mark
					didcp = true;
					break;
					
					// JAR
				case "-jar":
					// Only once
					if (didcp || didjar)
						throw new IllegalArgumentException("The switch " +
							"-cp or -jar has already been specified.");
					
					// Eat
					hargs.pollFirst();
					
					// Get next
					String jarf = hargs.pollFirst();
					if (jarf == null)
						throw new IllegalArgumentException("Expected a " +
							"JAR path following -jar.");
					
					// Single JAR only
					classpath.add(Paths.get(jarf));	
					
					// Mark
					didjar = true;
					break;
				
					// Unknown
				default:
					if (cur.startsWith("-"))
						throw new IllegalArgumentException(String.format(
							"Unknown command line switch: %s", cur));
					
					// Stop processing because these are normal commands now
					break __outer_loop;
			}
		}
		
		// Add the CLDC to the boot classpath always
		bootclasspath.add(Paths.get("javame-cldc.jar"));
		
		// Always add the current directory to the class path so that random
		// class files which are lying around work
		String pwd = System.getProperty("user.dir");
		if (pwd != null)
			classpath.add(Paths.get(pwd));
		
		// Determine the main class to use
		String mainclass;
		if (didjar)
			throw new Error("TODO");
		
		// Otherwise it is specified on the command line
		else
			mainclass = hargs.pollFirst();
		
		// If not specified, fail
		if (mainclass == null)
			throw new IllegalArgumentException("Main class not specified or " +
				"is missing in the JAR manifest.");
		
		// Setup the local interpreter engine
		LocalEngine le = new LocalEngine(bootclasspath, classpath, mainclass,
			hargs.<String>toArray(new String[hargs.size()]));
		
		// Run while it has not terminated
		while (!le.isTerminated())
			throw new Error("TODO");
	}
}

