// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * This is the main entry point for the NARF interpreter, it handles a standard
 * {@code java}-like command interface so that it is similar to launching
 * desktop applications.
 *
 * @since 2016/04/20
 */
public class Main
{
	/** The property to use for the path separator. */
	public static final String PATH_SEPARATOR_PROPERTY =
		"path.separator";
	
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
		String spps = System.getProperty(PATH_SEPARATOR_PROPERTY);
		
		// If available, use it
		if (spps != null)
			PATH_SEPARATOR = spps;
		
		// Otherwise, assume UNIX
		else
			PATH_SEPARATOR = ":";
		
		// Big problems if it is multiple characters long
		// {@squirreljme.error NI07 The local interpreter does not support path
		// separators which are more than one character long. (The path
		// separater; The property to set)}
		if (PATH_SEPARATOR.length() != 1)
			throw new RuntimeException(String.format("NI07 %s %s",
				PATH_SEPARATOR, PATH_SEPARATOR_PROPERTY));
	}
	
	/**
	 * This is the main entry point for the NARF interpreter.
	 *
	 * @param __args Program arguments.
	 * @since 2016/04/20
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
					// {@squirreljme.error NI06 -classpath cannot be specified
					// if it or -jar has already been specified.}
					if (didcp || didjar)
						throw new IllegalArgumentException("NI06");
					
					// Eat
					hargs.pollFirst();
					
					// Get next
					String cparg = hargs.pollFirst();
					
					// {@squirreljme.error NI05 An argument is expected to
					// follow the -classpath option.}
					if (cparg == null)
						throw new IllegalArgumentException("NI05");
					
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
					// {@squirreljme.error NI04 -jar cannot be specified
					// if it or -classpath has already been specified.}
					if (didcp || didjar)
						throw new IllegalArgumentException("NI04");
					
					// Eat
					hargs.pollFirst();
					
					// Get next
					String jarf = hargs.pollFirst();
					
					// {@squirreljme.error NI03 An argument is expected to
					// follow the -jar option.}
					if (jarf == null)
						throw new IllegalArgumentException("NI03");
					
					// Single JAR only
					classpath.add(Paths.get(jarf));	
					
					// Mark
					didjar = true;
					break;
				
					// Unknown
				default:
					// {@squirreljme.error LI08 An unknown command line switch
					// was specified. (The command line switch)}
					if (cur.startsWith("-"))
						throw new IllegalArgumentException(String.format(
							"LI08 %s", cur));
					
					// Stop processing because these are normal commands now
					break __outer_loop;
			}
		}
		
		// Add the CLDC to the boot classpath always
		bootclasspath.add(Paths.get("javame-cldc-compact.jar"));
		bootclasspath.add(Paths.get("javame-cldc-full.jar"));
		
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
		
		// {@squirreljme.error NI01 The input JAR does not have a "Main-Class"
		// attribute.}
		// {@squirreljme.error NI02 No main class was specified in the
		// program arguments.}
		if (mainclass == null)
			throw new IllegalArgumentException((didjar ? "NI01" : "NI02"));
		
		// Setup the class library
		InterpreterLibrary ilib = new InterpreterLibrary(bootclasspath,
			classpath);
		
		throw new Error("TODO");
	}
}

