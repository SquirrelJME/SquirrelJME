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
 * This implements the base for namespaces for APIs and Liblets and is used
 * to handle and decode project formats.
 *
 * @since 2016/12/13
 */
abstract class __Namespace__
{
	/** The owning project manager. */
	protected final ProjectManager projectman;
	
	/**
	 * Initializes the base namespace manager.
	 *
	 * @param __pm The owning project manager.
	 * @param __p The paths containing APIs.
	 * @throws IllegalStateException If a project is malformed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/13
	 */
	@SuppressWarnings({"unchecked"})
	__Namespace__(ProjectManager __pm, Iterable<Path> __p)
		throws IllegalStateException, IOException, NullPointerException
	{
		// Check
		if (__pm == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.projectman = __pm;
		
		throw new Error("TODO");
	}
}

