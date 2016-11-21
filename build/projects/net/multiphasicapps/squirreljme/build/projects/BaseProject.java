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

/**
 * This represents the base for all projects which are associated with the
 * project manager.
 *
 * @since 2016/11/20
 */
public abstract class BaseProject
{
	/** The location representing the project. */
	protected final Path path;
	
	/**
	 * Initializes the base project.
	 *
	 * @param __p The path to the project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/20
	 */
	BaseProject(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.path = __p;
	}
	
	/**
	 * Returns the path to this project.
	 *
	 * @return The project path.
	 * @since 2016/11/20
	 */
	public Path path()
	{
		return this.path;
	}
}

