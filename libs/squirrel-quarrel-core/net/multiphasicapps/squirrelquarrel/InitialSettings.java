// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel;

/**
 * This contains the initial settings which is used to determine how to
 * generate and initialize the initial game.
 *
 * @since 2017/02/09
 */
public final class InitialSettings
{
	/**
	 * Initializes the initial settings.
	 *
	 * @param __b The initial settings to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/09
	 */
	InitialSettings(InitialSettingsBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
	}
}

