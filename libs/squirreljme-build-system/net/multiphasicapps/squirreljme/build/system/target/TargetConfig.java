// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system.target;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This stores the configuration which is used to specify differences in how
 * the target projects are built.
 *
 * @since 2017/03/13
 */
public final class TargetConfig
{
	/** Groups to build. */
	private final Set<String> _groups =
		new LinkedHashSet<>();
	
	/**
	 * Initializes the target configuration.
	 *
	 * @param __b The builder for configurations.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/13
	 */
	TargetConfig(TargetConfigBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Copy
		this._groups.addAll(__b._groups);
	}
	
	/**
	 * Checks whether the target configuration includes the given group.
	 *
	 * @param __g The group to check for inclusion.
	 * @return If the group is to be included.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/15
	 */
	public boolean hasGroup(String __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Lowercase because all groups are lowercase
		__g = TargetConfigBuilder.__lowercase(__g);
		
		// Go through groups
		for (String s : this._groups)
			if (s.equals(__g))
				return true;
		
		// Not found
		return false;
	}
}

