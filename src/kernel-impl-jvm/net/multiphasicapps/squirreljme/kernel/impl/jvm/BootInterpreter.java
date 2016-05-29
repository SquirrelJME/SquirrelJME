// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassLoaderNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodFlags;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;
import net.multiphasicapps.squirreljme.classpath.jar.fs.FSJarClassUnit;
import net.multiphasicapps.squirreljme.classpath.jar.fs.FSJarClassUnitProvider;
import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This is the main entry point for the JVM based test kernel for running the
 * interpreter.
 *
 * @since 2016/05/27
 */
public class BootInterpreter
{
	/** The property to use for the path separator. */
	public static final String PATH_SEPARATOR_PROPERTY =
		"path.separator";
	
	/** The separator for paths. */
	public static final String PATH_SEPARATOR;
	
	/** The default interpreter core. */
	public static final String DEFAULT_INTERPRETER =
		"net.multiphasicapps.squirreljme.terp.std.StandardInterpreter";
	
	/** The deterministic interpreter. */
	public static final String RR_INTERPRETER =
		"net.multiphasicapps.squirreljme.terp.rr.RRInterpreter";
	
	/** X options which were handled by the interpreter. */
	protected final Map<String, String> xoptions =
		new LinkedHashMap<>();
	
	/** The claspath which is associated with the interpreter. */
	protected final ClassPath classpath;
	
	/** The main class to use. */
	protected final CIClass maincl;
	
	/** The main entry method. */
	protected final CIMethod mainentry;
	
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
		
