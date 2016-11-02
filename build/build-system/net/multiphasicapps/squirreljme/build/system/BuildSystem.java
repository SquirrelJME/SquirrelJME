// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import net.multiphasicapps.squirreljme.build.interpreter.AutoInterpreter;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;

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
		// ({@code interpret (interpreter arguments...)}: Runs the auto
		// interpreter which is used to create simulated SquirrelJME
		// environments.)
		//}
		int na = __args.length;
		if (na <= 0)
			throw new IllegalArgumentException("AO01");
		
		// Depends on the input command
		String command;
		switch ((command = Objects.toString(__args[0], "").trim().
			toLowerCase()))
		{
				// Run the auto-interpreter
			case "interpret":
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
			
				// {@squirreljme.error AO02 An unknown command was specified.
				// Check the description for error code AO01 to see which
				// commands are valid. (The command)}
			default:
				throw new IllegalArgumentException(String.format("AO02 %s",
					command));
		}
	}
}

