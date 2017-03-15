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

import java.io.IOException;
import java.io.OutputStream;
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
	protected abstract void accept(ExecutableClass __ec)
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
		
		throw new todo.TODO();
	}
}

