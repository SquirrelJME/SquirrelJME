// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.autointerpreter;

import net.multiphasicapps.squirreljme.projects.ProjectGroup;

/**
 * This is a namespace which uses a project group.
 *
 * @since 2016/10/11
 */
public class GroupNamespace
	implements RuntimeNamespace
{
	/** The group to refer to binary packages. */
	protected final ProjectGroup group;
	
	/**
	 * initializes the group namespace.
	 *
	 * @param __g The group to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/11
	 */
	public GroupNamespace(ProjectGroup __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.group = __g;
	}
}

