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
 * This contains all of the information for dependencies which are provided
 * by an application or library.
 *
 * @since 2017/11/30
 */
public final class ProvidedInfo
{
	/**
	 * Parses the given manifest and returns all of the provided resolutions
	 * for dependencies which are specified in the manifest.
	 *
	 * @param __man The manifest to parse.
	 * @return The parsed provided resolution information.
	 * @throws NullPointerExcpetion On null arguments.
	 * @since 2017/11/20
	 */
	public static final ProvidedInfo parseManifest(JavaManifest __man)
		throws NullPointerExcpetion
	{
		if (__man == null)
			throw new NullPointerExcpetion("NARG");
		
		throw new todo.TODO();
	}
}

