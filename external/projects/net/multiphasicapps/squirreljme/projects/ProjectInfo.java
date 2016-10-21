// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

/**
 * This acts as the base class for projects within the project directory.
 *
 * @since 2016/06/15
 */
public abstract class ProjectInfo
{
	/**
	 * Initializes the base for projects.
	 *
	 * @since 2016/10/20
	 */
	ProjectInfo()
	{
	}
	
	/**
	 * Returns the name of the project.
	 *
	 * @return The project name.
	 * @since 2016/10/21
	 */
	public abstract ProjectName projectName();
}

