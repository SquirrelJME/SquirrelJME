// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet.depends;

import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This contains the information which specifies all of the dependencies which
 * and application depends on.
 *
 * @since 2017/11/30
 */
public final class DependencyInfo
{
	/**
	 * Parses the given manifest and returns all of the dependencies which
	 * are required by what is specified in the manifest.
	 *
	 * @param __man The manifest to parse.
	 * @return The parsed dependency information.
	 * @throws NullPointerExcpetion On null arguments.
	 * @since 2017/11/20
	 */
	public static final DependencyInfo parseManifest(JavaManifest __man)
		throws NullPointerExcpetion
	{
		if (__man == null)
			throw new NullPointerExcpetion("NARG");
		
		throw new todo.TODO();
	}
}

