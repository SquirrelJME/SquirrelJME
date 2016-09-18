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

import java.util.ArrayDeque;
import java.util.Deque;
import net.multiphasicapps.squirreljme.bootstrap.base.compiler.BootCompiler;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.BootLauncher;
import net.multiphasicapps.squirreljme.projects.ProjectList;

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
				throw new Error("TODO");
				
				// Launch the given project
			case "launch":
				throw new Error("TODO");
				
				// Build the given project
			case "build":
				throw new Error("TODO");
			
				// Cross compile SquirrelJME's target binary
			case "target":
				args.removeFirst();
			default:
				throw new Error("TODO");
		}
	}
}

