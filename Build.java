// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.TreeMap;
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
	
	/** Build JARs with no compression? */
	private static volatile boolean _nocompression;
	
	/** This is a cached set of known projects. */
	protected final Map<String, Project> projects =
		new HashMap<>();
	
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
	 * Initializes the builder.
	 *
	 * @since 2016/03/21
	 */
	public Build()
	{
	}
	
	/**
	 * Obtains the given project.
	 *
	 * @param __n The name of the project to get.
	 * @return The project description.
	 * @throws IllegalStateException If no project exists with that name or
	 * it is an invalid project.
	 * @since 2016/03/21
	 */
	public Project getProject(String __n)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException();
		
		// Already known?
		Project rv = projects.get(__n);
		if (rv != null)
			return rv;
		
		// Setup new project
		try
		{
			projects.put(__n, (rv = new Project(PROJECT_ROOT.resolve("src").
				resolve(__n))));
		}
		
		// Report a bad project
		catch (RuntimeException|Error e)
		{
			// Report it
			System.err.printf("Illegal Project: %s%n", __n);
			
			// Toss it
			throw e;
		}
		
		// Change names?
		if (!__n.equals(rv.name))
			throw new IllegalArgumentException(String.format(
				"Project `%s` changed name to `%s`.", __n, rv.name));
		
		// Return it
		return rv;
	}
	
	/**
	 * Invokes the build system.
	 *
	 * @param __args Argument queue.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	public void invoke(Deque<String> __args)
		throws NullPointerException
	{
		// Check
		if (__args == null)
			throw new NullPointerException();
		
		// Get the command to use
		String command = __args.pollFirst();
		if (command == null)
			command = "target";
		
		// Depends on the command
		Project pp;
		switch (command)
		{
				// Run tests on the host
			case "host-tests":
				__launch(false, getProject("test-all"));
				break;
			
				// Run tests on the interpreter
			case "interpreter-tests":
				__launch(true, getProject("test-all"));
				break;
				
				// Target a specific system
			case "target":
				// Build hairball
				__build((pp = getProject("hairball")));
				
				// Launch it
				__launch(false, pp, __args);
				break;
				
				// Use the interpreter too compile to the target
			case "interpreter-target":
				// Build hairball
				__build((pp = getProject("hairball")));
				
				// Launch it
				__launch(true, pp, __args);
				break;
			
				// Launch a program
			case "launch":
				__launch(false, getProject(__args.removeFirst()), __args);
				break;
			
				// Use the interpreter to launch a program
			case "interpreter-launch":
				__launch(true, getProject(__args.removeFirst()), __args);
				break;
			
				// Build a project
			case "build":
				__build(getProject(__args.removeFirst()));
				break;
			
				// Unknown
			default:
				throw new IllegalArgumentException("command");
		}
	}
	
	/**
	 * Builds the given project.
	 *
	 * @param __p The project to build.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	private void __build(Project __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException();
		
		throw new Error("TODO");
	}
	
	/**
	 * Launches the given project.
	 *
	 * @param __interp Run this project using the internal interpreter?
	 * @param __p The project to launch.
	 * @param __args The program arguments
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	private void __launch(boolean __interp, Project __p, String... __args)
		throws NullPointerException
	{
		__launch(__interp, __p,
			new LinkedList<>(Arrays.<String>asList(__args)));
	}
	
	/**
	 * Launches the given project.
	 *
	 * @param __interp Run this project using the internal interpreter?
	 * @param __p The project to launch.
	 * @param __args The program arguments
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	private void __launch(boolean __interp, Project __p, Deque<String> __args)
		throws NullPointerException
	{
		// Check
		if (__p == null || __args == null)
			throw new NullPointerException();
		
		// If using the interpreter, build it
		if (__interp)
			__build(getProject("java-interpreter-local"));
		
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
		
		// Setup builder
		Build b = new Build();
		
		// Send arguments to the builder
		b.invoke(args);
	}
	
	
	/**
	 * This represents a project which may be built.
	 *
	 * @since 2016/03/21
	 */
	public class Project
	{
		/** The root directory of the project. */
		protected final Path root;
		
		/** Manifest attributes. */
		protected final Attributes attr;
		
		/** Project name. */
		protected final String name;
		
		/** Dependencies. */
		protected final Set<Project> depends;
		
		/** The name of the output JAR file. */
		protected final Path jarname;
		
		/** The library title. */
		protected final String libtitle;
		
		/** The library vendor. */
		protected final String libvendor;
		
		/** The library version. */
		protected final String libversion;
		
		/**
		 * Initializes the project.
		 *
		 * @param __root The root directory of the project.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/21
		 */
		private Project(Path __root)
			throws NullPointerException
		{
			// Check
			if (__root == null)
				throw new NullPointerException();
			
			// Set
			root = __root;
			
			// Load manifest
			Manifest man;
			try (InputStream is = new FileInputStream(root.
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
			
			// Get project name
			name = Objects.<String>requireNonNull(
				attr.getValue("X-Hairball-Name"), "Missing package name.");
			
			// Optional dependencies
			Set<Project> xdeps = new HashSet<>();
			String odeps = attr.getValue("X-Hairball-Depends");
			if (odeps != null)
				for (String s : odeps.split(Pattern.quote(",")))
					xdeps.add(getProject(s.trim()));
			depends = Collections.<Project>unmodifiableSet(xdeps);
			
			// These must exist for all projects
			libtitle = Objects.<String>requireNonNull(attr.getValue(
				"LIBlet-Title"), "Missing library title.");
			libvendor = Objects.<String>requireNonNull(attr.getValue(
				"LIBlet-Vendor"), "Missing library title.");
			libversion = Objects.<String>requireNonNull(attr.getValue(
				"LIBlet-Version"), "Missing library title.");
			
			// Determine JAR name
			jarname = Paths.get(name + ".jar");
		}
	}
}