		// Otherwise, assume UNIX line separators
		else
			PATH_SEPARATOR = ":";
	}
	
	/**
	 * This initializes the information that is needed to start a guest
	 * virtual such as handling its options and its class path.
	 *
	 * @param __args Arguments to the virtual machine.
	 * @sicne 2016/05/29
	 */
	public BootInterpreter(String... __args)
	{
		// Put all arguments into the queue
		Deque<String> args = new LinkedList<>();
		if (__args != null)
			for (String a : __args)
				args.offerLast(a);
		
		// Loaded classpath
		boolean didcp = false;
		Set<String> cp = new LinkedHashSet<>();
		
		// If these JARs exist, always add them
		for (String s : new String[]{"javame-cldc-compact.jar",
			"javame-cldc-full.jar"})
			if (Files.exists(Paths.get(s)))
				cp.add(s);
		
		// Program main and arguments to send to main
		ClassLoaderNameSymbol pmain = null;
		List<String> pargs = new LinkedList<>();
		
		// X options passed to the virtual machine (possible that the
		// interpreter would use these).
		Map<String, String> xoptions = this.xoptions;
		
		// Handle all arguments
		while (!args.isEmpty())
		{
			// Get next argument
			String arg = args.removeFirst();
			
			// Load classes from JAR file
			if (arg.equals("-jar"))
			{
				// {@squirreljme.error BC02 -jar cannot be specified with
				// -cp or -classpath.}
				if (didcp)
					throw new IllegalArgumentException("BC02");	
				
				if (true)
					throw new Error("TODO");
				
				// Copy program arguments
				while (!args.isEmpty())
					pargs.add(args.removeFirst());
			}
			
			// Command line switch
			else if (arg.startsWith("-"))
			{
				// {@squirreljme.error BC06 Ignoring unknown -J option.}
				boolean isx;
				if ((isx = arg.startsWith("-X")) || arg.startsWith("-J"))
				{
					// X option, add to X option mapping
					if (isx)
					{
						// Has equal sign?
						int eq = arg.indexOf('=');
						if (eq >= 0)
							xoptions.put(arg.substring(1, eq),
								arg.substring(eq + 1));
						
						// Does not
						else
							xoptions.put(arg.substring(1), "");	
					}
					
					// Unknown -J option
					else
						System.err.printf("BC06 %s", arg);
				}
				
				// Class path specified
				else if (arg.equals("-cp") || arg.equals("-classpath"))
				{
					// {@squirreljme.error BC03 -cp or -classpath has already
					// been specified on the command line.}
					if (didcp)
						throw new IllegalArgumentException("BC03");
					
					// Only once
					didcp = true;
					
					// {@squirreljme.error BC05 Expected an argument to follow
					// -cp or -classpath.}
					String parse = args.removeFirst();
					if (parse == null)
						throw new IllegalArgumentException("BC05");
					
					// Read all path fragments
					int n = parse.length();
					for (int i = 0; i < n;)
					{
						// Find the next instance of the path separator
						int ps = parse.indexOf(PATH_SEPARATOR, i);
						
						// This is the last set?
						if (ps < 0)
						{
							String v = parse.substring(i);
							if (v.length() > 0)
								cp.add(parse.substring(i));
							break;
						}
						
						// Add fragment
						String v = parse.substring(i, ps);
						if (v.length() > 0)
							cp.add(v);
						
						// Skip to next fragment
						i = ps + PATH_SEPARATOR.length();
					}
				}
				
				// {@squirreljme.error BC01 Unknown command line switch. (The
				// command line switch)}
				else
					throw new IllegalArgumentException(String.format("BC01 %s",
						arg));
			}
			
			// BootInterpreter class followed by arguments
			else
			{
				// Setup main class
				pmain = ClassLoaderNameSymbol.of(arg);
				
				// Copy program arguments
				while (!args.isEmpty())
					pargs.add(args.removeFirst());
			}
		}
		
		// {@squirreljme.error BC04 No main class was specified.}
		if (pmain == null)
			throw new IllegalArgumentException("BC04");
		
		// Load JAR classunit providers with all of the given 
		int numcp = cp.size();
		ClassUnit[] units = new ClassUnit[numcp];
		Iterator<String> cpit = cp.iterator();
		for (int i = 0; i < numcp; i++)
			units[i] = new FSJarClassUnit(Paths.get(cpit.next()));
		cpit = null;
		
		// Setup the class path
		ClassPath classpath = new ClassPath(units);
		this.classpath = classpath;
		
		// Locate the main class
		CIClass maincl = classpath.locateClass(pmain.asClassName());
		this.maincl = maincl;
		
		// {@squirreljme.error BC07 The main class was not found in the class
		// path to be used for execution. (The main class)}
		if (maincl == null)
			throw new IllegalArgumentException(String.format("BC07 %s",
				pmain));
		
		// Find the main method in the given class
		CIMethod mainentry = maincl.methods().get(new CIMethodID(
			IdentifierSymbol.of("main"),
			MethodSymbol.of("([Ljava/lang/String;)V")));
		this.mainentry = mainentry;
		
		// {@squirreljme.error BC08 The main method in the main class does not
		// exist. (The main class)}
		if (mainentry == null)
			throw new IllegalArgumentException(String.format("BC08 %s",
				pmain));
		
		// {@squirreljme.error BC09 The main method in the main class exists
		// however it is not static. (The main class)}
		if (!mainentry.flags().isStatic())
			throw new IllegalArgumentException(String.format("BC09 %s",
				pmain));
	}
	
	/**
	 * Returns the X options which are used by the boot interpreter.
	 *
	 * @return The mapping of X options.
	 * @since 2016/05/29
	 */
	public Map<String, String> xOptions()
	{
		return this.xoptions;
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
		// {@squirreljme.property net.multiphasicapps.squirreljme.interpreter
		// This is the class which should be used as the interpreter for the
		// code which runs in the JVM based kernel.}
		String useterp = System.getProperty(
			"net.multiphasicapps.squirreljme.interpreter");
		if (useterp == null)
			useterp = DEFAULT_INTERPRETER;
		else if (useterp.equals("rerecord"))
			useterp = RR_INTERPRETER;
		
		// Setup the boot interpreter
		BootInterpreter bi = new BootInterpreter(__args);
		
		// Choose another interpreter core?
		Map<String, String> xoptions = bi.xOptions();
		String altterp = xoptions.get("squirreljme-interpreter");
		if (altterp != null)
			if (altterp.equals("rerecord"))
				useterp = RR_INTERPRETER;
			else
				useterp = altterp;
		
		// Create an instance of the interpreter
		Interpreter terp;
		try
		{
			// Find it
			Class<?> terpcl = Class.forName(useterp);
			
			// Initialize it
			terp = (Interpreter)terpcl.newInstance();
		}
		
		// {@squirreljme.error BC0a Could not initialize the interpreter.
		catch (ClassCastException|ClassNotFoundException|
			IllegalAccessException|InstantiationException e)
		{
			throw new RuntimeException("BC0a", e);
		}
		
		// Handle X options in the interpreter, if applicable
		terp.handleXOptions(xoptions);
		
		// Setup kernel using the given interpreter
		JVMKernel kernel = new JVMKernel(terp);
		
		// Run kernel cycles
		for (;; Thread.yield())
			kernel.runCycle();
	}
}

