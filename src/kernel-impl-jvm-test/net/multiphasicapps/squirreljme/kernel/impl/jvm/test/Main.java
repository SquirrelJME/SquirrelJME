// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassLoaderNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.terp.TerpInterpreter;
import net.multiphasicapps.squirreljme.terp.pure.PureInterpreter;

/**
 * This is the main entry point for the JVM based test kernel for running the
 * interpreter.
 *
 * @since 2016/05/27
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
		
		// Otherwise, assume UTerpX
		else
			PATH_SEPARATOR = ":";
		
		// Big problems if it is multiple characters long
		// {@squirreljme.error AN07 The local interpreter does not support path
		// separators which are more than one character long. (The path
		// separater; The property to set)}
		if (PATH_SEPARATOR.length() != 1)
			throw new RuntimeException(String.format("AN07 %s %s",
				PATH_SEPARATOR, PATH_SEPARATOR_PROPERTY));
	}
	
	/**
	 * This is the main entry point for the NARF interpreter.
	 *
	 * @param __args Program arguments.
	 * @since 2016/04/20
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// Initialization may fail
		JVMTestKernel jtk = null;
		try
		{
			// Setup interpreter
			TerpInterpreter terp = new PureInterpreter();
		
			// Create test kernel
			jtk = new JVMTestKernel(terp);
		
			// Block until all workers are terminated
			for (;;)
			{
				// Kernel loop
				try
				{
					jtk.untilProcessless();
		
					// Would normally terminate
					return;
				}
		
				// Interrupted, yield and retry
				catch (InterruptedException e)
				{
					Thread.yield();
				}
			}
		}
		
		// Fall out of main
		catch (Throwable t)
		{
			// Print the stack trace
			t.printStackTrace(System.err);
			
			// Attempt to quit the kernel
			jtk.quitKernel();
			
			// Rethrow if quit failed
			throw t;
		}
		
		/*
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
					// {@squirreljme.error AN06 -classpath cannot be specified
					// if it or -jar has already been specified.}
					if (didcp || didjar)
						throw new IllegalArgumentException("AN06");
					
					// Eat
					hargs.pollFirst();
					
					// Get next
					String cparg = hargs.pollFirst();
					
					// {@squirreljme.error AN05 An argument is expected to
					// follow the -classpath option.}
					if (cparg == null)
						throw new IllegalArgumentException("AN05");
					
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
					// {@squirreljme.error AN04 -jar cannot be specified
					// if it or -classpath has already been specified.}
					if (didcp || didjar)
						throw new IllegalArgumentException("AN04");
					
					// Eat
					hargs.pollFirst();
					
					// Get next
					String jarf = hargs.pollFirst();
					
					// {@squirreljme.error AN03 An argument is expected to
					// follow the -jar option.}
					if (jarf == null)
						throw new IllegalArgumentException("AN03");
					
					// Single JAR only
					classpath.add(Paths.get(jarf));	
					
					// Mark
					didjar = true;
					break;
				
					// Unknown
				default:
					// {@squirreljme.error AN08 An unknown command line switch
					// was specified. (The command line switch)}
					if (cur.startsWith("-"))
						throw new IllegalArgumentException(String.format(
							"AN08 %s", cur));
					
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
		
		// {@squirreljme.error AN01 The input JAR does not have a "Main-Class"
		// attribute.}
		// {@squirreljme.error AN02 No main class was specified in the
		// program arguments.}
		if (mainclass == null)
			throw new IllegalArgumentException((didjar ? "AN01" : "AN02"));
		
		// Setup the class library
		TerpLibrary ilib = new TerpLibrary(bootclasspath, classpath);
		
		// Setup the interpreter core
		TerpCore ic = new TerpCore(ilib, ClassLoaderNameSymbol.of(mainclass),
			hargs.<String>toArray(new String[hargs.size()]));
		
		// If the VM is still running, yield the current thread. Although they
		// say to never use yield, it is very possible that this environment
		// can be running on a system which has cooperative multi-tasking. If
		// the main thread never yields then essentially the VM will freeze
		// solid.
		while (ic.isRunning())
			Thread.yield();
		*/
	}
}

