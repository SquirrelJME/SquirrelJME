// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import java.util.ArrayList;
import java.util.List;

/**
 * Base for bundle platforms because I do not believe in being stuck to one,
 * also as well SquirrelJME is on multiple platforms as well.
 *
 * @since 2024/03/04
 */
public abstract class BundlePlatform
{
	/**
	 * Returns the bundling platforms.
	 *
	 * @return The current bundling platforms.
	 * @since 2024/03/04
	 */
	public static BundlePlatform[] instance()
	{
		List<BundlePlatform> platforms = new ArrayList<>();
		
		// CircleCi
		if (Boolean.parseBoolean(System.getenv("CIRCLECI")))
			platforms.add(new BundlePlatformCircleCi());
		
		// GitLab
		if (Boolean.parseBoolean(System.getenv("GITLAB_CI")))
			platforms.add(new BundlePlatformGitLab());
		
		// Return bundles, if any
		if (!platforms.isEmpty())
			return platforms.toArray(new BundlePlatform[platforms.size()]);
		return null;
	}
}
