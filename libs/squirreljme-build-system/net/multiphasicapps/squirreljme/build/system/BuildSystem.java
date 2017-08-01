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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.multiphasicapps.io.hexdumpstream.HexDumpOutputStream;
import net.multiphasicapps.squirreljme.build.projects.Project;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.projects.ProjectName;

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
		// {@code build [project]}: Builds the specified project.)
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
						tb.run(os);
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

