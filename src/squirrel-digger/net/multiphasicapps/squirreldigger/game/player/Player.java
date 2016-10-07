// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreldigger.game.player;

import net.multiphasicapps.squirreldigger.game.Game;

/**
 * This class represents the player that exists in a game which controls the
 * entity that it is assigned to. A player has an inventory and is controlled
 * by a player (or AI).
 *
 * @since 2016/10/06
 */
public class Player
{
	/** The game this player is in. */
	protected final Game game;
	
	/** The current controller that controls the player. */
	private volatile Controller _controller;
	
	/**
	 * Initializes the player that exists in the game.
	 *
	 * @param __g The game to set the player in.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/06
	 */
	public Player(Game __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __g;
	}
	
	/**
	 * Sets the controller to use for the player.
	 *
	 * @param __c The controller to use, {@code null} clears it (and makes the
	 * player idle).
	 * @since 2016/10/06
	 */
	public void setController(Controller __c)
	{
		// Lock
		synchronized (this.game)
		{
			this._controller = __c;
		}
	}
}

