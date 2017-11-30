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
		/*
		
		if (__m == null)
			throw new NullPointerException("NARG");
		
		JavaManifestAttributes attr = __m.getMainAttributes();
		Set<ManifestedDependency> deps = new LinkedHashSet<>();
		
		// Normally required, configuration specifies CLDC and such
		String config = attr.getValue("microedition-configuration");
		if (config != null)
			deps.add(new APIConfiguration(config.trim()));
		
		// Normally required, this may or might not exist but normally when
		// binaries are generated any dependencies that rely on APIs will
		// be transformed to this
		String profiles = attr.getValue("microedition-profile");
		if (profiles != null)
			for (APIProfile dep : APIProfile.parseList(profiles))
				deps.add(dep);
		
		// Determine the prefix to use, for MIDlets or liblets
		String prefix = (attr.getValue("midlet-name") != null ?
			"midlet-dependency-" : "liblet-dependency-");
		
		// Parse entries in sequential order
		for (int i = 1; i >= 1; i++)
		{
			// Stop if no more values are read
			String value = attr.getValue(prefix + i);
			if (value == null)
				break;
			
			// Decode dependency
			deps.add(new MidletDependency(value));
		}
		
		// Build
		return new DependencySet(deps);
		*/
	}
}

