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
	
	/**
	 * Initializes the source representation.
	 *
	 * @param __p The project owning this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/17
	 */
	ProjectBase(Project __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.project = __p;
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
}

