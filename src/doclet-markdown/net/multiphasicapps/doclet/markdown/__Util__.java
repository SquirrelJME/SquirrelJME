// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.doclet.markdown;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Extra utilities that may be needed.
 *
 * @since 2016/10/01
 */
class __Util__
{
	/**
	 * Converts the qualified name of a class to a path.
	 *
	 * @param __cl The class to convert to a path.
	 * @return The path for the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/01
	 */
	static Path __classToPath(MarkdownClass __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

