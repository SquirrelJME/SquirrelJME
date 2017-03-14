// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
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
import java.util.Objects;
import net.multiphasicapps.squirreljme.build.interpreter.AutoInterpreter;
import net.multiphasicapps.squirreljme.build.projects.Project;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.projects.ProjectName;
import net.multiphasicapps.squirreljme.build.system.target.TargetConfig;
import net.multiphasicapps.squirreljme.build.system.target.TargetConfigBuilder;
import net.multiphasicapps.squirreljme.build.system.target.webdemo.
	WebDemoTarget;

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
	 * Creates and return an auto interpreter instance.
	 *
	 * @param __args Arguments to the interpreter.
	 * @return The interpreter.
	 * @throws IOException If it could not be created.
	 * @since 2016/10/29
	 */
	public AutoInterpreter autoInterpreter(String... __args)
		throws IOException
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Create
		return new AutoInterpreter(this.projects, __args);
	}
	
	/**
	 * This wraps the main entry point from a single specified string which
	 * may be used by host environments.
	 *
	 * @param __args Program arguments.
	 * @throws IllegalArgumentException If the input arguments are not
	 * correct.
	 * @since 2016/10/29
	 */
	public void main(String... __args)
		throws IllegalArgumentException
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// {@squirreljme.error AO01 No arguments specified. The following are
		// commands which are valid.
		// ({@code interpret [interpreter arguments...]}: Runs the auto
		// interpreter which is used to create simulated SquirrelJME
		// environments.;
		// {@code squirrel-quarrel}: Runs a sample real-time strategy game
		// in the build environment.;
		// {@code build [project]}: Builds the specified project.)
		//}
		int na = __args.length;
		if (na <= 0)
			throw new IllegalArgumentException("AO01");
		
		// Depends on the input command
		String command;
		switch ((command = Objects.toString(__args[0], "").trim().
			toLowerCase()))
		{
				// Build a project
			case "build":
				{
					// {@squirreljme.error AO04 The build command requires a
					// project to build.}
					if (na < 2)
						throw new IllegalArgumentException("AO04");
					
					// Get project
					ProjectManager projects = this.projects;
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
			
				// Run the auto-interpreter
			case "interpret":
			case "interpreter":
				{
					// Create subset of arguments
					String[] pargs = new String[na - 1];
					for (int i = 0, j = 1; j < na; i++, j++)
						pargs[i] = Objects.toString(__args[j], "");
					
					// Create interpreter and run it
					try (AutoInterpreter ai = autoInterpreter(pargs))
					{
						ai.run();
					}
					
					// {@squirreljme.error AO03 Read/write error while
					// interpreting.}
					catch (IOException e)
					{
						throw new RuntimeException("AO03");
					}
				}
				break;
				
				// Builds the web demo and outputs it to the file specified
				// on the command line
			case "webdemo":
				Path temp = null;
				try
				{
					// {@squirreljme.error AO06 The web demo command requires
					// a path to be specified for the output file.}
					if (na < 2)
						throw new IllegalArgumentException("AO06");
					
					// Output file
					temp = Files.createTempFile("squirreljme", "targetbuild");
					try (OutputStream os = Files.newOutputStream(temp,
						StandardOpenOption.CREATE,
						StandardOpenOption.TRUNCATE_EXISTING))
					{
						// Setup configuration
						TargetConfigBuilder tcb = new TargetConfigBuilder();
					
						// Run the build sytstem
						new WebDemoTarget(this.projects, tcb.build(), os).
							run();
					}
					
					// Built, move it out
					Files.move(temp, Paths.get(__args[2]),
						StandardCopyOption.REPLACE_EXISTING);
				}
					
				// Oops
				catch (IOException e)
				{
					// Delete temp if it exists
					if (temp != null)
						try
						{
							Files.delete(temp);
						}
						catch (IOException f)
						{
							// Ignore
							f.printStackTrace();
						}
					
					// {@squirreljme.error AO07 Failed to build the
					// web demo.}
					throw new RuntimeException("AO07", e);
				}
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

