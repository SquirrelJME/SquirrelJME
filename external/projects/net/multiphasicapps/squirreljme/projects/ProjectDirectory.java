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

import java.util.AbstractMap;

/**
 * This acts as the base class for project directories.
 *
 * @param <P> The type of projects to store.
 * @since 2016/10/25
 */
public abstract class ProjectDirectory<P extends ProjectInfo>
	extends AbstractMap<ProjectName, P>
{
	/**
	 * Initializes the directory base.
	 *
	 * @since 2016/10/25
	 */
	ProjectDirectory()
	{
	}
}

