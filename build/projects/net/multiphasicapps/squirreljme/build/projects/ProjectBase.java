// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.nio.file.Path;
import java.util.Set;

/**
 * This acts as the common base for binary and source projects.
 *
 * @since 2016/12/17
 */
public abstract class ProjectBase
{
	/** The owning project. */
	protected final Project project;
	
	/** The path to the source or binary. */
	protected final Path path;
	
	/**
	 * Initializes the source representation.
	 *
	 * @param __p The project owning this.
	 * @param __fp The path to the file.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/17
	 */
	ProjectBase(Project __p, Path __fp)
		throws NullPointerException
	{
		// Check
		if (__p == null || __fp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.project = __p;
		this.path = __fp;
	}
		
	/**
	 * Obtains the binary projects which this binary project depends on.
	 *
	 * @param __rec If {@code true} then binaries are recursively obtained.
	 * @return The binary dependencies.
	 * @since 2016/12/17
	 */
	public final Set<ProjectBinary> binaryDependencies(boolean __rec)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the path to the file.
	 *
	 * @return The file path.
	 * @since 2016/12/17
	 */
	public final Path path()
	{
		return this.path;
	}
	
	/**
	 * Recursively determines the time and date of the project base.
	 *
	 * @return The time that the binary or source code last changed in
	 * milliseconds.
	 * @since 2016/12/17
	 */
	public final long time()
	{
		throw new Error("TODO");
	}
}

