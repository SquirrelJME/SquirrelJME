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

import java.io.IOException;
import java.nio.file.Path;

/**
 * This class is used to manage projects which represent modules that
 * SquirrelJME contains. Each module could be part of the build system, an API
 * which is implemented by SquirrelJME, or a library/midlet which may be
 * included in a target environment.
 *
 * @since 2016/10/28
 */
public class ProjectManager
{
	/**
	 * Initializes the project manager.
	 *
	 * @param __bin The binary path.
	 * @param __src The root of the SquirrelJME tree.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/28
	 */
	public ProjectManager(Path __bin, Path __src)
		throws IOException, NullPointerException
	{
		// Check
		if (__bin == null || __src == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

