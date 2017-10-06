// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.multiphasicapps.io.hexdumpstream.HexDumpOutputStream;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.projects.Project;
import net.multiphasicapps.squirreljme.build.projects.ProjectBinary;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.projects.ProjectName;
import net.multiphasicapps.squirreljme.interpreter.Interpreter;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.jit.VerifiedJITInput;
import net.multiphasicapps.squirreljme.launcher.EntryPoint;
import net.multiphasicapps.squirreljme.launcher.EntryPoints;

/**
 * This is the build system which is used to dispatch the compiler to generate
 * target binaries or to launch the interpreter.
 *
 * @since 2016/10/29
 */
public class BuildSystem
{
	/** The manager for projects. */
	protected final ProjectManager projects;
	
	/**
	 * Initializes the build system.
	 *
	 * @param __bin The binary output path.
	 * @param __src The SquirrelJME source tree root.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/29
	 */
	public BuildSystem(Path __bin, Path __src)
		throws IOException, NullPointerException
	{
		// Check
		if (__bin == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Initializes the build system
		ProjectManager projects = new ProjectManager(__bin, __src);
		this.projects = projects;
	}
	
	/**
	 * Runs the interpreter.
	 *
	 * @param __args The arguments for the interpreter.
	 * @throws IllegalArgumentException If the input arguments are not
	 * correct.
	 * @throws IOException On read/write errors.
	 * @since 2017/10/05
	 */
	public void interpret(String... __args)
		throws IllegalArgumentException, IOException
	{
		// Copy arguments
		Deque<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.offerLast(a);
		
		// Determines how the code is ran
		String interpretertype = args.removeFirst().trim().toLowerCase();
		
		// Parse an arguments
		// -Dproperty=value.
		// -Ttemplate Which set of built-in commands to use.
		// -Rid The index of the midlet in the manifest to execute based on
		// the entry point order.
		Map<String, String> properties = new LinkedHashMap<>();
		List<String> templates = new ArrayList<>();
		int entrydx = 0;
		while (!args.isEmpty())
		{
			String a = args.peekFirst();
			
			if (!a.startsWith("-"))
				break;
			
			throw new todo.TODO();
		}
		
		// {@squirreljme.error AO0e No program was specified to interpret.}
		if (args.isEmpty())
			throw new IllegalArgumentException("AO0e");
		
		// If there are no templates, just use the interpreter one
		if (templates.isEmpty())
			templates.add("interpreter");
		
		// Determine if the program to be ran is external (it is out of the
		// project space) or if it is internal
		String run = args.removeFirst();
		Path runpath = Paths.get(run);
		ProjectManager projects = this.projects;
		String bootgroup;
		if (Files.exists(runpath))
			throw new todo.TODO();
		
		// Otherwise treat as internal!
		else
		{
			bootgroup = run;
			templates.add(run + ".jar");
		}
		
		// Setup a target builder
		// Use the input templates to provide an enviroment for what is
		// included
		// If the project to be built is internal then include that in the
		// projects to build
		TargetBuilder tb = new TargetBuilder(projects,
			templates.<String>toArray(new String[templates.size()]));
		
		// Get the verified input, which will be sent to the interpreter
		// The higher level interpreters will actually perform compilation but
		// the lowest levels will not
		VerifiedJITInput vji = tb.getVerifiedInput();
		
		// Load the initial manifest
		JavaManifest man;
		try (InputStream in = vji.loadResource(bootgroup,
			"META-INF/MANIFEST.MF"))
		{
			man = new JavaManifest(in);
		}
		
		// Load entry points
		JavaManifestAttributes attr = man.getMainAttributes();
		EntryPoints eps = new EntryPoints(attr);
		
		// {@squirreljme.error AO0f The JAR to launch does not specify
		// any midlets and there is no main class.}
		if (eps.isEmpty())
			throw new RuntimeException("AO0f");
		
		// Debug print entry points
		System.err.println("Available programs:");
		for (int i = 0, n = eps.size(); i < n; i++)
			System.err.printf("%d> %s%n", i, eps.get(i));
		System.err.println();
		
		// Get the one to use
		String entrypoint = eps.get(entrydx).entryPoint();
		
		// Launching depends on the interpreter type
		Runnable runner;
		switch (interpretertype)
		{
				// Standard interpreter
			case "interpret":
				runner = new Interpreter(vji, properties, entrypoint);
				break;
			
				// {@squirreljme.error AO0a Unknown interpreter type.
				// (The interpreter type)}
			default:
				throw new IllegalArgumentException(String.format("AO0a %s",
					interpretertype));
		}
		
		// Run the VM
		runner.run();
	}
	
	/**
	 * This wraps the main entry point from a single specified string which
	 * may be used by host environments.
	 *
	 * @param __args Program arguments.
	 * @throws IllegalArgumentException If the input arguments are not
	 * correct.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/29
	 */
	public void main(String... __args)
		throws IllegalArgumentException, IOException
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// {@squirreljme.error AO01 No arguments specified. The following are
		// commands which are valid.
		// ({@code target [template]}: Loads a pre-created target template and
		// performs compilation of that target.;
		// {@code build [project]}: Builds the specified project.;
		// {@code generate-docs [target]}: Parses the source code and generates
		// documentation from it and places it within the target directory.;
		// {@code interpret (-Dproperty=value) (-Rid) (-Ttemplate) [project]}:
		// Launches the specified project and runs it in the interpreter;
		// {@code ok}: Does nothing, is used to determine if the build system
		// was able to be built.)
		// }
		int na = __args.length;
		if (na <= 0)
			throw new IllegalArgumentException("AO01");
		
		// Depends on the input command
		String command;
		ProjectManager projects = this.projects;
		switch ((command = Objects.toString(__args[0], "").trim().
			toLowerCase()))
		{
				// Ok -- Does nothing, just to see if the build system was
				// properly built
			case "ok":
				break;
			
				// Build a project
			case "build":
				{
					// {@squirreljme.error AO04 The build command requires a
					// project to build.}
					if (na < 2)
						throw new IllegalArgumentException("AO04");
					
					// Get project
					Project p = projects.get(new ProjectName(__args[1]));
					
					// {@squirreljme.error AO05 The specified project is not
					// valid. (The project name)}
					if (p == null)
						throw new IllegalArgumentException(String.format(
							"AO05 %s", __args[1]));
					
					// Just get the binary (which tries to compile it)
					p.binary();
				}
				break;
			
				// Generates all of the documentation
			case "generate-docs":
				{
					// {@squirreljme.error AO0d The documentation generator
					// requires an output directory.}
					if (na < 2)
						throw new IllegalArgumentException("AO0d");
					
					new DocumentBuilder(projects, Paths.get(__args[1])).run();
				}
				
				// Target a specific system
			case "target":
				{
					// {@squirreljme.error AO03 The target command requires a
					// template system to build.}
					if (na < 2)
						throw new IllegalArgumentException("AO03");
					
					// Fill in arguments
					List<String> templates = new ArrayList<>();
					for (int i = 1; i < na; i++)
						templates.add(__args[i]);
					
					// Setup target builder
					TargetBuilder tb = new TargetBuilder(projects,
						templates.<String>toArray(
							new String[templates.size()]));
					
					// Compile target to the given output stream
					try (OutputStream os = new HexDumpOutputStream(System.out))
					{
						tb.runTarget(os);
					}
				}
				break;
				
				// Interpreter
			case "interpret":
				interpret(__args);
				break;
				
				// {@squirreljme.error AO02 An unknown command was specified.
				// Check the description for error code AO01 to see which
				// commands are valid. (The command)}
			default:
				throw new IllegalArgumentException(String.format("AO02 %s",
					command));
		}
	}
}

