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

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is a project which provides source code that may be compiled by a
 * compiler into a binary project.
 *
 * @since 2016/10/20
 */
public final class SourceProject
	extends ProjectInfo
{
	/**
	 * Generates a binary manifest from this source project.
	 *
	 * This is used by the compiler. This may also be used to determine what
	 * kind of project a source project compiles into before compiling it.
	 *
	 * @return The binary manifest.
	 * @since 2016/10/20
	 */
	public BinaryProjectManifest generateBinaryManifest()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Calculates the name that a file would appear as inside of a ZIP file.
	 *
	 * @param __root The root path.
	 * @param __p The file to add.
	 * @return The ZIP compatible name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	static String __zipName(Path __root, Path __p)
		throws NullPointerException
	{
		// Check
		if (__root == null || __p == null)
			throw new NullPointerException();
		
		// Calculate relative name
		Path rel = __root.toAbsolutePath().relativize(__p.toAbsolutePath());
		
		// Build name
		StringBuilder sb = new StringBuilder();
		for (Path comp : rel)
		{
			// Prefix slash
			if (sb.length() > 0)
				sb.append('/');
			
			// Add component
			sb.append(comp);
		}
		
		// Return it
		return sb.toString();
	}
}

