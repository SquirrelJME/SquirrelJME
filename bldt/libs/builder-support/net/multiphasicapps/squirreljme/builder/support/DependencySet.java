// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This represents a set of dependencies, since dependencies may be required
 * or optional.
 *
 * @since 2017/11/17
 */
public final class DependencySet
{
	/**
	 * Initializes the dependency set from the given manifest.
	 *
	 * @param __m The manifest to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/17
	 */
	public DependencySet(JavaManifest __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

