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
 * Thie class is used to setup configurations which are used to adjust which
 * projects are included into the target along with a few other detauils.
 *
 * This class is not thread safe.
 *
 * @since 2017/03/13
 */
public class TargetConfigBuilder
{
	/**
	 * Builds the target configuration.
	 *
	 * @return The target configuration.
	 * @since 2017/03/13
	 */
	public final TargetConfig build()
	{
		return new TargetConfig(this);
	}
}

