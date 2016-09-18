// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap;

import java.nio.file.attribute.FileTime;
import java.util.ArrayDeque;
import java.util.Deque;
import net.multiphasicapps.squirreljme.bootstrap.base.compiler.BootCompiler;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.BootLauncher;
import net.multiphasicapps.squirreljme.projects.ProjectGroup;
import net.multiphasicapps.squirreljme.projects.ProjectInfo;
import net.multiphasicapps.squirreljme.projects.ProjectList;
import net.multiphasicapps.squirreljme.projects.ProjectName;

/**
 * This is the bootstrapper which is used to build and potentially launch
 * projects such as the SquirrelJME target builder.
 *
 * @since 2016/09/18
 */
public class Bootstrapper
{
	/** The project list to use. */
	protected final ProjectList projects;
	
	/** The bootstrap compiler. */
	protected final BootCompiler compiler;
	
	/** The bootstrap launcher. */
	protected final BootLauncher launcher;
	
	/**
	 * Initializes the bootstrapper.
	 *
	 * @param __pl The package list.
	 * @param __bc The compier for projects, optional.
	 * @param __bl The launcher for projects, optional.
	 * @throws NullPointerException If no project list was specified.
	 * @since 2016/09/18
	 */
	public Bootstrapper(ProjectList __pl, BootCompiler __bc, BootLauncher __bl)
		throws NullPointerException
	{
		// Check
		if (__pl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.projects = __pl;
		this.compiler = __bc;
		this.launcher = __bl;
		
		// {@squirreljme.error CL01 No bootstrap compiler was specified,
		// projects cannot be built.}
		if (__bc == null)
			System.err.println("CL01");
		
		// {@squirreljme.error CL02 No bootstrap launcher was specified,
		// projects cannot be launched.}
		if (__bl == null)
			System.err.println("CL02");
	}
	
	/**
	 * Runs the specified set of input commands.
	 *
	 * @param __args The bootstrap arguments to run.
	 * @since 2016/09/18
	 */
	public void run(String... __args)
	{
		// Force to exist, default to target compilation when nothing has
		// be specified.
		if (__args == null || __args.length <= 0)
			__args = new String[]{"target"};
		
		// Add arguments to queue
		Deque<String> args = new ArrayDeque<>();
		for (String a : __args)
			args.offerLast(a);
		
		// Depends
		switch (args.peekFirst())
		{
				// Run tests on the host
			case "tests":
				args.removeFirst();
				args.offerFirst("test-all");
				
				// Launch the given project
			case "launch":
				args.removeFirst();
				__launch(__getBinary(new ProjectName(args.removeFirst())),
					args.<String>toArray(new String[args.size()]));
				return;
				
				// Build the given project(s)
			case "build":
				args.removeFirst();
				while (!args.isEmpty())
					__getBinary(new ProjectName(args.remove()));
				return;
			
				// Cross compile SquirrelJME's target binary
			case "target":
				args.removeFirst();
			default:
				__launch(__getBinary(new ProjectName("builder-all")),
					args.<String>toArray(new String[args.size()]));
				return;
		}
	}
	
	/**
	 * Returns the binary for the given project, building dependencies if they
	 * are missing.
	 *
	 * @param __n The binary to get and potentially build.
	 * @return The binary for the given project.
	 * @throws RuntimeException If the binary project could not be obtained
	 * or built.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	ProjectInfo __getBinary(ProjectName __n)
		throws RuntimeException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CL04 The specified project does not exist.
		// (The name of the project)}
		ProjectList projects = this.projects;
		ProjectGroup group = projects.get(__n);
		if (group == null)
			throw new RuntimeException(String.format("CL04 %s", __n));
		
		// Try to get the binary for it
		ProjectInfo rv = group.binary();
		if (rv != null)
		{
			// If there is no source package for the binary then use it
			// regardless
			ProjectInfo src = group.source();
			if (src == null)
				return rv;
			
			// If the binary is up to date, make sure the dependencies are
			// also up to date
			FileTime btime;
			if ((btime = rv.date()).compareTo(src.date()) >= 0)
			{
				// Recursively get the dependencies for the project
				boolean outofdate = false;
				for (ProjectName depn : rv.dependencies())
				{
					// {@squirreljme.error CL05 A dependent project does not
					// have an associated group, there are no binaries or
					// sources for it. (This project; The dependency)}
					ProjectGroup depg = projects.get(depn);
					if (depg == null)
						throw new RuntimeException(String.format("CL05 %s %s",
							__n, depn));
					
					// Get the binary for that dependency
					ProjectInfo depi = __getBinary(depn);
					
					// Is a dependency newer than the binary?
					if (depi.date().compareTo(btime) > 0)
						outofdate |= true;
				}
			
				// If not out of date then use it
				if (!outofdate)
					return rv;
			}
		}
		
		// It must be compiled
		System.err.printf("*** Building %s...%n", __n);
		
		throw new Error("TODO");
	}
	
	/**
	 * Launches the specified binary with the given set of arguments.
	 *
	 * @param __bin The binary to launch.
	 * @param __args The arguments to the call.
	 * @throws NullPointerException If no binary was specified.
	 * @since 2016/09/18
	 */
	void __launch(ProjectInfo __bin, String... __args)
		throws NullPointerException
	{
		// Check
		if (__bin == null)
			throw new NullPointerException("NARG");
		
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		throw new Error("TODO");
	}
}

