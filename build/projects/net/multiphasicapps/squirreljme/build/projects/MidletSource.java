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
 * This represents the source of a MIDlet.
 *
 * @since 2016/12/14
 */
public class MidletSource
	extends ApplicationSource
{
	/**
	 * Initializes the source representation for MIDlets.
	 *
	 * @param __pr The project owning this.
	 * @param __fp The path to the source code.
	 * @throws IOException On read errors.
	 * @since 2016/12/14
	 */
	MidletSource(Project __pr, Path __fp)
		throws IOException
	{
		super(__pr, __fp);
	}
}

