// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.Map;
import java.util.Objects;
import javax.lang.model.SourceVersion;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * This is a simple and basic build system.
 *
 * @since 2016/03/21
 */
public class Build
{
	/** Project root directory. */
	public static final Path PROJECT_ROOT;
	
	/** This is a cached set of known projects. */
	protected static final Map<String, Project> projects =
		new HashMap<>();
	
	/** Build JARs with no compression? */
	private static volatile boolean _nocompression;
	
	/**
	 * Obtain the project root.
	 *
	 * @since 2016/03/21
	 */
	static
	{
		// Get the root and make sure it exists
		PROJECT_ROOT = Paths.get(Objects.<String>requireNonNull(
			System.getProperty("project.root"),
			"The system property `project.root` was not specified."));
	}
	
	/**
	 * Builds the given program.
	 *
	 * @param __p The program to build.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	public static void build(String __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("No program specified for build.");
		
		// Use the system's Java compiler
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		if (javac == null)
			throw new NullPointerException("Your run-time does not have a " +
				"Java compiler available.");
		
		// Must support Java 7
		if (!javac.getSourceVersions().contains(SourceVersion.RELEASE_7))
			throw new IllegalStateException("You have a Java compiler " +
				"available in your run-time, however it does not support " +
				"Java 7.");
		
		// Get the project to target
		Project proj = project(__p);
		
		throw new Error("TODO");
	}
	
	/**
	 * Launches the given program.
	 *
	 * @param __args The program to launch and its arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	public static void launch(Deque<String> __args)
		throws NullPointerException
	{
		// Check
		if (__args == null)
			throw new NullPointerException();
		
		// Get the project to launch
		String project = __args.pollFirst();
		if (project == null)
			throw new NullPointerException("No project specified.");
		
		// Build it
		main("build", project);
		
		// Setup for launching
		throw new Error("TODO");
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/03/21
	 */
	public static void main(String... __args)
	{
		// Need commands?
		if (__args == null)
			__args = new String[0];
		
		// Fill in arguments to a queue
		Deque<String> args = new LinkedList<>(Arrays.<String>asList(__args));
		
		// If the first argument is -0, then disable ZIP compression
		if ("-0".equals(args.peekFirst()))
		{
			args.removeFirst();
			_nocompression |= true;
		}
		
		// Get the command to use
		String command = args.pollFirst();
		if (command == null)
			command = "target";
		
		// Depends on the command
		switch (command)
		{
				// Run tests on the host
			case "host-tests":
				main("launch", "test-all");
				break;
			
				// Run tests on the interpreter
			case "interpreter-tests":
				main("interpreter-launch", "test-all");
				break;
				
				// Target a specific system
			case "target":
				// Build hairball
				main("build", "hairball");
				
				// Launch it
				recurse("launch", "hairball",
					args.<String>toArray(new String[args.size()]));
				break;
				
				// Use the interpreter too compile to the target
			case "interpreter-target":
				// Build hairball
				main("build", "hairball");
				
				// Launch it
				recurse("interpreter-launch", "hairball",
					args.<String>toArray(new String[args.size()]));
				break;
			
				// Launch a program
			case "launch":
				launch(args);
				break;
			
				// Use the interpreter to launch a program
			case "interpreter-launch":
				// Build the interpreter first
				main("build", "java-interpreter-local");
				
				// Launch it
				recurse("launch", "java-interpreter-local",
					args.<String>toArray(new String[args.size()]));
				break;
			
				// Build a project
			case "build":
				build(args.pollFirst());
				break;
			
				// Unknown
			default:
				throw new IllegalArgumentException("command");
		}
	}
	
	/**
	 * Obtains a project.
	 *
	 * @param __n The name of the project to return.
	 * @return The project by the given name.
	 * @throws IllegalArgumentException If a project
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	public static Project project(String __n)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException();
		
		// Already known?
		Project rv = projects.get(__n);
		if (rv != null)
			return rv;
		
		// Setup new project
		projects.put(__n, (rv = new Project(PROJECT_ROOT.resolve("src").
			resolve(__n))));
		return rv;
	}
	
	/**
	 * Recurse into main.
	 *
	 * @param __first First argument.
	 * @param __rest The remaining arguments.
	 * @since 2016/03/21
	 */
	public static void recurse(String __first, String... __rest)
	{
		// Slice in
		int n = __rest.length;
		String[] use = new String[1 + n];
		use[0] = __first;
		for (int i = 0; i < n; i++)
			use[1 + i] = __rest[i];
		
		// Call
		main(use);
	}
	
	/**
	 * Recurse into main.
	 *
	 * @param __first First argument.
	 * @param __second Second argument.
	 * @param __rest The remaining arguments.
	 * @since 2016/03/21
	 */
	public static void recurse(String __first, String __second,
		String... __rest)
	{
		// Slice in
		int n = __rest.length;
		String[] use = new String[2 + n];
		use[0] = __first;
		use[1] = __second;
		for (int i = 0; i < n; i++)
			use[2 + i] = __rest[i];
		
		// Call
		main(use);
	}
	
	/**
	 * Contains project details.
	 *
	 * @since 2016/03/21
	 */
	public static class Project
	{
		/** The project root directory. */
		public final Path root;
		
		/** Manifest attributes, if needed. */
		public final Attributes attr;
		
		/**
		 * Initializes the project details.
		 *
		 * @param __dir The directory where the project lies.
		 * @throws IllegalArgumentException If the project is not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/21
		 */
		public Project(Path __dir)
			throws IllegalArgumentException, NullPointerException
		{
			// Check
			if (__dir == null)
				throw new NullPointerException();
			
			// Set
			root = __dir;
			
			// Load manifest
			Manifest man;
			try (InputStream is = new FileInputStream(__dir.
				resolve("META-INF").resolve("MANIFEST.MF").toFile()))
			{
				man = new Manifest(is);
			}
			
			// Failed to read
			catch (IOException ioe)
			{
				throw new IllegalArgumentException(ioe);
			}
			
			// Get main attributes
			attr = man.getMainAttributes();
			
			throw new Error("TODO");
		}
	}
}

