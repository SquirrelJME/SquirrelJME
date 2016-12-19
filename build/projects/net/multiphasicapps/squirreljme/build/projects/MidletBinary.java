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
 * This is the binary representation of a midlet.
 *
 * @since 2016/12/17
 */
public final class MidletBinary
	extends ApplicationBinary
{
	/**
	 * Initializes the midlet binary.
	 *
	 * @param __p The project owning this.
	 * @param __fp The path to the project's binary.
	 * @throws IOException If the specified path is not valid for a binary.
	 * @since 2016/12/17
	 */
	MidletBinary(Project __p, Path __fp)
		throws IOException
	{
		super(__p, __fp);
	}
}

