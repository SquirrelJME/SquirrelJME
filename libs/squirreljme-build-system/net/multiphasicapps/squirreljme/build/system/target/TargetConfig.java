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

/**
 * This stores the configuration which is used to specify differences in how
 * the target projects are built.
 *
 * @since 2017/03/13
 */
public final class TargetConfig
{
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
	}
}

