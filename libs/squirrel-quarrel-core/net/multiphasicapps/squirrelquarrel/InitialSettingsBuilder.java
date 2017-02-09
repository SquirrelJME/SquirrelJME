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
 * This is used to build the initial settings for the game.
 *
 * This class is not thread safe.
 *
 * @since 2017/02/09
 */
public class InitialSettingsBuilder
{
	/**
	 * Builds the settings.
	 *
	 * @return The resulting settings.
	 * @since 2017/02/09
	 */
	public InitialSettings build()
	{
		return new InitialSettings(this);
	}
}

