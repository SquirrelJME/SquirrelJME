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
 * This represents the base class for MIDlets and LIBlets.
 *
 * @since 2016/11/20
 */
public abstract class ApplicationProject
	extends BaseProject
{
	/**
	 * Initializes the project information.
	 *
	 * @param __p The path to the project.
	 * @since 2016/11/20
	 */
	ApplicationProject(Path __p)
	{
		super(__p);
		
		throw new Error("TODO");
	}
}

