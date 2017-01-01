// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.gui;

import net.multiphasicapps.squirrelscavenger.game.Game;
import net.multiphasicapps.squirrelscavenger.game.player.Controller;

/**
 * This is the base class for GUI interfaces.
 *
 * @since 2016/10/06
 */
public interface GUI
{
	/**
	 * Returns the controller for the local game.
	 *
	 * @param __id The local ID of the player.
	 * @return The local game controller or {@code null} if it is not valid
	 * for the given player ID.
	 * @throws IndexOutOfBoundsException If the player ID is negative.
	 * @since 2016/10/06
	 */
	public abstract Controller localController(int __id)
		throws IndexOutOfBoundsException;
	
	/**
	 * Renders the specified game.
	 *
	 * @param __g The game to render.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/10
	 */
	public abstract void renderGame(Game __g)
		throws NullPointerException;
}

