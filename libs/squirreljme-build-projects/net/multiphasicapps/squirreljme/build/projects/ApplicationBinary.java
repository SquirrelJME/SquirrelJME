// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * This acts as the base class for application binaries which are used by
 * the user space runtime.
 *
 * @since 2016/12/17
 */
public abstract class ApplicationBinary
	extends ProjectBinary
{
	/**
	 * Initializes the application binary.
	 *
	 * @param __p The project owning this.
	 * @param __fp The path to the project's binary.
	 * @throws IOException If the specified path is not valid for a binary.
	 * @since 2016/12/17
	 */
	ApplicationBinary(Project __p, Path __fp)
		throws IOException
	{
		super(__p, __fp);
	}
}

