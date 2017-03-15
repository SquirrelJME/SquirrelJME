// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system.target;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.projects.Project;
import net.multiphasicapps.squirreljme.build.projects.ProjectBinary;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;
import net.multiphasicapps.squirreljme.jit.TranslationEngineProvider;

/**
 * This is the base class which is implemented for any target output handler.
 *
 * @since 2017/03/13
 */
public abstract class AbstractTarget
{
	/** The manager for projects. */
	protected final ProjectManager projects;
	
	/** The target configuration. */
	protected final TargetConfig config;
	
	/** Binary projects which were accepted. */
	private final Set<ProjectBinary> _accepted =
		new HashSet<>();
	
	/**
	 * Initializes the base target.
	 *
	 * @param __pm The projects available for usage.
	 * @param __conf The configuration to use during build.
	 * @param __os The stream where the output target is to be placed.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/13
	 */
	public AbstractTarget(ProjectManager __pm, TargetConfig __conf,
		OutputStream __os)
		throws IOException, NullPointerException
	{
		// Check
		if (__pm == null || __conf == null || __os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.projects = __pm;
		this.config = __conf;
	}
	
	/**
	 * This is called when the compilation step generates an executable from
	 * the compiled code.
	 *
	 * @param __ec The executable that was just compiled.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/15
	 */
	protected abstract void acceptClass(ExecutableClass __ec)
		throws IOException, NullPointerException;
	
	/**
	 * This is called when a resource should be written the specified output
	 * stream.
	 *
	 * @param __n The name of the resource.
	 * @param __is The input stream to the resource data.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/15
	 */
	protected abstract void acceptResource(String __n, InputStream __is)
		throws IOException, NullPointerException;
	
	/**
	 * Runs the target generator.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2017/03/14
	 */
	public abstract void run()
		throws IOException;
	
	/**
	 * Performs the compilation step by running through every project to be
	 * included and compiles it.
	 *
	 * @param __tep The engine provider.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/15
	 */
	protected final void compile(TranslationEngineProvider __tep)
		throws IOException, NullPointerException
	{
		// Check
		if (__tep == null)
			throw new NullPointerException("NARG");
		
		// Go through projects and compile ones to be included
		ProjectManager projects = this.projects;
		TargetConfig config = this.config;
		for (Project mp : projects)
		{
			// Check if this project is in the given group
			boolean was = false;
			for (String g : mp.groups())
				was |= config.hasGroup(g);
			if (!was)
				continue;
			
			// Compile the target and additionally any of its dependecies
			ProjectBinary mb = mp.binary();
			for (ProjectBinary db : mb.binaryDependencies(true))
				__accept(db);
			
			// Accept the primary binary
			__accept(mb);
		}
	}
	
	/**
	 * Compiles the specified binary to native code and accepts it
	 *
	 * @param __pb The binary to compile then accept.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/15
	 */
	private final void __accept(ProjectBinary __pb)
		throws IOException, NullPointerException
	{
		// Check
		if (__pb == null)
			throw new NullPointerException("NARG");
		
		// Only accept once, otherwise binaries will be bloated with a few
		// dozen copies of the same code
		Set<ProjectBinary> accepted = this._accepted;
		if (accepted.contains(__pb))
			return;
		
		// Process all files in the JAR
		try (FileDirectory fd = __pb.openFileDirectory())
		{
			for (String fn : fd)
				try (InputStream is = fd.open(fn))
				{
					// Compile Java class file
					if (fn.endsWith(".class"))
					{
						throw new todo.TODO();
					}
				
					// Add resource
					else
						acceptResource(fn, is);
				}
		}
	}
}

